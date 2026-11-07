package br.com.ficonecta.adapter.input.controller.swagger;

import br.com.ficonecta.application.dto.request.CadastroPrestadorRequest;
import br.com.ficonecta.application.dto.response.PrestadorDashboardResponse;
import br.com.ficonecta.application.dto.response.PrestadorResponse;
import br.com.ficonecta.infrastructure.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import br.com.ficonecta.application.dto.response.PaginaResponse;
import br.com.ficonecta.application.dto.response.VagaDisponivelResponse;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "Prestadores", description = "Cadastro, perfil, painel e vagas disponíveis para prestadores de serviço.")
public interface PrestadorControllerSwagger {

    @Operation(summary = "Cadastra um prestador", description = "Cria um prestador e sua conta de acesso. Este endpoint é público.")
    @RequestBody(required = true, description = "Dados do prestador a cadastrar",
            content = @Content(schema = @Schema(implementation = CadastroPrestadorRequest.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Prestador cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = PrestadorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "E-mail ou CPF já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<PrestadorResponse> cadastrarPrestador(CadastroPrestadorRequest cadastroPrestadorRequest);

    @Operation(summary = "Consulta o perfil do prestador autenticado")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil encontrado",
                    content = @Content(schema = @Schema(implementation = PrestadorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui o perfil PRESTADOR"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    PrestadorResponse meuPerfil();

    @Operation(summary = "Consulta o dashboard do prestador autenticado")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dashboard encontrado",
                    content = @Content(schema = @Schema(implementation = PrestadorDashboardResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui o perfil PRESTADOR")
    })

    PrestadorDashboardResponse dashboard();

    @Operation(summary = "Lista as vagas disponíveis para o prestador",
            description = "Retorna uma página de vagas abertas, de todas as empresas, com data de hoje em diante, "
                    + "da data mais próxima para a mais distante. O tamanho da página é limitado a 50.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de vagas disponíveis",
                    content = @Content(schema = @Schema(implementation = PaginaResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui o perfil PRESTADOR")
    })
    PaginaResponse<VagaDisponivelResponse> listarVagasDisponiveis(
            @Parameter(description = "Número da página, começando em 0") int page,
            @Parameter(description = "Itens por página (padrão 20, máximo 50)") int size);
}
