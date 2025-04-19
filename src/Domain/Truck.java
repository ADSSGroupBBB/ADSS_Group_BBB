package Domain;

public class Truck {
    private String truck_id;
    private int type;
    private int truck_weight;
    private int max_weight;

    // Constructor
    public Truck(String truck_id, int type, int truck_weight, int max_weight) {
        this.truck_id = truck_id;
        this.type = type;
        // add exception of truckweight > max
        this.truck_weight = truck_weight;
        this.max_weight = max_weight;
    }

    // Getters and Setters
    public String getTruck_id() {
        return truck_id;
    }

    public void setTruck_id(String truck_id) {
        this.truck_id = truck_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTruck_weight() {
        return truck_weight;
    }

    public void setTruck_weight(int truck_weight) {
        this.truck_weight = truck_weight;
    }

    public int getMax_weight() {
        return max_weight;
    }

    public void setMax_weight(int max_weight) {
        this.max_weight = max_weight;
    }
}
