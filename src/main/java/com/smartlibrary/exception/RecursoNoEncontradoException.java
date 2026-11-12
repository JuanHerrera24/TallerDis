package com.smartlibrary.exception;

/** Se traduce a 404 Not Found (estudiante o libro inexistente). */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
