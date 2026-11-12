package com.smartlibrary.repository;

import com.smartlibrary.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    /** R5: un libro solo puede tener un préstamo activo a la vez. */
    boolean existsByLibroIdAndActivoTrue(Long libroId);
}
