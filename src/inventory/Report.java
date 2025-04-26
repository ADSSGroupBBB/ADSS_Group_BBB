package inventory;

import java.util.Date;
import java.util.List;

public class Report {
    private Date date;
    private List<Item> items;

    public Report(Date date, List<Item> items) {
        this.date = date;
        this.items = items;
    }

    public Date getDate() {
        return date;
    }

    public List<Item> getItems() {
        return items;
    }

    // הצגת תוכן הדוח (לא חובה אבל נוח לניפוי שגיאות)
    public void printReport() {
        System.out.println("Inventory Report - " + date);
        for (Item item : items) {
            System.out.println("Name: " + item.getName());
            System.out.println("Quantity: " + item.getQuantity());
            System.out.println("Min Quantity: " + item.getMinQuantity());
            System.out.println("Location: " + item.getLocation());
            System.out.println("Category: " + item.getFullCategoryPath());
            System.out.println("Needs restock? " + item.isLowStock());
            System.out.println("----------------------");
        }
    }
}

