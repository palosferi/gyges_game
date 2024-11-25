package gyges;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {

    Game game = new Game();
    JTable table = new JTable(game.getBoard());
    final int CELL_SIZE = 64;
    final int CELL_COUNT = 6;

    public MainFrame() {
        // Set preferred size for the entire window
        setPreferredSize(new Dimension(CELL_COUNT * CELL_SIZE + 100, (CELL_COUNT+2) * CELL_SIZE));

        table.setRowHeight(CELL_SIZE);
        table.setDefaultRenderer(Object.class, new PieceImageRenderer());
        table.addMouseListener(new BoardMouseListener());

        // Wrap the table in a JScrollPane
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(CELL_COUNT * CELL_SIZE, CELL_COUNT * CELL_SIZE));

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

    private class BoardMouseListener extends MouseAdapter {
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
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20)); // Full-width line
        controlPanel.add(separator);

        JLabel messageLabel = new JLabel("Welcome to Gyges!");
        controlPanel.add(messageLabel);

        JSeparator separator2 = new JSeparator(SwingConstants.HORIZONTAL);
        separator2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20)); // Full-width line
        controlPanel.add(separator2);

        JButton startGameButton = new JButton("New Game");
        startGameButton.addActionListener(e -> newGame());
        controlPanel.add(startGameButton);
    }

    public void newGame() {
        game = new Game();
        table.setModel(game.getBoard()); // Refresh the table with the new game's board
    }
}
