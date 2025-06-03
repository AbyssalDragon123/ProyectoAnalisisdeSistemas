package com.mycompany.tecno.ventas;

//import Configuracion.Conexion;
import Servicio.ServiceLogin;
import Vista.MenuPrincipal;
import Vista.ViewLogin;
import Controlador.LoginController;

public class TecnoVentas {

    public static void main(String[] args) {

        //cargar servicios para el login
        ViewLogin vista = new ViewLogin();
        ServiceLogin servicio = new ServiceLogin();
        LoginController controller = new LoginController(vista, servicio);

        vista.setVisible(true);

    }
}
