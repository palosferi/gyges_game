package gyges;

public record Position(int x, int y) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position(int x1, int y1))) return false;
        return this.x == x1 && this.y == y1;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

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
}
