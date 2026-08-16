package pacman.model;

/*
* Wall IS-A Entity
* Wall HAS-A Position
* */
public class Wall extends Entity implements Collidable {

    private final int width;
    private final int height;

    public Wall(Position position, int width, int height) {
        super(position);
        this.height = height;
        this.width = width;
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
