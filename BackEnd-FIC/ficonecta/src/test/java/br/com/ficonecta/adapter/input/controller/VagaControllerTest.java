package br.com.ficonecta.adapter.input.controller;


import br.com.ficonecta.application.dto.request.CadastroVagaRequest;
import br.com.ficonecta.application.dto.response.PaginaResponse;
import br.com.ficonecta.application.dto.response.VagaResponse;
import br.com.ficonecta.application.service.CadastroVagaService;
import br.com.ficonecta.application.service.CancelamentoVagaService;
import br.com.ficonecta.application.service.EdicaoVagaService;
import br.com.ficonecta.application.service.EmpresaVagaService;
import br.com.ficonecta.domain.model.enums.StatusVaga;
import br.com.ficonecta.infrastructure.security.AutenticacaoUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VagaControllerTest {

    @Mock
    private CadastroVagaService cadastroVagaService;

    @Mock
    private AutenticacaoUtil autenticacaoUtil;

    @Mock
    private EmpresaVagaService empresaVagaService;

    @Mock
    private CancelamentoVagaService cancelamentoVagaService;

    @Mock
    private EdicaoVagaService edicaoVagaService;

    @InjectMocks
    private VagaController vagaController;


    @Test
    void deveCadastrarVagaComSucesso() {

        // Arrange
        UUID usuarioId = UUID.randomUUID();

        CadastroVagaRequest request = mock(CadastroVagaRequest.class);

        VagaResponse response = mock(VagaResponse.class);

        when(autenticacaoUtil.getUsuarioIdAutenticado())
                .thenReturn(usuarioId);

        when(cadastroVagaService.cadastrar(usuarioId, request))
                .thenReturn(response);


        // Act
        ResponseEntity<VagaResponse> resultado =
                vagaController.cadastrar(request);


        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertSame(response, resultado.getBody());

        verify(autenticacaoUtil).getUsuarioIdAutenticado();

        verify(cadastroVagaService)
                .cadastrar(usuarioId, request);
    }


    @Test
    void deveCancelarVagaComSucesso() {

        // Arrange
        UUID usuarioId = UUID.randomUUID();
        UUID vagaId = UUID.randomUUID();

        VagaResponse response = mock(VagaResponse.class);

        when(autenticacaoUtil.getUsuarioIdAutenticado())
                .thenReturn(usuarioId);

        when(cancelamentoVagaService.cancelar(usuarioId, vagaId))
                .thenReturn(response);


        // Act
        VagaResponse resultado = vagaController.cancelar(vagaId);


        // Assert
        assertNotNull(resultado);
        assertSame(response, resultado);

        verify(autenticacaoUtil).getUsuarioIdAutenticado();

        verify(cancelamentoVagaService)
                .cancelar(usuarioId, vagaId);
    }


    @Test
    @SuppressWarnings("unchecked")
    void deveListarVagasComSucesso() {

        // Arrange
        UUID usuarioId = UUID.randomUUID();

        StatusVaga status = StatusVaga.ABERTA;
        int page = 0;
        int size = 20;

        PaginaResponse<VagaResponse> paginaResponse =
                mock(PaginaResponse.class);

        when(autenticacaoUtil.getUsuarioIdAutenticado())
                .thenReturn(usuarioId);

        when(empresaVagaService.listar(usuarioId, status, page, size))
                .thenReturn(paginaResponse);


        // Act
        PaginaResponse<VagaResponse> resultado =
                vagaController.listar(status, page, size);


        // Assert
        assertNotNull(resultado);
        assertSame(paginaResponse, resultado);

        verify(autenticacaoUtil).getUsuarioIdAutenticado();

        verify(empresaVagaService)
                .listar(usuarioId, status, page, size);
    }


    @Test
    void deveEditarVagaComSucesso() {

        // Arrange
        UUID usuarioId = UUID.randomUUID();
        UUID vagaId = UUID.randomUUID();

        CadastroVagaRequest request = mock(CadastroVagaRequest.class);

        VagaResponse response = mock(VagaResponse.class);

        when(autenticacaoUtil.getUsuarioIdAutenticado())
                .thenReturn(usuarioId);

        when(edicaoVagaService.editarVaga(usuarioId, vagaId, request))
                .thenReturn(response);


        // Act
        VagaResponse resultado =
                vagaController.editarVaga(vagaId, request);


        // Assert
        assertNotNull(resultado);
        assertSame(response, resultado);

        verify(autenticacaoUtil).getUsuarioIdAutenticado();

        verify(edicaoVagaService)
                .editarVaga(usuarioId, vagaId, request);
    }
}