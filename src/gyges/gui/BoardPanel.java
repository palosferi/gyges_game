package gyges.gui;

import gyges.*;
import gyges.enums.GamePhase;
import gyges.enums.PlayerType;
import gyges.piece.Piece;
import gyges.piece.Piece1;
import gyges.piece.Piece2;
import gyges.piece.Piece3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class BoardPanel extends JPanel {
    private MainFrame mainFrame;
    private final int cellSize = 60;
    private Position selectedPosition = null;
    private Map<Class<? extends Piece>, Color> pieceColors;

    public BoardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
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
                Piece piece = mainFrame.getController().getGame().getBoard().getPieceAt(pos);
                if (piece != null) {
                    drawPiece(g, row, col, piece);
                } else if (GameLogic.isInitialPiecePosition(row, mainFrame.getController().getGame().getCurrentPlayer())) {
                    drawInitialPiece(g, row, col);
                }
            }
        }
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

            if (mainFrame.getController().getGame().getCurrentPhase() == GamePhase.SETUP) {
                handleSetupPhase(clickedPosition);
            } else {
                handlePlayPhase(clickedPosition);
            }
        }

        private void handleSetupPhase(Position clickedPosition) {
            int pieceType = mainFrame.getControlPanel().getSelectedPieceType();
            if (mainFrame.getController().placePiece(clickedPosition, pieceType)) {
                updateBoardDisplay();
            } else {
                mainFrame.getController().showMessage("Invalid placement!");
            }
        }

        private void handlePlayPhase(Position clickedPosition) {
            if (selectedPosition == null) {
                Piece piece = mainFrame.getController().getGame().getBoard().getPieceAt(clickedPosition);
                if (piece != null) {
                    selectedPosition = clickedPosition;
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(BoardPanel.this, "No piece selected!");
                }
            } else {
                if (mainFrame.getController().getGame().movePiece(selectedPosition, clickedPosition)) {
                    selectedPosition = null;
                    updateBoardDisplay();
                    mainFrame.getControlPanel().updateDisplay();
                    checkGameOver();
                } else {
                    JOptionPane.showMessageDialog(BoardPanel.this, "Invalid move!");
                }
            }
        }
    }

    private void checkGamePhaseChange() {
        if (mainFrame.getController().getGame().getCurrentPhase() == GamePhase.PLAY) {
            JOptionPane.showMessageDialog(this, "Setup phase complete. Game phase begins!");
        }
    }

    private void checkGameOver() {
        if (mainFrame.getController().getGame().isGameOver()) {
            JOptionPane.showMessageDialog(this, "Game Over! Player " + mainFrame.getController().getGame().getWinner() + " wins!");
        }
    }
}