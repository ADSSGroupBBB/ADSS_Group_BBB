package Domain;

public class Exception extends java.lang.Exception {
    private String message;

    // Constructor
    public Exception(String message) {
        super(message);
        this.message = message;
    }

    // Getter and Setter
    @Override
    public String getMessage() {
        return message;
    }

}
