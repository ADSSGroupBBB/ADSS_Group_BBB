package Domain;

import DTO.LocationDTO;
import DTO.ShippingZoneDTO;
import DataAccess.DAO.LocationDAOImpl;
import DataAccess.Interface.LocationDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationController  extends DeliveriesController{
    protected static final LocationDAO locationDao = new LocationDAOImpl();

    // Adds a new location to the system
    public String insertLocation(String contactName, String contactNum, String address, String input_zone) throws SQLException {
        if (locationsMap.containsKey(address)) {
            return "A location with this address already exists."; // Check if address exists
        }
        Optional<LocationDTO> optional = locationDao.findByAddress(address);
        if (optional.isPresent()) {
            return "A location with this address already exists."; // Check if address exists
        }
        Shipping_Zone zone = zoneMap.get(input_zone);
        if (zone == null) {
            return "Zone doesn't exist. Add it before adding new location."; // Zone not found
        }
        Location newLocation = new Location(contactName, contactNum, address, zone); // Create new location object
        locationsMap.put(address, newLocation); // Add new location to the map
        locationDao.save(new LocationDTO(address, contactName, contactNum, new ShippingZoneDTO(zone.getName(), zone.getNum())));
        return "New location added: " + address; // Return confirmation message
    }

    // Deletes a location from the system by its address
    public String deleteLocation(String address) throws SQLException {
        if (locationsMap.containsKey(address)) {
            locationsMap.remove(address); // Remove location from the map
            locationDao.deleteByAddress(address);
            return "Location removed: " + address; // Return confirmation message
        } else {
            if (locationDao.deleteByAddress(address)){
                return "Location removed: " + address; // Return confirmation message
            }
            return "No location found with address: " + address; // Location not found
        }
    }

    // Prints all location addresses in the system
    public String printLocations() throws SQLException {
        StringBuilder sb = new StringBuilder();
        List<LocationDTO> list = locationDao.findAll();
        for (LocationDTO dto : list){
            if (!locationsMap.containsKey(dto.address())){
                Shipping_Zone getZone = zoneMap.computeIfAbsent(dto.zone().name(), k -> new Shipping_Zone(dto.zone().num(), dto.zone().name()));
                Location newLoc = new Location(dto.contact_name(), dto.contact_num(), dto.address(), getZone); // Create a new location object
                locationsMap.put(newLoc.getAddress(), newLoc);
            }
        }
        for (String key : locationsMap.keySet()) {
            sb.append("Address: ").append(key).append("\n"); // Append address to result
        }
        // Remove the last newline character if the StringBuilder is not empty
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString(); // Return formatted string of addresses
    }

    public String addItemToLocation(String address, String itemName, int amountToAdd) throws SQLException {
        Location location;
        if (!locationsMap.containsKey(address)) {
            Optional<LocationDTO> optional = locationDao.findByAddress(address);
            if (optional.isPresent()) {
                LocationDTO dto = optional.get();
                Shipping_Zone getZone = zoneMap.get(dto.zone().name());
                location = new Location(dto.contact_name(), dto.contact_num(), dto.address(), getZone); // Create a new location object
            }
            else {
                return "No location found with address: " + address;
            }
        }
        else{
            location = locationsMap.get(address);
        }
        List<Shipment_item> items = location.getItems_required();
        if (totalItemsMap.containsKey(itemName)) {
            if (items == null ) {
                items = new ArrayList<>();
                Shipment_item item = new Shipment_item(totalItemsMap.get(itemName), itemName);
                item.setAmount(amountToAdd);
                items.add(item);
                location.setItems_required(items);
                return "Updated item '" + itemName + "' with additional amount: " + amountToAdd;
            } else {
                // Look for the item in the location's shipment items list
                for (Shipment_item item : items) {
                    if (item.getName().equalsIgnoreCase(itemName)) {
                        // Item found, update amount
                        item.setAmount(item.getAmount() + amountToAdd);
                        return "Updated item '" + itemName + "' with additional amount: " + amountToAdd;
                    }
                }
                Shipment_item item = new Shipment_item(totalItemsMap.get(itemName), itemName);
                item.setAmount(amountToAdd);
                items.add(item);
                location.setItems_required(items);
                return "Updated item '" + itemName + "' with additional amount: " + amountToAdd;
            }
        }
        // Item not found in this location
        return "This item isn't known.";
    }
}