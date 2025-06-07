package Servicio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Servicio para enviar detalles de factura a la API.
 */
public class ServiceDetalleFactura {

    // Reemplaza con la URL real de tu endpoint de detalle de factura
    private final String DETALLE = "http://localhost:5167/api/DetalleVentas";

    /**
     * Envía un detalle de factura al backend usando HTTP POST.
     *
     * @param detalle Objeto ModeloDetalleFactura a enviar.
     * @return true si fue exitoso, false si hubo error.
     */
    public boolean guardarDetalleFactura(Modelos.ModeloDetalleVenta detalle) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(detalle);

            System.out.println("Json enviado" + json);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(DETALLE))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Código respuesta: " + response.statusCode());
            System.out.println("Cuerpo respuesta: " + response.body());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Error al guardar detalle: Código " + response.statusCode());
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Excepción al guardar detalle: " + e.getMessage());
            return false;
        }
    }
     public List<Modelos.ModeloDetalleVenta> obtenerDetalleFactura(int idFactura) {
         
        try {
            HttpClient client = HttpClient.newHttpClient();
            String url = DETALLE + "/porFactura/" + idFactura; 
            System.out.println("Endpoint" + url);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String json = response.body();
                Gson gson = new Gson();

                Type listaTipo = new TypeToken<List<Modelos.ModeloDetalleVenta>>(){}.getType();
                List<Modelos.ModeloDetalleVenta> detalles = gson.fromJson(json, listaTipo);

                return detalles;
            } else {
                System.err.println("Error al obtener detalle: Código " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
