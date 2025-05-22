package Domain;

import java.util.List;

public class LocationController  extends DeliveriesController{
    // Adds a new location to the system
    public String insertLocation(String contactName, String contactNum, String address, String input_zone) {
        if (locationsMap.containsKey(address)) {
            return "A location with this address already exists."; // Check if address exists
        }
        Shipping_Zone zone = zoneMap.get(input_zone);
        if (zone == null) {
            return "Zone doesn't exist. Add it before adding new location."; // Zone not found
        }
        Location newLocation = new Location(contactName, contactNum, address, zone); // Create new location object
        locationsMap.put(address, newLocation); // Add new location to the map
        return "New location added: " + address; // Return confirmation message
    }

    // Deletes a location from the system by its address
    public String deleteLocation(String address) {
        if (locationsMap.containsKey(address)) {
            locationsMap.remove(address); // Remove location from the map
            return "Location removed: " + address; // Return confirmation message
        } else {
            return "No location found with address: " + address; // Location not found
        }
    }

    // Prints all location addresses in the system
    public String printLocations() {
        StringBuilder sb = new StringBuilder();
        for (String key : locationsMap.keySet()) {
            sb.append("Address: ").append(key).append("\n"); // Append address to result
        }
        // Remove the last newline character if the StringBuilder is not empty
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString(); // Return formatted string of addresses
    }

    public String addItemToLocation(String address, String itemName, int amountToAdd) {
        if (!locationsMap.containsKey(address)) {
            return "No location found with address: " + address;
        }

        Location location = locationsMap.get(address);
        List<Shipment_item> items = location.getItems_required();

        if (items == null || items.isEmpty()) {
            return "This item isn't in the location.";
        }

        // Look for the item in the location's shipment items list
        for (Shipment_item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                // Item found, update amount
                item.setAmount(item.getAmount() + amountToAdd);
                return "Updated item '" + itemName + "' with additional amount: " + amountToAdd;
            }
        }

        // Item not found in this location
        return "This item isn't in the location.";
    }
}
