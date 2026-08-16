package pacman.game;

import pacman.model.Player;
import java.util.Comparator;

public class PlayerNameComparator implements Comparator<Player> {

    @Override
    public int compare(Player first, Player second) {
        return  first.getName().compareToIgnoreCase(second.getName());
    }
}
