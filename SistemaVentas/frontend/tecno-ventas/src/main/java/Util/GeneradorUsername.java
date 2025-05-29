/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

public class GeneradorUsername {

    public static String generarNombreUsuario(String nombre, String apellido) {
        
        if (nombre == null || apellido == null || nombre.trim().isEmpty() || apellido.trim().isEmpty()) {
            return ""; // O puedes lanzar una excepción si lo prefieres
        }

        // Toma la primera letra del nombre y concatena el apellido completo, todo en minúsculas
        String username = nombre.trim().substring(0, 1).toLowerCase() + apellido.trim().toLowerCase();

        return username;
    }
}
