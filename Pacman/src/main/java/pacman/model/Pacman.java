package pacman.model;
/*
* Pacman IS-A Entity
* Pacman HAS-A Position
*
* The reference is Entity but the real object is Pacman
* */
public class Pacman extends Entity implements Movable, Collidable {

    private Direction direction;
    private static final int SPEED = 8;
    private final int width;
    private final int height;
    /*
    * Call to parent constructor (super)
    * */
    public Pacman(Position position, int width, int height) {
        super(position);
        this.width = width;
        this.height = height;
        this.direction = Direction.RIGHT;
    }

    /*
    * Pacman HAS-A Direction
    * */
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction){
        this.direction = direction;
    }

    @Override
    public void move() {
        setPosition(getNextPosition());
    }

    public void moveTo(Position position){
        setPosition(position);
    }


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

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }


}
