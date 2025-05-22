package Domain;

/**
 * Represents a truck with ID, type, weight details, and driving status.
 */
public class Truck {
    private String truck_id;       // Unique identifier for the truck
    private int type;              // Type/classification of the truck
    private int truck_weight;      // The truck's own weight (empty)
    private int curr_weight;       // Current weight of the truck (includes loaded items)
    private int max_weight;        // Maximum allowed weight for the truck
    private boolean onDrive;       // Indicates if the truck is currently on a delivery

    // Constructor
    public Truck(String truck_id, int type, int truck_weight, int max_weight) {
        this.truck_id = truck_id;
        this.type = type;
        this.truck_weight = truck_weight;
        this.max_weight = max_weight;
        curr_weight = truck_weight;   // Initially, current weight equals truck's own weight
        onDrive = false;              // Truck starts as not driving
    }

    // Getters and Setters
    public String getTruck_id() {
        return truck_id;
    }

    public int getType() {
        return type;
    }

    public int getTruck_weight() {
        return truck_weight;
    }

    public int getCurrWeight() {
        return truck_weight;  // ⚠️ Note: This seems like a bug—it should return curr_weight
    }

    /**
     * Attempts to add an item to the truck.
     * Throws WeightEx if it exceeds the truck's max weight.
     */
    public void addItem(int itemWeight) throws WeightEx {
        if ((curr_weight + itemWeight) < max_weight) {
            this.curr_weight += itemWeight;
        } else {
            throw new WeightEx(curr_weight + itemWeight);
        }
    }

    /**
     * Removes weight from the truck, ensuring it doesn't go below truck's own weight.
     */
    public void removeItem(int itemWeight) {
        if ((curr_weight - itemWeight) > truck_weight) {
            this.curr_weight -= itemWeight;
        } else {
            System.out.println("Invalid");
        }
    }

    public int getMax_weight() {
        return max_weight;
    }

    public boolean getOnDrive() {
        return onDrive;
    }

    public void setCurr_weight(int curr_weight) {
        this.curr_weight = curr_weight;
    }

    public void setOnDrive(boolean onDrive) {
        this.onDrive = onDrive;
    }
}
