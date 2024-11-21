package gyges;

public class Board {
    private static final int BOARD_SIZE = 6;
    private Piece[][] grid;

    public Board() {
        this.grid = new Piece[BOARD_SIZE][BOARD_SIZE];
    }

    public int getSize() {
        return BOARD_SIZE;
    }

    public void movePiece(Position from, Position to) {
        if (isPositionEmpty(to)) {
            grid[to.getX()][to.getY()] = grid[from.getX()][from.getY()];

            // Update the piece's internal position
            grid[to.getX()][to.getY()].move(to, this);
        } else {
            System.out.println("Position is already occupied!");
        }
    }

    public void placePiece(Piece piece, Position newPosition) {
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
        return newPosition.getX() >= 0 && newPosition.getX() < grid.length &&
               newPosition.getY() >= 0 && newPosition.getY() < grid[0].length;
    }

    private String createEdgeIdentifier(Position from, Position to) {
        return from.getX() + "," + from.getY() + "->" + to.getX() + "," + to.getY();
    }

    public void clear() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = null;
            }
        }
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
}