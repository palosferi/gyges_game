package gyges.gui;

import gyges.gui.GameController;
import gyges.enums.GamePhase;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private MainFrame mainFrame;
    private JLabel messageLabel;
    private JComboBox<Integer> pieceTypeComboBox;

    public ControlPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
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
        startGameButton.addActionListener(e -> {
            if (mainFrame.getController().getGame().getCurrentPhase() == GamePhase.SETUP) {
                mainFrame.getController().startSetupPhase();
            }
        });
        add(startGameButton);
    }

    public int getSelectedPieceType() {
        return (Integer) pieceTypeComboBox.getSelectedItem();
    }

    public void showMessage(String message) {
        messageLabel.setText(message);
    }

    public void updateDisplay() {
        // Update the display based on the current game state
        GamePhase currentPhase = mainFrame.getController().getGame().getCurrentPhase();

        if (currentPhase == GamePhase.SETUP) {
            showMessage("Setup Phase: Place your pieces.");
            pieceTypeComboBox.setEnabled(true); // Enable piece selection during setup
        } else if (currentPhase == GamePhase.PLAY) {
            showMessage("Player " + mainFrame.getController().getGame().getCurrentPlayer().getType() + "'s turn.");
            pieceTypeComboBox.setEnabled(false); // Disable piece selection during play
        } else {
            showMessage("Game Over! Winner: Player " + mainFrame.getController().getGame().getWinner());
        }
    }
}
