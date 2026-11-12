package com.smartlibrary.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

/**
 * Entidad del dominio: préstamo de un libro a un estudiante.
 * Asociaciones: Prestamo 0..* -> 1 Estudiante y Prestamo 0..* -> 1 Libro (R2, R5).
 */
@Entity
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    @Column(nullable = false)
    private LocalDate fechaPrestamo;

    private LocalDate fechaDevolucion;

    @Column(nullable = false)
    private boolean activo;

    protected Prestamo() {
        // requerido por JPA
    }

    /** R2: el préstamo registra estudiante, libro y fecha. */
    public Prestamo(Estudiante estudiante, Libro libro, LocalDate fechaPrestamo) {
        this.estudiante = estudiante;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.activo = true;
    }

    public Long getId() {
        return id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Libro getLibro() {
        return libro;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public boolean isActivo() {
        return activo;
    }
}
