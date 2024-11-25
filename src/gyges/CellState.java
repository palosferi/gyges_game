package gyges;

import javax.swing.*;
import java.util.Objects;

public enum CellState {
    EMPTY,
    ONE,
    TWO,
    THREE,
    SELECTED,
    SELECTED_ONE,
    SELECTED_TWO,
    SELECTED_THREE;

    // Getter for the integer value
    public int getValue() {
        return switch (this) {
            case EMPTY -> 0;
            case ONE -> 1;
            case TWO -> 2;
            case THREE -> 3;
            case SELECTED -> 4;
            case SELECTED_ONE -> 5;
            case SELECTED_TWO -> 6;
            case SELECTED_THREE -> 7;
        };
    }

    // Method to convert an int to an enum
    public static CellState fromInt(int value) {
        return switch (value) {
            case 0 -> EMPTY;
            case 1 -> ONE;
            case 2 -> TWO;
            case 3 -> THREE;
            case 4 -> SELECTED;
            case 5 -> SELECTED_ONE;
            case 6 -> SELECTED_TWO;
            case 7 -> SELECTED_THREE;
            default -> throw new IllegalArgumentException("Invalid value: " + value);
        };
    }
    public ImageIcon toIcon() {
        return switch (this) {
            case EMPTY -> null;
            case ONE ->  new ImageIcon(Objects.requireNonNull(getClass().getResource("/p1.png")));
            case TWO ->  new ImageIcon(Objects.requireNonNull(getClass().getResource("/p2.png")));
            case THREE ->  new ImageIcon(Objects.requireNonNull(getClass().getResource("/p3.png")));
            case SELECTED -> new ImageIcon(Objects.requireNonNull(getClass().getResource("/sel.png")));
            case SELECTED_ONE ->  new ImageIcon(Objects.requireNonNull(getClass().getResource("/sel_p1.png")));
            case SELECTED_TWO ->  new ImageIcon(Objects.requireNonNull(getClass().getResource("/sel_p2.png")));
            case SELECTED_THREE ->  new ImageIcon(Objects.requireNonNull(getClass().getResource("/sel_p3.png")));
        };
    }
}
