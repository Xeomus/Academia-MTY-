package pacman.game;

/*
 * GameState represents and controls the current state
 * of the game.
 *
 * It is responsible for:
 * - Score
 * - Lives
 * - Game Over state
 *
 * This class is an example of encapsulation because
 * its internal state is private and cannot be modified
 * directly from outside the class.
 *
 * Instead of exposing setters, GameState provides
 * specific operations such as:
 * - addScore()
 * - loseLife()
 * - reset()
 *
 * This allows the class to control how its state changes.
 */

public class GameState {

    private static final int INITIAL_LIVES = 3;

    private int score;
    private int lives;
    private boolean gameOver;

    public GameState() {
        reset();
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void addScore(int points) {
        if (points > 0) {
            score += points;
        }
    }

    public void loseLife() {
        if (lives > 0) {
            lives--;
        }

        if (lives == 0) {
            gameOver = true;
        }
    }

    public void reset() {
        score = 0;
        lives = INITIAL_LIVES;
        gameOver = false;
    }
}