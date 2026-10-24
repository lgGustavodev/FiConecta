package br.com.ficonecta.application.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrestadorDashboardResponse {

    private PrestadorResponse perfil;
    private long vagasAbertas;
}