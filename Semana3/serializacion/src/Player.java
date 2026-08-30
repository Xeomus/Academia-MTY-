import java.io.Serializable;

public class Player implements Serializable {

    private String name;
    private int number;
    private String position;

    public Player(String name, int number, String position) {
        this.name = name;
        this.number = number;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Player {" +
                "name = " + name + '\'' +
                ", number = " + number +
                ", position = " + position + '\'' +
                '}';
    }
}