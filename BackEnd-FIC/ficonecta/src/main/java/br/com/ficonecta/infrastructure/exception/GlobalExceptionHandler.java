package br.com.ficonecta.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ErrorResponse> handleEmailJaCadastrado(EmailJaCadastradoException ex) {
        return montarResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CpfJaCadastradoException.class)
    public ResponseEntity<ErrorResponse> handleCpfJaCadastrado(CpfJaCadastradoException ex) {
        return montarResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacao(MethodArgumentNotValidException ex) {
        String mensagens = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return montarResposta(HttpStatus.BAD_REQUEST, mensagens);
    }

    private ResponseEntity<ErrorResponse> montarResposta(HttpStatus status, String mensagem) {
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem
        );
        return ResponseEntity.status(status).body(erro);
    }

    @ExceptionHandler(DocumentoJaCadastradoException.class)
    public ResponseEntity<ErrorResponse> handleDocumentoJaCadastrado(DocumentoJaCadastradoException ex) {
        return montarResposta(HttpStatus.CONFLICT, ex.getMessage());
    }
}
