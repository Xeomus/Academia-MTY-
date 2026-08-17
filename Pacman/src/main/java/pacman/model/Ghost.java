package pacman.model;

import pacman.strategy.MovementStrategy;

import java.util.List;

/*
 * Ghost IS-A Entity through inheritance.
 *
 * Ghost implements:
 * - Movable
 * - Collidable
 *
 * Ghost HAS-A GhostType.
 * Ghost HAS-A Direction.
 * Ghost HAS-A MovementStrategy.
 *
 * MovementStrategy is used through composition and delegation:
 * Ghost does not decide how to choose its direction.
 * It delegates that responsibility to its current MovementStrategy.
 *
 * Different Ghost objects can use different MovementStrategy
 * implementations without changing the Ghost class.
 */
public class Ghost extends Entity implements Movable, Collidable {

    /*
    * Identifies which ghost this object represents
    * never change so its final
    * */
    private final GhostType type;
    private static final int SPEED = 8;
    private Direction direction;
    /*
    * Strategy used to decide the ghost movements
    * not final because can be changed at runtime
    * (when Pacman eats a supper pellet)
    * */
    private MovementStrategy movementStrategy;

    /*
    * Final because ghost never change their size
    * */
    private final int width;
    private final int height;

    /*
     * Creates a Ghost with:
     * - an initial position
     * - dimensions
     * - a ghost type
     * - a movement strategy
     *
     * super(position) calls the Entity constructor.
     */
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

    /*
     * Changes the movement strategy at runtime.
     *
     * This allows the same Ghost object to change behavior
     * without changing its class.
     */
    public void setMovementStrategy(MovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    /*
     * Delegates the direction decision to MovementStrategy.
     *
     * The strategy receives:
     * - this Ghost
     * - Pacman
     * - the valid directions available at that moment
     *
     * Ghost does not know how the strategy makes the decision.
     */
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

    /*
     * Calculates the future position of the Ghost
     * if it moved in the specified direction.
     *
     * This method does not modify the Ghost position.
     * It only returns a new Position.
     *
     * Movement strategies use this method to evaluate
     * possible directions before choosing one.
     */
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
    public void move() {
        setPosition(getNextPosition(direction));
    }

    public void moveTo(Position position) {
        setPosition(position);
    }
}
