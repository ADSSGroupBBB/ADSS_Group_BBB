package Domain;

public class ItemOrder {
    private QuantityAgreement item;
    private int amountOrder;
    public ItemOrder(String productName,int productNumber, unit unitOfMeasure,String manufacturer, int supplierNumber,int price,int catalogNumber,int amountToDiscount,int discount,int amountOrder){
        this.item=new QuantityAgreement( productName, productNumber,  unitOfMeasure, manufacturer, price, catalogNumber, amountToDiscount, discount);
        this.amountOrder=amountOrder;
    }
    public void setNameItem(String name){
        this.item.setNameAgreement(name);
    }
    public String getNameItem(){return this.item.getNameAgreement();}
    public int getNumberItem(){return this.item.getNumberAgreement();}
    public void setUnitOfMeasureItem(unit unitOfMeasure){
        this.item.setUnitOfMeasureAgreement(unitOfMeasure);
    }
    public unit getUnitOfMeasureItem(){
        return this.item.getUnitOfMeasureAgreement();
    }
    public void setManufacturerItem(String manufacturer) {
        this.item.setManufacturerAgreement(manufacturer);
    }
    public String getManufacturerItem(){
        return this.item.getManufacturerAgreement();
    }



    public int getPriceItem() {
        return this.item.getPriceAgreement();
    }

    public void setPriceItem(int price) {
        this.item.setPriceAgreement(price);
    }

    public int getCatalogNumberItem() {
        return this.item.getCatalogNumberAgreement();
    }

    public void setCatalogNumberItem(int catalogNumber) {
        this.item.setCatalogNumberAgreement(catalogNumber);
    }

    public int getAmountToDiscountItem() {
        return this.item.getAmountToDiscountAgreement();
    }

    public void setAmountToDiscountItem(int amountToDiscount) {
        this.item.setAmountToDiscountAgreement(amountToDiscount);
    }

    public int getDiscountItem() {
        return this.item.getDiscountAgreement();
    }

    public void setDiscountItem(int discount) {
        this.item.setDiscountAgreement(discount);
    }

    public void setAmountOrder(int amountOrder) {
        this.amountOrder = amountOrder;
    }

    public int getAmountOrder() {
        return amountOrder;
    }
    public String printItem(){
        String print_product= this.item.printQuantityAgreement();
        String print_item=print_product+"\namount:"+this.amountOrder;
        return print_item;
    }
}

