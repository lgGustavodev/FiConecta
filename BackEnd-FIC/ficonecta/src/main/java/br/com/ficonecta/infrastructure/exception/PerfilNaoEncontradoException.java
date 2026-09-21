package br.com.ficonecta.infrastructure.exception;

public class PerfilNaoEncontradoException extends RuntimeException {
    public PerfilNaoEncontradoException() {
        super("Perfil não encontrado para o usuário autenticado");
    }
}
