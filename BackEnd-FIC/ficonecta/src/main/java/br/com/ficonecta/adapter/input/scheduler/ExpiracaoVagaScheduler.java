package br.com.ficonecta.adapter.input.scheduler;

import br.com.ficonecta.application.service.ExpiracaoVagaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpiracaoVagaScheduler {

    private final ExpiracaoVagaService expiracaoVagaService;

    @Scheduled(cron = "${app.vagas.expiracao-cron:0 5 0 * * *}", zone = "${app.timezone}")
    public void expirarVagasVencidas() {
        log.debug("Job de expiração de vagas iniciado");
        expiracaoVagaService.expirarVagasVencidas();
    }
}