package gyges;

import javax.swing.*;
import java.util.Objects;

public enum CellState {
    EMPTY,          // mod 4 == 0
    ONE,            // mod 4 == 1
    TWO,            // mod 4 == 2
    THREE,          // mod 4 == 3
    SELECTED,       // mod 4 == 0
    SELECTED_ONE,   // mod 4 == 1
    SELECTED_TWO,   // mod 4 == 2
    SELECTED_THREE, // mod 4 == 3
    START_EMPTY,    // mod 4 == 0
    START_ONE,      // mod 4 == 1
    START_TWO,      // mod 4 == 2
    START_THREE;    // mod 4 == 3

    public static final int COUNT = 4;
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
            case START_EMPTY -> 8;
            case START_ONE -> 9;
            case START_TWO -> 10;
            case START_THREE -> 11;
        };
    }
    public int getHeight() {
    return getValue() % COUNT;
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
            case 8 -> START_EMPTY;
            case 9 -> START_ONE;
            case 10 -> START_TWO;
            case 11 -> START_THREE;
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
            case START_EMPTY -> null; //new ImageIcon(Objects.requireNonNull(getClass().getResource("/start_p1.png")));
            case START_ONE ->  new ImageIcon(Objects.requireNonNull(getClass().getResource("/start_p1.png")));
            case START_TWO ->  new ImageIcon(Objects.requireNonNull(getClass().getResource("/start_p2.png")));
            case START_THREE ->  new ImageIcon(Objects.requireNonNull(getClass().getResource("/start_p3.png")));
        };
    }

}
