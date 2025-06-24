package main;

import Presentation_employee.NavigationManager;
import util.Database_HR_DL;

import static PresentationD.DeliveriesManagerIO.presentingDeliveriesMenu;

import java.sql.SQLException;
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

        System.out.println("Enter your choice: ");
        System.out.println("1. Use sample data");
        System.out.println("2. Use empty database");

        // Validate that the input is an integer
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next(); // Consume invalid input
        }

        choice = scanner.nextInt(); // Read user's choice

        // Execute the appropriate action based on user's choice
        switch (choice) {
            case 1:
                Database_HR_DL.useSampleDatabase();
                break;
            case 2:
                Database_HR_DL.useProductionDatabase();
                break;
            default:
                System.out.println("Invalid choice choose 1 or 2.");
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
                NavigationManager navigationManager = new NavigationManager();
                navigationManager.start();
                break;
            case 3:
                break;
            default:
                System.out.println("Invalid choice choose 1 or 2.");
        }

        System.out.println("System shutdown complete.");
    }


}