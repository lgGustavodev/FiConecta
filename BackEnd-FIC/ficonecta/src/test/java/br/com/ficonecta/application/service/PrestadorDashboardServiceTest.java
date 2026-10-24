package br.com.ficonecta.application.service;

import br.com.ficonecta.application.dto.response.PrestadorDashboardResponse;
import br.com.ficonecta.application.dto.response.PrestadorResponse;
import br.com.ficonecta.domain.model.enums.StatusVaga;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrestadorDashboardServiceTest {

    @Mock
    private PrestadorPerfilService prestadorPerfilService;

    @Mock
    private VagaRepository vagaRepository;

    @InjectMocks
    private PrestadorDashboardService prestadorDashboardService;

    @Test
    void deveRetornarPerfilEQuantidadeDeVagasAbertas() {

        // Arrange
        UUID usuarioId = UUID.randomUUID();

        PrestadorResponse perfil = PrestadorResponse.builder()
                .id(UUID.randomUUID())
                .nome("João Silva")
                .email("joao@email.com")
                .disponivel(true)
                .build();

        when(prestadorPerfilService.buscarMeuPerfil(usuarioId)).thenReturn(perfil);
        when(vagaRepository.countByStatus(StatusVaga.ABERTA)).thenReturn(5L);

        // Act
        PrestadorDashboardResponse resultado =
                prestadorDashboardService.buscarDashboard(usuarioId);

        // Assert
        assertNotNull(resultado);
        assertEquals(perfil, resultado.getPerfil());
        assertEquals(5L, resultado.getVagasAbertas());

        verify(prestadorPerfilService).buscarMeuPerfil(usuarioId);
        verify(vagaRepository).countByStatus(StatusVaga.ABERTA);
    }

    @Test
    void devePropagarExcecaoQuandoPerfilNaoForEncontrado() {

        // Arrange
        UUID usuarioId = UUID.randomUUID();

        when(prestadorPerfilService.buscarMeuPerfil(usuarioId))
                .thenThrow(new PerfilNaoEncontradoException());

        // Act & Assert
        assertThrows(
                PerfilNaoEncontradoException.class,
                () -> prestadorDashboardService.buscarDashboard(usuarioId)
        );

        verify(vagaRepository, never()).countByStatus(any(StatusVaga.class));
    }
}