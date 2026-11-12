package com.smartlibrary.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entidad del dominio: libro del catálogo.
 * Es experto en su propia disponibilidad (GRASP Experto en información).
 */
@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String titulo;

    private String autor;

    @Column(nullable = false)
    private boolean disponible = true;

    protected Libro() {
        // requerido por JPA
    }

    public Libro(String isbn, String titulo, String autor) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.disponible = true;
    }

    /** R1: solo un libro disponible puede prestarse. */
    public boolean estaDisponible() {
        return disponible;
    }

    /** R3: al prestarlo, el libro deja de estar disponible. */
    public void marcarPrestado() {
        this.disponible = false;
    }

    public void marcarDisponible() {
        this.disponible = true;
    }

    public Long getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }
}
