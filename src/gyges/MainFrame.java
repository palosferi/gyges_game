package gyges;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {
    private JLabel messageLabel;
    private JComboBox<Integer> pieceTypeComboBox;
    static Game game = new Game();
    static JTable table = new JTable(game.getBoard());

    public MainFrame() {
        // Set preferred size for the entire window
        setPreferredSize(new Dimension(6 * 64 + 150, 8 * 64));

        table.setRowHeight(64);
        table.setDefaultRenderer(Object.class, new PieceImageRenderer());
        table.addMouseListener(new BoardMouseListener());

        // Wrap the table in a JScrollPane
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(6 * 64, 6 * 64));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        initializeComponents(controlPanel);

        setLayout(new BorderLayout());
        add(tableScrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);

        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Call pack at the end to set the correct size
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private static class BoardMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int r = table.rowAtPoint(e.getPoint());
            int c = table.columnAtPoint(e.getPoint());

            if (game.isOver()) {
                table.setEnabled(false);
            }
        }
    }

    private void initializeComponents(JPanel controlPanel) {
        messageLabel = new JLabel("Welcome to Gyges!");
        controlPanel.add(messageLabel);

        JButton startGameButton = new JButton("New Game");
        startGameButton.addActionListener(e -> newGame());
        controlPanel.add(startGameButton);
    }

    public void newGame() {
        game = new Game();
        table.setModel(game.getBoard()); // Refresh the table with the new game's board
    }
}
