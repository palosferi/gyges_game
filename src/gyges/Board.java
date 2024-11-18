package gyges;

public class Board {
    private Piece[][] grid;
    public Board(int size) {
        /* Initialize grid of given size */
        grid = new Piece[size][size];
    }

    public void placePiece(Piece piece, Position position) {
        if (isPositionEmpty(position)) {
            grid[position.getX()][position.getY()] = piece;

            // Update the piece's internal position
            piece.move(position, this);
        } else {
            System.out.println("Position is already occupied!");
        }
    }

    public void updatePiecePosition(Piece piece, Position newPosition) {
        Position oldPosition = piece.getPosition();

        // Clear old position on the grid
        if (oldPosition != null) {
            grid[oldPosition.getX()][oldPosition.getY()] = null;
        }

        // Set the piece at the new position
        grid[newPosition.getX()][newPosition.getY()] = piece;
    }

    public boolean isPositionEmpty(Position position) {
        return grid[position.getX()][position.getY()] == null;
    }

    public Piece getPieceAt(Position position) {
        return grid[position.getX()][position.getY()];
    }

    public boolean isWithinBounds(Position newPosition) {

    }
}