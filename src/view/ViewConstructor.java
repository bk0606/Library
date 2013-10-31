package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import view.handlers.DatabaseEventHandler;
import view.handlers.FrameActionListener;
import model.IDatabaseController;

/**
 * Class for connecting GUI tables and database tables. 
 * And creates a frame of them.
 * 
 * @param appName - title of whole window
 * @param dbController - implementation of interface IDatabaseController, 
 * which can perform database transactions
 */
public class ViewConstructor {
	private JTable guiTable 		 		 = null;
	private JTabbedPane tabbedPane 	 		 = new JTabbedPane();
	private JFrame frame  			 		 = null;
	private IDatabaseController dbController = null;
	private String appName 					 = null;
	
	/** Set relation between GUI table and DB table and create view.
	 * @param title - title of current tab bar
	 * @param dbTable - name of DB table
	 * @param tableTitles - titles for GUI table
	 */
	public void createGridView(String title, String dbTable, String[] tableTitles) {
		JPanel basePanel = new JPanel(new BorderLayout());
		// Designing window
		JPanel tblPanel = new JPanel(new BorderLayout(5, 5));
		tblPanel.setPreferredSize(new Dimension(600, 300));		
		// Set resource and create JTable
		JScrollPane scrolledTable = this.createTable(dbTable, tableTitles);
		tblPanel.add(scrolledTable, BorderLayout.NORTH);
		// Create buttons
		JPanel btnPanel = new JPanel();
		btnPanel.add(this.createButton("Добавить запись", "addCmd", dbTable));
		btnPanel.add(this.createButton("Удалить запись", "removeCmd", dbTable));
		// Construct main panel
        basePanel.add(tblPanel, BorderLayout.NORTH);
        basePanel.add(btnPanel, BorderLayout.SOUTH);
        // Add tab if tabbedPane is exists else create new  
        tabbedPane.addTab(title, null, basePanel, title);
        // Create frame if it not exists else create new
        frame = createFrame(title, basePanel);
        frame.pack();
	}
	/**
	 * Creates button, set the action command (add, delete or else),
	 * create and link with action listener.
	 * @param title - title for button
	 * @param command - says event hadler what to do (add, delete or else)
	 * @param dbTable - name of DB table where send sql queries 
	 */
	public JButton createButton(String title, String command, String dbTable) {
		JButton btn = new JButton(title);
		btn.setActionCommand(command);
		DatabaseEventHandler handler = new DatabaseEventHandler(dbController, dbTable, guiTable);
		FrameActionListener listener = new FrameActionListener(handler);
		btn.addActionListener(listener);
		return btn;
	}
	/**
	 * Get data from db creates table model and create filled table.
	 */
	public JScrollPane createTable(String dbTable, String[] tableTitles) {
		AbstractTableModel tableModel = null;
		try {
			ResultSet tableResourse = dbController.select(dbTable);
			tableModel = new GridTableModel(tableResourse, tableTitles);
			guiTable   = new JTable(tableModel);
			guiTable.setPreferredSize(new Dimension(600, 300));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new JScrollPane(guiTable);
	}
	/**
	 * Creates frame if it not exists.
	 * @param title - title of all window
	 * @param panel - main panel, contains all desgin
	 */
	public JFrame createFrame(String title, JPanel panel) {
		if (frame != null) {
			return frame;
		} else {			
	        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        int sizeWidth = screenSize.width/2;
	        int sizeHeight = screenSize.height/2;
	        int locationX = (screenSize.width - sizeWidth) / 2;
	        int locationY = (screenSize.height - sizeHeight) / 2;
	        frame = new JFrame(appName);
	        frame.setBounds(locationX, locationY, sizeWidth, sizeHeight);
	        frame.setVisible(true);
	        frame.add(tabbedPane);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        return frame;
		}
	}
	
	public ViewConstructor(String appName, IDatabaseController dbController) {
		this.appName = appName;
		this.dbController = dbController;
	}
}
