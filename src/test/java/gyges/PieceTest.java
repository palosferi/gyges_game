package gyges;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {

    @Test
    void testPieceDefaultConstructor() {
        Piece piece = new Piece();
        assertEquals(CellState.EMPTY, piece.getState(), "Default constructor should initialize piece with EMPTY state.");
    }

    @Test
    void testPieceParameterizedConstructor() {
        Piece piece = new Piece(1);
        assertEquals(CellState.ONE, piece.getState(), "Piece should initialize with the correct state based on the value.");

        piece = new Piece(3);
        assertEquals(CellState.THREE, piece.getState(), "Piece should initialize with the correct state based on the value.");
    }

    @Test
    void testGetValue() {
        Piece piece = new Piece(2);
        assertEquals(2, piece.getValue(), "getValue() should return the correct integer representation of the state.");
    }

    @Test
    void testToImage() {
        Piece piece = new Piece(1);
        ImageIcon darkIcon = piece.toImage(true);
        ImageIcon lightIcon = piece.toImage(false);

        assertNotNull(darkIcon, "Dark mode image icon should not be null.");
        assertNotNull(lightIcon, "Light mode image icon should not be null.");
    }

    @Test
    void testToString() {
        Piece piece = new Piece(2);
        assertEquals("TWO", piece.toString(), "toString() should return the name of the state.");
    }

    @Test
    void testSetSelected() {
        Piece piece0 = new Piece();
        Piece piece1 = new Piece(1);
        Piece piece2 = new Piece(2);
        Piece piece3 = new Piece(3);

        piece0.setSelected(true);
        assertEquals(CellState.SELECTED, piece0.getState(), "setSelected(true) should change the state to SELECTED_TWO.");

        piece0.setSelected(false);
        assertEquals(CellState.EMPTY, piece0.getState(), "setSelected(false) should revert the state to TWO.");

        piece1.setSelected(true);
        assertEquals(CellState.SELECTED_ONE, piece1.getState(), "setSelected(true) should change the state to SELECTED_TWO.");

        piece1.setSelected(false);
        assertEquals(CellState.ONE, piece1.getState(), "setSelected(false) should revert the state to TWO.");

        piece2.setSelected(true);
        assertEquals(CellState.SELECTED_TWO, piece2.getState(), "setSelected(true) should change the state to SELECTED_TWO.");

        piece2.setSelected(false);
        assertEquals(CellState.TWO, piece2.getState(), "setSelected(false) should revert the state to TWO.");

        piece3.setSelected(true);
        assertEquals(CellState.SELECTED_THREE, piece3.getState(), "setSelected(true) should change the state to SELECTED_TWO.");

        piece3.setSelected(false);
        assertEquals(CellState.THREE, piece3.getState(), "setSelected(false) should revert the state to TWO.");
    }

    @Test
    void testSetStart() {
        Piece piece0 = new Piece(0);
        Piece piece1 = new Piece(1);
        Piece piece2 = new Piece(2);
        Piece piece3 = new Piece(3);

        piece0.setStart(true);
        assertEquals(CellState.START_EMPTY, piece0.getState(), "setStart(true) should change the state to START_THREE.");

        piece0.setStart(false);
        assertEquals(CellState.EMPTY, piece0.getState(), "setStart(false) should revert the state to THREE.");

        piece1.setStart(true);
        assertEquals(CellState.START_ONE, piece1.getState(), "setStart(true) should change the state to START_THREE.");

        piece1.setStart(false);
        assertEquals(CellState.ONE, piece1.getState(), "setStart(false) should revert the state to THREE.");

        piece2.setStart(true);
        assertEquals(CellState.START_TWO, piece2.getState(), "setStart(true) should change the state to START_THREE.");

        piece2.setStart(false);
        assertEquals(CellState.TWO, piece2.getState(), "setStart(false) should revert the state to THREE.");

        piece3.setStart(true);
        assertEquals(CellState.START_THREE, piece3.getState(), "setStart(true) should change the state to START_THREE.");

        piece3.setStart(false);
        assertEquals(CellState.THREE, piece3.getState(), "setStart(false) should revert the state to THREE.");
    }

    @Test
    void testIsStart() {
        Piece piece = new Piece(2);

        piece.setStart(false);
        assertFalse(piece.isStart(), "isStart() should return false for non-start states.");

        piece.setStart(true);
        assertTrue(piece.isStart(), "isStart() should return true for start states.");
    }
}
