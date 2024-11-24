package gyges.piece;

import gyges.Player;

public class Piece3 extends Piece {

    public Piece3(Player player) {
        super(player, 3);
    }

    @Override
    public Piece copy() {
        Piece3 copy = new Piece3(this.getPlayer());
        copy.setPosition(this.getPosition());
        return copy;
    }
}