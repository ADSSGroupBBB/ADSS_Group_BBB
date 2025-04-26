package inventory;

import java.util.Date;

public class ItemReport {
    private Item item;            // הפריט שדווח לגביו
    private Date reportDate;      // תאריך הדיווח
    private String issueType;     // סוג הבעיה: "Low Stock", "Expired", "Damaged"
    private String comments;      // הערות נוספות מהעובד (אם צריך)

    public ItemReport(Item item, Date reportDate, String issueType, String comments) {
        this.item = item;
        this.reportDate = reportDate;
        this.issueType = issueType;
        this.comments = comments;
    }

    public Item getItem() {
        return item;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public String getIssueType() {
        return issueType;
    }

    public String getComments() {
        return comments;
    }

    public void printReportEntry() {
        System.out.println("---- Item Report ----");
        System.out.println("Item: " + item.getName());
        System.out.println("Issue: " + issueType);
        System.out.println("Date: " + reportDate);
        System.out.println("Notes: " + comments);
        System.out.println("---------------------");
    }
}

