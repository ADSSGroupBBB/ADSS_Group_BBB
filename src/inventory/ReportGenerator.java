package inventory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportGenerator {
    private Inventory inventory;

    public ReportGenerator(Inventory inventory) {
        this.inventory = inventory;
    }

    // הפקת דוח פריטים מתחת לרף המינימום
    public Report generateLowStockReport() {
        List<Item> lowStockItems = inventory.getItemsBelowMinQuantity();
        return new Report(new Date(), lowStockItems);
    }

    // הפקת דוח לפי קטגוריה
    public Report generateReportByCategory(String categoryName) {
        List<Item> result = new ArrayList<>();
        for (Item item : inventory.getAllItems()) {
            if (item.getCategory() != null && item.getCategory().getFullPath().contains(categoryName)) {
                result.add(item);
            }
        }
        return new Report(new Date(), result);
    }

    // הפקת דוח לפי בעיה (Expired, Damaged וכו')
    public List<ItemReport> generateIssueReport(String issueType) {
        return inventory.getReportsByIssueType(issueType);
    }
}
