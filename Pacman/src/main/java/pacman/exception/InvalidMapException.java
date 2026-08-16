package pacman.exception;

public class InvalidMapException extends RuntimeException {

    public InvalidMapException(String message) {
        super(message);
    }
}