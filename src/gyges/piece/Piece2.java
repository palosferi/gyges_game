package gyges.piece;

import gyges.Player;

public class Piece2 extends Piece {

    public Piece2(Player player) {
        super(player, 2);
    }

    @Override
    public Piece copy() {
        Piece2 copy = new Piece2(this.getPlayer());
        copy.setPosition(this.getPosition());
        return copy;
    }
}