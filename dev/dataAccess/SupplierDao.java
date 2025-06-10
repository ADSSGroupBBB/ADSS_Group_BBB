package dataAccess;

import dto.SupplierDto;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Optional;

public interface SupplierDao {
    SupplierDto saveSup(SupplierDto sup) throws SQLException;
    String getContactNameById(int supplierNumber) throws SQLException;
     boolean findDayById(int supplierNumber, String day) throws SQLException;
     void updateNameSupById(int numSupplier,String nameSupplier) throws SQLException;
     void updateBankAccountSupById(int numSupplier,String bankAccount) throws SQLException;
     void updatePaymentSupById(int numSupplier,String payment) throws SQLException;
     void updateTelephoneSupById(int numSupplier,String telephone) throws SQLException;
     void updateDeliverySendingSupById(int numSupplier,String deliverySending) throws SQLException;
     void saveContactNamesSupById(int numSupplier, LinkedList<String> contactNames) throws SQLException;
     void saveDeliveryDaysSupById(int numSupplier, LinkedList<String> days) throws SQLException;
     void removeDaysById(int numSupplier,String day) throws SQLException;
     void saveAgreeById(int supplierNumber,int agree_id) throws SQLException;
     void removeAgreeById(int supplierNumber,int agree_id) throws SQLException;
     boolean isConstantById(int supplierNumber ) throws SQLException;
    Optional<SupplierDto> findSupById(int id) throws SQLException;
     void removeSupById(int id) throws SQLException;
     void updateAddressSupById (int numSupplier,String address) throws SQLException;
     void updateContactPhoneSupById (int numSupplier,String contactPhone) throws SQLException;
}
