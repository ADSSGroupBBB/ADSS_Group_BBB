package src.Presentation;

import src.Service.DeliveriesApplication;
import src.Service.LocationApplication;
import src.Service.ZonesApplication;

import java.sql.SQLException;
import java.util.Scanner;


public class LocationsMenu {
    private static LocationApplication la = new LocationApplication();
    private static ZonesApplication za = new ZonesApplication();
    private static DeliveriesApplication da = new DeliveriesApplication();

    // Method to add a new location
    public void addLocation() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Current locations in the system: (address must be unique)");
        System.out.println(la.printLocations());
        // Prompt for Contact Name
        System.out.print("Enter Contact Name: ");
        String contactName = scanner.nextLine().trim();

        // Prompt for Contact Number
        System.out.print("Enter Contact Number: ");
        String contactNum = scanner.nextLine().trim();

        // Prompt for Address, ensuring it is not empty
        String address;
        while (true) {
            System.out.print("Enter Address: ");
            address = scanner.nextLine().trim();
            if (!address.isEmpty()) break;
            System.out.println("Address cannot be empty.");
        }

        // Prompt for Shipping Zone, handling invalid zone input
        System.out.println("List of current zones in the system: ");
        System.out.println(za.printZones());
        String zone = null;
        while (zone == null) {
            System.out.print("Enter Shipping Zone: ");
            try {
                zone = scanner.nextLine().trim();
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid zone. Try again.");
            }
        }

        // Call the service method to insert the new location
        System.out.println(la.insertLocation(contactName, contactNum, address, zone));
    }

    // Method to delete an existing location
    public void deleteLocation() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Current locations in the system:");
        System.out.println(la.printLocations());
        // Prompt for the address of the location to delete
        System.out.print("Enter Address of location to delete: ");
        String address = scanner.nextLine().trim();
        try {
            // Call the service method to delete the location
            System.out.println(la.deleteLocation(address));
        } catch (SQLException e){
            System.out.println("Cant delete this location because it is part of a delivery.");
        }
    }
    public void addItemToLocation() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Current locations in the system:");
        System.out.println(la.printLocations());
        // Prompt for the location address
        System.out.print("Enter Location Address: ");
        String address = scanner.nextLine().trim();

        System.out.println("Items known to our store:");
        System.out.println(da.printItems());

        System.out.print("Enter Item Name: ");
        String itemName = scanner.nextLine().trim();

        // Prompt for the quantity
        int quantity = 0;
        while (quantity <= 0) {
            System.out.print("Enter Quantity to Add: ");
            try {
                quantity = Integer.parseInt(scanner.nextLine().trim());
                if (quantity <= 0) {
                    System.out.println("Quantity must be positive.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }

        // Call the service layer without weight parameter
        String result = la.addItemToLocation(address, itemName, quantity);
        System.out.println(result);
    }
}