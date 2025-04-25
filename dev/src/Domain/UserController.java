package Domain;

import java.util.*;

public class UserController {

    private static Map<String, Driver> driversMap = new HashMap<>();
    private static Map<String, Truck> trucksMap = new HashMap<>();
    private static Map<String, Integer> totalItemsMap = new HashMap<>();
    private static Map<String, Shipping_Zone> zoneMap = new HashMap<>();
    private static Map<String, Location> locationsMap = new HashMap<>();
    private static Map<String, Document> documentsMap = new HashMap<>();

    // Initialize base data for drivers, trucks, zones, locations, etc.
    public static void initBaseData() {
        // Create some sample shipment items
        Shipment_item item1 = new Shipment_item(10, "Milk");
        Shipment_item item2 = new Shipment_item(5, "Bread");
        Shipment_item item3 = new Shipment_item(20, "Juice");
        Shipment_item item4 = new Shipment_item(2, "Eggs");
        Shipment_item item5 = new Shipment_item(15, "Meat");

        // Add items to the totalItemsMap
        totalItemsMap.put(item1.getName(), item1.getWeight());
        totalItemsMap.put(item2.getName(), item2.getWeight());
        totalItemsMap.put(item3.getName(), item3.getWeight());
        totalItemsMap.put(item4.getName(), item4.getWeight());
        totalItemsMap.put(item5.getName(), item5.getWeight());

        // Create trucks and add them to the trucksMap
        Truck truck1 = new Truck("T1", 111, 1, 5);
        Truck truck2 = new Truck("T2", 222, 10000, 20000);
        Truck truck3 = new Truck("T3", 333, 10000, 19000);
        Truck truck4 = new Truck("T4", 111, 10000, 16000);
        Truck truck5 = new Truck("T5", 222, 10000, 30000);

        trucksMap.put(truck1.getTruck_id(), truck1);
        trucksMap.put(truck2.getTruck_id(), truck2);
        trucksMap.put(truck3.getTruck_id(), truck3);
        trucksMap.put(truck4.getTruck_id(), truck4);
        trucksMap.put(truck5.getTruck_id(), truck5);

        // Create drivers and add them to the driversMap
        List<Integer> licenses1 = new ArrayList<>(Arrays.asList(111, 222, 333));
        List<Integer> licenses2 = new ArrayList<>(Arrays.asList(111, 222, 333));
        List<Integer> licenses3 = new ArrayList<>(Arrays.asList(222, 333));
        List<Integer> licenses4 = new ArrayList<>(Arrays.asList(111, 444));
        List<Integer> licenses5 = new ArrayList<>(Arrays.asList(111, 222));

        Driver driver1 = new Driver("D1", "Avi Cohen", licenses1);
        Driver driver2 = new Driver("D2", "Sarah Levi", licenses2);
        Driver driver3 = new Driver("D3", "Dan Ron", licenses3);
        Driver driver4 = new Driver("D4", "Muhammad Younes", licenses4);
        Driver driver5 = new Driver("D5", "Lionel Messi", licenses5);

        driversMap.put(driver1.getDriver_id(), driver1);
        driversMap.put(driver2.getDriver_id(), driver2);
        driversMap.put(driver3.getDriver_id(), driver3);
        driversMap.put(driver4.getDriver_id(), driver4);
        driversMap.put(driver5.getDriver_id(), driver5);

        // Create shipping zones and add them to the zoneMap
        Shipping_Zone zone1 = new Shipping_Zone(1, "Downtown");
        Shipping_Zone zone2 = new Shipping_Zone(2, "Airport");
        Shipping_Zone zone3 = new Shipping_Zone(3, "Suburbs");
        Shipping_Zone zone4 = new Shipping_Zone(4, "Industrial Park");
        Shipping_Zone zone5 = new Shipping_Zone(5, "Shopping District");

        zoneMap.put(zone1.getName(), zone1);
        zoneMap.put(zone2.getName(), zone2);
        zoneMap.put(zone3.getName(), zone3);
        zoneMap.put(zone4.getName(), zone4);
        zoneMap.put(zone5.getName(), zone5);

        // Create sample locations with items required and add them to the locationsMap
        List<Shipment_item> items1 = new ArrayList<>(Arrays.asList(item1, item2));
        List<Shipment_item> items2 = new ArrayList<>(Arrays.asList(item3, item4));
        List<Shipment_item> items3 = new ArrayList<>(Arrays.asList(item5, item3));
        List<Shipment_item> items4 = new ArrayList<>(Arrays.asList(item1, item5));
        List<Shipment_item> items5 = new ArrayList<>(Arrays.asList(item2, item4));

        Location location1 = new Location("Omer", "555-1234", "Haim Hazaz 123", zone1);
        Location location2 = new Location("Ben", "555-5678", "Rager 12", zone2);
        Location location3 = new Location("Lior", "555-9012", "Derech Metzada 17", zone3);
        Location location4 = new Location("Meir", "555-3456", "Bialik 19", zone4);
        Location location5 = new Location("Ariel", "555-7890", "BenGurion 1", zone5);
        Location location6 = new Location("Noam", "123-456", "Headquarters", zone1);

        location1.setItems_required(items1);
        location2.setItems_required(items2);
        location3.setItems_required(items3);
        location4.setItems_required(items4);
        location5.setItems_required(items5);
        location6.setItems_required(items1);

        locationsMap.put(location1.getAddress(), location1);
        locationsMap.put(location2.getAddress(), location2);
        locationsMap.put(location3.getAddress(), location3);
        locationsMap.put(location4.getAddress(), location4);
        locationsMap.put(location5.getAddress(), location5);
        locationsMap.put(location6.getAddress(), location6);

        documentsMap = new HashMap<>();
    }

    // Method to insert a new driver
    public String insertDriver(String id, String name, List<Integer> licenseList) {
        Driver new_d = new Driver(id, name, licenseList);

        // Check if the driver already exists
        if (!driversMap.containsKey(id)) {
            driversMap.put(id, new_d);
            return "New driver added: " + new_d.toString();
        } else {
            return "Driver with the same ID already exist.";
        }
    }

    // Method to delete a driver
    public String deleteDriver(String id) {
        if (driversMap.containsKey(id)) {
            Driver removed = driversMap.remove(id);
            return "Driver removed: " + removed;
        } else {
            return "Driver with ID " + id + " doesn't exist.";
        }
    }

    // Method to delete a license from a driver
    public String deleteLicense(String id, int license) {
        Driver driver = driversMap.get(id);
        if (driver == null) {
            return "No driver found with ID: " + id;
        }
        List<Integer> licenses = driver.getLicenses_list();

        if (licenses.contains(license)) {
            Integer licenseObj = license;
            licenses.remove(licenseObj); // Removes the license from the list
            return "License removed successfully.";
        } else {
            return "License not found.";
        }
    }

    // Method to add a license to a driver
    public String addLicense(String id, int license) {
        Driver driver = driversMap.get(id);
        if (driver == null) {
            return "No driver found with ID: " + id;
        }
        List<Integer> licenses = driver.getLicenses_list();
        if (licenses.contains(license)) {
            return "License already exist.";
        } else {
            licenses.add(license);
            return "License added successfully.";
        }
    }

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

    // Adds a new truck to the system if it doesn't already exist
    public String insertTruck(String id, int type, int truckWeight, int maxWeight) {
        if (truckWeight > maxWeight) {
            return "Error: Truck weight cannot exceed max weight."; // Check if weight is valid
        }
        if (!trucksMap.containsKey(id)) {
            Truck newTruck = new Truck(id, type, truckWeight, maxWeight); // Create a new truck object
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

    // Checks if a driver is available for a specific truck
    public String isAvailableDriver(String id, String truckID) {
        if (driversMap.containsKey(id)) {
            Driver driver = driversMap.get(id);
            if (driver.is_available()) {
                Truck truck = trucksMap.get(truckID);
                if (driver.getLicenses_list().contains(truck.getType())) {
                    driver.set_availability(false); // Mark driver as unavailable
                    return "Driver is available."; // Return success message
                }
                return "Driver doesn't have correct license for this truck type"; // Incorrect license
            } else {
                return "Driver is not available for a new delivery"; // Driver unavailable
            }
        } else {
            return "Driver doesn't exist"; // Driver not found
        }
    }

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

    // Adds a destination location to a list
    public String addDestination(String address, List<Location> list) {
        Location new_dest = locationsMap.get(address);
        if (new_dest == null) {
            return "Location unknown."; // Location not found
        }
        if (!list.contains(new_dest)) {
            list.add(new_dest); // Add destination to list
            return "Location added successfully."; // Return success message
        }
        return "Location already in route."; // Destination already in list
    }

    // Calculates the total weight of items in the route
    public int weightRouteItems(String id, List<Location> list) throws WeightEx {
        Truck truck = trucksMap.get(id);
        int curr_weight = truck.getTruck_weight(); // Get truck's current weight
        for (int i = 1; i < list.size(); i++) {
            for (Shipment_item item : list.get(i).getItems_required()) {
                curr_weight += item.getWeight() * item.getAmount(); // Add item weights to total
            }
        }
        if (curr_weight > truck.getMax_weight()) {
            throw new WeightEx(curr_weight); // Throw exception if weight exceeds max
        }
        truck.setCurr_weight(curr_weight); // Update truck's current weight
        return curr_weight; // Return total weight
    }

    // Sorts the route locations according to their zone ranks
    public void sortRouteAccordingToZones(List<Location> list) {
        if (list == null || list.size() <= 1) return; // Nothing to sort

        // Extract the sublist excluding the origin (first element)
        List<Location> subList = list.subList(1, list.size());

        // Sort the sublist based on the rank of the zone
        Collections.sort(subList, Comparator.comparingInt(loc -> loc.getZone().getNum()));
    }

    // Changes the truck for a route, considering the total weight of items
    public String changeTruck(List<Location> route, String truckID, String driverID) {
        Truck truck = trucksMap.get(truckID);
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

    // Removes items from a route if the truck exceeds weight limits
    public void removeItems(List<Location> route, String truckID, int total_weight) {
        Truck truck = trucksMap.get(truckID);
        int max = truck.getMax_weight(); // Get truck's max weight capacity
        for (int i = 1; i < route.size(); i++) {
            List<Shipment_item> items = route.get(i).getItems_required();
            for (int j = 0; j < items.size() && total_weight > max; j++) {
                Shipment_item item = items.get(j);
                int itemWeight = item.getWeight();
                int itemAmount = item.getAmount();

                while (itemAmount > 0 && total_weight > max) {
                    itemAmount--; // Remove one item
                    total_weight -= itemWeight; // Reduce total weight
                }

                item.setAmount(itemAmount); // Update item amount after removal

                // Remove item entirely if amount is zero
                if (itemAmount == 0) {
                    items.remove(j);
                    j--; // Adjust index after removal
                }
            }
        }
    }

    // Returns a list of all items required in a route
    public List<Shipment_item> getTotalItems(List<Location> route) {
        List<Shipment_item> items = new ArrayList<>();
        for (int i = 1; i < route.size(); i++) {
            List<Shipment_item> loc_items = route.get(i).getItems_required();
            items.addAll(loc_items); // Add all items to the list
        }
        return items; // Return all items in the route
    }

    // Adds a new document to the system
    public String addDocument(List<Shipment_item> items, String date, String truck_id,
                              String dep_hour, String driver_id, String dep_from, List<Location> destinations, String eventMessage) {
        Document document = new Document(items, date, truck_id, dep_hour, driver_id, dep_from, destinations, eventMessage);
        documentsMap.put(document.getDocument_id(), document); // Add document to the map
        return document.getDocument_id(); // Return document ID
    }

    // Returns the origin address from the route
    public String getOriginAddressFromRoute(List<Location> route) {
        return route.getFirst().getAddress(); // Get address of the first location
    }

    // Checks if an item exists in the system
    public boolean setItem(String itemName) {
        return totalItemsMap.containsKey(itemName); // Check if item exists in map
    }

    // Sets the required item in the route if not already present
    public String setRequiredItemInRoute(List<Location> route, String itemName, int amount) {
        List<Shipment_item> loc_items;
        boolean found;
        Shipment_item new_item;
        for (int i = 1; i < route.size(); i++) {
            found = false;
            loc_items = route.get(i).getItems_required();
            if (loc_items != null) {
                for (int j = 0; j < loc_items.size(); j++) {
                    if (Objects.equals(loc_items.get(j).getName(), itemName)) {
                        loc_items.get(j).setAmount(amount + loc_items.get(j).getAmount()); // Update item amount
                        found = true;
                    }
                }
            } else {
                loc_items = new ArrayList<>();
            }
            if (!found) {
                new_item = new Shipment_item(totalItemsMap.get(itemName), itemName); // Create new item
                new_item.setAmount(amount); // Set item amount
                loc_items.add(new_item); // Add item to location
                route.get(i).setItems_required(loc_items); // Update location with new item
            }
        }
        return "Added " + amount + " of " + itemName + " for every location in route."; // Return success message
    }

    // Prints the details of a specific document by ID
    public String printDocument(String doc_id) {
        if (documentsMap.get(doc_id) != null) {
            return documentsMap.get(doc_id).toString(); // Return document details
        } else {
            return "Document doesn't exist"; // Document not found
        }
    }

    // Prints all document IDs in the system
    public String printDocIDS() {
        StringBuilder sb = new StringBuilder();
        for (String key : documentsMap.keySet()) {
            sb.append("ID: ").append(key).append("\n"); // Append document IDs to result
        }
        return sb.toString(); // Return all document IDs
    }

    // Ends a delivery, marking the truck and driver as available
    public String endDelivery(String doc_id) {
        Document document = documentsMap.get(doc_id);
        if (document != null) {
            driversMap.get(document.getDriver_id()).set_availability(true); // Mark driver available
            Truck truck = trucksMap.get(document.getTruck_id());
            truck.setOnDrive(false); // Mark truck not in use
            truck.setCurr_weight(truck.getTruck_weight()); // Reset truck weight
            for (Location loc : document.getDestinations()) {
                loc.setItems_required(null); // All needs fulfilled
            }
            document.setEventMessage("Delivery finished."); // Set event message

            return "Delivery ended successfully.\nDriver is now available for another delivery.\nTruck is now available for another delivery."; // Return success message
        } else {
            return "Delivery doesn't exist"; // Document not found
        }
    }
}
