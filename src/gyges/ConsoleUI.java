package gyges;

import gyges.enums.PlayerType;

import java.util.Scanner;

public class ConsoleUI {
    private final Game game;
    private final Scanner scanner;

    public ConsoleUI() {
        this.game = new Game();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        game.startNewGame();
        setupInitialPieces();

        boolean gameOver = false;
        while (!gameOver) {
            displayBoard();
            gameOver = makeMove();
        }
        displayBoard();
        announceWinner();
    }

    private void setupInitialPieces() {
        // Implement logic for players to place their pieces
    }

    private boolean makeMove() {
        Player currentPlayer = game.getCurrentPlayer();
        System.out.println(currentPlayer.getType() + "'s turn");

        Position from = getPositionInput("Enter the position of the piece to move (row column): ");
        Position to = getPositionInput("Enter the position to move to (row column): ");

        boolean moveMade = game.takeTurn(from, to);
        if (!moveMade) {
            System.out.println("Invalid move. Try again.");
        }
        return moveMade;
    }

    private void displayBoard() {
        Board board = game.getBoard();
        System.out.println("Current Board:");
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                Piece piece = board.getPieceAt(new Position(i, j));
                if (piece == null) {
                    System.out.print("- ");
                } else if (piece.getPlayer().getType() == PlayerType.PLAYER_ONE) {
                    System.out.print(getPieceSymbol(piece) + " ");
                } else {
                    System.out.print(getPieceSymbol(piece).toLowerCase() + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private String getPieceSymbol(Piece piece) {
        if (piece instanceof Piece1) return "A";
        if (piece instanceof Piece2) return "B";
        if (piece instanceof Piece3) return "C";
        return "?";
    }

    private Position getPositionInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int row = scanner.nextInt();
                int col = scanner.nextInt();
                return new Position(row, col);
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter two numbers separated by a space.");
                scanner.nextLine(); // Clear the input buffer
            }
        }
    }

    private void announceWinner() {
        Player winner = game.getWinner();
        if (winner != null) {
            System.out.println("Game Over! " + winner.getType() + " wins!");
        } else {
            System.out.println("Game Over! It's a draw!");
        }
    }
}