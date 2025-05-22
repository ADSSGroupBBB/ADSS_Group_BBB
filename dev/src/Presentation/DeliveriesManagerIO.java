package Presentation;

import java.util.Scanner;

import Domain.DeliveriesController;
import Service.*;


public class DeliveriesManagerIO {

    // Method to present the main menu to the user
    public static void presentingMenu() {
        Scanner scanner = new Scanner(System.in);
        final int PASSWORD = 1; // Hardcoded password value
        int attempts = 0; // Counter for password attempts
        boolean accessGranted = false; // Flag for successful login

        // Password prompt loop (max 3 attempts)
        while (attempts < 3) {
            System.out.print("Enter password to access the system: ");

            // Validate that input is an integer
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next(); // Consume invalid input
            }

            int inputPassword = scanner.nextInt();

            // Check if password is correct
            if (inputPassword == PASSWORD) {
                accessGranted = true; // Set flag
                break; // Exit password loop
            } else {
                attempts++; // Increment failed attempts
                if (attempts < 3) {
                    System.out.println("Incorrect password. Try again.");
                }
            }
        }

        // If failed 3 times, exit the program
        if (!accessGranted) {
            System.out.println("Too many failed attempts. Shutting down...");
            scanner.close();
            return; // Exit the method (and program)
        }

        // Welcome message after successful login
        System.out.println("Welcome to the manager of delivery's system");

        int choice; // Variable to store user menu choice

        // Creating instances of various menu classes
        DriversMenu driversMenu = new DriversMenu();
        ZonesMenu zonesMenu = new ZonesMenu();
        DeliveriesMenu deliverysMenu = new DeliveriesMenu();
        TrucksMenu trucksMenu = new TrucksMenu();
        LocationsMenu locationsMenu = new LocationsMenu();
        LocationApplication ua = new LocationApplication();
        // Menu loop
        do {
            // Displaying the menu options
            System.out.println("\n===== MENU =====");
            System.out.println("1. Add Delivery");
            System.out.println("2. Got Storage alert");
            System.out.println("3. Add Driver");
            System.out.println("4. Update Driver");
            System.out.println("5. Add Shipment Zone");
            System.out.println("6. Update Shipment Zone");
            System.out.println("7. Add Truck");
            System.out.println("8. Delete Truck");
            System.out.println("9. View Documentation");
            System.out.println("10. Add location");
            System.out.println("11. Delete location");
            System.out.println("12. End delivery");
            System.out.println("13. Upload base info");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            // Validate that the input is an integer
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next(); // Consume invalid input
            }

            choice = scanner.nextInt(); // Read user's choice

            // Execute the appropriate action based on user's choice
            switch (choice) {
                case 1:
                    deliverysMenu.addDelivery(null);
                    break;
                case 2:
                    deliverysMenu.gotStorageAlert();
                    break;
                case 3:
                    driversMenu.addDriver();
                    break;
                case 4:
                    driversMenu.updateDrivers();
                    break;
                case 5:
                    zonesMenu.addShippingZone();
                    break;
                case 6:
                    zonesMenu.updateOrDeleteShipmentZone();
                    break;
                case 7:
                    trucksMenu.addTruck();
                    break;
                case 8:
                    trucksMenu.deleteTruck();
                    break;
                case 9:
                    deliverysMenu.viewDocumentation();
                    break;
                case 10:
                    locationsMenu.addLocation();
                    break;
                case 11:
                    locationsMenu.deleteLocation();
                    break;
                case 12:
                    deliverysMenu.endDelivery();
                    break;
                case 13:
                    DeliveriesController.initBaseData(); // Uploads base info
                    System.out.println("Data uploaded.");
                    break;
                case 0:
                    System.out.println("Exiting the system."); // Exit message
                    break;
                default:
                    System.out.println("Invalid choice. Please choose a valid option."); // Handle invalid input
            }

        } while (choice != 0); // Repeat until user selects exit

        scanner.close(); // Close scanner to prevent memory leaks
    }
}
