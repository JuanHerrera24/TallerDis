package com.smartlibrary;

import com.smartlibrary.repository.LibroRepository;
import com.smartlibrary.repository.PrestamoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Escenarios de la Actividad 6. Cada prueba se revierte al terminar (@Transactional),
 * así todas parten de los datos de data.sql.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PrestamoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private LibroRepository libroRepository;

    private static String cuerpo(long estudianteId, long libroId) {
        return "{\"estudianteId\":" + estudianteId + ",\"libroId\":" + libroId + "}";
    }

    @Test
    @DisplayName("Préstamo de libro disponible -> 201 Created y préstamo registrado")
    void prestamoDeLibroDisponible() throws Exception {
        long antes = prestamoRepository.count();

        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(1, 1)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.estudianteId").value(1))
                .andExpect(jsonPath("$.libroId").value(1))
                .andExpect(jsonPath("$.activo").value(true));

        assertThat(prestamoRepository.count()).isEqualTo(antes + 1);
        assertThat(libroRepository.findById(1L).orElseThrow().estaDisponible()).isFalse();
    }

    @Test
    @DisplayName("Libro ya prestado -> 409 Conflict y sin nuevo préstamo")
    void libroYaPrestado() throws Exception {
        long antes = prestamoRepository.count();

        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(1, 3)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));

        assertThat(prestamoRepository.count()).isEqualTo(antes);
    }

    @Test
    @DisplayName("Mismo libro prestado dos veces -> el segundo intento da 409")
    void segundoPrestamoDelMismoLibro() throws Exception {
        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(1, 2)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(2, 2)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Estudiante inexistente -> 404 Not Found")
    void estudianteInexistente() throws Exception {
        long antes = prestamoRepository.count();

        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(99, 1)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        assertThat(prestamoRepository.count()).isEqualTo(antes);
        assertThat(libroRepository.findById(1L).orElseThrow().estaDisponible()).isTrue();
    }

    @Test
    @DisplayName("Libro inexistente -> 404 Not Found")
    void libroInexistente() throws Exception {
        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(1, 99)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Consulta de préstamos -> 200 OK con registros")
    void consultaDePrestamos() throws Exception {
        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$[0].libroTitulo").exists());
    }

    @Test
    @DisplayName("Solicitud inválida (sin libroId) -> 400 Bad Request y sin préstamo (R4)")
    void solicitudInvalida() throws Exception {
        long antes = prestamoRepository.count();

        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content("{\"estudianteId\":1}"))
                .andExpect(status().isBadRequest());

        assertThat(prestamoRepository.count()).isEqualTo(antes);
    }
}
