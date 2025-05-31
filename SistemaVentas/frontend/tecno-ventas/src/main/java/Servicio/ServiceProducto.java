package Servicio;

import Modelos.ModeloProducto;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Adaptador para serializar y deserializar LocalDateTime en formato ISO
class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(localDateTime.format(formatter));
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(), formatter);
    }
}

public class ServiceProducto {

    private static final String PRODUCTO_API = "http://localhost:5167/api/Productos"; // Cambia la URL según tu API

    private final Gson gson;

    public ServiceProducto() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    // Obtener lista de productos
    public List<ModeloProducto> obtenerProductos() throws Exception {
        URL url = new URL(PRODUCTO_API);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Error al obtener productos. Código: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Type listType = new TypeToken<List<ModeloProducto>>() {}.getType();
        return gson.fromJson(response.toString(), listType);
    }

    // Agregar producto
    public boolean agregarProducto(ModeloProducto producto) throws Exception {
        URL url = new URL(PRODUCTO_API);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = gson.toJson(producto);
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
            System.out.println("Error al agregar producto: " + errorResponse.toString());
        }

        return responseCode == 201 || responseCode == 200;
    }

    // Actualizar producto
    public boolean actualizarProducto(ModeloProducto producto) throws Exception {
        URL url = new URL(PRODUCTO_API + "/" + producto.getIdProducto());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = gson.toJson(producto);

        System.out.println("Json antes de enviar: " + jsonInput);

        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.writeBytes(jsonInput);
            wr.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 204) {
            System.out.println("Error al actualizar producto. Código: " + responseCode);
        }

        return responseCode == 204;
    }

    // Eliminar producto
    public boolean eliminarProducto(int idProducto) throws Exception {
        URL url = new URL(PRODUCTO_API + "/" + idProducto);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200 && responseCode != 204) {
            System.out.println("Error al eliminar producto. Código: " + responseCode);
        }

        return responseCode == 200 || responseCode == 204;
    }
}