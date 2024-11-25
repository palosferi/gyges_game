package gyges;

import java.awt.*;

public class Piece {
    private final int value;

    public Piece(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String toImage() {
        return switch (value) {
            case 1 -> "Piece1.png";
            case 2 -> "Piece2.png";
            case 3 -> "Piece3.png";
            default -> "";
        };
    }
}
