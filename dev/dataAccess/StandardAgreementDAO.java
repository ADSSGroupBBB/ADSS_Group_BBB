package dataAccess;

import dto.AgreementDto;
import dto.QuantityAgreementDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface StandardAgreementDAO {
    AgreementDto saveStandard(AgreementDto agree) throws SQLException;
    void addProById(int id, QuantityAgreementDto pro)throws SQLException;
    void removeProById(int numAgree, int productNumber)throws SQLException;
    void updateDateById(int numAgreement,String date) throws SQLException;
    void removeStandard(int numAgree)throws SQLException;
    Optional<AgreementDto> findStandardAgreeById(int numA) throws SQLException;
    void updateCatalogById(int numAgree,int productNumber,int catalogNumber) throws SQLException;
    void updatePriceById(int numAgree,int productNumber,double price) throws SQLException;
    void updateAmountToDiscountById(int numAgree,int productNumber,int amountToDiscount) throws SQLException;
    void updateDiscountById(int numAgree,int productNumber,int discount) throws SQLException;
    List<AgreementDto> findAllStandardAgreeBySupId(int numS) throws SQLException;

}
