package gyges;

import com.google.gson.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Game {
    private final MainFrame mainFrame;
    private final Board board = new Board();
    private boolean player = true; // Igaz: Alsó játékos van soron, Hamis: Felső játékos van soron
    private Position selectedClick; // Elős klikk
    private Position nextClick; // Második kattintás
    private GameState state; // = new GameState();

    public Game(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        state = GameState.IDLE;
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

    }

    public void bottomCellClicked() {

    }

    public void clicked(int x, int y) {
        switch (state) {
            case IDLE:
                // Itt nem csinálunk semmit
                board.setAllCellsUnselected();
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
                    board.setAllCellsUnselected();
                }
                break;
            case PLAYING:
                // Itt a játék közben van
                int activeRow = board.getActiveRow(player);
                // Ha első kattintás, és az aktív sorban katitntunk
                if (selectedClick == null && y == activeRow) {
                    selectedClick = new Position(x, y);
                    board.setAllCellsUnselected(); // Minden kiálasztás törlése
                    board.exploreMoves(selectedClick); // Léphető pozíciók kiválasztása
                    board.setStartPosition(selectedClick); // Kezdő pozíció beállítása
                } else if (selectedClick != null && nextClick == null && board.isPositionJumpable(new Position(x, y))) {
                    nextClick = new Position(x, y);
                    if (board.tryToMovePiece(selectedClick, nextClick)) {
                        selectedClick = null;
                        nextClick = null;
                        board.setAllCellsUnselected(); // Minden kiálasztás törlése
                        player = !player; // játékosváltás
                        mainFrame.playerLabel.setText("Player: " + (player? "Bottom" : "Top"));
                    } else {
                        board.setAllCellsUnselected(); // Minden kiálasztás törlése
                        board.setStartPosition(selectedClick); // Kezdő pozíció beállítása
                        board.exploreMoves(nextClick);
                        nextClick = null;
                    }
                } else if (board.getPieceAt(new Position(x, y)).getState() == CellState.EMPTY) {
                    selectedClick = null;
                    nextClick = null;
                    board.setAllCellsUnselected(); // Minden kiálasztás törlése
                }


                    //board.exploreMoves(selectedClick);
                //player = !player; // játékosváltás
                //mainFrame.playerLabel.setText("Player: " + (player? "Bottom" : "Top"));
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

    public void saveGameState(String filePath) {
        Gson gson = new Gson();
        GameStateData gameStateData = new GameStateData(board.getBoardState(), player);

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(gameStateData, writer);
            System.out.println("Game state saved successfully!");
        } catch (IOException e) {
            System.err.println("Failed to save game state: " + e.getMessage());
        }
    }

    public void loadGameState(String filePath) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(filePath)) {
            GameStateData gameStateData = gson.fromJson(reader, GameStateData.class);
            board.setBoardState(gameStateData.getBoardState());
            player = gameStateData.isCurrentPlayer();
            mainFrame.playerLabel.setText("Player: " + (player ? "Bottom" : "Top"));
            System.out.println("Game state loaded successfully!");
        } catch (IOException e) {
            System.err.println("Failed to load game state: " + e.getMessage());
        }
    }
}