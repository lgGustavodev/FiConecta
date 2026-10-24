package br.com.ficonecta.domain.port.output;

import br.com.ficonecta.domain.model.enums.StatusVaga;

import java.util.UUID;

public interface VagaRepository {

    long countByStatus(StatusVaga status);

    long countByEmpresaIdAndStatus(UUID empresaId, StatusVaga status);
}
