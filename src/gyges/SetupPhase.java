package gyges;

import java.util.Scanner;

public class SetupPhase {
    private Board board;
    private Player playerOne;
    private Player playerTwo;
    private Scanner scanner;

    public SetupPhase(Board board, Player playerOne, Player playerTwo) {
        this.board = board;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Choose setup method:");
        System.out.println("1. Alternating placement");
        System.out.println("2. One player places all pieces");
        int choice = scanner.nextInt();

        if (choice == 1) {
            alternatingPlacement();
        } else {
            onePlayerPlacement();
        }
    }

    private void alternatingPlacement() {
        Player currentPlayer = playerOne;
        int piecesPlaced = 0;

        while (piecesPlaced < 12) {
            System.out.println(currentPlayer.getType() + "'s turn to place a piece.");
            placePiece(currentPlayer, 5 - piecesPlaced / 2);
            piecesPlaced++;
            currentPlayer = (currentPlayer == playerOne) ? playerTwo : playerOne;
        }
    }

    private void onePlayerPlacement() {
        System.out.println(playerOne.getType() + " places all pieces.");
        for (int i = 0; i < 6; i++) {
            placePiece(playerOne, 5);
        }

        System.out.println(playerTwo.getType() + " places all pieces.");
        for (int i = 0; i < 6; i++) {
            placePiece(playerTwo, 0);
        }
    }

    private void placePiece(Player player, int row) {
        System.out.println("Enter column (0-5) and piece type (1-3) separated by space:");
        int col = scanner.nextInt();
        int type = scanner.nextInt();

        while (col < 0 || col > 5 || type < 1 || type > 3 || board.getPieceAt(new Position(row, col)) != null) {
            System.out.println("Invalid input or position already occupied. Try again:");
            col = scanner.nextInt();
            type = scanner.nextInt();
        }

        Piece piece = null;
        if(type == 1) {
            piece = new Piece1(player);
        } else if(type == 2) {
            piece = new Piece2(player);
        } else {
            piece = new Piece3(player);
        }
        board.placePiece(piece, new Position(row, col));
        System.out.println("Piece placed at row " + row + ", column " + col);
    }
}