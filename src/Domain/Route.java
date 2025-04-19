package Domain;

import java.util.List;

public class Route {
    private List<Location> destination_list;

    // Constructor
    public Route(List<Location> destination_list) {
        this.destination_list = destination_list;
    }

    // Getters and Setters
    public List<Location> getDestination_list() {
        return destination_list;
    }

    public void setDestination_list(List<Location> destination_list) {
        this.destination_list = destination_list;
    }
}
