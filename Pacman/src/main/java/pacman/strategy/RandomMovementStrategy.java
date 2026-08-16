package pacman.strategy;

import pacman.model.Direction;
import pacman.model.Ghost;

import java.util.Random;

public class RandomMovementStrategy implements MovementStrategy {

    private final Random random = new Random();

    private static final Direction[] DIRECTIONS = {
            Direction.UP,
            Direction.DOWN,
            Direction.LEFT,
            Direction.RIGHT,
    };

    @Override
    public Direction chooseDirection(Ghost ghost) {
        int index = random.nextInt(DIRECTIONS.length);
        return DIRECTIONS[index];
    }

}
