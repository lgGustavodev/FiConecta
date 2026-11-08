package br.com.ficonecta.infrastructure.exception;

public class VagaNaoEncontradaException extends RuntimeException{
    public VagaNaoEncontradaException(){
        super("Vaga não encontrada");
    }
}
