package gyges;

import javax.swing.table.DefaultTableModel;
import java.util.LinkedList;
import java.util.List;

public class Board extends DefaultTableModel {
    private final Piece[][] board;
    private final int rows;
    private final int cols;

    //public static final Piece NULL_PIECE = new Piece(0);

    public Board() {
        this.rows = 6;
        this.cols = 6;
        this.board = new Piece[cols][rows];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                board[x][y] = new Piece();
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

    public boolean tryToMovePiece(Position from, Position to) {
        if (isPositionJumpable(to)) {
            setValueAt(board[from.x()][from.y()], to.y(), to.x());
            setValueAt(new Piece(), from.y(), from.x());
            //board[to.x()][to.y()] = board[from.x()][from.y()];
            //board[from.x()][from.y()] = new Piece();
            return true;
        }
        return false;
    }

    public boolean isPositionJumpable(Position position) {
        return board[position.x()][position.y()].getState() == CellState.SELECTED;
    }

    public Piece getPieceAt(Position position) {
        return board[position.x()][position.y()];
    }
    public Piece getPieceAt(int x, int y) {
        return board[x][y];
    }
    public int getActiveRow(boolean player) {
        if (player) {
            for(int y = getRowCount()-1; y > 0; y--) {
                for(int x = 0; x < getColumnCount(); x++) {
                    if(getPieceAt(x, y).getState() != CellState.EMPTY) {
                        return y;
                    }
                }
            }
        } else {
            for(int y = 0; y < getRowCount(); y++) {
                for(int x = 0; x < getColumnCount(); x++) {
                    if(getPieceAt(x, y).getState() != CellState.EMPTY) {
                        return y;
                    }
                }
            }
        }
        return -1;
    }

//
//    @Override
//    public Object getValueAt(int row, int column) {
//        return getPieceAt(new Position(column, row));
//    }

//    @Override
//    @SuppressWarnings("unchecked")
//    public Vector<Vector> getDataVector() {
//        return super.getDataVector();
//    }


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

    public void exploreMoves(Position pos) {
        //board[pos.x()][pos.y()].setSelected(false); // A kiválasztott cellát unselectiddé teszünk
        CellState cell = board[pos.x()][pos.y()].getState();
        //  cell != CellState.EMPTY && cell != CellState.SELECTED
        if (cell.getHeight() != 0) {
            // Minden elérhető szabályos moveot selectiddé teszünk
            findPositions(pos, cell.getHeight(), new LinkedList<>());
        }

    }
    private void findPositions(Position pos, int depth, List<Position> visited) {
        visited.add(pos);
        if (depth == 0) {
            board[pos.x()][pos.y()].setSelected(true);
        } else {
            // Balra
            if (pos.x() > 0 && !visited.contains(pos.left()) && ( depth == 1 || isPositionJumpable(pos.left()))) {
                findPositions(pos.left(), depth - 1, visited);
            }
            // Jobbra
            if (pos.x() < cols - 1 && !visited.contains(pos.right()) && ( depth == 1 || isPositionJumpable(pos.right()))) {
                findPositions(pos.right(), depth - 1, visited);
            }
            // Fel
            if (pos.y() > 0 && !visited.contains(pos.up()) && ( depth == 1 || isPositionJumpable(pos.up()))) {
                findPositions(pos.up(), depth - 1, visited);
            }
            // Le
            if (pos.y() < rows - 1 && !visited.contains(pos.down()) && ( depth == 1 || isPositionJumpable(pos.down()))) {
                findPositions(pos.down(), depth - 1, visited);
            }
        }
    }

    public void setStartPosition(Position selectedClick) {
        board[selectedClick.x()][selectedClick.y()].setStart(true);
    }
}