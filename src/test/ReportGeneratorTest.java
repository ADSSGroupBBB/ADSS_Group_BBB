package test;
import inventory.Inventory;
import inventory.Item;
import inventory.Report;
import inventory.ReportGenerator;
import inventory.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class ReportGeneratorTest {

    @Test
    public void testGenerateLowStockReport() {
        Inventory inventory = new Inventory();
        Item item = new Item(1, "Milk", 3, 10, "A1", 2.0, 5.0, new Date(), null, null);
        inventory.addItem(item);
        ReportGenerator generator = new ReportGenerator(inventory);
        Report report = generator.generateLowStockReport();
        Assertions.assertEquals(1, report.getItems().size());
    }

    @Test
    public void testGenerateReportByCategory() {
        Category dairy = new Category("Dairy", null);
        Inventory inventory = new Inventory();
        Item item = new Item(2, "Cheese", 8, 5, "A2", 3.0, 7.0, new Date(), dairy, null);
        inventory.addItem(item);
        ReportGenerator generator = new ReportGenerator(inventory);
        Report report = generator.generateReportByCategory("Dairy");
        Assertions.assertEquals(1, report.getItems().size());
    }
}
