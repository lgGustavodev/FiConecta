package br.com.ficonecta.domain.port.output;

import br.com.ficonecta.domain.model.Vaga;
import br.com.ficonecta.domain.model.enums.StatusVaga;

import java.util.UUID;

public interface VagaRepository {
    Vaga save(Vaga vaga);

    long countByStatus(StatusVaga status);

    long countByEmpresaIdAndStatus(UUID empresaId, StatusVaga status);
}
