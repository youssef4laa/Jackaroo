package exception;

/**
 * Exception thrown when an invalid action occurs in the game.
 * This is a subclass of {@code GameException} and serves as a custom
 * exception for handling action-related errors.
 */
public abstract class ActionException extends GameException {
    
    /**
     * Serial version UID for ensuring compatibility during serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code ActionException} with no detail message.
     */
    public ActionException() {
        super();
    }

    /**
     * Constructs a new {@code ActionException} with the specified detail message.
     *
     * @param message the detail message describing the exception.
     */
    public ActionException(String message) {
        super(message);
    }
}
