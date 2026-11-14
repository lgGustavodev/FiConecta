package br.com.ficonecta.application.service;


import br.com.ficonecta.domain.model.enums.StatusVaga;
import br.com.ficonecta.domain.port.output.VagaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpiracaoVagaService {

    private final VagaRepository vagaRepository;
    private final Clock clock;

    @Transactional
    public int expirarVagasVencidas() {

        LocalDate hoje = LocalDate.now(clock);
        LocalDateTime agora = LocalDateTime.now(clock);

        int expiradas = vagaRepository.alterarStatusDeVagasAnterioresA(
                StatusVaga.ABERTA, StatusVaga.EXPIRADA, hoje, agora);

        if (expiradas > 0) {
            log.info("Vagas expiradas (quantidade={}, dataLimite={})", expiradas, hoje);
        } else {
            log.debug("Nenhuma vaga vencida para expirar (dataLimite={})", hoje);
        }

        return expiradas;
    }
}