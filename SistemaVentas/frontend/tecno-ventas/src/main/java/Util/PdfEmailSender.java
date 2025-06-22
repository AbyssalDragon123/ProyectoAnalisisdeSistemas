package Util;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

public class PdfEmailSender {
    
    private static final String FROM_EMAIL = "pruebasunregional@gmail.com";
    private static final String APP_PASSWORD = "grmdgfbqomerobkb";
    
    /**
     * Envía un correo con archivo PDF adjunto
     * @param to Correo del destinatario
     * @param subject Asunto del correo
     * @param bodyText Cuerpo del mensaje
     * @param pdfFilePath Ruta del archivo PDF a adjuntar
     * @return true si se envió correctamente, false en caso contrario
     */
    public static boolean sendEmailWithPDF(String to, String subject, String bodyText, String pdfFilePath) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        
        //forzar TLS 1.2
        properties.put("mail.smtp.ssl.protocols","TLSv1.2");
        properties.put("mail.smtp.ssl.trust","smtp.gmail.com");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        try {
            // Crear el mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            // Crear el contenido multipart
            Multipart multipart = new MimeMultipart();

            // Parte del texto del mensaje
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(bodyText);
            multipart.addBodyPart(messageBodyPart);

            // Verificar si el archivo PDF existe
            File pdfFile = new File(pdfFilePath);
            if (!pdfFile.exists()) {
                System.err.println("Error: El archivo PDF no existe en la ruta: " + pdfFilePath);
                return false;
            }

            // Parte del archivo adjunto
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(pdfFilePath);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(pdfFile.getName());
            multipart.addBodyPart(messageBodyPart);

            // Agregar el contenido multipart al mensaje
            message.setContent(multipart);

            // Enviar el mensaje
            Transport.send(message);
            System.out.println("Correo con PDF enviado correctamente a: " + to);
            return true;

        } catch (MessagingException e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Método específico para enviar reporte de productos con bajo stock
     * @param to Correo del destinatario
     * @param pdfFilePath Ruta del PDF generado (por defecto "ProductosBajoStock.pdf")
     * @return true si se envió correctamente
     */
    public static boolean sendLowStockReport(String to, String pdfFilePath) {
        String subject = "🚨 Alerta: Reporte de Productos con Bajo Stock";
        String body = "Estimado/a,\n\n" +
                     "Adjunto encontrará el reporte de productos con bajo stock que requieren atención inmediata.\n\n" +
                     "Por favor, revise la información y considere realizar pedidos de reabastecimiento " +
                     "para evitar desabastecimiento.\n\n" +
                     "Productos incluidos en el reporte:\n" +
                     "• Productos que han alcanzado el stock mínimo\n" +
                     "• Productos con inventario crítico\n\n" +
                     "Recomendaciones:\n" +
                     "1. Revisar proveedores disponibles\n" +
                     "2. Verificar tiempos de entrega\n" +
                     "3. Priorizar productos de alta rotación\n\n" +
                     "Saludos cordiales,\n" +
                     "Sistema de Gestión de Inventario";
        
        return sendEmailWithPDF(to, subject, body, pdfFilePath);
    }
    
    /**
     * Sobrecarga para usar la ruta por defecto del PDF
     */
    public static boolean sendLowStockReport(String to) {
        return sendLowStockReport(to, "ProductosBajoStock.pdf");
    }
    
    // Método de prueba
    public static void main(String[] args) {
        // Ejemplo de uso
        String testEmail = "carlosorozcok@gmail.com";
        String testPdfPath = "ProductosBajoStock.pdf"; // Tu PDF por defecto
        
        boolean success = sendLowStockReport(testEmail, testPdfPath);
        
        if (success) {
            System.out.println("Prueba exitosa: Correo enviado");
        } else {
            System.out.println("Prueba fallida: Error al enviar correo");
        }
    }
}
