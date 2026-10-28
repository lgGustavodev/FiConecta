package br.com.ficonecta.adapter.input.controller.swagger;

import br.com.ficonecta.application.dto.request.LoginRequest;
import br.com.ficonecta.application.dto.response.LoginResponse;
import br.com.ficonecta.infrastructure.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Autenticação", description = "Autenticação e geração de token JWT.")
public interface LoginControllerSwagger {

    @Operation(summary = "Autentica um usuário", description = "Valida e-mail e senha e retorna um token JWT para os endpoints protegidos.")
    @RequestBody(required = true, description = "Credenciais de acesso",
            content = @Content(schema = @Schema(implementation = LoginRequest.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<LoginResponse> login(LoginRequest request);
}
