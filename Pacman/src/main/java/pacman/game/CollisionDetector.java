package pacman.game;

import pacman.model.Collidable;
import pacman.model.Position;

public final class CollisionDetector {
    private CollisionDetector() {
    }
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
