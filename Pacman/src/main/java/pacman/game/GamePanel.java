package pacman.game;

import pacman.exception.ResourceLoadException;
import pacman.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
 * GamePanel represents the main game area.
 *
 * GamePanel IS-A JPanel through inheritance.
 *
 * GamePanel implements:
 * - KeyListener: receives keyboard input.
 * - ActionListener: receives events from the game Timer.
 *
 * GamePanel HAS-A:
 * - Board
 * - Pacman
 * - GameState
 * - Leaderboard
 * - Timer
 *
 * This class coordinates the main game flow:
 * rendering, movement, input, collisions and game updates.
 */

public class GamePanel extends JPanel implements KeyListener, ActionListener {

    private static final int TUNNEL_ROW = 9;

    /*
     * Board dimensions measured in tiles.
     */
    private static final int ROWS = 21;
    private static final int COLS = 19;

    /*
     * Size of each board tile in pixels.
     */
    private static final int TILE_SIZE = 32;
    private static final int FOOD_SIZE = 4;

    /*
     * Board dimensions measured in pixels.
     */
    private static final int BOARD_WIDTH = COLS * TILE_SIZE;
    private static final int BOARD_HEIGHT = ROWS * TILE_SIZE;

    private final Timer gameLoop;

    /*
     * GamePanel HAS-A Board.
     *
     * Board contains the walls, food and ghosts
     * that form the game map.
     */
    private final Board board;
    private final Image wallImage;

    /*
     * GamePanel HAS-A GameState.
     *
     * GameState manages score, lives and Game Over.
     */
    private final GameState gameState;

    /*
     * GamePanel HAS-A Leaderboard.
     *
     * It stores the players and their scores.
     */
    private final Leaderboard leaderboard;

    /*
     * Prevents the same score from being registered
     * multiple times after Game Over.
     */
    private boolean scoreRegistered;


    /*
     * GamePanel HAS-A Pacman.
     *
     * GamePanel is not Pacman; it contains and coordinates
     * a Pacman object.
     *
     * Pacman HAS-A Position through Entity.
     */
    private final Pacman pacman;

    /*
     * Stores the direction requested by the player.
     *
     * The requested direction may be different from
     * Pacman's current direction until the movement
     * becomes possible.
     *
     * This provides input buffering at intersections.
     */
    private Direction requestedDirection = Direction.RIGHT;

    private final Image pacmanUpImage;
    private final Image pacmanDownImage;
    private final Image pacmanLeftImage;
    private final Image pacmanRightImage;

    private final Image blinkyImage;
    private final Image pinkyImage;
    private final Image inkyImage;
    private final Image clydeImage;

    /*
     * Creates and initializes the game panel.
     *
     * The constructor:
     * - configures the JPanel
     * - creates the game state
     * - creates Pacman and the Board
     * - loads image resources
     * - creates and starts the game loop
     */
    public GamePanel() {

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        gameState = new GameState();
        leaderboard = new Leaderboard();
        scoreRegistered = false;

        pacman = new Pacman(
                new Position(9 * TILE_SIZE, 15 * TILE_SIZE),
                TILE_SIZE,
                TILE_SIZE
        );

        board = new Board();

        wallImage = loadImage("/wall.png");

        pacmanUpImage = loadImage("/pacmanUp.png");
        pacmanDownImage = loadImage("/pacmanDown.png");
        pacmanLeftImage = loadImage("/pacmanLeft.png");
        pacmanRightImage = loadImage("/pacmanRight.png");

        blinkyImage = loadImage("/redGhost.png");
        pinkyImage = loadImage("/pinkGhost.png");
        inkyImage = loadImage("/blueGhost.png");
        clydeImage = loadImage("/orangeGhost.png");

        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

    /*
     * Loads an image from the application resources.
     *
     * If the resource cannot be found,
     * ResourceLoadException is thrown.
     */
    private Image loadImage(String path) {

        URL resource = GamePanel.class.getResource(path);

        if (resource == null) {
            throw new ResourceLoadException(
                    "Image resource not found: " + path
            );
        }

        return new ImageIcon(resource).getImage();
    }

    /*
     * Returns the image corresponding to a GhostType.
     *
     * The GhostType enum guarantees that the ghost
     * belongs to the predefined set of ghost types.
     */
    private Image getGhostImage(Ghost ghost) {

        switch (ghost.getType()) {

            case BLINKY:
                return blinkyImage;

            case PINKY:
                return pinkyImage;

            case INKY:
                return inkyImage;

            case CLYDE:
                return clydeImage;

            default:
                throw new IllegalStateException(
                        "Unknown ghost type: " + ghost.getType()
                );
        }
    }

    /*
     * Swing calls paintComponent when the panel
     * needs to be rendered.
     *
     * Each visual responsibility is delegated
     * to a separate drawing method.
     */
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        drawWalls(g);
        drawFood(g);
        drawGhosts(g);
        drawPacman(g);
        drawHud(g);
        drawLeaderboard(g);
    }

    private void drawLeaderboard(Graphics g) {

        if (!gameState.isGameOver()) {
            return;
        }

        var players = leaderboard.getPlayersByScore();

        int y = TILE_SIZE * 2;

        g.drawString(
                "Leaderboard",
                TILE_SIZE / 2,
                y
        );

        int limit = Math.min(
                players.size(),
                3
        );

        for (int i = 0; i < limit; i++) {

            Player player = players.get(i);

            y += 25;

            g.drawString(
                    (i + 1)
                            + ". "
                            + player.getName()
                            + " - "
                            + player.getScore(),
                    TILE_SIZE / 2,
                    y
            );
        }
    }

    private void drawFood(Graphics g) {

        g.setColor(Color.WHITE);

        for (Food food : board.getFoods()) {

            Position position = food.getPosition();

            g.fillOval(
                    position.getX() - FOOD_SIZE / 2,
                    position.getY() - FOOD_SIZE / 2,
                    FOOD_SIZE,
                    FOOD_SIZE
            );
        }
    }

    private void drawHud(Graphics g) {

        g.setColor(Color.WHITE);

        g.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        if (gameState.isGameOver()) {

            g.drawString(
                    "GAME OVER - Score: "
                            + gameState.getScore(),
                    TILE_SIZE / 2,
                    TILE_SIZE / 2
            );

        } else {

            g.drawString(
                    "Lives: "
                            + gameState.getLives()
                            + "  Score: "
                            + gameState.getScore(),
                    TILE_SIZE / 2,
                    TILE_SIZE / 2
            );
        }
    }

    private void drawWalls(Graphics g) {

        for (Wall wall : board.getWalls()) {

            Position position = wall.getPosition();

            g.drawImage(
                    wallImage,
                    position.getX(),
                    position.getY(),
                    TILE_SIZE,
                    TILE_SIZE,
                    null
            );
        }
    }

    private void drawGhosts(Graphics g) {

        for (Ghost ghost : board.getGhosts()) {

            Position position = ghost.getPosition();
            Image image = getGhostImage(ghost);

            g.drawImage(
                    image,
                    position.getX(),
                    position.getY(),
                    ghost.getWidth(),
                    ghost.getHeight(),
                    null
            );
        }
    }

    private void drawPacman(Graphics g) {

        Position position = pacman.getPosition();
        Image image;

        switch (pacman.getDirection()) {

            case UP:
                image = pacmanUpImage;
                break;

            case DOWN:
                image = pacmanDownImage;
                break;

            case LEFT:
                image = pacmanLeftImage;
                break;

            case RIGHT:
                image = pacmanRightImage;
                break;

            default:
                image = pacmanRightImage;
        }

        g.drawImage(
                image,
                position.getX(),
                position.getY(),
                TILE_SIZE,
                TILE_SIZE,
                null
        );
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    /*
     * Handles keyboard input.
     *
     * Arrow keys update the requested direction.
     *
     * During Game Over, ENTER starts a new game.
     */
    @Override
    public void keyPressed(KeyEvent e) {

        if (gameState.isGameOver()) {

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                restartGame();
            }

            return;
        }

        switch (e.getKeyCode()) {

            case KeyEvent.VK_UP:
                requestedDirection = Direction.UP;
                break;

            case KeyEvent.VK_DOWN:
                requestedDirection = Direction.DOWN;
                break;

            case KeyEvent.VK_LEFT:
                requestedDirection = Direction.LEFT;
                break;

            case KeyEvent.VK_RIGHT:
                requestedDirection = Direction.RIGHT;
                break;

            default:
                return;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}


    /*
     * Called periodically by the Swing Timer.
     *
     * Each game tick:
     * 1. Updates Pacman.
     * 2. Updates the ghosts.
     * 3. Checks collisions.
     * 4. Requests a new render.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        updatePacman();
        updateGhosts();

        checkGhostCollision();
        checkFoodCollision();

        repaint();
    }

    /*
     * Registers the player's final score.
     *
     * scoreRegistered prevents this operation
     * from executing more than once per Game Over.
     *
     * If no valid name is provided,
     * "Player" is used as the default name.
     */
    private void registerScore() {

        if (scoreRegistered) {
            return;
        }

        String name = JOptionPane.showInputDialog(
                this,
                "Enter your name:"
        );

        if (name == null || name.isBlank()) {
            name = "Player";
        }

        Player player = new Player(
                name,
                gameState.getScore()
        );

        leaderboard.addPlayer(player);

        scoreRegistered = true;
    }

    /*
     * Checks whether Pacman can move one step
     * in the specified direction.
     *
     * Pacman is temporarily moved to its future position
     * to test collisions with walls and is then restored
     * to its original position.
     *
     * The method only answers whether the movement
     * is valid; it does not leave Pacman moved.
     */
    private boolean canPacmanMove(Direction direction) {

        //stores current position (x.y)
        Position currentPosition = pacman.getPosition();
        //stores current direction (up,down,left,right)
        Direction currentDirection = pacman.getDirection();
        //direction to validate
        pacman.setDirection(direction);
        //calculate the movement to the direction that we want validate
        Position nextPosition = pacman.getNextPosition();
        //restores original direction (before: up , after: up)
        pacman.setDirection(currentDirection);
        //temporally move pacman, for the collision proof
        pacman.moveTo(nextPosition);
        //check all walls, for each wall ask if pacman crash
        //we use nextPosition to simulate the move and check for a collision

        for (Wall wall : board.getWalls()) {

            if (CollisionDetector.isColliding(pacman, wall)) {

                pacman.moveTo(currentPosition);
                //Pacman can't go to the simulated position
                return false;
            }
        }

        pacman.moveTo(currentPosition);

        return true;
    }

    /*
     * Updates Pacman's position.
     *
     * If the player's requested direction becomes valid,
     * Pacman changes to that direction.
     *
     * Otherwise Pacman continues moving in its
     * current direction when possible.
     *
     * Horizontal tunnel behavior is applied before
     * the final position is assigned.
     */
    private void updatePacman() {

        if (canPacmanMove(requestedDirection)) {
            pacman.setDirection(requestedDirection);
        }

        if (canPacmanMove(pacman.getDirection())) {

            Position nextPosition =
                    pacman.getNextPosition();

            nextPosition =
                    applyHorizontalTunnel(
                            nextPosition,
                            pacman.getWidth()
                    );

            pacman.moveTo(nextPosition);
        }
    }

    /*
     * Updates every Ghost on the Board.
     *
     * A Ghost chooses a new direction when:
     * - its current direction is blocked
     * - it reaches an intersection
     *
     * The direction decision is delegated to the
     * Ghost's current MovementStrategy.
     */
    private void updateGhosts() {

        //check for all ghost
        for (Ghost ghost : board.getGhosts()) {
            //store all the ghost on a list
            //and gets all directions that
            //ghost can move without crash with wall
            //[up,right,left] that means down its blocked
            List<Direction> validDirections =
                    getValidDirections(ghost);

            //if the ghost can't move in any direction
            //pass to the next ghost
            if (validDirections.isEmpty()) {
                continue;
            }
            //ask if the current direction is blocked
            //valid direction contains down? = false
            //! turns to true, so down is blocked
            //ghost cant continuous in his current direction
            boolean currentDirectionBlocked =
                    !validDirections.contains(
                            ghost.getDirection()
                    );

            //avoid change direction if the ghost is in the middle
            //of two tiles
            boolean intersection =
                    isAtTileCenter(ghost)
                            //ask if the ghost have more than 2 possible directions
                            //[UP, LEFT, RIGHT] = 3
                            //if both conditions are true it means,
                            //that the ghost is in a intersection
                            && validDirections.size() > 2;
            //ghost need choose a new direction
            //to reasons: one is blocked or ghost is in a intersection
            if (currentDirectionBlocked || intersection) {
                //list with the possible directions
                List<Direction> candidates =
                        //we dont want ghost goes backwards
                        removeReverseDirection(
                                ghost,
                                validDirections
                        );
                //ghost choose a new direction
                //he needs to know where Pacman is?
                //and candidates (valid directions)
                ghost.updateDirection(
                        pacman,
                        candidates
                );
            }
            //calculates the new ghost direction
            Position nextPosition =
                    ghost.getNextPosition(
                            //gets the simulated direction
                            ghost.getDirection()
                    );
            //tunnel logic
            nextPosition =
                    applyHorizontalTunnel(
                            nextPosition,
                            //to determinate when the ghost disappears
                            ghost.getWidth()
                    );
            //the real movement happens
            ghost.moveTo(nextPosition);
        }
    }

    /*
     * Calculates every direction in which the Ghost
     * can move without colliding with a Wall.
     *
     * Direction.values() provides all Direction enum values.
     *
     * Each possible movement is temporarily simulated
     * and checked using CollisionDetector.
     */
    private List<Direction> getValidDirections(Ghost ghost) {

        List<Direction> validDirections =
                new ArrayList<>();

        for (Direction direction : Direction.values()) {

            Position currentPosition =
                    ghost.getPosition();

            Position nextPosition =
                    ghost.getNextPosition(direction);

            ghost.moveTo(nextPosition);

            boolean collision = false;

            for (Wall wall : board.getWalls()) {

                if (CollisionDetector.isColliding(ghost, wall)) {

                    collision = true;
                    break;
                }
            }

            ghost.moveTo(currentPosition);

            if (!collision) {
                validDirections.add(direction);
            }
        }

        return validDirections;
    }

    /*
     * Removes the direction opposite to the Ghost's
     * current movement direction.
     *
     * This prevents ghosts from unnecessarily reversing
     * direction at intersections.
     *
     * If reversing is the only available option,
     * the original list is returned.
     */
    private List<Direction> removeReverseDirection(
            Ghost ghost,
            List<Direction> directions
    ) {

        if (directions.size() <= 1) {
            return directions;
        }

        List<Direction> filteredDirections =
                new ArrayList<>(directions);

        filteredDirections.remove(
                ghost.getDirection().opposite()
        );

        if (filteredDirections.isEmpty()) {
            return directions;
        }

        return filteredDirections;
    }

    /*
     * Checks whether the Ghost is aligned with
     * the board's tile grid.
     *
     * Ghosts evaluate intersections when their
     * position is aligned with a complete tile.
     */
    private boolean isAtTileCenter(Ghost ghost) {

        Position position = ghost.getPosition();

        return position.getX() % TILE_SIZE == 0
                && position.getY() % TILE_SIZE == 0;
    }

    /*
     * Applies horizontal tunnel teleportation.
     *
     * Teleportation is only allowed on TUNNEL_ROW.
     *
     * Leaving through the left side places the entity
     * on the right side and vice versa.
     *
     * A new Position is returned because Position
     * is immutable.
     */
    private Position applyHorizontalTunnel(
            Position position,
            int width
    ) {

        int x = position.getX();
        int y = position.getY();

        int tunnelY = TUNNEL_ROW * TILE_SIZE;

        if (y != tunnelY) {
            return position;
        }

        if (x + width <= 0) {
            x = BOARD_WIDTH;
        } else if (x >= BOARD_WIDTH) {
            x = -width;
        }

        return new Position(x, y);
    }


    private void restartGame() {

        scoreRegistered = false;

        gameState.reset();
        board.reset();

        pacman.resetPosition();
        pacman.setDirection(Direction.RIGHT);

        gameLoop.start();

        repaint();
    }

    /*
     * Checks collisions between Pacman and every Ghost.
     *
     * A collision removes one life.
     *
     * If no lives remain:
     * - the score is registered
     * - the game loop stops
     *
     * Otherwise Pacman and the ghosts return
     * to their initial positions.
     */
    private void checkGhostCollision() {

        for (Ghost ghost : board.getGhosts()) {

            if (CollisionDetector.isColliding(pacman, ghost)) {

                gameState.loseLife();

                if (gameState.isGameOver()) {

                    registerScore();

                    gameLoop.stop();

                    return;
                }

                resetPositions();

                return;
            }
        }
    }

    /*
     * Checks whether a Food pellet is located
     * inside Pacman's current area.
     *
     * When food is eaten:
     * - its points are added to the score
     * - it is removed from the Board
     */
    private void checkFoodCollision() {

        Food eatenFood = null;

        Position pacmanPosition =
                pacman.getPosition();

        for (Food food : board.getFoods()) {

            Position foodPosition =
                    food.getPosition();

            boolean isInsidePacman =
                    foodPosition.getX()
                            >= pacmanPosition.getX()

                            && foodPosition.getX()
                            < pacmanPosition.getX()
                            + pacman.getWidth()

                            && foodPosition.getY()
                            >= pacmanPosition.getY()

                            && foodPosition.getY()
                            < pacmanPosition.getY()
                            + pacman.getHeight();

            if (isInsidePacman) {

                eatenFood = food;

                break;
            }
        }

        if (eatenFood != null) {

            gameState.addScore(
                    eatenFood.getPoints()
            );

            board.removeFood(eatenFood);
        }
    }

    /*
     * Restores Pacman and every Ghost to their
     * initial positions after losing a life.
     *
     * The current score and remaining food
     * are preserved.
     */
    private void resetPositions() {

        pacman.resetPosition();
        pacman.setDirection(Direction.RIGHT);

        requestedDirection = Direction.RIGHT;

        for (Ghost ghost : board.getGhosts()) {

            ghost.resetPosition();

            List<Direction> validDirections =
                    getValidDirections(ghost);

            if (!validDirections.isEmpty()) {

                ghost.updateDirection(
                        pacman,
                        validDirections
                );
            }
        }
    }

}