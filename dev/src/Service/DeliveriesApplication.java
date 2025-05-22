package Service;

import Domain.DeliveriesController;
import Domain.Location;
import Domain.Shipment_item;
import Domain.WeightEx;

import java.util.List;

public class DeliveriesApplication {
    private static DeliveriesController dc = new DeliveriesController();


    // Method to add a destination to a route
    public String addDestination(String address, List<Location> list) {
        return dc.addDestination(address, list); // Delegate to UserController to add destination to route
    }

    // Method to calculate and return the total weight of items in the route
    public int weightRouteItems(String id, List<Location> list) throws WeightEx {
        return dc.weightRouteItems(id, list); // Delegate to UserController to calculate weight
    }

    // Method to sort the route according to shipment zones
    public void sortRouteAccordingToZones(List<Location> list) {
        dc.sortRouteAccordingToZones(list); // Delegate to UserController to sort the route
    }
    // Method to remove items from the route based on truck capacity
    public String removeItems(List<Location> route, String truckID, String itemName, int itemAmount,String address, int totalWeight) {
        return dc.removeItems(route, truckID,  itemName,  itemAmount, address, totalWeight); // Delegate to UserController to remove items from the route
    }

    // Method to get the total items in a route
    public List<Shipment_item> getTotalItems(List<Location> route) {
        return dc.getTotalItems(route); // Delegate to UserController to retrieve total items in route
    }

    // Method to add a shipment document to the system
    public String addDocument(List<Shipment_item> items, String date, String truck_id, String dep_hour,
                              String driver_id, String dep_from, List<Location> destinations, String eventMessage) {
        return dc.addDocument(items, date, truck_id, dep_hour, driver_id, dep_from, destinations, eventMessage);
        // Delegate to UserController to add the document
    }

    // Method to get the origin address from a route
    public String getOriginAddressFromRoute(List<Location> route) {
        return dc.getOriginAddressFromRoute(route); // Delegate to UserController to get the origin address
    }

    // Method to set an item (likely for shipment or tracking purposes)
    public boolean setItem(String itemName) {
        return dc.setItem(itemName); // Delegate to UserController to set an item
    }

    // Method to set the required amount of an item in a route
    public String setRequiredItemInRoute(List<Location> route, String itemName, int amount) {
        return dc.setRequiredItemInRoute(route, itemName, amount); // Delegate to UserController to set required item
    }

    // Method to print a specific shipment document by ID
    public String printDocument(String doc_id) {
        return dc.printDocument(doc_id); // Delegate to UserController to print the document
    }

    // Method to print all document IDs
    public String printDocIDS() {
        return dc.printDocIDS(); // Delegate to UserController to print all document IDs
    }

    // Method to print all the route items
    public String printRouteItems(List<Location> route) {
        return dc.printRouteItems(route); // Delegate to DriverController to print the IDs
    }

    // Method to end the delivery and finalize the document
    public String endDelivery(String doc_id) {
        return dc.endDelivery(doc_id); // Delegate to UserController to end the delivery and finalize the document
    }
}
