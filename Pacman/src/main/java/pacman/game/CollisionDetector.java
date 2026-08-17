package pacman.game;

import pacman.model.Collidable;
import pacman.model.Position;

/*
 * CollisionDetector is responsible for detecting collisions
 * between objects that implement Collidable.
 *
 * It does not depend on specific classes such as Pacman,
 * Ghost or Wall. It works with the Collidable interface,
 * allowing different types of game objects to be checked
 * using the same method.
 *
 * The class is final because it is not intended
 * to be extended.
 */
public final class CollisionDetector {

    /*
     * Private constructor prevents objects of this class
     * from being created.
     *
     * CollisionDetector does not need instances because
     * its behavior is provided through static methods.
     */
    private CollisionDetector() {
    }

    /*
     * Checks whether two Collidable objects overlap.
     *
     * Because the parameters are Collidable, this method
     * can receive Pacman, Ghost, Wall or any future class
     * that implements the Collidable interface.
     *
     * The collision is calculated using the position,
     * width and height of both objects.
     */
    public static boolean isColliding(Collidable first, Collidable second) {
        Position firstPosition = first.getPosition();
        Position secondPosition = second.getPosition();
        return firstPosition.getX()
                < secondPosition.getX() + second.getWidth()

                && firstPosition.getX() + first.getWidth()
                > secondPosition.getX()

                && firstPosition.getY()
                < secondPosition.getY() + second.getHeight()

                && firstPosition.getY() + first.getHeight()
                > secondPosition.getY();
    }
}
