package src.Presentation;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import src.Domain.DeliveriesController;
import src.Presentation_employee.NavigationManager;
import src.Service.*;


public class DeliveriesManagerIO {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    // Method to present the main menu to the user
    public static void presentingDeliveriesMenu() throws SQLException {
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
            System.out.println("3. Update Driver's license");
            System.out.println("4. Add Shipment Zone");
            System.out.println("5. Update Shipment Zone");
            System.out.println("6. Add Truck");
            System.out.println("7. Delete Truck");
            System.out.println("8. View Documentation");
            System.out.println("9. Add location");
            System.out.println("10. Delete location");
            System.out.println("11. End delivery");
            System.out.println("12. Add item to location's required list");
            System.out.println("13. Move to employee menu");
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
                    deliverysMenu.addDelivery(null, "", LocalDate.parse("11/11/1111", dateFormatter),"");
                    break;
                case 2:
                    deliverysMenu.gotStorageAlert();
                    break;
                case 3:
                    deliverysMenu.updateDriversLicense();
                    break;
                case 4:
                    zonesMenu.addShippingZone();
                    break;
                case 5:
                    zonesMenu.updateOrDeleteShipmentZone();
                    break;
                case 6:
                    trucksMenu.addTruck();
                    break;
                case 7:
                    trucksMenu.deleteTruck();
                    break;
                case 8:
                    deliverysMenu.viewDocumentation();
                    break;
                case 9:
                    locationsMenu.addLocation();
                    break;
                case 10:
                    locationsMenu.deleteLocation();
                    break;
                case 11:
                    deliverysMenu.endDelivery();
                    break;
                case 12:
                    locationsMenu.addItemToLocation();
                    break;
                case 13:
                    // Move to employee menu code here
                    break;
                case 14:
                    NavigationManager navigationManager = new NavigationManager();
                    navigationManager.start();
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