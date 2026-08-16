package pacman.strategy;

import pacman.model.Direction;
import pacman.model.Ghost;
import pacman.model.Position;

public interface MovementStrategy {

    Direction chooseDirection(
            Ghost ghost,
            Position target
    );
}