package pacman.model;

import pacman.strategy.MovementStrategy;

import java.util.List;

/*
* Ghost IS-A Entity
* Ghost IS Movable
* Ghost IS Collidable
* Ghost HAS-A MovementStrategy (composition, delegation)
* Ghost doesn't choose a direction, MovementStrategy does
*
* */
public class Ghost extends Entity implements Movable, Collidable {

    private final GhostType type;
    private static final int SPEED = 8;

    private Direction direction;
    private MovementStrategy movementStrategy;

    private final int width;
    private final int height;

    public Ghost(Position position, int width, int height, GhostType type ,MovementStrategy movementStrategy){
        super(position);
        this.width = width;
        this.height = height;
        this.type = type;
        this.movementStrategy = movementStrategy;
        this.direction = Direction.UP;
    }

    public GhostType getType() {
        return type;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public MovementStrategy getMovementStrategy() {
        return movementStrategy;
    }

    public void setMovementStrategy(MovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    public void updateDirection(
            Pacman pacman,
            List<Direction> validDirections
    ) {

        direction = movementStrategy.chooseDirection(
                this,
                pacman,
                validDirections
        );
    }

    public Position getNextPosition(Direction direction) {

        Position currentPosition = getPosition();

        int x = currentPosition.getX();
        int y = currentPosition.getY();

        switch (direction) {

            case UP:
                y -= SPEED;
                break;

            case DOWN:
                y += SPEED;
                break;

            case LEFT:
                x -= SPEED;
                break;

            case RIGHT:
                x += SPEED;
                break;
        }

        return new Position(x, y);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void move() {
        setPosition(getNextPosition(direction));
    }

    public void moveTo(Position position) {
        setPosition(position);
    }
}
