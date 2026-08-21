package pacman.model;
/*
 * Pacman IS-A Entity through inheritance.
 *
 * Pacman implements the behaviors defined by:
 * - Movable
 * - Collidable
 *
 * Pacman HAS-A Position through Entity.
 * Pacman HAS-A Direction.
 *
 * Polymorphism example:
 *
 * Entity entity = new Pacman(...);
 *
 * The reference type is Entity,
 * but the real object is Pacman.
 */
public class Pacman extends Entity implements Movable, Collidable {

    /*
     * Pacman HAS-A Direction.
     * It represents the current movement direction.
     */
    private Direction direction;
    /*
     * Constant shared by every Pacman instance.
     * static: belongs to the class.
     * final: cannot be reassigned.
     */
    private static final int SPEED = 8;

    /*
    * Dimensions used for collisions,
    * final because Pacman size doesn't change
    * */
    private final int width;
    private final int height;
    /*
    * Creates a Pacman
    * super (position) call the constructor in Entity
    * */
    public Pacman(Position position, int width, int height) {
        super(position);
        this.width = width;
        this.height = height;
        this.direction = Direction.RIGHT;
    }

    /*
    * Pacman HAS-A Direction
    * current direction of Pacman
    * */
    public Direction getDirection() {
        return direction;
    }

    /*
    * Changes Pacman's current direction
    * */
    public void setDirection(Direction direction){
        this.direction = direction;
    }

    /*
     * Returns Pacman's size
     * contract with Collidable
     * */
    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    /*
    * Move Pacman to the next position calculated
    * from its current direction (Movable)
    * (move)
    * */
    @Override
    public void move() {
        setPosition(getNextPosition());
    }

    /*
    * Used by game logic to make Pacman turns
    * when the movement already been validated
    * (tunnel teleport, move to this  specific position)
    * */
    public void moveTo(Position position){
        setPosition(position);
    }

    /*
    * Calculates Pacman next position without modify
    * the current position of Pacman
    * (where is pacman going?)
    * */
    public Position getNextPosition() {

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



}
