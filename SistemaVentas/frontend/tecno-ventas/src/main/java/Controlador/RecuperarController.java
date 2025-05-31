package Controlador;

import Modelos.ModeloRecuperarContrasena;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Vista.ViewRecuperarContrasena;
import Servicio.ServiceRecuperarContrasena;
import com.mysql.cj.CoreSession;
import java.awt.Component;

public class RecuperarController {

    private ViewRecuperarContrasena Vista;
    private ServiceRecuperarContrasena service;

    public RecuperarController(ViewRecuperarContrasena Vista, ServiceRecuperarContrasena service) {

        this.Vista = Vista;
        this.service = service;

        // Acción para enviar código de recuperación por correo
        this.Vista.getBtnEnviarCodigo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarCodigo();
            }
        });

        // Acción para restablecer la contraseña
        this.Vista.getBtnRestablecer().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restablecerPassword();
            }
        });
    }

    private void enviarCodigo() {
    String correo = Vista.getTxtCorreo().getText().trim();
    System.out.println("Correo obtenido: " + correo);

    if (correo.isEmpty()) {
        JOptionPane.showMessageDialog(Vista, "Por favor ingresa tu correo electrónico.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    JDialog dialogoEspera = crearDialogoEspera((JFrame) SwingUtilities.getWindowAncestor(Vista), "Enviando correo, por favor espera...");
    System.out.println("Mostrando diálogo de espera");

    SwingWorker<Boolean, Void> worker = new SwingWorker<>() {

        @Override
        protected Boolean doInBackground() {
            System.out.println("Ejecutando SwingWorker...");
            boolean resultado = service.enviarToken(correo);
            System.out.println("Resultado desde el servicio: " + resultado);
            return resultado;
        }

        @Override
        protected void done() {
            dialogoEspera.dispose();
            System.out.println("Cerrando diálogo de espera");

            try {
                boolean enviado = get();
                System.out.println("Resultado final obtenido: " + enviado);

                if (enviado) {
                    JOptionPane.showMessageDialog(Vista, "Se envió el enlace de recuperación al correo.");
                    Vista.getBtnRestablecer().setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(Vista, "No se pudo enviar el correo. Intenta más tarde.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                System.out.println("Excepción en SwingWorker: " + e.getMessage());
                JOptionPane.showMessageDialog(Vista, "Error al enviar el correo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    };

    worker.execute();
    dialogoEspera.setVisible(true);
    System.out.println("SwingWorker ejecutado");
}

private void restablecerPassword() {
    String username = Vista.getTxtUserName().getText().trim();
    String correo = Vista.getTxtCorreo().getText().trim();
    String codigo = Vista.getTxtCodigo().getText().trim();
    String nuevaPass = new String(Vista.getTxtNewPassword().getPassword()).trim();
    String repetirPass = new String(Vista.getTxtRepetirPassword().getPassword()).trim();

    System.out.println("Usuario ingresado: " + username);
    System.out.println("Correo ingresado: " + correo);
    System.out.println("Código/token ingresado: " + codigo);
    System.out.println("Nueva contraseña: " + nuevaPass);
    System.out.println("Repetir contraseña: " + repetirPass);

    // Validar campos vacíos
    if (username.isEmpty() || correo.isEmpty() || codigo.isEmpty() || nuevaPass.isEmpty() || repetirPass.isEmpty()) {
        JOptionPane.showMessageDialog(Vista, "Completa todos los campos para restablecer la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
        System.out.println("Faltan campos obligatorios.");
        return;
    }

    // Validar que las contraseñas coincidan
    if (!nuevaPass.equals(repetirPass)) {
        JOptionPane.showMessageDialog(Vista, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
        System.out.println("Las contraseñas no coinciden.");
        return;
    }

    try {
        System.out.println("Enviando solicitud para actualizar contraseña...");
        boolean actualizado = service.resetearContrasena(username, correo, codigo, nuevaPass);
        System.out.println("Resultado de la solicitud: " + actualizado);

        if (actualizado) {
            JOptionPane.showMessageDialog(Vista, "Contraseña actualizada correctamente.");
            limpiarCampos();
            Vista.getBtnRestablecer().setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(Vista, "Código inválido, expirado o datos incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Token inválido o expirado.");
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(Vista, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

    private void limpiarCampos() {
        Vista.getTxtCodigo().setText("");
        Vista.getTxtNewPassword().setText("");
        Vista.getTxtRepetirPassword().setText("");
    }

    //ventana de espera mientras se envia el correo
    private JDialog crearDialogoEspera(JFrame parent, String mensaje) {
        JDialog dialog = new JDialog(parent, "Enviando...", true);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Texto
        JLabel lblMensaje = new JLabel(mensaje, SwingConstants.CENTER);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Imagen (asegúrate de que la ruta sea válida)
        ImageIcon icono = new ImageIcon(getClass().getResource("/enviando.png")); // <-- adapta la ruta si es necesario
        JLabel lblImagen = new JLabel(icono);
        lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Añadir al panel
        panel.add(lblImagen);
        panel.add(Box.createVerticalStrut(10)); // espacio entre imagen y texto
        panel.add(lblMensaje);

        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        return dialog;
    }

}
