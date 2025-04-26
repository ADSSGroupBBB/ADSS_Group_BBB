package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import inventory.Item;
import inventory.ItemReport;

public class ItemReportTest {

    @Test
    public void testReportCreation() {
        Item item = new Item(1, "Milk", 5, 10, "A1", 3.0, 5.0, new Date(), null, null);
        ItemReport report = new ItemReport(item, new Date(), "Damaged", "Leaking");
        Assertions.assertEquals("Damaged", report.getIssueType());
        Assertions.assertEquals("Leaking", report.getComments());
    }
}
