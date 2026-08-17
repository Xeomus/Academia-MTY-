package pacman.model;

/*
 * Food represents a pellet that Pacman can eat.
 *
 * Food HAS-A Position.
 *
 * Food is not an Entity because it does not need
 * common Entity behaviors such as movement or
 * changing its position.
 *
 * Food is final because it is not intended
 * to be extended.
 */
public final class Food {

    private final Position position;
    private final int points;

    public Food(Position position, int points) {
        this.position = position;
        this.points = points;
    }

    public Position getPosition() {
        return position;
    }

    public int getPoints() {
        return points;
    }
}
