package pacman.game;

import pacman.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
* gamePanel IS-A JPanel
* gamePanel HAS-A KeyListener
* gamePanel HAS-A Board
* gamePanel HAS-A Pacman
* */
public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private static final int ROWS = 21;
    private static final int COLS = 19;
    private static final int TILE_SIZE= 32;
    private static final int FOOD_SIZE = 4;

    private static final int BOARD_WIDTH = COLS * TILE_SIZE;
    private static final int BOARD_HEIGHT = ROWS * TILE_SIZE;
    private final Timer gameLoop;
    private final Board board;
    private final Image wallImage;
    private final GameState gameState;
    private final Leaderboard leaderboard;
    private boolean scoreRegistered;
    /*
    * building two objects by composition
    * because gamePanel isn't a Pacman
    *
    * gamePanel HAS-A Pacman
    * and
    * Pacman HAS-A Position
    * and
    * position is immutable object but Pacman doesn't
    * */
    private final Pacman pacman;
    private final Image pacmanUpImage;
    private final Image pacmanDownImage;
    private final Image pacmanLeftImage;
    private final Image pacmanRightImage;

    private final Image blinkyImage;
    private final Image pinkyImage;
    private final Image inkyImage;
    private final Image clydeImage;

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

        gameLoop = new Timer(500, this);
        gameLoop.start();

    }

    private Image loadImage(String path) {
        return new ImageIcon(GamePanel.class.getResource(path)).getImage();
    }

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
                        "Unknown ghost type: "
                                + ghost.getType()
                );
        }
    }

    @Override
    protected void paintComponent(Graphics g){
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
    private void drawWalls(Graphics g){
        for (Wall wall: board.getWalls()) {
            Position position = wall.getPosition();

            g.drawImage(wallImage, position.getX(), position.getY(), TILE_SIZE, TILE_SIZE, null);
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

    private void drawPacman(Graphics g){
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
        g.drawImage(image, position.getX(), position.getY(), TILE_SIZE, TILE_SIZE, null);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

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
                pacman.setDirection(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                pacman.setDirection(Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                pacman.setDirection(Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                pacman.setDirection(Direction.RIGHT);
                break;
            default:
                return;
        }

    }

    private void restartGame() {

        scoreRegistered = false;
        gameState.reset();
        resetPositions();
        gameLoop.start();
        repaint();
    }

    private void updatePacman() {

        Position currentPosition = pacman.getPosition();
        Position nextPosition = pacman.getNextPosition();

        pacman.moveTo(nextPosition);

        for (Wall wall : board.getWalls()) {

            if (CollisionDetector.isColliding(pacman, wall)) {
                pacman.moveTo(currentPosition);
                return;
            }
        }
    }

    private void updateGhosts() {

        for (Ghost ghost : board.getGhosts()) {

            Position currentPosition = ghost.getPosition();
            Position nextPosition = ghost.getNextPosition();

            ghost.moveTo(nextPosition);

            boolean collision = false;

            for (Wall wall : board.getWalls()) {

                if (CollisionDetector.isColliding(ghost, wall)) {

                    collision = true;
                    break;
                }
            }

            if (collision) {

                ghost.moveTo(currentPosition);
                ghost.updateDirection(
                        pacman.getPosition()
                );
            }
        }
    }

    private void resetPositions() {

        pacman.resetPosition();
        pacman.setDirection(Direction.RIGHT);

        for (Ghost ghost : board.getGhosts()) {

            ghost.resetPosition();

            ghost.updateDirection(
                    pacman.getPosition()
            );
        }
    }

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

    private void checkFoodCollision() {

        Food eatenFood = null;

        Position pacmanPosition = pacman.getPosition();

        for (Food food : board.getFoods()) {

            Position foodPosition = food.getPosition();

            boolean isInsidePacman =
                    foodPosition.getX() >= pacmanPosition.getX()
                            && foodPosition.getX() < pacmanPosition.getX() + pacman.getWidth()
                            && foodPosition.getY() >= pacmanPosition.getY()
                            && foodPosition.getY() < pacmanPosition.getY() + pacman.getHeight();

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

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updatePacman();
        updateGhosts();

        checkGhostCollision();
        checkFoodCollision();

        repaint();
    }
}
