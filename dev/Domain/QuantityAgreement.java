package Domain;

public class QuantityAgreement {
    private int numAgreement;
    private Product prod;
    private double price;
    private int catalogNumber;
    private int amountToDiscount;
    private  int discount;

    public QuantityAgreement( int numAgreement,Product prod,double price,int catalogNumber,int amountToDiscount,int discount){
        this.numAgreement=numAgreement;
        this.prod=prod;
        this.price=price;
        this.catalogNumber=catalogNumber;
        this.amountToDiscount=amountToDiscount;
        this.discount=discount;
    }

    public void setNameAgreement(String name){
        this.prod.setProductName(name);
    }
    public String getNameAgreement(){
        return this.prod.getProductName();
    }
    public int getNumberProAgreement(){
        return this.prod.getProductNumber();
    }
    public void setUnitOfMeasureAgreement(unit unitOfMeasure){
        this.prod.setUnitOfMeasure(unitOfMeasure);
    }
    public unit getUnitOfMeasureAgreement(){
        return this.prod.getUnitOfMeasure();
    }
    public void setManufacturerAgreement(String manufacturer) {
        this.prod.setManufacturer(manufacturer);
    }
    public String getManufacturerAgreement(){
        return this.prod.getManufacturer();
    }




    public double getPriceAgreement() {
        return price;
    }

    public void setPriceAgreement(double price) {
        this.price = price;
    }

    public int getCatalogNumberAgreement() {
        return catalogNumber;
    }

    public void setCatalogNumberAgreement(int catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public int getAmountToDiscountAgreement() {
        return amountToDiscount;
    }

    public void setAmountToDiscountAgreement(int amountToDiscount) {
        this.amountToDiscount = amountToDiscount;
    }

    public int getDiscountAgreement() {
        return discount;
    }

    public void setDiscountAgreement(int discount) {
        this.discount = discount;
    }
    public String printQuantityAgreement(){
        String print_product= this.prod.printProduct();
        String print_QuantityAgreement=print_product+"\nprice:"+this.price+"\ncatalogNumber:"+this.catalogNumber+"\namount to discount:"+this.amountToDiscount+"\ndiscount:"+this.discount;
        return print_QuantityAgreement;
    }
    public String printQuantityforOrder(){
        return this.prod.printProduct();
    }
}

