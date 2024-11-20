package gyges;

public class Piece3 extends Piece {

    public Piece3(Player player) {
        super(player);
    }

    @Override
    public int getValue() {
        return 3;
    }

    @Override
    public Piece copy() {
        Piece3 copy = new Piece3(this.getPlayer());
        copy.setPosition(this.getPosition());
        return copy;
    }
}