package Servicio;

import com.google.gson.Gson;
import Modelos.ModeloRecuperarContrasena;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ServiceRecuperarContrasena {

    private static final String RECUPERAR = "http://localhost:5167/api/Recuperar";

    // Enviar token de recuperación al correo
    public static boolean enviarToken(String correo) {
        try {
            ModeloRecuperarContrasena datos = new ModeloRecuperarContrasena();
            datos.setCorreo(correo);
            datos.setUsername("");
            datos.setToken("");
            datos.setNuevaContrasena("");

            URL url = new URL(RECUPERAR + "/forgot-password");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Ver el endpoint generado
            System.out.println("Endpoint: " + RECUPERAR + "/forgot-password");

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            String json = new Gson().toJson(datos);
            System.out.println("Datos en el json: " + json);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Response code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                // Leer el cuerpo del error para diagnosticar
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Error desde API: " + response.toString());
                } catch (Exception ex) {
                    System.out.println("No se pudo leer el cuerpo del error: " + ex.getMessage());
                }
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Resetear la contraseña con el token recibido
    public static boolean resetearContrasena(String username, String correo, String token, String nuevaContrasena) {
    try {
        System.out.println("Iniciando solicitud de restablecimiento de contraseña...");
        System.out.println("Username: " + username);
        System.out.println("Correo: " + correo);
        System.out.println("Token: " + token);
        System.out.println("Nueva contraseña: " + nuevaContrasena);

        // Crear un objeto sólo con los campos requeridos por la API
        Map<String, String> datos = new HashMap<>();
        datos.put("username", username);
        datos.put("correo", correo);
        datos.put("token", token);
        datos.put("nuevaContrasena", nuevaContrasena);

        URL url = new URL(RECUPERAR + "/reset-password");
        System.out.println("Endpoint completo: " + url.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        String json = new Gson().toJson(datos);
        System.out.println("JSON a enviar: " + json);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        System.out.println("Código de respuesta HTTP: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Contraseña restablecida exitosamente.");
            return true;
        } else {
            System.out.println("Error al restablecer la contraseña. Código HTTP: " + responseCode);
            return false;
        }

    } catch (Exception e) {
        System.out.println("Excepción al restablecer contraseña: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

}
