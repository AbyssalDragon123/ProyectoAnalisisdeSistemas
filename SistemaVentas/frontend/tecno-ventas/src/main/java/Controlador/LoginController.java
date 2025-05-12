/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.ViewLogin;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import Modelos.ModeloLogin;
import Servicio.ServiceLogin;
import Vista.MenuPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author C-Orozco
 */
public class LoginController {
    //Instancia a la vista del login
    
    private ViewLogin viewLogin;
    private ServiceLogin servicio;
    
     //Constructor que recibe la vista y el servicio
    
    public LoginController(ViewLogin viewLogin, ServiceLogin servicio) {
        
        this.viewLogin = viewLogin;
        this.servicio = servicio;

        // Configurar el botón
        this.viewLogin.setVisible(true);
        
        this.viewLogin.getBtnLogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Boton clickeado");
                loginActionPerformed(e);
            }
        });
    }
    
    // obtener usuario y contraseña del formulario
    
   /* public LoginController(){
        
        this.viewLogin = new ViewLogin(); // el objeto se ejectura correctamente.
    
    }*/
    
    public void loginActionPerformed(ActionEvent e){
    
        //obtenemos los datos del formulario
        
        String username = viewLogin.getTxtUsuario().getText();
        String password = new String(viewLogin.jPasswordField().getPassword());
        
        //llamamos el servicio de autenticación
        
        Modelos.ModeloLogin usuario = Servicio.ServiceLogin.autenticar(username, password);
        
        if (usuario != null) {
            
            System.out.println("Usuario autenticado : " + usuario.getUserName());
           
            //llamar formulario principal
            
            MenuPrincipal menu = new MenuPrincipal(); //instancia menu principal
            menu.setName(usuario.getUserName()); //pasar el nombre de usuario al formulario principal
            menu.setLocationRelativeTo(menu); //centrar formulario
            menu.setVisible(true); //mostror form
            viewLogin.dispose(); //cerrar form login
            
        }else{
        
            JOptionPane.showMessageDialog(viewLogin, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        
        }
    
    }
    
}
