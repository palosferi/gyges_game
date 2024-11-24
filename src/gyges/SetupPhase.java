package gyges;

import gyges.gui.GameController;

public class SetupPhase {
    private final Board board;
    private final Player playerOne;
    private final Player playerTwo;

    public SetupPhase(Board board, Player playerOne, Player playerTwo) {
        this.board = board;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public void start(GameController controller) {
        controller.showMessage("Setup phase started. Place your pieces.");
    }

    public boolean placePiece(Player player, Position position, int pieceType) {
        if (position.y() != (player == playerOne ? 5 : 0)) {
            return false;
        }

        Piece piece;
        switch (pieceType) {
            case 1: piece = new Piece1(player); break;
            case 2: piece = new Piece2(player); break;
            case 3: piece = new Piece3(player); break;
            default: return false;
        }

        return board.placePiece(piece, position);
    }

    public boolean isSetupComplete() {
        return board.isSetupComplete();
    }
}