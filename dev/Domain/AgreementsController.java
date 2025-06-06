package Domain;


import Service.OrderApplication;
import dto.AgreementDto;
import dto.PeriodAgreementDto;
import dto.QuantityAgreementDto;

import java.sql.SQLException;
import java.util.*;

//a class for the controller of agreement (manager)
public class AgreementsController {
    private static AgreementsController instance;
    //private Map<Integer, Agreement> allStandardAgreements;
    //private Map<Integer, PeriodAgreement> allPeriodAgreements;
    private StandardAgreementRepository standardAgreeRepo;
    private PeriodAgreementRepository periodAgreeRepo;

    //public PeriodAgreementRepository getPeriodAgreeRepo() {
    //    return periodAgreeRepo;
    //}

    //public StandardAgreementRepository getStandardAgreeRepo() {
    //    return standardAgreeRepo;
    //}

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
        return (this.standardAgreeRepo.saveStandardAgreement(supplierNumber,date)).IDNumber();
    }
    public int addNewPeriodAgreement(int supplierNumber,String date,String address,String contactPhone){
        return (this.periodAgreeRepo.savePeriodAgreement(supplierNumber,date,address,contactPhone)).agreement().IDNumber();
    }
    //add product to agreemennt
    //parameters:int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount
    public void addProToStandardAgreement(int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount){
        this.standardAgreeRepo.addProStandardAgreement(id,numPro,price,catalogNumber,amountToDiscount,discount); //The product with the identifier id has already been added to Products.
    }
    public void addProToPeriodAgreement(int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount,int amountToOrder){
        this.periodAgreeRepo.addProPeriodAgreement(id,numPro,price,catalogNumber,amountToDiscount,discount,amountToOrder);
    }
    // remove product from agreement
    //parameters: int numAgree,int productNumber
    public void deleteProductFromStandardAgree(int numAgree,int productNumber){
        this.standardAgreeRepo.deleteProductStandardAgree(numAgree,productNumber);
    }
    public void deleteProductFromPeriodAgree(int numAgree,int productNumber){
        this.periodAgreeRepo.deleteProductPeriodAgree(numAgree,productNumber);
    }
    //set date for agreement
    //parameters: int numAgreement,String date
    public void setDatedAgree(int numAgreement,String date){
        this.standardAgreeRepo.setDateStandardAgreement(numAgreement,date);
    }

    public void setAddressPeriod(int numAgreement,String address){
        this.periodAgreeRepo.setAddressAgreePeriod(numAgreement,address);
    }
    public void setContactPhonePeriod(int numAgreement,String ContactPhone){
        this.periodAgreeRepo.setContactPhoneAgreePeriod(numAgreement,ContactPhone);
    }
    //delete agreement
    //parameters: int numSup,int numAgree
    public void deleteStandardAgree(int numSup,int numAgree) throws SQLException {
        //SupplierController s=SupplierController.getInstance();
        //s.deleteAgreement(numSup,numAgree);
        this.standardAgreeRepo.deleteStandardAgreement(numSup,numAgree);
    }
    public void deletePeriodAgree(int numSup,int numAgree){
        this.periodAgreeRepo.deletePeriodAgreement(numSup,numAgree);
    }
    //checks if a certain product exists in agreement
    //parameters:int numP,int numA
    //returns aa boolean value. true is such product exists and false otherwise
    public boolean existProStandardAgre(int numP,int numA){
        return (new Agreement(this.standardAgreeRepo.getStandardAgreement(numA))).searchProduct(numP);
    }
    public boolean existProPeriodAgre(int numP,int numA){
        return (new PeriodAgreement(this.periodAgreeRepo.getPeriodAgreement(numA))).searchProduct(numP);
    }
    //checks if a certain agreement exists
    //parameters:int sup,int numA
    //returns a boolean value. true if the agreement exists and false otherwise
    public boolean existRegularAgreement(int sup,int numA) {
        Optional<AgreementDto> s=this.standardAgreeRepo.getStandardAgreement(numA);
         if(s.isPresent()) {
            if(s.get().supplierNumber()==sup){
                return true;
            }
        }
         return false;
    }
    public boolean existConstantAgreement(int sup,int numA) {
        Optional<PeriodAgreementDto> s=this.periodAgreeRepo.getPeriodAgreement(numA);
        if(s.isPresent()) {
            if(s.get().agreement().supplierNumber()==sup){
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
        Optional<AgreementDto> agree=this.standardAgreeRepo.getStandardAgreement(numAgree);
        if(agree.isPresent()) {
            List<QuantityAgreementDto> products = agree.get().productsList();
            for (QuantityAgreementDto qa : products) {
                if (i == numP) {
                    return new QuantityAgreement(qa);
                }
                i++;
            }
        }
        return null;
    }

    //finds the amount of products
    //parameters:int numAgreement
    //return int- the amount of products
    public int numProduct(int numAgreement) {
        Optional<AgreementDto> agree = this.standardAgreeRepo.getStandardAgreement(numAgreement);
        if (agree.isPresent()) {
            return agree.get().productsList().size();
        }
        return -1;//error
    }

    //print the products from a specific agreement
    //parameters: int num
    //returns a String
    public String printProduct(int num){ //only standard
        Optional<AgreementDto> agree= this.standardAgreeRepo.getStandardAgreement(num);
        if(agree.isPresent()){
            Agreement a=new Agreement(agree);
            return a.printListProducts();
        }
        return "";//error
    }
    //set catalog number
    //parameters: int productNumber,int numAgree,int catalogNumber
    public void setcatalogNum(int productNumber,int numAgree,int catalogNumber){
        this.standardAgreeRepo.setNumCatalogStandard(numAgree,productNumber,catalogNumber);
    }

    //set price to product
    //parameters: int productNumber,int numAgree,double price
    public void setPriceAgree(int productNumber,int numAgree,double price){
        this.standardAgreeRepo.setPriceStandardAgreement(numAgree,productNumber,price);
    }

    //set the amount for discount
    //parameters: int productNumber,int numAgree,int amountToDiscount
    public void setAmountToDiscountAgree(int productNumber,int numAgree,int amountToDiscount){
        this.standardAgreeRepo.setAmountToDiscountStandardAgreement(productNumber,numAgree,amountToDiscount);
    }

    public void setAmountOrder(int productNumber,int numAgree,int amount){
        this.periodAgreeRepo.setAmountPeriodOrder(productNumber,numAgree,amount);
    }
    //set the discounted price
    //parameters: int productNumber,int numAgree,int discount
    public void setDiscountAgree(int productNumber,int numAgree,int discount){
        this.standardAgreeRepo.setDiscountStandardAgreement(productNumber,numAgree,discount);
    }

    //checks if product exists given supplier
    //parameters:int numS,int numP
    //returns a boolean value
    public boolean existProByRegularSup(int numS,int numP ) {
        List<Optional<AgreementDto>> allAgree=this.standardAgreeRepo.allAgreementsByStandardSup(numS);
        for (Optional<AgreementDto> agree: allAgree){
            if(new Agreement(agree).searchProduct(numP)){
                    return true;
            }
        }
        return false;
    }
    public boolean existProByPeriodSup(int numS,int numP ) {
        List<Optional<PeriodAgreementDto>> allAgree=this.periodAgreeRepo.allAgreementsByPeriodSup(numS);
        for (Optional<PeriodAgreementDto> agree: allAgree){
            if(new PeriodAgreement(agree).searchProduct(numP)){
                return true;
            }
        }
        return false;
    }

}
