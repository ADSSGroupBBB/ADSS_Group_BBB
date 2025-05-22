package Domain;

/**
 * Represents a shipping zone with a name and rank.
 */
public class Shipping_Zone {
    private int rank;  // Rank of the importance of the zone
    private String zone_name; // Name of the zone

    // Constructor
    public Shipping_Zone(int num, String name) {
        this.zone_name = name;
        this.rank = num;
    }

    // Getters and Setters
    public int getNum() {
        return rank;
    }

    public void setNum(int num) {
        this.rank = num;
    }

    public String getName() {
        return zone_name;
    }

    public void setName(String name) {
        this.zone_name = name;
    }

    // String representation of the shipping zone
    @Override
    public String toString() {
        return "Shipping_Zone{rank=" + rank + ": zone_name='" + zone_name + "'}";
    }
}
