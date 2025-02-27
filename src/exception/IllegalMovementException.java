package exception;

/**
 * Exception thrown when an illegal movement action is attempted in the game.
 * This is a subclass of {@code ActionException} and is used
 * to indicate errors when a movement operation is not allowed.
 */
public class IllegalMovementException extends ActionException {
    
    /**
     * Serial version UID for ensuring compatibility during serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code IllegalMovementException} with no detail message.
     */
    public IllegalMovementException() {
        super();
    }

    /**
     * Constructs a new {@code IllegalMovementException} with the specified detail message.
     *
     * @param message the detail message describing the exception.
     */
    public IllegalMovementException(String message) {
        super(message);
    }
}
