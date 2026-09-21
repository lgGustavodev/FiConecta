package br.com.ficonecta.application.service;

import br.com.ficonecta.adapter.output.persistence.mapper.EmpresaMapper;
import br.com.ficonecta.application.dto.response.EmpresaResponse;
import br.com.ficonecta.domain.port.output.EmpresaRepository;
import br.com.ficonecta.infrastructure.exception.PerfilNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmpresaPerfilService {

    private final EmpresaRepository empresaRepository;
    private  final EmpresaMapper empresaMapper;

    public EmpresaResponse buscarMeuPerfil(UUID usuarioId) {
        return empresaRepository.findByUsuarioId(usuarioId)
                .map(empresaMapper::toResponse)
                .orElseThrow(PerfilNaoEncontradoException::new);
    }
}
