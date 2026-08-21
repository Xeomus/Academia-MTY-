package pacman.strategy;

import pacman.model.Direction;
import pacman.model.Ghost;
import pacman.model.Pacman;

import java.util.List;
import java.util.Random;

public class RandomMovementStrategy implements MovementStrategy {

    private final Random random = new Random();

    @Override
    public Direction chooseDirection(
            Ghost ghost,
            Pacman pacman,
            List<Direction> validDirections
    ) {

        int index = random.nextInt(
                validDirections.size()
        );

        return validDirections.get(index);
    }
}