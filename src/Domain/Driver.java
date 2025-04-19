package Domain;

import java.util.List;
import java.util.Objects;

public class Driver {
    private String driver_id;
    private String name;
    private List<Integer> licenses_list;
    private boolean availability;

    // Constructor
    public Driver(String driver_id, String name, List<Integer> licenses_list) {
        // check distinct value of id
        this.driver_id = driver_id;
        this.name = name;
        this.licenses_list = licenses_list;
        this.availability = true;
    }

    // Getters and Setters
    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
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
