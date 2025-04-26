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
        boolean running = true;
        while (running) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            switch (choice) {
                case 1 -> showAllItems();
                case 2 -> showLowStockItems();
                case 3 -> reportItemIssue();
                case 4 -> showItemReports();
                case 0 -> {
                    System.out.println("Exiting system. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== Inventory Management ===");
        System.out.println("1. Show all items");
        System.out.println("2. Show low stock items");
        System.out.println("3. Report item issue (expired/damaged)");
        System.out.println("4. Show all item reports");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
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
        System.out.print("Enter item ID to report: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Item item = inventory.findItemById(id);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        System.out.print("Enter issue type (Expired/Damaged): ");
        String type = scanner.nextLine();

        System.out.print("Enter comment: ");
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
