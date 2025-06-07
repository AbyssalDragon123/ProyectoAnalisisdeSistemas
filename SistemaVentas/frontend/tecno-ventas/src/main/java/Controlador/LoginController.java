package Controlador;

import Vista.ViewLogin;
import Vista.MenuPrincipal;
import Modelos.ModeloLogin;
import Modelos.SesionUsuario;
import Servicio.ServiceLogin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author C-Orozco
 */
public class LoginController {

    private ViewLogin viewLogin;
    private ServiceLogin servicio;

    // Constructor que recibe la vista y el servicio
    public LoginController(ViewLogin viewLogin, ServiceLogin servicio) {
        this.viewLogin = viewLogin;
        this.servicio = servicio;

        // Mostrar la vista del login
        this.viewLogin.setVisible(true);

        // Agregar el listener al botón de login
        this.viewLogin.getBtnLogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginActionPerformed(e);
            }
        });
    }

    // Método que se ejecuta al hacer clic en el botón de login
    public void loginActionPerformed(ActionEvent e) {
        // Obtener usuario y contraseña del formulario
        String username = viewLogin.getTxtUsuario().getText();
        String password = new String(viewLogin.jPasswordField().getPassword());

        // Autenticación mediante el servicio
        ModeloLogin usuario = ServiceLogin.autenticar(username, password);

        if (usuario != null) {
            System.out.println("Usuario autenticado: " + usuario.getUsername());
            JOptionPane.showMessageDialog(viewLogin, "Bienvenido!!  " + usuario.getUsername());
            // Guardar en la sesión
            SesionUsuario.nombreUsuario = usuario.getUsername();

            // Crear e iniciar el formulario principal, pasando el nombre del usuario
            MenuPrincipal menu = new MenuPrincipal();
            menu.setLocationRelativeTo(null); // Centrar formulario
            menu.setVisible(true); // Mostrar menú
            viewLogin.dispose(); // Cerrar login

        } else {
            JOptionPane.showMessageDialog(viewLogin, "Nombre de usuario o contraseña inválidos", "Credenciales incorrectas", JOptionPane.ERROR_MESSAGE);
        }
    }
}
