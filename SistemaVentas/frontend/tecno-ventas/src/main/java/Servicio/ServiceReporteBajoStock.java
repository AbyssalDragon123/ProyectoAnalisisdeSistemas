package Servicio;

import Modelos.ModeloProducto;
import Util.GenerarPDFstock;
import Util.PdfEmailSender;
import java.io.File;
import java.util.List;

/**
 * Servicio para generar y enviar reportes de productos con bajo stock
 */
public class ServiceReporteBajoStock {
    
    private static final String NOMBRE_PDF_DEFAULT = "ProductosBajoStock.pdf";
    
    /**
     * Genera PDF de productos con bajo stock y lo envía por correo
     * @param productos Lista de productos con bajo stock
     * @param emailDestinatario Correo del destinatario
     * @return ResultadoOperacion con el resultado de la operación
     */
    public static ResultadoOperacion generarYEnviarReporte(List<ModeloProducto> productos, String emailDestinatario) {
        try {
            // Validar parámetros
            if (emailDestinatario == null || emailDestinatario.trim().isEmpty()) {
                return new ResultadoOperacion(false, "Email del destinatario no válido");
            }
            
            if (productos == null) {
                return new ResultadoOperacion(false, "Lista de productos no válida");
            }
            
            // Generar PDF usando tu clase existente
            System.out.println("Generando PDF de productos con bajo stock...");
            GenerarPDFstock.generarPDFStockBajo(productos);
            
            // Verificar que el archivo se creó correctamente
            File archivoPDF = new File(NOMBRE_PDF_DEFAULT);
            if (!archivoPDF.exists()) {
                return new ResultadoOperacion(false, "El archivo PDF no se generó correctamente");
            }
            
            // Enviar por correo
            System.out.println("Enviando reporte por correo a: " + emailDestinatario);
            boolean emailEnviado = PdfEmailSender.sendLowStockReport(emailDestinatario, NOMBRE_PDF_DEFAULT);
            
            if (emailEnviado) {
                String mensaje = productos.isEmpty() ? 
                    "Reporte enviado: No hay productos con bajo stock" :
                    "Reporte enviado exitosamente con " + productos.size() + " productos con bajo stock";
                    
                return new ResultadoOperacion(true, mensaje);
            } else {
                return new ResultadoOperacion(false, "Error al enviar el correo electrónico");
            }
            
        } catch (Exception e) {
            System.err.println("Error en generarYEnviarReporte: " + e.getMessage());
            e.printStackTrace();
            return new ResultadoOperacion(false, "Error inesperado: " + e.getMessage());
        }
    }
    
    /**
     * Solo genera el PDF sin enviarlo (usa tu método existente)
     * @param productos Lista de productos con bajo stock
     * @return ResultadoOperacion con el resultado
     */
    public static ResultadoOperacion soloGenerarPDF(List<ModeloProducto> productos) {
        try {
            GenerarPDFstock.generarPDFStockBajo(productos);
            
            File archivoPDF = new File(NOMBRE_PDF_DEFAULT);
            if (archivoPDF.exists()) {
                return new ResultadoOperacion(true, "PDF generado exitosamente: " + NOMBRE_PDF_DEFAULT);
            } else {
                return new ResultadoOperacion(false, "Error al generar el PDF");
            }
        } catch (Exception e) {
            return new ResultadoOperacion(false, "Error al generar PDF: " + e.getMessage());
        }
    }
    
    /**
     * Método para enviar un PDF ya existente
     * @param emailDestinatario Email del destinatario
     * @return ResultadoOperacion con el resultado
     */
    public static ResultadoOperacion enviarPDFExistente(String emailDestinatario) {
        try {
            if (emailDestinatario == null || emailDestinatario.trim().isEmpty()) {
                return new ResultadoOperacion(false, "Email del destinatario no válido");
            }
            
            File archivoPDF = new File(NOMBRE_PDF_DEFAULT);
            if (!archivoPDF.exists()) {
                return new ResultadoOperacion(false, "No existe el archivo PDF. Genere el reporte primero.");
            }
            
            boolean emailEnviado = PdfEmailSender.sendLowStockReport(emailDestinatario, NOMBRE_PDF_DEFAULT);
            
            if (emailEnviado) {
                return new ResultadoOperacion(true, "Reporte enviado exitosamente");
            } else {
                return new ResultadoOperacion(false, "Error al enviar el correo electrónico");
            }
            
        } catch (Exception e) {
            return new ResultadoOperacion(false, "Error: " + e.getMessage());
        }
    }
    
    /**
     * Clase interna para manejar resultados de operaciones
     */
    public static class ResultadoOperacion {
        private final boolean exito;
        private final String mensaje;
        
        public ResultadoOperacion(boolean exito, String mensaje) {
            this.exito = exito;
            this.mensaje = mensaje;
        }
        
        public boolean isExito() {
            return exito;
        }
        
        public String getMensaje() {
            return mensaje;
        }
        
        @Override
        public String toString() {
            return "ResultadoOperacion{" +
                   "exito=" + exito +
                   ", mensaje='" + mensaje + '\'' +
                   '}';
        }
    }
    
    // Método de ejemplo/prueba
    public static void main(String[] args) {
        // Ejemplo de uso:
        // List<ModeloProducto> productosConBajoStock = obtenerProductosBajoStock(); // tu método
        // ResultadoOperacion resultado = generarYEnviarReporte(productosConBajoStock, "destino@email.com");
        // System.out.println(resultado.getMensaje());
    }
}