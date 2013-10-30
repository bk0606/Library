package view.handlers;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import view.DatabaseTableModel;
import model.IDatabaseController;

public class DatabaseEventHandler implements IActionHandler {
	private IDatabaseController dbInst    = null;
	private String dbTable                = null;
	private JTable guiTable               = null;
	private DatabaseTableModel tableModel = null;
	private int userAnswer                = 2;    // "Cansel" answer from ConfirmDialog
	
	/** Implementation of IActionHandler interface.
	 * Realize database operations handle (add/remove row)
	 */
	@Override
	public void handleEvent(ActionEvent event) {
		tableModel = (DatabaseTableModel) guiTable.getModel();
		// Determines which operation (insert/delete) perform
		if (event.getActionCommand() == "addCmd") {
			this.addition();
		} 
		else if (event.getActionCommand() == "removeCmd") {
			this.deletion();
		}
	}
	
	/** Method for handling insert operations. */
	public void addition() {
		int colCount   = tableModel.getColumnCount();
		// Create frame and textFields for input data. 
		// Loop starts from 1 because ID column is auto incrim.
		JPanel message = new JPanel(new GridLayout(0,1));
		JTextField[] txtFields = new JTextField[colCount];
		for (int i = 1; i < colCount; i++) { 
			message.add(new JLabel(tableModel.getColumnName(i)));
			txtFields[i] = new JTextField(20);
            message.add(txtFields[i]);
		}
		// get feedback from user
		userAnswer = JOptionPane.showConfirmDialog(
				FrameActionListener.getTopFrame(), 
				message, "Добавление записи",
				JOptionPane.YES_NO_OPTION
			);
		// If answer - yes, send data to database
        if (userAnswer == JOptionPane.YES_OPTION) {
        	LinkedHashMap<String, String> tableLine = new LinkedHashMap<String, String>();
        	ArrayList<Object> rowData = new ArrayList<Object>();
        	String[] colTitles = new String[tableModel.getColumnCount()];
        	for (int i = 1; i < colCount; i++) {
        		colTitles[i] = tableModel.getColumnName(i);
        		tableLine.put(tableModel.getColumnAlias(i), txtFields[i].getText());
        		rowData.add(txtFields[i].getText());
        	}
        	dbInst.performQuery(tableLine, dbTable);
        	// Get updated table, for realtime change GUI
        	ResultSet rs = dbInst.performQuery(dbTable);
        	tableModel.setDataSource(rs, colTitles);
        	guiTable.repaint();
        }
	}
	/** Method for handling delete operations. */
	public boolean deletion() {
		int row   = guiTable.getSelectedRow();
		if (row == -1) {
			return false; // if row not select
		}
		// get row id from GUI table
		int rowId = Integer.parseInt(guiTable.getModel().getValueAt(row-1, 0).toString());
		// get feedback from user
		userAnswer = JOptionPane.showConfirmDialog(
				FrameActionListener.getTopFrame(),
				"Удалить запись №"+rowId+"?",
                "Удаление записи",
                JOptionPane.YES_NO_OPTION
			);
		// Delete row if answer - yes
		if (userAnswer == JOptionPane.YES_OPTION) {
			dbInst.performQuery(dbTable, rowId);
			tableModel.removeRow(row);
			guiTable.repaint();
        }
		return true;
	}
	/**
	 * @param controller - realization of IDatabaseController, 
	 * whitch know how to work with DB
	 * @param dbTableName - DB table name
	 * @param guiTable - GUI table in frame
	 */
	public DatabaseEventHandler(IDatabaseController controller,
				String dbTableName, JTable guiTable) {
		this.dbInst   = controller;
		this.dbTable  = dbTableName;
		this.guiTable = guiTable;
	}
}
