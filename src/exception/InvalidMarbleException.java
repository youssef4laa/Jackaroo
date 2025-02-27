package exception;

/**
 * Exception thrown when an invalid marble selection is made in the game.
 * This is a subclass of {@code InvalidSelectionException} and is used
 * to indicate errors when an invalid marble is selected.
 */
public class InvalidMarbleException extends InvalidSelectionException {
    
    /**
     * Serial version UID for ensuring compatibility during serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code InvalidMarbleException} with no detail message.
     */
    public InvalidMarbleException() {
        super();
    }

    /**
     * Constructs a new {@code InvalidMarbleException} with the specified detail message.
     *
     * @param message the detail message describing the exception.
     */
    public InvalidMarbleException(String message) {
        super(message);
    }
}
