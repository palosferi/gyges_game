package gyges;

import gyges.enums.PlayerType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameLogic {

    public boolean isValidMove(Board board, Player player, Position from, Position to) {
        Piece piece = board.getPieceAt(from);
        if (piece == null || !player.getPieces().contains(piece) || !isInActiveRow(player, from)) {
            return false; // No piece at the position or not owned by the player or not in the active row
        }
        // Check path validity
        return isPathValid(board, from, to, piece);
    }

    private boolean isPathValid(Board board, Position from, Position to, Piece piece) {
        int remainingMoves = piece.getValue();
        Position current = from;

        while (remainingMoves > 0) {
            // Get all possible next positions
            List<Position> nextPositions = getValidAdjacentPositions(board, current);

            // If we've reached the destination, the move is valid
            if (nextPositions.contains(to)) {
                return true;
            }

            // Find the next position that's closest to the destination
            Position nextStep = getClosestPosition(nextPositions, to);

            if (nextStep == null) {
                // No valid next step, path is blocked
                return false;
            }

            Piece occupyingPiece = board.getPieceAt(nextStep);
            if (occupyingPiece != null) {
                // Handle jump logic
                remainingMoves -= occupyingPiece.getValue();
                if (remainingMoves < 0) {
                    return false; // Can't jump over this piece
                }
                // Update current position to the space after the jumped piece
                current = getPositionAfterJump(current, nextStep);
            } else {
                // Regular move
                current = nextStep;
                remainingMoves--;
            }
        }

        // If we've used all moves and haven't reached the destination, it's not a valid path
        return false;
    }

    private List<Position> getValidAdjacentPositions(Board board, Position position) {
        List<Position> adjacentPositions = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right

        for (int[] direction : directions) {
            Position newPosition = new Position(position.getX() + direction[0], position.getY() + direction[1]);
            if (board.isWithinBounds(newPosition)) {
                adjacentPositions.add(newPosition);
            }
        }

        return adjacentPositions;
    }

    private Position getClosestPosition(List<Position> positions, Position target) {
        return positions.stream()
                .min(Comparator.comparingInt(p -> calculateDistance(p, target)))
                .orElse(null);
    }

    private Position getPositionAfterJump(Position current, Position jumped) {
        int dx = jumped.getX() - current.getX();
        int dy = jumped.getY() - current.getY();
        return new Position(jumped.getX() + dx, jumped.getY() + dy);
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
            if ((player.getType()==PlayerType.PLAYER_ONE && piece.getPosition().getY() < position.getY()) ||
                    (player.getType()==PlayerType.PLAYER_TWO && piece.getPosition().getY() > position.getY())) {
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

    public void undoMove(Board board, Move move) {
        // Move the piece back to its original position
        board.movePiece(move.getTo(), move.getFrom());

        // If a piece was displaced, put it back
        if (move.getDisplacedPiece() != null) {
            board.placePiece(move.getDisplacedPiece(), move.getTo());
        }
    }
}