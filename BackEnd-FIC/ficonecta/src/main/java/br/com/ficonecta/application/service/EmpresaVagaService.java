package br.com.ficonecta.application.service;


import br.com.ficonecta.adapter.output.persistence.mapper.VagaMapper;
import br.com.ficonecta.application.dto.response.PaginaResponse;
import br.com.ficonecta.application.dto.response.VagaResponse;
import br.com.ficonecta.domain.model.Empresa;
import br.com.ficonecta.domain.model.Vaga;
import br.com.ficonecta.domain.model.enums.StatusVaga;
import br.com.ficonecta.domain.port.output.EmpresaRepository;
import br.com.ficonecta.domain.port.output.VagaRepository;
import br.com.ficonecta.infrastructure.exception.PerfilNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmpresaVagaService {

    private static final int TAMANHO_PADRAO = 20;
    private static final int TAMANHO_MAXIMO = 50;

    private final EmpresaRepository empresaRepository;
    private final VagaRepository vagaRepository;
    private final VagaMapper vagaMapper;

    @Transactional(readOnly = true)
    public PaginaResponse<VagaResponse> listar(UUID usuarioId, StatusVaga status, int page, int size) {

        Empresa empresa = empresaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(PerfilNaoEncontradoException::new);

        Pageable pageable = PageRequest.of(
                ajustarPagina(page),
                ajustarTamanho(size),
                Sort.by(Sort.Order.desc("createdAt"), Sort.Order.asc("id"))
        );

        log.debug("Listando vagas da empresa {}, status={}, page={}, size={}",
                empresa.getNome(), status, pageable.getPageNumber(), pageable.getPageSize());

        Page<Vaga> vagas = (status == null)
                ? vagaRepository.findByEmpresaId(empresa.getId(), pageable)
                : vagaRepository.findByEmpresaIdAndStatus(empresa.getId(), status, pageable);

        return PaginaResponse.de(vagas, vagaMapper::toResponse);
    }

    private int ajustarPagina(int page) {
        return Math.max(page, 0);
    }

    private int ajustarTamanho(int size) {
        if (size < 1) {
            return TAMANHO_PADRAO;
        }
        return Math.min(size, TAMANHO_MAXIMO);
    }
}