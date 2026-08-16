package pacman.game;

import pacman.model.Position;
import pacman.model.Pacman;

import javax.swing.*;
import java.awt.*;
/*
* gamePanel IS-A JPanel
* */
public class GamePanel extends JPanel {
    private static final int ROWS = 21;
    private static final int COLS = 19;
    private static final int TILE_SIZE= 32;

    private static final int BOARD_WIDTH = COLS * TILE_SIZE;
    private static final int BOARD_HEIGHT = ROWS * TILE_SIZE;

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
    private final Image pacmanImage;

    public GamePanel() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        pacman = new Pacman(
                new Position(9 * TILE_SIZE, 15 * TILE_SIZE)
        );

        pacmanImage = new ImageIcon(
                GamePanel.class.getResource("/pacmanRight.png")
        ).getImage();

    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        drawPacman(g);
    }

    private void drawPacman(Graphics g){
        Position position = pacman.getPosition();
        g.drawImage(pacmanImage, position.getX(), position.getY(), TILE_SIZE, TILE_SIZE, null);
    }
}
