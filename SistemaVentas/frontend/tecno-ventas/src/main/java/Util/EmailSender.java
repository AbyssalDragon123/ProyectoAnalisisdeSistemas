package Util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    public static void sendEmail(String to, String subject, String body) {
        final String from = "pruebasunregional@gmail.com"; // Tu nuevo correo
        final String password = "grmdgfbqomerobkb"; // Contraseña de aplicación (sin espacios)

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Correo enviado correctamente.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Ejemplo de uso
        String username = "nombre_de_usuario_ejemplo";
        String password = "contraseña_ejemplo";
        String body = "Hola,\n\n"
                    + "Aquí están tus credenciales de acceso:\n\n"
                    + "Nombre de usuario: " + username + "\n"
                    + "Contraseña: " + password + "\n\n"
                    + "Por favor, cambia tu contraseña después de iniciar sesión.\n\n"
                    + "Gracias.";

        sendEmail("carlosorozcok@gmail.com", "Tus credenciales de acceso", body);
    }
}
