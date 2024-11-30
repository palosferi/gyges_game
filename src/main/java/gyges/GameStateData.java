package gyges;

import java.util.List;
import java.util.Stack;

public class GameStateData {
    private List<List<Integer>> boardState;
    private boolean currentPlayer;
    private Stack<Pair<Position, Position>> moveHistory;
    private boolean isDarkMode;

    public GameStateData(
            List<List<Integer>> boardState,
            boolean currentPlayer,
            Stack<Pair<Position, Position>> moveHistory,
            boolean isDarkMode
    ) {
        this.boardState = boardState;
        this.currentPlayer = currentPlayer;
        this.moveHistory = moveHistory;
        this.isDarkMode = isDarkMode;
    }

    public List<List<Integer>> getBoardState() {
        return boardState;
    }

    public boolean isCurrentPlayer() {
        return currentPlayer;
    }

    public Stack<Pair<Position, Position>> getMoveHistory() {
        return moveHistory;
    }

    public boolean isDarkMode() {
        return isDarkMode;
    }
}