package gyges.piece;

import gyges.Player;

public class Piece1 extends Piece {

    public Piece1(Player player) {
        super(player, 1);
    }

    @Override
    public Piece copy() {
        Piece1 copy = new Piece1(this.getPlayer());
        copy.setPosition(this.getPosition());
        return copy;
    }
}