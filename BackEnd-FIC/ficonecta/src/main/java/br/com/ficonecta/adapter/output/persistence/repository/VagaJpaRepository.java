package br.com.ficonecta.adapter.output.persistence.repository;

import br.com.ficonecta.domain.model.Vaga;
import br.com.ficonecta.domain.model.enums.StatusVaga;
import br.com.ficonecta.domain.port.output.VagaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

public interface VagaJpaRepository extends VagaRepository, JpaRepository<Vaga, UUID> {

    @Override
    @EntityGraph(attributePaths = "empresa")
    Page<Vaga> findByStatusAndDataVagaGreaterThanEqual(StatusVaga status, LocalDate data, Pageable pageable);
}
