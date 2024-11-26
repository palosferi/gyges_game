package gyges;

import static gyges.GameState.*;

public class Game {
    private final Board board = new Board();
    //boolean closerSideActive = true;

    private Position selectedClick; // Elős klikk
    private Position nextClick; // Második kattintás

    private GameState state;

    public Game(GameState state) {
        this.state = state;
        selectedClick = null;
        nextClick = null;
    }

    public void init() {
        board.init();
        // Itt majd lesznek bábuk rakosgathatóva
    }

    public void run() {
        // Ez igazából nem csinál semmit
    }

    public void clicked(int x, int y) {
        switch (state.current) {
            case IDLE:
                // Itt nem csinálunk semmit
                board.setAllCellsUnselected();
                break;
            case SETUP:
                // Itt a bábúk rakottak legyenek

                // Még nincs kijelölve egy se -> első klikk
                if (selectedClick == null) {
                    selectedClick = new Position(x, y);
                    board.exploreSwappables(selectedClick);
                }
                // Második klikk
                else if (nextClick == null) {
                    nextClick = new Position(x, y);
                    board.swapPieces(selectedClick, nextClick);
                    selectedClick = null;
                    nextClick = null;
                    board.setAllCellsUnselected();
                }
                break;
            case PLAYING:
                // Itt a játék közben van
                board.setAllCellsUnselected();
                    //board.exploreMoves(selectedClick);
                break;
        }
    }

//    public boolean move(Position from, Position to) {
//        if(from.y()!=activeRow()) {
//            return false;
//        }
//        boolean moved = false;
//        while(!moved) {
//            moved = board.move(from, to);
//            from = to;
//        }
//        return true;
//    }

//    public int activeRow() {
//        if (closerSideActive) {
//            for(int y = 0; y < 5; y++) {
//                for(int x = 0; x < 6; x++) {
//                    if(board.getPieceAt(new Position(x, y)) != null) {
//                        return y;
//                    }
//                }
//            }
//        } else {
//            for(int y = 5; y > 0; y--) {
//                for(int x = 0; x < 6; x++) {
//                    if(board.getPieceAt(new Position(x, y))!= null) {
//                        return y;
//                    }
//                }
//            }
//        }
//        return -1;
//    }

    public Board getBoard() {
        return board;
    }



//    public boolean isOver() {
//        return false;
//    }


}