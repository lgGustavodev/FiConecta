package br.com.ficonecta.adapter.input.controller.swagger;

import br.com.ficonecta.application.dto.request.CadastroEmpresaRequest;
import br.com.ficonecta.application.dto.response.EmpresaDashboardResponse;
import br.com.ficonecta.application.dto.response.EmpresaResponse;
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

@Tag(name = "Empresas", description = "Cadastro, perfil e painel de empresas.")
public interface EmpresaControllerSwagger {

    @Operation(
            summary = "Cadastra uma empresa",
            description = "Cria uma empresa e sua conta de acesso. Este endpoint é público."
    )
    @RequestBody(
            required = true,
            description = "Dados da empresa a cadastrar",
            content = @Content(schema = @Schema(implementation = CadastroEmpresaRequest.class))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Empresa cadastrada com sucesso",
                    content = @Content(schema = @Schema(implementation = EmpresaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "E-mail ou documento já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<EmpresaResponse> cadastrarEmpresa(CadastroEmpresaRequest request);

    @Operation(summary = "Consulta o perfil da empresa autenticada")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil encontrado",
                    content = @Content(schema = @Schema(implementation = EmpresaResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui o perfil EMPRESA"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    EmpresaResponse meuPerfil();

    @Operation(summary = "Consulta o dashboard da empresa autenticada")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dashboard encontrado",
                    content = @Content(schema = @Schema(implementation = EmpresaDashboardResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui o perfil EMPRESA")
    })
    EmpresaDashboardResponse dashboard();
}
