package pacman.model;
/*
 * Direction represents the fixed set of movement
 * directions available in the game.
 *
 * An enum is used because movement direction can only
 * have one of a predefined set of valid values:
 * UP, DOWN, LEFT or RIGHT.
 *
 * Using an enum provides type safety and avoids using
 * arbitrary String values to represent directions.
 */
public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public Direction opposite() {

        /*
         * Returns the opposite direction.
         *
         * This is mainly used by the ghost movement logic
         * to identify and avoid unnecessary reverse movements.
         *
         * Examples:
         * UP    -> DOWN
         * LEFT  -> RIGHT
         */
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}
