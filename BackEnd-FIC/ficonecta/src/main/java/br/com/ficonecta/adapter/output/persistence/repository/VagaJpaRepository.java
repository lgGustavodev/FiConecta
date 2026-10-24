package br.com.ficonecta.adapter.output.persistence.repository;

import br.com.ficonecta.domain.model.Vaga;
import br.com.ficonecta.domain.port.output.VagaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VagaJpaRepository extends VagaRepository, JpaRepository<Vaga, UUID> {
}
