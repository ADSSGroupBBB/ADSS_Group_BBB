package inventory;

import java.util.Scanner;
import java.util.Date;
import java.util.List;

public class InventoryConsole {
    private Inventory inventory;
    private ReportGenerator reportGenerator;
    private Scanner scanner;

    public InventoryConsole(Inventory inventory) {
        this.inventory = inventory;
        this.reportGenerator = new ReportGenerator(inventory);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the Inventory Management System");

        String userRole = chooseRole();

        boolean running = true;
        while (running) {
            printMenu(userRole);
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            running = handleChoice(choice, userRole);
        }
    }

    private String chooseRole() {
        System.out.println("Please select your role:");
        System.out.println("1. Worker");
        System.out.println("2. Transport Manager");
        System.out.println("3. Manager");

        int roleChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        return switch (roleChoice) {
            case 1 -> "Worker";
            case 2 -> "TransportManager";
            case 3 -> "Manager";
            default -> "Worker"; // Default role
        };
    }

    private void printMenu(String userRole) {
        System.out.println("\n=== Inventory Management Menu ===");
        System.out.println("1. Show all items");
        System.out.println("2. Show low stock items");
        System.out.println("3. Report item issue");

        if (userRole.equals("Manager") || userRole.equals("TransportManager")) {
            System.out.println("4. Show all item reports");
        }

        if (userRole.equals("Manager")) {
            System.out.println("5. Generate advanced reports (coming soon)");
        }

        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private boolean handleChoice(int choice, String userRole) {
        switch (choice) {
            case 1 -> showAllItems();
            case 2 -> showLowStockItems();
            case 3 -> reportItemIssue();
            case 4 -> {
                if (userRole.equals("Manager") || userRole.equals("TransportManager")) {
                    showItemReports();
                } else {
                    System.out.println("You do not have permission to view reports.");
                }
            }
            case 5 -> {
                if (userRole.equals("Manager")) {
                    System.out.println("Advanced reporting features will be added soon.");
                } else {
                    System.out.println("You do not have permission to generate advanced reports.");
                }
            }
            case 0 -> {
                System.out.println("Exiting the system. Goodbye!");
                return false;
            }
            default -> System.out.println("Invalid choice, please try again.");
        }
        return true;
    }

    private void showAllItems() {
        List<Item> items = inventory.getAllItems();
        for (Item item : items) {
            System.out.println(item.getName() + " | Qty: " + item.getQuantity() +
                    " | Category: " + item.getFullCategoryPath() +
                    " | Discounted price: " + item.getDiscountedPrice());
        }
    }

    private void showLowStockItems() {
        List<Item> lowStock = inventory.getItemsBelowMinQuantity();
        if (lowStock.isEmpty()) {
            System.out.println("All stock levels are sufficient.");
        } else {
            for (Item item : lowStock) {
                System.out.println(item.getName() + " | Qty: " + item.getQuantity());
            }
        }
    }

    private void reportItemIssue() {
        System.out.print("Enter the item ID to report: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Item item = inventory.findItemById(id);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        System.out.print("Enter the issue type (Expired/Damaged): ");
        String type = scanner.nextLine();

        System.out.print("Enter additional comments: ");
        String comment = scanner.nextLine();

        ItemReport report = new ItemReport(item, new Date(), type, comment);
        inventory.addReport(report);
        System.out.println("Report added successfully.");
    }

    private void showItemReports() {
        List<ItemReport> reports = inventory.getAllReports();
        if (reports.isEmpty()) {
            System.out.println("No reports available.");
        } else {
            for (ItemReport r : reports) {
                r.printReportEntry();
            }
        }
    }
}
