package br.com.ficonecta.application.service;

import br.com.ficonecta.application.dto.response.EmpresaDashboardResponse;
import br.com.ficonecta.application.dto.response.EmpresaResponse;
import br.com.ficonecta.application.dto.response.ResumoVagasResponse;
import br.com.ficonecta.domain.model.enums.StatusVaga;
import br.com.ficonecta.domain.port.output.VagaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmpresaDashboardService {

    private final EmpresaPerfilService empresaPerfilService;
    private final VagaRepository vagaRepository;

    @Transactional(readOnly = true)
    public EmpresaDashboardResponse buscarDashboard(UUID usuarioId) {
        log.debug("Montando dashboard da empresa (usuarioId={})", usuarioId);

        EmpresaResponse perfil = empresaPerfilService.buscarMeuPerfil(usuarioId);

        ResumoVagasResponse resumoVagas = montarResumoVagas(perfil.getId());

        return EmpresaDashboardResponse.builder()
                .perfil(perfil)
                .resumoVagas(resumoVagas)
                .build();
    }

    private ResumoVagasResponse montarResumoVagas(UUID empresaId) {

        long abertas = vagaRepository.countByEmpresaIdAndStatus(empresaId, StatusVaga.ABERTA);
        long fechadas = vagaRepository.countByEmpresaIdAndStatus(empresaId, StatusVaga.FECHADA);
        long canceladas = vagaRepository.countByEmpresaIdAndStatus(empresaId, StatusVaga.CANCELADA);

        log.debug("Resumo de vagas (empresaId={}): abertas={}, fechadas={}, canceladas={}",
                empresaId, abertas, fechadas, canceladas);

        return ResumoVagasResponse.builder()
                .abertas(abertas)
                .fechadas(fechadas)
                .canceladas(canceladas)
                .total(abertas + fechadas + canceladas)
                .build();
    }
}