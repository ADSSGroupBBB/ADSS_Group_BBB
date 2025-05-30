package DomainDelivery;

import java.util.*;

public class DeliveriesController {

    protected static Map<String, Domain.Driver> driversMap = new HashMap<>();
    protected static Map<String, Truck> trucksMap = new HashMap<>();
    protected static Map<String, Integer> totalItemsMap = new HashMap<>();
    protected static Map<String, DomainDelivery.Shipping_Zone> zoneMap = new HashMap<>();
    protected static Map<String, DomainDelivery.Location> locationsMap = new HashMap<>();
    protected static Map<String, Document> documentsMap = new HashMap<>();

    // Initialize base data for drivers, trucks, zones, locations, etc.
    public static void initBaseData() {
        // Create some sample shipment items
        DomainDelivery.Shipment_item item1 = new DomainDelivery.Shipment_item(10, "Milk");
        DomainDelivery.Shipment_item item2 = new DomainDelivery.Shipment_item(5, "Bread");
        DomainDelivery.Shipment_item item3 = new DomainDelivery.Shipment_item(20, "Juice");
        DomainDelivery.Shipment_item item4 = new DomainDelivery.Shipment_item(2, "Eggs");
        DomainDelivery.Shipment_item item5 = new DomainDelivery.Shipment_item(15, "Meat");

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

        Domain.Driver driver1 = new Domain.Driver("D1", "Avi Cohen", licenses1);
        Domain.Driver driver2 = new Domain.Driver("D2", "Sarah Levi", licenses2);
        Domain.Driver driver3 = new Domain.Driver("D3", "Dan Ron", licenses3);
        Domain.Driver driver4 = new Domain.Driver("D4", "Muhammad Younes", licenses4);
        Domain.Driver driver5 = new Domain.Driver("D5", "Lionel Messi", licenses5);

        driversMap.put(driver1.getDriver_id(), driver1);
        driversMap.put(driver2.getDriver_id(), driver2);
        driversMap.put(driver3.getDriver_id(), driver3);
        driversMap.put(driver4.getDriver_id(), driver4);
        driversMap.put(driver5.getDriver_id(), driver5);

        // Create shipping zones and add them to the zoneMap
        DomainDelivery.Shipping_Zone zone1 = new DomainDelivery.Shipping_Zone(1, "Downtown");
        DomainDelivery.Shipping_Zone zone2 = new DomainDelivery.Shipping_Zone(2, "Airport");
        DomainDelivery.Shipping_Zone zone3 = new DomainDelivery.Shipping_Zone(3, "Suburbs");
        DomainDelivery.Shipping_Zone zone4 = new DomainDelivery.Shipping_Zone(4, "Industrial Park");
        DomainDelivery.Shipping_Zone zone5 = new DomainDelivery.Shipping_Zone(5, "Shopping District");

        zoneMap.put(zone1.getName(), zone1);
        zoneMap.put(zone2.getName(), zone2);
        zoneMap.put(zone3.getName(), zone3);
        zoneMap.put(zone4.getName(), zone4);
        zoneMap.put(zone5.getName(), zone5);

        // Create sample locations with items required and add them to the locationsMap
        List<DomainDelivery.Shipment_item> items1 = new ArrayList<>(Arrays.asList(item1, item2));
        List<DomainDelivery.Shipment_item> items2 = new ArrayList<>(Arrays.asList(item3, item4));
        List<DomainDelivery.Shipment_item> items3 = new ArrayList<>(Arrays.asList(item5, item3));
        List<DomainDelivery.Shipment_item> items4 = new ArrayList<>(Arrays.asList(item1, item5));
        List<DomainDelivery.Shipment_item> items5 = new ArrayList<>(Arrays.asList(item2, item4));

        DomainDelivery.Location location1 = new DomainDelivery.Location("Omer", "555-1234", "Rager", zone1);
        DomainDelivery.Location location2 = new DomainDelivery.Location("Ben", "555-5678", "Hazaz", zone2);
        DomainDelivery.Location location3 = new DomainDelivery.Location("Lior", "555-9012", "Metzada", zone3);
        DomainDelivery.Location location4 = new DomainDelivery.Location("Meir", "555-3456", "Bialik", zone4);
        DomainDelivery.Location location5 = new DomainDelivery.Location("Ariel", "555-7890", "Bengurion", zone5);
        DomainDelivery.Location location6 = new DomainDelivery.Location("Noam", "123-456", "Headquarters", zone1);

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

    // Adds a new location to the system
    public String insertLocation(String contactName, String contactNum, String address, String input_zone) {
        if (locationsMap.containsKey(address)) {
            return "A location with this address already exists."; // Check if address exists
        }
        Shipping_Zone zone = zoneMap.get(input_zone);
        if (zone == null) {
            return "Zone doesn't exist. Add it before adding new location."; // Zone not found
        }
        DomainDelivery.Location newLocation = new DomainDelivery.Location(contactName, contactNum, address, zone); // Create new location object
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

    // Adds a destination location to a list
    public String addDestination(String address, List<DomainDelivery.Location> list) {
        DomainDelivery.Location new_dest = locationsMap.get(address);
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
    public int weightRouteItems(String id, List<DomainDelivery.Location> list) throws WeightEx {
        Truck truck = trucksMap.get(id);
        int curr_weight = truck.getTruck_weight(); // Get truck's current weight
        for (int i = 1; i < list.size(); i++) {
            for (DomainDelivery.Shipment_item item : list.get(i).getItems_required()) {
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
    public void sortRouteAccordingToZones(List<DomainDelivery.Location> list) {
        if (list == null || list.size() <= 1) return; // Nothing to sort

        // Extract the sublist excluding the origin (first element)
        List<DomainDelivery.Location> subList = list.subList(1, list.size());

        // Sort the sublist based on the rank of the zone
        Collections.sort(subList, Comparator.comparingInt(loc -> loc.getZone().getNum()));
    }



    // Removes items from a route if the truck exceeds weight limits
    public String removeItems(List<DomainDelivery.Location> route, String truckID, String itemName, int itemAmount, String address, int totalWeight) {
        Truck truck = trucksMap.get(truckID);
        if (truck == null) {
            return "Truck ID not found.";
        }

        // Find the location in the route that matches the address (excluding origin at index 0)
        DomainDelivery.Location targetLocation = null;
        for (int i = 1; i < route.size(); i++) {
            if (route.get(i).getAddress().equals(address)) {
                targetLocation = route.get(i);
                break;
            }
        }

        if (targetLocation == null) {
            return "Address not found in route.";
        }

        List<DomainDelivery.Shipment_item> items = targetLocation.getItems_required();
        DomainDelivery.Shipment_item targetItem = null;
        for (DomainDelivery.Shipment_item item : items) {
            if (item.getName().equals(itemName)) {
                targetItem = item;
                break;
            }
        }

        if (targetItem == null) {
            return "Item not found at the given address.";
        }

        if (itemAmount > targetItem.getAmount()) {
            return "Requested amount exceeds available amount.";
        }

        // Calculate weight to reduce and update truck weight
        int weightPerItem = targetItem.getWeight();
        int weightToRemove = weightPerItem * itemAmount;
        int newTruckWeight = totalWeight - weightToRemove;

        // Update item amount or remove it entirely if amount becomes zero
        int remainingAmount = targetItem.getAmount() - itemAmount;
        if (remainingAmount == 0) {
            items.remove(targetItem);
        } else {
            targetItem.setAmount(remainingAmount);
        }
        if (truck.getMax_weight() >= newTruckWeight ){
            truck.setCurr_weight(newTruckWeight);
            return "Weight after removal still higher than max weight of this truck.";
        }
        else{return "Items removed successfully.";}

    }

    // Returns a list of all items required in a route
    public List<DomainDelivery.Shipment_item> getTotalItems(List<DomainDelivery.Location> route) {
        List<DomainDelivery.Shipment_item> items = new ArrayList<>();
        for (int i = 1; i < route.size(); i++) {
            List<DomainDelivery.Shipment_item> loc_items = route.get(i).getItems_required();
            items.addAll(loc_items); // Add all items to the list
        }
        return items; // Return all items in the route
    }

    // Adds a new document to the system
    public String addDocument(List<DomainDelivery.Shipment_item> items, String date, String truck_id,
                              String dep_hour, String driver_id, String dep_from, List<DomainDelivery.Location> destinations, String eventMessage) {
        Document document = new Document(items, date, truck_id, dep_hour, driver_id, dep_from, destinations, eventMessage);
        documentsMap.put(document.getDocument_id(), document); // Add document to the map
        return document.getDocument_id(); // Return document ID
    }

    // Returns the origin address from the route
    public String getOriginAddressFromRoute(List<DomainDelivery.Location> route) {
        return route.getFirst().getAddress(); // Get address of the first location
    }

    // Checks if an item exists in the system
    public boolean setItem(String itemName) {
        return totalItemsMap.containsKey(itemName); // Check if item exists in map
    }

    // Sets the required item in the route if not already present
    public String setRequiredItemInRoute(List<DomainDelivery.Location> route, String itemName, int amount) {
        List<DomainDelivery.Shipment_item> loc_items;
        boolean found;
        DomainDelivery.Shipment_item new_item;
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
                new_item = new DomainDelivery.Shipment_item(totalItemsMap.get(itemName), itemName); // Create new item
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

    // Method to print all the route items
    public String printRouteItems(List<DomainDelivery.Location> route) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < route.size(); i++) {
            List<DomainDelivery.Shipment_item> loc_items = route.get(i).getItems_required();
            sb.append(route.get(i).getAddress()).append(":\n");
            for (Shipment_item item : loc_items){
                sb.append("Item: ").append(item.getName()).append(" Amount: ").append(item.getAmount()).append(" Weight" +
                        " per one: ").append(item.getWeight()).append("\n");
            }
        }
        // Remove the last newline character if the StringBuilder is not empty
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString(); // Return formatted string of addresses
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
