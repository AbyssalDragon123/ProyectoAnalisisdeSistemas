-- Desactivar las restricciones temporalmente
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- Crear esquema
CREATE SCHEMA IF NOT EXISTS `tecno_ventas` DEFAULT CHARACTER SET utf8mb4;
USE `tecno_ventas`;

-- Tabla: usuario (modificada con campo rol)
CREATE TABLE IF NOT EXISTS `usuario` (
  `id_usuario` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `apellido` VARCHAR(45) NOT NULL,
  `correo` VARCHAR(45) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `pass` VARCHAR(255) NOT NULL,
  `rol` ENUM('admin', 'vendedor', 'cajero') NOT NULL DEFAULT 'cajero',
  `password_reset_token` VARCHAR(255) NULL,
  `password_reset_expires` DATETIME NULL,
  PRIMARY KEY (`id_usuario`),
  CHECK (CHAR_LENGTH(`pass`) > 0)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- Tabla: categoria
CREATE TABLE IF NOT EXISTS `categoria` (
  `id_categoria` INT NOT NULL AUTO_INCREMENT,
  `nombre_cat` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(256) NULL,
  `creado_por` INT NULL,
  `fecha_creacion` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_categoria`),
  INDEX `fk_categoria_creado_por_idx` (`creado_por` ASC),
  CONSTRAINT `fk_categoria_creado_por`
    FOREIGN KEY (`creado_por`)
    REFERENCES `usuario` (`id_usuario`)
    ON DELETE SET NULL
    ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- Tabla: cliente
CREATE TABLE IF NOT EXISTS `cliente` (
  `id_cliente` INT(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` INT NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `apellido` VARCHAR(100) NOT NULL,
  `direccion` VARCHAR(45) NULL,
  `telefono` VARCHAR(45) NULL,
  `correo` VARCHAR(45) NULL,
  `nit` VARCHAR(13) NULL,
  `fecha_creacion` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_cliente`),
  INDEX `fk_cliente_usuario1_idx` (`id_usuario` ASC),
  CONSTRAINT `fk_cliente_usuario1`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- Tabla: factura
CREATE TABLE IF NOT EXISTS `factura` (
  `id_factura` INT(11) NOT NULL AUTO_INCREMENT,
  `fecha_factura` DATE NULL DEFAULT NULL,
  `id_cliente` INT(11) NULL DEFAULT NULL,
  `id_usuario` INT NOT NULL,
  PRIMARY KEY (`id_factura`),
  INDEX `id_cliente` (`id_cliente` ASC),
  INDEX `fk_factura_usuario1_idx` (`id_usuario` ASC),
  CONSTRAINT `factura_ibfk_1`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `cliente` (`id_cliente`),
  CONSTRAINT `fk_factura_usuario1`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- Tabla: producto
CREATE TABLE IF NOT EXISTS `producto` (
  `id_producto` INT(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` INT NOT NULL,
  `id_categoria` INT NOT NULL,
  `nombre` VARCHAR(100) NULL DEFAULT NULL,
  `precio_venta` DECIMAL(10,2) NULL DEFAULT NULL,
  `stock` INT(11) NULL DEFAULT NULL,
  `fecha_creacion` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_producto`),
  INDEX `fk_producto_categoria1_idx` (`id_categoria` ASC),
  INDEX `fk_producto_usuario1_idx` (`id_usuario` ASC),
  CONSTRAINT `fk_producto_categoria1`
    FOREIGN KEY (`id_categoria`)
    REFERENCES `categoria` (`id_categoria`),
  CONSTRAINT `fk_producto_usuario1`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- Tabla: detalle_venta
CREATE TABLE IF NOT EXISTS `detalle_venta` (
  `id_detalle` INT(11) NOT NULL AUTO_INCREMENT,
  `id_factura` INT(11) NOT NULL,
  `id_producto` INT(11) NOT NULL,
  `cantidad` INT(11) NULL DEFAULT NULL,
  `precio_venta` DECIMAL(10,2) NULL DEFAULT NULL,
  PRIMARY KEY (`id_detalle`),
  INDEX `id_factura` (`id_factura` ASC),
  INDEX `id_producto` (`id_producto` ASC),
  CONSTRAINT `detallefactura_ibfk_1`
    FOREIGN KEY (`id_factura`)
    REFERENCES `factura` (`id_factura`),
  CONSTRAINT `detallefactura_ibfk_2`
    FOREIGN KEY (`id_producto`)
    REFERENCES `producto` (`id_producto`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- Restaurar los valores anteriores
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
