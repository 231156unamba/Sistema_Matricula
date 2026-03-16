CREATE database Sistematricula;
use Sistematricula;
CREATE TABLE estudiantes (
    id_estudiante INT AUTO_INCREMENT PRIMARY KEY,
    codigo_estudiante VARCHAR(20) UNIQUE,
    dni VARCHAR(8) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    tipo ENUM('INGRESANTE','REGULAR') NOT NULL,
    estado ENUM('ACTIVO','INHABILITADO','RETIRADO') DEFAULT 'ACTIVO',
    creditos_maximos INT DEFAULT 23,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE cursos (
    id_curso INT AUTO_INCREMENT PRIMARY KEY,
    codigo_curso VARCHAR(10) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    creditos INT NOT NULL,
    semestre INT NOT NULL
); CREATE TABLE prerrequisitos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_curso INT,
    id_curso_prerrequisito INT,
    FOREIGN KEY (id_curso) REFERENCES cursos(id_curso),
    FOREIGN KEY (id_curso_prerrequisito) REFERENCES cursos(id_curso)
);
CREATE TABLE periodos_academicos (
    id_periodo INT AUTO_INCREMENT PRIMARY KEY,
    anio INT NOT NULL,
    semestre ENUM('I','II') NOT NULL,
    estado ENUM('ABIERTO','CERRADO') DEFAULT 'ABIERTO'
);
CREATE TABLE matriculas (
    id_matricula INT AUTO_INCREMENT PRIMARY KEY,
    id_estudiante INT,
    id_periodo INT,
    tipo ENUM('INGRESANTE','REGULAR'),
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id_estudiante),
    FOREIGN KEY (id_periodo) REFERENCES periodos_academicos(id_periodo)
);
CREATE TABLE detalle_matricula (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_matricula INT,
    id_curso INT,
    veces_llevado INT DEFAULT 1,
    nota_final DECIMAL(4,2),
    estado ENUM('EN_CURSO','APROBADO','DESAPROBADO'),
    FOREIGN KEY (id_matricula) REFERENCES matriculas(id_matricula),
    FOREIGN KEY (id_curso) REFERENCES cursos(id_curso)
);
CREATE TABLE documentos_ingresante (
    id_documento INT AUTO_INCREMENT PRIMARY KEY,
    id_estudiante INT,
    declaracion_jurada VARCHAR(255),
    certificado_estudios VARCHAR(255),
    boleta_matricula VARCHAR(255),
    boleta_examen VARCHAR(255),
    pago_centro_medico VARCHAR(255),
    hoja_matricula VARCHAR(255),
    verificado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id_estudiante)
);
CREATE TABLE pagos (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_estudiante INT,
    tipo_pago ENUM('MATRICULA','EXAMEN','CENTRO_MEDICO'),
    voucher VARCHAR(50),
    monto DECIMAL(8,2),
    fecha_pago DATE,
    validado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id_estudiante)
);

CREATE TABLE anuncios (
    id_anuncio INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    contenido TEXT NOT NULL,
    tipo ENUM('MATRICULA','EXAMEN','EVENTO','COMUNICADO','HORARIO') NOT NULL,
    fecha_inicio DATETIME,
    fecha_fin DATETIME,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE administradores (
    id_admin INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    rol ENUM('SUPER_ADMIN','ADMIN','REGISTRADOR') DEFAULT 'ADMIN',
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);
-- Datos de ejemplo
INSERT INTO anuncios (titulo, contenido, tipo, fecha_inicio, fecha_fin) VALUES
('Matrícula 2024-I', 'Inicio de matrícula para el semestre 2024-I del 15 al 30 de enero', 'MATRICULA', '2024-01-15 08:00:00', '2024-01-30 18:00:00'),
('Horario de Atención', 'Oficina de Registros: Lunes a Viernes 8:00 AM - 5:00 PM', 'HORARIO', NULL, NULL),
('Examen de Admisión', 'Examen de admisión programado para el 10 de febrero 2024', 'EXAMEN', '2024-02-10 09:00:00', '2024-02-10 13:00:00');



-- Administradores (password: admin123 para todos)
INSERT INTO administradores (usuario, password, nombres, apellidos, email, rol) VALUES
('admin', 'admin123', 'Carlos', 'Rodríguez', 'admin@unamba.edu.pe', 'SUPER_ADMIN'),
('registrador1', 'admin123', 'María', 'López', 'mlopez@unamba.edu.pe', 'REGISTRADOR'),
('registrador2', 'admin123', 'Juan', 'Pérez', 'jperez@unamba.edu.pe', 'ADMIN');

-- Periodos Académicos
INSERT INTO periodos_academicos (anio, semestre, estado) VALUES
(2024, 'I', 'ABIERTO'),
(2023, 'II', 'CERRADO'),
(2023, 'I', 'CERRADO');

-- Cursos de Primer Semestre
INSERT INTO cursos (codigo_curso, nombre, creditos, semestre) VALUES
('MAT101', 'Cálculo I', 4, 1),
('FIS101', 'Física I', 4, 1),
('QUI101', 'Química General', 3, 1),
('COM101', 'Comunicación I', 3, 1),
('INF101', 'Introducción a la Programación', 4, 1),
('MAT102', 'Álgebra Lineal', 3, 1);

-- Cursos de Segundo Semestre
INSERT INTO cursos (codigo_curso, nombre, creditos, semestre) VALUES
('MAT201', 'Cálculo II', 4, 2),
('FIS201', 'Física II', 4, 2),
('INF201', 'Programación Orientada a Objetos', 4, 2),
('EST201', 'Estadística I', 3, 2),
('COM201', 'Comunicación II', 3, 2);

-- Cursos de Tercer Semestre
INSERT INTO cursos (codigo_curso, nombre, creditos, semestre) VALUES
('MAT301', 'Cálculo III', 4, 3),
('INF301', 'Estructura de Datos', 4, 3),
('BD301', 'Base de Datos I', 4, 3),
('ALG301', 'Análisis de Algoritmos', 3, 3);

-- Cursos de Cuarto Semestre
INSERT INTO cursos (codigo_curso, nombre, creditos, semestre) VALUES
('INF401', 'Ingeniería de Software I', 4, 4),
('BD401', 'Base de Datos II', 4, 4),
('WEB401', 'Desarrollo Web', 4, 4),
('ARQ401', 'Arquitectura de Software', 3, 4);

-- Prerrequisitos
INSERT INTO prerrequisitos (id_curso, id_curso_prerrequisito) VALUES
-- Cálculo II requiere Cálculo I
(7, 1),
-- Física II requiere Física I
(8, 2),
-- POO requiere Introducción a la Programación
(9, 5),
-- Cálculo III requiere Cálculo II
(12, 7),
-- Estructura de Datos requiere POO
(13, 9),
-- Base de Datos I requiere POO
(14, 9),
-- Ingeniería de Software requiere Estructura de Datos
(16, 13),
-- Base de Datos II requiere Base de Datos I
(17, 14);

-- Estudiantes Regulares
INSERT INTO estudiantes (codigo_estudiante, dni, nombres, apellidos, tipo, estado, creditos_maximos) VALUES
('231001', '72345678', 'Ana', 'García Mendoza', 'REGULAR', 'ACTIVO', 23),
('231002', '73456789', 'Luis', 'Torres Quispe', 'REGULAR', 'ACTIVO', 23),
('231003', '74567890', 'Carmen', 'Flores Huamán', 'REGULAR', 'ACTIVO', 12),
('222001', '71234567', 'Pedro', 'Mamani Cruz', 'REGULAR', 'ACTIVO', 23),
('222002', '70123456', 'Rosa', 'Ccama Yupanqui', 'REGULAR', 'ACTIVO', 23);

-- Estudiantes Ingresantes (sin código aún)
INSERT INTO estudiantes (dni, nombres, apellidos, tipo, estado) VALUES
('75678901', 'Miguel', 'Condori Apaza', 'INGRESANTE', 'ACTIVO'),
('76789012', 'Julia', 'Huanca Quispe', 'INGRESANTE', 'ACTIVO');

-- Pagos Validados
INSERT INTO pagos (id_estudiante, tipo_pago, voucher, monto, fecha_pago, validado) VALUES
(1, 'MATRICULA', 'VCH001234', 150.00, '2024-01-10', TRUE),
(2, 'MATRICULA', 'VCH001235', 150.00, '2024-01-11', TRUE),
(3, 'MATRICULA', 'VCH001236', 150.00, '2024-01-12', TRUE),
(4, 'MATRICULA', 'VCH001237', 150.00, '2024-01-13', TRUE),
(5, 'MATRICULA', 'VCH001238', 150.00, '2024-01-14', TRUE),
(6, 'MATRICULA', 'VCH001239', 150.00, '2024-01-15', TRUE),
(6, 'EXAMEN', 'VCH001240', 100.00, '2024-01-05', TRUE),
(7, 'MATRICULA', 'VCH001241', 150.00, '2024-01-16', TRUE),
(7, 'EXAMEN', 'VCH001242', 100.00, '2024-01-06', TRUE);

-- Matrículas del periodo 2024-I
INSERT INTO matriculas (id_estudiante, id_periodo, tipo) VALUES
(1, 1, 'REGULAR'),
(2, 1, 'REGULAR'),
(3, 1, 'REGULAR'),
(4, 1, 'REGULAR'),
(5, 1, 'REGULAR');

-- Detalle de Matrícula - Estudiante 1 (Ana García) - Segundo Semestre
INSERT INTO detalle_matricula (id_matricula, id_curso, veces_llevado, estado) VALUES
(1, 7, 1, 'EN_CURSO'),  -- Cálculo II
(1, 8, 1, 'EN_CURSO'),  -- Física II
(1, 9, 1, 'EN_CURSO'),  -- POO
(1, 10, 1, 'EN_CURSO'), -- Estadística I
(1, 11, 1, 'EN_CURSO'); -- Comunicación II

-- Detalle de Matrícula - Estudiante 2 (Luis Torres) - Segundo Semestre
INSERT INTO detalle_matricula (id_matricula, id_curso, veces_llevado, estado) VALUES
(2, 7, 1, 'EN_CURSO'),
(2, 8, 1, 'EN_CURSO'),
(2, 9, 1, 'EN_CURSO'),
(2, 10, 1, 'EN_CURSO');

-- Detalle de Matrícula - Estudiante 3 (Carmen Flores) - Solo 12 créditos (jaló 2 veces)
INSERT INTO detalle_matricula (id_matricula, id_curso, veces_llevado, estado) VALUES
(3, 7, 3, 'EN_CURSO'),  -- Cálculo II (3ra vez)
(3, 9, 2, 'EN_CURSO');  -- POO (2da vez)

-- Detalle de Matrícula - Estudiante 4 (Pedro Mamani) - Tercer Semestre
INSERT INTO detalle_matricula (id_matricula, id_curso, veces_llevado, estado) VALUES
(4, 12, 1, 'EN_CURSO'), -- Cálculo III
(4, 13, 1, 'EN_CURSO'), -- Estructura de Datos
(4, 14, 1, 'EN_CURSO'), -- Base de Datos I
(4, 15, 1, 'EN_CURSO'); -- Análisis de Algoritmos

-- Detalle de Matrícula - Estudiante 5 (Rosa Ccama) - Cuarto Semestre
INSERT INTO detalle_matricula (id_matricula, id_curso, veces_llevado, estado) VALUES
(5, 16, 1, 'EN_CURSO'), -- Ingeniería de Software
(5, 17, 1, 'EN_CURSO'), -- Base de Datos II
(5, 18, 1, 'EN_CURSO'), -- Desarrollo Web
(5, 19, 1, 'EN_CURSO'); -- Arquitectura de Software

-- Historial académico del periodo anterior (2023-II)
-- Estudiante 1 - Primer Semestre (todos aprobados)
INSERT INTO detalle_matricula (id_matricula, id_curso, veces_llevado, nota_final, estado) VALUES
(1, 1, 1, 14.5, 'APROBADO'),
(1, 2, 1, 13.0, 'APROBADO'),
(1, 3, 1, 15.5, 'APROBADO'),
(1, 4, 1, 16.0, 'APROBADO'),
(1, 5, 1, 17.0, 'APROBADO'),
(1, 6, 1, 14.0, 'APROBADO');

-- Estudiante 3 - Historial con jalados
INSERT INTO detalle_matricula (id_matricula, id_curso, veces_llevado, nota_final, estado) VALUES
(3, 7, 1, 9.5, 'DESAPROBADO'),
(3, 7, 2, 10.0, 'DESAPROBADO'),
(3, 9, 1, 8.0, 'DESAPROBADO');

-- Anuncios
INSERT INTO anuncios (titulo, contenido, tipo, fecha_inicio, fecha_fin) VALUES
('Matrícula 2024-I', 'Inicio de matrícula para el semestre 2024-I del 15 al 30 de enero. Todos los estudiantes deben realizar su matrícula en las fechas establecidas.', 'MATRICULA', '2024-01-15 08:00:00', '2024-01-30 18:00:00'),
('Horario de Atención', 'Oficina de Registros Académicos: Lunes a Viernes de 8:00 AM a 5:00 PM. Sábados de 9:00 AM a 1:00 PM.', 'HORARIO', NULL, NULL),
('Examen de Admisión 2024', 'Examen de admisión programado para el 10 de febrero 2024. Inscripciones abiertas hasta el 5 de febrero.', 'EXAMEN', '2024-02-10 09:00:00', '2024-02-10 13:00:00'),
('Ceremonia de Graduación', 'Se invita a todos los egresados a la ceremonia de graduación que se realizará el 20 de marzo en el auditorio principal.', 'EVENTO', '2024-03-20 10:00:00', NULL),
('Comunicado Importante', 'Se informa a la comunidad universitaria que el día 15 de febrero no habrá atención por feriado nacional.', 'COMUNICADO', NULL, NULL),
('Inicio de Clases', 'Las clases del semestre 2024-I iniciarán el 5 de febrero. Se solicita puntualidad.', 'COMUNICADO', '2024-02-05 08:00:00', NULL);

-- ============================================
-- DATOS PARA PRUEBAS
-- ============================================
-- Usuarios Admin:
--   usuario: admin, password: admin123 (SUPER_ADMIN)
--   usuario: registrador1, password: admin123 (REGISTRADOR)
--   usuario: registrador2, password: admin123 (ADMIN)
--
-- Estudiantes Regulares (para login):
--   codigo: 231001, voucher: VCH001234 (Ana García - 2do semestre)
--   codigo: 231002, voucher: VCH001235 (Luis Torres - 2do semestre)
--   codigo: 231003, voucher: VCH001236 (Carmen Flores - 12 créditos)
--   codigo: 222001, voucher: VCH001237 (Pedro Mamani - 3er semestre)
--   codigo: 222002, voucher: VCH001238 (Rosa Ccama - 4to semestre)
--
-- Estudiantes Ingresantes (para login):
--   dni: 75678901, voucher: VCH001239 (Miguel Condori)
--   dni: 76789012, voucher: VCH001241 (Julia Huanca)
-- ============================================

