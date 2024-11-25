package gyges;

import javax.swing.*;
import java.util.Objects;

public class Piece {
    private final CellState value;

    public Piece(int value) {
        this.value = CellState.fromInt(value);
    }

    public CellState getValue() {
        return value;
    }

    public ImageIcon toImage() {
        return value.toIcon();
    }

    public String toString() {
        return value + "";
    }
}
