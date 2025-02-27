package exception;

/**
 * Exception thrown when an illegal swap action is attempted in the game.
 * This is a subclass of {@code ActionException} and is used
 * to indicate errors when a swap operation is not allowed.
 */
public class IllegalSwapException extends ActionException {
    
    /**
     * Serial version UID for ensuring compatibility during serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code IllegalSwapException} with no detail message.
     */
    public IllegalSwapException() {
        super();
    }

    /**
     * Constructs a new {@code IllegalSwapException} with the specified detail message.
     *
     * @param message the detail message describing the exception.
     */
    public IllegalSwapException(String message) {
        super(message);
    }
}
