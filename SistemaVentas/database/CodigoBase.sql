-- Crear base de datos

CREATE DATABASE tecno_ventas;

-- Elegir base de datos para trabajar

USE tecno_ventas;

-- Crear la tabla producto

CREATE TABLE IF NOT EXISTS producto (

    id_producto INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    nombre VARCHAR(100),
    precio_venta DECIMAL(10,2),
    stock INT
);

-- Crear tabla cliente

CREATE TABLE IF NOT EXISTS cliente (

    id_cliente INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    nombre VARCHAR(100),
    apellido VARCHAR(100)

);

-- Crear tabla factura

CREATE TABLE IF NOT EXISTS factura(
    
    id_factura INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    fecha_factura DATE,
    id_cliente INT,

    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

-- Crar tabla detalle Factura

CREATE TABLE IF NOT EXISTS detalleFactura(

    id_detalle INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    id_factura INT,
    id_producto INT,
    cantidad    INT,
    precio_venta DECIMAL(10,2),
    
    FOREIGN KEY (id_factura) REFERENCES factura(id_factura),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)

);
