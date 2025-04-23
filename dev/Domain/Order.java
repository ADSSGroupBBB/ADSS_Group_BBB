package Domain;

import java.util.LinkedList;

public class Order {
    private int orderNumber;
    private String supplierName;
    private int supplierNumber;
    private String address;
    private String date;
    private String contactPhone;
    private LinkedList<ItemOrder> items;
    private Status statusOrder;
    public Order(int orderNumber,String supplierName,int supplierNumber,String address,String date,String contactPhone,Status statusOrder){
        this.orderNumber=orderNumber;
        this.supplierName=supplierName;
        this.supplierNumber=supplierNumber;
        this.address=address;
        this.date=date;
        this.contactPhone=contactPhone;
        this.items=new LinkedList<ItemOrder>();
        this.statusOrder=statusOrder;
    }
    public void addProductOrder(String productName,int productNumber, unit unitOfMeasure,String manufacturer, int supplierNumber,int price,int catalogNumber,int amountToDiscount,int discount,int amountOrder){
        this.items.add(new ItemOrder( productName, productNumber,  unitOfMeasure, manufacturer,  supplierNumber, price, catalogNumber, amountToDiscount, discount, amountOrder));
    }


    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setSupplierNumber(int supplierNumber) {
        this.supplierNumber = supplierNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void setProductName(int catalogNumber,String productName) {
        for (int i=0;i<items.size();i++){
            if (items.get(i).getCatalogNumberItem()==catalogNumber){
                items.get(i).setNameItem(productName);
                break;
            }
        }
    }
    public void setUnitOfMeasure(int catalogNumber,unit unitOfMeasure) {
        for (int i=0;i<items.size();i++){
            if (items.get(i).getCatalogNumberItem()==catalogNumber){
                items.get(i).setUnitOfMeasureItem(unitOfMeasure);
                break;
            }
        }
    }
    public void setManufacturer(int catalogNumber,String manufacturer) {
        for (int i=0;i<items.size();i++){
            if (items.get(i).getCatalogNumberItem()==catalogNumber){
                items.get(i).setManufacturerItem(manufacturer);
                break;
            }
        }
    }
    public void setPrice(int catalogNumber,int price) {
        for (int i=0;i<items.size();i++){
            if (items.get(i).getCatalogNumberItem()==catalogNumber){
                items.get(i).setPriceItem(price);
                break;
            }
        }
    }
    public void setCatalog(int catalogNumber) {
        for (int i=0;i<items.size();i++){
            if (items.get(i).getCatalogNumberItem()==catalogNumber){
                items.get(i).setCatalogNumberItem(catalogNumber);
                break;
            }
        }
    }
    public void setAmountToDiscount(int catalogNumber,int amountToDiscount) {
        for (int i=0;i<items.size();i++){
            if (items.get(i).getCatalogNumberItem()==catalogNumber){
                items.get(i).setAmountToDiscountItem(amountToDiscount);
                break;
            }
        }
    }
    public void setDiscount(int catalogNumber,int discount) {
        for (int i=0;i<items.size();i++){
            if (items.get(i).getCatalogNumberItem()==catalogNumber){
                items.get(i).setDiscountItem(discount);
                break;
            }
        }
    }

    public void setStatusOrder(Status statusOrder) {
        this.statusOrder = statusOrder;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public int getSupplierNumber() {
        return supplierNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getContactPhone() {
        return contactPhone;
    }


    public Status getStatusOrder() {
        return statusOrder;
    }
    public String print_Order(){
        String printList="";
        for (int i=0;i<items.size();i++){
                printList=printList+"\n"+items.get(i).printItem();
            }

        String printO="orderNumber:"+this.orderNumber+"\nsupplierName:"+this.supplierName+"\nsupplierNumber:"+this.supplierNumber+"\naddress:"+this.address+"\ndate:"+this.date+"\ntelephone:"+this.contactPhone+printList+"\nstatus:"+this.statusOrder;
        return printO;
    }

}
