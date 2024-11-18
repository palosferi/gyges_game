package gyges;

public class GameLogic {
    private Board board;

    public GameLogic(Board board) { this.board = board; }

    public boolean validateMove(Piece piece, Position newPosition) { /* rules */ return false;}
    public boolean checkWinCondition(Player player) { /* win check logic */ return false;}
}