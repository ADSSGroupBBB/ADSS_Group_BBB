package Domain;

import DTO.TruckDTO;
import DataAccess.DAO.TruckDAOImpl;
import DataAccess.Interface.TruckDAO;
import Domain_employee.Driver;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TrucksController  extends DeliveriesController{
    protected static final TruckDAO truckDAO = new TruckDAOImpl();

    // Adds a new truck to the system if it doesn't already exist
    public String insertTruck(String id, int type, int truckWeight, int maxWeight) throws SQLException {
        if (truckWeight > maxWeight) {
            return "Error: Truck weight cannot exceed max weight."; // Check if weight is valid
        }
        if (!trucksMap.containsKey(id)) {
            Optional<TruckDTO> optionalTruck = truckDAO.findById(id);
            if (optionalTruck.isPresent()) {
                return "Truck with ID already exists."; // Truck already exists
            }
            Truck newTruck = new Truck(id, type, truckWeight, maxWeight); // Create a new truck object
            trucksMap.put(id, newTruck); // Add new truck to the map
            TruckDTO newDto = new TruckDTO(id,type,truckWeight, maxWeight, truckWeight, false);
            truckDAO.save(newDto);
            return "Truck added: " + id; // Return confirmation message
        } else {
            return "Truck with ID already exists."; // Truck already exists
        }
    }

    // Deletes a truck from the system by its ID
    public String deleteTruck(String id) throws SQLException {
        if (trucksMap.containsKey(id)) {
            trucksMap.remove(id); // Remove truck from the map
            truckDAO.deleteById(id);
            return "Truck removed: " + id; // Return confirmation message
        } else {
            if (truckDAO.deleteById(id)) {
                return "Truck removed: " + id; // Return confirmation message
            }
            return "Truck with ID " + id + " doesn't exist."; // Truck not found
        }
    }

    // Marks a truck as available for driving
    public boolean isAvailableTruck(String id) throws SQLException {
        if (trucksMap.containsKey(id)) {
            if (trucksMap.get(id).getOnDrive()){
                return false;
            }
            trucksMap.get(id).setOnDrive(true); // Set truck to on drive
            return true; // Truck is available
        } else{
            Optional<TruckDTO> optionalTruck = truckDAO.findById(id);
            if (optionalTruck.isPresent()) {
                TruckDTO truck = optionalTruck.get();
                Truck newTruck = new Truck(truck.truck_id(), truck.type(), truck.truck_weight(), truck.max_weight());
                newTruck.setOnDrive(truck.onDrive());
                trucksMap.put(truck.truck_id(), newTruck);
                if (truck.onDrive()){
                    newTruck.setOnDrive(true);
                    return false;
                }
                return true;
            }
            return false; // Truck not found in table
        }
    }

    // Changes the truck for a route, considering the total weight of items
    public String changeTruck(List<Location> route, String truckID, String driverID) throws SQLException {
        Truck truck = trucksMap.get(truckID);
        truck.setOnDrive(false);
        truck.setCurr_weight(truck.getTruck_weight());// Mark current truck as not in use
        truckDAO.save(new TruckDTO(truck.getTruck_id(), truck.getType(), truck.getTruck_weight(), truck.getMax_weight(), truck.getCurrWeight(), truck.getOnDrive()));
        int total_weight = 0;
        Driver driver = driversMap.get(driverID);
        for (int i = 1; i < route.size(); i++) {
            for (Shipment_item item : route.get(i).getItems_required()) {
                total_weight += item.getWeight() * item.getAmount(); // Calculate total weight
            }
        }
        for (Truck t : trucksMap.values()) {
            if (!t.getOnDrive() && driver.getLicenses_list().contains(t.getType())) {
                int availableCapacity = t.getMax_weight() - t.getTruck_weight();
                if (total_weight <= availableCapacity) {
                    try {
                        t.addItem(total_weight); // Add items to the truck
                        t.setOnDrive(true); // Set truck as in use
                        truckDAO.save(new TruckDTO(t.getTruck_id(), t.getType(), t.getTruck_weight(), t.getMax_weight(), t.getCurrWeight(), t.getOnDrive()));
                        return t.getTruck_id(); // Return new truck ID
                    } catch (WeightEx e) {
                        // Continue to next truck if exception occurs
                    }
                }
            }
        }
        return null; // No suitable truck found
    }

    // Prints all trucks id in the system
    public String printTrucks() throws SQLException {
        StringBuilder sb = new StringBuilder();
        List<TruckDTO> list = truckDAO.findAll();
        for (TruckDTO dto : list){
            if (!trucksMap.containsKey(dto.truck_id())){
                Truck newTruck = new Truck(dto.truck_id(), dto.type(), dto.truck_weight(), dto.max_weight()); // Create a new truck object
                trucksMap.put(newTruck.getTruck_id(), newTruck);
            }
        }
        for (String key : trucksMap.keySet()) {
            sb.append("Truck: ").append(key).append("\n"); // Append truck to result
        }
        // Remove the last newline character if the StringBuilder is not empty
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString(); // Return formatted string of addresses
    }
}