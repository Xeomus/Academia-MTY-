package pacman.game;

import pacman.model.*;
import pacman.strategy.*;
import pacman.exception.InvalidMapException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Board represents the game maze and the objects
 * that are created from the tile map.
 *
 * Board HAS-A List<Wall>.
 * Board HAS-A List<Food>.
 * Board HAS-A List<Ghost>.
 *
 * The tile map is used as the source of truth
 * to build the walls, food pellets and ghosts.
 */
public class Board {

    private static final int TILE_SIZE = 32;
    private static final int FOOD_POINTS = 10;

    /*
     * Collections that store the objects currently
     * present on the board.
     *
     * Generics provide type safety:
     * - List<Wall> only stores Wall objects.
     * - List<Food> only stores Food objects.
     * - List<Ghost> only stores Ghost objects.
     */
    private final List<Wall> walls = new ArrayList<>();
    private final List<Food> foods = new ArrayList<>();
    private final List<Ghost> ghosts = new ArrayList<>();

    /*
     * Text representation of the game board.
     *
     * Each character represents a different element:
     *
     * X -> Wall
     * ' ' -> Food
     * P -> Pacman spawn
     * r -> Blinky
     * p -> Pinky
     * b -> Inky
     * o -> Clyde
     * O -> Empty tunnel area
     */
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

    public void reset() {
        loadMap();
    }

    /*
     * Returns a read-only view of the Ghost/Food/Wall collection.
     *
     * Collections.unmodifiableList prevents external
     * classes from modifying the internal list directly.
     */
    public List<Ghost> getGhosts() {
        return Collections.unmodifiableList(ghosts);
    }

    public List<Wall> getWalls(){
        return Collections.unmodifiableList(walls);
    }

    public List<Food> getFoods() {
        return Collections.unmodifiableList(foods);
    }

    public void removeFood(Food food) {
        foods.remove(food);
    }

    /*
     * Rebuilds all board objects from tileMap.
     *
     * Existing collections are cleared first so that
     * objects are not duplicated when the board resets.
     */
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

    /*
    * Representation of valid characters (white-list)
    * */
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

    /*
     * Validates the tile map before it is loaded.
     *
     * The validation checks:
     * - the map is not empty
     * - every row has the same length
     * - every tile character is valid
     * - exactly one Pacman spawn exists
     *
     * InvalidMapException is thrown when one of
     * these rules is violated.
     */
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
