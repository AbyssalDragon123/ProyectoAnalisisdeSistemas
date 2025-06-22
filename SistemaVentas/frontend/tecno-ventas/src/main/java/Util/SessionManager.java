package Util;

import java.util.Base64;

/**
 * Clase SessionManager Esta clase se encarga de almacenar la sesión actual del
 * usuario logueado, incluyendo su token JWT, nombre de usuario y rol. Además,
 * extrae automáticamente los datos del payload del token.
 */
public class SessionManager {

    // Variable que guarda el token JWT del usuario
    private static String token;

    // Nombre de usuario extraído del token
    private static String nombreUsuario;

    // Rol del usuario extraído del token
    private static String rol;

    //Id del usuario
    private static int idUsuario = -1;

    /**
     * Método para establecer el token después del login. También extrae
     * automáticamente los datos de nombre y rol desde el token.
     *
     * @param tokenValue El token JWT completo.
     */
    public static void setToken(String tokenValue) {
        token = tokenValue;
        extraerDatosDesdeToken(tokenValue);  // Decodifica el token para obtener datos
    }

    // Devuelve el token guardado
    public static String getToken() {
        return token;
    }

    // Devuelve el nombre de usuario
    public static String getNombreUsuario() {
        return nombreUsuario;
    }

    // Devuelve el rol del usuario
    public static String getRol() {
        return rol;
    }

    //Devuelve el id del usuario
    public static int getIdUsuario() {

        return idUsuario;

    }

    /**
     * Método para decodificar el token y extraer nombre y rol del usuario. El
     * token JWT tiene 3 partes separadas por puntos (header.payload.signature)
     *
     * @param token Token JWT completo.
     */
    private static void extraerDatosDesdeToken(String token) {
        try {
            // Dividimos el token en partes
            String[] partes = token.split("\\.");

            // Verificamos que tenga las 3 partes
            if (partes.length == 3) {
                String payloadCodificado = partes[1];  // Segunda parte = payload (Base64URL)

                // Decodificamos el payload (Base64 URL-safe)
                String payloadJson = new String(Base64.getUrlDecoder().decode(payloadCodificado));
                System.out.println("PAYLOAD DECODIFICADO" + payloadJson);

                // Extraemos nombre de usuario y rol desde el JSON del payload
                nombreUsuario = extraerValor(payloadJson, "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name");
                rol = extraerValor(payloadJson, "http://schemas.microsoft.com/ws/2008/06/identity/claims/role");
                idUsuario = Integer.parseInt(extraerValor(payloadJson, "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier"));

            }
        } catch (Exception e) {
            System.out.println("Error al procesar el token: " + e.getMessage());
        }
    }

    /**
     * Método auxiliar para extraer un valor de un campo JSON específico No
     * usamos librerías externas, lo hacemos con búsqueda de texto.
     *
     * @param json El texto JSON decodificado.
     * @param clave La clave que queremos buscar (ej. URL del claim de "role")
     * @return El valor encontrado o null si no se encuentra.
     */
    private static String extraerValor(String json, String clave) {
        // Buscamos la clave completa (con comillas y dos puntos)
        String patron = "\"" + clave + "\":\"";

        // Posición donde inicia la clave
        int inicio = json.indexOf(patron);
        if (inicio == -1) {
            return null;
        }

        // Posiciones de inicio y fin del valor
        int desde = inicio + patron.length();
        int hasta = json.indexOf("\"", desde);
        if (hasta == -1) {
            return null;
        }

        // Extraemos el valor
        return json.substring(desde, hasta);
    }
    
    public static void mostrarDatosSesion() {
    System.out.println("Token: " + token);
    System.out.println("Nombre usuario: " + nombreUsuario);
    System.out.println("Rol: " + rol);
    System.out.println("ID usuario: " + idUsuario);
}

}
