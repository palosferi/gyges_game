package gyges.gui;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private final GameController controller;
    private JLabel messageLabel;
    private JComboBox<Integer> pieceTypeComboBox;

    public ControlPanel(GameController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(200, 400));
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        messageLabel = new JLabel("Welcome to Gyges!");
        add(messageLabel);

        pieceTypeComboBox = new JComboBox<>(new Integer[]{1, 2, 3});
        add(new JLabel("Select piece type:"));
        add(pieceTypeComboBox);

        JButton startGameButton = new JButton("Start Game");
        startGameButton.addActionListener(e -> controller.startSetupPhase());
        add(startGameButton);
    }

    public int getSelectedPieceType() {
        return (Integer) pieceTypeComboBox.getSelectedItem();
    }

    public void showMessage(String message) {
        messageLabel.setText(message);
    }

    public void updateDisplay() {
        // Update any necessary display elements based on the game state
    }
}