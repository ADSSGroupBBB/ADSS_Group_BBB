package Main;

import Presentation.NavigationManager;

/**
 * Main class for the Super-Li Employee Management System
 * This is the entry point for the application
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  Super-Li Employee Management System");
        System.out.println("===========================================");

        // Create and start the navigation manager
        NavigationManager navigationManager = new NavigationManager();
        navigationManager.start();

        System.out.println("System shutdown complete.");
    }
}