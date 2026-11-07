package br.com.ficonecta.application.service;


import br.com.ficonecta.adapter.output.persistence.mapper.VagaMapper;
import br.com.ficonecta.application.dto.response.PaginaResponse;
import br.com.ficonecta.application.dto.response.VagaDisponivelResponse;
import br.com.ficonecta.domain.model.Vaga;
import br.com.ficonecta.domain.model.enums.StatusVaga;
import br.com.ficonecta.domain.port.output.VagaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrestadorVagaService {

    private static final int TAMANHO_PADRAO = 20;
    private static final int TAMANHO_MAXIMO = 50;

    private final VagaRepository vagaRepository;
    private final VagaMapper vagaMapper;
    private final Clock clock;

    @Transactional(readOnly = true)
    public PaginaResponse<VagaDisponivelResponse> listarDisponiveis(int page, int size) {

        LocalDate hoje = LocalDate.now(clock);

        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                ajustarTamanho(size),
                Sort.by(Sort.Order.asc("dataVaga"), Sort.Order.asc("horarioInicio"), Sort.Order.asc("id"))
        );

        log.debug("Listando vagas disponíveis a partir de {} page={} size={}",
                hoje, pageable.getPageNumber(), pageable.getPageSize());

        Page<Vaga> vagas = vagaRepository
                .findByStatusAndDataVagaGreaterThanEqual(StatusVaga.ABERTA, hoje, pageable);

        return PaginaResponse.de(vagas, vagaMapper::toDisponivelResponse);
    }

    private int ajustarTamanho(int size) {
        if (size < 1) {
            return TAMANHO_PADRAO;
        }
        return Math.min(size, TAMANHO_MAXIMO);
    }
}