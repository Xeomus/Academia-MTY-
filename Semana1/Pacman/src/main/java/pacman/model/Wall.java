package pacman.model;

/*
 * Wall represents a wall inside the game board.
 *
 * Wall IS-A Entity through inheritance.
 * Wall implements Collidable because other entities
 * can detect collisions with it.
 *
 * Wall HAS-A Position through Entity.
 */
public class Wall extends Entity implements Collidable {

    private final int width;
    private final int height;

    public Wall(Position position, int width, int height) {
        super(position);
        this.height = height;
        this.width = width;
    }

    /*
    * comes from Collidable
    * */
    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

}
