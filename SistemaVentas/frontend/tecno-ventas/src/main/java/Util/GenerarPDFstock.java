package Util;

import Modelos.ModeloProducto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.util.List;

public class GenerarPDFstock {

    public static void generarPDFStockBajo(List<ModeloProducto> productos) {
        Document document = new Document(PageSize.A4, 36, 36, 54, 36); // márgenes
        try {
            PdfWriter.getInstance(document, new FileOutputStream("ProductosBajoStock.pdf"));
            document.open();

            // Título
            Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLUE);
            Paragraph titulo = new Paragraph("REPORTE DE PRODUCTOS CON BAJO STOCK", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            // Tabla de 4 columnas
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new int[]{1, 4, 2, 1}); // proporción columnas

            // Encabezados
            Font fuenteEncabezado = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            BaseColor fondoEncabezado = new BaseColor(0, 51, 102); // azul oscuro
            String[] encabezados = {"ID", "Nombre", "Precio (Q)", "Stock"};

            for (String encabezado : encabezados) {
                PdfPCell celda = new PdfPCell(new Phrase(encabezado, fuenteEncabezado));
                celda.setBackgroundColor(fondoEncabezado);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPadding(8);
                tabla.addCell(celda);
            }

            // Fuente para los datos
            Font fuenteDatos = new Font(Font.FontFamily.HELVETICA, 11);

            // Agregar filas de productos
            for (ModeloProducto p : productos) {
                tabla.addCell(crearCelda(String.valueOf(p.getIdProducto()), Element.ALIGN_CENTER, fuenteDatos));
                tabla.addCell(crearCelda(p.getNombre(), Element.ALIGN_LEFT, fuenteDatos));
                tabla.addCell(crearCelda("Q " + String.format("%.2f", p.getPrecioVenta()), Element.ALIGN_RIGHT, fuenteDatos));
                tabla.addCell(crearCelda(String.valueOf(p.getStock()), Element.ALIGN_CENTER, fuenteDatos));
            }

            document.add(tabla);
            document.close();

            // Abrir automáticamente
            java.awt.Desktop.getDesktop().open(new java.io.File("ProductosBajoStock.pdf"));

            System.out.println("PDF generado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para crear celdas con alineación y padding
    private static PdfPCell crearCelda(String texto, int alineacion, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setHorizontalAlignment(alineacion);
        celda.setPadding(6);
        return celda;
    }
}
