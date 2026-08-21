package pacman.strategy;

import pacman.model.Direction;
import pacman.model.Ghost;
import pacman.model.Pacman;
import pacman.model.Position;

import java.util.List;

public class ChaseMovementStrategy implements MovementStrategy {

    @Override
    public Direction chooseDirection(
            Ghost ghost,
            Pacman pacman,
            List<Direction> validDirections
    ) {

        Position target = pacman.getPosition();

        Direction bestDirection =
                validDirections.getFirst();

        double shortestDistance =
                Double.MAX_VALUE;

        for (Direction direction : validDirections) {

            Position nextPosition =
                    ghost.getNextPosition(direction);

            double distance =
                    calculateDistance(
                            nextPosition,
                            target
                    );

            if (distance < shortestDistance) {
                shortestDistance = distance;
                bestDirection = direction;
            }
        }

        return bestDirection;
    }

    private double calculateDistance(
            Position first,
            Position second
    ) {

        int deltaX =
                second.getX() - first.getX();

        int deltaY =
                second.getY() - first.getY();

        return Math.sqrt(
                deltaX * deltaX
                        + deltaY * deltaY
        );
    }
}