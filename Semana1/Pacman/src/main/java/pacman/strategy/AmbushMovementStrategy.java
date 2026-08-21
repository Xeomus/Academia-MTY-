package pacman.strategy;

import pacman.model.Direction;
import pacman.model.Ghost;
import pacman.model.Pacman;
import pacman.model.Position;

import java.util.List;

public class AmbushMovementStrategy implements MovementStrategy {

    private static final int TILE_SIZE = 32;
    private static final int LOOK_AHEAD_TILES = 4;

    @Override
    public Direction chooseDirection(
            Ghost ghost,
            Pacman pacman,
            List<Direction> validDirections
    ) {

        Position target = calculateTarget(pacman);

        return chooseClosestDirection(
                ghost,
                target,
                validDirections
        );
    }

    private Position calculateTarget(Pacman pacman) {

        Position position = pacman.getPosition();

        int x = position.getX();
        int y = position.getY();

        int distance = TILE_SIZE * LOOK_AHEAD_TILES;

        switch (pacman.getDirection()) {
            case UP:
                y -= distance;
                break;

            case DOWN:
                y += distance;
                break;

            case LEFT:
                x -= distance;
                break;

            case RIGHT:
                x += distance;
                break;
        }

        return new Position(x, y);
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