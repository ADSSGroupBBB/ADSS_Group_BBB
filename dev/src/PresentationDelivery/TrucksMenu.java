package PresentationDelivery;

import ServiceDelivery.TrucksApplication;

import java.util.Scanner;

public class TrucksMenu {
    private static TrucksApplication ta = new TrucksApplication();
    // Method for adding a new truck
    public void addTruck() {
        System.out.println("Add Truck selected.");
        Scanner scanner = new Scanner(System.in);

        // Prompting user to enter the Truck ID
        System.out.print("Enter Truck ID: ");
        String truckId = scanner.nextLine().trim();

        // Prompting user to enter the Truck Type (integer value)
        System.out.print("Enter Truck Type (int): ");
        int type = Integer.parseInt(scanner.nextLine().trim());

        // Prompting user to enter the Truck Weight
        System.out.print("Enter Truck Weight: ");
        int truckWeight = Integer.parseInt(scanner.nextLine().trim());

        // Prompting user to enter the Max Weight that the truck can carry
        System.out.print("Enter Max Weight: ");
        int maxWeight = Integer.parseInt(scanner.nextLine().trim());

        // Calling the method in the UserApplication to insert the truck data and printing the result
        System.out.println(ta.insertTruck(truckId, type, truckWeight, maxWeight));
    }

    // Method for deleting an existing truck by its ID
    public void deleteTruck() {
        System.out.println("Delete Truck selected.");
        Scanner scanner = new Scanner(System.in);

        // Prompting user to enter the Truck ID to delete
        System.out.print("Enter Truck ID: ");
        String truckId = scanner.nextLine().trim();

        // Calling the method in the UserApplication to delete the truck and printing the result
        System.out.println(ta.deleteTruck(truckId));
    }
}
