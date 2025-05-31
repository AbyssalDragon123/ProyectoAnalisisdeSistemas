package Controlador;

import Modelos.ModeloUsuario;
import Servicio.ServiceUsuario;
import Util.GeneradorContrasena;
import Util.EmailSender;
import Vista.ViewUsuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import Util.GeneradorUsername;
import java.awt.BorderLayout;

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

        //Boton para enviar correo con usuario y contraseña
        vista.getBtnEnviarEmail().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarCorreoDesdeFormulario();
            }
        });

        //llamando el boton desde la vista sin anotacion labda
        vista.getBtnGenerar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nuevaContrasena = GeneradorContrasena.generar(8); // Ajustar la longitud de la contraseña
                vista.getTxtPass().setText(nuevaContrasena);//setear la contraseña al campo password
            }
        });

        vista.getBtnGenerarUser().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = vista.getTxtNombre().getText();
                String apellido = vista.getTxtApellido().getText();

                String username = GeneradorUsername.generarNombreUsuario(nombre, apellido);

                vista.getTxtUserName().setText(username); //enviar el nombre de usuario al campo username
            }
        });

        vista.getTableUsuarios().getSelectionModel().addListSelectionListener(e -> {//seleccionar registro
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

            System.out.println("usuarios obtenidos" + usuarios);

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
                    u.getRol(),
                    u.getPass(),
                    u.getPasswordResetToken(),
                    u.getPasswordResetExpires()
                });
            }

            vista.getTableUsuarios().setModel(modelo);
            ocultarColumnas(new int[]{0,}); // Ocultar ID, Pass, Token y Expiración

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar usuarios: " + e.getMessage());
        }
    }

    private void agregarUsuario() {
        try {
            if (vista.getTxtNombre().getText().trim().isEmpty()
                    || vista.getTxtApellido().getText().trim().isEmpty()
                    || vista.getTxtCorreo().getText().trim().isEmpty()
                    || vista.getTxtUserName().getText().trim().isEmpty()
                    || vista.getTxtPass().getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.");
                return;

            }

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
        usuario.setRol(vista.getComboBoxRol().getSelectedItem().toString());
        usuario.setPasswordResetToken(null);
        usuario.setPasswordResetExpires(null);

        return usuario;
    }

    //Seleccionar fila y rellenar el formulario con los datos obtenidos
    private void llenarFormularioDesdeTabla() {
        int filaVista = vista.getTableUsuarios().getSelectedRow();
        if (filaVista != -1) {
            int filaModelo = vista.getTableUsuarios().convertRowIndexToModel(filaVista);
            DefaultTableModel modelo = (DefaultTableModel) vista.getTableUsuarios().getModel();

            // Obtener valores desde la tabla
            String id = modelo.getValueAt(filaModelo, 0).toString();
            String nombre = modelo.getValueAt(filaModelo, 1).toString();
            String apellido = modelo.getValueAt(filaModelo, 2).toString();
            String correo = modelo.getValueAt(filaModelo, 3).toString();
            String username = modelo.getValueAt(filaModelo, 4).toString();
            String rol = modelo.getValueAt(filaModelo, 5).toString();
            String pass = modelo.getValueAt(filaModelo, 6).toString();

            // Setear los valores en los campos
            vista.getTxtIdUsuario().setText(id);
            vista.getTxtNombre().setText(nombre);
            vista.getTxtApellido().setText(apellido);
            vista.getTxtCorreo().setText(correo);
            vista.getTxtUserName().setText(username);
            vista.getTxtPass().setText(pass); //contraseña con texto plano
            // Setear rol en el ComboBox
            vista.getComboBoxRol().setSelectedItem(rol);

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

    //metodo para el envio de correo
    private void enviarCorreoDesdeFormulario() {
        String destinatario = vista.getTxtCorreo().getText().trim();
        String username = vista.getTxtUserName().getText().trim();
        String password = vista.getTxtPass().getText().trim();
        String nombre = vista.getTxtNombre().getText().trim();

        if (destinatario.isEmpty() || username.isEmpty() || password.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, complete todos los campos necesarios para enviar el correo.");
            return;
        }

        String asunto = "Tus credenciales de acceso";
        String cuerpo = "Hola " + nombre + ",\n\n"
                + "Aquí están tus credenciales de acceso:\n\n"
                + "Nombre de usuario: " + username + "\n"
                + "Contraseña: " + password + "\n\n"
                + "Por favor, cambia tu contraseña después de iniciar sesión.\n\n"
                + "Gracias.";

        // Crear un JDialog para mostrar mientras se envía el correo
        JDialog dialogoEspera = new JDialog(vista, "Enviando correo", true);
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Cargar la imagen
        ImageIcon icono = new ImageIcon(getClass().getResource("/enviando.png")); // Usa un .gif si quieres animación
        JLabel etiquetaImagen = new JLabel(icono);
        panel.add(etiquetaImagen, BorderLayout.WEST);

        JLabel etiquetaTexto = new JLabel("Enviando correo, por favor espera...");
        etiquetaTexto.setVerticalAlignment(SwingConstants.CENTER);
        panel.add(etiquetaTexto, BorderLayout.CENTER);

        dialogoEspera.getContentPane().add(panel);
        dialogoEspera.pack();
        dialogoEspera.setLocationRelativeTo(vista);

        // Worker en segundo plano
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                EmailSender.sendEmail(destinatario, asunto, cuerpo);
                return null;
            }

            @Override
            protected void done() {
                dialogoEspera.dispose(); // Cerrar diálogo de espera

                try {
                    get(); // Verifica si hubo alguna excepción
                    JOptionPane.showMessageDialog(vista, "Correo enviado correctamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vista, "Error al enviar correo: " + ex.getMessage());
                }
            }
        };

        // Ejecutar worker y mostrar diálogo
        worker.execute();
        dialogoEspera.setVisible(true);
    }

}
