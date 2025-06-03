package Service;

import Domain.Location;
import Domain.TrucksController;

import java.sql.SQLException;
import java.util.List;

public class TrucksApplication {
    private static TrucksController tc = new TrucksController();

    // Method to change the truck for a route
    public String changeTruck(List<Location> route, String truckID, String driverID) throws SQLException {
        return tc.changeTruck(route, truckID, driverID); // Delegate to TrucksController to change the truck
    }
    // Method to insert a new truck
    public String insertTruck(String id, int type, int truckWeight, int maxWeight) throws SQLException {
        return tc.insertTruck(id, type, truckWeight, maxWeight); // Delegate to TrucksController to insert the truck
    }

    // Method to delete a truck by ID
    public String deleteTruck(String id) throws SQLException {
        return tc.deleteTruck(id); // Delegate to TrucksController to delete the truck
    }
    // Method to check if a truck is available
    public boolean isAvailableTruck(String id) throws SQLException {
        return tc.isAvailableTruck(id); // Delegate to TrucksController to check truck availability
    }
    public String printTrucks() throws SQLException {
        return tc.printTrucks(); // Delegate to TrucksController to print all locations
    }
}