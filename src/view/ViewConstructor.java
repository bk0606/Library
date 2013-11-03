package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import com.michaelbaranov.microba.calendar.DatePicker;
import view.handlers.ArrowsEventHandler;
import view.handlers.GreedEventHandler;
import view.handlers.FrameActionListener;
import view.handlers.IActionHandler;
import view.handlers.SearchEventHandler;
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
    private JTable guiTable                  = null;
    private JTabbedPane tabbedPane           = new JTabbedPane();
    private JFrame frame                     = null;
    private IDatabaseController dbController = null;
    private String appName                   = null;

    /**
     * Set relation between GUI table and DB table and create view.
     * @param title - title of current tab bar
     * @param tableTitles - titles for GUI table
     * @param dbTable - name of DB table
     * @param tableRes - resourse for table model
     */
    public void createGridView(String title, String[] tableTitles, String dbTable, ResultSet tableRes) {
        JPanel basePanel = new JPanel(new BorderLayout());
        // Designing window
        JPanel tblPanel = new JPanel(new BorderLayout(5, 5));
        tblPanel.setPreferredSize(new Dimension(600, 300));
        // Set resource and create JTable
        JScrollPane scrolledTable = this.createTable(tableRes, tableTitles);
        tblPanel.add(scrolledTable, BorderLayout.NORTH);
        // Create buttons
        JPanel formPanel = new JPanel(new GridLayout(2, 0, 10, 10));
        JPanel btnPanel = new JPanel();
        JPanel arrPanel = new JPanel();
        IActionHandler dbHandler = new GreedEventHandler(dbController, dbTable, guiTable);
        btnPanel.add(this.createButton("Добавить запись", "addCmd", dbTable, dbHandler));
        btnPanel.add(this.createButton("Удалить запись", "removeCmd", dbTable, dbHandler));
        IActionHandler guiHandler = new ArrowsEventHandler(guiTable);
        arrPanel.add(this.createButton("<", "left", "", guiHandler));
        arrPanel.add(this.createButton(">", "right", "", guiHandler));
        arrPanel.add(this.createButton("∧", "up", "", guiHandler));
        arrPanel.add(this.createButton("∨", "down", "", guiHandler));
        formPanel.add(arrPanel);
        formPanel.add(btnPanel);
        // Construct main panel
        basePanel.add(tblPanel, BorderLayout.NORTH);
        basePanel.add(formPanel, BorderLayout.SOUTH);
        // Add tab if tabbedPane is exists else create new
        tabbedPane.addTab(title, null, basePanel, title);
        // Create frame if it not exists else create new
        frame = createFrame(title, basePanel);
        frame.pack();
    }
    /**
     * Creates search view.
     * @param title - title of current tab bar
     * @param tableTitles - titles for GUI table
     * @param dbTable - name of DB table
     * @param tableRes - resourse for table model
     */
    public void createSearchView(String title, String[] tableTitles, String dbTable, ResultSet tableRes) {
        JPanel basePanel = new JPanel(new BorderLayout());
        // Create grid table and add to panel
        JScrollPane table = this.createTable(tableRes, tableTitles);
        JPanel tblPanel = new JPanel(new BorderLayout(5, 5));
        tblPanel.setPreferredSize(new Dimension(600, 300));
        tblPanel.add(table, BorderLayout.NORTH);
        // Create text areaes with labels
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        this.addFormLine(formPanel, "C: ",  "from");
        this.addFormLine(formPanel, "По: ", "to");
        formPanel.add(new JLabel("№ пользователя: "));
        JTextField uNameField = new JTextField(20);
        uNameField.setName("userName");
        formPanel.add(uNameField);
        // Create radiobuttons
        JRadioButton radioAlph = new JRadioButton("Названию", true);
        radioAlph.setName("radioName");
        JRadioButton radioNum = new JRadioButton("Номерам");
        radioNum.setName("radioNum");
        ButtonGroup radioGr = new ButtonGroup();
        radioGr.add(radioAlph);
        radioGr.add(radioNum);
        formPanel.add(new JLabel("Сортировать по: "));
        formPanel.add(radioAlph);
        formPanel.add(new JLabel(" "));
        formPanel.add(radioNum);
        // Create button
        JPanel btnPanel = new JPanel();
        IActionHandler handler = new SearchEventHandler(dbController, dbTable, guiTable, formPanel);
        btnPanel.add(this.createButton("Найти", "find", dbTable, handler));
        // Desgin
        basePanel.add(formPanel, BorderLayout.NORTH);
        basePanel.add(btnPanel, BorderLayout.CENTER);
        basePanel.add(tblPanel, BorderLayout.SOUTH);
        // Add to tab pane
        tabbedPane.addTab(title, null, basePanel, title);
        frame = createFrame(title, basePanel);
        frame.pack();
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
            int locationX = (screenSize.width - sizeWidth) / 2 - 100;
            int locationY = (screenSize.height - sizeHeight) / 2 - 100;
            frame = new JFrame(appName);
            frame.setBounds(locationX, locationY, sizeWidth, sizeHeight);
            frame.setVisible(true);
            frame.add(tabbedPane);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            return frame;
        }
    }
    /** Get data from db creates table model and create filled table. */
    public JScrollPane createTable(ResultSet tableRes, String[] tableTitles) {
        AbstractTableModel tableModel = null;
        try {
            tableModel = new GridTableModel(tableRes, tableTitles);
            guiTable   = new JTable(tableModel);
            guiTable.setCellSelectionEnabled(true);
            guiTable.setRowSelectionAllowed(true);
            guiTable.setPreferredSize(new Dimension(600, 300));
            tableRes.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new JScrollPane(guiTable);
    }
    /**
     * Creates button, set the action command (add, delete or else),
     * create and link with action listener.
     * @param title - title for button
     * @param command - says event hadler what to do (add, delete or else)
     * @param dbTable - name of DB table where send sql queries
     * @param handler - implem-on of IActionHandler, provide click handle
     */
    public JButton createButton(String title, String command, String dbTable, IActionHandler handler) {
        JButton btn = new JButton(title);
        btn.setActionCommand(command);
        FrameActionListener listener = new FrameActionListener(handler);
        btn.addActionListener(listener);
        return btn;
    }
    /** Create labels & txt fields */
    public void addFormLine(JPanel panelToAdd, String label, String txtFieldCmd) {
        panelToAdd.add(new JLabel(label));
        DatePicker pck = new DatePicker();
        pck.setName(txtFieldCmd);
        panelToAdd.add(pck);
    }

    public ViewConstructor(String appName, IDatabaseController dbController) {
        this.appName = appName;
        this.dbController = dbController;
    }
}
