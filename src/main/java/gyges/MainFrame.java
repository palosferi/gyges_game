package gyges;

import javax.swing.*;
import java.awt.*;

import static gyges.GameState.*;

public class MainFrame extends JFrame {
    Game game;
    JTable table;
    static final int CELL_SIZE = 64;
    static final int CELL_COUNT = 6;
    JButton topSpecialCell;
    JButton bottomSpecialCell;
    JLabel messageLabel  = new JLabel("Welcome to Gyges!");
    JLabel playerLabel = new JLabel();
    JButton actionButton;
    JPanel controlPanel;
    JTextArea messageArea;
    public boolean isDarkTheme = false; //TODO: set to true

    public MainFrame() {
        // Set preferred size for the entire window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(CELL_COUNT * CELL_SIZE + 330, (CELL_COUNT + 2) * CELL_SIZE + 60));
        setResizable(false);
        setLayout(new BorderLayout());

        game = new Game(this);
        game.getBoard().init();

        table = new JTable(game.getBoard());
        initTable(table);

        // Wrap the table in a JScrollPane
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(CELL_COUNT * CELL_SIZE, CELL_COUNT * CELL_SIZE));

        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        messageArea = new JTextArea(5, 30);
        messageArea.setEditable(false); // Make it read-only
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        initControlPanel(controlPanel);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new BorderLayout());

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topSpecialCell = new JButton();
        topSpecialCell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        topSpecialCell.addActionListener(e -> game.topCellClicked());
        topRow.add(topSpecialCell);

        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomSpecialCell = new JButton();
        bottomSpecialCell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        bottomSpecialCell.addActionListener(e -> game.bottomCellClicked());
        bottomRow.add(bottomSpecialCell);

        boardPanel.add(topRow, BorderLayout.NORTH);
        boardPanel.add(tableScrollPane, BorderLayout.CENTER);
        boardPanel.add(bottomRow, BorderLayout.SOUTH);

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);

        setDefaultTheme();

        // Call pack at the end to set the correct size
        pack();
        setVisible(true);
    }

    private void initTable(JTable table) {
        table.setRowHeight(CELL_SIZE);
        table.setDefaultRenderer(Object.class, new PieceImageRenderer());
        table.setTableHeader(null);
        table.addMouseListener(new BoardMouseListener(table, game));
        table.setCellSelectionEnabled(true); // Enable cell selection
        table.setRowSelectionAllowed(false); // Disable row selection
        table.setColumnSelectionAllowed(false); // Optional: Disable column selection
    }

    private void initControlPanel(JPanel controlPanel) {
        controlPanel.add(Box.createVerticalStrut(20)); // Blank space

        controlPanel.add(messageLabel);

        controlPanel.add(Box.createVerticalStrut(20)); // Blank space

        actionButton = new JButton("New Game");
        actionButton.addActionListener(e -> actionButtonClicked());
        controlPanel.add(actionButton);

        controlPanel.add(Box.createVerticalStrut(20)); // Blank space

        JButton saveMenuItem = new JButton("Save Game");
        saveMenuItem.addActionListener(e -> game.saveGameState());
        controlPanel.add(saveMenuItem);

        controlPanel.add(Box.createVerticalStrut(20)); // Blank space

        JButton loadMenuItem = new JButton("Load Game");
        loadMenuItem.addActionListener(e -> game.loadGameState());
        table.revalidate();
        table.repaint();
        controlPanel.add(loadMenuItem);

        controlPanel.add(Box.createVerticalStrut(20)); // Blank space

        updatePlayerLabel(game.getPlayer());
        playerLabel.setPreferredSize(new Dimension(200, 40)); // Fixed width, same height
        playerLabel.setMaximumSize(new Dimension(200, 40)); // Ensure it doesn't expand
        controlPanel.add(playerLabel);

        controlPanel.add(Box.createVerticalStrut(20)); // Blank space

        JScrollPane scrollPane = new JScrollPane(messageArea);
        controlPanel.add(scrollPane, BorderLayout.SOUTH);

        controlPanel.add(Box.createVerticalStrut(20)); // Blank space

        // Create a JPanel to hold the buttons next to each other (for rules and user guide)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Create the "Rules" button
        JButton rulesButton = new JButton("Rules");
        rulesButton.addActionListener(e -> showRulesDialog());
        buttonPanel.add(rulesButton);

        // Create the "User Guide" button
        JButton guideButton = new JButton("User Guide");
        guideButton.addActionListener(e -> showUserGuideDialog());
        buttonPanel.add(guideButton);

        JButton themeButton = new JButton(String.format("<html><div width='72'>%-10s</div></html>", (isDarkTheme ? "Light Theme" : "Dark Theme")));
        themeButton.addActionListener(e -> toggleTheme(themeButton));
        buttonPanel.add(themeButton);

        // Create the "Exit" button
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0)); // Closes the application
        buttonPanel.add(exitButton);

        // Add the button panel to the control panel
        controlPanel.add(buttonPanel);

        // Create a panel for "Made by palosferi" and set it to be aligned to the bottom right
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel madeByLabel = new JLabel("/// made by _palosferi_ ///");
        footerPanel.add(madeByLabel);

        // Add the footerPanel to the bottom of the controlPanel
        controlPanel.add(footerPanel, BorderLayout.SOUTH);
    }

    private void setDefaultTheme() {
        //TODO
    }

    private void toggleTheme(JButton themeButton) {
        isDarkTheme = !isDarkTheme;

        // Define dark and light theme colors
        Color tableColor = isDarkTheme ? Color.BLACK : Color.WHITE;
        Color backgroundColor = isDarkTheme ? Color.BLACK : Color.LIGHT_GRAY;
        Color foregroundColor = isDarkTheme ? Color.WHITE : Color.BLACK;

        topSpecialCell.setBackground(backgroundColor);
        bottomSpecialCell.setBackground(backgroundColor);

        // Apply theme to main frame and control panel
        getContentPane().setBackground(backgroundColor);
        controlPanel.setBackground(backgroundColor);
        controlPanel.setForeground(foregroundColor);

        // Apply theme to other components
        table.setBackground(tableColor);
        table.setForeground(tableColor);

        messageLabel.setBackground(backgroundColor);

        messageArea.setBackground(backgroundColor);
        messageArea.setForeground(foregroundColor);

        playerLabel.setBackground(backgroundColor);
        playerLabel.setForeground(foregroundColor);

        // Change button text
        themeButton.setText(String.format("<html><div width='72'>%-10s</div></html>", (isDarkTheme ? "Light Theme" : "Dark Theme")));
        themeButton.repaint();

        // Repaint to apply changes
        repaint();
    }


    private void showRulesDialog() {
        // Create a dialog
        JDialog rulesDialog = new JDialog(this, "Rules of Gyges", true);
        rulesDialog.setSize(400, 300);
        rulesDialog.setLocationRelativeTo(this); // Center the dialog

        // Create a text area to display the rules
        JTextArea rulesText = new JTextArea();
        rulesText.setText("""
        Rules of Gyges:
        
        1. The board is a 6x6 grid.
        2. Each player takes turns moving pieces.
        3. Pieces move based on their height (1, 2, or 3 steps).
        4. A piece can jump over other pieces.
        5. The goal is to move a piece to the opponent's starting row.
        
        """);
        //TODO: More detailed rules
        rulesText.setEditable(false);
        rulesText.setLineWrap(true);
        rulesText.setWrapStyleWord(true);

        // Add the text area to a scroll pane in case the rules are long
        JScrollPane scrollPane = new JScrollPane(rulesText);
        rulesDialog.add(scrollPane);

        // Add a "Close" button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> rulesDialog.dispose());
        rulesDialog.add(closeButton, BorderLayout.SOUTH);

        // Show the dialog
        rulesDialog.setVisible(true);
    }

    private void showUserGuideDialog() {
        JDialog guideDialog = new JDialog(this, "User Guide", true);
        guideDialog.setSize(400, 300);
        guideDialog.setLocationRelativeTo(this);

        JTextArea guideText = new JTextArea();
        guideText.setText("""
                User Guide:
                
                1. **New Game Button**: Starts a new game. The game is initialized, and you can begin playing.
                2. **Save Game Button**: Saves the current game state, so you can continue later.
                3. **Load Game Button**: Loads a previously saved game state.
                4. **Player Info**: Displays the current player (Top or Bottom).
                5. **Game Board**: Click on the cells to make your move. The goal is to reach the opponent's starting row.
                6. **Special Cells**: Located at the top and bottom of the board. Clicking them places a piece in the respective row.
                """);
        //TODO: more detailed text to explain how the buttons work
        guideText.setEditable(false);
        guideText.setLineWrap(true);
        guideText.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(guideText);
        guideDialog.add(scrollPane);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> guideDialog.dispose());
        guideDialog.add(closeButton, BorderLayout.SOUTH);

        guideDialog.setVisible(true);
    }

    public void actionButtonClicked() {
        switch (game.getState()) {
            case IDLE:
                actionButton.setText("Start Game");
                game.setState(SETUP);
                game.getBoard().init(); // Init the game
                break;
            case SETUP:
                table.repaint();
                actionButton.setText("Undo move");
                game.run(); // Run the game
                break;
            case PLAYING:
                game.undoMove();
                break;
        }
    }
    public void appendMessage(String message) {
        messageArea.append(message + "\n"); // Append the message and move to a new line
        messageArea.setCaretPosition(messageArea.getDocument().getLength()); // Scroll to the bottom
    }

    public void updatePlayerLabel(boolean isBottomPlayer) {
        String playerText = String.format("<html><div width='200'>Current Player: %-10s</div></html>", (isBottomPlayer ? "Bottom" : "Top"));
        playerLabel.setText(playerText);
    }

}
