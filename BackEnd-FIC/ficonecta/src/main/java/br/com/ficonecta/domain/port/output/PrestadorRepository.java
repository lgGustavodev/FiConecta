package br.com.ficonecta.domain.port.output;

import br.com.ficonecta.domain.model.Prestador;

import java.util.Optional;
import java.util.UUID;

public interface PrestadorRepository {

    Prestador save(Prestador prestador);

    Optional<Prestador> findById(UUID id);

    Optional<Prestador> findByUsuarioId(UUID usuarioId);

    boolean existsByCpf(String cpf);
}
