package view.handlers;

import java.awt.event.ActionEvent;
import javax.swing.JTable;

/**
 * Implementation of IActionHandler, which handle clicks on arrows btns
 *
 * @param guiTable - GUI table in frame
 */
public class ArrowsEventHandler implements IActionHandler {
    private JTable guiTable = null;

    @Override
    public void handleEvent(ActionEvent event) {
        int curRow = (guiTable.getSelectedRow() == -1) ? 0 : guiTable.getSelectedRow();
        int curCol = (guiTable.getSelectedColumn() == -1) ? 0 : guiTable.getSelectedColumn();
        int cntRow = guiTable.getRowCount();
        int cntCol = guiTable.getColumnCount();
        if (event.getActionCommand() == "up" && curRow > 0)
            guiTable.changeSelection(curRow-1, curCol, false, false);
        if (event.getActionCommand() == "down" && curRow < cntRow-1)
            guiTable.changeSelection(curRow+1, curCol, false, false);
        if (event.getActionCommand() == "left" && curCol > 0)
            guiTable.changeSelection(curRow, curCol-1, false, false);
        if (event.getActionCommand() == "right" && curCol < cntCol-1)
            guiTable.changeSelection(curRow, curCol+1, false, false);

    }

    public ArrowsEventHandler(JTable guiTable) {
        this.guiTable = guiTable;
    }
}
