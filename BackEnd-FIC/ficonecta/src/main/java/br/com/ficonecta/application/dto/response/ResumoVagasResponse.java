package br.com.ficonecta.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumoVagasResponse {
    private long abertas;
    private long fechadas;
    private long canceladas;
    private long total;
}
