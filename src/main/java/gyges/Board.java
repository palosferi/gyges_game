package gyges;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Board extends DefaultTableModel {
    private final Piece[][] board;
    private final int rows;
    private final int cols;
    boolean isDarkMode;

    public Board(boolean isDMode) {
        this.rows = 6;
        this.cols = 6;
        this.isDarkMode = isDMode; // Enable dark mode if true, otherwise light mode
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
        return board[column][row].toImage(isDarkMode);
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
            // Set Player 1 pieces at the top (row 0)
            board[i][0] = new Piece(getPieceValue(i));

            // Set Player 2 pieces at the bottom (row 5)
            board[i][5] = new Piece(getPieceValue(i));

            for (int j = 1; j < 5; j++) {
                // Set empty pieces in between Player 1 and Player 2
                board[i][j] = new Piece();
            }
        }
    }

    // Helper method to get the piece value in the desired pattern: 1, 2, 3, 3, 2, 1
    private int getPieceValue(int index) {
        int[] pattern = {1, 2, 3, 3, 2, 1}; // Pattern for piece values
        return pattern[index % pattern.length]; // Use modulus to cycle through the pattern
    }

    public boolean tryToMovePiece(Position from, Position to) {
        if (isPositionFinalJump(to)) {
            setValueAt(board[from.x()][from.y()], to.y(), to.x());
            setValueAt(new Piece(), from.y(), from.x());
            return true;
        }
        return false;
    }

    public boolean isPositionFinalJump(Position position) {
        return getPieceAt(position).getState() == CellState.SELECTED;
    }

    public boolean isCellEmpty(Position position) {
        return (getPieceAt(position).getState().getHeight() == 0);
    }

    public boolean isPositionJumpable(Position position) {
        return getPieceAt(position).getValue() / CellState.COUNT == 1;
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

    public void exploreSwappables(Position pos) {
        if (pos.y() == 0 || pos.y() == 5) {
            for (int x = 0; x < cols; x++) {
                if (x != pos.x()) {
                    board[x][pos.y()].setSelected(true);
                } else {
                    board[x][pos.y()].setStart(true);
                }
            }
        }
    }

    public void setAllCellsUnselectedAndNonstart(){
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                board[x][y].setSelected(false);
                board[x][y].setStart(false);
            }
        }
    }

    public void swapPieces(Position selectedClick, Position nextClick) {
        Piece temp = board[selectedClick.x()][selectedClick.y()];
        board[selectedClick.x()][selectedClick.y()] = board[nextClick.x()][nextClick.y()];
        board[nextClick.x()][nextClick.y()] = temp;
    }

    public void exploreMoves(Position pos) {
        CellState cell = board[pos.x()][pos.y()].getState();
        if (cell.getHeight() != 0) {
            // Minden elérhető szabályos moveot selectiddé teszünk
            findPositions(pos, cell.getHeight(), new LinkedList<>(), new Position(0, 0));
        }

    }

    public boolean findPositions(Position pos, int depth, List<Position> visited, Position lastMove) {
        visited.add(pos);
        if (depth == 0) {
            board[pos.x()][pos.y()].setSelected(true);
            return true;
        }
        // Balra
        if (pos.x() > 0 && lastMove.x()!=1 && ((depth == 1 && !visited.contains(pos.left())) || isCellEmpty(pos.left()))) {
            findPositions(pos.left(), depth - 1, visited, new Position(-1, 0));
        }
        // Jobbra
        if (pos.x() < cols - 1 && lastMove.x()!=-1 && ((depth == 1 && !visited.contains(pos.right())) || isCellEmpty(pos.right()))) {
            findPositions(pos.right(), depth - 1, visited, new Position(1, 0));
        }
        // Fel
        if (pos.y() > 0 && lastMove.y()!=-1 && ((depth == 1 && !visited.contains(pos.up())) || isCellEmpty(pos.up()))) {
            findPositions(pos.up(), depth - 1, visited, new Position(0, 1));
        }
        // Le
        if (pos.y() < rows - 1 && lastMove.y()!=1 && ((depth == 1  && !visited.contains(pos.down())) || isCellEmpty(pos.down()))) {
            findPositions(pos.down(), depth - 1, visited, new Position(0, -1));
        }
        return false;
    }

    public void setStartPosition(Position selectedClick) {
        board[selectedClick.x()][selectedClick.y()].setStart(true);
    }

    public List<List<Integer>> getBoardState() {
        // Convert board's cells into a list of lists (for Gson serialization)
        List<List<Integer>> state = new ArrayList<>();
        for (int y = 0; y < rows; y++) {
            List<Integer> row = new ArrayList<>();
            for (int x = 0; x < cols; x++) {
                row.add(getPieceId(x, y)); // Example: assuming each cell has a unique piece ID or 0 for empty
            }
            state.add(row);
        }
        return state;
    }

    public void setBoardState(List<List<Integer>> state) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                setPieceId(x, y, state.get(y).get(x));  // Use setPieceId to set the piece ID for each cell
            }
        }
    }

    public int getPieceId(int x, int y) {
        Piece piece = board[x][y];
        return piece.getValue();  // Return the integer value of the piece's state
    }

    public void setPieceId(int x, int y, int pieceId) {
        Piece piece = new Piece(pieceId);  // Create a new Piece based on the given ID
        board[x][y] = piece;  // Set the piece at the given position
    }

    public boolean findIfWins(Position pos, int depth, boolean player, List<Position> visited, Position lastMove) {
        visited.add(pos);
        if (depth == 0 && pos.y() == (player? 0 : 5)) {
            return true;
        } else {
            // Balra
            if (pos.x() > 0 && !lastMove.equals(new Position(1, 0)) && isCellEmpty(pos.left()) && findIfWins(pos.left(), depth - 1, player, visited, new Position(-1, 0))) return true;

            // Jobbra
            if (pos.x() < cols - 1 && !lastMove.equals(new Position(-1, 0)) && isCellEmpty(pos.right()) && findIfWins(pos.right(), depth - 1, player, visited, new Position(1, 0))) return true;

            // Fel
            if (pos.y() > 0 && !lastMove.equals(new Position(0, -1)) && isCellEmpty(pos.up()) && findIfWins(pos.up(), depth - 1, player, visited, new Position(0, 1))) return true;

            // Le
            if (pos.y() < rows - 1 && !lastMove.equals(new Position(0, 1)) && isCellEmpty(pos.down()) && findIfWins(pos.down(), depth - 1, player, visited, new Position(0, -1))) return true;

        }
        return false;
    } //TODO: wins
}