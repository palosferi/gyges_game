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
    JButton themeButton;
    private boolean isDarkTheme = false;

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

        toggleTheme(themeButton);

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

        themeButton = new JButton(String.format("<html><div width='72'>%-10s</div></html>", (isDarkTheme ? "Light Theme" : "Dark Theme")));
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

    public void toggleTheme(JButton themeButton) {
        isDarkTheme = !isDarkTheme;
        game.getBoard().isDarkMode = isDarkTheme;

        // Colors for light and dark mode
        Color backgroundColor = isDarkTheme ? new Color(18, 18, 18) : new Color(245, 245, 245);
        Color gridColor = isDarkTheme ? new Color(56, 56, 56) : new Color(204, 204, 204);
        Color textColor = isDarkTheme ? new Color(255, 255, 255) : new Color(0, 0, 0);

        // Update the theme of components
        updateTheme(backgroundColor, gridColor, textColor);

        // Update the button label based on the theme
        themeButton.setText(String.format("<html><div width='72'>%-10s</div></html>", (isDarkTheme ? "Light Theme" : "Dark Theme")));

        // Update the pop-up dialogs' themes
        updatePopupTheme(backgroundColor, textColor);

        // Repaint the frame to apply changes
        repaint();
    }

    private void updateTheme(Color backgroundColor, Color gridColor, Color textColor) {
        // Set the background color and text color for the entire board and UI components
        controlPanel.setBackground(backgroundColor);
        controlPanel.setForeground(textColor);
        messageArea.setBackground(backgroundColor);
        messageArea.setForeground(textColor);
        messageLabel.setForeground(textColor);
        playerLabel.setForeground(textColor);

        // Set the board table background and foreground colors
        table.setBackground(backgroundColor);
        table.setForeground(textColor);

        // Check if the table has a header and set its background color
        if (table.getTableHeader() != null) {
            table.getTableHeader().setBackground(gridColor);
        }

        // Update the special cells' backgrounds
        topSpecialCell.setBackground(backgroundColor);
        bottomSpecialCell.setBackground(backgroundColor);

        // Update component backgrounds recursively
        updateComponentBackgrounds(getContentPane(), backgroundColor, textColor);

        // Update the piece icons
        updatePiecePics();
    }

    private void updatePopupTheme(Color backgroundColor, Color textColor) {
        // Update the rules dialog
        JDialog rulesDialog = new JDialog();
        rulesDialog.getContentPane().setBackground(backgroundColor);
        for (Component comp : rulesDialog.getContentPane().getComponents()) {
            if (comp instanceof JComponent) {
                comp.setForeground(textColor);
                comp.setBackground(backgroundColor);
            }
        }

        // Update the user guide dialog
        JDialog guideDialog = new JDialog();
        guideDialog.getContentPane().setBackground(backgroundColor);
        for (Component comp : guideDialog.getContentPane().getComponents()) {
            if (comp instanceof JComponent) {
                comp.setForeground(textColor);
                comp.setBackground(backgroundColor);
            }
        }
    }

    private void updateComponentBackgrounds(Component component, Color backgroundColor, Color foregroundColor) {
        component.setBackground(backgroundColor);
        if (component instanceof JComponent) {
            (component).setForeground(foregroundColor);
        }

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                updateComponentBackgrounds(child, backgroundColor, foregroundColor);
            }
        }
    }

    private void updatePiecePics() {
        for (int i = 0; i < CELL_COUNT; i++) {
            for (int j = 0; j < CELL_COUNT; j++) {
                game.getBoard().getPieceAt(i, j).toImage(isDarkTheme);
            }
        }
        table.repaint();
    }

    private void showRulesDialog() {
        JDialog rulesDialog = new JDialog(this, "Rules of Gyges", true);
        rulesDialog.setSize(580, 500);
        rulesDialog.setLocationRelativeTo(this);

        // Create a text area with the rules
        JTextArea rulesText = new JTextArea();
        rulesText.setText("""
        Rules of Gyges:
        
        1. Components
         . A 6×6 board
         . 12 pieces, with 4 of each height (1, 2, and 3)
        2. Setup
         . Each player starts with 2 pieces of each height
         . Players move their pieces in the row closest to them
        3. Gameplay
         . Players alternate turns
         . On each turn, a player may move one piece from the closest non-empty row to them (the "active row")
         . If no pieces in the active row can move, pieces in the next row forward become eligible for movement
         . Pieces move orthogonally (horizontally or vertically) a number of spaces equal to their height
         . Direction changes are allowed within a single move, but a piece may not
           cross the same line between two spaces more than once during the same move
        4. Special Rules for Movement
         . A turn ends when a piece stops on an empty space
         . If a piece's movement ends on an occupied space:
           - Bounce: Continue movement based on the height of the piece you land on. This
             can chain into additional bounces if further occupied spaces are encountered.
           - Replacement: Instead of bouncing, the player may choose to remove the piece they land
             on and place it anywhere on the board, except in the opponent’s active row or behind it
        5. Winning
         . A player wins by ending their movement exactly on the target cell on the opponent’s side of the board
        6. Additional Notes
         . Pieces cannot jump over other pieces
        """);
        setDialogButtons(rulesDialog, rulesText);
    }

    private void showUserGuideDialog() {
        JDialog guideDialog = new JDialog(this, "User Guide", true);
        guideDialog.setSize(580, 500);
        guideDialog.setLocationRelativeTo(this);

        JTextArea guideText = new JTextArea();
        guideText.setText("""
        User Guide:
        
        1. Game Interface Components
          . Game Board
            - 6×6 Grid: The main board for gameplay. Players interact with the grid to move pieces
            - Top Special Cell (North): Click to place a piece in the top row
            - Bottom Special Cell (South): Click to place a piece in the bottom row
            - Pieces can be selected and moved based on the game rules
            - Clicking a piece highlights its potential moves
          . Control Panel
            - This panel provides gameplay options and displays key information
         2. Control Panel Buttons
          . New Game
            - Starts a new game session
            - Resets the board and initializes player turns
          . Save Game
            - Saves the current game state to continue later
          . Load Game
            - Loads a previously saved game state
          . Exit
            - Closes the application
          . Theme Toggle
            - Switch between Dark Mode and Light Mode
         3. Additional Features
          . Player Info
            - Displays the current player's turn (Bottom or Top)
          . Message Area
            - Displays game updates, player actions, and instructions
         4. Gameplay Workflow
          . Setup Phase
            - After starting a new game, arrange your pieces in the row closest to you
            - You can rearrange pieces by clicking them
          . Game Phase
            - Players take turns moving their pieces
            - Special moves, like "bounce" or "replacement," are triggered when landing on occupied spaces
          . Endgame
            - The game ends when a player successfully moves a piece to the special cell close to the opponent
         5. Special Instructions
            - Special Cells: The special cells do not light up even when it is
              possible for the player to press, so pay attention not to miss winning moves
            - Undo Move: During gameplay, click Undo (if available) to revert the last move
         6. Customizing Your Experience
          . Dark Mode and Light Mode
            - Toggle between themes to match your preference
         7. Pro Tips
          . Plan Ahead: Use the height of pieces strategically to maximize movement potential
          . Utilize Special Moves: Consider using the replacement move to reposition pieces advantageously
          . Save Often: Use the save function to protect your progress, especially during long matches
        """);
        setDialogButtons(guideDialog, guideText);

    }

    private void setDialogButtons(JDialog dialog, JTextArea messageArea) {
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        // Apply the current theme
        Color backgroundColor = isDarkTheme ? new Color(18, 18, 18) : new Color(245, 245, 245);
        Color textColor = isDarkTheme ? new Color(255, 255, 255) : new Color(0, 0, 0);
        messageArea.setBackground(backgroundColor);
        messageArea.setForeground(textColor);

        JScrollPane scrollPane = new JScrollPane(messageArea);
        dialog.add(scrollPane);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        closeButton.setBackground(backgroundColor);
        closeButton.setForeground(textColor);
        dialog.add(closeButton, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public void actionButtonClicked() {
        switch (game.getState()) {
            case IDLE:
                actionButton.setText("Start Game");
                game.setState(SETUP);
                game.getBoard().init(); // Init the game
                appendMessage("Set up your pieces! You can change the order of the pieces in the row closest to you by clicking them.");
                table.repaint();
                break;
            case SETUP:
                table.repaint();
                actionButton.setText("Undo move");
                appendMessage("The game has started! Player 1 (bottom) is the first to move.");
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

    public boolean getIsDarkTheme() {
        return isDarkTheme;
    }

    public void setIsDarkTheme(boolean isDarkTheme) {
        this.isDarkTheme = isDarkTheme;
    }

}
