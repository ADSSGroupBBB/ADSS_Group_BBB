package Service;
import Domain.*;

import java.util.List;

public class UserApplication {
    // Create an instance of the UserController class
    UserController uc = new UserController();

    // Method to insert a new driver
    public String insertDriver(String id, String name, List<Integer> licenseList) {
        return uc.insertDriver(id, name, licenseList); // Delegate to UserController to insert the driver
    }

    // Method to delete a driver by ID
    public String deleteDriver(String id) {
        return uc.deleteDriver(id); // Delegate to UserController to delete the driver
    }

    // Method to delete a specific license for a driver
    public String deleteLicense(String id, int license) {
        return uc.deleteLicense(id, license); // Delegate to UserController to delete the license
    }

    // Method to add a new license to a driver
    public String addLicense(String id, int license) {
        return uc.addLicense(id, license); // Delegate to UserController to add the license
    }

    // Method to add a new shipping zone
    public String addShippingZone(int id, String name) {
        return uc.addShippingZone(id, name); // Delegate to UserController to add the shipping zone
    }

    // Method to delete a shipping zone by name
    public String deleteZone(String name) {
        return uc.deleteZone(name); // Delegate to UserController to delete the zone
    }

    // Method to update the rank of a shipping zone
    public String updateRank(String name, int rank) {
        return uc.updateRank(name, rank); // Delegate to UserController to update the rank
    }

    // Method to insert a new truck
    public String insertTruck(String id, int type, int truckWeight, int maxWeight) {
        return uc.insertTruck(id, type, truckWeight, maxWeight); // Delegate to UserController to insert the truck
    }

    // Method to delete a truck by ID
    public String deleteTruck(String id) {
        return uc.deleteTruck(id); // Delegate to UserController to delete the truck
    }

    // Method to check if a truck is available
    public boolean isAvailableTruck(String id) {
        return uc.isAvailableTruck(id); // Delegate to UserController to check truck availability
    }

    // Method to check if a driver is available for a specific truck
    public String isAvailableDriver(String id, String truckID) {
        return uc.isAvailableDriver(id, truckID); // Delegate to UserController to check driver availability
    }

    // Method to insert a new location
    public String insertLocation(String contactName, String contactNum, String address, String zone) {
        return uc.insertLocation(contactName, contactNum, address, zone); // Delegate to UserController to insert the location
    }

    // Method to delete a location by address
    public String deleteLocation(String address) {
        return uc.deleteLocation(address); // Delegate to UserController to delete the location
    }

    // Method to print all locations
    public String printLocations() {
        return uc.printLocations(); // Delegate to UserController to print all locations
    }

    // Method to add a destination to a route
    public String addDestination(String address, List<Location> list) {
        return uc.addDestination(address, list); // Delegate to UserController to add destination to route
    }

    // Method to calculate and return the total weight of items in the route
    public int weightRouteItems(String id, List<Location> list) throws WeightEx {
        return uc.weightRouteItems(id, list); // Delegate to UserController to calculate weight
    }

    // Method to sort the route according to shipment zones
    public void sortRouteAccordingToZones(List<Location> list) {
        uc.sortRouteAccordingToZones(list); // Delegate to UserController to sort the route
    }

    // Method to change the truck for a route
    public String changeTruck(List<Location> route, String truckID, String driverID) {
        return uc.changeTruck(route, truckID, driverID); // Delegate to UserController to change the truck
    }

    // Method to remove items from the route based on truck capacity
    public void removeItems(List<Location> route, String truckID, int total_weight) {
        uc.removeItems(route, truckID, total_weight); // Delegate to UserController to remove items from the route
    }

    // Method to get the total items in a route
    public List<Shipment_item> getTotalItems(List<Location> route) {
        return uc.getTotalItems(route); // Delegate to UserController to retrieve total items in route
    }

    // Method to add a shipment document to the system
    public String addDocument(List<Shipment_item> items, String date, String truck_id, String dep_hour,
                              String driver_id, String dep_from, List<Location> destinations, String eventMessage) {
        return uc.addDocument(items, date, truck_id, dep_hour, driver_id, dep_from, destinations, eventMessage);
        // Delegate to UserController to add the document
    }

    // Method to get the origin address from a route
    public String getOriginAddressFromRoute(List<Location> route) {
        return uc.getOriginAddressFromRoute(route); // Delegate to UserController to get the origin address
    }

    // Method to set an item (likely for shipment or tracking purposes)
    public boolean setItem(String itemName) {
        return uc.setItem(itemName); // Delegate to UserController to set an item
    }

    // Method to set the required amount of an item in a route
    public String setRequiredItemInRoute(List<Location> route, String itemName, int amount) {
        return uc.setRequiredItemInRoute(route, itemName, amount); // Delegate to UserController to set required item
    }

    // Method to print a specific shipment document by ID
    public String printDocument(String doc_id) {
        return uc.printDocument(doc_id); // Delegate to UserController to print the document
    }

    // Method to print all document IDs
    public String printDocIDS() {
        return uc.printDocIDS(); // Delegate to UserController to print all document IDs
    }

    // Method to end the delivery and finalize the document
    public String endDelivery(String doc_id) {
        return uc.endDelivery(doc_id); // Delegate to UserController to end the delivery and finalize the document
    }
    public void initBaseData(){
        UserController.initBaseData();
    }
}
