package gyges;

import gyges.enums.PlayerType;

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
}
