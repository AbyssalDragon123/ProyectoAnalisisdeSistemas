package Controlador;

import Servicio.ServiceUsuario;
import Vista.ViewUsuario;
import Modelos.ModeloUsuario;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class UsuarioController {

    private ViewUsuario vista;

    public UsuarioController(ViewUsuario vista) {
        this.vista = vista;
        mostrarUsuarios(); // Mostrar al iniciar
    }

    private void mostrarUsuarios() {
        List<ModeloUsuario> usuarios = ServiceUsuario.getAllUsuarios();
        
        // Crear nuevo modelo y definir columnas
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Correo");
        modelo.addColumn("Username");
        modelo.addColumn("Rol");

        if (usuarios != null) {
            for (ModeloUsuario u : usuarios) {
                String nombreRol;
                    switch (u.getRol()) {
                    case "0": nombreRol = "Admin"; break;
                    case "1": nombreRol = "Vendedor"; break;
                    case "2": nombreRol = "Cajero"; break;
                    
                        default: nombreRol = "Desconocido"; break;
                    }
                modelo.addRow(new Object[]{
                    u.getIdUsuario(),
                    u.getNombre(),
                    u.getApellido(),
                    u.getCorreo(),
                    u.getUsername(),
                    nombreRol
                });
            }
        }
        
        //asignar el modelo a la tabla
        vista.getTableUsuarios().setModel(modelo);
    }
}




