# Students CRUD — Spring Boot + Kotlin + PostgreSQL

API REST con CRUD completo para 4 entidades: Student, Professor, Subject y Enrollment.

## Tecnologías
- Kotlin 2.2.0
- Spring Boot 4.0.6
- Spring Data JPA
- PostgreSQL 16 (vía Docker)
- Gradle
- Java 25

## Estructura de paquetes

```
src/main/kotlin/com/pucetec/students
├── controllers
├── services
├── repositories
├── mappers
├── entities
├── dto
├── exceptions
└── handlers
```

## Levantar la base de datos

```bash
docker compose up -d
```

Para detener sin borrar datos:
```bash
docker compose stop
```

Para detener y borrar datos:
```bash
docker compose down -v
```

## Ejecutar la aplicación

```bash
./gradlew bootRun
```

La app corre en `http://localhost:8787`. Hibernate crea las tablas automáticamente al arrancar (`ddl-auto: update`).

## Endpoints

### Student — `/api/students`
| Verbo | Ruta | Descripción |
|---|---|---|
| POST | /api/students | Crear estudiante |
| GET | /api/students | Listar estudiantes |
| GET | /api/students/{id} | Obtener por id |
| PUT | /api/students/{id} | Actualizar |
| DELETE | /api/students/{id} | Eliminar |

### Professor — `/api/professors`
Mismos 5 endpoints que Student.

### Subject — `/api/subjects`
Mismos 5 endpoints. El POST y PUT requieren `professorId` válido.

### Enrollment — `/api/enrollments`
| Verbo | Ruta | Descripción |
|---|---|---|
| POST | /api/enrollments | Inscribir estudiante en materia (status inicial: INSCRITO) |
| GET | /api/enrollments | Listar inscripciones |
| GET | /api/enrollments/{id} | Obtener por id |
| PUT | /api/enrollments/{id} | Actualizar status |
| DELETE | /api/enrollments/{id} | Eliminar |

## Manejo de errores

Cada entidad tiene su propia excepción (`StudentNotFoundException`, `ProfessorNotFound`, `SubjectNotFound`, `EnrollmentNotFound`), capturadas por `GlobalExceptionHandler` y devueltas como JSON con status `404`:

```json
{
  "timestamp": "2026-06-19T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Student with id 99 not found"
}
```

Las validaciones de campos en blanco lanzan `IllegalArgumentException`, capturada con status `400`.

## Validaciones implementadas

- `name` no puede estar en blanco en Student, Professor y Subject.
- `code` no puede estar en blanco en Subject.
- `professorId` debe existir al crear o actualizar un Subject.
- `studentId` y `subjectId` deben existir al crear un Enrollment.
- El status inicial de un Enrollment siempre es `"INSCRITO"`.

## Postman

Importar `students-crud.postman_collection.json`, organizada en 4 carpetas (una por entidad) con los 20 requests listos para ejecutar.
