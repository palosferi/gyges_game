package gyges.gui;

import gyges.*;
import gyges.enums.GamePhase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class BoardPanel extends JPanel {
    private final GameController controller;
    private final int cellSize = 60;
    private Position selectedPosition = null;
    private Map<Class<? extends Piece>, Color> pieceColors;

    public BoardPanel(GameController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(cellSize * 6, cellSize * 6));
        initializePieceColors();
        addMouseListener(new BoardMouseListener());
    }

    private void initializePieceColors() {
        pieceColors = new HashMap<>();
        pieceColors.put(Piece1.class, Color.YELLOW);
        pieceColors.put(Piece2.class, Color.ORANGE);
        pieceColors.put(Piece3.class, Color.RED);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawPieces(g);
    }

    private void drawBoard(Graphics g) {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                int x = col * cellSize;
                int y = row * cellSize;
                g.setColor((row + col) % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
                g.fillRect(x, y, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellSize, cellSize);
            }
        }
    }

    private void drawPieces(Graphics g) {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                Position pos = new Position(row, col);
                Piece piece = controller.getGame().getBoard().getPieceAt(pos);
                if (piece != null) {
                    drawPiece(g, row, col, piece);
                } else if (isInitialPiecePosition(row, col)) {
                    drawInitialPiece(g, row, col);
                }
            }
        }
    }

    private boolean isInitialPiecePosition(int row, int col) {
        return (row == 0 || row == 5);
    }

    private void drawInitialPiece(Graphics g, int row, int col) {
        int x = col * cellSize + cellSize / 10;
        int y = row * cellSize + cellSize / 10;
        int size = cellSize * 8 / 10;

        g.setColor(Color.GRAY);
        g.fillOval(x, y, size, size);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, size, size);
    }

    private void drawPiece(Graphics g, int row, int col, Piece piece) {
        int x = col * cellSize + cellSize / 10;
        int y = row * cellSize + cellSize / 10;
        int size = cellSize * 8 / 10;

        g.setColor(pieceColors.get(piece));
        g.fillOval(x, y, size, size);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, size, size);

        if (new Position(row, col).equals(selectedPosition)) {
            g.setColor(Color.BLUE);
            g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize);
        }
    }

    public void updateBoardDisplay() {
        repaint();
    }

    private class BoardMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int col = e.getX() / cellSize;
            int row = e.getY() / cellSize;
            Position clickedPosition = new Position(row, col);

            if (controller.getGame().getCurrentPhase() == GamePhase.SETUP) {
                handleSetupPhase(clickedPosition);
            } else {
                handlePlayPhase(clickedPosition);
            }
        }

        private void handleSetupPhase(Position clickedPosition) {
            int pieceType = controller.getControlPanel().getSelectedPieceType();
            if (controller.placePiece(clickedPosition, pieceType)) {
                updateBoardDisplay();
            } else {
                controller.showMessage("Invalid placement!");
            }
        }

        private void handlePlayPhase(Position clickedPosition) {
            if (selectedPosition == null) {
                Piece piece = controller.getGame().getBoard().getPieceAt(clickedPosition);
                if (piece != null) {
                    selectedPosition = clickedPosition;
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(BoardPanel.this, "No piece selected!");
                }
            } else {
                if (controller.getGame().movePiece(selectedPosition, clickedPosition)) {
                    selectedPosition = null;
                    updateBoardDisplay();
                    controller.getControlPanel().updateDisplay();
                    checkGameOver();
                } else {
                    JOptionPane.showMessageDialog(BoardPanel.this, "Invalid move!");
                }
            }
        }
    }

    private void checkGamePhaseChange() {
        if (controller.getGame().getCurrentPhase() == GamePhase.PLAY) {
            JOptionPane.showMessageDialog(this, "Setup phase complete. Game phase begins!");
        }
    }

    private void checkGameOver() {
        if (controller.getGame().isGameOver()) {
            JOptionPane.showMessageDialog(this, "Game Over! Player " + controller.getGame().getWinner() + " wins!");
        }
    }
}