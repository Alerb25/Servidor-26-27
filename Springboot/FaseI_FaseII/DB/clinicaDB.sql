CREATE DATABASE Clinica; 
Use Clinica;

CREATE TABLE ESPECIALIDAD (
    id_especialidad INT PRIMARY KEY,
    nombre_especialidad VARCHAR(100),
    descripcion VARCHAR(255)
);

CREATE TABLE MEDICO (
    id_medico INT PRIMARY KEY,
    num_colegiado VARCHAR(50),
    nombre VARCHAR(100),
    apellidos VARCHAR(100),
    telefono VARCHAR(20),
    id_especialidad INT REFERENCES ESPECIALIDAD(id_especialidad)
);

CREATE TABLE HABITACION_CONSULTA (
    id_habitacion INT PRIMARY KEY,
    codigo_consulta VARCHAR(20),
    planta VARCHAR(20),
    bloque_hospital VARCHAR(50)
);

CREATE TABLE PACIENTE (
    id_paciente INT PRIMARY KEY,
    nombre_completo VARCHAR(150),
    fecha_nacimiento DATE,
    direccion_residencia VARCHAR(200),
    grupo_sanguineo VARCHAR(10),
    telefono_emergencia VARCHAR(20),
    num_afiliacion_ss VARCHAR(50),
    cip_autonomico VARCHAR(50)
);

CREATE TABLE HISTORIA_CLINICA_DIGITAL (
    id_historia INT PRIMARY KEY,
    num_historia VARCHAR(50),
    antecedentes_personales TEXT,
    alergias_conocidas TEXT,
    fecha_apertura DATE,
    id_paciente INT UNIQUE REFERENCES PACIENTE(id_paciente)
);

CREATE TABLE CITA_MEDICA (
    id_cita INT PRIMARY KEY,
    motivo_consulta VARCHAR(255),
    estado_cita VARCHAR(50),
    observaciones_previas TEXT,
    duracion_minutos INT,
    costo_estimado DECIMAL(10,2),
    fecha_hora DATETIME,
    id_paciente INT REFERENCES PACIENTE(id_paciente),
    id_medico INT REFERENCES MEDICO(id_medico),
    id_habitacion INT REFERENCES HABITACION_CONSULTA(id_habitacion)
);

CREATE TABLE TRATAMIENTO_PROCEDIMIENTO (
    id_tratamiento INT PRIMARY KEY,
    nombre_procedimiento VARCHAR(100),
    descripcion TEXT,
    protocolo_estandar TEXT
);

CREATE TABLE CITA_TRATAMIENTO (
    id_cita INT REFERENCES CITA_MEDICA(id_cita),
    id_tratamiento INT REFERENCES TRATAMIENTO_PROCEDIMIENTO(id_tratamiento),
    PRIMARY KEY (id_cita, id_tratamiento)
);

CREATE TABLE RECETA_ELECTRONICA (
    id_receta INT PRIMARY KEY,
    codigo_receta VARCHAR(50),
    fecha_emision DATE,
    duracion_dias INT,
    instrucciones_posologia TEXT,
    id_cita INT REFERENCES CITA_MEDICA(id_cita)
);

CREATE TABLE ASEGURADORA_MUTUA (
    id_aseguradora INT PRIMARY KEY,
    nombre_entidad VARCHAR(100),
    cif VARCHAR(20),
    tipo_cobertura VARCHAR(50)
);

CREATE TABLE PACIENTE_ASEGURADORA (
    id_paciente INT REFERENCES PACIENTE(id_paciente),
    id_aseguradora INT REFERENCES ASEGURADORA_MUTUA(id_aseguradora),
    PRIMARY KEY (id_paciente, id_aseguradora)
);
