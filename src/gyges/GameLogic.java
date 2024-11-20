package gyges;

import gyges.enums.PlayerType;

public class GameLogic {

    public boolean isValidMove(Board board, Player player, Position from, Position to) {
        Piece piece = board.getPieceAt(from);
        if (piece == null || !player.getPieces().contains(piece)) {
            return false; // No piece at the position or not owned by the player
        }

        int requiredDistance = piece.getValue();
        int calculatedDistance = calculateDistance(from, to);

        // Check if the exact distance is covered
        if (requiredDistance != calculatedDistance) {
            return false;
        }

        // Check path validity
        return isPathValid(board, from, to, piece);
    }

    private boolean isPathValid(Board board, Position from, Position to, Piece piece) {
        Position current = from;
        int distanceCovered = 0;

        // Simulate path traversal
        while (!current.equals(to)) {
            Position nextStep = getNextStep(current, to);
            distanceCovered++;

            if (!board.isWithinBounds(nextStep)) {
                return false; // Out of bounds
            }

            Piece occupyingPiece = board.getPieceAt(nextStep);
            if (occupyingPiece != null && !current.equals(from)) {
                // Handle jump logic if landing on an occupied square
                distanceCovered += occupyingPiece.getValue();
            }

            current = nextStep;

            // Ensure we don't exceed the allowed distance
            if (distanceCovered > piece.getValue()) {
                return false;
            }
        }

        return true;
    }

    private Position getNextStep(Position current, Position destination) {
        int dx = Integer.compare(destination.getX(), current.getX());
        int dy = Integer.compare(destination.getY(), current.getY());
        return new Position(current.getX() + dx, current.getY() + dy);
    }

    public Move executeMove(Board board, Position from, Position to) {
        Piece movingPiece = board.getPieceAt(from);
        Piece destinationPiece = board.getPieceAt(to);

        board.movePiece(from, to);

        if (destinationPiece != null) {
            // Handle displacement or continuation
            Position newPosition = handleCollision(board, to, movingPiece, destinationPiece);
            return new Move(from, newPosition, movingPiece, destinationPiece);
        }

        return new Move(from, to, movingPiece, null);
    }

    private Position handleCollision(Board board, Position position, Piece movingPiece, Piece destinationPiece) {
        // Implement jump or push logic based on game rules
        Position newPosition = calculateNewPosition(position, destinationPiece.getValue());
        destinationPiece.setPosition(newPosition);
        return newPosition;
    }

    private Position calculateNewPosition(Position start, int distance) {
        return new Position(start.getX() + distance, start.getY()); // Example logic
    }

    public boolean isWinningMove(Board board, Move move, Player currentPlayer) {
        if (currentPlayer.getType() == PlayerType.PLAYER_ONE) {
            return move.getTo().getY() == -1;
        } else {
            return move.getTo().getY() == board.getSize();
        }
    }

    public boolean canOpponentPrevent(Board board, Move winningMove, Player currentPlayer) {
        Player opponent = (currentPlayer.getType() == PlayerType.PLAYER_ONE) ?
                new Player(PlayerType.PLAYER_TWO) : new Player(PlayerType.PLAYER_ONE);

        // Create a copy of the board to simulate moves
        Board simulationBoard = board.copy();

        // Apply the winning move to the simulation board
        simulationBoard.movePiece(winningMove.getFrom(), winningMove.getTo());

        // Check all possible moves for the opponent
        for (int row = 0; row < simulationBoard.getSize(); row++) {
            for (int col = 0; col < simulationBoard.getSize(); col++) {
                Position from = new Position(row, col);
                Piece piece = simulationBoard.getPieceAt(from);

                if (piece != null && piece.getPlayer().getType() == opponent.getType()) {
                    // Check all possible destinations for this piece
                    for (int toRow = 0; toRow < simulationBoard.getSize(); toRow++) {
                        for (int toCol = 0; toCol < simulationBoard.getSize(); toCol++) {
                            Position to = new Position(toRow, toCol);

                            if (isValidMove(simulationBoard, opponent, from, to)) {
                                // If there's a valid move that prevents the win, return true
                                Move preventiveMove = executeMove(simulationBoard, from, to);
                                if (!isWinningMove(simulationBoard, preventiveMove, currentPlayer)) {
                                    return true;
                                }
                                // Undo the move for the next iteration
                                simulationBoard = board.copy();
                            }
                        }
                    }
                }
            }
        }

        // If no preventive move was found, return false
        return false;
    }

    private boolean isInActiveRow(Player player, Position position) {
        for(Piece piece : player.getPieces()) {
            if (player.getType()==PlayerType.PLAYER_ONE && piece.getPosition().getY() < position.getY()) {
                return false;
            } else if (player.getType()==PlayerType.PLAYER_TWO && piece.getPosition().getY() > position.getY()) {
                return false;
            }
        }
        return true;
    }

    private int calculateDistance(Position from, Position to) {
        // Implement logic to calculate the distance between the two positions
        return Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY());
    }

    public boolean isGameOver(Board board) {
        // Check if any piece from Player One has reached the last row
        for (int col = 0; col < board.getSize(); col++) {
            Piece piece = board.getPieceAt(new Position(0, col)); // Top row
            if (piece != null && piece.getPlayer().getType() == PlayerType.PLAYER_ONE) {
                return true; // Player One wins
            }
        }

        // Check if any piece from Player Two has reached the first row
        for (int col = 0; col < board.getSize(); col++) {
            Piece piece = board.getPieceAt(new Position(board.getSize() - 1, col)); // Bottom row
            if (piece != null && piece.getPlayer().getType() == PlayerType.PLAYER_TWO) {
                return true; // Player Two wins
            }
        }

        return false; // Game is not over
    }
}