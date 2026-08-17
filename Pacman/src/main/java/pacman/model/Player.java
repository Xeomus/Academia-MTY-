package pacman.model;

/*
 * Player represents a player registered in the leaderboard.
 *
 * Player implements Comparable<Player>, which defines
 * the natural ordering between Player objects.
 */
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

    /*
     * Defines the natural ordering of Player objects.
     *
     * Integer.compare compares the other player's score
     * with this player's score so that higher scores
     * are ordered before lower scores.
     *
     * Comparable<Player> uses Generics to specify that
     * a Player is compared with another Player.
     */
    @Override
    public int compareTo(Player other) {
        return Integer.compare(other.score, this.score);
    }

    /*
     * Returns a readable representation of the Player.
     *
     * Example:
     * John Doe - 1500
     */
    @Override
    public String toString() {
        return name + " - " + score;
    }
}
