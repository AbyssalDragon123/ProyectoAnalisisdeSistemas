/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicio;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class ServiceRecuperarContrasena {
public boolean recuperarContrasena(String username, String nuevaPassword) {
        try {
            URL url = new URL("http://localhost:5167/api/Usuarios/RecuperarContrasena");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{ \"username\": \"" + username + "\", \"nuevaContrasena\": \"" + nuevaPassword + "\" }";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                return true; // Si se actualiza correctamente
            } else {
                return false; // Si no se pudo actualizar
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false; // Error en la conexión o la solicitud
        }
    }

}
