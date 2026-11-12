package com.smartlibrary.service;

import com.smartlibrary.dto.PrestamoRequest;
import com.smartlibrary.exception.LibroNoDisponibleException;
import com.smartlibrary.exception.RecursoNoEncontradoException;
import com.smartlibrary.model.Estudiante;
import com.smartlibrary.model.Libro;
import com.smartlibrary.model.Prestamo;
import com.smartlibrary.repository.EstudianteRepository;
import com.smartlibrary.repository.LibroRepository;
import com.smartlibrary.repository.PrestamoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Pruebas unitarias de las reglas de negocio, sin base de datos (repositorios simulados). */
@ExtendWith(MockitoExtension.class)
class PrestamoServiceTest {

    @Mock
    private EstudianteRepository estudianteRepository;
    @Mock
    private LibroRepository libroRepository;
    @Mock
    private PrestamoRepository prestamoRepository;

    private PrestamoService service;
    private Estudiante estudiante;
    private Libro libro;

    @BeforeEach
    void setUp() {
        service = new PrestamoService(estudianteRepository, libroRepository, prestamoRepository);
        estudiante = new Estudiante("2024001", "Ana Gomez", "ana@u.edu.co");
        libro = new Libro("978-0134685991", "Effective Java", "Joshua Bloch");
    }

    @Test
    void registraPrestamoYMarcaLibroComoPrestado() {
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(prestamoRepository.save(any(Prestamo.class))).thenAnswer(inv -> inv.getArgument(0));

        var respuesta = service.registrarPrestamo(new PrestamoRequest(1L, 1L));

        assertThat(respuesta.activo()).isTrue();
        assertThat(respuesta.libroTitulo()).isEqualTo("Effective Java");
        assertThat(libro.estaDisponible()).isFalse();
        verify(prestamoRepository).save(any(Prestamo.class));
    }

    @Test
    void rechazaLibroNoDisponibleSinGuardarNada() {
        libro.marcarPrestado();
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));

        assertThatThrownBy(() -> service.registrarPrestamo(new PrestamoRequest(1L, 1L)))
                .isInstanceOf(LibroNoDisponibleException.class);
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    void rechazaEstudianteInexistente() {
        when(estudianteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registrarPrestamo(new PrestamoRequest(99L, 1L)))
                .isInstanceOf(RecursoNoEncontradoException.class);
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    void rechazaLibroInexistente() {
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registrarPrestamo(new PrestamoRequest(1L, 99L)))
                .isInstanceOf(RecursoNoEncontradoException.class);
        verify(prestamoRepository, never()).save(any());
    }
}
