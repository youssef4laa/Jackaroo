package exception;

public class IllegalDestroyException extends ActionException {
    
    /**
     * Serial version UID for ensuring compatibility during serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code IllegalDestroyException} with no detail message.
     */
    public IllegalDestroyException() {
        super();
    }

    /**
     * Constructs a new {@code IllegalDestroyException} with the specified detail message.
     *
     * @param message the detail message describing the exception.
     */
    public IllegalDestroyException(String message) {
        super(message);
    }
}
