package Domain;

public class TrafficEx extends Exception {
    public TrafficEx(String message) {  // what happened as an input
        super("An event related to traffic has delayed the shipment: " +message);
    }
}
