package gyges.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private GameController controller;
    private BoardPanel boardPanel;
    private ControlPanel controlPanel;

    public MainFrame() {
        controller = new GameController();
        boardPanel = new BoardPanel(controller);
        controlPanel = new ControlPanel(controller);

        controller.setBoardPanel(boardPanel);
        controller.setControlPanel(controlPanel);

        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}