package gyges;

import javax.swing.*;

public class Piece {
    private CellState value;

    public Piece(int value) {
        this.value = CellState.fromInt(value);
    }

    public Piece() {
        this.value = CellState.EMPTY;
    }

    public CellState getState() {
        return value;
    }

    public int getValue() {
        return value.getValue();
    }

    public ImageIcon toImage(boolean isDarkMode) {
        return value.toIcon(isDarkMode);
    }

    public String toString() {
        return value + "";
    }

    public void setSelected(boolean isSelected) {
        if (isSelected) {
            switch (value) {
                case EMPTY:
                    value = CellState.SELECTED;
                    break;
                case ONE:
                    value = CellState.SELECTED_ONE;
                    break;
                case TWO:
                    value = CellState.SELECTED_TWO;
                    break;
                case THREE:
                    value = CellState.SELECTED_THREE;
                    break;
            }
        } else {
            switch (value) {
                case SELECTED, START_EMPTY:
                    value = CellState.EMPTY;
                    break;
                case SELECTED_ONE, START_ONE:
                    value = CellState.ONE;
                    break;
                case SELECTED_TWO, START_TWO:
                    value = CellState.TWO;
                    break;
                case SELECTED_THREE, START_THREE:
                    value = CellState.THREE;
                    break;
            }
        }
    }

    public void setStart(boolean b) {
        if (b) {
            switch (value) {
                case EMPTY:
                    value = CellState.START_EMPTY;
                    break;
                case ONE:
                    value = CellState.START_ONE;
                    break;
                case TWO:
                    value = CellState.START_TWO;
                    break;
                case THREE:
                    value = CellState.START_THREE;
                    break;
            }
        } else {
            switch (value) {
                case START_EMPTY:
                    value = CellState.EMPTY;
                    break;
                case START_ONE:
                    value = CellState.ONE;
                    break;
                case START_TWO:
                    value = CellState.TWO;
                    break;
                case START_THREE:
                    value = CellState.THREE;
                    break;
            }
        }
    }
    public boolean isStart() {
        return value.getValue() >= CellState.START_ONE.getValue() && value.getValue() <= CellState.START_THREE.getValue();
    }
}
