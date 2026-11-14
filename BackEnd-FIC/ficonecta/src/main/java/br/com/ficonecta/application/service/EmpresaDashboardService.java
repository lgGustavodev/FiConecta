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

        ResumoVagasResponse resumoVagas = montarResumoVagas(perfil.getId(), perfil.getNome());

        return EmpresaDashboardResponse.builder()
                .perfil(perfil)
                .resumoVagas(resumoVagas)
                .build();
    }

    private ResumoVagasResponse montarResumoVagas(UUID empresaId, String nomeEmpresa) {

        long abertas = vagaRepository.countByEmpresaIdAndStatus(empresaId, StatusVaga.ABERTA);
        long fechadas = vagaRepository.countByEmpresaIdAndStatus(empresaId, StatusVaga.FECHADA);
        long canceladas = vagaRepository.countByEmpresaIdAndStatus(empresaId, StatusVaga.CANCELADA);
        long expiradas = vagaRepository.countByEmpresaIdAndStatus(empresaId, StatusVaga.EXPIRADA);


        log.debug(
                "Resumo de vagas da empresa '{}': abertas={}, fechadas={}, canceladas={}, expiradas={}",
                nomeEmpresa, abertas, fechadas, canceladas, expiradas
        );

        return ResumoVagasResponse.builder()
                .abertas(abertas)
                .fechadas(fechadas)
                .canceladas(canceladas)
                .expiradas(expiradas)
                .total(abertas + fechadas + canceladas + expiradas)
                .build();
    }
}