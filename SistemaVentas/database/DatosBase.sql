-- Roles
INSERT INTO rol (nombre, descripcion, estado) VALUES ('Administrador', 'Acceso total al sistema', 1);
INSERT INTO rol (nombre, descripcion, estado) VALUES ('Vendedor', 'Puede registrar ventas', 1);

-- Permisos
INSERT INTO permiso (nombre, descripcion) VALUES ('ver_productos', 'Puede ver productos');
INSERT INTO permiso (nombre, descripcion) VALUES ('crear_venta', 'Puede crear ventas');
INSERT INTO permiso (nombre, descripcion) VALUES ('gestion_usuarios', 'Acceso completo a usuarios');

-- Asignar permisos a roles
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES (1, 1);
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES (1, 2);
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES (1, 3);
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES (2, 1);
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES (2, 2);

-- Usuarios
INSERT INTO usuario (id_rol, nombre, tipo_documento, num_documento, direccion, telefono, email, password, estado)
VALUES (1, 'Carlos Admin', 'DNI', '12345678', 'Calle Principal 123', '999999999', 'admin@tecno.com', CONVERT(VARBINARY, 'admin123'), 1);

INSERT INTO usuario (id_rol, nombre, tipo_documento, num_documento, direccion, telefono, email, password, estado)
VALUES (2, 'Lucía Vendedora', 'DNI', '87654321', 'Av. Secundaria 456', '988888888', 'lucia@tecno.com', CONVERT(VARBINARY, 'venta123'), 1);

-- Categorías
INSERT INTO categoria (nombre, descripcion, estado) VALUES ('Teclados', 'Teclados de diferentes tipos', 1);
INSERT INTO categoria (nombre, descripcion, estado) VALUES ('Mouse', 'Ratones ópticos, gamer y más', 1);
INSERT INTO categoria (nombre, descripcion, estado) VALUES ('Monitores', 'Monitores LED y LCD', 1);

-- Marcas
INSERT INTO marca (nombre, estado) VALUES ('Logitech', 1);
INSERT INTO marca (nombre, estado) VALUES ('HP', 1);
INSERT INTO marca (nombre, estado) VALUES ('Dell', 1);

-- Artículos
INSERT INTO articulo (id_categoria, id_marca, codigo, nombre, precio_venta, stock, descripcion, estado)
VALUES (1, 1, 'TEC001', 'Teclado Logitech K120', 59.90, 50, 'Teclado clásico USB', 1);

INSERT INTO articulo (id_categoria, id_marca, codigo, nombre, precio_venta, stock, descripcion, estado)
VALUES (2, 2, 'MOU002', 'Mouse HP X1000', 39.90, 40, 'Mouse óptico con cable', 1);

INSERT INTO articulo (id_categoria, id_marca, codigo, nombre, precio_venta, stock, descripcion, estado)
VALUES (3, 3, 'MON003', 'Monitor Dell 22\"', 899.00, 15, 'Monitor LED 22 pulgadas', 1);

-- Personas
INSERT INTO persona (tipo_persona, nombre, tipo_documento, num_documento, direccion, telefono, email)
VALUES ('Proveedor', 'Distribuidora Tech', 'RUC', '10455678901', 'Av. Proveedores 321', '912345678', 'contacto@tech.com');

INSERT INTO persona (tipo_persona, nombre, tipo_documento, num_documento, direccion, telefono, email)
VALUES ('Cliente', 'Pedro Pérez', 'DNI', '45678912', 'Jr. Lima 789', '987654321', 'pedro@gmail.com');

-- Ingreso (entrada de stock)
INSERT INTO ingreso (id_proveedor, id_usuario, fecha, impuesto, total, estado)
VALUES (1, 1, GETDATE(), 18.00, 1999.00, 'Aceptado');

-- Detalle ingreso
INSERT INTO detalle_ingreso (id_ingreso, id_articulo, cantidad, precio) VALUES (1, 1, 30, 45.00);
INSERT INTO detalle_ingreso (id_ingreso, id_articulo, cantidad, precio) VALUES (1, 2, 20, 30.00);

-- Venta
INSERT INTO venta (id_cliente, id_usuario, tipo_comprobante, serie_comprobante, num_comprobante, fecha_hora, impuesto, total, estado)
VALUES (2, 2, 'Boleta', 'B001', '00012345', GETDATE(), 18.00, 150.00, 'Aceptado');

-- Detalle venta
INSERT INTO detalle_venta (id_venta, id_articulo, cantidad, precio, descuento)
VALUES (1, 1, 2, 59.90, 0.00);

INSERT INTO detalle_venta (id_venta, id_articulo, cantidad, precio, descuento)
VALUES (1, 2, 1, 39.90, 0.00);

-- Historial de stock
INSERT INTO historial_stock (id_articulo, fecha, tipo_movimiento, cantidad, descripcion)
VALUES (1, GETDATE(), 'entrada', 30, 'Ingreso inicial de stock');

INSERT INTO historial_stock (id_articulo, fecha, tipo_movimiento, cantidad, descripcion)
VALUES (1, GETDATE(), 'salida', 2, 'Venta a cliente');

