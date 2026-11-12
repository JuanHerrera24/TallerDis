package com.smartlibrary.dto;

import com.smartlibrary.model.Prestamo;

import java.time.LocalDate;

/** Objeto de salida: evita exponer las entidades JPA directamente. */
public record PrestamoResponse(
        Long id,
        Long estudianteId,
        String estudianteNombre,
        Long libroId,
        String libroTitulo,
        LocalDate fechaPrestamo,
        boolean activo) {

    public static PrestamoResponse desde(Prestamo prestamo) {
        return new PrestamoResponse(
                prestamo.getId(),
                prestamo.getEstudiante().getId(),
                prestamo.getEstudiante().getNombre(),
                prestamo.getLibro().getId(),
                prestamo.getLibro().getTitulo(),
                prestamo.getFechaPrestamo(),
                prestamo.isActivo());
    }
}
