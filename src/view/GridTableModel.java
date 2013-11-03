package view;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.microsoft.sqlserver.jdbc.SQLServerResultSetMetaData;

/**
 * This model - base for represent database tables.
 * DB tables are use this model, how resourse.
 *
 * @param resultSet - data from database to fill the GUI table
 * @param tableTitles - titles to GUI table
 */
public class GridTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private ArrayList<String> columnNames      = new ArrayList<String>(); // use for table titles
    private ArrayList<String> columnAliases    = new ArrayList<String>(); // use for sql query
    private ArrayList<Class> columnTypes       = new ArrayList<Class>();
    private ArrayList<ArrayList<Object>> data  = new ArrayList<ArrayList<Object>>();

    public int getRowCount() {
        synchronized (data) {
            return data.size();
        }
    }

    public int getColumnCount() {
        return columnNames.size();
    }

    public Object getValueAt(int row, int col) {
        synchronized (data) {
            return data.get(row).get(col);
        }
    }

    public String getColumnName(int col) {
        return columnNames.get(col);
    }

    public String getColumnAlias(int col) {
        return columnAliases.get(col);
    }

    public Class<?> getColumnClass(int col) {
        return columnTypes.get(col);
    }

    public Object removeRow(int row) {
        synchronized (data) {
            return data.remove(row);
        }
    }

    public void addRow(ArrayList<Object> rowData) {
        synchronized (data) {
            data.add(rowData);
            this.fireTableRowsInserted(data.size() - 1, data.size() - 1);
        }
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public void setValueAt(Object obj, int row, int col) {
        synchronized (data) {
            data.get(row).set(col, obj);
        }
    }

    /**
     * Core of the model. Initializes column names, types, data from ResultSet.
     *
     * @param rs ResultSet from which all information for model is token.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void setDataSource(ResultSet rs, String[] tableTitles) {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            columnNames.clear();
            columnAliases.clear();
            columnTypes.clear();
            data.clear();
            int columnCount = rsmd.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                columnAliases.add(rsmd.getColumnName(i + 1)); // use for sql query
                columnNames.add(tableTitles[i]);              // use for table titles
                Class<?> type = Class.forName(rsmd.getColumnClassName(i + 1));
                columnTypes.add(type);
            }
            fireTableStructureChanged();
            while (rs.next()) {
                ArrayList<Object> rowData = new ArrayList<Object>();
                for (int i = 0; i < columnCount; i++) {
                    if (columnTypes.get(i) == String.class)
                        rowData.add(rs.getString(i + 1));
                    else
                        rowData.add(rs.getObject(i + 1));
                }
                synchronized (data) {
                    data.add(rowData);
                    this.fireTableRowsInserted(data.size() - 1, data.size() - 1);
                }
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    public GridTableModel(ResultSet resultSet, String[] tableTitles) throws SQLException, ClassNotFoundException {
        this.setDataSource(resultSet, tableTitles);
    }
}
