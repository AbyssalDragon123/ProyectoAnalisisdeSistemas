package servicio;

import Modelos.ModeloCategoria;
import Modelos.ModeloProducto;
import Util.SesionUsuarioJWT;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategoria {
    private static final String CATEGORIA_API = "http://localhost:5167/api/Categorias";
    private static final String PRODUCTO_API = "http://localhost:5167/api/Productos";
    private final Gson gson = new Gson();

    // Obtener lista completa de categorías
    public List<ModeloCategoria> obtenerCategorias() throws Exception {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(CATEGORIA_API);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + SesionUsuarioJWT.getToken()); //Validación de token

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Error al obtener Categorías. Código: " + responseCode);
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                Type listType = new TypeToken<List<ModeloCategoria>>() {}.getType();
                return gson.fromJson(response.toString(), listType);
            }

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    // Obtener solo nombres de categorías para llenar combo
    public List<String> obtenerNombresCategorias() throws Exception {
        List<ModeloCategoria> categorias = obtenerCategorias();
        List<String> nombres = new ArrayList<>();
        for (ModeloCategoria cat : categorias) {
            nombres.add(cat.getNombreCat());
        }
        return nombres;
    }

    // Obtener nombre de categoría por id
    public String obtenerNombreCategoriaPorId(int idCategoria) throws Exception {
        List<ModeloCategoria> categorias = obtenerCategorias();
        for (ModeloCategoria cat : categorias) {
            if (cat.getIdCategoria() == idCategoria) {
                return cat.getNombreCat();
            }
        }
        return null; // No encontrada
    }

    // Obtener lista de productos
    public List<ModeloProducto> obtenerProductos() throws Exception {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(PRODUCTO_API);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + SesionUsuarioJWT.getToken());//validación de token
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Error al obtener Productos. Código: " + responseCode);
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                Type listType = new TypeToken<List<ModeloProducto>>() {}.getType();
                return gson.fromJson(response.toString(), listType);
            }

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    // Agregar categoría
    public boolean agregarCategoria(ModeloCategoria categoria) throws Exception {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(CATEGORIA_API);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + SesionUsuarioJWT.getToken()); //validación de token
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            String jsonInput = gson.toJson(categoria);
            System.out.println("JSON enviado (agregar): " + jsonInput);

            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8")) {
                writer.write(jsonInput);
                writer.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != 201 && responseCode != 200) {
                InputStream errorStream = conn.getErrorStream();
                if (errorStream != null) {
                    try (BufferedReader err = new BufferedReader(new InputStreamReader(errorStream, "UTF-8"))) {
                        StringBuilder errorResponse = new StringBuilder();
                        String line;
                        while ((line = err.readLine()) != null) {
                            errorResponse.append(line);
                        }
                        System.out.println("Error al agregar Categoría: " + errorResponse.toString());
                    }
                } else {
                    System.out.println("Error al agregar Categoría: No hay mensaje de error disponible.");
                }
                return false;
            }

            return true;

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    // Actualizar categoría
    public boolean actualizarCategoria(ModeloCategoria categoria) throws Exception {
        HttpURLConnection conn = null;
        try {
            System.out.println("Datos a enviar:");
            System.out.println("ID: " + categoria.getIdCategoria());
            System.out.println("Nombre: " + categoria.getNombreCat());
            System.out.println("Descripción: " + categoria.getDescripcion());

            URL url = new URL(CATEGORIA_API + "/" + categoria.getIdCategoria());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Authorization", "Bearer " + SesionUsuarioJWT.getToken());//validación de token
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            String jsonInput = gson.toJson(categoria);
            System.out.println("JSON enviado (actualizar): " + jsonInput);

            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8")) {
                writer.write(jsonInput);
                writer.flush();
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Código de respuesta HTTP: " + responseCode);

            if (responseCode != 200 && responseCode != 204) {
                InputStream errorStream = conn.getErrorStream();
                if (errorStream != null) {
                    try (BufferedReader err = new BufferedReader(new InputStreamReader(errorStream, "UTF-8"))) {
                        StringBuilder errorResponse = new StringBuilder();
                        String line;
                        while ((line = err.readLine()) != null) {
                            errorResponse.append(line);
                        }
                        System.out.println("Error al actualizar Categoría: " + errorResponse.toString());
                    }
                } else {
                    System.out.println("Error al actualizar Categoría: No hay mensaje de error disponible.");
                }
                return false;
            }

            return true;

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    // Eliminar categoría
    public boolean eliminarCategoria(int idCategoria) throws Exception {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(CATEGORIA_API + "/" + idCategoria);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Bearer " + SesionUsuarioJWT.getToken()); //validación de token
            int responseCode = conn.getResponseCode();
            if (responseCode != 200 && responseCode != 204) {
                System.out.println("Error al eliminar Categoría. Código: " + responseCode);
                return false;
            }

            return true;

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    // Obtener categoría por Id
    public ModeloCategoria obtenerCategoriaPorId(int idCategoria) throws Exception {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(CATEGORIA_API + "/" + idCategoria);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + SesionUsuarioJWT.getToken()); //Validación de token
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Error al obtener Categoría. Código: " + responseCode);
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return gson.fromJson(response.toString(), ModeloCategoria.class);
            }

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


}