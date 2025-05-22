package DomainDelivery;

/**
 * Custom exception class extending Java's built-in Exception.
 */
public class Exception extends java.lang.Exception {
    private String message; // Custom message for the exception

    // Constructor
    public Exception(String message) {
        super(message); // Pass message to superclass
        this.message = message;
    }

    // Getter (overrides getMessage from java.lang.Exception)
    @Override
    public String getMessage() {
        return message;
    }
}
