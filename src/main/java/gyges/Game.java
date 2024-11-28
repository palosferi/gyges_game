package gyges;

import com.google.gson.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import static gyges.GameState.*;

public class Game {
    private final MainFrame mainFrame;
    private final Board board = new Board();
    private boolean player = true; // Igaz: Alsó játékos van soron, Hamis: Felső játékos van soron
    private Position selectedClick; // Elős klikk
    private Position nextClick; // Második kattintás
    private GameState state;

    public Game(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        state = IDLE;
        selectedClick = null;
        nextClick = null;
    }

    public void init() {
        board.init();
        // Itt majd lesznek bábuk rakosgathatóva
    }

    public void run() {
        // Ez igazából nem csinál semmit
        player = true;
        selectedClick = null;
        nextClick = null;
    }

    public void topCellClicked() {
        if(state == PLAYING && player && selectedClick != null && nextClick == null) {
            Piece piece = board.getPieceAt(selectedClick.x(), selectedClick.y());
            if(piece.isStart() && board.findIfWins(selectedClick, piece.getState().getHeight()-1, true, new LinkedList<>())) {
                for(int i = 0; i < 6; i++) {
                    if(board[][]) {
                        gameOver();
                    }
                }
            }

        }
        //TODO
    }

    public void bottomCellClicked() {
        //TODO
    }

    public void clicked(int x, int y) {
        switch (state) {
            case IDLE:
                // Itt nem csinálunk semmit
                board.setAllCellsUnselectedAndNonstart();
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
                }
                break;
            case PLAYING:
                // Itt a játék közben van
                int activeRow = board.getActiveRow(player);
                // Ha első kattintás, és az aktív sorban katitntunk
                if (selectedClick == null && y == activeRow) {
                    selectedClick = new Position(x, y);
                    board.setAllCellsUnselectedAndNonstart(); // Minden kiálasztás törlése
                    board.exploreMoves(selectedClick); // Léphető pozíciók kiválasztása
                    board.setStartPosition(selectedClick); // Kezdő pozíció beállítása
                } else if (selectedClick != null && nextClick == null && board.isPositionJumpable(new Position(x, y))) {
                    nextClick = new Position(x, y);
                    if (board.tryToMovePiece(selectedClick, nextClick)) {
                        selectedClick = null;
                        nextClick = null;
                        board.setAllCellsUnselectedAndNonstart(); // Minden kiálasztás törlése
                        player = !player; // játékosváltás
                        mainFrame.switchPlayerLabel();
                    } else {
                        board.setAllCellsUnselectedAndNonstart(); // Minden kiválasztás törlése
                        board.setStartPosition(selectedClick); // Kezdő pozíció beállítása
                        board.exploreMoves(nextClick);
                        nextClick = null;
                    }
                } else if (board.getPieceAt(new Position(x, y)).getState() == CellState.EMPTY) {
                    selectedClick = null;
                    nextClick = null;
                    board.setAllCellsUnselectedAndNonstart(); // Minden kiválasztás törlése
                }
                break;
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
        JFileChooser fileChooser = new JFileChooser(new File("."));
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json")); // Restrict to JSON files

        int returnValue = fileChooser.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Append .json if not already present
            if (!selectedFile.getName().endsWith(".json")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".json");
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
    }

    public void loadGameState() {
        JFileChooser fileChooser = new JFileChooser(new File("."));
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json")); // Restrict to JSON files

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Ensure the selected file is a JSON file
            if (selectedFile.getName().endsWith(".json")) {
                try {
                    Gson gson = new Gson();
                    FileReader fileReader = new FileReader(selectedFile);
                    GameStateData gameStateData = gson.fromJson(fileReader, GameStateData.class);
                    fileReader.close();

                    // Apply the loaded state
                    board.setBoardState(gameStateData.getBoardState());
                    player = gameStateData.isCurrentPlayer();
                    mainFrame.switchPlayerLabel();

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
    }

    public void revertMove() {
    //TODO
    }

    public void gameOver() {
        mainFrame.actionButton.setText("New Game");
        board.setAllCellsUnselectedAndNonstart();
        setState(IDLE);
    }
}