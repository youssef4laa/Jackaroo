package exception;

/**
 * An abstract class that represents a game-specific exception.
 * <p>
 * This class extends the built-in {@link Exception} class and serves as the base
 * class for all exceptions related to game-related errors.
 * </p>
 * 
 * @author YourName
 * @version 1.0
 * @since 1.0
 */
public abstract class GameException extends Exception {

    /** The serial version UID for serialization. */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor that initializes the {@link GameException} without any error message.
     * <p>
     * This constructor calls the default constructor of the {@link Exception} class.
     * </p>
     */
    public GameException() {
        super();
    }

    /**
     * Constructor that initializes the {@link GameException} with a custom error message.
     * <p>
     * This constructor calls the constructor of the {@link Exception} class and passes the provided
     * message to the parent class.
     * </p>
     * 
     * @param message The error message that describes the exception.
     */
    public GameException(String message) {
        super(message);
    }
}

