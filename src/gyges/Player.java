package gyges;

import gyges.enums.PlayerType;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Piece> pieces;
    private PlayerType type;

    public Player(PlayerType type) {
        this.type = type;
    }

    public PlayerType getType() {
        return type;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(List<Piece> pieces) {
        this.pieces = new ArrayList<>(pieces); // Deep copy if needed
    }

}
