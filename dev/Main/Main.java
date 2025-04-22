//package Main;
//
//import Presentation.NavigationManager;
//
///**
// * Main class for the Super-Li Employee Management System
// * This is the entry point for the application
// */
//public class Main {
//
//    public static void main(String[] args) {
//        System.out.println("===========================================");
//        System.out.println("  Super-Li Employee Management System");
//        System.out.println("===========================================");
//
//        // Create and start the navigation manager
//        NavigationManager navigationManager = new NavigationManager();
//        navigationManager.start();
//
//        System.out.println("System shutdown complete.");
//    }
//}

package Main;

import Domain.Employee.UserRole;
import Presentation.NavigationManager;
import Service.EmployeeDTO;
import Service.EmployeeService;

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

        // יצירת עובד ראשוני אם אין עובדים במערכת
        initializeFirstUser();

        // Create and start the navigation manager
        NavigationManager navigationManager = new NavigationManager();
        navigationManager.start();

        System.out.println("System shutdown complete.");
    }

    private static void initializeFirstUser() {
        try {
            EmployeeService employeeService = new EmployeeService();

            // בדיקה אם יש עובדים במערכת
            if (employeeService.getAllEmployees().isEmpty()) {
                System.out.println("No employees found. Creating initial admin user...");

                // יצירת מנהל כח אדם ראשוני
                boolean success = employeeService.addNewEmployee(
                        "admin", "Admin", "User", "123456",
                        LocalDate.now(), 100.0, "HR_MANAGER", "admin123");

                if (success) {
                    System.out.println("Initial admin user created successfully");
                    System.out.println("Login with ID: admin and password: admin123");
                } else {
                    System.out.println("Failed to create initial admin user");
                }
            }
        } catch (Exception e) {
            System.out.println("Error initializing first user: " + e.getMessage());
            e.printStackTrace();
        }
    }
}/////