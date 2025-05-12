/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicio;

/**
 *
 * @author Carlos Orozco
 */
import Controlador.UsuarioController;
import Modelos.ModeloLogin;
import Modelos.ModeloUsuario;
import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class ServiceLogin {

    public static ModeloLogin autenticar(String username, String password) {
        try {
            URL url = new URL("http://localhost:5167/api/Login/login"); //Endpoint
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            //Enviar JSON de credenciales
            
            String jsonInput = String.format("{\"userName\": \"%s\", \"password\": \"%s\"}", username, password);
            try (OutputStream os = con.getOutputStream()) {
                os.write(jsonInput.getBytes("utf-8"));
            }

            // Verificamos la respuesta
            
            if (con.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line.trim());
                }

                // Convertir JSON a objeto Java
                JSONObject json = new JSONObject(response.toString());

                ModeloLogin login = new ModeloLogin();
                login.setUserName(json.getString("username"));
                login.setRol(String.valueOf(json.get("rol"))); // Convertimos el enum a string (si es necesario)
                
                System.out.println("Usuario autenticado: " + username + "Rol" + password);

                return login;
                
            } else if (con.getResponseCode() == 401) {
               // JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
            } else {
                JOptionPane.showMessageDialog(null, "Error del servidor: " + con.getResponseCode());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la API: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}

