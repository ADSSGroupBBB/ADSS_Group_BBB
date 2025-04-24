package Presentation;

import Service.UserApplication;

import java.util.Scanner;

public class LocationsMenu {

    // Method to add a new location
    public void addLocation() {
        Scanner scanner = new Scanner(System.in);
        UserApplication ua = new UserApplication();

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
        UserApplication ua = new UserApplication();

        // Prompt for the address of the location to delete
        System.out.print("Enter Address of location to delete: ");
        String address = scanner.nextLine().trim();

        // Call the service method to delete the location
        System.out.println(ua.deleteLocation(address));
    }
}
