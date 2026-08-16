package pacman.model;

public class Player implements Comparable<Player>{

    private final String name;
    private final int score;

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Player other) {
        return Integer.compare(other.score, this.score);
    }

    @Override
    public String toString() {
        return name + " - " + score;
    }

}
