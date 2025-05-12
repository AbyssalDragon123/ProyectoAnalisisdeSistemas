/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import javax.swing.JButton;

/**
 *
 * @author Carlos Orozco
 */
public class ModeloUsuario {
    
    private int idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String username;
    private String pass;
    private String rol;
    
    //constructor vacio
    public ModeloUsuario(){}
    
    
    //Getters y setters
    
    public int getIdusuario(){ return idUsuario;}
    public void setIdUsuario(int idUsuario){ this.idUsuario = idUsuario;}
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
    
}
