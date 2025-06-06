package Domain;

import dto.AgreementDto;
import dto.PeriodAgreementDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PeriodAgreementRepository {
    PeriodAgreementDto savePeriodAgreement (int supplierNumber, String date,String address,String contactPhone) throws SQLException;
    void addProPeriodAgreement(int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount,int amountToOrder);
    void deleteProductPeriodAgree(int numAgree,int productNumber);
    void deletePeriodAgreement(int numSup,int numAgree);
    Optional<PeriodAgreementDto> getPeriodAgreement(int numA );
    void setAddressAgreePeriod(int numAgreement,String address);
    void setContactPhoneAgreePeriod(int numAgreement,String ContactPhone);
    void setAmountPeriodOrder(int productNumber,int numAgree,int amount);
    List<Optional<PeriodAgreementDto>> allAgreementsByPeriodSup(int numS );
}
