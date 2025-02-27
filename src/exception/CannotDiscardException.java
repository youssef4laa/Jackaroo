package exception;

/**
 * Exception thrown when a discard action cannot be performed in the game.
 * This is a subclass of {@code ActionException} and is used
 * to indicate errors when a discard operation is not allowed.
 */
public class CannotDiscardException extends ActionException {
    
    /**
     * Serial version UID for ensuring compatibility during serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code CannotDiscardException} with no detail message.
     */
    public CannotDiscardException() {
        super();
    }

    /**
     * Constructs a new {@code CannotDiscardException} with the specified detail message.
     *
     * @param message the detail message describing the exception.
     */
    public CannotDiscardException(String message) {
        super(message);
    }
}