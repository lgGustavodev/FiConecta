package br.com.ficonecta.application.service;


import br.com.ficonecta.adapter.output.persistence.mapper.VagaMapper;
import br.com.ficonecta.application.dto.request.CadastroVagaRequest;
import br.com.ficonecta.application.dto.response.VagaResponse;
import br.com.ficonecta.domain.model.Empresa;
import br.com.ficonecta.domain.model.Vaga;
import br.com.ficonecta.domain.model.enums.StatusVaga;
import br.com.ficonecta.domain.port.output.EmpresaRepository;
import br.com.ficonecta.domain.port.output.VagaRepository;
import br.com.ficonecta.infrastructure.exception.PerfilNaoEncontradoException;
import br.com.ficonecta.infrastructure.exception.VagaNaoEncontradaException;
import br.com.ficonecta.infrastructure.exception.VagaStatusInvalidoException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EdicaoVagaService {

    private final EmpresaRepository empresaRepository;
    private final VagaRepository vagaRepository;
    private final VagaMapper vagaMapper;

    @Transactional
    public VagaResponse editarVaga(UUID usuarioId, UUID vagaId, CadastroVagaRequest cadastroVagaRequest) {

        Empresa empresa = empresaRepository.findByUsuarioId(usuarioId).orElseThrow(PerfilNaoEncontradoException::new);

        Vaga vaga = vagaRepository.findByIdAndEmpresaId(vagaId, empresa.getId())
                .orElseThrow(() -> {
                    log.warn("Edição recusada: vaga não encontrada (vagaId={}, empresaId={})",
                            vagaId, empresa.getId());
                    return new VagaNaoEncontradaException();
                });

        if (vaga.getStatus() != StatusVaga.ABERTA) {
            log.warn("Edição recusada: status inválido (vagaId={}, status={})",
                    vagaId, vaga.getStatus());
            throw new VagaStatusInvalidoException("editar", vaga.getStatus());
        }

        vagaMapper.atualizarEntity(cadastroVagaRequest, vaga);
        Vaga salva = vagaRepository.save(vaga);

        log.info("Vaga editada (vagaId={}, empresaId={})", salva.getId(), empresa.getId());
        return vagaMapper.toResponse(salva);
    }
}
