package Servicio;

import Modelos.ModeloDetalleVenta;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ServiceDetalleVenta {

    private final String baseUrl = "http://localhost:5167/api/DetalleVentas"; // Cambia por tu URL real
    private final Gson gson;

    public ServiceDetalleVenta() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public boolean guardarDetalleVenta(ModeloDetalleVenta detalle) {
        try {
            URL url = new URL(baseUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setDoOutput(true);
            
            String jsonInputString = gson.toJson(detalle);
            System.out.println("Detalle enviado" + jsonInputString);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = con.getResponseCode();
            return code == 201 || code == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean guardarDetallesVenta(List<ModeloDetalleVenta> detalles) {
        for (ModeloDetalleVenta detalle : detalles) {
            boolean exito = guardarDetalleVenta(detalle);
            if (!exito) {
                return false; // Si falla alguno, retorna false
            }
        }
        return true; // Todos guardados correctamente
    }
   public boolean guardarDetallesVentaLista(List<ModeloDetalleVenta> detalles) {
    try {
        URL url = new URL(baseUrl + "/guardarLista"); // Asegúrate que baseUrl termina sin /
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setDoOutput(true);

        String jsonInputString = gson.toJson(detalles);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int code = con.getResponseCode();

        java.io.InputStream stream;
        if (code >= 200 && code < 300) {
            stream = con.getInputStream();
        } else {
            stream = con.getErrorStream();
        }

        if (stream != null) {
            try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(stream, StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line.trim());
                }
                if (code >= 200 && code < 300) {
                    System.out.println("Respuesta servidor: " + response.toString());
                } else {
                    System.err.println("Error servidor: " + response.toString());
                }
            }
        } else {
            System.err.println("No hay contenido en la respuesta del servidor.");
        }

        return code == 200 || code == 201;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
}