-- Datos de ejemplo para H2 Database

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
('Matrícula 2024-I', 'Inicio de matrícula para el semestre 2024-I del 15 al 30 de enero', 'MATRICULA', '2024-01-15 08:00:00', '2024-01-30 18:00:00'),
('Horario de Atención', 'Oficina de Registros: Lunes a Viernes 8:00 AM - 5:00 PM', 'HORARIO', NULL, NULL),
('Examen de Admisión', 'Examen de admisión programado para el 10 de febrero 2024', 'EXAMEN', '2024-02-10 09:00:00', '2024-02-10 13:00:00');
