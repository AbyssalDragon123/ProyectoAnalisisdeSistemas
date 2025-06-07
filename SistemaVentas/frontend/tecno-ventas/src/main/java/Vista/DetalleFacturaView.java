package Modelos;

import Servicio.ServiceDetalleFactura;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DetalleFacturaView extends JFrame {

    private JTable tblDetalleFactura;
    private int idFactura;

    public DetalleFacturaView(int idFactura) {
        this.idFactura = idFactura;
        setTitle("Detalle de Factura #" + idFactura);
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        cargarDetalleFactura();
    }

    private void initComponents() {
        tblDetalleFactura = new JTable();
        JScrollPane scrollPane = new JScrollPane(tblDetalleFactura);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void cargarDetalleFactura() {
        ServiceDetalleFactura serviceDetalle = new ServiceDetalleFactura();
        List<ModeloDetalleVenta> detalles = serviceDetalle.obtenerDetalleFactura(idFactura);

        if (detalles != null && !detalles.isEmpty()) {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID Producto");
            model.addColumn("Cantidad");
            model.addColumn("Precio Venta");

            for (ModeloDetalleVenta detalle : detalles) {
                Object[] fila = new Object[]{
                    detalle.getIdProducto(),
                    detalle.getCantidad(),
                    detalle.getPrecioVenta()
                };
                model.addRow(fila);
            }
            tblDetalleFactura.setModel(model);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron detalles para esta factura");
        }
    }
}
