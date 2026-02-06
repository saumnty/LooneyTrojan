CREATE DATABASE looney_troyans;
USE looney_troyans;

-- Crear tabla de planes
CREATE TABLE planes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    duracion INT NOT NULL,  -- en meses
    descripcion TEXT
);

-- Crear tabla de usuarios
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('activo', 'inactivo') DEFAULT 'activo',
    id_plan INT,
    FOREIGN KEY (id_plan) REFERENCES planes(id)
);

-- Crear tabla de diccionario de virus
CREATE TABLE diccionario_virus (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_archivo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    fecha_descubrimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);