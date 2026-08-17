package pacman.model;
/*
 * Entity represents a general concept shared by
 * different game objects.
 *
 * It is abstract because we do not want to create
 * generic Entity objects directly.
 *
 * Pacman IS-A Entity.
 * Ghost IS-A Entity.
 * Wall IS-A Entity.
 *
 * Entity HAS-A Position.
 *
 * Entity does not extend Position because an Entity
 * IS NOT a Position; it only has one.
 */
public abstract class Entity {

    private Position position;
    private final Position initialPosition;

    /*
     * Initializes the current and initial positions.
     *
     * protected allows subclasses such as Pacman,
     * Ghost and Wall to call this constructor,
     * while preventing unrelated classes from using it directly.
     */
    protected Entity(Position position) {
        this.position = position;
        this.initialPosition = position;
    }

    public void resetPosition() {
        this.position = initialPosition;
    }

    /*
     * Returns the current position.
     *
     * It is public because other parts of the game
     * need to know where an Entity is located.
     */
    public Position getPosition() {
        return position;
    }

    /*
     * Changes the current position.
     *
     * It is protected so Entity and its subclasses
     * can modify the position, while external classes
     * cannot modify it directly through this method.
     */
    protected void setPosition(Position position) {
        this.position = position;
    }
}

