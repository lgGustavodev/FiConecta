package br.com.ficonecta.application.service;

import br.com.ficonecta.adapter.output.persistence.mapper.PrestadorMapper;
import br.com.ficonecta.application.dto.response.PrestadorResponse;
import br.com.ficonecta.domain.port.output.PrestadorRepository;
import br.com.ficonecta.infrastructure.exception.PerfilNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrestadorPerfilService {

    private final PrestadorRepository prestadorRepository;
    private final PrestadorMapper prestadorMapper;

    public PrestadorResponse buscarMeuPerfil(UUID usuarioId) {

        return prestadorRepository.findByUsuarioId(usuarioId)
                .map(prestadorMapper::toResponse)
                .orElseThrow(PerfilNaoEncontradoException::new);
    }
}
