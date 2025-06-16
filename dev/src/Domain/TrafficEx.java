package Domain;

// Custom exception class for handling traffic-related delays in shipments
public class TrafficEx extends java.lang.Exception {

    // Constructor to initialize the exception with a specific traffic-related message
    public TrafficEx(String message) {
        // Call the parent constructor with a custom error message
        super("An event related to traffic has delayed the shipment: " + message);
    }
}