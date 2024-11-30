package gyges;

public record Position(int x, int y) {

    public Position left() {
        return new Position(x - 1, y);
    }

    public Position right() {
        return new Position(x + 1, y);
    }

    public Position up() {
        return new Position(x, y - 1);
    }

    public Position down() {
        return new Position(x, y + 1);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
