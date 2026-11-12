package com.smartlibrary.dto;

import java.time.LocalDateTime;

/** Cuerpo uniforme para las respuestas de error (400, 404, 409). */
public record ErrorResponse(int status, String error, String mensaje, LocalDateTime timestamp) {

    public static ErrorResponse de(int status, String error, String mensaje) {
        return new ErrorResponse(status, error, mensaje, LocalDateTime.now());
    }
}
