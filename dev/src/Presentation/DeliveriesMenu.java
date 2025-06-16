package Presentation;

import Domain.Location;
import Domain.Shipment_item;
import Domain.WeightEx;
import Presentation_employee.NavigationManager;
import Service.DeliveriesApplication;
import Service.DriversApplication;
import Service.LocationApplication;
import Service.TrucksApplication;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DeliveriesMenu {

    private static DeliveriesApplication da = new DeliveriesApplication();
    private static LocationApplication la = new LocationApplication();
    private static DriversApplication dra = new DriversApplication();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final NavigationManager navigationManager = new NavigationManager();

    // Method to add a new delivery to the system
    public void addDelivery(List<Location> route, String shiftID, LocalDate startDate, String shiftTime ) throws SQLException {
        Scanner scanner = new Scanner(System.in);  // Scanner object to read user input
        TrucksApplication ta = new TrucksApplication();
        DriversApplication dra = new DriversApplication();

        System.out.println("Add Delivery selected.");
        String deliveryTime = "", dateStr = "11/11/1111", originAddress = "", res ;
        boolean wasEmpty = false, added_succesfully = false;

        if (route == null) { // If the route is not provided, prompt the user to choose locations

            while (true) {
                try {
                    System.out.println("Enter Delivery date (format: dd/MM/yyyy) or -1 to exit:");
                    dateStr = scanner.nextLine();

                    // Check for exit condition
                    if (dateStr.equals("-1")) {
                        break;
                    }

                    startDate = LocalDate.parse(dateStr, dateFormatter);

                    System.out.print("Enter delivery hour (format: HH:mm) or -1 to exit: ");
                    deliveryTime = scanner.nextLine();

                    // Check for exit condition
                    if (deliveryTime.equals("-1")) {
                        break;
                    }

                    String[] timeParts = deliveryTime.split(":");
                    int hour = Integer.parseInt(timeParts[0]);
                    shiftTime = "invalid";

                    // Check if hour is between 08:00 and 14:00 (8 AM to 2 PM)
                    if (hour >= 7 && hour < 14) {
                        shiftTime = "MORNING";
                    } else if (hour >= 14 && hour < 21) {
                        shiftTime = "EVENING";
                    }


                    route = new ArrayList<>();
                    System.out.println("Choose origin from known locations: ");
                    System.out.println(la.printLocations());

                    while (!added_succesfully) {  // Keep asking for origin until added successfully
                        originAddress = scanner.nextLine();
                        shiftID = navigationManager.getShiftService().getShiftIdByTime(startDate, shiftTime, originAddress);
                        if (!shiftID.equals("Non existent shift.")) {
                            res = da.addDestination(originAddress, route, shiftID);
                            String shortm = res.substring(0, res.indexOf("."));
                            if (shortm.equals("Success")) {
                                added_succesfully = true;
                            }
                            System.out.println(res);
                        } else {
                            System.out.println(shiftID);
                            return;
                        }
                    }


                    shiftID = navigationManager.getShiftService().getShiftIdByTime(startDate, shiftTime, originAddress);

                    // Check if we got a valid shift ID
                    if (shiftID != null && !shiftTime.equals("invalid")) {
                        // Valid selection - break out of loop or continue with your logic
                        System.out.println("Valid shift selected: " + shiftID);
                        wasEmpty = true;
                        break; // Remove this if you want to continue processing
                    } else {
                        System.out.println("Invalid date/time combination. Please try again.");
                    }

                } catch (Exception e) {
                    System.out.println("Invalid input format. Please try again.");
                }
            }
        } else {
            originAddress = da.getOriginAddressFromRoute(route);
        }

        if (Objects.equals(shiftID, "")) {
            System.out.println("No valid shift selected.");
            return; // or handle accordingly
        }

        System.out.print("Enter wanted truck's id: ");
        System.out.println(ta.printTrucks());
        String truckID = scanner.nextLine();
        while (!truckID.equals("-1") && !ta.isAvailableTruck(truckID)) {
            System.out.println("Truck unavailable. Please choose another one.");
            System.out.println(ta.printTrucks());  // Print list of trucks
            System.out.print("Enter wanted truck's id (or -1 to cancel): ");
            truckID = scanner.nextLine();
        }

        if (truckID.equals("-1")) {
            System.out.println("Operation cancelled.");
            return;  // Exit or handle cancellation appropriately
        }
        System.out.println("These are the company drivers");
        System.out.println(dra.printDrivers());
        System.out.print("Enter wanted driver's id: ");
        String driverID = scanner.nextLine();
        res = dra.isAvailableDriver(driverID, truckID, shiftID);

        System.out.println(res);

        while (!driverID.equals("-1") && !Objects.equals(res, "Driver have correct license and is ready to drive.")) {
            System.out.println(dra.printDrivers());  // Print list of drivers
            System.out.print("Enter wanted driver's id (or -1 to cancel): ");
            driverID = scanner.nextLine();
            res = dra.isAvailableDriver(driverID, truckID, shiftID);  // Re-check availability
            System.out.println(res);
        }

        if (driverID.equals("-1")) {
            System.out.println("Operation cancelled.");
            return;  // Exit or handle cancellation appropriately
        }

        if (wasEmpty) {  // If the route is not provided, prompt the user to choose locations

            System.out.println("Choose destinations from known locations: ");

            String address;
            added_succesfully = false;
            while (true) {  // Allow the user to add multiple destinations
                while (!added_succesfully) {  // Keep asking for destinations until added successfully

                    System.out.print("Enter destination address: ");
                    System.out.println(la.printLocations());  // Print available locations for the route
                    address = scanner.nextLine().trim();
                    shiftID = navigationManager.getShiftService().getShiftIdByTime(startDate, shiftTime, address);
                    res = da.addDestination(address, route, shiftID);
                    String shortm = res.substring(0, res.indexOf("."));
                    if (shortm.equals("Success")){
                        System.out.println(res);
                        added_succesfully = true;
                    } else {
                        System.out.println(res);
                    }
                }
                added_succesfully = false;
                System.out.println("If you want to add another destination press 1. To continue press any other key. ");
                String choice = scanner.nextLine().trim();
                if (!choice.equals("1")) {  // Exit the loop if the user doesn't want to add another destination
                    break;
                }
            }
        }

        System.out.println("Finished adding destinations. Checking if delivery weight is valid. ");
        String eventMessage = "Everything is good. Delivery ongoing";
        try {
            int total_weight = da.weightRouteItems(truckID, route);  // Calculate the total weight of the items in the route
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
                    String new_id = ta.changeTruck(route, truckID, driverID);  // Try changing the truck
                    if (new_id == null) {  // If no matching truck is found
                        removeItemsWrapper(route, truckID, total_weight);
                        eventMessage = "Removed items because of weight overflow, after no matching truck found.";
                        System.out.println(eventMessage);
                    } else {
                        System.out.println("Switched to truck: " + new_id);  // Truck switch successful
                        eventMessage = "Switched to truck: " + new_id;
                        truckID = new_id;  // Update the truck ID
                    }
                    break;
                case 2:
                    removeItemsWrapper(route,truckID, total_weight);
                    System.out.println("Removed items because of weight overflow.");
                    eventMessage = "Removed items because of weight overflow.";
                    break;
            }
        }
        da.sortRouteAccordingToZones(route);  // Sort the route according to zones
        List<Shipment_item> items = da.getTotalItems(route);  // Get the total items for the route
        String doc_id = da.addDocument(items, dateStr,truckID,deliveryTime,driverID,originAddress,route, eventMessage);  // Add delivery document
        System.out.println("Document saved.");
        System.out.println(da.printDocument(doc_id));  // Print the document
        System.out.println("Delivery ongoing");
    }

    public void removeItemsWrapper(List<Location> route, String truckID, int totalWeight) throws SQLException {
        System.out.println(da.printRouteItems(route));
        Scanner scanner = new Scanner(System.in);
        String success = "false";

        int itemAmount = 0;
        String address = null;
        String itemName = null;
        while (!success.equals("Items removed successfully.")) {
            // Print route items
            System.out.println(da.printRouteItems(route));

            // Prompt for user input
            System.out.print("Enter item name to remove: ");
            itemName = scanner.nextLine();

            System.out.print("Enter amount to remove: ");
            try {
                itemAmount = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
                continue;
            }

            System.out.print("Enter address to remove from: ");
            address = scanner.nextLine();

            // Attempt to remove items
            success = da.removeItems(route, truckID, itemName, itemAmount, address, totalWeight);
            System.out.println(success);
        }

        System.out.println("Removed " + itemAmount + " units of " + itemName + " from " + address);
    }

    // Method to handle storage alerts for items
    public void gotStorageAlert() throws SQLException {
        System.out.println("Got storage alert.");
        Scanner scanner = new Scanner(System.in);  // Scanner object to read user input

        List<Location> route = new ArrayList<>();  // List of locations for the delivery route
        boolean added_succesfully = false;
        String itemName = "";

        System.out.println("Items known to our store:");
        System.out.println(da.printItems());
        System.out.println("Enter the name of the item that ran out");

        while (!added_succesfully) {  // Keep asking for the item name until valid
            itemName = scanner.nextLine().trim();
            added_succesfully = da.setItem(itemName);
            if (!added_succesfully){System.out.println("Unknown item name. Try again.");}
        }

        String originAddress = "Headquarters";  // Set the origin address as Headquarters
        String shiftID = "", deliveryTime = "", dateStr = "11/11/1111", res = "" , shiftTime = "";
        LocalDate startDate = LocalDate.parse(dateStr, dateFormatter);

        while (true) {
            try {
                System.out.println("Enter Delivery date (format: dd/MM/yyyy) or -1 to exit:");
                dateStr = scanner.nextLine();

                // Check for exit condition
                if (dateStr.equals("-1")) {
                    break;
                }

                startDate = LocalDate.parse(dateStr, dateFormatter);

                System.out.print("Enter delivery hour (format: HH:mm) or -1 to exit: ");
                deliveryTime = scanner.nextLine();

                // Check for exit condition
                if (deliveryTime.equals("-1")) {
                    break;
                }

                String[] timeParts = deliveryTime.split(":");
                int hour = Integer.parseInt(timeParts[0]);
                shiftTime = "invalid";

                // Check if hour is between 08:00 and 14:00 (8 AM to 2 PM)
                if (hour >= 7 && hour < 14) {
                    shiftTime = "MORNING";
                }
                else if (hour >= 14 && hour < 21) {
                    shiftTime = "EVENING";
                }


                shiftID = navigationManager.getShiftService().getShiftIdByTime(startDate, shiftTime, originAddress);

                // Check if we got a valid shift ID
                if (shiftID != null && !shiftTime.equals("invalid")) {
                    // Valid selection - break out of loop or continue with your logic
                    System.out.println("Valid shift selected: " + shiftID);
                    break; // Remove this if you want to continue processing
                } else {
                    System.out.println("Invalid date/time combination. Please try again.");
                }

            } catch (Exception e) {
                System.out.println("Invalid input format. Please try again.");
            }
        }

        System.out.println(da.addDestination(originAddress, route, shiftID));  // Add the origin location to the route


        System.out.println("Set destinations with " + itemName + " storage alert: (Headquarters is the origin (main warehouse from which the " +
                "goods are taken)");

        String address;
        System.out.println("Choose destinations from known locations: ");

        added_succesfully = false;
        while (true) {  // Allow the user to add multiple destinations
            while (!added_succesfully) {  // Keep asking for destinations until added successfully

                System.out.print("Enter destination address: ");
                System.out.println(la.printLocations());  // Print available locations for the route
                address = scanner.nextLine().trim();
                shiftID = navigationManager.getShiftService().getShiftIdByTime(startDate, shiftTime, originAddress);
                res = da.addDestination(address, route, shiftID);
                String shortm = res.substring(0, res.indexOf("."));
                if (shortm.equals("Success")){
                    System.out.println(res);
                    added_succesfully = true;
                }
            }
            added_succesfully = false;
            System.out.println("If you want to add another destination press 1. To continue press any other key. ");
            String choice = scanner.nextLine().trim();
            if (!choice.equals("1")) {  // Exit the loop if the user doesn't want to add another destination
                break;
            }
        }
        System.out.println("What amount is missing of " + itemName);  // Ask for the amount of the missing item
        int amount = Integer.parseInt(scanner.nextLine().trim());  // Get the amount as input
        System.out.println(da.setRequiredItemInRoute(route, itemName, amount));  // Set the required amount in the route
        System.out.println("Send you now to full delivery planning after requirements added to specific locations");
        addDelivery(route, shiftID, startDate, shiftTime);  // Proceed to add the delivery
    }

    // Method to end the current delivery
    public void endDelivery() throws SQLException {
        System.out.println("End delivery selected.");
        Scanner scanner = new Scanner(System.in);  // Scanner object to read user input

        String docID = "";
        if(da.printDocIDS().isEmpty())
        {
            System.out.println("No Existing deliveries yet");

        }
        else {
            System.out.println(da.printDocIDS());
        }
        while (true) {  // Keep asking for the document ID until valid
            System.out.print("Enter document id: ");
            docID = scanner.nextLine().trim();
            if (!docID.isEmpty()) break;  // Exit the loop once a valid document ID is entered
            System.out.println("Zone name cannot be empty.");
        }
        System.out.println(da.endDelivery(docID));  // End the delivery based on the document ID
    }

    // Method to view documentation for deliveries
    public void viewDocumentation() throws SQLException {
        System.out.println("View Documentation selected.");
        Scanner scanner = new Scanner(System.in);  // Scanner object to read user input

        String docID = "";
        if(da.printDocIDS().isEmpty())
        {
            System.out.println("No Existing deliveries yet");

        }
        else {

            System.out.println(da.printDocIDS());  // Print available document IDs
            while (true) {  // Keep asking for the document ID until valid
                System.out.print("Enter document id: ");
                docID = scanner.nextLine().trim();
                if (!docID.isEmpty()) break;  // Exit the loop once a valid document ID is entered
                System.out.println("Zone name cannot be empty.");
            }
            System.out.println(da.printDocument(docID));  // Print the document based on the document ID
        }
    }

    // Method to update or delete an existing driver
    public void updateDriversLicense() throws SQLException {
        System.out.println("Update Driver License selected.");
        Scanner scanner = new Scanner(System.in);
        System.out.println("These are the existing drivers");
        System.out.println(dra.printDrivers());
        // Prompt for Driver ID, ensuring it is not empty
        String driverId = "";
        while (true) {
            System.out.print("Enter Driver ID: ");
            driverId = scanner.nextLine().trim();
            if (!driverId.isEmpty()) break;
            System.out.println("Driver ID cannot be empty.");
        }


        // If changing license list, present sub-options to add or remove licenses
        System.out.println("Choose an option ");
        System.out.println("1. Delete license");
        System.out.println("2. Add license");

        // Ensure a valid choice (either 1 or 2)
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.nextInt();
        }
        int choice = scanner.nextInt();
        if (choice == 1) {
            // Prompt for license to delete and call the service method
            System.out.print("Enter license to remove: ");
            scanner.nextInt();
            int license = scanner.nextInt();
            System.out.println(dra.deleteLicense(driverId, license));
        } else if (choice == 2) {
            // Prompt for license to add and call the service method
            System.out.print("Enter license to add: ");
            int license = scanner.nextInt();
            System.out.println(dra.addLicense(driverId, license));
        } else {
            System.out.println("Invalid option try again.");
        }
    }
}