package gyges;

import gyges.enums.PlayerType;
import gyges.enums.GamePhase;

public class Game {
    private Board board;
    private Player playerOne;
    private Player playerTwo;
    private Player currentPlayer;
    private boolean gameOver;
    private Player winner;
    private GamePhase currentPhase;

    public Game() {
        startNewGame();
    }

    public void startNewGame() {
        board = new Board();
        playerOne = new Player(PlayerType.PLAYER_ONE);
        playerTwo = new Player(PlayerType.PLAYER_TWO);
        currentPlayer = playerOne;
        gameOver = false;
        winner = null;
        currentPhase = GamePhase.SETUP;
    }

    public boolean placePiece(Position position, int pieceType) {
        if (currentPhase != GamePhase.SETUP) {
            return false;
        }

        Piece piece = createPiece(pieceType);
        if (piece == null) {
            return false;
        }

        boolean placed = board.placePiece(piece, position);
        if (placed) {
            if (board.isSetupComplete()) {
                currentPhase = GamePhase.PLAY;
            } else {
                switchPlayer();
            }
        }
        return placed;
    }

    private Piece createPiece(int pieceType) {
        return switch (pieceType) {
            case 1 -> new Piece1(currentPlayer);
            case 2 -> new Piece2(currentPlayer);
            case 3 -> new Piece3(currentPlayer);
            default -> null;
        };
    }

    public boolean movePiece(Position from, Position to) {
        if (currentPhase != GamePhase.PLAY) {
            return false;
        }

        boolean moved = board.movePiece(from, to);
        if (moved) {
            if (GameLogic.isGameOver(board)) {
                gameOver = true;
                winner = currentPlayer;
            } else {
                switchPlayer();
            }
        }
        return moved;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer.getType() == PlayerType.PLAYER_ONE) ? playerTwo : playerOne;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Player getWinner() {
        return winner;
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(GamePhase phase) {
        this.currentPhase = phase;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }
}