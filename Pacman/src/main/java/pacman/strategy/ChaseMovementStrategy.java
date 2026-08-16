package pacman.strategy;

import pacman.model.Direction;
import pacman.model.Ghost;
import pacman.model.Position;

public class ChaseMovementStrategy implements MovementStrategy {

    @Override
    public Direction chooseDirection(
            Ghost ghost,
            Position target
    ) {

        Position ghostPosition = ghost.getPosition();

        int horizontalDistance =
                target.getX() - ghostPosition.getX();

        int verticalDistance =
                target.getY() - ghostPosition.getY();

        if (Math.abs(horizontalDistance)
                > Math.abs(verticalDistance)) {

            return horizontalDistance > 0
                    ? Direction.RIGHT
                    : Direction.LEFT;
        }

        return verticalDistance > 0
                ? Direction.DOWN
                : Direction.UP;
    }
}