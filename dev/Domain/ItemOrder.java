package Domain;

public class ItemOrder {
    private QuantityAgreement item;
    private int amountOrder;
    private double finalPrice;
    private double initialPrice;
    private int numOrder;
    public ItemOrder(QuantityAgreement item,int amountOrder,int numOrder){
        this.item=item;
        this.amountOrder=amountOrder;
        double initialPrice=amountOrder*(item.getPriceAgreement());
        this.initialPrice=initialPrice;
        if(amountOrder>=item.getAmountToDiscountAgreement()){
            this.finalPrice=initialPrice*((100-item.getDiscountAgreement())/100.0);
        }
        this.numOrder=numOrder;
    }
    public void setNameItem(String name){
        this.item.setNameAgreement(name);
    }
    public String getNameItem(){return this.item.getNameAgreement();}
    public int getNumberItem(){return this.item.getNumberProAgreement();}
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



    public double getPriceItem() {
        return this.item.getPriceAgreement();
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
        String print_product= this.item.printQuantityforOrder();
        int dis;
        if(this.amountOrder>=item.getAmountToDiscountAgreement()) {
            dis=this.item.getDiscountAgreement();
        }
        else {
            dis=0;
        }
            String print_item=print_product+"\namount:"+this.amountOrder+"\ninitial price:"+this.initialPrice+"\ndiscount:"+dis+"\nfinal price:"+this.finalPrice;
        return print_item;
    }
}

