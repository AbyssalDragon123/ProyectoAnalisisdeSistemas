package util;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class StockCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Resaltar toda la fila si el stock es bajo (columna 3 = "Cantidad")
        Object cantidadObj = table.getValueAt(row, 3);
        if (cantidadObj instanceof Integer) {
            int cantidad = (Integer) cantidadObj;
            if (cantidad <= 5) {
                c.setBackground(new Color(255, 204, 204)); // Rosado claro
            } else {
                c.setBackground(Color.WHITE); // Normal
            }
        }

        // Mostrar símbolo de moneda en la columna de precio (columna 2)
        if (column == 2 && value instanceof Number) {
            double precio = ((Number) value).doubleValue();
            setText(String.format("Q %.2f", precio));
        }

        return c;
    }
}
