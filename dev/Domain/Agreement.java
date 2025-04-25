package Domain;

import java.util.LinkedList;

public class Agreement {
    private static int counterID=1;
    private int IDNumber;
    private int supplierNumber;
    private LinkedList<QuantityAgreement> productsList;
    private String date;

    public Agreement(int supplierNumber,String date){
        this.IDNumber=counterID++;
        this.supplierNumber=supplierNumber;
        this.productsList=new LinkedList<QuantityAgreement>();
        this.date=date;
    }
    public void addProductAgreement(Product p, double price,int catalogNumber,int amountToDiscount,int discount){
        this.productsList.add(new QuantityAgreement(this.IDNumber,  p, price, catalogNumber, amountToDiscount, discount));
    }
    public void removeProductAgreement(int proN){
        for(QuantityAgreement pro: this.productsList){
            if(pro.getNumberProAgreement()==proN){
                this.productsList.remove(pro);
                break;
            }
        }
    }



    public boolean searchProduct(int proN) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getNumberProAgreement()==proN){
                return true;
            }
        }
        return false;
    }
    public void setUnitOfMeasure(int catalogNumber,unit unitOfMeasure) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getCatalogNumberAgreement()==catalogNumber){
                productsList.get(i).setUnitOfMeasureAgreement(unitOfMeasure);
                break;
            }
        }
    }
    public void setManufacturer(int catalogNumber,String manufacturer) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getCatalogNumberAgreement()==catalogNumber){
                productsList.get(i).setManufacturerAgreement(manufacturer);
                break;
            }
        }
    }
    public void setPrice(int proN,double price) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getNumberProAgreement()==proN){
                productsList.get(i).setPriceAgreement(price);
                break;
            }
        }
    }
    public void setCatalog(int proN,int catalogNumber) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getNumberProAgreement()==proN){
                productsList.get(i).setCatalogNumberAgreement(catalogNumber);
                break;
            }
        }
    }
    public void setAmountToDiscount(int proN,int amountToDiscount) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getNumberProAgreement()==proN){
                productsList.get(i).setAmountToDiscountAgreement(amountToDiscount);
                break;
            }
        }
    }

    public LinkedList<QuantityAgreement> getProductsList() {
        return productsList;
    }

    public void setDiscount(int proN, int discount) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getNumberProAgreement()==proN){
                productsList.get(i).setAmountToDiscountAgreement(discount);
                break;
            }
        }
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIDNumber() {
        return IDNumber;
    }

    public int getSupplierNumber() {
        return supplierNumber;
    }

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
