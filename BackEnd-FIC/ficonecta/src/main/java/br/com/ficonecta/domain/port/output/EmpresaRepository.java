package br.com.ficonecta.domain.port.output;

import br.com.ficonecta.domain.model.Empresa;

import java.util.Optional;
import java.util.UUID;

public interface EmpresaRepository {
    Empresa save(Empresa empresa);

    Optional<Empresa> findByUsuarioId(UUID usuarioId);

    boolean existsByDocumento(String documento);
}
