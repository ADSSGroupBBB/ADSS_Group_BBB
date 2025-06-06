package Domain;

import java.util.LinkedList;
//a class to represent an agreement between the store and the supplier
public class Agreement {
    protected static int counterID=1; //a counter to keep track of the amount of agreements
    protected int IDNumber;   //agreement ID
    protected int supplierNumber; //the supplier this agreement belongs to
    protected LinkedList<QuantityAgreement> productsList; //the list of products for this agreement
    protected String date;    //the date the agreement was made

    public String getDate() {
        return date;
    }

    //the agreement constructor
    //gets supplierNumber, date
    public Agreement(int supplierNumber,String date){
        this.IDNumber=counterID++;
        this.supplierNumber=supplierNumber;
        this.productsList=new LinkedList<QuantityAgreement>();
        this.date=date;

    }
    public Agreement(int IDNumber, int supplierNumber, LinkedList<QuantityAgreement> productsList, String date) {
        this.IDNumber = IDNumber;
        this.supplierNumber = supplierNumber;
        this.productsList = productsList;
        this.date = date;
    }

    //a method to add product to an agreement
    //gets: Product, double,int(catalogNumber),int(amountToDiscount),int(discount)
    public void addProductAgreement(Product p, double price,int catalogNumber,int amountToDiscount,int discount){
        this.productsList.add(new QuantityAgreement(this.IDNumber,  p, price, catalogNumber, amountToDiscount, discount));
    }
    //removes product from agreement
    //gets type int
    public void removeProductAgreement(int proN){
        for(QuantityAgreement pro: this.productsList){
            if(pro.getNumberProAgreement()==proN){
                this.productsList.remove(pro);
                break;
            }
        }
    }
    //searches for a product in agreement
    //gets type int
    //return boolean value. true if product exists in agreement and false otherwise
    public boolean searchProduct(int proN) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getNumberProAgreement()==proN){
                return true;
            }
        }
        return false;
    }
    //set a unit for a product
    //gets type int and unit
    public void setUnitOfMeasure(int catalogNumber,unit unitOfMeasure) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getCatalogNumberAgreement()==catalogNumber){
                productsList.get(i).setUnitOfMeasureAgreement(unitOfMeasure);
                break;
            }
        }
    }
    //set the name of the manufacturer
    //ghts type int(catalogNumber) and String(the name of the manufacturer)
    public void setManufacturer(int catalogNumber,String manufacturer) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getCatalogNumberAgreement()==catalogNumber){
                productsList.get(i).setManufacturerAgreement(manufacturer);
                break;
            }
        }
    }
    //set price for the product
    //gets type int (productnumber) and double (price)
    public void setPrice(int proN,double price) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getNumberProAgreement()==proN){
                productsList.get(i).setPriceAgreement(price);
                break;
            }
        }
    }
    //set catalog number for product
    //gets the productNumber and catalogNumber
    public void setCatalog(int proN,int catalogNumber) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getNumberProAgreement()==proN){
                productsList.get(i).setCatalogNumberAgreement(catalogNumber);
                break;
            }
        }
    }
    //set the amount of product until unlocking discount
    //gets productNumber and the amount until discount
    public void setAmountToDiscount(int proN,int amountToDiscount) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getNumberProAgreement()==proN){
                productsList.get(i).setAmountToDiscountAgreement(amountToDiscount);
                break;
            }
        }
    }
    // productsList getter
    public LinkedList<QuantityAgreement> getProductsList() {
        return productsList;
    }

    //set the discount for product
    //gets productNumber and the discount amount
    public void setDiscount(int proN, int discount) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getNumberProAgreement()==proN){
                productsList.get(i).setAmountToDiscountAgreement(discount);
                break;
            }
        }
    }

    //set data
    //gets type String
    public void setDate(String date) {
        this.date = date;
    }


    //IDNumber getter
    public int getIDNumber() {
        return IDNumber;
    }
    //supplierNumber getter
    public int getSupplierNumber() {
        return supplierNumber;
    }

    //method to print all product in agreement
    //returns String
    public String printListProducts(){
        String printList=""+this.IDNumber+":\n";
        for (int i=0;i<productsList.size();i++){
            if (i==0) {
                printList =(i+1)+"." +productsList.get(i).printQuantityAgreement();
            }
            else {
                printList=printList+"\n"+(i+1)+"."+productsList.get(i).printQuantityAgreement();
            }
        }
        return printList;
    }
}
