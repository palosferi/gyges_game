package gyges;

public class Board {
    private Piece[][] grid;
    private int size;

    public Board(int size) {
        /* Initialize grid of given size */
        grid = new Piece[size][size];
        this.size = size;
    }

    public int getSize() {
        return size;
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
        int x = newPosition.getX();
        int y = newPosition.getY();
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }

    public void clearBoard() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = null;
            }
        }
    }
}