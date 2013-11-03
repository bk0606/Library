package view.handlers;

import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.michaelbaranov.microba.calendar.DatePicker;

import model.IDatabaseController;
import view.GridTableModel;

/**
 * Implementation of IActionHandler, which handle
 * search operation on search form
 *
 * @param controller - realization of IDatabaseController,
 * whitch know how to work with DB
 * @param dbTable - DB table name
 * @param guiTable - GUI table in frame
 * @param form - from which contains text fields with params
 */
public class SearchEventHandler implements IActionHandler {
    private IDatabaseController dbInst = null;
    private String dbTable             = null;
    private JTable guiTable            = null;
    private JPanel form                = null;

    @Override
    public void handleEvent(ActionEvent event) {
        GridTableModel tableModel = (GridTableModel)guiTable.getModel();
        int colCount  = tableModel.getColumnCount();
        int compCount = form.getComponentCount();
        String fromDate = null;
        String toDate   = null;
        String compName = "";
        String userId   = "";
        String orderName = "";
        String orderNum  = "";
        for (int i = 0; i < compCount; i++) {
            compName = form.getComponent(i).getName();
            if (compName == "from") {
                DatePicker fr = ((DatePicker)form.getComponent(i));
                Date dt = fr.getDate();
                fromDate = new SimpleDateFormat("yyyy-MM-dd").format(dt);
            }
            if (compName == "to") {
                DatePicker fr = ((DatePicker)form.getComponent(i));
                Date dt = fr.getDate();
                toDate = new SimpleDateFormat("yyyy-MM-dd").format(dt);
            }
            if (compName == "userName")
                userId = ((JTextField)form.getComponent(i)).getText();
            if (compName == "radioName")
                orderName = ((JRadioButton)form.getComponent(i)).isSelected() ? " ORDER BY t.name " : "";
            if (compName == "radioNum")
                orderNum = ((JRadioButton)form.getComponent(i)).isSelected() ? " ORDER BY t.id " : "";
        }
        String[] colTitles = new String[colCount];
        for (int i = 0; i < colCount; i++) {
            colTitles[i] = tableModel.getColumnName(i);
        }
        try {
            String query = "SELECT * FROM "+dbTable+" AS t INNER JOIN User_books AS ub ON t.id = ub.id_book "
                            + "WHERE ub.id_user = "+userId + " AND issue_dt > '"+fromDate+"'AND issue_dt < '"+toDate+"'"
                            + orderName + orderNum;
            ResultSet rs = dbInst.executeQuery(query);
            tableModel.setDataSource(rs, colTitles);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SearchEventHandler(IDatabaseController dbController, String dbTable, JTable guiTable, JPanel form) {
        this.dbInst   = dbController;
        this.dbTable  = dbTable;
        this.guiTable = guiTable;
        this.form     = form;
    }
}
