package Domain;

import dto.AgreementDto;
import dto.QuantityAgreementDto;
import dto.SupplierDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface   StandardAgreementRepository {
    AgreementDto saveStandardAgreement (int supplierNumber,String date)  throws SQLException;
    void addProStandardAgreement(int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount) throws SQLException;
    void deleteProductStandardAgree(int numAgree,int productNumber) throws SQLException;
    void setDateStandardAgreement(int numAgreement,String date) throws SQLException;
    void deleteStandardAgreement(int numSup,int numAgree) throws SQLException;
    Optional<AgreementDto> getStandardAgreement(int numA ) throws SQLException;
    void setNumCatalogStandard(int numAgree,int productNumber,int catalogNumber) throws SQLException;
    void setPriceStandardAgreement(int numAgree,int productNumber ,double price) throws SQLException;
    void setAmountToDiscountStandardAgreement(int productNumber,int numAgree,int amountToDiscount) throws SQLException;
    void setDiscountStandardAgreement(int productNumber,int numAgree,int discount) throws SQLException;
    List<AgreementDto> allAgreementsByStandardSup(int numS ) throws SQLException;
    QuantityAgreementDto proFromAgreeByIndex(int numAgree, int numP) throws SQLException;
    List<AgreementDto> getAllStandardAgreeWithPro(int numP) throws SQLException;
}
