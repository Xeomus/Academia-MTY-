package pacman.model;

/*
* Wall IS-A Entity
* Wall HAS-A Position
* */
public class Wall extends Entity {

    public Wall(Position position) {
        super(position);
    }

    @Override
    public void move() {
        //a wall doesn't move
    }
}
