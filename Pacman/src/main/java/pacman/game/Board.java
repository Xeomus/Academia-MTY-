package pacman.game;

import pacman.model.*;
import pacman.strategy.*;
import pacman.exception.InvalidMapException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
* HAS-A List<Wall>
* HAS-A List<Food>
* HAS-A List<Ghost>
* */
public class Board {

    private static final int TILE_SIZE = 32;
    private final List<Wall> walls = new ArrayList<>();
    private static final int FOOD_POINTS = 10;
    private final List<Food> foods = new ArrayList<>();
    private final List<Ghost> ghosts = new ArrayList<>();

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
        validateMap();
        loadMap();
    }

    private void loadMap() {

        walls.clear();
        foods.clear();
        ghosts.clear();

        for (int row = 0; row < tileMap.length; row++) {

            String currentRow = tileMap[row];

            for (int column = 0; column < currentRow.length(); column++) {

                char tile = currentRow.charAt(column);

                int x = column * TILE_SIZE;
                int y = row * TILE_SIZE;

                switch (tile) {

                    case 'X':
                        walls.add(
                                new Wall(
                                        new Position(x, y),
                                        TILE_SIZE,
                                        TILE_SIZE
                                )
                        );
                        break;

                    case ' ':
                        foods.add(
                                new Food(
                                        new Position(
                                                x + TILE_SIZE / 2,
                                                y + TILE_SIZE / 2
                                        ),
                                        FOOD_POINTS
                                )
                        );
                        break;

                    case 'r':
                        ghosts.add(
                                new Ghost(
                                        new Position(x, y),
                                        TILE_SIZE,
                                        TILE_SIZE,
                                        GhostType.BLINKY,
                                        new ChaseMovementStrategy()
                                )
                        );
                        break;

                    case 'p':
                        ghosts.add(
                                new Ghost(
                                        new Position(x, y),
                                        TILE_SIZE,
                                        TILE_SIZE,
                                        GhostType.PINKY,
                                        new AmbushMovementStrategy()
                                )
                        );
                        break;

                    case 'b':
                        ghosts.add(
                                new Ghost(
                                        new Position(x, y),
                                        TILE_SIZE,
                                        TILE_SIZE,
                                        GhostType.INKY,
                                        new FlankMovementStrategy()
                                )
                        );
                        break;

                    case 'o':
                        ghosts.add(
                                new Ghost(
                                        new Position(x, y),
                                        TILE_SIZE,
                                        TILE_SIZE,
                                        GhostType.CLYDE,
                                        new HybridMovementStrategy()
                                )
                        );
                        break;

                    default:
                        break;
                }
            }
        }
    }

    public void reset() {
        loadMap();
    }

    public List<Ghost> getGhosts() {
        return Collections.unmodifiableList(ghosts);
    }

    public List<Food> getFoods() {
        return Collections.unmodifiableList(foods);
    }

    public void removeFood(Food food) {
        foods.remove(food);
    }

    public List<Wall> getWalls(){
        return Collections.unmodifiableList(walls);
    }

    private boolean isValidTile(char tile) {

        return tile == 'X'
                || tile == 'O'
                || tile == ' '
                || tile == 'P'
                || tile == 'r'
                || tile == 'p'
                || tile == 'b'
                || tile == 'o';
    }

    private void validateMap() {

        if (tileMap.length == 0) {
            throw new InvalidMapException(
                    "Tile map cannot be empty"
            );
        }

        int expectedColumns = tileMap[0].length();
        int pacmanCount = 0;

        for (int row = 0; row < tileMap.length; row++) {

            String currentRow = tileMap[row];

            if (currentRow.length() != expectedColumns) {

                throw new InvalidMapException(
                        "Invalid row length at row " + row
                );
            }

            for (int column = 0;
                 column < currentRow.length();
                 column++) {

                char tile = currentRow.charAt(column);

                if (!isValidTile(tile)) {

                    throw new InvalidMapException(
                            "Invalid tile '"
                                    + tile
                                    + "' at row "
                                    + row
                                    + ", column "
                                    + column
                    );
                }

                if (tile == 'P') {
                    pacmanCount++;
                }
            }
        }

        if (pacmanCount != 1) {

            throw new InvalidMapException(
                    "Tile map must contain exactly one Pacman spawn"
            );
        }
    }
}
