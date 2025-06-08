package Service;
import Domain.AgreementsController;

import java.util.LinkedList;

public class AgreementsApplication {
    AgreementsController ac=AgreementsController.getInstance();
    public int addStandardAgreement(int supplierNumber,String date){
        return ac.addNewStandardAgreement(supplierNumber,date);
    }
    public int addPeriodAgreement(int supplierNumber,String date,String address,String contactPhone){
        return ac.addNewPeriodAgreement(supplierNumber,date,address,contactPhone);
    }
    public void addProductToStandardAgreement(int id,int numPro,double price,int catalogNumber,int amountToDiscount,int discount){
        ac.addProToStandardAgreement(id,numPro, price,catalogNumber,amountToDiscount,discount);
    }
    public void addProductToPeriodAgreement(int id,int numPro,double price,int catalogNumber,int amountToDiscount,int discount,int amountToOrder){
        ac.addProToPeriodAgreement(id,numPro, price,catalogNumber,amountToDiscount,discount,amountToOrder);
    }
    public void deleteProFromStandardAgree(int numSup,int numAgree,int productNumber){
        ac.deleteProductFromStandardAgree(numAgree,productNumber);
    }
    public void deleteProFromPeriodAgree(int numSup,int numAgree,int productNumber){
        ac.deleteProductFromPeriodAgree(numAgree,productNumber);
    }
    public void setDate(int supplierNumber,int numAgreement,String date ) {
        ac.setDatedAgree(numAgreement,date);
    }

    public void deleteStandardAgreement(int numSup,int numAgree){
        ac.deleteStandardAgree(numSup,numAgree);
    }
    public void deletePeriodAgreement(int numSup,int numAgree) {
        ac.deletePeriodAgree(numSup, numAgree);
    }
    public boolean existProductStandardAgre(int numP,int numA){
        return ac.existProStandardAgre(numP,numA);
    }
    public boolean existProductPeriodAgre(int numP,int numA){
        return ac.existProPeriodAgre(numP,numA);
    }
    public boolean existRegularAgree(int sup,int numAgree){
        return ac.existRegularAgreement(sup,numAgree);
    }
    public boolean existConstantAgree(int sup,int numAgree){
        return ac.existConstantAgreement(sup,numAgree);
    }
    public void setCatalogNumber(int productNumber,int numAgree,int catalogNumber){
        ac.setcatalogNum(productNumber,numAgree,catalogNumber);
    }
    public void setPrice(int productNumber,int numAgree,double price){
        ac.setPriceAgree(productNumber,numAgree,price);
    }
    public void setAmountToDiscount(int productNumber,int numAgree,int amountToDiscount){
        ac.setAmountToDiscountAgree(productNumber,numAgree,amountToDiscount);
    }

    public void setDiscount(int productNumber,int numAgree,int discount){
        ac.setDiscountAgree(productNumber,numAgree,discount);
    }

    public boolean existPRegular(int numS,int numP ){
        return ac.existProByRegularSup(numS,numP);
    }
    public boolean existPPeriod(int numS,int numP ){
        return ac.existProByPeriodSup(numS,numP);
    }
    public void setPeriodaddress( int numAgree,String address ){
        ac.setAddressPeriod(numAgree,address);
    }
    public void setPeriodContactPhone( int numAgree,String ContactPhone ){
        ac.setContactPhonePeriod(numAgree,ContactPhone);
    }
    public void setPeriodAmountToOrder(int productNumber, int numAgree,int amount ){
        ac.setAmountOrder(productNumber,numAgree,amount);
    }
    public boolean periodAgreementCanEdit(int numS ){
        return ac.periodAgreeCanEdit(numS);
    }


}
