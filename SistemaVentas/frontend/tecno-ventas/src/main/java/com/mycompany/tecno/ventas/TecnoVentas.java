/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tecno.ventas;

/**
 *
 * @author Admin
 */
public class TecnoVentas {

    public static void main(String[] args) {
       Configuracion.Conexion objetoConexion = new Configuracion.Conexion();
        objetoConexion.estableceConexion();
        
        Formularios.MenuPrincipal objetoMenuPrincipal = new Formularios.MenuPrincipal();
        objetoMenuPrincipal.setVisible(true);
    }
}
