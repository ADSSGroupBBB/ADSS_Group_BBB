package Main;

import Controller_employee.EmployeeController;
import Presentation_employee.NavigationManager;

import java.time.LocalDate;

/**
 * Main class for the Super-Li Employee Management System
 * This is the entry point for the application
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  Super-Li Employee Management System");
        System.out.println("===========================================");

        initializeFirstUser(); // create the first user if there is no employee in the system

        // Create and start the navigation manager
        NavigationManager navigationManager = new NavigationManager();
        navigationManager.start();
        System.out.println("System shutdown complete.");
    }

    private static void initializeFirstUser() {
        try {
            EmployeeController employeeController = new EmployeeController();

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
}