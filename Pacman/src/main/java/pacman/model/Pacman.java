package pacman.model;
/*
* Pacman IS-A Entity
* Pacman HAS-A Position
*
* The reference is Entity but the real object is Pacman
* */
public class Pacman extends Entity{

    /*
    * Call to parent constructor (super)
    * */
    public Pacman(Position position){
        super(position);
    }

    @Override
    public void move() {

    }
}
