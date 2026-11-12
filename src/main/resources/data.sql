-- Datos iniciales de prueba: estudiantes y libros ya registrados (supuesto del taller)
INSERT INTO estudiante (codigo, nombre, email) VALUES ('2024001', 'Ana Gomez', 'ana.gomez@universidad.edu.co');
INSERT INTO estudiante (codigo, nombre, email) VALUES ('2024002', 'Carlos Ruiz', 'carlos.ruiz@universidad.edu.co');

INSERT INTO libro (isbn, titulo, autor, disponible) VALUES ('978-0134685991', 'Effective Java', 'Joshua Bloch', TRUE);
INSERT INTO libro (isbn, titulo, autor, disponible) VALUES ('978-0132350884', 'Clean Code', 'Robert C. Martin', TRUE);
INSERT INTO libro (isbn, titulo, autor, disponible) VALUES ('978-0201633610', 'Design Patterns', 'Gamma, Helm, Johnson, Vlissides', FALSE);

-- El libro 3 ya está prestado: tiene un préstamo activo (R5)
INSERT INTO prestamo (estudiante_id, libro_id, fecha_prestamo, fecha_devolucion, activo) VALUES (2, 3, DATE '2026-11-01', NULL, TRUE);
