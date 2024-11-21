package gyges.gui;

import gyges.Game;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private JComboBox<String> pieceSelector;

    public ControlPanel(Game game, BoardPanel boardPanel) {
        setLayout(new FlowLayout());

        pieceSelector = new JComboBox<>(new String[]{"1", "2", "3"});
        add(new JLabel("Select piece type:"));
        add(pieceSelector);

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> {
            game.startNewGame();
            boardPanel.updateBoardDisplay();
        });
        add(newGameButton);
    }
}
