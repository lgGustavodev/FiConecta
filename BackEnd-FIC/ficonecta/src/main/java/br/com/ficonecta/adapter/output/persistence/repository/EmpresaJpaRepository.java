package br.com.ficonecta.adapter.output.persistence.repository;

import br.com.ficonecta.domain.model.Empresa;
import br.com.ficonecta.domain.port.output.EmpresaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmpresaJpaRepository extends EmpresaRepository, JpaRepository<Empresa, UUID> {
}
