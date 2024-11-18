package gyges;

public abstract class Piece {
    private Position position;
    private Player player;
    public abstract int getMoveDistance();
    public void move(Position newPosition, Board board) {
        int distance = calculateDistance(newPosition);
        // Validate move distance
        if (distance == getMoveDistance() && board.isPositionEmpty(newPosition)) {
            // Update the board first
            board.updatePiecePosition(this, newPosition);
            // Update the piece's position
            position = newPosition;
        }
    }
    private int calculateDistance(Position newPosition) {
        return position.getX() - newPosition.getX() + position.getY() - newPosition.getY();
    }
    public Position getPosition() {return position;}
    public Player getPlayer() {return player;}
}