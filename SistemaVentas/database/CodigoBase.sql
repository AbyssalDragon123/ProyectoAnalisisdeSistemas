-- Desactivar las restricciones temporalmente para permitir la creación de tablas con claves foráneas
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema tecno_ventas
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `tecno_ventas` DEFAULT CHARACTER SET utf8mb4;

-- Seleccionar el esquema 

USE `tecno_ventas`;

-- -----------------------------------------------------
-- Table `tecno_ventas`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tecno_ventas`.`usuario` (
  `idusuario` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `apellido` VARCHAR(45) NOT NULL,
  `correo` VARCHAR(45) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idusuario`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4;

-- -----------------------------------------------------
-- Table `tecno_ventas`.`categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tecno_ventas`.`categoria` (
  `idcategoria` INT NOT NULL AUTO_INCREMENT,
  `nombre_cat` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(256) NULL,
  PRIMARY KEY (`idcategoria`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4;

-- -----------------------------------------------------
-- Table `tecno_ventas`.`cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tecno_ventas`.`cliente` (
  `id_cliente` INT(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` INT NOT NULL,  -- fk
  `nombre` VARCHAR(100) NOT NULL,
  `apellido` VARCHAR(100) NOT NULL,
  `direccion` VARCHAR(45) NULL,
  `telefono` VARCHAR(45) NULL,
  `correo` VARCHAR(45) NULL,
  `nit` VARCHAR(13) NULL,
  
  PRIMARY KEY (`id_cliente`),
  INDEX `fk_cliente_usuario1_idx` (`id_usuario` ASC),
  CONSTRAINT `fk_cliente_usuario1`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `tecno_ventas`.`usuario` (`idusuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4;

-- -----------------------------------------------------
-- Table `tecno_ventas`.`factura`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tecno_ventas`.`factura` (
  `id_factura` INT(11) NOT NULL AUTO_INCREMENT,
  `fecha_factura` DATE NULL DEFAULT NULL,
  `id_cliente` INT(11) NULL DEFAULT NULL,
  `id_usuario` INT NOT NULL,
  PRIMARY KEY (`id_factura`),
  INDEX `id_cliente` (`id_cliente` ASC),
  INDEX `fk_factura_usuario1_idx` (`id_usuario` ASC),
  CONSTRAINT `factura_ibfk_1`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `tecno_ventas`.`cliente` (`id_cliente`),
  CONSTRAINT `fk_factura_usuario1`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `tecno_ventas`.`usuario` (`idusuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4;

-- -----------------------------------------------------
-- Table `tecno_ventas`.`producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tecno_ventas`.`producto` (
  `id_producto` INT(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` INT NOT NULL,
  `id_categoria` INT NOT NULL,
  `nombre` VARCHAR(100) NULL DEFAULT NULL,
  `precio_venta` DECIMAL(10,2) NULL DEFAULT NULL,
  `stock` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id_producto`),
  INDEX `fk_producto_categoria1_idx` (`id_categoria` ASC),
  INDEX `fk_producto_usuario1_idx` (`id_usuario` ASC),
  CONSTRAINT `fk_producto_categoria1`
    FOREIGN KEY (`id_categoria`)
    REFERENCES `tecno_ventas`.`categoria` (`idcategoria`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_producto_usuario1`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `tecno_ventas`.`usuario` (`idusuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4;

-- -----------------------------------------------------
-- Table `tecno_ventas`.`detalle_venta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tecno_ventas`.`detalle_venta` (
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
    REFERENCES `tecno_ventas`.`factura` (`id_factura`),
  CONSTRAINT `detallefactura_ibfk_2`
    FOREIGN KEY (`id_producto`)
    REFERENCES `tecno_ventas`.`producto` (`id_producto`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4;

-- Restablecer los valores originales

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;