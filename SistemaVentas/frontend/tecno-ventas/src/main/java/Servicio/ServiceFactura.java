/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicio;

import Modelos.ModeloFactura;
import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.swing.JOptionPane;
import Modelos.ModeloRespuestaFactura;

/**
 *
 * @author Carlos Orozco
 */
public class ServiceFactura {

    private final String URL = "http://localhost:5167/api/Facturas"; // Reemplaza con tu endpoint

    public int guardarFactura(ModeloFactura factura) {
        int idGenerado = -1;

        try {
            Gson gson = new Gson();
            String json = gson.toJson(factura);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                Gson gsonRespuesta = new Gson();
                ModeloRespuestaFactura respuesta = gsonRespuesta.fromJson(response.body(), ModeloRespuestaFactura.class);
                idGenerado = respuesta.getIdFactura();
            } else {
                JOptionPane.showMessageDialog(null, "Error al generar factura: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return idGenerado;
    }
}
