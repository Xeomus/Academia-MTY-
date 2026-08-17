package pacman.model;

/*
 * Movable defines the movement behavior
 * that a game object can implement.
 *
 * Any class that implements Movable
 * must provide its own implementation of move().
 *
 * Examples:
 * - Pacman IS Movable
 * - Ghost IS Movable
 * both are Entities and both can move
 * but WALL IS-A Entity but can't move
 * because doesn't implement this interface
 */
public interface Movable {
    public void move();
}
