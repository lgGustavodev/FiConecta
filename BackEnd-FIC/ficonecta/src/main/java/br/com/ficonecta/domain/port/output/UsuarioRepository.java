package br.com.ficonecta.domain.port.output;

import br.com.ficonecta.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
