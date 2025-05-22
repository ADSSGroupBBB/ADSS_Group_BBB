package ServiceDelivery;
import DomainDelivery.LocationController;

public class LocationApplication {
    // Create an instance of the UserController class
    LocationController lc = new LocationController();


    // Method to insert a new location
    public String insertLocation(String contactName, String contactNum, String address, String zone) {
        return lc.insertLocation(contactName, contactNum, address, zone); // Delegate to LocationController to insert the location
    }

    // Method to delete a location by address
    public String deleteLocation(String address) {
        return lc.deleteLocation(address); // Delegate to LocationsController to delete the location
    }

    // Method to print all locations
    public String printLocations() {
        return lc.printLocations(); // Delegate to LocationsController to print all locations
    }
    public String addItemToLocation(String address, String itemName, int quantity) {
        return lc.addItemToLocation(address, itemName, quantity);
    }
}
