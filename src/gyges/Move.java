package gyges;

import gyges.piece.Piece;

public record Move(Position from, Position to, Piece movingPiece, Piece displacedPiece) {
}
