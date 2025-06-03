package Util;

public class SesionUsuarioJWT {

    private static String token;
    private static String rol;
    private static String username;

    // Métodos para el token
    public static void setToken(String value) {
        token = value;
    }

    public static String getToken() {
        return token;
    }

    // Métodos para el rol
    public static void setRol(String value) {
        rol = value;
    }

    public static String getRol() {
        return rol;
    }

    // Métodos para el username
    public static void setUsername(String value) {
        username = value;
    }

    public static String getUsername() {
        return username;
    }
}


