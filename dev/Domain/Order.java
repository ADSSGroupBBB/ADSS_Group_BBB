package Domain;

import java.util.LinkedList;
//a class to represent the supplier orders
public class Order {
    private int orderNumber;//order ID
    private int numAgreement;
    private String supplierName;    //the name of the supplier the order is from
    private int supplierNumber; //the number of the supplier the order is from
    private String address; //the address for shipment
    private String date;    //the date the order was made
    private String contactPhone;    //contact info (phone number)
    private LinkedList<ItemOrder> items;    //the items in the order
    private Status statusOrder; //the status of the order



    //order constructor
    //parameters: int orderNumber,String supplierName,int supplierNumber,String address,String date,String contactPhone,Status statusOrder
    public Order(int orderNumber,int numAgreement,String supplierName,int supplierNumber,String address,String date,String contactPhone,Status statusOrder){
        this.orderNumber=orderNumber;
        this.numAgreement=numAgreement;
        this.supplierName=supplierName;
        this.supplierNumber=supplierNumber;
        this.address=address;
        this.date=date;
        this.contactPhone=contactPhone;
        this.items=new LinkedList<ItemOrder>();
        this.statusOrder=statusOrder;
    }
    //add product to order
    //parameters: QuantityAgreement item,int amountOrder
    //returns false if the product already was in the order and true if it wasn't
    public boolean addProductOrder(QuantityAgreement item,int amountOrder) {
        ItemOrder it = new ItemOrder(item, amountOrder,this.orderNumber);
        for (ItemOrder i : this.items) {
            if (i.getNumberItem()==it.getNumberItem()) {
                return false;
            }
        }
        this.items.add(it);
        return true;
    }

    public LinkedList<ItemOrder> getItems() {
        return items;
    }

    //supplierName setter
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    //supplierNumber setter
    public void setSupplierNumber(int supplierNumber) {
        this.supplierNumber = supplierNumber;
    }

    //address setter
    public void setAddress(String address) {
        this.address = address;
    }

    //date setter
    public void setDate(String date) {
        this.date = date;
    }

    //contactPhone setter
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    //setter: set productName to a product given the catalogNumber
    //parameters:int catalogNumber,String productName
    public void setProductName(int catalogNumber,String productName) {
        for (int i=0;i<items.size();i++){
            if (items.get(i).getCatalogNumberItem()==catalogNumber){
                items.get(i).setNameItem(productName);
                break;
            }
        }
    }
    //setter: set the unitOfMeasure given the catalogNumber
    //parameters: int catalogNumber,unit unitOfMeasure
    public void setUnitOfMeasure(int catalogNumber,unit unitOfMeasure) {
        for (int i=0;i<items.size();i++){
            if (items.get(i).getCatalogNumberItem()==catalogNumber){
                items.get(i).setUnitOfMeasureItem(unitOfMeasure);
                break;
            }
        }
    }
    //setter: set the manufacturer given the catalogNumber
    //parameters: int catalogNumber,String manufacturer
    public void setManufacturer(int catalogNumber,String manufacturer) {
        for (int i=0;i<items.size();i++){
            if (items.get(i).getCatalogNumberItem()==catalogNumber){
                items.get(i).setManufacturerItem(manufacturer);
                break;
            }
        }
    }

    //catalogNumber setter
    public void setCatalog(int catalogNumber) {
        for (int i=0;i<items.size();i++){
            if (items.get(i).getCatalogNumberItem()==catalogNumber){
                items.get(i).setCatalogNumberItem(catalogNumber);
                break;
            }
        }
    }
    //setter: set the amountToDiscount given the catalogNumber
    //parameters:int catalogNumber,int amountToDiscount
    public void setAmountToDiscount(int catalogNumber,int amountToDiscount) {
        for (int i=0;i<items.size();i++){
            if (items.get(i).getCatalogNumberItem()==catalogNumber){
                items.get(i).setAmountToDiscountItem(amountToDiscount);
                break;
            }
        }
    }
    //setter: set the discount given the catalogNumber
    //parameters:int catalogNumber, int discount
    public void setDiscount(int catalogNumber,int discount) {
        for (int i=0;i<items.size();i++){
            if (items.get(i).getCatalogNumberItem()==catalogNumber){
                items.get(i).setDiscountItem(discount);
                break;
            }
        }
    }

    //statusOrder setter
    public void setStatusOrder(Status statusOrder) {
        this.statusOrder = statusOrder;
    }

    //orderNumber getter
    public int getOrderNumber() {
        return orderNumber;
    }

    //supplierName getter
    public String getSupplierName() {
        return supplierName;
    }
    //supplierNumber getter
    public int getSupplierNumber() {
        return supplierNumber;
    }
    //address getter
    public String getAddress() {
        return address;
    }
    //date getter
    public String getDate() {
        return date;
    }
    //contactPhone getter
    public String getContactPhone() {
        return contactPhone;
    }

    //statusOrder getter
    public Status getStatusOrder() {
        return statusOrder;
    }

    //a method to print the order detail
    //returns String
    public String print_Order(){
        String printList="";
        for (int i=0;i<items.size();i++){
                printList=printList+"\n"+items.get(i).printItem();
            }
        String printO="orderNumber:"+this.orderNumber+"\nsupplierName:"+this.supplierName+"\nsupplierNumber:"+this.supplierNumber+"\naddress:"+this.address+"\ndate:"+this.date+"\ntelephone:"+this.contactPhone+printList+"\nstatus:"+this.statusOrder;
        return printO;
    }

    public int getNumAgreement() {
        return numAgreement;
    }

}
