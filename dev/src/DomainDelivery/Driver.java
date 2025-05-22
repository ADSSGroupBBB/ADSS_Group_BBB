package DomainDelivery;

import java.util.List;
import java.util.Objects;

/**
 * Represents a driver in the system.
 */
public class Driver {
    private String driver_id; // Unique identifier for the driver
    private String name; // Driver's name
    private List<Integer> licenses_list; // List of license types (as integers)
    private boolean availability; // Driver's availability status

    // Constructor
    public Driver(String driver_id, String name, List<Integer> licenses_list) {
        // Initialize driver fields
        this.driver_id = driver_id;
        this.name = name;
        this.licenses_list = licenses_list;
        this.availability = true; // Default availability is true
    }

    // Getters and Setters

    public String getDriver_id() {
        return driver_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getLicenses_list() {
        return licenses_list;
    }

    public void setLicenses_list(List<Integer> licenses_list) {
        this.licenses_list = licenses_list;
    }

    public boolean is_available() {
        return availability;
    }

    public void set_availability(boolean is_available) {
        this.availability = is_available;
    }

    // String representation of the driver
    @Override
    public String toString() {
        StringBuilder licensesStr = new StringBuilder();
        for (int i = 0; i < licenses_list.size(); i++) {
            licensesStr.append(licenses_list.get(i));

            if (i < licenses_list.size() - 1) {
                licensesStr.append(" ");
            }
        }
        return "Driver {id=" + driver_id + ", name='" + name + "', " + " licences list: " + licensesStr + "}";
    }
}
