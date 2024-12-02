package gyges;

import com.google.gson.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Deque;

import static gyges.GameState.*;

public class Game {
    private final MainFrame mainFrame;
    private final Board board;
    private boolean player = true; // Igaz: Alsó játékos van soron, Hamis: Felső játékos van soron
    private Position selectedClick; // Első klikk
    private Position nextClick; // Második kattintás
    private Position lastJumpFromHere = null;
    private GameState state;
    private static final String DEFAULT_FILE_EXTENSION = ".json";
    private final Deque<Pair<Position, Position>> moveHistory = new LinkedList<>();

    public Game(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        board = new Board(mainFrame.getIsDarkTheme());
        state = IDLE;
        selectedClick = null;
        nextClick = null;
    }

    public void run() {
        getBoard().setAllCellsUnselectedAndNonstart();
        setState(PLAYING);
        player = true;
        selectedClick = null;
        nextClick = null;
    }

    public void topCellClicked() {
        if(state == PLAYING && player && selectedClick != null && nextClick == null) {
            specialCellClicked();
        }
    }

    public void bottomCellClicked() {
        if(state == PLAYING && !player && selectedClick != null && nextClick == null) {
            specialCellClicked();
        }
    }

    private void specialCellClicked() {
        Piece piece = board.getPieceAt(selectedClick.x(), selectedClick.y());
        Piece lastJumpPiece = board.getPieceAt(lastJumpFromHere.x(), lastJumpFromHere.y());
        if(piece.isStart() && board.findIfWins(lastJumpFromHere, lastJumpPiece.getState().getHeight()-1, player, new LinkedList<>(), new Position(0, 0))) {
            gameOver();
        }
    }

    public void leftClicked(int x, int y) {
        switch (state) {
            case IDLE:
                // No action on IDLE
                board.setAllCellsUnselectedAndNonstart();
                mainFrame.table.repaint();
                break;
            case SETUP:
                // Set up pieces
                if (selectedClick == null) {
                    selectedClick = new Position(x, y);
                    board.exploreSwappables(selectedClick);
                } else if (nextClick == null && selectedClick.y() == y) {
                    nextClick = new Position(x, y);
                    if ((selectedClick.y() == 0 || selectedClick.y() == 5) && (nextClick.y() == 0 || nextClick.y() == 5)) {
                        board.swapPieces(selectedClick, nextClick);
                    }
                    selectedClick = null;
                    nextClick = null;
                    board.setAllCellsUnselectedAndNonstart();
                    mainFrame.table.repaint();
                } else {
                    selectedClick = new Position(x, y);
                    nextClick = null;
                    board.setAllCellsUnselectedAndNonstart();
                    mainFrame.table.repaint();
                    board.exploreSwappables(selectedClick);
                    mainFrame.table.repaint();
                }
                break;
            case PLAYING:
                leftClickedInPlayingState(x, y);
                break;
        }
    }

    public void leftClickedInPlayingState(int x, int y) {
        int activeRow = board.getActiveRow(player);
        if ((selectedClick != null && selectedClick.equals(new Position(x, y))) || board.getPieceAt(new Position(x, y)).getState() == CellState.EMPTY) {
            selectedClick = null;
            nextClick = null;
            board.setAllCellsUnselectedAndNonstart(); // Clear all selections
            mainFrame.table.repaint();
        } else if (y == activeRow && (selectedClick == null || nextClick == null && !board.isPositionJumpable(new Position(x, y)))) {
            selectedClick = new Position(x, y);
            board.setAllCellsUnselectedAndNonstart(); // Clear all selections
            mainFrame.table.repaint();
            board.exploreMoves(selectedClick, true); // Select movable positions
            board.setStartPosition(selectedClick); // Set start position
        } else if (selectedClick != null && nextClick == null && board.isPositionJumpable(new Position(x, y))) {
            nextClick = new Position(x, y);
            if (board.tryToMovePiece(selectedClick, nextClick)) {
                moveHistory.push(new Pair<>(selectedClick, nextClick));
                selectedClick = null;
                nextClick = null;
                board.setAllCellsUnselectedAndNonstart(); // Clear all selections
                mainFrame.table.repaint();
                player = !player; // Switch players
                mainFrame.updatePlayerLabel(getPlayer());
            } else {
                board.setAllCellsUnselectedAndNonstart(); // Minden kiválasztás törlése
                mainFrame.table.repaint();
                board.setStartPosition(selectedClick); // Kezdő pozíció beállítása
                board.exploreMoves(nextClick, true);
                lastJumpFromHere = nextClick;
                nextClick = null;
            }
        }
    }

    public void rightClicked(int x, int y) {
        if (selectedClick != null && nextClick == null && board.isPositionJumpable(new Position(x, y))) {
            nextClick = new Position(x, y);
            board.setAllCellsUnselectedAndNonstart(); // Clear all selections
            board.knockOutPiece(selectedClick, nextClick, player);
            moveHistory.push(new Pair<>(selectedClick, nextClick));
            nextClick = null;
            mainFrame.table.repaint();
        }
    }

    public Board getBoard() {
        return board;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public boolean getPlayer() {
        return player;
    }

    public void saveGameState() {
        if (state == SETUP) {
            mainFrame.appendMessage("Start the game first.");
            return;
        } else if (state == IDLE) {
            mainFrame.appendMessage("No game to save. Start a new game first.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser(new File("."));
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json")); // Restrict to JSON files

        int returnValue = fileChooser.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Append .json if not already present
            if (!selectedFile.getName().endsWith(DEFAULT_FILE_EXTENSION)) {
                selectedFile = new File(selectedFile.getAbsolutePath() + DEFAULT_FILE_EXTENSION);
            }

            try {
                GameStateData gameStateData = new GameStateData(
                        board.getBoardState(),
                        player,
                        moveHistory,
                        board.isDarkMode
                );
                Gson gson = new Gson();
                FileWriter fileWriter = new FileWriter(selectedFile);
                gson.toJson(gameStateData, fileWriter);
                fileWriter.close();

                mainFrame.appendMessage("Game saved to: " + selectedFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                mainFrame.appendMessage("Error saving game: " + e.getMessage());
            }
        } else {
            mainFrame.appendMessage("Save operation cancelled by user.");
        }
    }

    public void loadGameState() {
        JFileChooser fileChooser = new JFileChooser(new File("."));
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json")); // Restrict to JSON files

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Ensure the selected file is a JSON file
            if (selectedFile.getName().endsWith(DEFAULT_FILE_EXTENSION)) {
                try {
                    Gson gson = new Gson();
                    FileReader fileReader = new FileReader(selectedFile);
                    GameStateData gameStateData = gson.fromJson(fileReader, GameStateData.class);
                    fileReader.close();

                    // Apply the loaded state
                    board.setBoardState(gameStateData.boardState());
                    player = gameStateData.currentPlayer();
                    moveHistory.clear();
                    moveHistory.addAll(gameStateData.moveHistory());
                    board.isDarkMode = gameStateData.isDarkMode();
                    mainFrame.setIsDarkTheme(!board.isDarkMode);
                    mainFrame.toggleTheme(mainFrame.themeButton);
                    mainFrame.updatePlayerLabel(getPlayer());
                    state = PLAYING;
                    mainFrame.actionButton.setText("Undo Move");
                    mainFrame.themeButton.setText(String.format("<html><div width='72'>%-10s</div></html>", (board.isDarkMode ? "Light Theme" : "Dark Theme")));

                    mainFrame.appendMessage("Game loaded from: " + selectedFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                    mainFrame.appendMessage("Error loading game: " + e.getMessage());
                } catch (JsonSyntaxException e) {
                    mainFrame.appendMessage("Error: Invalid JSON file format.");
                }
            } else {
                mainFrame.appendMessage("Error: Please select a valid JSON file.");
            }
        } else {
            mainFrame.appendMessage("Load operation cancelled by user.");
        }
    }

    public void undoMove() {
        if (!moveHistory.isEmpty()) {
            Pair<Position, Position> lastMove = moveHistory.pop();
            Position source = lastMove.key();
            Position destination = lastMove.value();

            // Undo the move on the board
            board.swapPieces(destination, source);

            // Reset the player state and update the board
            player = !player;
            mainFrame.updatePlayerLabel(getPlayer());
            mainFrame.table.repaint();

            mainFrame.appendMessage("Move undone.");
        } else {
            mainFrame.appendMessage("No moves to undo.");
        }
    }

    public void gameOver() {
        mainFrame.actionButton.setText("New Game");
        mainFrame.appendMessage("Game over! Player " + (player? "1 (bottom) wins!" : "2 (top) wins!"));
        board.setAllCellsUnselectedAndNonstart();
        mainFrame.table.repaint();
        setState(IDLE);
        selectedClick = null;
        nextClick = null;
    }
}