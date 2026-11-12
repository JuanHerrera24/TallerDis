package com.smartlibrary.exception;

/** Se traduce a 409 Conflict (R1: el libro ya está prestado). */
public class LibroNoDisponibleException extends RuntimeException {

    public LibroNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
