package ServiceD;
import DomainD.LocationController;

import java.sql.SQLException;

public class LocationApplication {
    // Create an instance of the UserController class
    LocationController lc = new LocationController();


    // Method to insert a new location
    public String insertLocation(String contactName, String contactNum, String address, String zone) throws SQLException {
        return lc.insertLocation(contactName, contactNum, address, zone); // Delegate to LocationController to insert the location
    }

    // Method to delete a location by address
    public String deleteLocation(String address) throws SQLException {
        return lc.deleteLocation(address); // Delegate to LocationsController to delete the location
    }

    // Method to print all locations
    public String printLocations() throws SQLException {
        return lc.printLocations(); // Delegate to LocationsController to print all locations
    }
    public String addItemToLocation(String address, String itemName, int quantity) throws SQLException {
        return lc.addItemToLocation(address, itemName, quantity);
    }
}