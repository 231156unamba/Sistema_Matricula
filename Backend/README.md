# Sistema de Matrículas - Backend

Backend del sistema de matrículas desarrollado con Spring Boot.

## Requisitos

- Java 17 o superior
- Maven 3.6+
- MySQL 8.0+

## Configuración

1. Crear la base de datos en MySQL:
```sql
CREATE DATABASE Sistematricula;
```

2. Ejecutar el script `proyecto/bd.sql` para crear las tablas

3. Configurar las credenciales de MySQL en `src/main/resources/application.properties`:
```properties
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

## Ejecutar

```bash
mvn spring-boot:run
```

El servidor estará disponible en `http://localhost:8080`

## Endpoints API

### Autenticación

- `POST /api/auth/login/ingresante` - Login para ingresantes (DNI + voucher)
- `POST /api/auth/login/regular` - Login para regulares (código + voucher)

### Matrículas

- `POST /api/matriculas/ingresante` - Matrícula de ingresante
- `POST /api/matriculas/regular` - Matrícula de regular

### Cursos

- `GET /api/cursos` - Listar todos los cursos
- `GET /api/cursos/semestre/{semestre}` - Cursos por semestre
- `GET /api/cursos/disponibles/{idEstudiante}/{semestre}` - Cursos disponibles para estudiante

### Estudiantes

- `GET /api/estudiantes` - Listar todos
- `GET /api/estudiantes/{id}` - Obtener por ID
- `GET /api/estudiantes/codigo/{codigo}` - Obtener por código
- `PUT /api/estudiantes/{id}/estado` - Actualizar estado
- `PUT /api/estudiantes/{id}/creditos` - Actualizar créditos

## Reglas de Negocio Implementadas

### Matrícula Ingresante
- Login con DNI + voucher validado
- Subir 6 documentos obligatorios
- Matrícula automática en cursos del 1er semestre
- Asignación automática de código de estudiante

### Matrícula Regular
- Login con código + voucher validado
- Validación de prerrequisitos
- Límite de créditos según historial
- Control de veces llevado un curso:
  - 1 vez jalado: créditos normales
  - 2 veces jalado: máximo 12 créditos
  - 3 veces jalado: solo ese curso
  - 4 veces jalado: inhabilitado
