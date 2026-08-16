package pacman.strategy;

import pacman.model.Direction;
import pacman.model.Ghost;

public interface MovementStrategy {
    Direction chooseDirection(Ghost ghost);
}
