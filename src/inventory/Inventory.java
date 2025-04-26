package inventory;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private List<Item> items;
    private List<ItemReport> reports;

    public Inventory(){
        this.items=new ArrayList<>();
        this.reports = new ArrayList<>();

    }
    //הוספת פריט
    public  void addItem(Item item){
        items.add(item);
    }
    //הסרת פריט לפי מזהה
    public boolean removeItemById(int id){
        return items.removeIf(item -> item.getId() == id);
    }

    //חיפוש פריט לפי מזהה
    public  Item findItemById(int id) {
        for (Item item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    //שליפת כל הפריטים במלאי
    public  List<Item> getAllItems(){
        return items;
    }

    //שליפת פירטים שצריך להזמין--> מתחת למינימום
    public List<Item> getItemsBelowMinQuantity(){
        List <Item> lowStock=new ArrayList<>();
        for (Item item :items){
            if (item.isLowStock()){
                lowStock.add(item);
            }
        }
        return  lowStock;
    }

    // הוספת דיווח
    public void addReport(ItemReport report) {
        reports.add(report);
    }

    // שליפת כל הדיווחים
    public List<ItemReport> getAllReports() {
        return reports;
    }

    // שליפת דיווחים לפי סוג בעיה
    public List<ItemReport> getReportsByIssueType(String type) {
        List<ItemReport> result = new ArrayList<>();
        for (ItemReport report : reports) {
            if (report.getIssueType().equalsIgnoreCase(type)) {
                result.add(report);
            }
        }
        return result;
    }
}

