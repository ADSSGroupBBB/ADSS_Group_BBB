package Main;

import Service_employee.EmployeeService;
import Presentation_employee.NavigationManager;
import util.Database;

import static Presentation.DeliveriesManagerIO.presentingDeliveriesMenu;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Scanner;

/**
 * Main class for the Super-Li Employee Management System
 * This is the entry point for the application
 */
public class Main {

    public static void main(String[] args) throws SQLException {
        System.out.println("===========================================");
        System.out.println("  Employees and deliveries Management System");
        System.out.println("===========================================");
        Scanner scanner = new Scanner(System.in);
        int choice;
        System.out.println("If you want to start with empty database enter 'empty'.");
        String empty = scanner.nextLine();  // User enters the delivery date
        if (empty.equals("empty")){
            System.out.println("Sure? the data will be deleted? (yes/no)");
            empty = scanner.nextLine();
            if (Objects.equals(empty, "yes")){
                Database.initializeEmptyDatabase();
            }
        }

        System.out.println("Enter your choice: ");
        System.out.println("1. Deliveries menu");
        System.out.println("2. Employees menu");
        System.out.println("Any other number: Exit");

        // Validate that the input is an integer
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next(); // Consume invalid input
        }

        choice = scanner.nextInt(); // Read user's choice

        // Execute the appropriate action based on user's choice
        switch (choice) {
            case 1:
                presentingDeliveriesMenu();
                break;
            case 2:
                //initializeFirstUser(); // create the first user if there is no employee in the system
                // Create and start the navigation manager
                NavigationManager navigationManager = new NavigationManager();
                navigationManager.start();
                break;
            case 3:
                break;
            default:
                System.out.println("Invalid choice choose 1 or 2 or 3.");
        }


        System.out.println("System shutdown complete.");
    }
/**
    private static void initializeFirstUser() {
        try {
            EmployeeService employeeController = new EmployeeService();

            // check if there is employee in the system
            if (employeeController.getAllEmployees().isEmpty()) {
                System.out.println("No employees found. Creating initial admin user...");
                // creating the first HR manager
                boolean success = employeeController.addManagerEmployee("admin", "Admin", "User", "123456",
                        LocalDate.now(), 100.0, "HR_MANAGER", "admin123",
                        50, 50, "Migdal");

                if (success) {
                    System.out.println("Initial admin user created successfully");
                } else {
                    System.out.println("Failed to create initial admin user");
                }
            }
        } catch (Exception e) {
            System.out.println("Error initializing first user: " + e.getMessage());
            e.printStackTrace();
        }
    }
 */
}