package Service;

import Domain.ZonesController;

public class ZonesApplication {
    private static ZonesController zc = new ZonesController();
    // Method to add a new shipping zone
    public String addShippingZone(int id, String name) {
        return zc.addShippingZone(id, name); // Delegate to UserController to add the shipping zone
    }

    // Method to delete a shipping zone by name
    public String deleteZone(String name) {
        return zc.deleteZone(name); // Delegate to UserController to delete the zone
    }

    // Method to update the rank of a shipping zone
    public String updateRank(String name, int rank) {
        return zc.updateRank(name, rank); // Delegate to UserController to update the rank
    }
}
