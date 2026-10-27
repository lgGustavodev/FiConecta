package br.com.ficonecta.infrastructure;

import br.com.ficonecta.adapter.input.controller.EmpresaController;
import br.com.ficonecta.adapter.input.controller.PrestadorController;
import br.com.ficonecta.application.dto.request.CadastroEmpresaRequest;
import br.com.ficonecta.application.dto.request.CadastroPrestadorRequest;
import br.com.ficonecta.application.dto.response.EmpresaDashboardResponse;
import br.com.ficonecta.application.dto.response.EmpresaResponse;
import br.com.ficonecta.application.dto.response.PrestadorDashboardResponse;
import br.com.ficonecta.application.dto.response.PrestadorResponse;
import br.com.ficonecta.application.dto.response.ResumoVagasResponse;
import br.com.ficonecta.application.service.CadastroEmpresaService;
import br.com.ficonecta.application.service.CadastroPrestadorService;
import br.com.ficonecta.application.service.EmpresaDashboardService;
import br.com.ficonecta.application.service.EmpresaPerfilService;
import br.com.ficonecta.application.service.PrestadorDashboardService;
import br.com.ficonecta.application.service.PrestadorPerfilService;
import br.com.ficonecta.domain.model.enums.TipoUsuario;
import br.com.ficonecta.infrastructure.security.AutenticacaoUtil;
import br.com.ficonecta.infrastructure.security.JwtService;
import br.com.ficonecta.infrastructure.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({PrestadorController.class, EmpresaController.class})
@Import({SecurityConfig.class, JwtService.class, AutenticacaoUtil.class})
class SegurancaEndpointsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Value("${app.jwt.secret}")
    private String secret;

    // Os controllers exigem estes beans. Viram mocks: aqui não testamos regra de negócio.
    @MockitoBean private CadastroPrestadorService cadastroPrestadorService;
    @MockitoBean private CadastroEmpresaService cadastroEmpresaService;
    @MockitoBean private PrestadorPerfilService prestadorPerfilService;
    @MockitoBean private EmpresaPerfilService empresaPerfilService;
    @MockitoBean private PrestadorDashboardService prestadorDashboardService;
    @MockitoBean private EmpresaDashboardService empresaDashboardService;

    // ---------------------------------------------------------------
    // 1. CAMINHO FELIZ: o tipo certo, com token válido, entra
    // ---------------------------------------------------------------

    @Test
    void empresaComTokenValidoAcessaODashboardDaEmpresa() throws Exception {

        // Arrange
        UUID usuarioId = UUID.randomUUID();

        EmpresaDashboardResponse resposta = EmpresaDashboardResponse.builder()
                .perfil(EmpresaResponse.builder()
                        .id(UUID.randomUUID())
                        .nome("Supermercado X")
                        .build())
                .resumoVagas(ResumoVagasResponse.builder().abertas(3).total(3).build())
                .build();

        when(empresaDashboardService.buscarDashboard(usuarioId)).thenReturn(resposta);

        // Act & Assert
        mockMvc.perform(get("/empresas/dashboard")
                        .header("Authorization", bearer(usuarioId, TipoUsuario.EMPRESA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.perfil.nome").value("Supermercado X"))
                .andExpect(jsonPath("$.resumoVagas.abertas").value(3));

        verify(empresaDashboardService).buscarDashboard(usuarioId);
    }

    @Test
    void prestadorComTokenValidoAcessaODashboardDoPrestador() throws Exception {

        // Arrange
        UUID usuarioId = UUID.randomUUID();

        PrestadorDashboardResponse resposta = PrestadorDashboardResponse.builder()
                .perfil(PrestadorResponse.builder()
                        .id(UUID.randomUUID())
                        .nome("João Silva")
                        .build())
                .vagasAbertas(7)
                .build();

        when(prestadorDashboardService.buscarDashboard(usuarioId)).thenReturn(resposta);

        // Act & Assert
        mockMvc.perform(get("/prestadores/dashboard")
                        .header("Authorization", bearer(usuarioId, TipoUsuario.PRESTADOR)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.perfil.nome").value("João Silva"))
                .andExpect(jsonPath("$.vagasAbertas").value(7));

        verify(prestadorDashboardService).buscarDashboard(usuarioId);
    }

    // ---------------------------------------------------------------
    // 2. AUTORIZAÇÃO: o tipo errado é barrado
    // ---------------------------------------------------------------

    @ParameterizedTest(name = "{0} não acessa {1}")
    @CsvSource({
            "PRESTADOR, /empresas/dashboard",
            "PRESTADOR, /empresas/me",
            "EMPRESA,   /prestadores/dashboard",
            "EMPRESA,   /prestadores/me"
    })
    void tipoErradoRecebe403(TipoUsuario tipo, String rota) throws Exception {

        mockMvc.perform(get(rota)
                        .header("Authorization", bearer(UUID.randomUUID(), tipo)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(empresaDashboardService, prestadorDashboardService,
                empresaPerfilService, prestadorPerfilService);
    }

    // ---------------------------------------------------------------
    // 3. AUTENTICAÇÃO: sem token não entra
    // ---------------------------------------------------------------

    @ParameterizedTest(name = "sem token em {0}")
    @ValueSource(strings = {
            "/empresas/dashboard",
            "/empresas/me",
            "/prestadores/dashboard",
            "/prestadores/me"
    })
    void semTokenRecebe403(String rota) throws Exception {

        mockMvc.perform(get(rota))
                .andExpect(status().isForbidden());

        verifyNoInteractions(empresaDashboardService, prestadorDashboardService,
                empresaPerfilService, prestadorPerfilService);
    }

    // ---------------------------------------------------------------
    // 4. AUTENTICAÇÃO: tokens defeituosos são rejeitados
    // ---------------------------------------------------------------

    @Test
    void tokenAdulteradoRecebe403() throws Exception {

        // Arrange: token válido com o payload alterado (a assinatura não bate mais)
        String token = jwtService.gerarToken(UUID.randomUUID(), TipoUsuario.EMPRESA);
        String[] partes = token.split("\\.");
        String payloadAlterado = (partes[1].charAt(0) == 'A' ? "B" : "A") + partes[1].substring(1);
        String adulterado = partes[0] + "." + payloadAlterado + "." + partes[2];

        // Act & Assert
        mockMvc.perform(get("/empresas/dashboard")
                        .header("Authorization", "Bearer " + adulterado))
                .andExpect(status().isForbidden());

        verifyNoInteractions(empresaDashboardService);
    }

    @Test
    void tokenExpiradoRecebe403() throws Exception {

        // Arrange: serviço com validade negativa gera um token que já nasce expirado
        JwtService jwtServiceExpirado = new JwtService(secret, -1000L);
        String token = jwtServiceExpirado.gerarToken(UUID.randomUUID(), TipoUsuario.EMPRESA);

        // Act & Assert
        mockMvc.perform(get("/empresas/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());

        verifyNoInteractions(empresaDashboardService);
    }

    @Test
    void tokenAssinadoComOutraChaveRecebe403() throws Exception {

        // Arrange: token perfeito, mas assinado por quem não conhece o segredo da aplicação
        JwtService jwtServiceDeOutraChave =
                new JwtService("uma-outra-chave-secreta-com-mais-de-32-caracteres", 60000L);
        String token = jwtServiceDeOutraChave.gerarToken(UUID.randomUUID(), TipoUsuario.EMPRESA);

        // Act & Assert
        mockMvc.perform(get("/empresas/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());

        verifyNoInteractions(empresaDashboardService);
    }

    // ---------------------------------------------------------------
    // 5. ROTAS PÚBLICAS: o cadastro continua aberto
    // ---------------------------------------------------------------

    @Test
    void cadastroDePrestadorNaoExigeToken() throws Exception {

        when(cadastroPrestadorService.cadastrar(any(CadastroPrestadorRequest.class)))
                .thenReturn(PrestadorResponse.builder().id(UUID.randomUUID()).nome("João Silva").build());

        String json = """
                { "nome": "João Silva", "email": "joao@email.com", "senha": "123456", "cpf": "12345678900" }
                """;

        mockMvc.perform(post("/prestadores/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void cadastroDeEmpresaNaoExigeToken() throws Exception {

        when(cadastroEmpresaService.cadastrar(any(CadastroEmpresaRequest.class)))
                .thenReturn(EmpresaResponse.builder().id(UUID.randomUUID()).nome("Supermercado X").build());

        String json = """
                { "nome": "Supermercado X", "email": "contato@x.com", "senha": "123456",
                  "tipoDocumento": "CNPJ", "documento": "12345678000199" }
                """;

        mockMvc.perform(post("/empresas/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    // ---------------------------------------------------------------

    private String bearer(UUID usuarioId, TipoUsuario tipo) {
        return "Bearer " + jwtService.gerarToken(usuarioId, tipo);
    }
}
