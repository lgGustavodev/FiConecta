package br.com.ficonecta.application.service;


import br.com.ficonecta.adapter.output.persistence.mapper.VagaMapper;
import br.com.ficonecta.application.dto.request.CadastroVagaRequest;
import br.com.ficonecta.application.dto.response.VagaResponse;
import br.com.ficonecta.domain.model.Empresa;
import br.com.ficonecta.domain.model.Vaga;
import br.com.ficonecta.domain.model.enums.StatusVaga;
import br.com.ficonecta.domain.port.output.EmpresaRepository;
import br.com.ficonecta.domain.port.output.VagaRepository;
import br.com.ficonecta.infrastructure.exception.PerfilNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastroVagaServiceTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private VagaRepository vagaRepository;

    @Mock
    private VagaMapper vagaMapper;

    @InjectMocks
    private CadastroVagaService cadastroVagaService;

    @Captor
    private ArgumentCaptor<Vaga> vagaCaptor;

    @Test
    void deveCadastrarVagaVinculadaAEmpresaDoTokenComStatusAberta() {

        // Arrange
        UUID usuarioId = UUID.randomUUID();
        UUID vagaId = UUID.randomUUID();

        Empresa empresa = Empresa.builder()
                .id(UUID.randomUUID())
                .nome("Supermercado X")
                .build();

        CadastroVagaRequest request = CadastroVagaRequest.builder()
                .titulo("Repositor de estoque")
                .build();

        // O mapper devolve uma vaga com status errado de propósito, para provar
        // que o Service impõe a regra e não depende do valor padrão da entidade
        Vaga vagaDoMapper = Vaga.builder()
                .titulo("Repositor de estoque")
                .status(StatusVaga.FECHADA)
                .build();

        VagaResponse response = VagaResponse.builder()
                .id(vagaId)
                .titulo("Repositor de estoque")
                .status(StatusVaga.ABERTA)
                .build();

        when(empresaRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(empresa));
        when(vagaMapper.toEntity(request)).thenReturn(vagaDoMapper);
        when(vagaRepository.save(any(Vaga.class))).thenAnswer(invocacao -> {
            Vaga vaga = invocacao.getArgument(0);
            vaga.setId(vagaId);                      // simula o id gerado no banco
            return vaga;
        });
        when(vagaMapper.toResponse(any(Vaga.class))).thenReturn(response);

        // Act
        VagaResponse resultado = cadastroVagaService.cadastrar(usuarioId, request);

        // Assert
        verify(vagaRepository).save(vagaCaptor.capture());
        Vaga vagaSalva = vagaCaptor.getValue();

        assertSame(empresa, vagaSalva.getEmpresa());
        assertEquals(StatusVaga.ABERTA, vagaSalva.getStatus());
        assertEquals(response, resultado);

        verify(empresaRepository).findByUsuarioId(usuarioId);
        verify(vagaMapper).toResponse(vagaSalva);
    }

    @Test
    void deveLancarExcecaoQuandoEmpresaNaoForEncontrada() {

        // Arrange
        UUID usuarioId = UUID.randomUUID();
        CadastroVagaRequest request = CadastroVagaRequest.builder()
                .titulo("Repositor de estoque")
                .build();

        when(empresaRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                PerfilNaoEncontradoException.class,
                () -> cadastroVagaService.cadastrar(usuarioId, request)
        );

        verifyNoInteractions(vagaMapper);
        verify(vagaRepository, never()).save(any(Vaga.class));
    }
}