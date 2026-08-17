package pacman.model;

/*
 * Position represents the coordinates of an entity
 * inside the game board.
 *
 * Position is an immutable class:
 * once a Position object is created,
 * its x and y values cannot be changed.
 *
 * final prevents this class from being extended.
 */
public final class Position {

    /*
     * The coordinates are final, so they cannot
     * be reassigned after the object is constructed.
     *
     * They are also private, so they can only
     * be accessed through the class methods.
     */
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

}
