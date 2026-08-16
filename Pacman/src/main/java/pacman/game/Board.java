package pacman.game;

import pacman.model.Position;
import pacman.model.Wall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
* HAS-A List<Wall>
* */
public class Board {

    private static final int TILE_SIZE = 32;
    private final List<Wall> walls = new ArrayList<>();

    private final String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    public Board() {
        loadWalls();
    }

    private void loadWalls() {

        for (int row = 0; row < tileMap.length; row++) {

            String currentRow = tileMap[row];

            for (int column = 0; column < currentRow.length(); column++) {

                char tile = currentRow.charAt(column);

                if (tile == 'X') {

                    int x = column * TILE_SIZE;
                    int y = row * TILE_SIZE;

                    walls.add(
                            new Wall(
                                    new Position(x, y)
                            )
                    );
                }
            }
        }
    }

    public List<Wall> getWalls(){
        return Collections.unmodifiableList(walls);
    }
}
