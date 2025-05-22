package Presentation;

import Service.LocationApplication;

import java.util.Scanner;

public class LocationsMenu {
    private static LocationApplication ua = new LocationApplication();
    // Method to add a new location
    public void addLocation() {
        Scanner scanner = new Scanner(System.in);

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
        System.out.println(ua.insertLocation(contactName, contactNum, address, zone));
    }

    // Method to delete an existing location
    public void deleteLocation() {
        Scanner scanner = new Scanner(System.in);

        // Prompt for the address of the location to delete
        System.out.print("Enter Address of location to delete: ");
        String address = scanner.nextLine().trim();

        // Call the service method to delete the location
        System.out.println(ua.deleteLocation(address));
    }
    public void addItemToLocation() {
        Scanner scanner = new Scanner(System.in);

        // Prompt for the location address
        System.out.print("Enter Location Address: ");
        String address = scanner.nextLine().trim();

        // Prompt for the item name
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
        String result = ua.addItemToLocation(address, itemName, quantity);
        System.out.println(result);
    }
}
