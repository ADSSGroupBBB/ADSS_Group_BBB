package Domain;

import dto.AgreementDto;
import dto.PeriodAgreementDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PeriodAgreementRepository {
    PeriodAgreementDto savePeriodAgreement (int supplierNumber, String date,String address,String contactPhone) throws SQLException;
    void addProPeriodAgreement(int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount,int amountToOrder) throws SQLException;
    void deleteProductPeriodAgree(int numAgree,int productNumber) throws SQLException;
    void deletePeriodAgreement(int numSup,int numAgree) throws SQLException;
    Optional<PeriodAgreementDto> getPeriodAgreement(int numA ) throws SQLException;
    void setAddressAgreePeriod(int numAgreement,String address) throws SQLException;
    void setContactPhoneAgreePeriod(int numAgreement,String ContactPhone) throws SQLException;
    void setAmountPeriodOrder(int productNumber,int numAgree,int amount) throws SQLException;
    List<PeriodAgreementDto> allAgreementsByPeriodSup(int numS ) throws SQLException;
     List<PeriodAgreementDto> allPeriodAgreementsToOrder(String day,String date) throws SQLException;

    }
