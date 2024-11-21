package gyges.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gyges.Game;

import javax.swing.*;
import java.io.*;

public class MenuBar extends JMenuBar {
    public MenuBar(JFrame frame, Game game, BoardPanel boardPanel) {
        JMenu fileMenu = new JMenu("File");
        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem saveGameItem = new JMenuItem("Save Game");
        JMenuItem loadGameItem = new JMenuItem("Load Game");
        JMenuItem exitItem = new JMenuItem("Exit");

        newGameItem.addActionListener(e -> {
            game.startNewGame();
            boardPanel.updateBoardDisplay();
        });
        saveGameItem.addActionListener(e -> saveGame(game));
        loadGameItem.addActionListener(e -> loadGame(game, boardPanel));
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(newGameItem);
        fileMenu.add(saveGameItem);
        fileMenu.add(loadGameItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        add(fileMenu);
    }

    private void saveGame(Game game) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game");

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                writer.write(gson.toJson(game));
                JOptionPane.showMessageDialog(null, "Game saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadGame(Game game, BoardPanel boardPanel) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Game");

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try (FileReader reader = new FileReader(fileChooser.getSelectedFile())) {
                Gson gson = new Gson();
                Game loadedGame = gson.fromJson(reader, Game.class);

                // Use the copyFrom method to update the current game instance
                game.copyFrom(loadedGame);

                boardPanel.updateBoardDisplay();
                JOptionPane.showMessageDialog(null, "Game loaded successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error loading game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
