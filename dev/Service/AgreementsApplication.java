package Service;
import Domain.AgreementsController;

import java.sql.SQLException;
import java.util.LinkedList;

public class AgreementsApplication {
    AgreementsController ac=AgreementsController.getInstance();
    public int addStandardAgreement(int supplierNumber,String date) throws SQLException {
        return ac.addNewStandardAgreement(supplierNumber,date);
    }
    public int addPeriodAgreement(int supplierNumber,String date,String address,String contactPhone) throws SQLException{
        return ac.addNewPeriodAgreement(supplierNumber,date,address,contactPhone);
    }
    public void addProductToStandardAgreement(int id,int numPro,double price,int catalogNumber,int amountToDiscount,int discount) throws SQLException{
        ac.addProToStandardAgreement(id,numPro, price,catalogNumber,amountToDiscount,discount);
    }
    public void addProductToPeriodAgreement(int id,int numPro,double price,int catalogNumber,int amountToDiscount,int discount,int amountToOrder) throws SQLException{
        ac.addProToPeriodAgreement(id,numPro, price,catalogNumber,amountToDiscount,discount,amountToOrder);
    }
    public void deleteProFromStandardAgree(int numSup,int numAgree,int productNumber) throws SQLException{
        ac.deleteProductFromStandardAgree(numAgree,productNumber);
    }
    public void deleteProFromPeriodAgree(int numSup,int numAgree,int productNumber) throws SQLException{
        ac.deleteProductFromPeriodAgree(numAgree,productNumber);
    }
    public void setDate(int supplierNumber,int numAgreement,String date ) throws SQLException{
        ac.setDatedAgree(numAgreement,date);
    }

    public void deleteStandardAgreement(int numSup,int numAgree) throws SQLException{
        ac.deleteStandardAgree(numSup,numAgree);
    }
    public void deletePeriodAgreement(int numSup,int numAgree) throws SQLException{
        ac.deletePeriodAgree(numSup, numAgree);
    }
    public boolean existProductStandardAgre(int numP,int numA) throws SQLException{
        return ac.existProStandardAgre(numP,numA);
    }
    public boolean existProductPeriodAgre(int numP,int numA) throws SQLException{
        return ac.existProPeriodAgre(numP,numA);
    }
    public boolean existRegularAgree(int sup,int numAgree) throws SQLException{
        return ac.existRegularAgreement(sup,numAgree);
    }
    public boolean existConstantAgree(int sup,int numAgree) throws SQLException{
        return ac.existConstantAgreement(sup,numAgree);
    }
    public void setCatalogNumber(int productNumber,int numAgree,int catalogNumber) throws SQLException{
        ac.setcatalogNum(productNumber,numAgree,catalogNumber);
    }
    public void setPrice(int productNumber,int numAgree,double price) throws SQLException{
        ac.setPriceAgree(productNumber,numAgree,price);
    }
    public void setAmountToDiscount(int productNumber,int numAgree,int amountToDiscount) throws SQLException{
        ac.setAmountToDiscountAgree(productNumber,numAgree,amountToDiscount);
    }

    public void setDiscount(int productNumber,int numAgree,int discount) throws SQLException{
        ac.setDiscountAgree(productNumber,numAgree,discount);
    }

    public boolean existPRegular(int numS,int numP ) throws SQLException{
        return ac.existProByRegularSup(numS,numP);
    }
    public boolean existPPeriod(int numS,int numP ) throws SQLException{
        return ac.existProByPeriodSup(numS,numP);
    }
    public void setPeriodaddress( int numAgree,String address ) throws SQLException{
        ac.setAddressPeriod(numAgree,address);
    }
    public void setPeriodContactPhone( int numAgree,String ContactPhone ) throws SQLException{
        ac.setContactPhonePeriod(numAgree,ContactPhone);
    }
    public void setPeriodAmountToOrder(int productNumber, int numAgree,int amount ) throws SQLException{
        ac.setAmountOrder(productNumber,numAgree,amount);
    }
    public boolean periodAgreementCanEdit(int numS ) throws SQLException{
        return ac.periodAgreeCanEdit(numS);
    }


}
