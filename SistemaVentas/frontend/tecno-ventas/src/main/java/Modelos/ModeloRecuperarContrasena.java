package Modelos;

public class ModeloRecuperarContrasena {

    private String username;
    private String nuevaContrasena;
    private String token;
    private String correo;

    // Constructor vacío
    public ModeloRecuperarContrasena() {
    }

    // Constructor completo
    public ModeloRecuperarContrasena(String username, String nuevaContrasena, String token, String correo) {
        this.username = username;
        this.nuevaContrasena = nuevaContrasena;
        this.token = token;
        this.correo = correo;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNuevaContrasena() {
        return nuevaContrasena;
    }

    public void setNuevaContrasena(String nuevaContrasena) {
        this.nuevaContrasena = nuevaContrasena;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
