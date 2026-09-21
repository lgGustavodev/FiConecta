package br.com.ficonecta.infrastructure.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AutenticacaoUtil {

    public UUID getUsuarioIdAutenticado() {
        String usuarioId = (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return UUID.fromString(usuarioId);
    }
}