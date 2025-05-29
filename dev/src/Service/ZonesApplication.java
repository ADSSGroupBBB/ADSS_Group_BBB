package Service;

import Domain.ZonesController;

import java.sql.SQLException;

public class ZonesApplication {
    private static ZonesController zc = new ZonesController();
    // Method to add a new shipping zone
    public String addShippingZone(int rank, String name) throws SQLException {
        return zc.addShippingZone(rank, name); // Delegate to UserController to add the shipping zone
    }

    // Method to delete a shipping zone by name
    public String deleteZone(String name) throws SQLException {
        return zc.deleteZone(name); // Delegate to UserController to delete the zone
    }

    // Method to update the rank of a shipping zone
    public String updateRank(String name, int rank) throws SQLException {
        return zc.updateRank(name, rank); // Delegate to UserController to update the rank
    }

    public String printZones() throws SQLException {
        return zc.printZones();
    }
}
