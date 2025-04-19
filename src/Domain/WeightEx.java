package Domain;

public class WeightEx extends Exception{
    public WeightEx() {
        super("The truck cant handle this shipment's weight");
    }
}
