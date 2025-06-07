/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Carlos Orozco
 */
package Servicio;

import Modelos.ModeloCliente;
import Util.SesionUsuarioJWT;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ServiceCliente {

    private static final String CLIENTE = "http://localhost:5167/api/Clientes"; // URL corregida (http)

    private final Gson gson = new Gson();

    // Obtener lista de clientes
    public List<ModeloCliente> obtenerClientes() throws Exception {
        URL url = new URL(CLIENTE);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + SesionUsuarioJWT.getToken());

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Error al obtener clientes. Código: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Type listType = new TypeToken<List<ModeloCliente>>() {
        }.getType();
        return gson.fromJson(response.toString(), listType);
    }

    // Agregar cliente
    public boolean agregarCliente(ModeloCliente cliente) throws Exception {
        URL url = new URL(CLIENTE);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + SesionUsuarioJWT.getToken());//validacion de token
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = gson.toJson(cliente);
        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.writeBytes(jsonInput);
            wr.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 201 && responseCode != 200) {
            BufferedReader err = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = err.readLine()) != null) {
                errorResponse.append(line);
            }
            err.close();
            System.out.println("Error al agregar cliente: " + errorResponse.toString());
        }

        return responseCode == 201 || responseCode == 200;
    }

    // Actualizar cliente
    public boolean actualizarCliente(ModeloCliente cliente) throws Exception {
        URL url = new URL(CLIENTE + "/" + cliente.getIdCliente());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Authorization", "Bearer " + SesionUsuarioJWT.getToken());//validacion de token
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = gson.toJson(cliente);

        System.out.println("Json antes de enviar" + jsonInput);

        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.writeBytes(jsonInput);
            wr.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 204) {
            System.out.println("Error al actualizar cliente. Código: " + responseCode);
        }

        return responseCode == 204;
    }

    // Eliminar cliente
    public boolean eliminarCliente(int idCliente) throws Exception {
        URL url = new URL(CLIENTE + "/" + idCliente);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Authorization", "Bearer " + SesionUsuarioJWT.getToken());//validacion de token
        int responseCode = conn.getResponseCode();
        if (responseCode != 200 && responseCode != 204) {
            System.out.println("Error al eliminar cliente. Código: " + responseCode);
        }

        return responseCode == 200 || responseCode == 204;
    }
}
