package Service;
import Domain.AgreementsController;

import java.util.LinkedList;

public class AgreementsApplication {
    AgreementsController ac=AgreementsController.getInstance();
    public int addAgreement(int supplierNumber,String date){
        return ac.addNewAgreement(supplierNumber,date);
    }
    public void addProductToAgreement(int id,int numPro,double price,int catalogNumber,int amountToDiscount,int discount){
        ac.addProToAgreement(id,numPro, price,catalogNumber,amountToDiscount,discount);
    }
    public void deleteProFromAgree(int numSup,int numAgree,int productNumber){
        ac.deleteProductFromAgree(numAgree,productNumber);
    }
    public void setDate(int supplierNumber,int numAgreement,String date ) {
        ac.setDateAgree(numAgreement,date);
    }
    public void deleteAgreement(int numSup,int numAgree){
        ac.deleteAgree(numSup,numAgree);
    }
    public boolean existProductAgre(int numP,int numA){
        return ac.existProAgre(numP,numA);
    }
    public boolean existAgree(int sup,int numAgree){
        return ac.existAgreement(sup,numAgree);
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
    public boolean existP(int numS,int numP ){
        return ac.existProBySup(numS,numP);
    }

}
