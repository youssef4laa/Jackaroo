package exception;

/**
 * Exception thrown when a split operation exceeds the allowed range.
 * This is a subclass of {@code InvalidSelectionException} and is used
 * to indicate errors when an out-of-range split is attempted.
 */
public class SplitOutOfRangeException extends InvalidSelectionException {
    
    /**
     * Serial version UID for ensuring compatibility during serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code SplitOutOfRangeException} with no detail message.
     */
    public SplitOutOfRangeException() {
        super();
    }

    /**
     * Constructs a new {@code SplitOutOfRangeException} with the specified detail message.
     *
     * @param message the detail message describing the exception.
     */
    public SplitOutOfRangeException(String message) {
        super(message);
    }
}
