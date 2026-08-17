package pacman.strategy;

import pacman.model.Direction;
import pacman.model.Ghost;
import pacman.model.Pacman;

import java.util.List;

public interface MovementStrategy {

    Direction chooseDirection(
            Ghost ghost,
            Pacman pacman,
            List<Direction> validDirections
    );
}