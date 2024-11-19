package gyges;

import gyges.enums.PlayerType;

public class Game {
    protected Player player1, player2;
    protected GameLogic gameLogic;
    private Player currentPlayer;

    public Game() {
        this.gameLogic = new GameLogic();
        gameLogic.board = new Board(6); // Gyges is typically played on a 6x6 board
        this.player1 = new Player(PlayerType.PLAYER_ONE);
        this.player2 = new Player(PlayerType.PLAYER_TWO);
    }

    public void startNewGame() {
        gameLogic.initializeGame(player1);
        currentPlayer = player1;
        System.out.println("New game started. Player 1 goes first.");
    }

    public void takeTurn(Piece piece, Position newPosition) {
        if (gameLogic.isValidMove(piece, newPosition)) {
            gameLogic.makeMove(piece, newPosition);
            System.out.println(currentPlayer.getType() + " moved " + piece + " to " + newPosition);

            if (gameLogic.isGameOver()) {
                System.out.println("Game over! " + getWinner().getType() + " wins!");
            } else {
                switchPlayer();
                System.out.println("It's now " + currentPlayer.getType() + "'s turn.");
            }
        } else {
            System.out.println("Invalid move. Try again.");
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getWinner() {
        if (!gameLogic.isGameOver()) {
            return null;
        }
        // Assuming the last player to make a move is the winner
        return (currentPlayer == player1) ? player2 : player1;
    }

    public Board getBoard() {
        return gameLogic.board;
    }
}