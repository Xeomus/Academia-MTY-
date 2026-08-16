package pacman.game;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private static final int ROWS = 21;
    private static final int COLS = 19;
    private static int TILE_SIZE= 32;

    private static final int BOARD_WIDTH = COLS * TILE_SIZE;
    private static final int BOARD_HEIGHT = ROWS * TILE_SIZE;

    public GamePanel() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
    }
}
