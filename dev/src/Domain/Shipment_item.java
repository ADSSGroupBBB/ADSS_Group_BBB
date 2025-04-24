package Domain;

/**
 * Represents a single item in a shipment.
 */
public class Shipment_item {
    private int weight; // Weight of the item
    private String name; // Name of the item
    private int amount; // Quantity of this item in the shipment

    // Constructor
    public Shipment_item(int weight, String name) {
        this.weight = weight;
        this.name = name;
        this.amount = 1; // Default amount is 1
    }

    // Getters and Setters
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    // String representation of the shipment item
    @Override
    public String toString() {
        return "Item name: " + name + ", weight: " + weight + ", amount: " + amount;
    }
}
