package Modelos;

public class ModeloUsuario {

    // Atributos de la clase
    private int idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String username;
    private String pass;
    private String rol;  // El rol se maneja como texto (ej. "admin", "vendedor", etc.)
    private String passwordResetToken;
    private String passwordResetExpires;

    // Constructor vacío
    public ModeloUsuario() {
    }

    // Constructor con todos los atributos
    public ModeloUsuario(int idUsuario, String nombre, String apellido, String correo,
                         String username, String pass, String rol,
                         String passwordResetToken, String passwordResetExpires) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.username = username;
        this.pass = pass;
        this.rol = rol;
        this.passwordResetToken = passwordResetToken;
        this.passwordResetExpires = passwordResetExpires;
    }

    // Getters y setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public String getPasswordResetExpires() {
        return passwordResetExpires;
    }

    public void setPasswordResetExpires(String passwordResetExpires) {
        this.passwordResetExpires = passwordResetExpires;
    }

    // Método que devuelve el nombre completo del usuario
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (" + username + ")";
    }

    public static class Rol {

        public Rol() {
        }
    }
}
