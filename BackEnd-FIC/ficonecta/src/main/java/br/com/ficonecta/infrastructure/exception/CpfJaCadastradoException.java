package br.com.ficonecta.infrastructure.exception;

public class CpfJaCadastradoException extends RuntimeException {
    public CpfJaCadastradoException(String cpf) {
        super("CPF já cadastrado: " + cpf);
    }
}
