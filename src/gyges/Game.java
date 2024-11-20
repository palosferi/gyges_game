package gyges;

import gyges.enums.PlayerType;

public class Game {
    private Board board;
    private Player playerOne;
    private Player playerTwo;
    private Player currentPlayer;
    private GameLogic gameLogic;

    public Game() {
        this.board = new Board(); // 6x7 board to allow for the winning move
        this.playerOne = new Player(PlayerType.PLAYER_ONE);
        this.playerTwo = new Player(PlayerType.PLAYER_TWO);
        this.gameLogic = new GameLogic();
        this.currentPlayer = playerOne;
    }

    public void startNewGame() {
        board.clear();
        setupInitialPieces();
        currentPlayer = playerOne;
    }

    private void setupInitialPieces() {
        // Implement the initial setup logic here
        // Allow players to place pieces alternately or use the alternative setup
    }

    public boolean takeTurn(Position from, Position to) {
        if (!gameLogic.isValidMove(board, currentPlayer, from, to)) {
            System.out.println("Invalid move. Try again.");
            return false;
        }

        Move move = gameLogic.executeMove(board, from, to);
        if (gameLogic.isWinningMove(board, move, currentPlayer)) {
            if (!gameLogic.canOpponentPrevent(board, move, currentPlayer)) {
                System.out.println("Game over! " + currentPlayer.getType() + " wins!");
                return true;
            }
        }

        switchPlayer();
        return false;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == playerOne) ? playerTwo : playerOne;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getWinner() {
        if (!gameLogic.isGameOver(board)) {
            return null;
        }

        // Check if Player One has a piece in the bottom row
        for (int col = 0; col < board.getSize(); col++) {
            Piece piece = board.getPieceAt(new Position(0, col)); // Top row
            if (piece != null && piece.getPlayer().getType() == PlayerType.PLAYER_ONE) {
                return playerOne;
            }
        }

        // Check if Player Two has a piece in the top row
        for (int col = 0; col < board.getSize(); col++) {
            Piece piece = board.getPieceAt(new Position(board.getSize() - 1, col)); // Bottom row
            if (piece != null && piece.getPlayer().getType() == PlayerType.PLAYER_TWO) {
                return playerTwo;
            }
        }

        return null; // Fallback, though this case shouldn't happen
    }

    public Board getBoard() {
        return board;
    }
}