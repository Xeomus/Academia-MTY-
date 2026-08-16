package pacman.game;

import pacman.model.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Leaderboard {

    private final List<Player> players = new ArrayList<>();

    public void addPlayer(Player player) {
        players.add(player);
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
