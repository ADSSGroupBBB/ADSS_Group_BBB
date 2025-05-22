package Domain;

// Custom exception class for handling weight-related errors in shipments
public class WeightEx extends Exception{

    // Variable to store the weight that caused the exception
    public int weight;

    // Constructor to initialize the exception with the weight value
    public WeightEx(int weight) {
        // Call the parent constructor with a custom error message
        super("The truck cant handle this shipment's weight");

        // Assign the provided weight to the class variable
        this.weight = weight;
    }
}
