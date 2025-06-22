package Modelos;

import java.util.Date;
import java.util.List;

public class ModeloFacturaDetalle {

    private int idFactura;
    private Date fechaFactura;
    private String clienteNombre;
    private String usuarioNombre;
    private int estado;
    private List<ModeloDetalleDTO> detalles;

    public ModeloFacturaDetalle() {
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public List<ModeloDetalleDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<ModeloDetalleDTO> detalles) {
        this.detalles = detalles;
    }

    public boolean isEstado() {

        return estado ==1;
    }

    public void setEstado(int estado) {
        this.estado = estado;

    }
}
