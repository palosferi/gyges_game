package gyges;

public class Game {
    private final Board board = new Board();
    boolean closerSideActive = true;

    public boolean move(Position from, Position to) {
        if(from.y()!=activeRow()) {
            return false;
        }
        boolean moved = false;
        while(!moved) {
            moved = board.move(from, to);
            from = to;
        }
        return true;
    }

    public int activeRow() {
        if (closerSideActive) {
            for(int y = 0; y < 5; y++) {
                for(int x = 0; x < 6; x++) {
                    if(board.getPieceAt(new Position(x, y)) != null) {
                        return y;
                    }
                }
            }
        } else {
            for(int y = 5; y > 0; y--) {
                for(int x = 0; x < 6; x++) {
                    if(board.getPieceAt(new Position(x, y))!= null) {
                        return y;
                    }
                }
            }
        }
        return -1;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isOver() {
        return false;
    }
}