package gyges;

import gyges.enums.PlayerType;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Game implements Serializable {
    @Expose private Board board;
    @Expose private Player currentPlayer;
    @Expose private GameLogic gameLogic;
    private Player playerOne;
    private Player playerTwo;
    private SetupPhase setupPhase;

    public Game() {
        this.board = new Board(); // 6x7 board to allow for the winning move
        this.playerOne = new Player(PlayerType.PLAYER_ONE);
        this.playerTwo = new Player(PlayerType.PLAYER_TWO);
        this.gameLogic = new GameLogic();
        this.currentPlayer = playerOne;
        this.setupPhase = new SetupPhase(board, playerOne, playerTwo);
    }

    public void startNewGame() {
        board.clear();
        setupPhase.start();
        currentPlayer = playerOne;
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
            } else {
                System.out.println("This move can be prevented. Try another move.");
                gameLogic.undoMove(board, move);
                return false;
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

    public void copyFrom(Game other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot copy from a null Game object.");
        }

        // Copy the board
        this.board = other.board.copy();

        // Copy players
        this.playerOne = new Player(other.playerOne.getType());
        this.playerOne.setPieces(other.playerOne.getPieces());

        this.playerTwo = new Player(other.playerTwo.getType());
        this.playerTwo.setPieces(other.playerTwo.getPieces());

        // Copy current player reference
        this.currentPlayer = (other.currentPlayer.getType() == PlayerType.PLAYER_ONE) ? this.playerOne : this.playerTwo;

        // Copy game logic (if any additional logic needs to be reset, do so here)
        this.gameLogic = new GameLogic();

        // Copy setup phase if needed (optional, depending on game design)
        this.setupPhase = new SetupPhase(this.board, this.playerOne, this.playerTwo);
    }

}