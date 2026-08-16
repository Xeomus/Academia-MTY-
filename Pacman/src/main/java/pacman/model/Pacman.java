package pacman.model;
/*
* Pacman IS-A Entity
* Pacman HAS-A Position
*
* The reference is Entity but the real object is Pacman
* */
public class Pacman extends Entity{

    private Direction direction;
    /*
    * Call to parent constructor (super)
    * */
    public Pacman(Position position){
        super(position);
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

        Position currentPosition = getPosition();

        int x = currentPosition.getX();
        int y = currentPosition.getY();

        switch (direction){
            case UP:
                y--;
                break;
            case DOWN:
                y++;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
        }
        setPosition(new Position(x,y));
    }
}
