package Util;

import java.util.UUID;

public class GenerarToken {

    public static String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        String token = generateResetToken();
        System.out.println("Token generado: " + token);
    }
}