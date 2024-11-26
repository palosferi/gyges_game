package gyges;

import javax.swing.*;

public class Piece {
    private CellState value;

    public Piece(int value) {
        this.value = CellState.fromInt(value);
    }

    public CellState getState() {
        return value;
    }

    public ImageIcon toImage() {
        return value.toIcon();
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
                case SELECTED:
                    value = CellState.EMPTY;
                    break;
                case SELECTED_ONE:
                    value = CellState.ONE;
                    break;
                case SELECTED_TWO:
                    value = CellState.TWO;
                    break;
                case SELECTED_THREE:
                    value = CellState.THREE;
                    break;
            }
        }
    }
}
