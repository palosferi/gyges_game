package gyges;

import gyges.enums.PlayerType;

public class GameLogic {
    protected Board board;
    private Player currentPlayer;

    public GameLogic() {
        board = new Board(6); // 6x6 board
    }

    public void switchPlayer(Game game) {
        currentPlayer = (currentPlayer.equals(game.player1)) ? game.player2 : game.player1;
    }

    public boolean isValidMove(Piece piece, Position newPosition) {
        int moveDistance = piece.getMoveDistance();
        // Check bounds, distance
        return board.isWithinBounds(newPosition) && calculateDistance(piece.getPosition(), newPosition) == moveDistance;
    }

    public boolean isGameOver() {
        // Check if a player has won
        if(currentPlayer.getType()==PlayerType.PLAYER_ONE) {
            for(int i=0; i<board.getSize(); i++) {
                if(board.getPieceAt(new Position(i, board.getSize()-1))==null) {
                    continue;
                } else if(board.getPieceAt(new Position(i, board.getSize()-1)).getPlayer().getType()==PlayerType.PLAYER_ONE) {
                    return true;
                }
            }
        } else {
            for(int i=0; i<board.getSize(); i++) {
                if(board.getPieceAt(new Position(i, 0)).getPlayer().getType()==PlayerType.PLAYER_TWO) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int calculateDistance(Position start, Position end) {
        return Math.abs(start.getX() - end.getX()) + Math.abs(start.getY() - end.getY());
    }

    public void initializeGame(Player player1) {
        // Initialize players and board
        currentPlayer = player1;
        board.clearBoard();
    }

    public void makeMove(Piece piece, Position newPosition) {
        board.placePiece(piece, newPosition);
        board.updatePiecePosition(piece, newPosition);
        switchPlayer(new Game());
    }
}
