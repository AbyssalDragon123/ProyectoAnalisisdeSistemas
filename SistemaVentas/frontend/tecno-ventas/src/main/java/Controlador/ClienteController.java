package Controlador;

import Modelos.ModeloCliente;
import Servicio.ServiceCliente;
import Vista.ViewCliente;
import Util.GenericTableModel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ClienteController {

    // Referencia a la vista (interfaz gráfica)
    private final ViewCliente vista;
    // Referencia al servicio que maneja la lógica de negocio y acceso a datos
    private final ServiceCliente servicio;

    // Constructor que recibe la vista y crea el servicio
    public ClienteController(ViewCliente vista) {
        this.vista = vista;
        this.servicio = new ServiceCliente();

        // Estado inicial de los botones  
        vista.getBtnActualizar().setEnabled(false);
        vista.getbtnEliminar().setEnabled(false);
        vista.getBtnRegistrar().setEnabled(true);

        cargarClientes();      // Carga inicial de datos en la tabla
        agregarEventos();      // Asocia los eventos a los botones y tabla
    }

    // Método para asociar eventos a los componentes de la vista
    private void agregarEventos() {

        // Evento para agregar cliente (configuración de botones)
        vista.getBtnRegistrar().addActionListener(e -> agregarCliente());
        // Evento para actualizar cliente
        vista.getBtnActualizar().addActionListener(e -> actualizarCliente());
        // Evento para eliminar cliente
        vista.getbtnEliminar().addActionListener(e -> eliminarCliente());
        // Evento para limpiar campos del formulario
        vista.getBtnLimpiar().addActionListener(e -> limpiarCampos());

        // Evento para detectar selección en la tabla y llenar el formulario
        vista.getTblClientes().getSelectionModel().addListSelectionListener(e -> {

            // Evita que el evento se dispare dos veces (al ajustar selección)
            if (!e.getValueIsAdjusting()) {
                llenarFormularioDesdeTabla();

                vista.getbtnEliminar().setEnabled(true);
                vista.getBtnActualizar().setEnabled(true);
                vista.getBtnRegistrar().setEnabled(false);

            }
        });
    }

    // Método para cargar los clientes desde el servicio y mostrarlos en la tabla
    private void cargarClientes() {
        try {
            // Obtiene la lista de clientes desde el servicio
            List<ModeloCliente> clientes = servicio.obtenerClientes();
            // Obtiene el modelo de la tabla para manipular filas

            System.out.println("Lista obtenida" + clientes);

            DefaultTableModel modelo = new DefaultTableModel();//crear el modelo de la tabla con sus campos para mostrar los datos

            modelo.addColumn("ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Apellido");
            modelo.addColumn("Dirección");
            modelo.addColumn("Telefono");
            modelo.addColumn("Correo");
            modelo.addColumn("Nit");
            modelo.addColumn("FechaCreacion");

            modelo.setRowCount(0);  // Limpia todas las filas existentes

            // Agrega cada cliente como una fila en la tabla
            for (ModeloCliente c : clientes) {

                modelo.addRow(new Object[]{
                    c.getIdCliente(),
                    c.getNombre(),
                    c.getApellido(),
                    c.getDireccion(),
                    c.getTelefono(),
                    c.getCorreo(),
                    c.getNit(),
                    c.getFechaCreacion()
                });
            }

            vista.getTblClientes().setModel(modelo);//cargar los datos obtenidos a la tabla
            ocultarColumnas(new int[]{0, 7}); //ocultar columnas
            

        } catch (Exception e) {
            // Muestra mensaje de error si falla la carga
            JOptionPane.showMessageDialog(vista, "Error al cargar clientes: " + e.getMessage());
        }
    }

    // Método para agregar un nuevo cliente usando los datos del formulario
    private void agregarCliente() {
        try {
            // Construye un objeto ModeloCliente con los datos del formulario
            ModeloCliente cliente = construirClienteDesdeFormulario();

            // Llama al servicio para agregar el cliente
            boolean exito = servicio.agregarCliente(cliente);

            if (exito) {
                JOptionPane.showMessageDialog(vista, "Cliente agregado correctamente.");
                cargarClientes();  // Refresca la tabla con los datos actualizados
                limpiarCampos();   // Limpia el formulario para nueva entrada
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al agregar cliente: " + e.getMessage());
        }
    }

    // Método para actualizar un cliente existente
    private void actualizarCliente() {
        try {
            // Verifica que haya un ID seleccionado para actualizar
            if (vista.getTxtIdUsuario().getText().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Seleccione un cliente para actualizar.");
                return;
            }
            // Construye el cliente con datos del formulario
            ModeloCliente cliente = construirClienteDesdeFormulario();
            // Asigna el ID del cliente que se va a actualizar
            cliente.setIdCliente(Integer.parseInt(vista.getTxtIdUsuario().getText()));

            // Llama al servicio para actualizar el cliente
            boolean exito = servicio.actualizarCliente(cliente);
            if (exito) {
                JOptionPane.showMessageDialog(vista, "Cliente actualizado correctamente.");
                cargarClientes();  // Refresca la tabla
                limpiarCampos();   // Limpia el formulario
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(vista, "ID de cliente inválido.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al actualizar cliente: " + e.getMessage());
        }
    }

    // Método para eliminar un cliente seleccionado
    private void eliminarCliente() {
        try {
            // Verifica que haya un ID seleccionado para eliminar
            if (vista.getTxtIdUsuario().getText().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Seleccione un cliente para eliminar.");
                return;
            }
            int id = Integer.parseInt(vista.getTxtIdUsuario().getText());

            // Pregunta confirmación al usuario
            int confirm = JOptionPane.showConfirmDialog(vista, "¿Está seguro de eliminar este cliente?");
            if (confirm == JOptionPane.YES_OPTION) {
                // Llama al servicio para eliminar el cliente
                boolean exito = servicio.eliminarCliente(id);
                if (exito) {
                    JOptionPane.showMessageDialog(vista, "Cliente eliminado correctamente.");
                    cargarClientes();  // Refresca la tabla
                    limpiarCampos();   // Limpia el formulario
                }
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(vista, "ID de cliente inválido.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al eliminar cliente: " + e.getMessage());
        }
    }

    // Construye un objeto ModeloCliente con los datos ingresados en el formulario
    private ModeloCliente construirClienteDesdeFormulario() {
        ModeloCliente cliente = new ModeloCliente();
        cliente.setNombre(vista.getTxtNombre().getText());
        cliente.setApellido(vista.getTxtApellido().getText());
        cliente.setDireccion(vista.getTxtDireccion().getText());
        cliente.setTelefono(vista.getTxtTelefono().getText());
        cliente.setCorreo(vista.getTxtCorreo().getText());
        cliente.setNit(vista.getTxtNit().getText());
        cliente.setIdUsuario(1); // Aquí puedes asignar el ID del usuario que crea/modifica, si tienes sesión
        return cliente;
    }

    // Llena el formulario con los datos del cliente seleccionado en la tabla
    private void llenarFormularioDesdeTabla() {

        int filaVista = vista.getTblClientes().getSelectedRow();
        if (filaVista != -1) {
            // Convierte el índice de la fila visible al índice del modelo (por si hay ordenamiento)
            int filaModelo = vista.getTblClientes().convertRowIndexToModel(filaVista);
            vista.getTxtIdUsuario().setText(vista.getTblClientes().getModel().getValueAt(filaModelo, 0).toString());
            vista.getTxtNombre().setText(vista.getTblClientes().getModel().getValueAt(filaModelo, 1).toString());
            vista.getTxtApellido().setText(vista.getTblClientes().getModel().getValueAt(filaModelo, 2).toString());
            vista.getTxtDireccion().setText(vista.getTblClientes().getModel().getValueAt(filaModelo, 3).toString());
            vista.getTxtTelefono().setText(vista.getTblClientes().getModel().getValueAt(filaModelo, 4).toString());
            vista.getTxtCorreo().setText(vista.getTblClientes().getModel().getValueAt(filaModelo, 5).toString());
            vista.getTxtNit().setText(vista.getTblClientes().getModel().getValueAt(filaModelo, 6).toString());
        } else {

            // Si no hay fila seleccionada, limpia el formulario
            limpiarCampos();
        }
    }

    // Método para limpiar todos los campos del formulario
    private void limpiarCampos() {

        vista.getTxtIdUsuario().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtApellido().setText("");
        vista.getTxtDireccion().setText("");
        vista.getTxtTelefono().setText("");
        vista.getTxtCorreo().setText("");
        vista.getTxtNit().setText("");

        vista.getTxtNombre().requestFocus();//enfocar nombre para ingreso

        //Habilitar registrar y deshabilitar eliminar y actualizar
        vista.getbtnEliminar().setEnabled(false);
        vista.getBtnActualizar().setEnabled(false);
        vista.getBtnRegistrar().setEnabled(true);
    }

    //metodo para ocultar columnas //ejemplo de llamada (ocultarColumnas(new int[]{0, 3});)
    private void ocultarColumnas(int[] columnas) {
        
        for (int colIndex : columnas) {
            if (colIndex >= 0 && colIndex < vista.getTblClientes().getColumnModel().getColumnCount()) {
                vista.getTblClientes().getColumnModel().getColumn(colIndex).setMinWidth(0);
                vista.getTblClientes().getColumnModel().getColumn(colIndex).setMaxWidth(0);
                vista.getTblClientes().getColumnModel().getColumn(colIndex).setWidth(0);
                vista.getTblClientes().getColumnModel().getColumn(colIndex).setPreferredWidth(0);
            }
        }
    }
}
