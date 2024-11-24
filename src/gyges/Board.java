package gyges;

public class Board {
    private static final int BOARD_SIZE = 6;
    private final Piece[][] grid;

    public Board() {
        this.grid = new Piece[BOARD_SIZE][BOARD_SIZE];
    }

    public int getSize() {
        return BOARD_SIZE;
    }

    public boolean movePiece(Position from, Position to) {
        if (isPositionEmpty(to)) {
            grid[to.x()][to.y()] = grid[from.x()][from.y()];

            // Update the piece's internal position
            grid[to.x()][to.y()].move(to, this);
        } else {
            System.out.println("Position is already occupied!");
        }
        return true;
    }

    public boolean placePiece(Piece piece, Position newPosition) {
        Position oldPosition = piece.getPosition();

        // Clear old position on the grid
        if (oldPosition != null) {
            grid[oldPosition.x()][oldPosition.y()] = null;
        }

        // Set the piece at the new position
        grid[newPosition.x()][newPosition.y()] = piece;
        return true;
    }

    public boolean isPositionEmpty(Position position) {
        return grid[position.x()][position.y()] == null;
    }

    public Piece getPieceAt(Position position) {
        return grid[position.x()][position.y()];
    }

    public boolean isWithinBounds(Position newPosition) {
        return newPosition.x() >= 0 && newPosition.x() < grid.length &&
               newPosition.y() >= 0 && newPosition.y() < grid[0].length;
    }

    public Board copy() {
        Board clonedBoard = new Board();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j]!= null) {
                    clonedBoard.placePiece(grid[i][j].copy(), new Position(i, j));
                }
            }
        }
        return clonedBoard;
    }

    public boolean isSetupComplete() {
        for (Piece[] pieces : grid) {
            if (pieces[0] == null || pieces[5] == null) {
                return false;
            }
        }
        return true;
    }
}