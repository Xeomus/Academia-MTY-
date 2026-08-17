package pacman.strategy;

import pacman.model.Direction;
import pacman.model.Ghost;
import pacman.model.Pacman;
import pacman.model.Position;

import java.util.List;

public class HybridMovementStrategy implements MovementStrategy {

    private static final int TILE_SIZE = 32;
    private static final int SAFE_DISTANCE =
            TILE_SIZE * 6;

    @Override
    public Direction chooseDirection(
            Ghost ghost,
            Pacman pacman,
            List<Direction> validDirections
    ) {

        Position ghostPosition =
                ghost.getPosition();

        Position pacmanPosition =
                pacman.getPosition();

        double currentDistance =
                calculateDistance(
                        ghostPosition,
                        pacmanPosition
                );

        if (currentDistance > SAFE_DISTANCE) {

            return chooseClosestDirection(
                    ghost,
                    pacmanPosition,
                    validDirections
            );
        }

        return chooseFarthestDirection(
                ghost,
                pacmanPosition,
                validDirections
        );
    }

    private Direction chooseClosestDirection(
            Ghost ghost,
            Position target,
            List<Direction> validDirections
    ) {

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

    private Direction chooseFarthestDirection(
            Ghost ghost,
            Position target,
            List<Direction> validDirections
    ) {

        Direction bestDirection =
                validDirections.getFirst();

        double longestDistance = -1;

        for (Direction direction : validDirections) {

            Position nextPosition =
                    ghost.getNextPosition(direction);

            double distance =
                    calculateDistance(
                            nextPosition,
                            target
                    );

            if (distance > longestDistance) {

                longestDistance = distance;
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