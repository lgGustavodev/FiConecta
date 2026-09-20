package br.com.ficonecta.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrestadorResponse {

    private UUID id;
    private String nome;
    private String email;
    private String cpf;
    private String foto;
    private String descricao;
    private boolean disponivel;
    private LocalDateTime createdAt;
}
