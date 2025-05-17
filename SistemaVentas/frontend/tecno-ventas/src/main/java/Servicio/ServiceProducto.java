/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicio;

import Modelos.ModeloProducto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 *
 * @author Administrador
 */
public class ServiceProducto {
    
    private final String PRODUCTO = "http://localhost:7050/api/Productos"; // endpoint
    private final Gson gson = new Gson();
    //ver producto
     // Obtener lista productos
    public List<ModeloProducto> obtenerProducto() throws Exception {
        URL url = new URL("");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Error al obtener producto. Código: " + responseCode);
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

    
    
    
    //Agregar nuevo Producto
    public boolean agregarProducto(ModeloProducto producto) {
try {
        URL url = new URL("http://localhost:7050/api/Productos");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

      Gson gson = new Gson();
      String json = gson.toJson(producto);

        // Envía el JSON al servidor
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length); //
        }
//codigo de respuesta 
     int responseCode = conn.getResponseCode();
            if (responseCode == 200|| responseCode == 201) {  //OK,creado
                System.out.println("Producto agregado correctamente.");
                return true;
            } else {
                System.out.println("Error al agregar producto. Código de respuesta: " + responseCode);
                return false;
            }
    } catch (Exception e) { //Imprime error en consola
        e.printStackTrace();
        return false; //returna false si sale mal 
    }
}

//Modificar producto
    public static boolean modificarproducto (ModeloProducto producto){
   try{
        URL url = new URL("http://localhost:7050/api/Productos" + producto.getIdproducto());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);
        
        // Convertir el objeto Alumno a JSON
        Gson gson = new Gson();
        String json = gson.toJson(producto);

        // Enviar el JSON al servidor
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
//respuesta
          int responseCode = conn.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
}
}

//Eliminar producto
    public static boolean EliminarProducto(int idProducto){
   try{
        URL  url = new URL("http://localhost:7050/api/Productos" + idProducto);//endpoint
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();//abre conexion
        conn.setRequestMethod("DELETE");
        //respuesta
        int responseCode = conn.getResponseCode();
        
        if (responseCode == 200 || responseCode == 204){ // Si esta correcto muestra el siguiente mensaje
        System.out.println("Alumno eliminado correctamente");
        return true;
        }else{ // de lo contrario 
                System.out.println("Error al eliminar alumno. codigo de respuesta: " + responseCode); 
                return false;
   }
   }catch (Exception e) {
   e.printStackTrace();
   return false;
   }

}
}


