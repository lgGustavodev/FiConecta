package br.com.ficonecta.infrastructure.exception;

public class DocumentoJaCadastradoException extends RuntimeException {

    public DocumentoJaCadastradoException(String documento) {
        super("Documento já cadastrado: " + documento);
    }
}