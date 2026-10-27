package br.com.ficonecta.application.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CadastroVagaRequest {

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 150, message = "Título deve ter no máximo 150 caracteres")
    private String titulo;

    private String descricao;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.00", inclusive = false, message = "Valor deve ser maior que zero")
    @Digits(integer = 8, fraction = 2,
            message = "Valor deve ter no máximo 8 dígitos inteiros e 2 decimais")
    private BigDecimal valor;

    @NotNull(message = "Data da vaga é obrigatória")
    @FutureOrPresent(message = "Data da vaga não pode estar no passado")
    private LocalDate dataVaga;

    @NotNull(message = "Horário de início é obrigatório")
    private LocalTime horarioInicio;

    @NotNull(message = "Horário de fim é obrigatório")
    private LocalTime horarioFim;

    @NotNull(message = "Endereço é obrigatório")
    @Valid
    private EnderecoRequest endereco;
}
