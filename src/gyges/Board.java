package gyges;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class Board extends DefaultTableModel {
    private final Piece[][] grid = new Piece[6][6];

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public boolean move(Position from, Position to) {
        if (isPositionEmpty(to)) {
            grid[to.x()][to.y()] = grid[from.x()][from.y()];
            grid[from.x()][from.y()] = null;
            return true;
        }
        return false;
    }

    public boolean isPositionEmpty(Position position) {
        return grid[position.x()][position.y()] == null;
    }

    public Piece getPieceAt(Position position) {
        return grid[position.x()][position.y()];
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<Vector> getDataVector() {
        return super.getDataVector();
    }
}