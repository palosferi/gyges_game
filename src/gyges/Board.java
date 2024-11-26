package gyges;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class Board extends DefaultTableModel {
    private final Piece[][] board;
    private final int rows;
    private final int cols;

    public Board() {
        this.rows = 6;
        this.cols = 6;
        this.board = new Piece[cols][rows];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                board[x][y] = new Piece(0);
            }
        }
    }

    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public int getColumnCount() {
        return cols;
    }

    @Override
    public Object getValueAt(int row, int column) {
        return board[column][row].toImage();
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (row >= 0 && row < rows && column >= 0 && column < cols) {
            board[column][row] = (Piece) value;
            fireTableCellUpdated(row, column); // Notify JTable of data change
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // Customize if certain cells should not be editable
        return false;
    }

    public void init() {
        for (int i = 0; i < cols; i++) {
            board[i][0] = new Piece(1 + i % 3); // Player 1 pieces at the top
            board[i][5] = new Piece(1 + i % 3); // Player 2 pieces at the top
        }
    }

    public boolean move(Position from, Position to) {
        if (isPositionEmpty(to)) {
            board[to.x()][to.y()] = board[from.x()][from.y()];
            board[from.x()][from.y()] = null;
            return true;
        }
        return false;
    }

    public boolean isPositionEmpty(Position position) {
        return board[position.x()][position.y()] == null;
    }

    public Piece getPieceAt(Position position) {
        return board[position.x()][position.y()];
    }
//
//    @Override
//    public Object getValueAt(int row, int column) {
//        return getPieceAt(new Position(column, row));
//    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<Vector> getDataVector() {
        return super.getDataVector();
    }


    public void exploreSwappables(Position pos) {
        if (pos.y() == 0 || pos.y() == 5) {
            for (int x = 0; x < cols; x++) {
                if (x != pos.x()) {
                    board[x][pos.y()].setSelected(true);
                }
            }
        }
    }
    public void setAllCellsUnselected(){
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                board[x][y].setSelected(false);
            }
        }
    }

    public void swapPieces(Position selectedClick, Position nextClick) {
        Piece temp = board[selectedClick.x()][selectedClick.y()];
        board[selectedClick.x()][selectedClick.y()] = board[nextClick.x()][nextClick.y()];
        board[nextClick.x()][nextClick.y()] = temp;
    }
}