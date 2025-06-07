/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author Carlos Orozco
 */
public class ModeloFactura {

    private int idFactura;
    private String fechaFactura; // Formato: "yyyy-MM-ddTHH:mm:ss"
    private int idCliente;
    private int idUsuario;

    // Constructor vacío
    public ModeloFactura() {
    }

    // Constructor con todos los campos (sin cliente porque es null)
    public ModeloFactura(int idFactura, String fechaFactura, int idCliente, int idUsuario) {
        this.idFactura = idFactura;
        this.fechaFactura = fechaFactura;
        this.idCliente = idCliente;
        this.idUsuario = idUsuario;
    }

    // Getters y Setters
    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public String getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(String fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}

