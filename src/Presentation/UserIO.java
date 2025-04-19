package Presentation;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import Service.*;

public class UserIO {

        public static void presentingMenu() {
            Scanner scanner = new Scanner(System.in);

            int choice;

            do {
                System.out.println("\n===== MENU =====");
                System.out.println("1. Add Delivery");
                System.out.println("2. Change Delivery");
                System.out.println("3. Add Driver");
                System.out.println("4. Update/Delete Driver");
                System.out.println("5. Add Shipment Zone");
                System.out.println("6. Update/Delete Shipment Zone");
                System.out.println("7. Add Truck");
                System.out.println("8. Update/Delete Truck");
                System.out.println("9. View Documentation");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");

                // Make sure input is an int
                while (!scanner.hasNextInt()) {
                    System.out.print("Invalid input. Please enter a number: ");
                    scanner.next();
                }

                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addDelivery();
                        break;
                    case 2:
                        changeDelivery();
                        break;
                    case 3:
                        addDriver();
                        break;
                    case 4:
                        updateDrivers();
                        break;
                    case 5:
                        addShipmentZone();
                        break;
                    case 6:
                        updateOrDeleteShipmentZone();
                        break;
                    case 7:
                        addTruck();
                        break;
                    case 8:
                        updateOrDeleteTruck();
                        break;
                    case 9:
                        viewDocumentation();
                        break;
                    case 0:
                        System.out.println("Exiting the system.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose a valid option.");
                }

            } while (choice != 0);

            scanner.close();
        }

        // Dummy methods to be implemented
        public static void addDelivery() {
            System.out.println("Add Delivery selected.");
        }

        public static void changeDelivery() {
            System.out.println("Change Delivery selected.");
        }

        public static void addDriver() {
            Scanner scanner = new Scanner(System.in);
            UserApplication ua = new UserApplication();
            String driverId = "";
            while (true) {
                System.out.print("Enter Driver ID: ");
                driverId = scanner.nextLine().trim();
                if (!driverId.isEmpty()) break;
                System.out.println("Driver ID cannot be empty.");
            }

            String name = "";
            while (true) {
                System.out.print("Enter Driver Name: ");
                name = scanner.nextLine().trim();
                if (!name.isEmpty()) break;
                System.out.println("Name cannot be empty.");
            }

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

                ua.insertDriver(driverId, name, licenseList);
            }
        }

        public static void updateDrivers() {
            System.out.println("Update/Delete Driver selected.");
            Scanner scanner = new Scanner(System.in);
            UserApplication ua = new UserApplication();
            String driverId = "";
            while (true) {
                System.out.print("Enter Driver ID: ");
                driverId = scanner.nextLine().trim();
                if (!driverId.isEmpty()) break;
                System.out.println("Driver ID cannot be empty.");
            }
            System.out.print("Choose an option ");
            System.out.println("1. Delete driver");
            System.out.println("2. Change license list");

            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next();
            }
            int choice = scanner.nextInt();
            if (choice == 1) {
                ua.deleteDriver(driverId);;
            }
            else if(choice == 2) {
                System.out.print("Choose an option ");
                System.out.println("1. Delete license");
                System.out.println("2. Add license");

                while (!scanner.hasNextInt()) {
                    System.out.print("Invalid input. Please enter a number: ");
                    scanner.next();
                }
                choice = scanner.nextInt();
                if (choice == 1) {
                    System.out.print("Enter license to remove: ");
                    scanner.next();
                    int license = scanner.nextInt();
                    ua.deleteLicense(driverId, license);
                } else if (choice == 2){
                    System.out.print("Enter license to add: ");
                    scanner.next();
                    int license = scanner.nextInt();
                    ua.addLicense(driverId, license);
                }
            }
        }

        public static void addShipmentZone() {
            System.out.println("Add Shipment Zone selected.");
        }

        public static void updateOrDeleteShipmentZone() {
            System.out.println("Update/Delete Shipment Zone selected.");
        }

        public static void addTruck() {
            System.out.println("Add Truck selected.");
        }

        public static void updateOrDeleteTruck() {
            System.out.println("Update/Delete Truck selected.");
        }

        public static void viewDocumentation() {
            System.out.println("View Documentation selected.");
        }

}
