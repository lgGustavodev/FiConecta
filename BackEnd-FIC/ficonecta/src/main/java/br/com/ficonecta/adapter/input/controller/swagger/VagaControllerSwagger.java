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
import br.com.ficonecta.application.dto.response.PaginaResponse;
import br.com.ficonecta.domain.model.enums.StatusVaga;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "Vagas", description = "Cadastro e listagem de vagas pelas empresas.")
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


    @Operation(summary = "Lista as vagas da empresa autenticada",
            description = "Retorna uma página das vagas da própria empresa, das mais recentes para as mais antigas. "
                    + "O filtro por status é opcional. O tamanho da página é limitado a 50.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de vagas",
                    content = @Content(schema = @Schema(implementation = PaginaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parâmetro inválido (ex.: status inexistente)"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui o perfil EMPRESA"),
            @ApiResponse(responseCode = "404", description = "Perfil da empresa não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    PaginaResponse<VagaResponse> listar(
            @Parameter(description = "Filtra pelo status da vaga (opcional)") StatusVaga status,
            @Parameter(description = "Número da página, começando em 0") int page,
            @Parameter(description = "Itens por página (padrão 20, máximo 50)") int size);
}
