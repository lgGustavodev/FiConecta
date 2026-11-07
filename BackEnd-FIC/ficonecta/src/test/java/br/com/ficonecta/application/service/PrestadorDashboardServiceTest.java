
package br.com.ficonecta.application.service;

import br.com.ficonecta.application.dto.response.PrestadorDashboardResponse;
import br.com.ficonecta.application.dto.response.PrestadorResponse;
import br.com.ficonecta.domain.model.enums.StatusVaga;
import br.com.ficonecta.domain.port.output.VagaRepository;
import br.com.ficonecta.infrastructure.exception.PerfilNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrestadorDashboardServiceTest {

    private static final Clock CLOCK = Clock.fixed(
            Instant.parse("2026-11-05T12:00:00Z"),
            ZoneId.of("America/Fortaleza")
    );

    private static final LocalDate HOJE = LocalDate.of(2026, 11, 5);

    @Mock
    private PrestadorPerfilService prestadorPerfilService;

    @Mock
    private VagaRepository vagaRepository;

    private PrestadorDashboardService prestadorDashboardService;

    @BeforeEach
    void setUp() {
        prestadorDashboardService = new PrestadorDashboardService(
                prestadorPerfilService,
                vagaRepository,
                CLOCK
        );
    }

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

        when(prestadorPerfilService.buscarMeuPerfil(usuarioId))
                .thenReturn(perfil);

        when(vagaRepository.countByStatusAndDataVagaGreaterThanEqual(
                StatusVaga.ABERTA, HOJE
        )).thenReturn(5L);

        // Act
        PrestadorDashboardResponse resultado =
                prestadorDashboardService.buscarDashboard(usuarioId);

        // Assert
        assertNotNull(resultado);
        assertEquals(perfil, resultado.getPerfil());
        assertEquals(5L, resultado.getVagasAbertas());

        verify(prestadorPerfilService).buscarMeuPerfil(usuarioId);

        verify(vagaRepository)
                .countByStatusAndDataVagaGreaterThanEqual(
                        StatusVaga.ABERTA, HOJE
                );
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

        verify(vagaRepository, never())
                .countByStatusAndDataVagaGreaterThanEqual(
                        any(StatusVaga.class),
                        any(LocalDate.class)
                );
    }
}
