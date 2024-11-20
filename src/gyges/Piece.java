package gyges;

public abstract class Piece {
    private Position position;
    private Player player;

    public Piece(Player player) {
        this.player = player;
    }

    public abstract int getValue();

    public abstract Piece copy();

    public void move(Position newPosition, Board board) {
        position = newPosition;
        board.updatePiecePosition(this, newPosition);
    }

    public Position getPosition() {
        return position;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPosition(Position newPosition) {
        position = newPosition;
    }
}
