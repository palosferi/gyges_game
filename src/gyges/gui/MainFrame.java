package gyges.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private GameController controller;
    private BoardPanel boardPanel;
    private ControlPanel controlPanel;

    public MainFrame() {
        controller = new GameController(this);
        boardPanel = new BoardPanel(this);
        controlPanel = new ControlPanel(this);

        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public GameController getController() {
        return controller;
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }
}