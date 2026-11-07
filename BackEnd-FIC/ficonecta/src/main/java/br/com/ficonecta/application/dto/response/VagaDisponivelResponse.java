package br.com.ficonecta.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VagaDisponivelResponse {

    private UUID id;
    private String titulo;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataVaga;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    private EnderecoResponse endereco;
    private String empresaNome;
}