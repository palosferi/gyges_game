package gyges.gui;

import javax.swing.*;

public class GameMenuBar extends JMenuBar {

    public GameMenuBar(MainFrame mainFrame) {
        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> new MainFrame());
        gameMenu.add(newGameItem);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        gameMenu.add(exitItem);

        add(gameMenu);
    }
}