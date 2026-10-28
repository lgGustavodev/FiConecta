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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CadastroVagaService {

    private final EmpresaRepository empresaRepository;
    private final VagaRepository vagaRepository;
    private final VagaMapper vagaMapper;

    @Transactional
    public VagaResponse cadastrar(UUID usuarioId, CadastroVagaRequest request) {

        log.debug("Cadastrando vaga (usuarioId={})", usuarioId);

        Empresa empresa = empresaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(PerfilNaoEncontradoException::new);

        Vaga vaga = vagaMapper.toEntity(request);
        vaga.setEmpresa(empresa);
        vaga.setStatus(StatusVaga.ABERTA);

        Vaga vagaSalva = vagaRepository.save(vaga);

        log.info("Vaga cadastrada (empresaId={}, vagaId={}, dataVaga={})",
                empresa.getId(), vagaSalva.getId(), vagaSalva.getDataVaga());

        return vagaMapper.toResponse(vagaSalva);
    }
}