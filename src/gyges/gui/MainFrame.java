package gyges.gui;

import gyges.Game;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private Game game;
    private BoardPanel boardPanel;
    private ControlPanel controlPanel;

    public MainFrame(Game game) {
        super("Gyges Board Game");
        this.game = game;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600, 700);

        // Initialize components
        boardPanel = new BoardPanel(game);
        controlPanel = new ControlPanel(game, boardPanel);

        // Add components to the frame
        setLayout(new BorderLayout());
        setJMenuBar(new MenuBar(this, game, boardPanel));
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
