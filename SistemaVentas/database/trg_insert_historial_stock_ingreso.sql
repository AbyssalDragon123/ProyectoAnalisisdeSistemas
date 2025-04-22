--triguer para registrar movimientos automaticamente cada vez que se realiza un ingreso de producto

CREATE TRIGGER trg_insert_historial_stock_ingreso
ON detalle_ingreso
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO historial_stock (
        id_articulo,
        fecha,
        tipo_movimiento,
        cantidad,
        descripcion
    )
    SELECT 
        i.id_articulo,
        GETDATE(),
        'entrada',
        i.cantidad,
        CONCAT('Ingreso ID: ', i.id_ingreso)
    FROM inserted i;
END;
GO
