/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicio;

import Modelos.ModeloUsuario;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ServiceUsuario {
    
    private static final String Usuario = "http://localhost:5167/api/Usuarios"; // Ajusta el puerto/API si es necesario
    
    
    //metodo para cargar lista de usuarios de prueba
    
   /* public static List<ModeloUsuario> getAllUsuarios() {
        try {
            URL url = new URL(Usuario);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder respuesta = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                respuesta.append(linea);
            }
              System.out.println("listado" + respuesta); //pintar datos en consola
            reader.close();

            Gson gson = new Gson();
            Type listaTipo = new TypeToken<List<ModeloUsuario>>() {}.getType();
            //asignar una variable para prueba
            List<ModeloUsuario> usuarios = gson.fromJson(respuesta.toString(), listaTipo);
            
            //Log de prueba para validar funcionamiento de la API
            System.out.println("Usuarios obtenidos desde la API:");
            for (ModeloUsuario u : usuarios) {
            System.out.println("ID: " + u.getIdUsuario() 
                    + ", Nombre: " + u.getNombre() 
                    + ", Usuario: " + u.getUsername()
                    +"Contraseña: " + u.getPass()
            );
        }

            return usuarios;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    }*/

//metodo para cargar lista de usuarios
    
    public static List<ModeloUsuario> getAllUsuarios() {
        try {
            URL url = new URL(Usuario);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder respuesta = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                respuesta.append(linea);
            }
            reader.close();

            Gson gson = new Gson();
            Type listaTipo = new TypeToken<List<ModeloUsuario>>() {}.getType();
            return gson.fromJson(respuesta.toString(), listaTipo);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}





    

