package Domain;

import DTO.*;
import DataAccess.DAO.DocumentDAOImpl;
import DataAccess.DAO.ShipmentItemDAOImpl;
import DataAccess.EmployeeDAO.DriverDAOImpl;
import DataAccess.EmployeeDAO.ShiftAssignmentDAOImpl;
import DataAccess.EmployeeInterface.DriverDAO;
import DataAccess.EmployeeInterface.ShiftAssignmentDAO;
import DataAccess.Interface.DocumentDAO;
import DataAccess.Interface.ShipmentItemDAO;
import Domain_employee.Driver;

import java.sql.SQLException;
import java.util.*;
import Domain.Location;
import static Domain.DriverController.employeeDAO;
import static Domain.LocationController.locationDao;
import static Domain.TrucksController.truckDAO;

public class DeliveriesController {

    protected static Map<String, Driver> driversMap = new HashMap<>();
    protected static Map<String, Truck> trucksMap = new HashMap<>();
    protected static Map<String, Integer> totalItemsMap = new HashMap<>();
    protected static Map<String, Shipping_Zone> zoneMap = new HashMap<>();
    protected static Map<String, Location> locationsMap = new HashMap<>();
    protected static Map<String, Document> documentsMap = new HashMap<>();
    private static final DocumentDAO documentDAO = new DocumentDAOImpl();
    private static final ShipmentItemDAO itemDAO = new ShipmentItemDAOImpl();
    protected final ShiftAssignmentDAO shiftASDAO = new ShiftAssignmentDAOImpl();
    protected static final DriverDAO driverDAO = new DriverDAOImpl();

    // Adds a destination location to a list
    public String addDestination(String address, List<Location> list, String shiftID) throws SQLException {
        Location new_dest = locationsMap.get(address);
        if (new_dest == null) {
            Optional<LocationDTO> optional = locationDao.findByAddress(address);
            if (optional.isPresent()) {
                LocationDTO dto = optional.get();
                Shipping_Zone getZone = zoneMap.get(dto.zone().name());
                new_dest = new Location(dto.contact_name(), dto.contact_num(), address, getZone); // Create a new location object
            }
            else{
                return "Error. Location unknown."; // Location not found
            }
        }
        if (!list.contains(new_dest)) {
            String skID = shiftASDAO.getAssignedEmployee(shiftID, "STORE_KEEPER");
            if (skID != null) {
                list.add(new_dest); // Add destination to list
                return "Success. Location added successfully to route. StoreKeeper of this location is " + skID; // Return success message
            }
            return "Error. Location is missing a STORE_KEEPER in this shift.";
        }
        return "Error. Location already in route."; // Destination already in list
    }

    // Calculates the total weight of items in the route
    public int weightRouteItems(String id, List<Location> list) throws WeightEx, SQLException {
        Truck truck = trucksMap.get(id);
        int curr_weight = truck.getTruck_weight(); // Get truck's current weight
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).getItems_required() != null) {
                for (Shipment_item item : list.get(i).getItems_required()) {
                    curr_weight += item.getWeight() * item.getAmount(); // Add item weights to total
                }
            }
        }
        if (curr_weight > truck.getMax_weight()) {
            throw new WeightEx(curr_weight); // Throw exception if weight exceeds max
        }
        truck.setCurr_weight(curr_weight); // Update truck's current weight
        TruckDTO newDto = new TruckDTO(id,truck.getType(),truck.getTruck_weight(), truck.getMax_weight(), truck.getCurrWeight(), true);
        truckDAO.save(newDto);
        return curr_weight; // Return total weight
    }

    // Sorts the route locations according to their zone ranks
    public void sortRouteAccordingToZones(List<Location> list) {
        if (list == null || list.size() <= 1) return; // Nothing to sort

        // Extract the sublist excluding the origin (first element)
        List<Location> subList = list.subList(1, list.size());

        // Sort the sublist based on the rank of the zone
        subList.sort(Comparator.comparingInt(loc -> loc.getZone().getNum()));
    }



    // Removes items from a route if the truck exceeds weight limits
    public String removeItems(List<Location> route, String truckID, String itemName, int itemAmount, String address, int totalWeight) throws SQLException {
        Truck truck = trucksMap.get(truckID);

        // Find the location in the route that matches the address (excluding origin at index 0)
        Location targetLocation = null;
        for (int i = 1; i < route.size(); i++) {
            if (route.get(i).getAddress().equals(address)) {
                targetLocation = route.get(i);
                break;
            }
        }

        if (targetLocation == null) {
            return "Address not found in route.";
        }

        List<Shipment_item> items = targetLocation.getItems_required();
        Shipment_item targetItem = null;
        for (Shipment_item item : items) {
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
            TruckDTO newDto = new TruckDTO(truckID,truck.getType(),truck.getTruck_weight(), truck.getMax_weight(), truck.getCurrWeight(), true);
            truckDAO.save(newDto);
            return "Items removed successfully.";
        }
        else{
            return "Weight after removal still higher than max weight of this truck.";
        }

    }

    // Returns a list of all items required in a route
    public List<Shipment_item> getTotalItems(List<Location> route) {
        List<Shipment_item> items = new ArrayList<>();
        for (int i = 1; i < route.size(); i++) {
            if (route.get(i).getItems_required() != null) {
                List<Shipment_item> loc_items = route.get(i).getItems_required();
                items.addAll(loc_items); // Add all items to the list
            }
        }
        return items; // Return all items in the route
    }

    // Adds a new document to the system
    public String addDocument(List<Shipment_item> items, String date, String truck_id,
                              String dep_hour, String driver_id, String dep_from, List<Location> destinations, String eventMessage) throws SQLException {
        int id = 0;
        id = documentDAO.getNextId();
        Document document = new Document(id, items, date, truck_id, dep_hour, driver_id, dep_from, destinations, eventMessage);
        documentsMap.put(document.getDocument_id(), document); // Add document to the map

        List<ShipmentItemDTO> itemDTOs = items.stream()
                .map(item -> new ShipmentItemDTO(item.getName(), item.getWeight(), item.getAmount()))
                .toList();

        List<LocationDTO> destinationDTOs = destinations.stream()
                .map(loc -> new LocationDTO(
                        loc.getAddress(),
                        loc.getContact_name(),
                        loc.getContact_num(),
                        new ShippingZoneDTO(loc.getZone().getName(), loc.getZone().getNum()) // Adjust if ShippingZoneDTO has different fields
                ))
                .toList();
        DocumentDTO dto = new DocumentDTO(document.getDocument_id(), itemDTOs, date, truck_id, dep_hour, driver_id, dep_from, destinationDTOs, eventMessage);
        documentDAO.save(dto);
        return document.getDocument_id(); // Return document ID
    }

    // Returns the origin address from the route
    public String getOriginAddressFromRoute(List<Location> route) {
        return route.get(0).getAddress(); // Get address of the first location
    }

    // Checks if an item exists in the system
    public boolean setItem(String itemName) throws SQLException {
        if (totalItemsMap.containsKey(itemName))
        {
            return true;
        }

        Optional<ShipmentItemDTO> optional = itemDAO.findByName(itemName);
        if (optional.isPresent()) {
            ShipmentItemDTO item = optional.get();
            totalItemsMap.put(itemName, item.weight());
            return true;
        }
            return false; // Truck not found in table

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
    public String printDocument(String doc_id) throws SQLException {
        if (documentsMap.get(doc_id) != null) {
            return documentsMap.get(doc_id).toString(); // Return document details
        } else {
            Optional<DocumentDTO> optional = documentDAO.findById(doc_id);
            if (optional.isPresent()) {
                DocumentDTO dto = optional.get();
                List<Shipment_item> items = dto.items().stream()
                        .map(itemDTO -> new Shipment_item(itemDTO.weight(), itemDTO.name()))
                        .toList();

                List<Location> destinations = dto.destinations().stream()
                        .map(locDto -> new Location(
                                locDto.address(),
                                locDto.contact_name(),
                                locDto.contact_num(),
                                new Shipping_Zone(locDto.zone().num(), locDto.zone().name())))
                        .toList();
                Document newDoc = new Document(documentDAO.getId(dto.docID()), items, dto.date(), dto.truck_id(), dto.dep_hour(), dto.driver_id(), dto.dep_from(), destinations, dto.eventMessage()); // Create a new truck object
                documentsMap.put(newDoc.getDocument_id(), newDoc);
                return newDoc.toString();
            }
            else {
                return "Document doesn't exist"; // Document not found
            }
        }
    }

    // Prints all document IDs in the system
    public String printDocIDS() throws SQLException {
        StringBuilder sb = new StringBuilder();
        List<DocumentDTO> list = documentDAO.findAll();
        for (DocumentDTO dto : list){
            if (!documentsMap.containsKey(dto.truck_id())){
                List<Shipment_item> items = dto.items().stream()
                        .map(itemDTO -> new Shipment_item(itemDTO.weight(), itemDTO.name()))
                        .toList();

                List<Location> destinations = dto.destinations().stream()
                        .map(locDto -> new Location(
                                locDto.address(),
                                locDto.contact_name(),
                                locDto.contact_num(),
                                new Shipping_Zone(locDto.zone().num(), locDto.zone().name())))
                        .toList();
                Document newDoc = new Document(documentDAO.getId(dto.docID()), items, dto.date(), dto.truck_id(), dto.dep_hour(), dto.driver_id(), dto.dep_from(), destinations, dto.eventMessage()); // Create a new truck object
                documentsMap.put(newDoc.getDocument_id(), newDoc);
            }
        }
        for (String key : documentsMap.keySet()) {
            sb.append("ID: ").append(key).append("\n"); // Append document IDs to result
        }
        return sb.toString(); // Return all document IDs
    }

    // Method to print all the route items
    public String printRouteItems(List<Location> route) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < route.size(); i++) {
            List<Shipment_item> loc_items = route.get(i).getItems_required();
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
    public String endDelivery(String doc_id) throws SQLException {
        Document document = documentsMap.get(doc_id);
        if (document == null) {
            Optional<DocumentDTO> optional = documentDAO.findById(doc_id);
            if (optional.isPresent()) {
                DocumentDTO dto = optional.get();
                List<Shipment_item> items = dto.items().stream()
                        .map(itemDTO -> new Shipment_item(itemDTO.weight(), itemDTO.name()))
                        .toList();

                List<Location> destinations = dto.destinations().stream()
                        .map(locDto -> new Location(
                                locDto.address(),
                                locDto.contact_name(),
                                locDto.contact_num(),
                                new Shipping_Zone(locDto.zone().num(), locDto.zone().name())))
                        .toList();
                Document newDoc = new Document(documentDAO.getId(dto.docID()), items, dto.date(), dto.truck_id(), dto.dep_hour(), dto.driver_id(), dto.dep_from(), destinations, dto.eventMessage()); // Create a new truck object
                documentsMap.put(newDoc.getDocument_id(), newDoc);
            } else {
                return "Delivery doesn't exist"; // Document not found
            }
        }

        String dID = document.getDriver_id();
        if (driversMap.containsKey(dID)) {
            Driver driver = driversMap.get(dID);
            driver.set_availabilityToDrive(true); // Mark driver as available
        }

        // Update all driver licenses in database to reflect available status
        List<DriverDTO> driverLicenses = driverDAO.findByDriverId(dID);
        for (DriverDTO driverLicense : driverLicenses) {
            DriverDTO updatedDriver = new DriverDTO(dID, driverLicense.license(), 0); // 0 means not on drive
            driverDAO.save(updatedDriver);
        }


        Truck truck;
        if (trucksMap.containsKey(document.getTruck_id())) {
            truck = trucksMap.get(document.getTruck_id());

        } else {
            Optional<TruckDTO> optionalTruck = truckDAO.findById(document.getTruck_id());
            if (optionalTruck.isPresent()) {
                TruckDTO truckDTO = optionalTruck.get();
                truck = new Truck(truckDTO.truck_id(), truckDTO.type(), truckDTO.truck_weight(), truckDTO.max_weight());
            } else {
                return "Truck doesn't exist"; // Document not found
            }
        }
        truck.setOnDrive(false); // Mark truck not in use
        truck.setCurr_weight(truck.getTruck_weight()); // Reset truck weight
        for (Location loc : document.getDestinations()) {
            loc.setItems_required(null); // All needs fulfilled
        }
        TruckDTO newDto = new TruckDTO(truck.getTruck_id(),truck.getType(),truck.getTruck_weight(), truck.getMax_weight(), truck.getCurrWeight(), false);
        truckDAO.save(newDto);
        document.setEventMessage("Delivery finished."); // Set event message

        return "Delivery ended successfully.\nDriver is now available for another delivery.\nTruck is now available for another delivery."; // Return success message
    }

    // Prints all items in the system
    public String printItems() throws SQLException {
        StringBuilder sb = new StringBuilder();
        List<ShipmentItemDTO> list = itemDAO.findAll(); // You'll need to add this line at the top with other DAOs

        for (ShipmentItemDTO dto : list) {
            if (!totalItemsMap.containsKey(dto.name())) {
                totalItemsMap.put(dto.name(), dto.weight()); // Add item to map if not already present
            }
        }

        for (String itemName : totalItemsMap.keySet()) {
            sb.append("Item: ").append(itemName)
                    .append(", Weight: ").append(totalItemsMap.get(itemName))
                    .append("\n"); // Append item details to result
        }

        // Remove the last newline character if the StringBuilder is not empty
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString(); // Return formatted string of items
    }
}