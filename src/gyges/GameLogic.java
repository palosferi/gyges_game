package gyges;

import gyges.enums.PlayerType;

public class GameLogic {
    private final Board board;
    private Player currentPlayer;

    public GameLogic() {
        board = new Board(6); // 6x6 board
        currentPlayer = PlayerType.PLAYER_ONE; // Enum for players
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == PlayerType.PLAYER_ONE) ? PlayerType.PLAYER_TWO : PlayerType.PLAYER_ONE;
    }

    public boolean isValidMove(Piece piece, Position newPosition) {
        int moveDistance = piece.getMoveDistance();
        // Check bounds, distance, and if the path is clear
        return board.isWithinBounds(newPosition) && calculateDistance(piece.getPosition(), newPosition) == moveDistance;
    }

    public boolean isGameOver() {
        // Check if a player has won
        return false; // Placeholder logic
    }

    private int calculateDistance(Position start, Position end) {
        return Math.abs(start.getX() - end.getX()) + Math.abs(start.getY() - end.getY());
    }
}
