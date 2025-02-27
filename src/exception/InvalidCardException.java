package exception;

/**
 * Exception thrown when an invalid card selection is made in the game.
 * This is a subclass of {@code InvalidSelectionException} and is used
 * to indicate errors when a player selects an invalid card.
 */
public class InvalidCardException extends InvalidSelectionException {
    
    /**
     * Serial version UID for ensuring compatibility during serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code InvalidCardSelectionException} with no detail message.
     */
    public InvalidCardException() {
        super();
    }

    /**
     * Constructs a new {@code InvalidCardSelectionException} with the specified detail message.
     *
     * @param message the detail message describing the exception.
     */
    public InvalidCardException(String message) {
        super(message);
    }
}
