package br.com.ficonecta.adapter.output.persistence.mapper;

import static org.junit.jupiter.api.Assertions.*;

import br.com.ficonecta.application.dto.request.CadastroVagaRequest;
import br.com.ficonecta.application.dto.request.EnderecoRequest;
import br.com.ficonecta.application.dto.response.VagaResponse;
import br.com.ficonecta.domain.model.Vaga;
import br.com.ficonecta.domain.model.enums.Endereco;
import br.com.ficonecta.domain.model.enums.StatusVaga;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;


class VagaMapperTest {

    private final VagaMapper vagaMapper = Mappers.getMapper(VagaMapper.class);

    @Test
    void deveConverterRequestParaEntidadeIncluindoOEndereco() {

        // Arrange
        CadastroVagaRequest request = criarRequest();

        // Act
        Vaga vaga = vagaMapper.toEntity(request);

        // Assert
        assertEquals("Repositor de estoque", vaga.getTitulo());
        assertEquals("Reposição de prateleiras", vaga.getDescricao());
        assertEquals(new BigDecimal("120.00"), vaga.getValor());
        assertEquals(LocalDate.of(2026, 11, 5), vaga.getDataVaga());
        assertEquals(LocalTime.of(7, 0), vaga.getHorarioInicio());
        assertEquals(LocalTime.of(13, 0), vaga.getHorarioFim());

        assertNotNull(vaga.getEndereco());
        assertEquals("60000000", vaga.getEndereco().getCep());
        assertEquals("Rua das Flores", vaga.getEndereco().getLogradouro());
        assertEquals("123", vaga.getEndereco().getNumero());
        assertEquals("Loja 2", vaga.getEndereco().getComplemento());
        assertEquals("Centro", vaga.getEndereco().getBairro());
        assertEquals("Fortaleza", vaga.getEndereco().getCidade());
        assertEquals("CE", vaga.getEndereco().getUf());
    }

    @Test
    void naoDevePreencherOsCamposQueSaoDoServiceOuDoBanco() {

        // Act
        Vaga vaga = vagaMapper.toEntity(criarRequest());

        // Assert: o mapper só traduz, e quem define isto são o Service e o JPA
        assertNull(vaga.getId());
        assertNull(vaga.getEmpresa());
        assertNull(vaga.getCreatedAt());
        assertNull(vaga.getUpdatedAt());
    }

    @Test
    void deveConverterEntidadeParaResponseIncluindoOEndereco() {

        // Arrange
        UUID id = UUID.randomUUID();
        LocalDateTime criadaEm = LocalDateTime.of(2026, 10, 27, 9, 30);

        Vaga vaga = Vaga.builder()
                .id(id)
                .titulo("Repositor de estoque")
                .descricao("Reposição de prateleiras")
                .valor(new BigDecimal("120.00"))
                .dataVaga(LocalDate.of(2026, 11, 5))
                .horarioInicio(LocalTime.of(7, 0))
                .horarioFim(LocalTime.of(13, 0))
                .status(StatusVaga.ABERTA)
                .createdAt(criadaEm)
                .endereco(Endereco.builder()
                        .cep("60000000")
                        .logradouro("Rua das Flores")
                        .numero("123")
                        .complemento("Loja 2")
                        .bairro("Centro")
                        .cidade("Fortaleza")
                        .uf("CE")
                        .build())
                .build();

        // Act
        VagaResponse response = vagaMapper.toResponse(vaga);

        // Assert
        assertEquals(id, response.getId());
        assertEquals("Repositor de estoque", response.getTitulo());
        assertEquals(new BigDecimal("120.00"), response.getValor());
        assertEquals(LocalDate.of(2026, 11, 5), response.getDataVaga());
        assertEquals(LocalTime.of(7, 0), response.getHorarioInicio());
        assertEquals(LocalTime.of(13, 0), response.getHorarioFim());
        assertEquals(StatusVaga.ABERTA, response.getStatus());
        assertEquals(criadaEm, response.getCreatedAt());

        assertNotNull(response.getEndereco());
        assertEquals("60000000", response.getEndereco().getCep());
        assertEquals("Rua das Flores", response.getEndereco().getLogradouro());
        assertEquals("CE", response.getEndereco().getUf());
    }

    private CadastroVagaRequest criarRequest() {
        return CadastroVagaRequest.builder()
                .titulo("Repositor de estoque")
                .descricao("Reposição de prateleiras")
                .valor(new BigDecimal("120.00"))
                .dataVaga(LocalDate.of(2026, 11, 5))
                .horarioInicio(LocalTime.of(7, 0))
                .horarioFim(LocalTime.of(13, 0))
                .endereco(EnderecoRequest.builder()
                        .cep("60000000")
                        .logradouro("Rua das Flores")
                        .numero("123")
                        .complemento("Loja 2")
                        .bairro("Centro")
                        .cidade("Fortaleza")
                        .uf("CE")
                        .build())
                .build();
    }
}