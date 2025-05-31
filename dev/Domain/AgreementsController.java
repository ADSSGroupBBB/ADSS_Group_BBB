package Domain;


import Service.OrderApplication;

import java.util.*;

//a class for the controller of agreement (manager)
public class AgreementsController {
    private static AgreementsController instance;
    //private Map<Integer, Agreement> allStandardAgreements;
    //private Map<Integer, PeriodAgreement> allPeriodAgreements;
    private StandardAgreementRepository standardAgreeRepo;
    private PeriodAgreementRepository periodAgreeRepo;

    public PeriodAgreementRepository getPeriodAgreeRepo() {
        return periodAgreeRepo;
    }

    public StandardAgreementRepository getStandardAgreeRepo() {
        return standardAgreeRepo;
    }

    private AgreementsController() {
        standardAgreeRepo = new StandardAgreementRepositoryImpl();
        periodAgreeRepo=new PeriodAgreementRepositoryImpl();
    }

    public static AgreementsController getInstance() {
        if (instance == null) {
            instance = new AgreementsController();
        }
        return instance;
    }

    //a method to add a new agreement to a supplier
    //parameters:int supplierNumber,String date
    //returns an int
    public int addNewStandardAgreement(int supplierNumber,String date){
        Agreement agree=new Agreement(supplierNumber,date);
        SupplierController s=SupplierController.getInstance();
        s.addAgreement(supplierNumber,agree);
        allStandardAgreements.put(agree.getIDNumber(),agree);
        return agree.getIDNumber();
    }
    public int addNewPeriodAgreement(int supplierNumber,String date,String address,String contactPhone){
        SupplierController s=SupplierController.getInstance();
        LinkedList<Days> days=s.getDays(supplierNumber);
        PeriodAgreement agree=new PeriodAgreement(supplierNumber,date,days,address,contactPhone);
        s.addAgreement(supplierNumber,agree);
        allPeriodAgreements.put(agree.getIDNumber(),agree);
        return agree.getIDNumber();
    }
    //add product to agreemennt
    //parameters:int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount
    public void addProToStandardAgreement(int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount){
        ProductController p= ProductController.getInstance();
        Product pro=p.getPro(numPro);
        allStandardAgreements.get(id).addProductAgreement(pro,price,catalogNumber,amountToDiscount,discount);
    }
    public void addProToPeriodAgreement(int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount,int amountToOrder){
        ProductController p= ProductController.getInstance();
        Product pro=p.getPro(numPro);
        allPeriodAgreements.get(id).addProductAgreement(pro,price,catalogNumber,amountToDiscount,discount,amountToOrder);
    }
    // remove product from agreement
    //parameters: int numAgree,int productNumber
    public void deleteProductFromStandardAgree(int numAgree,int productNumber){
        allStandardAgreements.get(numAgree).removeProductAgreement(productNumber);
    }
    public void deleteProductFromPeriodAgree(int numAgree,int productNumber){
        allPeriodAgreements.get(numAgree).removeProductAgreement(productNumber);
    }
    //set date for agreement
    //parameters: int numAgreement,String date
    public void setDateStandardAgree(int numAgreement,String date){
        allStandardAgreements.get(numAgreement).setDate(date);
    }
    public void setDatePeriodAgree(int numAgreement,String date){
        allPeriodAgreements.get(numAgreement).setDate(date);
    }
    public void setAddressPeriod(int numAgreement,String address){
        allPeriodAgreements.get(numAgreement).setAddress(address);
    }
    public void setContactPhonePeriod(int numAgreement,String ContactPhone){
        allPeriodAgreements.get(numAgreement).setContactPhone(ContactPhone);
    }
    //delete agreement
    //parameters: int numSup,int numAgree
    public void deleteStandardAgree(int numSup,int numAgree){
        SupplierController s=SupplierController.getInstance();
        s.deleteAgreement(numSup,allStandardAgreements.get(numAgree));
        allStandardAgreements.remove(numAgree);
    }
    public void deletePeriodAgree(int numSup,int numAgree){
        SupplierController s=SupplierController.getInstance();
        s.deleteAgreement(numSup,allPeriodAgreements.get(numAgree));
        allPeriodAgreements.remove(numAgree);
    }
    //checks if a certain product exists in agreement
    //parameters:int numP,int numA
    //returns aa boolean value. true is such product exists and false otherwise
    public boolean existProStandardAgre(int numP,int numA){
        return allStandardAgreements.get(numA).searchProduct(numP);
    }
    public boolean existProPeriodAgre(int numP,int numA){
        return allPeriodAgreements.get(numA).searchProduct(numP);
    }
    //checks if a certain agreement exists
    //parameters:int sup,int numA
    //returns a boolean value. true if the agreement exists and false otherwise
    public boolean existRegularAgreement(int sup,int numA) {
        if (allStandardAgreements.containsKey(numA)) {
            if(allStandardAgreements.get(numA).getSupplierNumber()==sup){
                return true;
            }
        }
        return false;
    }
    public boolean existConstantAgreement(int sup,int numA) {
        if (allPeriodAgreements.containsKey(numA)) {
            if(allPeriodAgreements.get(numA).getSupplierNumber()==sup){
                return true;
            }
        }
        return false;
    }
    //find product from agreements using indexes
    //parameters:int numAgree,int numP
    //returns QuantityAgreement
    public QuantityAgreement productFromAgreeByIndex(int numAgree,int numP){
        int i=1;
        for(QuantityAgreement qa:allStandardAgreements.get(numAgree).getProductsList()){
            if(i==numP){
                return qa;
            }
            i++;
        }
        return null;
    }

    //finds the amount of products
    //parameters:int numAgreement
    //return int- the amount of products
    public int numProduct(int numAgreement){
        return allStandardAgreements.get(numAgreement).getProductsList().size();
    }

    //print the products from a specific agreement
    //parameters: int num
    //returns a String
    public String printProduct(int num){
        return allStandardAgreements.get(num).printListProducts();
    }
    //set catalog number
    //parameters: int productNumber,int numAgree,int catalogNumber
    public void setcatalogStandardNum(int productNumber,int numAgree,int catalogNumber){
        allStandardAgreements.get(numAgree).setCatalog(productNumber,catalogNumber);
    }
    public void setcatalogPeriodNum(int productNumber,int numAgree,int catalogNumber){
        allPeriodAgreements.get(numAgree).setCatalog(productNumber,catalogNumber);
    }
    //set price to product
    //parameters: int productNumber,int numAgree,double price
    public void setPriceStandardAgree(int productNumber,int numAgree,double price){
        allStandardAgreements.get(numAgree).setPrice(productNumber,price);
    }
    public void setPricePeriodAgree(int productNumber,int numAgree,double price){
        allPeriodAgreements.get(numAgree).setPrice(productNumber,price);
    }
    //set the amount for discount
    //parameters: int productNumber,int numAgree,int amountToDiscount
    public void setAmountToDiscountStandardAgree(int productNumber,int numAgree,int amountToDiscount){
        allStandardAgreements.get(numAgree).setAmountToDiscount(productNumber,amountToDiscount);
    }
    public void setAmountToDiscountPeriodAgree(int productNumber,int numAgree,int amountToDiscount){
        allPeriodAgreements.get(numAgree).setAmountToDiscount(productNumber,amountToDiscount);
    }
    public void setAmountOrder(int productNumber,int numAgree,int amount){
        allPeriodAgreements.get(numAgree).setAmount(productNumber,amount);
    }
    //set the discounted price
    //parameters: int productNumber,int numAgree,int discount
    public void setDiscountStandardAgree(int productNumber,int numAgree,int discount){
        allStandardAgreements.get(numAgree).setDiscount(productNumber,discount);
    }
    public void setDiscountPeriodAgree(int productNumber,int numAgree,int discount){
        allPeriodAgreements.get(numAgree).setDiscount(productNumber,discount);
    }
    //checks if product exists given supplier
    //parameters:int numS,int numP
    //returns a boolean value
    public boolean existProByRegularSup(int numS,int numP ) {
        for (Agreement agree: allStandardAgreements.values()){
            if(agree.getSupplierNumber()==numS){
                if(agree.searchProduct(numP))
                    return true;
            }
        }
        return false;
    }
    public boolean existProByPeriodSup(int numS,int numP ) {
        for (Agreement agree: allPeriodAgreements.values()){
            if(agree.getSupplierNumber()==numS){
                if(agree.searchProduct(numP))
                    return true;
            }
        }
        return false;
    }

}
