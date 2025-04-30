package Configuracion;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/tecno_ventas";
    private static final String USER = "root";
    private static final String PASSWORD = "Guatemala2025";
    
    public Connection estableceConexion() {
        Connection conn = null;
        try {
            // Cargar el driver (nueva forma para MySQL 8+)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establecer conexión con parámetros adicionales
            String connectionUrl = URL + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            conn = DriverManager.getConnection(connectionUrl, USER, PASSWORD);
            
            JOptionPane.showMessageDialog(null, "¡Conexión exitosa!");
            return conn;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al conectar: " + e.getMessage(), 
                "Error de conexión", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }
    
    public void cerrarConexion(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                JOptionPane.showMessageDialog(null, "Conexión cerrada");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cerrar conexión: " + e.getMessage());
        }
    }
}