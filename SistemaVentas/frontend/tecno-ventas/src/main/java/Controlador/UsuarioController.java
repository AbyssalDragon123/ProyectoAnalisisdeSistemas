package Controlador;

import Modelos.ModeloUsuario;
import Servicio.ServiceUsuario;
import Vista.ViewUsuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class UsuarioController {

    private final ViewUsuario vista;
    private final ServiceUsuario servicio;

    public UsuarioController(ViewUsuario vista) {
        this.vista = vista;
        this.servicio = new ServiceUsuario();

        // Estado inicial de los botones  
        vista.getBtnActualizar().setEnabled(false);
        vista.getBtnEliminar().setEnabled(false);
        vista.getBtnRegistrar().setEnabled(true);

        cargarUsuarios();      // Carga inicial de datos en la tabla
        agregarEventos();      // Asocia los eventos a los botones y tabla
    }

    private void agregarEventos() {
        vista.getBtnRegistrar().addActionListener(e -> agregarUsuario());
        vista.getBtnActualizar().addActionListener(e -> actualizarUsuario());
        vista.getBtnEliminar().addActionListener(e -> eliminarUsuario());
        vista.getBtnLimpiar().addActionListener(e -> limpiarCampos());

        vista.getTableUsuarios().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                llenarFormularioDesdeTabla();

                vista.getBtnEliminar().setEnabled(true);
                vista.getBtnActualizar().setEnabled(true);
                vista.getBtnRegistrar().setEnabled(false);
            }
        });
    }

    private void cargarUsuarios() {
        try {
            List<ModeloUsuario> usuarios = servicio.obtenerUsuarios();

            DefaultTableModel modelo = new DefaultTableModel();

            modelo.addColumn("ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Apellido");
            modelo.addColumn("Correo");
            modelo.addColumn("Username");
            modelo.addColumn("Rol");
            modelo.addColumn("Pass");
            modelo.addColumn("PasswordResetToken");
            modelo.addColumn("PasswordResetExpires");

            modelo.setRowCount(0);

            for (ModeloUsuario u : usuarios) {
                modelo.addRow(new Object[]{
                    u.getIdUsuario(),
                    u.getNombre(),
                    u.getApellido(),
                    u.getCorreo(),
                    u.getUsername(),
                    u.getRol() != null ? u.getRol() : "",
                    u.getPass(),
                    u.getPasswordResetToken(),
                    u.getPasswordResetExpires()
                });
            }

            vista.getTableUsuarios().setModel(modelo);
            ocultarColumnas(new int[]{0, 6, 7, 8}); // Ocultar ID, Pass, Token y Expiración

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar usuarios: " + e.getMessage());
        }
    }

    private void agregarUsuario() {
        try {
            ModeloUsuario usuario = construirUsuarioDesdeFormulario();

            boolean exito = servicio.agregarUsuario(usuario);

            if (exito) {
                JOptionPane.showMessageDialog(vista, "Usuario agregado correctamente.");
                cargarUsuarios();
                limpiarCampos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al agregar usuario: " + e.getMessage());
        }
    }

    private void actualizarUsuario() {
        try {
            if (vista.getTxtIdUsuario().getText().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Seleccione un usuario para actualizar.");
                return;
            }

            ModeloUsuario usuario = construirUsuarioDesdeFormulario();
            usuario.setIdUsuario(Integer.parseInt(vista.getTxtIdUsuario().getText()));

            boolean exito = servicio.actualizarUsuario(usuario);
            if (exito) {
                JOptionPane.showMessageDialog(vista, "Usuario actualizado correctamente.");
                cargarUsuarios();
                limpiarCampos();
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(vista, "ID de usuario inválido.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al actualizar usuario: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        try {
            if (vista.getTxtIdUsuario().getText().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Seleccione un usuario para eliminar.");
                return;
            }
            int id = Integer.parseInt(vista.getTxtIdUsuario().getText());

            int confirm = JOptionPane.showConfirmDialog(vista, "¿Está seguro de eliminar este usuario?");
            if (confirm == JOptionPane.YES_OPTION) {
                boolean exito = servicio.eliminarUsuario(id);
                if (exito) {
                    JOptionPane.showMessageDialog(vista, "Usuario eliminado correctamente.");
                    cargarUsuarios();
                    limpiarCampos();
                }
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(vista, "ID de usuario inválido.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al eliminar usuario: " + e.getMessage());
        }
    }

    private ModeloUsuario construirUsuarioDesdeFormulario() {
        ModeloUsuario usuario = new ModeloUsuario();

        usuario.setNombre(vista.getTxtNombre().getText());
        usuario.setApellido(vista.getTxtApellido().getText());
        usuario.setCorreo(vista.getTxtCorreo().getText());
        usuario.setUsername(vista.getTxtUserName().getText());
        usuario.setPass(vista.getTxtPass().getText());

       

        usuario.setPasswordResetToken(null);
        usuario.setPasswordResetExpires(null);

        return usuario;
    }

    private void llenarFormularioDesdeTabla() {
        int filaVista = vista.getTableUsuarios().getSelectedRow();
        if (filaVista != -1) {
            int filaModelo = vista.getTableUsuarios().convertRowIndexToModel(filaVista);
            DefaultTableModel modelo = (DefaultTableModel) vista.getTableUsuarios().getModel();

            vista.getTxtIdUsuario().setText(modelo.getValueAt(filaModelo, 0).toString());
            vista.getTxtNombre().setText(modelo.getValueAt(filaModelo, 1).toString());
            vista.getTxtApellido().setText(modelo.getValueAt(filaModelo, 2).toString());
            vista.getTxtCorreo().setText(modelo.getValueAt(filaModelo, 3).toString());
            vista.getTxtUserName().setText(modelo.getValueAt(filaModelo, 4).toString());

            
        } else {
            limpiarCampos();
        }
    }

    private void limpiarCampos() {
        vista.getTxtIdUsuario().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtApellido().setText("");
        vista.getTxtCorreo().setText("");
        vista.getTxtUserName().setText("");
        vista.getTxtPass().setText("");
       

        vista.getTxtNombre().requestFocus();

        vista.getBtnEliminar().setEnabled(false);
        vista.getBtnActualizar().setEnabled(false);
        vista.getBtnRegistrar().setEnabled(true);
    }

    private void ocultarColumnas(int[] columnas) {
        for (int colIndex : columnas) {
            if (colIndex >= 0 && colIndex < vista.getTableUsuarios().getColumnModel().getColumnCount()) {
                vista.getTableUsuarios().getColumnModel().getColumn(colIndex).setMinWidth(0);
                vista.getTableUsuarios().getColumnModel().getColumn(colIndex).setMaxWidth(0);
                vista.getTableUsuarios().getColumnModel().getColumn(colIndex).setWidth(0);
                vista.getTableUsuarios().getColumnModel().getColumn(colIndex).setPreferredWidth(0);
            }
        }
    }
}