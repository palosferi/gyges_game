package gyges;

import javax.swing.*;
import java.awt.*;

import static gyges.GameState.*;

public class MainFrame extends JFrame {

    Game game;
    JTable table;
    final int CELL_SIZE = 64;
    final int CELL_COUNT = 6;

    private GameState state = new GameState(); // = GameState.IDLE;

    public MainFrame() {
        // Set preferred size for the entire window
        //setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(CELL_COUNT * CELL_SIZE + 100, (CELL_COUNT+2) * CELL_SIZE));
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        state.current = IDLE; // Set initial state
        game = new Game(state);
        game.init();

        table = new JTable(game.getBoard());
        table.setRowHeight(CELL_SIZE);
        table.setDefaultRenderer(Object.class, new PieceImageRenderer());
        table.setTableHeader(null);
        table.addMouseListener(new BoardMouseListener(table, game));
        table.setCellSelectionEnabled(true); // Enable cell selection
        table.setRowSelectionAllowed(false); // Disable row selection
        table.setColumnSelectionAllowed(false); // Optional: Disable column selection

        // Wrap the table in a JScrollPane
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(CELL_COUNT * CELL_SIZE, CELL_COUNT * CELL_SIZE));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        initializeComponents(controlPanel);

        add(tableScrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);


        // Call pack at the end to set the correct size
        pack();
        setVisible(true);
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

        actionButton = new JButton("New Game");
        actionButton.addActionListener(e -> actionButtonClicked());
        controlPanel.add(actionButton);
    }
    JButton actionButton;

    public void actionButtonClicked() {
        switch (state.current) {
            case IDLE:
                actionButton.setText("Start Game");
                state.current = SETUP;
                game.init(); // Init the game
                break;
            case SETUP:
                actionButton.setText("End Game");
                state.current = PLAYING;
                game.run(); // Run the game
                break;
            case PLAYING:
                actionButton.setText("New Game");
                state.current = IDLE;
                break;
        }
    }
}
