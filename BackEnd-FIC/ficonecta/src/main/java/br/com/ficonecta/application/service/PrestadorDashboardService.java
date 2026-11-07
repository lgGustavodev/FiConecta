package br.com.ficonecta.application.service;


import br.com.ficonecta.application.dto.response.PrestadorDashboardResponse;
import br.com.ficonecta.application.dto.response.PrestadorResponse;
import br.com.ficonecta.domain.model.enums.StatusVaga;
import br.com.ficonecta.domain.port.output.VagaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;


import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class PrestadorDashboardService {

    private final PrestadorPerfilService prestadorPerfilService;
    private final VagaRepository vagaRepository;
    private final Clock clock;

    @Transactional(readOnly = true)
    public PrestadorDashboardResponse buscarDashboard(UUID usuarioId) {
        log.debug("Montando dashboard do prestador (usuarioId={})", usuarioId);
        PrestadorResponse perfil = prestadorPerfilService.buscarMeuPerfil(usuarioId);

        LocalDate hoje = LocalDate.now(clock);

        long vagasAbertas = vagaRepository.countByStatusAndDataVagaGreaterThanEqual(StatusVaga.ABERTA, hoje);
        log.debug("Dashboard do prestador pronto (vagasAbertas={})", vagasAbertas);

        return PrestadorDashboardResponse.builder()
                .perfil(perfil)
                .vagasAbertas(vagasAbertas)
                .build();

    }
}
