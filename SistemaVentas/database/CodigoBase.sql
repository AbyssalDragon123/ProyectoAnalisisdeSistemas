-- Crear base de datos
-- Estilo de escritura snake_case


CREATE DATABASE tecno_soluciones; --ejecutar primero este fragmento de codigo para crear la base de datos
GO

USE tecno_soluciones; --ejecutar este fragmento para seleccionar la base de datos
GO

--Proceder a ejecutar para crear las tablas y sus relaciones

-- Tabla categoria
CREATE TABLE categoria (
    id_categoria INT PRIMARY KEY IDENTITY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(256),
    estado BIT DEFAULT 1 CHECK (estado IN (0,1))
);
GO

-- Tabla marca
CREATE TABLE marca (
    id_marca INT PRIMARY KEY IDENTITY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    estado BIT DEFAULT 1 CHECK (estado IN (0,1))
);
GO

-- Tabla articulo
CREATE TABLE articulo (
    id_articulo INT PRIMARY KEY IDENTITY,
    id_categoria INT NOT NULL,
    id_marca INT NOT NULL,
    codigo VARCHAR(50),
    nombre VARCHAR(100) NOT NULL UNIQUE,
    precio_venta DECIMAL(11,2) NOT NULL,
    stock INT NOT NULL,
    descripcion VARCHAR(256),
    estado BIT DEFAULT 1 CHECK (estado IN (0,1)),
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria),
    FOREIGN KEY (id_marca) REFERENCES marca(id_marca) ON UPDATE CASCADE
);
GO

-- Tabla persona (cliente o proveedor) / en esta tabla se maneja la información tanto del cliente como del proveedor
CREATE TABLE persona (
    id_persona INT PRIMARY KEY IDENTITY,
    tipo_persona VARCHAR(20) NOT NULL CHECK (tipo_persona IN ('Cliente', 'Proveedor')),
    nombre VARCHAR(100) NOT NULL,
    tipo_documento VARCHAR(20),
    num_documento VARCHAR(20),
    direccion VARCHAR(70),
    telefono VARCHAR(20),
    email VARCHAR(50)
);
GO

-- Tabla rol
CREATE TABLE rol (
    id_rol INT PRIMARY KEY IDENTITY,
    nombre VARCHAR(30) NOT NULL,
    descripcion VARCHAR(100),
    estado BIT DEFAULT 1 CHECK (estado IN (0,1))
);
GO

-- Tabla usuario
CREATE TABLE usuario (
    id_usuario INT PRIMARY KEY IDENTITY,
    id_rol INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    tipo_documento VARCHAR(20),
    num_documento VARCHAR(20),
    direccion VARCHAR(70),
    telefono VARCHAR(20),
    email VARCHAR(50) NOT NULL,
    password VARBINARY(MAX) NOT NULL,
    estado BIT DEFAULT 1 CHECK (estado IN (0,1)),
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol) ON UPDATE CASCADE
);
GO

-- Tabla ingreso
CREATE TABLE ingreso (
    id_ingreso INT PRIMARY KEY IDENTITY,
    id_proveedor INT NOT NULL,
    id_usuario INT NOT NULL,
    fecha DATETIME NOT NULL,
    impuesto DECIMAL(4,2) NOT NULL,
    total DECIMAL(11,2) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    FOREIGN KEY (id_proveedor) REFERENCES persona(id_persona) ON UPDATE CASCADE,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON UPDATE CASCADE
);
GO

-- Tabla detalle_ingreso
CREATE TABLE detalle_ingreso (
    id_detalle_ingreso INT PRIMARY KEY IDENTITY,
    id_ingreso INT NOT NULL,
    id_articulo INT NOT NULL,
    cantidad INT NOT NULL,
    precio DECIMAL(11,2) NOT NULL,
    FOREIGN KEY (id_ingreso) REFERENCES ingreso(id_ingreso) ON UPDATE CASCADE,
    FOREIGN KEY (id_articulo) REFERENCES articulo(id_articulo)
);
GO

-- Tabla venta
CREATE TABLE venta (
    id_venta INT PRIMARY KEY IDENTITY,
    id_cliente INT NOT NULL,
    id_usuario INT NOT NULL,
    tipo_comprobante VARCHAR(20) NOT NULL,
    serie_comprobante VARCHAR(7),
    num_comprobante VARCHAR(10) NOT NULL,
    fecha_hora DATETIME NOT NULL,
    impuesto DECIMAL(4,2) NOT NULL,
    total DECIMAL(11,2) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES persona(id_persona) ON UPDATE CASCADE,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON UPDATE CASCADE
);
GO

-- Tabla detalle_venta
CREATE TABLE detalle_venta (
    id_detalle_venta INT PRIMARY KEY IDENTITY,
    id_venta INT NOT NULL,
    id_articulo INT NOT NULL,
    cantidad INT NOT NULL,
    precio DECIMAL(11,2) NOT NULL,
    descuento DECIMAL(11,2) NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES venta(id_venta) ON UPDATE CASCADE,
    FOREIGN KEY (id_articulo) REFERENCES articulo(id_articulo)
);
GO

-- Tabla historial_stock
CREATE TABLE historial_stock (
    id_historial INT PRIMARY KEY IDENTITY,
    id_articulo INT NOT NULL,
    fecha DATETIME DEFAULT GETDATE(),
    tipo_movimiento VARCHAR(10) CHECK (tipo_movimiento IN ('entrada', 'salida')),
    cantidad INT NOT NULL,
    descripcion VARCHAR(255),
    FOREIGN KEY (id_articulo) REFERENCES articulo(id_articulo) ON UPDATE CASCADE
);
GO

-- Tabla permiso
CREATE TABLE permiso (
    id_permiso INT PRIMARY KEY IDENTITY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(100)
);
GO

-- Tabla rol_permiso (relación muchos a muchos)
CREATE TABLE rol_permiso (
    id_rol INT NOT NULL,
    id_permiso INT NOT NULL,
    PRIMARY KEY (id_rol, id_permiso),
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol) ON DELETE CASCADE,
    FOREIGN KEY (id_permiso) REFERENCES permiso(id_permiso) ON DELETE CASCADE
);
GO
