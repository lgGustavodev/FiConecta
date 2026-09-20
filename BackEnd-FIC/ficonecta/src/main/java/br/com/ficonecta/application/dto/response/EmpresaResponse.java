package br.com.ficonecta.application.dto.response;


import br.com.ficonecta.domain.model.TipoDocumento;
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
public class EmpresaResponse {

    private UUID id;
    private String nome;
    private String email;
    private TipoDocumento tipoDocumento;
    private String documento;
    private String foto;
    private String descricao;
    private LocalDateTime createdAt;
}