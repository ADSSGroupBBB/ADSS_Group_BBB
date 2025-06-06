package dataAccess;

import dto.PeriodAgreementDto;
import dto.PeriodAgreementItemDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PeriodAgreementDao {
    PeriodAgreementDto savePeriod(PeriodAgreementDto agree) throws SQLException;
    void addProById(int id, PeriodAgreementItemDto pro)  throws SQLException;
    void removeProById(int numAgree,int productNumber) throws SQLException;
    void removePeriod(int numAgree)throws SQLException;
    Optional<PeriodAgreementDto> findPeriodAgreeById(int numA) throws SQLException;
    void updateAddressPeriodById(int numAgreement,String address) throws SQLException;
    void updateContactPhonePeriodById(int numAgreement,String ContactPhone) throws SQLException;
    void updateAmountById(int numAgreement,int productNumber,int amount) throws SQLException;
    List<PeriodAgreementDto> findAllPeriodAgreeBySupId(int numS)throws SQLException;

}
