package br.com.ficonecta.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CadastroPrestadorRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;


    @NotBlank(message = "CPF é obrigatório")
    @Pattern(
            regexp = "\\d{11}",
            message = "CPF deve possuir 11 dígitos"
    )
    private String cpf;

    private String descricao;
}
