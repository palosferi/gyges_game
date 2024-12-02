package gyges;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardMouseListener extends MouseAdapter {

    private final JTable table;
    private final Game game;

    public BoardMouseListener(JTable table, Game game) {
        this.table = table;
        this.game = game;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int y = table.rowAtPoint(e.getPoint());
        int x = table.columnAtPoint(e.getPoint());

        if (x != -1 && y != -1 && e.getButton() == MouseEvent.BUTTON1) {
            game.leftClicked(x, y);
        } else if(x != -1 && y != -1 && e.getButton() == MouseEvent.BUTTON3 && game.getState() == GameState.PLAYING && game.getBoard().getPieceAt(x, y).getState().getHeight()!= 0) {
            game.rightClicked(x, y);
        }
        table.repaint();
    }
}
