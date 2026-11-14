package br.com.ficonecta.infrastructure.exception;

import br.com.ficonecta.domain.model.enums.StatusVaga;

public class VagaStatusInvalidoException extends RuntimeException{
    public VagaStatusInvalidoException(String acao, StatusVaga statusAtual) {
        super("Não é possível " + acao + " uma vaga com status " + statusAtual);
    }
}
