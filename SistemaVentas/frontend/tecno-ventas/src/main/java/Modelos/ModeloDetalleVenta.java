package Modelos;

import java.math.BigDecimal;

public class ModeloDetalleVenta {
    private int idDetalle;
    private int idFactura;
    private int idProducto;
    private Integer cantidad; // nullable int
    private BigDecimal precioVenta; // decimal(10,2) en C#

    public ModeloDetalleVenta() {} //constructor vacio

    public ModeloDetalleVenta(int idFactura, int idProducto, Integer cantidad, BigDecimal precioVenta) {
        
        this.idFactura = idFactura;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
    }

    // Getters y setters
    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }
}