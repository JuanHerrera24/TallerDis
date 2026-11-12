package com.smartlibrary.controller;

import com.smartlibrary.dto.PrestamoRequest;
import com.smartlibrary.dto.PrestamoResponse;
import com.smartlibrary.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * Capa de presentación: solo recibe la petición HTTP, valida el formato
 * y delega en el servicio. No contiene reglas de negocio ni accede a repositorios.
 */
@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<PrestamoResponse> registrar(@Valid @RequestBody PrestamoRequest request) {
        PrestamoResponse creado = prestamoService.registrarPrestamo(request);
        return ResponseEntity.created(URI.create("/api/prestamos/" + creado.id())).body(creado);
    }

    @GetMapping
    public ResponseEntity<List<PrestamoResponse>> listar() {
        return ResponseEntity.ok(prestamoService.listarPrestamos());
    }
}
