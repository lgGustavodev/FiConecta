package br.com.ficonecta.application.service;

import br.com.ficonecta.application.dto.response.EmpresaDashboardResponse;
import br.com.ficonecta.application.dto.response.EmpresaResponse;
import br.com.ficonecta.domain.model.enums.StatusVaga;
import br.com.ficonecta.domain.model.enums.TipoDocumento;
import br.com.ficonecta.domain.port.output.VagaRepository;
import br.com.ficonecta.infrastructure.exception.PerfilNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpresaDashboardServiceTest {

    @Mock
    private EmpresaPerfilService empresaPerfilService;

    @Mock
    private VagaRepository vagaRepository;

    @InjectMocks
    private EmpresaDashboardService empresaDashboardService;

    @Test
    void deveMontarResumoDeVagasComTotalSomado() {

        // Arrange
        UUID usuarioId = UUID.randomUUID();
        UUID empresaId = UUID.randomUUID();

        EmpresaResponse perfil = criarPerfil(empresaId);

        when(empresaPerfilService.buscarMeuPerfil(usuarioId)).thenReturn(perfil);
        when(vagaRepository.countByEmpresaIdAndStatus(empresaId, StatusVaga.ABERTA)).thenReturn(3L);
        when(vagaRepository.countByEmpresaIdAndStatus(empresaId, StatusVaga.FECHADA)).thenReturn(1L);
        when(vagaRepository.countByEmpresaIdAndStatus(empresaId, StatusVaga.CANCELADA)).thenReturn(2L);

        // Act
        EmpresaDashboardResponse resultado =
                empresaDashboardService.buscarDashboard(usuarioId);

        // Assert
        assertEquals(perfil, resultado.getPerfil());
        assertEquals(3L, resultado.getResumoVagas().getAbertas());
        assertEquals(1L, resultado.getResumoVagas().getFechadas());
        assertEquals(2L, resultado.getResumoVagas().getCanceladas());
        assertEquals(6L, resultado.getResumoVagas().getTotal());
    }

    @Test
    void deveConsultarVagasPeloIdDaEmpresaENaoPeloIdDoUsuario() {

        // Arrange
        UUID usuarioId = UUID.randomUUID();
        UUID empresaId = UUID.randomUUID();

        when(empresaPerfilService.buscarMeuPerfil(usuarioId))
                .thenReturn(criarPerfil(empresaId));

        // Act
        empresaDashboardService.buscarDashboard(usuarioId);

        // Assert
        verify(vagaRepository).countByEmpresaIdAndStatus(empresaId, StatusVaga.ABERTA);
        verify(vagaRepository).countByEmpresaIdAndStatus(empresaId, StatusVaga.FECHADA);
        verify(vagaRepository).countByEmpresaIdAndStatus(empresaId, StatusVaga.CANCELADA);

        verify(vagaRepository, never())
                .countByEmpresaIdAndStatus(eq(usuarioId), any(StatusVaga.class));
    }

    @Test
    void deveRetornarTudoZeradoQuandoEmpresaNaoTemVagas() {

        // Arrange
        UUID usuarioId = UUID.randomUUID();

        when(empresaPerfilService.buscarMeuPerfil(usuarioId))
                .thenReturn(criarPerfil(UUID.randomUUID()));
        // sem stub de contagem: o mock devolve 0 por padrão

        // Act
        EmpresaDashboardResponse resultado =
                empresaDashboardService.buscarDashboard(usuarioId);

        // Assert
        assertEquals(0L, resultado.getResumoVagas().getAbertas());
        assertEquals(0L, resultado.getResumoVagas().getFechadas());
        assertEquals(0L, resultado.getResumoVagas().getCanceladas());
        assertEquals(0L, resultado.getResumoVagas().getTotal());
    }

    @Test
    void devePropagarExcecaoQuandoPerfilNaoForEncontrado() {

        // Arrange
        UUID usuarioId = UUID.randomUUID();

        when(empresaPerfilService.buscarMeuPerfil(usuarioId))
                .thenThrow(new PerfilNaoEncontradoException());

        // Act & Assert
        assertThrows(
                PerfilNaoEncontradoException.class,
                () -> empresaDashboardService.buscarDashboard(usuarioId)
        );

        verifyNoInteractions(vagaRepository);
    }

    private EmpresaResponse criarPerfil(UUID empresaId) {
        return EmpresaResponse.builder()
                .id(empresaId)
                .nome("Supermercado X")
                .email("contato@supermercadox.com")
                .tipoDocumento(TipoDocumento.CNPJ)
                .build();
    }
}