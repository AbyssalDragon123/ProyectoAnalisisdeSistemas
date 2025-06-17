package Servicio;

import Modelos.ModeloProducto;
import Util.ProductoEnUsoException;
import Util.SesionUsuarioJWT;
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
        conn.setRequestProperty("Authorization", "Bearer " + SesionUsuarioJWT.getToken()); //Validación de token
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
        conn.setRequestProperty("Authorization", "Bearer " + SesionUsuarioJWT.getToken());//Validaciónd de token
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
        conn.setRequestProperty("Authorization", "Bearer " + SesionUsuarioJWT.getToken()); //Validación de token
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

      public boolean eliminarProducto(int idProducto) throws ProductoEnUsoException, Exception { // Declara las excepciones que puede lanzar
        URL url = new URL(PRODUCTO_API + "/" + idProducto);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        String token = SesionUsuarioJWT.getToken();
        conn.setRequestProperty("Authorization", "Bearer " + token);

        int responseCode = conn.getResponseCode();

        // Si la eliminación fue exitosa (200 OK o 204 No Content)
        if (responseCode == 200 || responseCode == 204) {
            return true; // Retorna true para éxito
        } else { // Si hubo algún tipo de error
            BufferedReader errorReader = null;
            String errorMessage = "Error desconocido"; // Mensaje por defecto

            try {
                errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorResponse.append(line);
                }
                errorMessage = errorResponse.toString();
            } finally {
                if (errorReader != null) {
                    try {
                        errorReader.close();
                    } catch (Exception e) {
                        e.printStackTrace(); // Log del error al cerrar el reader
                    }
                }
            }

            // Si el código es 409, lanza la excepción personalizada
            if (responseCode == 409) {
                throw new ProductoEnUsoException(errorMessage);
            } else {
                // Para cualquier otro código de error (ej. 401 Unauthorized, 404 Not Found, 500 Internal Server Error)
                throw new Exception("Error al eliminar el producto. Código: " + responseCode + ". Mensaje: " + errorMessage);
            }
        }
    }
}
