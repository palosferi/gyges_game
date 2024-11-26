package gyges;

import javax.swing.*;
import java.awt.*;

import static gyges.GameState.*;

public class MainFrame extends JFrame {

    Game game;
    JTable table;
    final int CELL_SIZE = 64;
    final int CELL_COUNT = 6;


    JLabel playerLabel;
    JButton actionButton;

    //private GameState state = new GameState(); // = GameState.IDLE;

    public MainFrame() {
        // Set preferred size for the entire window
        //setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(CELL_COUNT * CELL_SIZE + 120, (CELL_COUNT + 2) * CELL_SIZE + 60));
        setLayout(new BorderLayout());

        //setLocationRelativeTo(null);


        game = new Game(this);
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

        // ----------------------------------------------------------------

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new BorderLayout());

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        topSpecialCell.setRowHeight(CELL_SIZE);
//        topSpecialCell.setDefaultRenderer(Object.class, new PieceImageRenderer());
//        topSpecialCell.setTableHeader(null);
//        topSpecialCell.addMouseListener(new BoardMouseListener(table, topSpecialCell, bottomSpecialCell, game));
//        topSpecialCell.setCellSelectionEnabled(true); // Enable cell selection
//        topSpecialCell.setRowSelectionAllowed(false); // Disable row selection
//        topSpecialCell.setColumnSelectionAllowed(false); // Optional: Disable column selection
        JButton topSpecialCell = new JButton();
        topSpecialCell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        topSpecialCell.setBackground(Color.WHITE);
        topSpecialCell.addActionListener(e -> topSpecialCellClicked());
        topRow.add(topSpecialCell);

        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        bottomSpecialCell.setRowHeight(CELL_SIZE);
//        bottomSpecialCell.setDefaultRenderer(Object.class, new PieceImageRenderer());
//        bottomSpecialCell.setTableHeader(null);
//        bottomSpecialCell.addMouseListener(new BoardMouseListener(table, topSpecialCell, bottomSpecialCell, game));
//        bottomSpecialCell.setCellSelectionEnabled(true); // Enable cell selection
//        bottomSpecialCell.setRowSelectionAllowed(false); // Disable row selection
//        bottomSpecialCell.setColumnSelectionAllowed(false); // Optional: Disable column selection
        JButton bottomSpecialCell = new JButton();
        bottomSpecialCell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        bottomSpecialCell.setBackground(Color.WHITE);
        bottomSpecialCell.addActionListener(e -> bottomSpecialCellClicked());
        bottomRow.add(bottomSpecialCell);

        boardPanel.add(topRow, BorderLayout.NORTH);
        boardPanel.add(tableScrollPane, BorderLayout.CENTER);
        boardPanel.add(bottomRow, BorderLayout.SOUTH);

//        actionButton = new JButton("New Game");
//        actionButton.addActionListener(e -> actionButtonClicked());

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);

        // Call pack at the end to set the correct size
        pack();
        setVisible(true);
    }

    private void bottomSpecialCellClicked() {
        game.bottomCellClicked(); // Place a piece in the bottom cell
    }

    private void topSpecialCellClicked() {
        game.topCellClicked(); // Place a piece in the top cell
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

        JSeparator separator3 = new JSeparator(SwingConstants.HORIZONTAL);
        separator3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20)); // Full-width line
        controlPanel.add(separator3);

        playerLabel = new JLabel("Player: " + (game.getPlayer() ? "Bottom" : "Top"));
        controlPanel.add(playerLabel);
    }


    public void actionButtonClicked() {
        switch (game.getState()) {
            case IDLE:
                actionButton.setText("Start Game");
                game.setState(SETUP);
                game.init(); // Init the game
                break;
            case SETUP:
                actionButton.setText("End Game");
                game.setState(PLAYING);
                game.run(); // Run the game
                break;
            case PLAYING:
                actionButton.setText("New Game");
                game.setState(IDLE);
                break;
        }
    }
}
