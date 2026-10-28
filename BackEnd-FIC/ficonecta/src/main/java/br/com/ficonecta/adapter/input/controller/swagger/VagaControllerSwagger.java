package br.com.ficonecta.adapter.input.controller.swagger;

import br.com.ficonecta.application.dto.request.CadastroVagaRequest;
import br.com.ficonecta.application.dto.response.VagaResponse;
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

@Tag(name = "Vagas", description = "Criação de vagas pelas empresas.")
public interface VagaControllerSwagger {

    @Operation(summary = "Cadastra uma vaga", description = "Cria uma vaga associada à empresa autenticada.")
    @SecurityRequirement(name = "bearerAuth")
    @RequestBody(required = true, description = "Dados da vaga a cadastrar",
            content = @Content(schema = @Schema(implementation = CadastroVagaRequest.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vaga cadastrada com sucesso",
                    content = @Content(schema = @Schema(implementation = VagaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui o perfil EMPRESA")
    })
    ResponseEntity<VagaResponse> cadastrar(CadastroVagaRequest request);
}
