package gyges;

import com.google.gson.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Stack;

import static gyges.GameState.*;

public class Game {
    private final MainFrame mainFrame;
    private final Board board;
    private boolean player = true; // Igaz: Alsó játékos van soron, Hamis: Felső játékos van soron
    private Position selectedClick; // Első klikk
    private Position nextClick; // Második kattintás
    private Position lastJumpFromHere = selectedClick;
    private GameState state;
    private static final String defaultFileExtension = ".json";
    private final Stack<Pair<Position, Position>> moveHistory = new Stack<>();

    public Game(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        board = new Board(mainFrame.isDarkTheme);
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
            Piece piece = board.getPieceAt(selectedClick.x(), selectedClick.y());
            Piece lastJumpPiece = board.getPieceAt(lastJumpFromHere.x(), lastJumpFromHere.y());
            if(piece.isStart() && board.findIfWins(lastJumpFromHere, lastJumpPiece.getState().getHeight()-1, player, new LinkedList<>())) {
                gameOver();
            }
        }
    }

    public void bottomCellClicked() {
        if(state == PLAYING && !player && selectedClick != null && nextClick == null) {
            Piece piece = board.getPieceAt(selectedClick.x(), selectedClick.y());
            Piece lastJumpPiece = board.getPieceAt(lastJumpFromHere.x(), lastJumpFromHere.y());
            if(piece.isStart() && board.findIfWins(lastJumpFromHere, lastJumpPiece.getState().getHeight()-1, player, new LinkedList<>())) {
                gameOver();
            }
        }
    }

    public void clicked(int x, int y) {
        switch (state) {
            case IDLE:
                // Itt nem csinálunk semmit
                board.setAllCellsUnselectedAndNonstart();
                mainFrame.table.repaint();
                break;
            case SETUP:
                // Itt a bábúk rakottak legyenek

                // Még nincs kijelölve egy se -> első klikk
                if (selectedClick == null) {
                    selectedClick = new Position(x, y);
                    board.exploreSwappables(selectedClick);
                }
                // Második klikk
                else if (nextClick == null) {
                    nextClick = new Position(x, y);
                    if((selectedClick.y() == 0 || selectedClick.y() == 5) && (nextClick.y() == 0 || nextClick.y() == 5)) {
                        board.swapPieces(selectedClick, nextClick);
                    }
                    selectedClick = null;
                    nextClick = null;
                    board.setAllCellsUnselectedAndNonstart();
                    mainFrame.table.repaint();
                }
                break;
            case PLAYING:
                // Itt a játék közben van
                int activeRow = board.getActiveRow(player);
                if((selectedClick != null && selectedClick.equals(new Position(x, y))) || board.getPieceAt(new Position(x, y)).getState() == CellState.EMPTY) {
                    selectedClick = null;
                    nextClick = null;
                    board.setAllCellsUnselectedAndNonstart(); // Minden kiválasztás törlése
                    mainFrame.table.repaint();
                } else if (y == activeRow && (selectedClick == null || nextClick == null && !board.isPositionJumpable(new Position(x, y)))) {
                    selectedClick = new Position(x, y);
                    board.setAllCellsUnselectedAndNonstart(); // Minden kiválasztás törlése
                    mainFrame.table.repaint();
                    board.exploreMoves(selectedClick); // Léphető pozíciók kiválasztása
                    board.setStartPosition(selectedClick); // Kezdő pozíció beállítása
                } else if (selectedClick != null && nextClick == null && board.isPositionJumpable(new Position(x, y))) {
                    nextClick = new Position(x, y);
                    if (board.tryToMovePiece(selectedClick, nextClick)) {
                        moveHistory.push(new Pair<>(selectedClick, nextClick));
                        selectedClick = null;
                        nextClick = null;
                        board.setAllCellsUnselectedAndNonstart(); // Minden kiválasztás törlése
                        mainFrame.table.repaint();
                        player = !player; // játékosváltás
                        mainFrame.updatePlayerLabel(getPlayer());
                    } else {
                        board.setAllCellsUnselectedAndNonstart(); // Minden kiválasztás törlése
                        mainFrame.table.repaint();
                        board.setStartPosition(selectedClick); // Kezdő pozíció beállítása
                        board.exploreMoves(nextClick);
                        lastJumpFromHere = nextClick;
                        nextClick = null;
                    }
                }
                break;
        } //TODO: kilökés
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
        JFileChooser fileChooser = new JFileChooser(new File("."));
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json")); // Restrict to JSON files

        int returnValue = fileChooser.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Append .json if not already present
            if (!selectedFile.getName().endsWith(defaultFileExtension)) {
                selectedFile = new File(selectedFile.getAbsolutePath() + defaultFileExtension);
            }

            try {
                GameStateData gameStateData = new GameStateData(board.getBoardState(), player);
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
    } //TODO: save movehistory, ?state?, board.isDarkMode

    public void loadGameState() {
        JFileChooser fileChooser = new JFileChooser(new File("."));
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json")); // Restrict to JSON files

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Ensure the selected file is a JSON file
            if (selectedFile.getName().endsWith(defaultFileExtension)) {
                try {
                    Gson gson = new Gson();
                    FileReader fileReader = new FileReader(selectedFile);
                    GameStateData gameStateData = gson.fromJson(fileReader, GameStateData.class);
                    fileReader.close();

                    // Apply the loaded state
                    board.setBoardState(gameStateData.getBoardState());
                    player = gameStateData.isCurrentPlayer();
                    mainFrame.updatePlayerLabel(getPlayer());
                    mainFrame.table.revalidate();
                    mainFrame.table.repaint();

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
    }  //TODO: load movehistory, ?state?, board.isDarkMode

    public void undoMove() {
        if (!moveHistory.isEmpty()) {
            Pair<Position, Position> lastMove = moveHistory.pop();
            Position source = lastMove.getKey();
            Position destination = lastMove.getValue();

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
    }
}