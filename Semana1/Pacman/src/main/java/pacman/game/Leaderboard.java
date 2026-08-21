package pacman.game;

import pacman.model.Player;

import java.util.*;

/*
 * Leaderboard manages the players registered
 * after a game ends.
 *
 * Leaderboard HAS-A List<Player>.
 *
 * This class also demonstrates different ways
 * of defining object ordering in Java:
 *
 * - Player implements Comparable<Player> for its natural ordering.
 * - An anonymous Comparator class is used to order by score.
 * - A lambda expression is used to order by name.
 *
 * Generics are used with List<Player> and Comparator<Player>
 * to provide type safety.
 */
public class Leaderboard {

    private final List<Player> players = new ArrayList<>();

    public void addPlayer(Player player) {
        players.add(player);
    }

    /*
     * Returns the players ordered by score
     * from highest to lowest.
     *
     * A copy of the original list is created so that
     * sorting does not modify the internal collection.
     *
     * This method uses an anonymous class that implements
     * Comparator<Player>.
     */
    public List<Player> getPlayersByScore() {

        List<Player> sortedPlayers =
                new ArrayList<>(players);

        sortedPlayers.sort(
                /*
                 * Anonymous class.
                 *
                 * An object that implements Comparator<Player>
                 * is created directly without declaring
                 * a separate named class.
                 *
                 * Comparator allows us to define an alternative
                 * ordering for Player objects.
                 */
                new Comparator<Player>() {
                    /*
                     * Compares two Player objects by score.
                     *
                     * The order is reversed (second vs first)
                     * so players with higher scores appear first.
                     */
                    @Override
                    public int compare(
                            Player first,
                            Player second
                    ) {
                        return Integer.compare(
                                second.getScore(),
                                first.getScore()
                        );
                    }
                }
        );

        return sortedPlayers;
    }


    /*
     * Returns the players ordered alphabetically by name.
     *
     * A copy of the original list is created before sorting.
     *
     * This method uses a lambda expression instead of
     * an anonymous Comparator class.
     */
    public List<Player> getPlayersByName() {

        List<Player> sortedPlayers =
                new ArrayList<>(players);

        /*
         * Lambda expression.
         *
         * List.sort() expects a Comparator<Player>.
         *
         * Comparator is a functional interface because
         * it has one abstract method: compare().
         *
         * Because of this, the Comparator can be
         * represented using a lambda expression.
         */
        sortedPlayers.sort(
                (first, second) ->
                        first.getName()
                                .compareToIgnoreCase(
                                        second.getName()
                                )
        );

        return sortedPlayers;
    }
}
