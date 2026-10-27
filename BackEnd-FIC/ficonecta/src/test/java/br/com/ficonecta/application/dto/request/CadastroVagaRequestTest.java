package br.com.ficonecta.application.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CadastroVagaRequestValidacaoTest {

    private static Validator validator;

    @BeforeAll
    static void criarValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void deveAceitarRequestValido() {
        assertTrue(camposComErro(requestValido()).isEmpty());
    }

    @Test
    void deveRejeitarTituloEmBranco() {
        CadastroVagaRequest request = requestValido();
        request.setTitulo("   ");

        assertEquals(Set.of("titulo"), camposComErro(request));
    }

    @ParameterizedTest(name = "valor {0} deve ser rejeitado")
    @ValueSource(strings = {"0", "0.00", "-5", "120.123", "123456789.00"})
    void deveRejeitarValorInvalido(String valor) {
        CadastroVagaRequest request = requestValido();
        request.setValor(new BigDecimal(valor));

        assertEquals(Set.of("valor"), camposComErro(request));
    }

    @Test
    void deveRejeitarDataNoPassado() {
        CadastroVagaRequest request = requestValido();
        request.setDataVaga(LocalDate.now().minusDays(1));

        assertEquals(Set.of("dataVaga"), camposComErro(request));
    }

    @Test
    void deveAceitarDataDeHoje() {
        CadastroVagaRequest request = requestValido();
        request.setDataVaga(LocalDate.now());

        assertTrue(camposComErro(request).isEmpty());
    }

    @Test
    void deveRejeitarEnderecoAusente() {
        CadastroVagaRequest request = requestValido();
        request.setEndereco(null);

        assertEquals(Set.of("endereco"), camposComErro(request));
    }

    @Test
    void deveValidarOsCamposDoEnderecoAninhado() {
        CadastroVagaRequest request = requestValido();
        request.getEndereco().setCep("60000-000");
        request.getEndereco().setUf("ce");

        assertEquals(Set.of("endereco.cep", "endereco.uf"), camposComErro(request));
    }

    // ---------------------------------------------------------------

    private Set<String> camposComErro(CadastroVagaRequest request) {
        return validator.validate(request).stream()
                .map(violacao -> violacao.getPropertyPath().toString())
                .collect(Collectors.toSet());
    }

    private CadastroVagaRequest requestValido() {
        return CadastroVagaRequest.builder()
                .titulo("Repositor de estoque")
                .descricao("Reposição de prateleiras")
                .valor(new BigDecimal("120.00"))
                .dataVaga(LocalDate.now().plusDays(1))
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