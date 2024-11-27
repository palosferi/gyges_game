package gyges;

import java.util.List;

public class GameStateData {
    private List<List<Integer>> boardState; // Encodes board cells and pieces
    private boolean currentPlayer; // True for "bottom" player, false for "top"

    public GameStateData(List<List<Integer>> boardState, boolean currentPlayer) {
        this.boardState = boardState;
        this.currentPlayer = currentPlayer;
    }

    public List<List<Integer>> getBoardState() {
        return boardState;
    }

    public void setBoardState(List<List<Integer>> boardState) {
        this.boardState = boardState;
    }

    public boolean isCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(boolean currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
