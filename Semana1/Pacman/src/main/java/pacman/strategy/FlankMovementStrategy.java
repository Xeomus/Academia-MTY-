package pacman.strategy;

import pacman.model.Direction;
import pacman.model.Ghost;
import pacman.model.Pacman;
import pacman.model.Position;

import java.util.List;

public class FlankMovementStrategy implements MovementStrategy {

    private static final int TILE_SIZE = 32;
    private static final int FORWARD_TILES = 2;
    private static final int SIDE_TILES = 2;

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

        int forwardDistance =
                TILE_SIZE * FORWARD_TILES;

        int sideDistance =
                TILE_SIZE * SIDE_TILES;

        switch (pacman.getDirection()) {

            case UP:
                y -= forwardDistance;
                x += sideDistance;
                break;

            case DOWN:
                y += forwardDistance;
                x -= sideDistance;
                break;

            case LEFT:
                x -= forwardDistance;
                y -= sideDistance;
                break;

            case RIGHT:
                x += forwardDistance;
                y += sideDistance;
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