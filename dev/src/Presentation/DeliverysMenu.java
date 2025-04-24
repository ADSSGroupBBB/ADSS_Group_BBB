package Presentation;

import Domain.Location;
import Domain.Shipment_item;
import Domain.WeightEx;
import Service.UserApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class DeliverysMenu {

    // Method to add a new delivery to the system
    public void addDelivery(List<Location> route) {
        Scanner scanner = new Scanner(System.in);  // Scanner object to read user input
        UserApplication ua = new UserApplication();  // UserApplication object to handle business logic

        System.out.println("Add Delivery selected.");
        System.out.println("Enter Delivery date (format: dd/MM/yyyy):");
        String dateInput = scanner.nextLine();  // User enters the delivery date
        System.out.print("Enter delivery hour (format: HH:mm): ");
        String deliveryTime = scanner.nextLine();  // User enters the delivery time
        System.out.print("Enter wanted truck's id: ");
        String truckID = scanner.nextLine();  // User enters the truck ID
        boolean isAvailable = ua.isAvailableTruck(truckID);  // Check if the truck is available
        if (!isAvailable){
            System.out.print("Truck unavailable. Please create new delivery. ");
            return;  // If the truck is not available, the method ends
        }
        System.out.print("Enter wanted driver's id: ");
        String driverID = scanner.nextLine();  // User enters the driver's ID
        String res = ua.isAvailableDriver(driverID, truckID);  // Check if the driver is available for the truck
        System.out.println(res);
        if (!Objects.equals(res, "Driver is available.")){
            System.out.print("Cant make the delivery with this driver. Please create new delivery. ");
            return;  // If the driver is not available, the method ends
        }
        String originAddress = "";
        if (route == null) {  // If the route is not provided, prompt the user to choose locations
            route = new ArrayList<>();
            System.out.println("Choose origin from known locations: ");
            System.out.println(ua.printLocations());

            boolean added_succesfully = false;
            while (!res.equals("Location added successfully.")) {  // Keep asking for origin until added successfully
                originAddress = scanner.nextLine();
                res = ua.addDestination(originAddress, route);
            }
            res = "";
            System.out.println("Choose destinations from known locations: ");
            String address;
            while (true) {  // Allow the user to add multiple destinations
                while (!res.equals("Location added successfully.")) {  // Keep asking for destinations until added successfully
                    System.out.print("Enter destination address: ");
                    address = scanner.nextLine().trim();
                    res = ua.addDestination(address, route);
                }
                res = "";
                System.out.println("If you want to add another destination press 1. To continue press any other key. ");
                String choice = scanner.nextLine().trim();
                if (!choice.equals("1")) {  // Exit the loop if the user doesn't want to add another destination
                    break;
                }
            }
        } else {
            originAddress = ua.getOriginAddressFromRoute(route);  // If route is provided, get the origin from the route
        }
        System.out.println("Finished adding destinations. Checking if delivery weight is valid. ");
        String eventMessage = "Everything is good. Delivery ongoing";
        try {
            int total_weight = ua.weightRouteItems(truckID, route);  // Calculate the total weight of the items in the route
            System.out.println(eventMessage);
        } catch (WeightEx e){  // Handle weight overflow exception
            int total_weight = e.weight;
            System.out.println("Weight overflow. Choose between these options:");
            System.out.println("1: Change truck");
            System.out.println("2: Remove items");

            int choice = -1;
            while (true) {  // Prompt the user to choose an action if there is a weight overflow
                System.out.print("Enter your choice (1 or 2): ");
                if (scanner.hasNextInt()) {  // Validate the user's input
                    choice = scanner.nextInt();
                    if (choice == 1 || choice == 2) {  // Only accept 1 or 2
                        break;
                    } else {
                        System.out.println("Invalid choice. Please enter 1 or 2.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // discard invalid input
                }
            }

            switch (choice) {  // Handle the user's choice based on weight overflow
                case 1:
                    String new_id = ua.changeTruck(route, truckID, driverID);  // Try changing the truck
                    if (new_id == null) {  // If no matching truck is found
                        ua.removeItems(route, truckID, total_weight);  // Remove items to fit weight requirements
                        System.out.println("Removed items because of weight overflow, after no matching truck found.");
                        eventMessage = "Removed items because of weight overflow, after no matching truck found.";
                    } else {
                        System.out.println("Switched to truck: " + new_id);  // Truck switch successful
                        eventMessage = "Switched to truck: " + new_id;
                        truckID = new_id;  // Update the truck ID
                    }
                    break;
                case 2:
                    ua.removeItems(route, truckID, total_weight);  // Remove items to fit weight requirements
                    System.out.println("Removed items because of weight overflow.");
                    eventMessage = "Removed items because of weight overflow.";
                    break;
            }
        }
        ua.sortRouteAccordingToZones(route);  // Sort the route according to zones
        List<Shipment_item> items = ua.getTotalItems(route);  // Get the total items for the route
        String doc_id = ua.addDocument(items, dateInput,truckID,deliveryTime,driverID,originAddress,route, eventMessage);  // Add delivery document
        System.out.println("Document saved.");
        System.out.println(ua.printDocument(doc_id));  // Print the document
        System.out.println("Delivery ongoing");
    }

    // Method to handle storage alerts for items
    public void gotStorageAlert() {
        System.out.println("Got storage alert.");
        Scanner scanner = new Scanner(System.in);  // Scanner object to read user input
        UserApplication ua = new UserApplication();  // UserApplication object to handle business logic
        List<Location> route = new ArrayList<>();  // List of locations for the delivery route
        boolean added_succesfully = false;
        String itemName = "";
        System.out.println("Enter the name of the item that ran out");

        while (!added_succesfully) {  // Keep asking for the item name until valid
            itemName = scanner.nextLine().trim();
            added_succesfully = ua.setItem(itemName);
        }
        added_succesfully = false;

        String originAddress = "Headquarters";  // Set the origin address as Headquarters
        System.out.println(ua.addDestination(originAddress, route));  // Add the origin location to the route


        System.out.println("Set destinations with " + itemName + " storage alert: (Headquarters is the origin (main warehouse from which the " +
                "goods are taken)");
        System.out.println(ua.printLocations());  // Print available locations for the route
        String address;
        String res = "";
        while (true) {  // Keep asking for destination addresses
            while (!res.equals("Location added successfully.")) {  // Keep asking until destination is added successfully
                System.out.print("Enter destination address: ");
                address = scanner.nextLine().trim();
                res = ua.addDestination(address, route);
            }
            res = "";
            System.out.println("If you want to add another destination press 1. To continue press any other key. ");
            String choice = scanner.nextLine().trim();
            if (!choice.equals("1")) {  // Exit the loop if the user doesn't want to add another destination
                break;
            }
        }
        System.out.println("What amount is missing of " + itemName);  // Ask for the amount of the missing item
        int amount = Integer.parseInt(scanner.nextLine().trim());  // Get the amount as input
        System.out.println(ua.setRequiredItemInRoute(route, itemName, amount));  // Set the required amount in the route
        System.out.println("Send you now to full delivery planning after requirements added to specific locations");
        addDelivery(route);  // Proceed to add the delivery
    }

    // Method to end the current delivery
    public void endDelivery(){
        System.out.println("End delivery selected.");
        Scanner scanner = new Scanner(System.in);  // Scanner object to read user input
        UserApplication ua = new UserApplication();  // UserApplication object to handle business logic
        String docID = "";
        if(ua.printDocIDS().isEmpty())
        {
            System.out.println("No Existing deliveries yet");

        }
        else {
            System.out.println(ua.printDocIDS());
        }
        while (true) {  // Keep asking for the document ID until valid
            System.out.print("Enter document id: ");
            docID = scanner.nextLine().trim();
            if (!docID.isEmpty()) break;  // Exit the loop once a valid document ID is entered
            System.out.println("Zone name cannot be empty.");
        }
        System.out.println(ua.endDelivery(docID));  // End the delivery based on the document ID
    }

    // Method to view documentation for deliveries
    public void viewDocumentation() {
        System.out.println("View Documentation selected.");
        Scanner scanner = new Scanner(System.in);  // Scanner object to read user input
        UserApplication ua = new UserApplication();  // UserApplication object to handle business logic
        String docID = "";
        if(ua.printDocIDS().isEmpty())
        {
            System.out.println("No Existing deliveries yet");

        }
        else {


            System.out.println(ua.printDocIDS());  // Print available document IDs
            while (true) {  // Keep asking for the document ID until valid
                System.out.print("Enter document id: ");
                docID = scanner.nextLine().trim();
                if (!docID.isEmpty()) break;  // Exit the loop once a valid document ID is entered
                System.out.println("Zone name cannot be empty.");
            }
            System.out.println(ua.printDocument(docID));  // Print the document based on the document ID
        }
    }
}
