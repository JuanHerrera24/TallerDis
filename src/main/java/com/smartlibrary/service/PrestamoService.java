package com.smartlibrary.service;

import com.smartlibrary.dto.PrestamoRequest;
import com.smartlibrary.dto.PrestamoResponse;
import com.smartlibrary.exception.LibroNoDisponibleException;
import com.smartlibrary.exception.RecursoNoEncontradoException;
import com.smartlibrary.model.Estudiante;
import com.smartlibrary.model.Libro;
import com.smartlibrary.model.Prestamo;
import com.smartlibrary.repository.EstudianteRepository;
import com.smartlibrary.repository.LibroRepository;
import com.smartlibrary.repository.PrestamoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Capa de negocio. Coordina el caso de uso «Registrar préstamo»
 * (GRASP Controlador de caso de uso) y aplica las reglas R1–R5.
 */
@Service
public class PrestamoService {

    private final EstudianteRepository estudianteRepository;
    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    public PrestamoService(EstudianteRepository estudianteRepository,
                           LibroRepository libroRepository,
                           PrestamoRepository prestamoRepository) {
        this.estudianteRepository = estudianteRepository;
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
    }

    /**
     * Registra un préstamo. Es transaccional: el cambio de disponibilidad del libro
     * y la creación del préstamo se confirman juntos o no se confirma ninguno (R3, R4).
     */
    @Transactional
    public PrestamoResponse registrarPrestamo(PrestamoRequest request) {
        Estudiante estudiante = estudianteRepository.findById(request.estudianteId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el estudiante con id " + request.estudianteId()));

        Libro libro = libroRepository.findById(request.libroId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el libro con id " + request.libroId()));

        // R1 y R5: solo un libro disponible y sin préstamo activo puede prestarse.
        if (!libro.estaDisponible() || prestamoRepository.existsByLibroIdAndActivoTrue(libro.getId())) {
            throw new LibroNoDisponibleException(
                    "El libro '" + libro.getTitulo() + "' no esta disponible");
        }

        // R2: el préstamo registra estudiante, libro y fecha.
        Prestamo prestamo = new Prestamo(estudiante, libro, LocalDate.now());
        // R3: al prestarlo, el libro deja de estar disponible.
        libro.marcarPrestado();

        libroRepository.save(libro);
        Prestamo guardado = prestamoRepository.save(prestamo);
        return PrestamoResponse.desde(guardado);
    }

    @Transactional(readOnly = true)
    public List<PrestamoResponse> listarPrestamos() {
        return prestamoRepository.findAll().stream()
                .map(PrestamoResponse::desde)
                .toList();
    }
}
