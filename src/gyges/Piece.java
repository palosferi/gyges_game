package gyges;

public abstract class Piece {
    private Position position;
    private final Player player;
    private final int type;

    public Piece(Player player, int type) {
        this.player = player;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public abstract Piece copy();

    public void move(Position newPosition, Board board) {
        position = newPosition;
        board.placePiece(this, newPosition);
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
