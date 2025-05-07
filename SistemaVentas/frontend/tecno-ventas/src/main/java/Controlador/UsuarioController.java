/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

/**
 *
 * @author C-Orozco
 */

import Modelos.ModeloUsuario;  //se importa el modelo
import Servicio.ServiceLogin;  //se importa el servicio
import Vista.ViewLogin;        //se importa la vista (formulario)

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;




public class UsuarioController {
    

public class LoginController {

    // Referencia a la vista (el formulario de login)
    private final LoginForm loginForm;

    // Constructor del controlador: recibe el formulario y configura el botón de login
    public LoginController(LoginForm loginForm) {
        this.loginForm = loginForm;

        // Asociamos el evento "click" al botón de login
        this.loginForm.getBtnLogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();  // Llamamos al método que intentará iniciar sesión
            }
        });
    }

    //Método para manejar el proceso de inicio de sesión
    
    private void iniciarSesion() {
        // Obtenemos el texto ingresado en los campos del formulario
        String username = loginForm.getTxtUsuario().getText();
        char[] passwordChars = loginForm.getTxtPassword().getPassword();  // se obtiene como arreglo de chars
        String password = new String(passwordChars);  // lo convertimos a String

        //Validación simple: verificar que los campos no estén vacíos
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese usuario y contraseña");
            return;  // detenemos el flujo si están vacíos
        }

        // Llamamos al servicio que consulta la API para autenticar
        modeloUsuario usuario = LoginService.autenticar(username, password);

        // Verificamos la respuesta del servicio
        if (usuario != null) {
            //Si el usuario es válido, mostramos un mensaje y podríamos abrir el menú principal
            JOptionPane.showMessageDialog(null, "¡Bienvenido, " + usuario.getNombre() + "!");

            // Aquí iría el código para abrir otra ventana, ejemplo:
            // MenuPrincipal menu = new MenuPrincipal(usuario);
            // menu.setVisible(true);
            // loginForm.dispose(); // cerrar el formulario de login
        } else {
            //Si las credenciales son incorrectas
            JOptionPane.showMessageDialog(null, "Credenciales incorrectas");
        }
    }
}

    
    
    
}
