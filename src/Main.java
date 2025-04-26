import inventory.Inventory;
import inventory.InventoryConsole;
import inventory.SampleDataInitializer;

public class Main {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        SampleDataInitializer.loadSampleData(inventory); // טעינת נתוני דוגמה
        InventoryConsole console = new InventoryConsole(inventory);
        console.start(); // מתחילים את המערכת
    }

}