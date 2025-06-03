package Modelos;

public class ModeloLogin {

    private String username;
    private String token;

    // Constructor vacío
    public ModeloLogin() {
    }

    // Constructor con parámetros
    public ModeloLogin(String username, String token) {
        this.username = username;
        this.token = token;
    }

    // Getters y Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
