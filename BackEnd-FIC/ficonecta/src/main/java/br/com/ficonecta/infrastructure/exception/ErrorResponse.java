package br.com.ficonecta.infrastructure.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem
) {
}
