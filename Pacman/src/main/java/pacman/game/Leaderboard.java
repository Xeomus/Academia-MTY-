package pacman.game;

import pacman.model.Player;

import java.util.*;

public class Leaderboard {

    private final List<Player> players = new ArrayList<>();

    public void addPlayer(Player player) {
        players.add(player);
    }

    public List<Player> getPlayersByScore() {

        List<Player> sortedPlayers =
                new ArrayList<>(players);

        sortedPlayers.sort(
                //anonymus class
                new Comparator<Player>() {

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

    //lambda
    public List<Player> getPlayersByName() {

        List<Player> sortedPlayers =
                new ArrayList<>(players);

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
