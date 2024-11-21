package gyges.gui;

import gyges.Game;
import gyges.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class BoardPanel extends JPanel {
    private Game game;
    private List<JButton> boardButtons;

    public BoardPanel(Game game) {
        this.game = game;
        setLayout(new GridLayout(6, 6));
        boardButtons = new ArrayList<>();

        for (int i = 0; i < 36; i++) {
            JButton button = new JButton();
            button.addActionListener(new BoardButtonListener(i / 6, i % 6));
            boardButtons.add(button);
            add(button);
        }
        updateBoardDisplay();
    }

    public void updateBoardDisplay() {
        // Existing logic for updating the board display
    }

    // Private inner class
    private class BoardButtonListener implements ActionListener {
        private int row;
        private int col;
        private Position selectedPosition = null;

        public BoardButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Position clickedPosition = new Position(row, col);
            if (selectedPosition == null) {
                // Piece selection logic
                if (game.getBoard().getPieceAt(clickedPosition) != null) {
                    selectedPosition = clickedPosition;
                    JButton button = boardButtons.get(row * 6 + col);
                    button.setBackground(Color.YELLOW);
                } else {
                    JOptionPane.showMessageDialog(null, "No piece selected!");
                }
            } else {
                // Move logic
                if (game.takeTurn(selectedPosition, clickedPosition)) {
                    selectedPosition = null;
                    updateBoardDisplay();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid move!");
                }
            }
        }
    }
}
