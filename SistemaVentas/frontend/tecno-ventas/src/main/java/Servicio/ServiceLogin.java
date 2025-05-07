/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicio;

/**
 *
 * @author Carlos Orozco
 */

import Modelos.ModeloUsuario;
import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class ServiceLogin {




    public static ModeloUsuario autenticar(String username, String password) {//llamando el ModeloUusario del paquete modelos
        
        
        try {
            
            URL url = new URL("http://localhost:5167/api/Usuarios"); // EndPoint para llamar usuarios
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            // Enviar JSON al servidor
            String jsonInput = String.format("{\"username\": \"%s\", \"pass\": \"%s\"}", username, password);
            try (OutputStream os = con.getOutputStream()) {
                os.write(jsonInput.getBytes("utf-8"));
            }

            // Leer la respuesta
            if (con.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line.trim());
                }

                JSONObject json = new JSONObject(response.toString());

                ModeloUsuario usuario = new ModeloUsuario();
                usuario.setIdUsuario(json.getInt("idUsuario"));
                usuario.setNombre(json.getString("nombre"));
                usuario.setApellido(json.getString("apellido"));
                usuario.setCorreo(json.getString("correo"));
                usuario.setUsername(json.getString("username"));
                usuario.setRol(json.getString("rol")); // Asegúrate de que sea string en tu API

                return usuario;
            } else {
                JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la API: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}

    

