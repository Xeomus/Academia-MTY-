package pacman;

import pacman.game.GamePanel;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {

        try {
            JFrame frame =
                    new JFrame("Pac-Man");

            GamePanel gamePanel =
                    new GamePanel();

            frame.add(gamePanel);

            frame.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );

            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        } catch (RuntimeException exception) {

            JOptionPane.showMessageDialog(
                    null,
                    exception.getMessage(),
                    "Game initialization error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
