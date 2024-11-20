package gyges;

public class Piece1 extends Piece {

    public Piece1(Player player) {
        super(player);
    }

    @Override
    public int getValue() {
        return 1;
    }

    @Override
    public Piece copy() {
        Piece1 copy = new Piece1(this.getPlayer());
        copy.setPosition(this.getPosition());
        return copy;
    }
}