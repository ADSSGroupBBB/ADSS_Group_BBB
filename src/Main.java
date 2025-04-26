import java.util.List;
import java.util.Date;
import inventory.Category;
import inventory.Discount;
import inventory.Item;
import inventory.Inventory;

public class Main {
    public static void main(String[] args) {
//        Category dairy= new Category("Dairy", null);
//        Category milk= new Category("Milk", dairy);
//        Category smallMilk= new Category("1 Litter", milk);
//
//        Item item= new Item(1," milk 3%",5,10, "Shelf 3B", 3.00, 5.50, new java.util.Date(), smallMilk);
//
//        System.out.println("Item category path: "+ item.getFullCategoryPath());
//
//        System.out.println("Need restock? "+ item.isLowStock());
//
//        Inventory inventory= new Inventory();
//        inventory.addItem(item);
//
//        List<Item> lowStock=inventory.getItemBelowMinQuantity();
//        System.out.println("Item below minimum quantity: "+ lowStock.size());

        Category cosmetics = new Category("Cosmetics", null);

        // יצירת הנחה תקפה (ליום אחד)
        Discount weekendSale = new Discount(
                10.0,
                new Date(System.currentTimeMillis() - 100000),  // התחיל לפני דקה
                new Date(System.currentTimeMillis() + 86400000) // נגמר מחר
        );

        // יצירת פריט ללא הנחה
        Item shampoo = new Item(
                1,
                "Shampoo",
                20,
                5,
                "B2",
                10.0,
                20.0,
                new Date(),
                cosmetics,
                null // ללא הנחה
        );

        // יצירת פריט עם הנחה פעילה
        Item shampooWithDiscount = new Item(
                2,
                "Shampoo + Discount",
                20,
                5,
                "B3",
                10.0,
                20.0,
                new Date(),
                cosmetics,
                weekendSale
        );

        // יצירת מערכת מלאי והוספת הפריטים
        Inventory inventory = new Inventory();
        inventory.addItem(shampoo);
        inventory.addItem(shampooWithDiscount);

        // הדפסה של מחירים רגילים ומוזלים
        System.out.println("---- Inventory Check ----");
        for (Item item : inventory.getAllItems()) {
            System.out.println("Name: " + item.getName());
            System.out.println("Category: " + item.getFullCategoryPath());
            System.out.println("Original Price: " + item.getSellPrice());
            System.out.println("Discounted Price: " + item.getDiscountedPrice());
            System.out.println("Needs restock? " + item.isLowStock());
            System.out.println();
        }

        // הדפסת פריטים שדורשים הזמנה
        List<Item> lowStockItems = inventory.getItemsBelowMinQuantity();
        System.out.println("Items that need restock: " + lowStockItems.size());
    }
}