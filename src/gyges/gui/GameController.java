package gyges.gui;

import gyges.*;

public class GameController {
    private Game game;
    private MainFrame mainFrame;

    public GameController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.game = new Game();
    }

    public void startSetupPhase() {
        showMessage("Setup phase started. Place your pieces.");
    }

    public boolean placePiece(Position position, int pieceType) {
        if (GameLogic.isInitialPiecePosition(position.y(), game.getCurrentPlayer()) && game.placePiece(position, pieceType)) {
            updateUI();
            return true;
        }
        return false;
    }

    public boolean movePiece(Position from, Position to) {
        if (game.movePiece(from, to)) {
            updateUI();
            return true;
        }
        return false;
    }

    private void updateUI() {
        if (mainFrame.getBoardPanel() != null) {
            mainFrame.getBoardPanel().updateBoardDisplay();
        }
        if (mainFrame.getControlPanel() != null) {
            mainFrame.getControlPanel().updateDisplay();
        }
    }

    public void showMessage(String message) {
        if (mainFrame.getControlPanel() != null) {
            mainFrame.getControlPanel().showMessage(message);
        }
    }

    public Game getGame() {
        return game;
    }
}