package pacman.model;

/*
 * Collidable defines the behavior required for an object
 * to participate in collision detection.
 *
 * Any class that implements Collidable must provide:
 * - its current Position
 * - its width
 * - its height
 *
 * Examples:
 * - Pacman IS Collidable
 * - Ghost IS Collidable
 * - Wall IS Collidable
 *
 * This allows the collision system to work with different
 * types of objects through the same interface.
 *
 * Collidable a (Pacman), Collidable b (Ghost/Food/Wall)
 */
public interface Collidable {
    Position getPosition();
    int getWidth();
    int getHeight();
}
