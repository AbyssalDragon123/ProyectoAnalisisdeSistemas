package Servicio;

import Modelos.ModeloLogin;
import Modelos.SesionUsuario;
import Util.SesionUsuarioJWT;
import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import Util.SessionManager;



public class ServiceLogin {

    public static ModeloLogin autenticar(String username, String password) {
        try {
            URL url = new URL("http://localhost:5167/api/Login/login"); // Endpoint
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            // Construir JSON con las credenciales
            String jsonInput = String.format("{\"userName\": \"%s\", \"password\": \"%s\"}", username, password);
            try (OutputStream os = con.getOutputStream()) {
                os.write(jsonInput.getBytes("utf-8"));
            }

            int responseCode = con.getResponseCode();

            if (responseCode == 200) {
                // Leer respuesta JSON con token
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line.trim());
                }
                System.out.println("Respuesta login: " + response.toString());

                // Parsear JSON para obtener token
                JSONObject json = new JSONObject(response.toString());

                // Crear modelo login y guardar token
                ModeloLogin login = new ModeloLogin();

                // guardamos el token
                String token = json.getString("token");
                
                //enviar el token a variable global para las demas peticiones
                SesionUsuarioJWT.setToken(token);
                SessionManager.setToken(token);
                SessionManager.mostrarDatosSesion();
                
               
                //Enviamos token para validar usuario
                login.setToken(token);

               
                // Fecha para expiración.
                // String expiration = json.getString("expiration");
                // login.setExpiration(expiration);

                login.setUsername(username);
                // login.setRol("rolPorDefecto"); // opcional

                return login;

            } else if (responseCode == 401) {
                JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
            } else {
                JOptionPane.showMessageDialog(null, "Error del servidor: " + responseCode);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la API: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
