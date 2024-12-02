package gyges;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testBoardInitialization() {
        // Test for dark mode board initialization
        Board darkBoard = new Board(true);
        assertTrue(darkBoard.isDarkMode, "Dark mode should be enabled.");

        // Test for light mode board initialization
        Board lightBoard = new Board(false);
        assertFalse(lightBoard.isDarkMode, "Light mode should be enabled.");
    }

    @Test
    void testSetAndGetPieceAtPosition() {
        Board board = new Board(false);
        Position position = new Position(2, 3);
        Piece piece = new Piece(5);

        // Place a piece on the board
        board.setValueAt(piece, position.y(), position.x());
        Piece retrievedPiece = board.getPieceAt(position);

        assertNotNull(retrievedPiece, "Piece at position should not be null.");
        assertEquals(5, retrievedPiece.getValue(), "Piece value should match the set value.");
    }

    @Test
    void testPieceMovement() {
        Board board = new Board(false);
        Position from = new Position(2, 2);
        Position to = new Position(2, 3);
        Piece piece = new Piece(3);

        // Set a piece at the starting position
        board.setValueAt(piece, from.y(), from.x());
        board.getPieceAt(to).setSelected(true);
        // Move the piece
        boolean moved = board.tryToMovePiece(from, to);

        assertTrue(moved, "Piece should be moved successfully.");
        assertEquals(piece, board.getPieceAt(to), "Piece at destination should match the moved piece.");
        assertTrue(board.isCellEmpty(from), "Starting cell should be empty after the move.");
    }

    @Test
    void testBoardStateSerialization() {
        Board board = new Board(false);
        board.init();

        // Get the current board state
        var state = board.getBoardState();

        // Ensure state matches expected size
        assertEquals(6, state.size(), "Board should have 6 rows.");
        for (List<Integer> row : state) {
            assertEquals(6, row.size(), "Each row should have 6 columns.");
        }

        // Modify the board and test deserialization
        state.get(0).set(0, 5); // Change a cell value
        board.setBoardState(state);
        assertEquals(5, board.getPieceId(0, 0), "Board state should reflect the updated value.");
    }

    @Test
    void testExploreMoves() {
        Board board = new Board(false);
        board.init();
        Position position = new Position(0, 5);

        boolean movesFound = board.exploreMoves(position, false);

        assertTrue(movesFound, "Moves should be found for the piece at the starting position.");
    }
}
