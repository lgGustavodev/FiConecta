package br.com.ficonecta.adapter.output.persistence.repository;

import br.com.ficonecta.domain.model.Prestador;
import br.com.ficonecta.domain.port.output.PrestadorRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PrestadorJpaRepository extends PrestadorRepository, JpaRepository<Prestador, UUID> {
}