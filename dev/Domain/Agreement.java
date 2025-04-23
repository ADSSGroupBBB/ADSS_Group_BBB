package Domain;

import java.util.LinkedList;

public class Agreement {
    private static int counterID=1;
    private int IDNumber;
    private int supplierNumber;
    private LinkedList<QuantityAgreement> productsList;
    private String date;

    public Agreement(int IDNumber,int supplierNumber, LinkedList<QuantityAgreement> productsList,String date){
        this.IDNumber=counterID++;
        this.supplierNumber=supplierNumber;
        this.productsList=new LinkedList<QuantityAgreement>();
        this.date=date;
    }
    public void addProductAgreement(String productName,int productNumber, unit unitOfMeasure,String manufacturer, int supplierNumber,int price,int catalogNumber,int amountToDiscount,int discount){
        this.productsList.add(new QuantityAgreement( productName, productNumber,  unitOfMeasure, manufacturer, price, catalogNumber, amountToDiscount, discount));
    }


    public void setSupplierNumber(int supplierNumber) {
        this.supplierNumber = supplierNumber;
    }

    public void setProductName(int catalogNumber,String productName) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getCatalogNumberAgreement()==catalogNumber){
                productsList.get(i).setNameAgreement(productName);
                break;
            }
        }
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
    public void setPrice(int catalogNumber,int price) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getCatalogNumberAgreement()==catalogNumber){
                productsList.get(i).setPriceAgreement(price);
                break;
            }
        }
    }
    public void setCatalog(int catalogNumber) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getCatalogNumberAgreement()==catalogNumber){
                productsList.get(i).setCatalogNumberAgreement(catalogNumber);
                break;
            }
        }
    }
    public void setAmountToDiscount(int catalogNumber,int amountToDiscount) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getCatalogNumberAgreement()==catalogNumber){
                productsList.get(i).setAmountToDiscountAgreement(amountToDiscount);
                break;
            }
        }
    }
    public void setDiscount(int catalogNumber,int discount) {
        for (int i=0;i<productsList.size();i++){
            if (productsList.get(i).getCatalogNumberAgreement()==catalogNumber){
                productsList.get(i).setAmountToDiscountAgreement(discount);
                break;
            }
        }
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String printListProducts(){
        String printList="";
        for (int i=0;i<productsList.size();i++){
            if (i==0) {
                printList = productsList.get(i).printQuantityAgreement();
            }
            else {
                printList=printList+"\n"+productsList.get(i).printQuantityAgreement();
            }
        }
        return printList;
    }
}
