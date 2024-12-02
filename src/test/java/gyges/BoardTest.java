package gyges;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(false); // Initialize with light mode
        board.init(); // Initialize the board pieces
    }

    @Test
    void testInitialization() {
        assertEquals(6, board.getRowCount(), "Board should have 6 rows.");
        assertEquals(6, board.getColumnCount(), "Board should have 6 columns.");
        assertNotNull(board.getPieceAt(0, 0), "Each cell should contain a piece.");
    }

    @Test
    void testGetValueAt() {
        Object value = board.getValueAt(0, 0);
        assertNotNull(value, "Value at (0,0) should not be null.");
        assertTrue(value instanceof javax.swing.ImageIcon, "Value at (0,0) should be an ImageIcon.");
    }

    @Test
    void testSetValueAt() {
        Piece newPiece = new Piece(3);
        board.setValueAt(newPiece, 0, 0);
        assertEquals(newPiece, board.getPieceAt(0, 0), "Piece at (0,0) should be updated to the new piece.");
        board.setValueAt(newPiece, -1, -1);
        assertEquals(newPiece, board.getPieceAt(0, 0), "Piece at (-1,-1) should not be updated to the new piece.");
    }

    @Test
    void testIsCellEditable() {
        assertFalse(board.isCellEditable(0, 0), "Cells should not be editable.");
    }

    @Test
    void testInitBoardState() {
        assertEquals(1, board.getPieceAt(0, 0).getValue(), "Top row should initialize with Player 1 pieces.");
        assertEquals(3, board.getPieceAt(2, 0).getValue(), "Pieces should follow the pattern 1, 2, 3.");
        assertEquals(1, board.getPieceAt(0, 5).getValue(), "Bottom row should initialize with Player 2 pieces.");
        assertEquals(0, board.getPieceAt(0, 1).getValue(), "Middle rows should initialize as empty.");
    }

    @Test
    void testTryToMovePiece() {
        Position from = new Position(0, 0);
        Position to = new Position(0, 1);
        Piece p1 = new Piece(1);
        board.setValueAt(p1, from.x(), from.y());
        board.setValueAt(new Piece(), to.x(), to.y());

        board.getPieceAt(to).setSelected(true); // Simulate a selected piece

        assertTrue(board.tryToMovePiece(from, to), "Piece should move successfully.");
        assertEquals(0, board.getPieceAt(from).getValue(), "Original cell should be empty after moving.");
        assertEquals(1, board.getPieceAt(to).getValue(), "Target cell should contain the moved piece.");

        Piece p2 = new Piece(2);
        board.setValueAt(p2, to.x(), to.y());

        assertFalse(board.tryToMovePiece(from, to), "Piece should move successfully.");
        assertEquals(0, board.getPieceAt(from).getValue(), "Original cell should be empty after moving.");
        assertEquals(1, board.getPieceAt(to).getValue(), "Target cell should contain the moved piece.");
    }

    @Test
    void testIsPositionFinalJump() {
        Position pos = new Position(0, 0);
        Piece p2 = new Piece();
        board.setValueAt(p2, pos.x(), pos.y());
        board.getPieceAt(pos).setSelected(true);
        assertTrue(board.isPositionFinalJump(pos), "Position with SELECTED state should return true.");
    }

    @Test
    void testIsPositionJumpamble() {
        Position pos = new Position(0, 0);
        board.getPieceAt(pos).setSelected(true);
        assertTrue(board.isPositionJumpable(pos), "Position with SELECTED state should return true.");
        board.getPieceAt(pos).setSelected(false);
        assertFalse(board.isPositionJumpable(pos), "Position with no SELECTED state should return false.");
    }

    @Test
    void testIsCellEmpty() {
        Position pos = new Position(0, 1);
        assertTrue(board.isCellEmpty(pos), "Empty cell should return true.");
    }

    @Test
    void testSwapPieces() {
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(0, 5);

        Piece piece1 = board.getPieceAt(pos1);
        Piece piece2 = board.getPieceAt(pos2);

        board.swapPieces(pos1, pos2);

        assertEquals(piece2, board.getPieceAt(pos1), "Pieces should be swapped correctly.");
        assertEquals(piece1, board.getPieceAt(pos2), "Pieces should be swapped correctly.");
    }

    @Test
    void testExploreMoves() {
        Position pos = new Position(0, 0);
        board.getPieceAt(pos).setSelected(true);

        boolean hasMoves = board.exploreMoves(pos, true);
        assertTrue(hasMoves, "Piece with a valid move should return true.");
    }

    @Test
    void testGetBoardState() {
        List<List<Integer>> boardState = board.getBoardState();
        assertEquals(6, boardState.size(), "Board state should have 6 rows.");
        assertEquals(6, boardState.get(0).size(), "Each row in board state should have 6 columns.");
    }

    @Test
    void testSetBoardState() {
        List<List<Integer>> newState = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                row.add((i + j) % 4); // Sample pattern
            }
            newState.add(row);
        }
        board.setBoardState(newState);

        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                assertEquals(newState.get(y).get(x), board.getPieceAt(x, y).getValue(), "Board state should match the new state.");
            }
        }
    }

    @Test
    void testKnockOutPiece() {
        Position selected = new Position(0, 5);
        Position next = new Position(1, 5);

        board.knockOutPiece(selected, next, true);
        board.setAllCellsUnselectedAndNonstart();
        assertEquals(2, board.getPieceAt(selected).getValue(), "Original position should be empty.");
        assertEquals(1, board.getPieceAt(next).getValue(), "Target position should contain the piece.");

        board.knockOutPiece(selected, next, false);
        board.setAllCellsUnselectedAndNonstart();
        assertEquals(1, board.getPieceAt(selected).getValue(), "Original position should be empty.");
        assertEquals(2, board.getPieceAt(next).getValue(), "Target position should contain the piece.");
    }
}
