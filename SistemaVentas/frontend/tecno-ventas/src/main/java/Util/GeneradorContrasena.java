/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import java.security.SecureRandom;
import javax.swing.JOptionPane;

public class GeneradorContrasena {

    private static final String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMEROS = "0123456789";
    private static final String CARACTERES_ESPECIALES = "!@#$%&*()_+-=[]|,./?><";

    private static final String TODOS_CARACTERES = MAYUSCULAS + MINUSCULAS + NUMEROS + CARACTERES_ESPECIALES;

    private static final SecureRandom random = new SecureRandom();

    public static String generar(int longitud) {
        if (longitud < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }

        StringBuilder contrasena = new StringBuilder(longitud);

        // Garantizar al menos un carácter de cada tipo
        contrasena.append(MAYUSCULAS.charAt(random.nextInt(MAYUSCULAS.length())));
        contrasena.append(MINUSCULAS.charAt(random.nextInt(MINUSCULAS.length())));
        contrasena.append(NUMEROS.charAt(random.nextInt(NUMEROS.length())));
        contrasena.append(CARACTERES_ESPECIALES.charAt(random.nextInt(CARACTERES_ESPECIALES.length())));

        // Rellenar el resto con caracteres aleatorios
        for (int i = 4; i < longitud; i++) {
            contrasena.append(TODOS_CARACTERES.charAt(random.nextInt(TODOS_CARACTERES.length())));
        }
        JOptionPane.showMessageDialog(null, "Contraseña segura generada: \n" + contrasena, "Generador de Contraseñas",
                 JOptionPane.INFORMATION_MESSAGE);

        return contrasena.toString();
    }

}
