/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelos.ModeloCliente;
import Servicio.ServiceCliente;
import Vista.ViewCliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ClienteController {

  /* private final ViewCliente vista;
    private final ServiceCliente servicio;

    public ClienteController(ViewCliente vista) {
        this.vista = vista;
        this.servicio = new ServiceCliente();
        cargarClientes();      // Carga inicial de datos en tabla
        agregarEventos();      // Asociación de botones
    }

    private void agregarEventos() {
        vista.getbtnAgregar().addActionListener(e -> agregarCliente());
        vista.getbtnEditar().addActionListener(e -> actualizarCliente());
        vista.getbtnEliminar().addActionListener(e -> eliminarCliente());
        vista.getBtnLimpiar().addActionListener(e -> limpiarCampos());
        vista.getTblClientes().getSelectionModel().addListSelectionListener(e -> llenarFormularioDesdeTabla());
    }

    private void cargarClientes() {
        try {
            List<ModeloCliente> clientes = servicio.obtenerClientes();
            DefaultTableModel modelo = (DefaultTableModel) vista.getTblClientes().getModel();
            modelo.setRowCount(0);  // Limpia la tabla
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar clientes: " + e.getMessage());
        }
    }

    private void agregarCliente() {
        try {
            ModeloCliente cliente = construirClienteDesdeFormulario();
            boolean exito = servicio.agregarCliente(cliente);
            if (exito) {
                JOptionPane.showMessageDialog(vista, "Cliente agregado correctamente.");
                cargarClientes();
                limpiarCampos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al agregar cliente: " + e.getMessage());
        }
    }

    private void actualizarCliente() {
        try {
            ModeloCliente cliente = construirClienteDesdeFormulario();
            cliente.setIdCliente(Integer.parseInt(vista.getTxtIdUsuario().getText()));
            boolean exito = servicio.actualizarCliente(cliente);
            if (exito) {
                JOptionPane.showMessageDialog(vista, "Cliente actualizado correctamente.");
                cargarClientes();
                limpiarCampos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al actualizar cliente: " + e.getMessage());
        }
    }

    private void eliminarCliente() {
        try {
            int id = Integer.parseInt(vista.getTxtIdUsuario().getText());
            int confirm = JOptionPane.showConfirmDialog(vista, "¿Está seguro de eliminar este cliente?");
            if (confirm == JOptionPane.YES_OPTION) {
                boolean exito = servicio.eliminarCliente(id);
                if (exito) {
                    JOptionPane.showMessageDialog(vista, "Cliente eliminado correctamente.");
                    cargarClientes();
                    limpiarCampos();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al eliminar cliente: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        vista.getTxtIdUsuario().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtApellido().setText("");
        vista.getTxtDireccion().setText("");
        vista.getTxtTelefono().setText("");
        vista.getTxtCorreo().setText("");
        vista.getTxtNit().setText("");
        
        
    }

    private ModeloCliente construirClienteDesdeFormulario() {
        ModeloCliente cliente = new ModeloCliente();
        cliente.setNombre(vista.getTxtNombre().getText());
        cliente.setApellido(vista.getTxtApellido().getText());
        cliente.setDireccion(vista.getTxtDireccion().getText());
        cliente.setTelefono(vista.getTxtTelefono().getText());
        cliente.setCorreo(vista.getTxtCorreo().getText());
        cliente.setNit(vista.getTxtNit().getText());
        cliente.setIdUsuario(1); // Este valor puede venir de sesión si fuera necesario
        return cliente;
    }

    private void llenarFormularioDesdeTabla() {
        int fila = vista.getTblClientes().getSelectedRow();
        if (fila != -1) {
            vista.getTxtIdUsuario().setText(vista.getTblClientes().getValueAt(fila, 0).toString());
            vista.getTxtNombre().setText(vista.getTblClientes().getValueAt(fila, 1).toString());
            vista.getTxtApellido().setText(vista.getTblClientes().getValueAt(fila, 2).toString());
            vista.getTxtDireccion().setText(vista.getTblClientes().getValueAt(fila, 3).toString());
            vista.getTxtTelefono().setText(vista.getTblClientes().getValueAt(fila, 4).toString());
            vista.getTxtCorreo().setText(vista.getTblClientes().getValueAt(fila, 5).toString());
            vista.getTxtNit().setText(vista.getTblClientes().getValueAt(fila, 6).toString());
        }
    }
   */ 
}

