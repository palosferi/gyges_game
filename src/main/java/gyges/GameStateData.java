package gyges;

import java.util.Deque;
import java.util.List;

public record GameStateData(List<List<Integer>> boardState, boolean currentPlayer,
                            Deque<Pair<Position, Position>> moveHistory, boolean isDarkMode) {
}