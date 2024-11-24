package gyges.gui;

import gyges.*;

public class GameController {
    private Game game;
    private BoardPanel boardPanel;
    private ControlPanel controlPanel;
    private SetupPhase setupPhase;

    public GameController() {
        this.game = new Game();
        this.setupPhase = new SetupPhase(game.getBoard(), game.getPlayerOne(), game.getPlayerTwo());
    }

    public void setBoardPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }

    public void setControlPanel(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }

    public Game getGame() {
        return game;
    }

    public void startSetupPhase() {
        setupPhase.start(this);
    }

    public boolean placePiece(Position position, int pieceType) {
        if (game.placePiece(position, pieceType)) {
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
        if (boardPanel != null) {
            boardPanel.updateBoardDisplay();
        }
        if (controlPanel != null) {
            controlPanel.updateDisplay();
        }
    }

    public void showMessage(String message) {
        if (controlPanel != null) {
            controlPanel.showMessage(message);
        }
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }
}