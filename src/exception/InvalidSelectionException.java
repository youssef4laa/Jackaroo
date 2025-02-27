package exception;

/**
 * Exception thrown when an invalid selection is made in the game.
 * This is a subclass of {@code GameException} and serves as a custom
 * exception for handling selection-related errors.
 */
public abstract class InvalidSelectionException extends GameException {
    
    /**
     * Serial version UID for ensuring compatibility during serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code InvalidSelectionException} with no detail message.
     */
    public InvalidSelectionException() {
        super();
    }

    /**
     * Constructs a new {@code InvalidSelectionException} with the specified detail message.
     *
     * @param message the detail message describing the exception.
     */
    public InvalidSelectionException(String message) {
        super(message);
    }
}


