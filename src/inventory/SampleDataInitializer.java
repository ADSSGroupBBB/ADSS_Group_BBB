package inventory;

import java.util.Date;

public class SampleDataInitializer {

    public static void loadSampleData(Inventory inventory) {
        // קטגוריות
        Category dairy = new Category("Dairy", null);
        Category milk = new Category("Milk", dairy);
        Category toiletries = new Category("Toiletries", null);

        // הנחה
        Discount milkDiscount = new Discount(
                10.0,
                new Date(System.currentTimeMillis() - 100000), // התחיל לפני דקה
                new Date(System.currentTimeMillis() + 86400000) // מסתיים מחר
        );

        // פריטים
        Item milk1 = new Item(1, "Tnuva Milk 1L", 5, 10, "Shelf A1", 3.00, 5.50, new Date(), milk, milkDiscount);
        Item milk2 = new Item(2, "Yotvata Milk 1L", 12, 10, "Shelf A2", 3.20, 5.70, new Date(), milk, null);
        Item shampoo = new Item(3, "Shampoo 250ml", 8, 5, "Shelf B1", 4.00, 8.00, new Date(), toiletries, null);

        inventory.addItem(milk1);
        inventory.addItem(milk2);
        inventory.addItem(shampoo);

        // דיווח חוסרים לדוגמה
        ItemReport report = new ItemReport(milk1, new Date(), "Low Stock", "Only 5 units left");
        inventory.addReport(report);
    }
}

