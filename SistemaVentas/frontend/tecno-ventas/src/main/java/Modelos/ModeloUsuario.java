
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
    private String passwordResetToken;
    private String passwordResetExpires;

    //constructor
    
    public ModeloUsuario(){}
    
    
    // Getters
    public int getIdUsuario() { return idUsuario; }

    public String getNombre() { return nombre; }

    public String getApellido() { return apellido; }

    public String getCorreo() { return correo; }

    public String getUsername() { return username; }

    public String getPass() { return pass; }

    public String getRol() { return rol; }

    public String getPasswordResetToken() { return passwordResetToken; }

    public String getPasswordResetExpires() { return passwordResetExpires; }

    // Setters
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public void setApellido(String apellido) { this.apellido = apellido; }

    public void setCorreo(String correo) { this.correo = correo; }

    public void setUsername(String username) { this.username = username; }

    public void setPass(String pass) { this.pass = pass; }

    public void setRol(String rol) { this.rol = rol; }

    public void setPasswordResetToken(String passwordResetToken) { this.passwordResetToken = passwordResetToken; }

    public void setPasswordResetExpires(String passwordResetExpires) { this.passwordResetExpires = passwordResetExpires; }
}