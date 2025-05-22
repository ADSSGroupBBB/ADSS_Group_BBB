package Domain;

import java.util.Objects;

public class ZonesController extends DeliveriesController {
    // Method to add a shipping zone
    public String addShippingZone(int id, String name) {
        Shipping_Zone existingZone = findZoneByName(name);

        if (existingZone != null) {
            return "Zone with name " + name + " already exists.\nCurrent zone rank: " + existingZone.getNum();
        } else {
            Shipping_Zone newZone = new Shipping_Zone(id, name);
            zoneMap.put(name, newZone);
            return "New shipping zone created: " + newZone;
        }
    }

    // Helper method to find a zone by name
    private static Shipping_Zone findZoneByName(String name) {
        for (String key : zoneMap.keySet()) {
            if (Objects.equals(key, name)) {
                return zoneMap.get(key);
            }
        }
        return null;
    }

    // Method to delete a shipping zone
    public String deleteZone(String name) {
        Shipping_Zone zone = findZoneByName(name);
        if (zone == null) {
            return "Zone with this name doesn't exist";
        } else {
            zoneMap.remove(name);
            return name + " deleted.";
        }
    }

    // Updates the rank of a shipping zone
    public String updateRank(String name, int rank) {
        Shipping_Zone zone = findZoneByName(name);
        if (zone == null) {
            return "Zone with this name doesn't exist"; // Zone not found
        } else {
            zone.setNum(rank); // Update zone rank
            return "Zone rank updated to " + rank; // Return confirmation message
        }
    }
}
