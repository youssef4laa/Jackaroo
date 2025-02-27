package exception;

/**
 * Exception thrown when a field action cannot be performed in the game.
 * This is a subclass of {@code ActionException} and is used
 * to indicate errors when a field operation is not allowed.
 */
public class CannotFieldException extends ActionException {
    
    /**
     * Serial version UID for ensuring compatibility during serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code CannotFieldException} with no detail message.
     */
    public CannotFieldException() {
        super();
    }

    /**
     * Constructs a new {@code CannotFieldException} with the specified detail message.
     *
     * @param message the detail message describing the exception.
     */
    public CannotFieldException(String message) {
        super(message);
    }
}
