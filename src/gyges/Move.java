package gyges;

public class Move {
    private Position from;
    private Position to;
    private Piece movingPiece;
    private Piece displacedPiece;

    public Move(Position from, Position to, Piece movingPiece, Piece displacedPiece) {
        this.from = from;
        this.to = to;
        this.movingPiece = movingPiece;
        this.displacedPiece = displacedPiece;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Piece getMovingPiece() {
        return movingPiece;
    }

    public Piece getDisplacedPiece() {
        return displacedPiece;
    }
}
