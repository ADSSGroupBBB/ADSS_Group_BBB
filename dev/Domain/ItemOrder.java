package Domain;
//a class for item in order
public class ItemOrder {
    private QuantityAgreement item;
    private int amountOrder;
    private double finalPrice;
    private double initialPrice;
    private int numOrder;
    //class constructor
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
    //set Name of Agreement
    public void setNameItem(String name){
        this.item.setNameAgreement(name);
    }
    //get name of agreement
    public String getNameItem(){return this.item.getNameAgreement();}
    //set number of Agreement
    public int getNumberItem(){return this.item.getNumberProAgreement();}
    //set unitOfMeasure for item
    public void setUnitOfMeasureItem(unit unitOfMeasure){
        this.item.setUnitOfMeasureAgreement(unitOfMeasure);
    }
    //get unitOfMeasure for item
    public unit getUnitOfMeasureItem(){
        return this.item.getUnitOfMeasureAgreement();
    }
    //set the manufacturer of item
    public void setManufacturerItem(String manufacturer) {
        this.item.setManufacturerAgreement(manufacturer);
    }
    //get the manufacturer of item
    public String getManufacturerItem(){
        return this.item.getManufacturerAgreement();
    }


    //get price item
    public double getPriceItem() {
        return this.item.getPriceAgreement();
    }

    //get catalog number
    public int getCatalogNumberItem() {
        return this.item.getCatalogNumberAgreement();
    }

    //set catalog number
    public void setCatalogNumberItem(int catalogNumber) {
        this.item.setCatalogNumberAgreement(catalogNumber);
    }
    //get amount till discount
    public int getAmountToDiscountItem() {
        return this.item.getAmountToDiscountAgreement();
    }

    //set amount till discount
    public void setAmountToDiscountItem(int amountToDiscount) {
        this.item.setAmountToDiscountAgreement(amountToDiscount);
    }

    //get how much discount
    public int getDiscountItem() {
        return this.item.getDiscountAgreement();
    }

    //set discount
    public void setDiscountItem(int discount) {
        this.item.setDiscountAgreement(discount);
    }

    //amountOrder setter
    public void setAmountOrder(int amountOrder) {
        this.amountOrder = amountOrder;
    }

    //amountOrder getter
    public int getAmountOrder() {
        return amountOrder;
    }
    //a print method for item
    //returns String
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

