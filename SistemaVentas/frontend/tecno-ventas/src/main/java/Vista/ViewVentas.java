/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Vista;

import Controlador.UsuarioController;
import Modelos.DetalleFacturaView;
import Modelos.ModeloCliente;
import Modelos.ModeloDetalleVenta;
import Modelos.ModeloFactura;
import Modelos.ModeloProducto;
import Modelos.SesionUsuario;
import Servicio.ServiceCliente;
import Servicio.ServiceDetalleFactura;
import Servicio.ServiceDetalleVenta;
import Servicio.ServiceFactura;
import Servicio.ServiceProducto;
import servicio.ServiceCategoria;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import Util.SessionManager;

/**
 *
 * @author Admin
 */
public class ViewVentas extends javax.swing.JFrame {

    private ServiceCliente serviceCliente;
    private ServiceProducto serviceProducto;
    private ServiceCategoria serviceCategoria;
    private ServiceDetalleVenta serviceDetalleVenta;
    private List<ModeloProducto> productosTotales = new ArrayList<>();
    private List<ModeloCliente> clientesTotales = new ArrayList<>();
    private DefaultTableModel modeloDetalle;

    public ViewVentas() {
        initComponents();

        cargarClientes();
        inicializarTablaDetalle(); //crear modelo de la tabla

        //DocumentListener-inicializa la busqueda durante la escritura en productos
        txtProductoBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarProductos();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarProductos();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarProductos();
            }
        });

        //Filtrar por escritura, clientes
        txtNombreBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarClientes();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarClientes();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarClientes();
            }
        });

        //Cargar datos de precio y stock al seleccionar un item del combobox
        cmbProducto.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Solo respondemos cuando el estado es "seleccionado"
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ModeloProducto productoSeleccionado = (ModeloProducto) cmbProducto.getSelectedItem();

                    if (productoSeleccionado != null) {
                        // Mostramos los datos del producto en los campos
                        txtPrecio.setText(String.valueOf(productoSeleccionado.getPrecioVenta()));
                        txtStock.setText(String.valueOf(productoSeleccionado.getStock()));

                        // Log opcional
                        System.out.println("Producto seleccionado: " + productoSeleccionado.getNombre());
                        System.out.println("Precio: " + productoSeleccionado.getPrecioVenta());
                        System.out.println("Stock: " + productoSeleccionado.getStock());
                    }
                }
            }
        });

        cargarProductos(); //cargar datos de producto desde la api.

    }

    private void cargarProductos() {
        try {
            ServiceProducto service = new ServiceProducto();

            // Guardamos todos los productos en el atributo de clase
            productosTotales = service.obtenerProductos();

            // Limpiamos el comboBox actual
            cmbProducto.removeAllItems();

            // Agregamos todos los productos al combo
            for (ModeloProducto producto : productosTotales) {
                cmbProducto.addItem(producto);
            }

            System.out.println("Productos cargados: " + productosTotales.size());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //filtrar producto
    private void filtrarProductos() {

        String textoBuscar = txtProductoBuscar.getText().trim().toLowerCase();

        cmbProducto.removeAllItems();

        for (ModeloProducto producto : productosTotales) {
            if (producto.getNombre().toLowerCase().contains(textoBuscar)) {
                cmbProducto.addItem(producto);
            }
        }

        // Si no se encuentra ninguno, puedes mostrar un mensaje (opcional)
        if (cmbProducto.getItemCount() == 0) {
            System.out.println("No se encontraron productos con: " + textoBuscar);
        }
    }

    //Inicializar tabla para capturar datos
    private void inicializarTablaDetalle() {

        modeloDetalle = new DefaultTableModel();
        modeloDetalle.addColumn("ID Producto");
        modeloDetalle.addColumn("Nombre");
        modeloDetalle.addColumn("Cantidad");
        modeloDetalle.addColumn("Precio");
        modeloDetalle.addColumn("Subtotal");

        tablaDetalle.setModel(modeloDetalle);

    }

    //Agregar producto a la tabla
    private void agregarProductoADetalle() {

        ModeloProducto producto = (ModeloProducto) cmbProducto.getSelectedItem();

        if (producto == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto.");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            return;
        }

        if (cantidad <= 0 || cantidad > producto.getStock()) {
            JOptionPane.showMessageDialog(this, "Cantidad fuera de rango.");
            return;
        }

        double precio = producto.getPrecioVenta();
        double subtotal = cantidad * precio;

        modeloDetalle.addRow(new Object[]{
            producto.getIdProducto(),
            producto.getNombre(),
            cantidad,
            precio,
            subtotal
        });

        //limpiar campos
        txtCantidad.setText("");
        txtProductoBuscar.setText("");

        calcularTotales(); //calcular totales

        System.out.println("Producto agregado a la tabla: " + producto.getNombre());
    }

    // Método para eliminar el producto seleccionado en la tabla de detalle
    private void eliminarProductoDeTabla() {

        int filaSeleccionada = tablaDetalle.getSelectedRow();

        if (filaSeleccionada >= 0) {
            // Confirmación opcional
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este producto?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                modeloDetalle.removeRow(filaSeleccionada); // Elimina la fila del modelo de la tabla
                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
                calcularTotales(); //recalcular totales
            }

        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para eliminar.");
        }
    }

    // Método para calcular subtotal, IVA y total de la venta
    private void calcularTotales() {
        double subtotal = 0.0;

        // Recorremos todas las filas de la tabla para acumular los subtotales
        for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
            double sub = Double.parseDouble(modeloDetalle.getValueAt(i, 4).toString()); // Columna 4 = Subtotal
            subtotal += sub;
        }

        // Calculamos IVA y total
        double iva = subtotal * 0.12;
        double total = subtotal + iva;

        // Mostramos los resultados en los campos correspondientes
        txtSubtotal.setText(String.format("%.2f", subtotal));
        txtIVA.setText(String.format("%.2f", iva));
        txtTotal.setText(String.format("%.2f", total));
    }

    // Método para limpiar completamente la tabla de detalle de venta
    private void limpiarTablaDetalle() {
        // Elimina todas las filas del modelo
        modeloDetalle.setRowCount(0);

        /* Limpia los campos de totales si deseas
        txtSubtotal.setText("");
        txtIVA.setText("");
        txtTotal.setText("");*/
        calcularTotales();

        // Opcional: mensaje de confirmación
        JOptionPane.showMessageDialog(this, "Se han eliminado todos los productos de la tabla.");
    }

    //Clientes
    private void cargarClientes() {
        try {
            ServiceCliente service = new ServiceCliente();

            // Obtener todos los clientes desde la API
            clientesTotales = service.obtenerClientes();

            // Limpiar el comboBox actual
            jcbNombreCliente.removeAllItems();

            // Agregar todos los clientes al comboBox
            for (ModeloCliente cliente : clientesTotales) {
                jcbNombreCliente.addItem(cliente); // Asegúrate de tener un buen toString() en ModeloCliente
            }

            System.out.println("Clientes cargados: " + clientesTotales.size());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void filtrarClientes() {
        String textoBusqueda = txtNombreBuscar.getText().toLowerCase();

        jcbNombreCliente.removeAllItems();

        for (ModeloCliente cliente : clientesTotales) {
            if (cliente.getNombre().toLowerCase().contains(textoBusqueda)) {
                jcbNombreCliente.addItem(cliente);
            }
        }
    }

    //Facturas
    //Generar factura
    private void generarFactura() {
        // 1. Obtener datos de la factura
        ModeloFactura factura = new ModeloFactura();

        String fechaActual = LocalDateTime.now().toString();
        factura.setFechaFactura(fechaActual);
        ModeloCliente clienteSeleccionado = (ModeloCliente) jcbNombreCliente.getSelectedItem();

        factura.setIdCliente(clienteSeleccionado.getIdCliente());

        //int idUsuario = SesionUsuario.nombreUsuario;
        factura.setIdUsuario(SessionManager.getIdUsuario());

        System.out.println("Usuario que genera la factura" + SessionManager.getIdUsuario());

        // 2. Guardar factura y obtener ID generado
        ServiceFactura serviceFactura = new ServiceFactura();
        int idFactura = serviceFactura.guardarFactura(factura);

        if (idFactura != -1) {
            // 3. Recorrer tabla y guardar detalles
            ServiceDetalleFactura serviceDetalle = new ServiceDetalleFactura();

            for (int i = 0; i < tablaDetalle.getRowCount(); i++) {
                int idProducto = Integer.parseInt(tablaDetalle.getValueAt(i, 0).toString());
                int cantidad = Integer.parseInt(tablaDetalle.getValueAt(i, 2).toString());
                double precioVenta = Double.parseDouble(tablaDetalle.getValueAt(i, 3).toString());

                ModeloDetalleVenta detalle = new ModeloDetalleVenta();
                detalle.setIdFactura(idFactura);
                detalle.setIdProducto(idProducto);
                detalle.setCantidad(cantidad);

                detalle.setPrecioVenta(BigDecimal.valueOf(precioVenta));

                boolean guardado = serviceDetalle.guardarDetalleFactura(detalle);
                if (!guardado) {
                    JOptionPane.showMessageDialog(this, "Error al guardar un detalle de producto.");
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "Factura generada con éxito.");
            limpiarTodo(); // Limpiar todos los campos
            DetalleFacturaView detalleView = new DetalleFacturaView(idFactura);
            detalleView.setVisible(true);
            detalleView.setLocationRelativeTo(null);

        } else {
            JOptionPane.showMessageDialog(this, "No se pudo generar la factura.");
        }
    }

    //limpiar campos del formulario despues de realizar una venta
    private void limpiarTodo() {

        // Limpiar campos de texto
        txtProductoBuscar.setText("");
        txtNombreBuscar.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtCantidad.setText("");
        //txtCorreo.setText("");
        txtSubtotal.setText("");
        txtTotal.setText("");
        txtIVA.setText("");

        // Limpiar combobox
        cmbProducto.removeAllItems();
        jcbNombreCliente.removeAllItems();

        // Limpiar tabla detalle
        if (modeloDetalle != null) {
            modeloDetalle.setRowCount(0); // Elimina todas las filas
        }

        // Limpiar listas si usas alguna para manejar datos en memoria
        productosTotales.clear();
        clientesTotales.clear();

        // También puedes resetear otras variables que uses para controlar el estado
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtNombreBuscar = new javax.swing.JTextField();
        jcbNombreCliente = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDetalle = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtIVA = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtSubtotal = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        txtprecio_venta = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtid_producto = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        txtCantidad = new javax.swing.JTextField();
        cmbProducto = new javax.swing.JComboBox<>();
        txtProductoBuscar = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnHome2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnAgregarProducto = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(192, 214, 223));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sensible.png"))); // NOI18N
        jLabel1.setText("TECNO VENTAS");

        jPanel4.setBackground(new java.awt.Color(48, 50, 61));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(192, 214, 223));
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sensible.png"))); // NOI18N
        jLabel18.setText("TECNO VENTAS");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel18)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(92, 128, 188));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(219, 233, 238));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("-------- VENTAS --------");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(181, 186, 208));

        jPanel1.setBackground(new java.awt.Color(115, 137, 174));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Datos del Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 24), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Roboto Medium", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Nombre:");

        txtNombreBuscar.setBackground(new java.awt.Color(164, 178, 202));
        txtNombreBuscar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtNombreBuscar.setBorder(null);

        jcbNombreCliente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Filtrar cliente");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcbNombreCliente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtNombreBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(56, 56, 56))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombreBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jcbNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(112, 112, 112))
        );

        jPanel7.setBackground(new java.awt.Color(115, 137, 174));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Tabla de Ventas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 24), new java.awt.Color(255, 255, 255))); // NOI18N

        tablaDetalle.setBackground(new java.awt.Color(174, 186, 208));
        tablaDetalle.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tablaDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaDetalle);

        jLabel17.setFont(new java.awt.Font("Roboto Medium", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Total Q.:");

        txtTotal.setBackground(new java.awt.Color(164, 178, 202));
        txtTotal.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotal.setBorder(null);
        txtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Roboto Medium", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("IVA Q.:");

        txtIVA.setBackground(new java.awt.Color(164, 178, 202));
        txtIVA.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtIVA.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtIVA.setBorder(null);
        txtIVA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIVAActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Roboto Medium", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Subtotal Q.:");

        txtSubtotal.setBackground(new java.awt.Color(164, 178, 202));
        txtSubtotal.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtSubtotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSubtotal.setBorder(null);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(10, 10, 10))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(txtIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(115, 137, 174));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Datos de Productos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 24), new java.awt.Color(255, 255, 255))); // NOI18N

        txtprecio_venta.setBackground(new java.awt.Color(164, 178, 202));
        txtprecio_venta.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Roboto Medium", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Producto");

        jLabel14.setFont(new java.awt.Font("Roboto Medium", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Stock:");

        jLabel15.setFont(new java.awt.Font("Roboto Medium", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Precio Q:");

        txtid_producto.setBackground(new java.awt.Color(164, 178, 202));
        txtid_producto.setBorder(null);

        jLabel16.setFont(new java.awt.Font("Roboto Medium", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Cantidad:");

        txtPrecio.setEditable(false);
        txtPrecio.setBackground(new java.awt.Color(164, 178, 202));
        txtPrecio.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtPrecio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPrecio.setBorder(null);

        txtCantidad.setBackground(new java.awt.Color(164, 178, 202));
        txtCantidad.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtCantidad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCantidad.setBorder(null);

        cmbProducto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        txtProductoBuscar.setBackground(new java.awt.Color(164, 178, 202));
        txtProductoBuscar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Filtrar producto");

        txtStock.setEditable(false);
        txtStock.setBackground(new java.awt.Color(164, 178, 202));
        txtStock.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtStock.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtStock, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(429, 429, 429)
                        .addComponent(txtid_producto, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtProductoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(509, 509, 509)
                        .addComponent(txtprecio_venta, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(txtprecio_venta, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProductoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtid_producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(cmbProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        jPanel2.setBackground(new java.awt.Color(77, 80, 97));

        btnHome2.setBackground(new java.awt.Color(154, 179, 227));
        btnHome2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/casa (1).png"))); // NOI18N
        btnHome2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(74, 111, 165), 3, true));
        btnHome2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome2ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(204, 255, 204));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Generar factura");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(154, 179, 227));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEliminar.setText("Eliminar item");
        btnEliminar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(74, 111, 165), 3));
        btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnLimpiar.setBackground(new java.awt.Color(154, 179, 227));
        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(74, 111, 165), 3));
        btnLimpiar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnAgregarProducto.setBackground(new java.awt.Color(154, 179, 227));
        btnAgregarProducto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAgregarProducto.setText("Agregar producto");
        btnAgregarProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(74, 111, 165), 3));
        btnAgregarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHome2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAgregarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(btnHome2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHome2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome2ActionPerformed
        // TODO add your handling code here:
        MenuPrincipal menu = new MenuPrincipal();
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnHome2ActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:

        limpiarTablaDetalle();

    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:

        eliminarProductoDeTabla(); //llamamos metodo para eliminar selección

    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        // TODO add your handling code here:

        agregarProductoADetalle();

    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalActionPerformed

    private void txtIVAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIVAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIVAActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        generarFactura();


    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnHome2;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox<ModeloProducto> cmbProducto;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<ModeloCliente> jcbNombreCliente;
    private javax.swing.JTable tablaDetalle;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtIVA;
    private javax.swing.JTextField txtNombreBuscar;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtProductoBuscar;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtid_producto;
    private javax.swing.JTextField txtprecio_venta;
    // End of variables declaration//GEN-END:variables
}
