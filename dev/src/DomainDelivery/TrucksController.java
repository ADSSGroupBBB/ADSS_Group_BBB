package DomainDelivery;

import Domain.Driver;

import java.util.List;

public class TrucksController  extends DeliveriesController{
    // Adds a new truck to the system if it doesn't already exist
    public String insertTruck(String id, int type, int truckWeight, int maxWeight) {
        if (truckWeight > maxWeight) {
            return "Error: Truck weight cannot exceed max weight."; // Check if weight is valid
        }
        if (!trucksMap.containsKey(id)) {
            DomainDelivery.Truck newTruck = new DomainDelivery.Truck(id, type, truckWeight, maxWeight); // Create a new truck object
            trucksMap.put(id, newTruck); // Add new truck to the map
            return "Truck added: " + id; // Return confirmation message
        } else {
            return "Truck with ID already exists."; // Truck already exists
        }
    }

    // Deletes a truck from the system by its ID
    public String deleteTruck(String id) {
        if (trucksMap.containsKey(id)) {
            trucksMap.remove(id); // Remove truck from the map
            return "Truck removed: " + id; // Return confirmation message
        } else {
            return "Truck with ID " + id + " doesn't exist."; // Truck not found
        }
    }

    // Marks a truck as available for driving
    public boolean isAvailableTruck(String id) {
        if (trucksMap.containsKey(id)) {
            trucksMap.get(id).setOnDrive(true); // Set truck to on drive
            return true; // Truck is available
        } else {
            return false; // Truck not found
        }
    }

    // Changes the truck for a route, considering the total weight of items
    public String changeTruck(List<Location> route, String truckID, String driverID) {
        DomainDelivery.Truck truck = trucksMap.get(truckID);
        truck.setOnDrive(false); // Mark current truck as not in use
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
    public String printTrucks() {
        StringBuilder sb = new StringBuilder();
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
