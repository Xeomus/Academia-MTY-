package pacman.model;

/*
* Food HAS-A Position
* Food IS-A Entity
* */
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
