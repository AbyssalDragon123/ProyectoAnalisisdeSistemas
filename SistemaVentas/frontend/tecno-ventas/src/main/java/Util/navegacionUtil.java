package Util;

import javax.swing.JFrame;
import javax.swing.JRootPane;

public class navegacionUtil {

    // Método para quitar (cerrar, minimizar, maximizar)
    public static void quitarDecoracionVentana(JFrame frame) {
        frame.setUndecorated(true); // Elimina la barra de título y botones
        frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    }

    // Método desactivar botones
    public static void desactivarControlesVentana(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.out.println("Cerrar deshabilitado.");
            }
        });

        frame.addWindowStateListener(e -> {
            int nuevoEstado = e.getNewState();
            if ((nuevoEstado & JFrame.ICONIFIED) == JFrame.ICONIFIED ||
                (nuevoEstado & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
                frame.setState(JFrame.NORMAL);
                System.out.println("Minimizar o maximizar deshabilitado.");
            }
        });
    }
}
