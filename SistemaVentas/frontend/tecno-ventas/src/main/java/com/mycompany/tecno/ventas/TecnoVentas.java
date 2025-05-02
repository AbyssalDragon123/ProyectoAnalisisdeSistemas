package com.mycompany.tecno.ventas;

import Configuracion.Conexion;
import Vistas.MenuPrincipal;

public class TecnoVentas {
    public static void main(String[] args) {
        // 1. Establecer conexión
        Conexion conexion = new Conexion();
        java.sql.Connection conn = conexion.estableceConexion();
        
        // 2. Verificar conexión antes de abrir el menú
        if(conn != null) {
            try {
                // 3. Crear el formulario de MenuPrincipal
                MenuPrincipal menu = new MenuPrincipal();
                MenuPrincipal ventana = new MenuPrincipal();
                // 4. Asegurarse de que el formulario tiene un tamaño adecuado
                menu.setVisible(true);    // Mostrar el formulario
                
                // 5. Forzar que la ventana esté al frente
                menu.toFront();
                
                // 6. Al cerrar la aplicación, cerrar la conexión
                menu.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        conexion.cerrarConexion(conn);
                    }
                });
                
            } catch (Exception e) {
                System.err.println("Error al iniciar la aplicación: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("No se pudo iniciar la aplicación por falta de conexión a BD");
        }
    }
}
