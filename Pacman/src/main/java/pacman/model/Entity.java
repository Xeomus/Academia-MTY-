package pacman.model;
    /*
    * Entity represents a general game idea, not an object Entity(type)
    *
    * Pacman IS-A Entity
    * Ghost IS-A Entity
    * */

    //not use Entity extends a Position because Entity isn't a Position
public abstract class Entity {

    /*
    * Entity HAS-A position
    * not use final cause each Object Position is immutable,
    * but an Entity should update their position
    * */

    private Position position;

    protected Entity(Position position) {
        this.position = position;
    }

    /*
    * Any part of the game can know where Entity is,
    * but only Entity and subclass can modify their own position
    * */
    public Position getPosition() {
        return position;
    }

    protected void setPosition(Position position) {
        this.position = position;
    }

    /*
    * Each Entity should move, but Entity doesn't
    * know how each Entity moves
    * */
    public abstract void move();
}

