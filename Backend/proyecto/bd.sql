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
    carrera VARCHAR(100),
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
INSERT INTO estudiantes (codigo_estudiante, dni, nombres, apellidos, tipo, estado, creditos_maximos, carrera) VALUES
('231001', '72345678', 'Ana', 'García Mendoza', 'REGULAR', 'ACTIVO', 23, 'Ingeniería de Sistemas'),
('231002', '73456789', 'Luis', 'Torres Quispe', 'REGULAR', 'ACTIVO', 23, 'Ingeniería Civil'),
('231003', '74567890', 'Carmen', 'Flores Huamán', 'REGULAR', 'ACTIVO', 12, 'Administración de Empresas'),
('222001', '71234567', 'Pedro', 'Mamani Cruz', 'REGULAR', 'ACTIVO', 23, 'Ingeniería de Sistemas'),
('222002', '70123456', 'Rosa', 'Ccama Yupanqui', 'REGULAR', 'ACTIVO', 23, 'Derecho');

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



SET SQL_SAFE_UPDATES = 0;
DELETE FROM prerrequisitos;
SET SQL_SAFE_UPDATES = 1;
-- 1. Limpiar prerrequisitos viejos
DELETE FROM prerrequisitos;

-- 2. Agregar cursos nuevos (solo si no existen)
INSERT IGNORE INTO cursos (codigo_curso, nombre, creditos, semestre) VALUES
('INF501', 'Ingeniería de Software II', 4, 5),
('RED501', 'Redes de Computadoras', 4, 5),
('SO501',  'Sistemas Operativos', 4, 5),
('MAT501', 'Matemática Discreta', 3, 5),
('INF502', 'Inteligencia Artificial', 3, 5),
('INF601', 'Desarrollo de Aplicaciones Móviles', 4, 6),
('SEG601', 'Seguridad Informática', 4, 6),
('INF602', 'Minería de Datos', 4, 6),
('GES601', 'Gestión de Proyectos TI', 3, 6),
('INF603', 'Computación en la Nube', 3, 6),
('INF701', 'Sistemas Distribuidos', 4, 7),
('INF702', 'Visión por Computadora', 3, 7),
('INF703', 'Procesamiento de Lenguaje Natural', 3, 7),
('INF704', 'Blockchain y Tecnologías Emergentes', 3, 7),
('INF705', 'Auditoría de Sistemas', 3, 7),
('INF801', 'Tesis I', 4, 8),
('INF802', 'Práctica Pre-Profesional', 4, 8),
('INF803', 'Emprendimiento Tecnológico', 3, 8),
('CIV501', 'Mecánica de Suelos', 4, 5),
('CIV502', 'Estructuras I', 4, 5),
('CIV503', 'Hidráulica', 4, 5),
('CIV504', 'Topografía Avanzada', 3, 5),
('CIV601', 'Estructuras II', 4, 6),
('CIV602', 'Diseño de Pavimentos', 4, 6),
('CIV603', 'Ingeniería Sanitaria', 3, 6),
('CIV701', 'Diseño Sísmico', 4, 7),
('CIV702', 'Gestión de Obras', 3, 7),
('CIV801', 'Tesis Civil I', 4, 8),
('ADM501', 'Marketing Estratégico', 4, 5),
('ADM502', 'Finanzas Corporativas', 4, 5),
('ADM503', 'Gestión del Talento Humano', 3, 5),
('ADM601', 'Comercio Internacional', 4, 6),
('ADM602', 'Contabilidad Gerencial', 4, 6),
('ADM701', 'Plan de Negocios', 4, 7),
('ADM702', 'Derecho Empresarial', 3, 7),
('ADM801', 'Tesis Administración I', 4, 8);

-- 3. Insertar prerrequisitos usando subqueries (IDs exactos)
INSERT INTO prerrequisitos (id_curso, id_curso_prerrequisito) VALUES
((SELECT id_curso FROM cursos WHERE codigo_curso='MAT201'),(SELECT id_curso FROM cursos WHERE codigo_curso='MAT101')),
((SELECT id_curso FROM cursos WHERE codigo_curso='FIS201'),(SELECT id_curso FROM cursos WHERE codigo_curso='FIS101')),
((SELECT id_curso FROM cursos WHERE codigo_curso='INF201'),(SELECT id_curso FROM cursos WHERE codigo_curso='INF101')),
((SELECT id_curso FROM cursos WHERE codigo_curso='MAT301'),(SELECT id_curso FROM cursos WHERE codigo_curso='MAT201')),
((SELECT id_curso FROM cursos WHERE codigo_curso='INF301'),(SELECT id_curso FROM cursos WHERE codigo_curso='INF201')),
((SELECT id_curso FROM cursos WHERE codigo_curso='BD301'), (SELECT id_curso FROM cursos WHERE codigo_curso='INF201')),
((SELECT id_curso FROM cursos WHERE codigo_curso='ALG301'),(SELECT id_curso FROM cursos WHERE codigo_curso='INF301')),
((SELECT id_curso FROM cursos WHERE codigo_curso='INF401'),(SELECT id_curso FROM cursos WHERE codigo_curso='INF301')),
((SELECT id_curso FROM cursos WHERE codigo_curso='BD401'), (SELECT id_curso FROM cursos WHERE codigo_curso='BD301')),
((SELECT id_curso FROM cursos WHERE codigo_curso='WEB401'),(SELECT id_curso FROM cursos WHERE codigo_curso='INF201')),
((SELECT id_curso FROM cursos WHERE codigo_curso='ARQ401'),(SELECT id_curso FROM cursos WHERE codigo_curso='INF401')),
((SELECT id_curso FROM cursos WHERE codigo_curso='INF501'),(SELECT id_curso FROM cursos WHERE codigo_curso='INF401')),
((SELECT id_curso FROM cursos WHERE codigo_curso='INF502'),(SELECT id_curso FROM cursos WHERE codigo_curso='INF301')),
((SELECT id_curso FROM cursos WHERE codigo_curso='INF601'),(SELECT id_curso FROM cursos WHERE codigo_curso='WEB401')),
((SELECT id_curso FROM cursos WHERE codigo_curso='INF602'),(SELECT id_curso FROM cursos WHERE codigo_curso='BD401')),
((SELECT id_curso FROM cursos WHERE codigo_curso='INF701'),(SELECT id_curso FROM cursos WHERE codigo_curso='RED501')),
((SELECT id_curso FROM cursos WHERE codigo_curso='INF702'),(SELECT id_curso FROM cursos WHERE codigo_curso='INF502')),
((SELECT id_curso FROM cursos WHERE codigo_curso='CIV601'),(SELECT id_curso FROM cursos WHERE codigo_curso='CIV502')),
((SELECT id_curso FROM cursos WHERE codigo_curso='CIV701'),(SELECT id_curso FROM cursos WHERE codigo_curso='CIV601')),
((SELECT id_curso FROM cursos WHERE codigo_curso='ADM502'),(SELECT id_curso FROM cursos WHERE codigo_curso='EST201')),
((SELECT id_curso FROM cursos WHERE codigo_curso='ADM701'),(SELECT id_curso FROM cursos WHERE codigo_curso='ADM502'));


SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'Sistematricula' AND TABLE_NAME = 'cursos' AND COLUMN_NAME = 'carrera';
ALTER TABLE cursos ADD COLUMN carrera VARCHAR(100);
USE Sistematricula;
SET SQL_SAFE_UPDATES = 0;

UPDATE cursos SET carrera = 'COMUN' WHERE codigo_curso IN ('MAT101','FIS101','QUI101','COM101','INF101','MAT102','MAT201','FIS201','INF201','EST201','COM201','MAT301','INF301','BD301','ALG301','INF401','BD401','WEB401','ARQ401');

UPDATE cursos SET carrera = 'Ingeniería de Sistemas' WHERE codigo_curso IN ('INF501','RED501','SO501','MAT501','INF502','INF601','SEG601','INF602','GES601','INF603','INF701','INF702','INF703','INF704','INF705','INF801','INF802','INF803');

UPDATE cursos SET carrera = 'Ingeniería Civil' WHERE codigo_curso IN ('CIV501','CIV502','CIV503','CIV504','CIV601','CIV602','CIV603','CIV701','CIV702','CIV801');

UPDATE cursos SET carrera = 'Administración de Empresas' WHERE codigo_curso IN ('ADM501','ADM502','ADM503','ADM601','ADM602','ADM701','ADM702','ADM801');

SET SQL_SAFE_UPDATES = 1;


-- Ver qué cursos tiene el estudiante 1 y su estado
SELECT e.nombres, e.carrera, dm.estado, dm.nota_final, c.nombre, c.semestre, c.carrera as carrera_curso
FROM estudiantes e
JOIN matriculas m ON m.id_estudiante = e.id_estudiante
JOIN detalle_matricula dm ON dm.id_matricula = m.id_matricula
JOIN cursos c ON c.id_curso = dm.id_curso
WHERE e.id_estudiante = 1;

-- Ver si los cursos tienen carrera asignada
SELECT codigo_curso, nombre, semestre, carrera FROM cursos ORDER BY semestre;



