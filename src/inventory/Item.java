package inventory;

import  java.util.Date;

public class Item {
    private int id;
    private String name;
    private int quantity; //כמות במלאי-סה"כ
    private int minQuantity;  //כמות מינימום- מתי צריך להזמין
    private String location;  //היכן נמצא- מדף\מחסן
    private double costPrice;  //המחיר שהספק דורש מאתינו
    private double sellPrice;  //מחיר שאנחנו דורשים מהלקוח
    private Date expireDate; //תאריך תפוגה במידה וקיים
    private Category category;
    private Discount discount;

    public Item(int id, String name, int quantity, int minQuantity, String location, double costPrice, double sellPrice, Date expireDate, Category category,Discount discount){
        this.id=id;
        this.name=name;
        this.quantity=quantity;
        this.minQuantity=minQuantity;
        this.location=location;
        this.costPrice=costPrice;
        this.sellPrice=sellPrice;
        this.expireDate=expireDate;
        this.category=category;
        this.discount = discount;

    }
    //פונקציה שבודקת אם צריך להזמין מהפריט
    public boolean isLowStock(){
        return  quantity<minQuantity;
    }

    public Category getCategory(){
        return category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public String getLocation() {
        return location;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public  String getFullCategoryPath(){
        return category != null? category.getFullPath() : "Uncategorized";
    }

    public int getId(){
        return id;
    }

    public Discount getDiscount(){
        return discount;

    }
    public  boolean hasActiveDiscount(){
        return  discount != null && discount.isActive();
    }

    public double getDiscountedPrice() {
        if (hasActiveDiscount()) {
            return sellPrice * (1 - discount.getPercentage() / 100);
        } else {
            return sellPrice;
        }
    }
}

