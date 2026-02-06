-- Tabla de planes
CREATE TABLE IF NOT EXISTS planes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    precio REAL NOT NULL,
    duracion INTEGER NOT NULL,  -- en meses
    descripcion TEXT
);

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    contraseña TEXT NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado TEXT DEFAULT 'activo',
    id_plan INTEGER,
    FOREIGN KEY (id_plan) REFERENCES planes(id)
);

-- Tabla de diccionario de virus
CREATE TABLE IF NOT EXISTS diccionario_virus (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_archivo TEXT NOT NULL,
    descripcion TEXT,
    fecha_descubrimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);