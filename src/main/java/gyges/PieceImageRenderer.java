package gyges;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class PieceImageRenderer extends DefaultTableCellRenderer {

    @Override
    public void setValue(Object value) {
        if (value instanceof Icon icon) {
            setIcon(icon);
            setText(null);
        } else {
            setIcon(null);
            super.setValue(value);
        }
    }
}