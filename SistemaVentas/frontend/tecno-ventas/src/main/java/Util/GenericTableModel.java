package Util;



import javax.swing.table.DefaultTableModel;

public class GenericTableModel extends DefaultTableModel {

    private Class<?>[] columnTypes;

    // Constructor recibe datos, nombres de columnas y tipos de columnas
    public GenericTableModel(Object[][] data, Object[] columnNames, Class<?>[] columnTypes) {
        super(data, columnNames);
        this.columnTypes = columnTypes;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // Todas las celdas no editables
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnTypes != null && columnIndex < columnTypes.length) {
            return columnTypes[columnIndex];
        }
        return Object.class;
    }
}