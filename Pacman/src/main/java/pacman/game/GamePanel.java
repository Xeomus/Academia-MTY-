package pacman.game;

import pacman.model.Direction;
import pacman.model.Position;
import pacman.model.Pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
* gamePanel IS-A JPanel
* gamePanel HAS-a KeyListener
* */
public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private static final int ROWS = 21;
    private static final int COLS = 19;
    private static final int TILE_SIZE= 32;

    private static final int BOARD_WIDTH = COLS * TILE_SIZE;
    private static final int BOARD_HEIGHT = ROWS * TILE_SIZE;
    private final Timer gameloop;

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

    public GamePanel() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        pacman = new Pacman(
                new Position(9 * TILE_SIZE, 15 * TILE_SIZE)
        );

        pacmanUpImage = loadImage("/pacmanUp.png");
        pacmanDownImage = loadImage("/pacmanDown.png");
        pacmanLeftImage = loadImage("/pacmanLeft.png");
        pacmanRightImage = loadImage("/pacmanRight.png");

        gameloop = new Timer(500, this);
        gameloop.start();

    }

    private Image loadImage(String path) {
        return new ImageIcon(GamePanel.class.getResource(path)).getImage();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        drawPacman(g);
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

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        pacman.move();
        repaint();
    }
}
