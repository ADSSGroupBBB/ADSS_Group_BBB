package PresentationDelivery;

import ServiceDelivery.DriversApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DriversMenu {
    private static DriversApplication da = new DriversApplication();
    // Method to add a new driver
    public void addDriver() {
        Scanner scanner = new Scanner(System.in);

        // Prompt for Driver ID, ensuring it is not empty
        String driverId = "";
        while (true) {
            System.out.print("Enter Driver ID: ");
            driverId = scanner.nextLine().trim();
            if (!driverId.isEmpty()) break;
            System.out.println("Driver ID cannot be empty.");
        }

        // Prompt for Driver Name, ensuring it is not empty
        String name = "";
        while (true) {
            System.out.print("Enter Driver Name: ");
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) break;
            System.out.println("Name cannot be empty.");
        }

        // Prompt for number of licenses, ensuring a valid non-negative integer is provided
        int numLicenses = 0;
        while (true) {
            System.out.print("Enter number of licenses: ");
            try {
                numLicenses = Integer.parseInt(scanner.nextLine().trim());
                if (numLicenses < 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a non-negative integer.");
            }
        }

        // Collect the list of licenses for the driver
        List<Integer> licenseList = new ArrayList<>();
        for (int i = 0; i < numLicenses; i++) {
            while (true) {
                System.out.print("Enter license #" + (i + 1) + ": ");
                try {
                    int license = Integer.parseInt(scanner.nextLine().trim());
                    licenseList.add(license);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid int.");
                }
            }
        }

        // Call the service method to insert the new driver
        System.out.println(da.insertDriver(driverId, name, licenseList));
    }

    // Method to update or delete an existing driver
    public void updateDrivers() {
        System.out.println("Update/Delete Driver selected.");
        Scanner scanner = new Scanner(System.in);

        // Prompt for Driver ID, ensuring it is not empty
        String driverId = "";
        while (true) {
            System.out.print("Enter Driver ID: ");
            driverId = scanner.nextLine().trim();
            if (!driverId.isEmpty()) break;
            System.out.println("Driver ID cannot be empty.");
        }

        // Present options to the user for updating the driver
        System.out.println("Choose an option ");
        System.out.println("1. Delete driver");
        System.out.println("2. Change license list");

        // Ensure a valid choice (either 1 or 2)
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.nextInt();
        }
        int choice = scanner.nextInt();
        if (choice == 1) {
            // Call the service method to delete the driver
            System.out.println(da.deleteDriver(driverId));
        } else if (choice == 2) {
            // If changing license list, present sub-options to add or remove licenses
            System.out.println("Choose an option ");
            System.out.println("1. Delete license");
            System.out.println("2. Add license");

            // Ensure a valid choice (either 1 or 2)
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.nextInt();
            }
            choice = scanner.nextInt();
            if (choice == 1) {
                // Prompt for license to delete and call the service method
                System.out.print("Enter license to remove: ");
                scanner.nextInt();
                int license = scanner.nextInt();
                System.out.println(da.deleteLicense(driverId, license));
            } else if (choice == 2) {
                // Prompt for license to add and call the service method
                System.out.print("Enter license to add: ");
                int license = scanner.nextInt();
                System.out.println(da.addLicense(driverId, license));
            }
        }
    }
}
