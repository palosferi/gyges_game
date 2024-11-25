package gyges;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Piece {
    private final int value;

    public Piece(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public ImageIcon toImage() {
        return switch (value) {
            case 1 -> new ImageIcon(Objects.requireNonNull(getClass().getResource("Piece1.png")));
            case 2 -> new ImageIcon(Objects.requireNonNull(getClass().getResource("Piece2.png")));
            case 3 -> new ImageIcon(Objects.requireNonNull(getClass().getResource("Piece3.png")));
            default -> null;
        };
    }
}
