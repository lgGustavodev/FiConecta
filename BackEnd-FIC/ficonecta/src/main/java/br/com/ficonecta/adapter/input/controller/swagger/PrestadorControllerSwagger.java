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

@Tag(name = "Prestadores", description = "Cadastro, perfil e painel de prestadores de serviço.")
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
}
