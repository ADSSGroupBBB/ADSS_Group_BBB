package dataAccess;

import Domain.QuantityAgreement;
import dto.PeriodAgreementDto;
import util.Database;

public class JdbcPeriodAgreementDao {
    public PeriodAgreementDto savePeriodAgreement (int supplierNumber, String date){
        try {
            Database.getConnection().setAutoCommit(false);
            String sql= """
                INSERT INTO (supplierNumber, supplierName, bankAccount, payment, contactNames, telephone, deliverySending) VALUES (?,?,?,?,?,?,?,?,?)
                """;
    }
    void addProPeriodAgreement(int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount,int amountToDiscount,int discount,int amountToOrder);
    void deleteProductStandardAgree(int numAgree,int productNumber);
    void setDateStandardAgreement(int numAgreement,String date);
    void deleteStandardAgreement(int numSup,int numAgree);
    boolean existProStandardAgreement(int numP,int numA);
    boolean existRegularAgree(int sup,int numA) ;
    QuantityAgreement productFromAgreementByIndex(int numAgree, int numP);
    int numProductOnAgree(int numAgreement);
    String printProductOnAgree(int num);
    void setNumCatalogStandard(int productNumber,int numAgree,int catalogNumber);
    void setPriceStandardAgreement(int productNumber,int numAgree,double price);
    void setAmountToDiscountStandardAgreement(int productNumber,int numAgree,int amountToDiscount);
    void setDiscountStandardAgreement(int productNumber,int numAgree,int discount);
    boolean existProByStandardSup(int numS,int numP );
}
