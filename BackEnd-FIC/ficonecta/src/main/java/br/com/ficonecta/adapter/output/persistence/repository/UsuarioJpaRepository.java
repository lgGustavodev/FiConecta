package br.com.ficonecta.adapter.output.persistence.repository;

import br.com.ficonecta.domain.model.Usuario;
import br.com.ficonecta.domain.port.output.UsuarioRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioJpaRepository extends UsuarioRepository, JpaRepository<Usuario, UUID> {
}
