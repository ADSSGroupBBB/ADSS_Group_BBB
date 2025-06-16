package src.Domain;

import src.DTO.ShippingZoneDTO;
import src.DTO.TruckDTO;
import src.DataAccess.DAO.ShippingZoneDAOImpl;
import src.DataAccess.Interface.ShippingZoneDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ZonesController extends DeliveriesController {
    private static final ShippingZoneDAO shippingZoneDAO = new ShippingZoneDAOImpl();

    // Method to add a shipping zone
    public String addShippingZone(int rank, String name) throws SQLException {
        Shipping_Zone existingZone = findZoneByName(name);

        if (existingZone != null) {
            return "Zone with name " + name + " already exists.\nCurrent zone rank: " + existingZone.getNum();
        } else {
            Shipping_Zone newZone = new Shipping_Zone(rank, name);
            zoneMap.put(name, newZone);
            shippingZoneDAO.save(new ShippingZoneDTO(name, rank));
            return "New shipping zone created: " + newZone;
        }
    }

    // Helper method to find a zone by name
    private static Shipping_Zone findZoneByName(String name) throws SQLException {
        if (zoneMap.containsKey(name)) {
            return zoneMap.get(name);
        } else{
            Optional<ShippingZoneDTO> optional = shippingZoneDAO.findByName(name);
            if (optional.isPresent()) {
                ShippingZoneDTO newZone = optional.get();
                Shipping_Zone realZone = new Shipping_Zone(newZone.num(), newZone.name());
                zoneMap.put(realZone.getName(), realZone);
                return realZone;
            }
            return null; // Zone not found in table
        }
    }

    // Method to delete a shipping zone
    public String deleteZone(String name) throws SQLException {
        Shipping_Zone zone = findZoneByName(name);
        if (zone == null) {
            return "Zone with this name doesn't exist";
        } else {
            zoneMap.remove(name);
            shippingZoneDAO.deleteByName(name);
            return name + " deleted.";
        }
    }

    // Updates the rank of a shipping zone
    public String updateRank(String name, int rank) throws SQLException {
        Shipping_Zone zone = findZoneByName(name);
        if (zone == null) {
            return "Zone with this name doesn't exist"; // Zone not found
        } else {
            zone.setNum(rank); // Update zone rank
            return "Zone rank updated to " + rank; // Return confirmation message
        }
    }

    public String printZones() throws SQLException {
        StringBuilder sb = new StringBuilder();
        List<ShippingZoneDTO> list = shippingZoneDAO.findAll();
        for (ShippingZoneDTO dto : list){
            if (!zoneMap.containsKey(dto.name())){
                Shipping_Zone newZone = new Shipping_Zone(dto.num(), dto.name());
                zoneMap.put(dto.name(), newZone);
            }
        }
        for (String key : zoneMap.keySet()) {
            sb.append("Zone: ").append(key).append("\n"); // Append truck to result
        }
        // Remove the last newline character if the StringBuilder is not empty
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString(); // Return formatted string of addresses
    }
}
