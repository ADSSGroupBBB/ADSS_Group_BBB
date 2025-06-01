package Domain;

import dto.AgreementDto;


import java.util.LinkedList;
import java.util.Map;

public class StandardAgreementRepositoryImpl implements StandardAgreementRepository{
    private Map<Integer, Agreement> allStandardAgreements;

    public AgreementDto saveStandardAgreement (int supplierNumber, String date){
        Agreement agree=new Agreement(supplierNumber,date);
        SupplierController s=SupplierController.getInstance();
        s.addAgreement(supplierNumber,agree);
        allStandardAgreements.put(agree.getIDNumber(),agree);
    }
    public void addProStandardAgreement(int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount){

    }
    public void deleteProductStandardAgree(int numAgree,int productNumber){}
    public void setDateStandardAgreement(int numAgreement,String date){}
    public void deleteStandardAgreement(int numSup,int numAgree){}
    public boolean existProStandardAgreement(int numP,int numA){}
    public boolean existRegularAgree(int sup,int numA) {}
    public QuantityAgreement productFromAgreementByIndex(int numAgree,int numP){}
    public int numProductOnAgree(int numAgreement){}
    public String printProductOnAgree(int num){}
    public void setNumCatalogStandard(int productNumber,int numAgree,int catalogNumber){}
    public void setPriceStandardAgreement(int productNumber,int numAgree,double price){}
    public void setAmountToDiscountStandardAgreement(int productNumber,int numAgree,int amountToDiscount){}
    public void setDiscountStandardAgreement(int productNumber,int numAgree,int discount){}
    public boolean existProByStandardSup(int numS,int numP ){}
    public boolean existProStandardAgreementByName(String nameP,int numA){}
    public Map<Integer, Agreement> getAllStandardAgreements() {
        return allStandardAgreements;
    }
    public QuantityAgreement productFromAgreementByName(int numAgree,String nameP{}
}
