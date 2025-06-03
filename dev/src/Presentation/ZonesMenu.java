package Presentation;

import Service.LocationApplication;
import Service.ZonesApplication;

import java.sql.SQLException;
import java.util.Scanner;

public class ZonesMenu {
    private static ZonesApplication za = new ZonesApplication();

    // Method to add a new shipping zone
    public void addShippingZone() throws SQLException {
        System.out.println("Add Shipment Zone selected.");
        Scanner scanner = new Scanner(System.in);
        String name = "";
        System.out.println("List of current zones in the system: ");
        System.out.println(za.printZones());
        // Loop to ensure the name is not empty
        while (true) {
            System.out.print("Enter Shipment zone name: ");
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) break; // Exit the loop if the name is valid
            System.out.println("Name cannot be empty."); // Prompt user to enter a name
        }

        System.out.println("Choose rank");

        // Validate that the input for rank is an integer
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next(); // Consumes invalid input
        }
        int id = scanner.nextInt(); // Reading the rank as an integer
        System.out.println(za.addShippingZone(id, name)); // Call to the service layer to add the zone
    }

    // Method to update or delete an existing shipping zone
    public void updateOrDeleteShipmentZone() throws SQLException {
        System.out.println("Update/Delete Shipment Zone selected.");
        System.out.println("List of current zones in the system: ");
        System.out.println(za.printZones());
        Scanner scanner = new Scanner(System.in);
        String zoneName = "";

        // Loop to ensure the zone name is not empty
        while (true) {
            System.out.print("Enter Zone name: ");
            zoneName = scanner.nextLine().trim();
            if (!zoneName.isEmpty()) break; // Exit loop if zone name is valid
            System.out.println("Zone name cannot be empty."); // Prompt user for a valid name
        }

        // Present the options for updating or deleting the zone
        System.out.println("Choose an option ");
        System.out.println("1. Delete zone");
        System.out.println("2. Change rank");

        // Validate that the choice is an integer
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.nextInt(); // Consumes invalid input
        }
        int choice = scanner.nextInt(); // Read user's choice

        if (choice == 1) {
            try {
                // If user chose to delete the zone
                System.out.println(za.deleteZone(zoneName)); // Call to service layer to delete the zone
            } catch (SQLException e){
                System.out.println("Cant delete this zone because it is part of another location.");
            }
        }
        else if(choice == 2) {
            // If user chose to change the rank of the zone
            System.out.println("Choose new rank ");

            // Validate the new rank input
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.nextInt(); // Consumes invalid input
            }
            int rank = scanner.nextInt(); // Read the new rank
            System.out.println(za.updateRank(zoneName, rank)); // Call to service layer to update the rank
        }
    }
}