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

INSERT INTO anuncios (titulo, contenido, tipo, fecha_inicio, fecha_fin) VALUES
('Matrícula 2024-I', 'Inicio de matrícula para el semestre 2024-I del 15 al 30 de enero', 'MATRICULA', '2024-01-15 08:00:00', '2024-01-30 18:00:00'),
('Horario de Atención', 'Oficina de Registros: Lunes a Viernes 8:00 AM - 5:00 PM', 'HORARIO', NULL, NULL),
('Examen de Admisión', 'Examen de admisión programado para el 10 de febrero 2024', 'EXAMEN', '2024-02-10 09:00:00', '2024-02-10 13:00:00');
