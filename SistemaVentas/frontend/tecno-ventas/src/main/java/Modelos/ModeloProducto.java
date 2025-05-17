/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author Admin
 */      // Define una clase Java 
public class ModeloProducto {
  
     private int id_producto;
     private String nombre;
     private double precio_venta;
     private String id_categoria;
     private String id_usuario;
     private int stock;
     private java.util.Date fecha_creacion;
    
     public ModeloProducto() {
    // Constructor vacío necesario para instanciar sin parámetros
}
     
     // Constructor
    public ModeloProducto(int IdProducto, String nombre, double precio, String IdCategoria, String IdUsuario, int stock, java.util.Date fechaRegistro) {
        this.id_producto = IdProducto;
        this.nombre = nombre;
        this.precio_venta = precio;
        this.id_categoria = IdCategoria;
        this.id_usuario = IdUsuario;
        this.stock = stock;
        this.fecha_creacion = fechaRegistro;
    }

        // Getters y Setters 
    public int getIdproducto() { return id_producto; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio_venta; }
    public String getidCategoria () { return id_categoria; }
    public String getidUsuario (){ return id_usuario;}
    public int getstock(){ return stock;}
    public java.util.Date getfechaRegistro (){ return fecha_creacion;}
    
//SET modifica, cambia (escribir el valor)
    public void setIdProducto(int idProducto) { this.id_producto = idProducto; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(double precio) { this.precio_venta = precio; }
    public void setIdCategoria(String idCategoria) { this.id_categoria = idCategoria; }
    public void setIdUsuario(String idUsuario) { this.id_usuario = idUsuario; }
    public void setStock(int stock) { this.stock = stock; }
    public void setFechaRegistro (java.util.Date fechaRegistro) { this.fecha_creacion = fechaRegistro;}
    
    
    
     // Método toString
    @Override
    public String toString() {
        return "ModeloProducto{" +
                "idProducto=" + id_producto +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio_venta +
                ", idCategoria='" + id_categoria + '\'' +
                ", idUsuario='" + id_usuario + '\'' +
                ", stock=" + stock +
                ", fechaRegistro=" + fecha_creacion +
                '}';
    }
} 

    
        
    


    
