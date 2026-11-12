# SmartLibrary

API REST por capas (Controller → Service → Repository) para **registrar y consultar préstamos de libros**.
Taller guiado de Diseño de Software, cuarto semestre.

**Integrantes:** Juan Felipe Narváez Herrera · Ian Santiago Ordóñez Rosero

📄 **Documento del taller (actividades 1–7, tablas y diagramas UML):** [docs/Taller-SmartLibrary.md](docs/Taller-SmartLibrary.md)

## Tecnologías

Java 21 · Spring Boot 3.3.5 · Maven · Spring Web · Spring Data JPA · Validation · H2

## Ejecutar

No hace falta instalar Maven; el proyecto trae el Maven Wrapper.

```bash
./mvnw spring-boot:run        # Linux / macOS
mvnw.cmd spring-boot:run      # Windows
```

La API queda en `http://localhost:8080` y la consola de H2 en `http://localhost:8080/h2-console`
(JDBC URL `jdbc:h2:mem:smartlibrary`, usuario `sa`, sin contraseña).

## Pruebas

```bash
./mvnw test
```

11 pruebas: 7 de integración con MockMvc (los escenarios del taller) y 4 unitarias del servicio con Mockito.

## Endpoints

| Método | Ruta | Cuerpo | Respuestas |
|---|---|---|---|
| POST | `/api/prestamos` | `{"estudianteId": 1, "libroId": 1}` | 201 creado · 400 inválido · 404 estudiante/libro inexistente · 409 libro no disponible |
| GET | `/api/prestamos` | — | 200 con la lista de préstamos |

Ejemplos:

```bash
# Préstamo exitoso (201)
curl -i -X POST http://localhost:8080/api/prestamos -H "Content-Type: application/json" -d "{\"estudianteId\":1,\"libroId\":1}"

# Libro ya prestado (409)
curl -i -X POST http://localhost:8080/api/prestamos -H "Content-Type: application/json" -d "{\"estudianteId\":1,\"libroId\":3}"

# Consultar préstamos (200)
curl -i http://localhost:8080/api/prestamos
```

## Datos de prueba

| Estudiantes | Libros |
|---|---|
| 1 · Ana Gómez | 1 · Effective Java (disponible) |
| 2 · Carlos Ruiz | 2 · Clean Code (disponible) |
| | 3 · Design Patterns (prestado) |

## Estructura

```
src/main/java/com/smartlibrary
├── controller/   PrestamoController
├── service/      PrestamoService
├── repository/   EstudianteRepository, LibroRepository, PrestamoRepository
├── model/        Estudiante, Libro, Prestamo
├── dto/          PrestamoRequest, PrestamoResponse, ErrorResponse
└── exception/    excepciones y GlobalExceptionHandler
docs/
└── Taller-SmartLibrary.md
```
