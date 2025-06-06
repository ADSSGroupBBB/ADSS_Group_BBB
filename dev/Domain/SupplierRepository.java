package Domain;

import dto.SupplierDto;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public interface SupplierRepository {
    SupplierDto addSupplier(int supplierNumber,String supplierName,String bankAccount,String payment,LinkedList<String> contactNames,String telephone,LinkedList<String> deliveryDays,String deliverySending) throws SQLException;
    void removeSupplier(int supplierNumber) throws SQLException;
    Optional<SupplierDto> getSupplier(int id) throws SQLException;
    String ContactName(int supplierNumber) throws SQLException;
    LinkedList<String> getDaysById(int numSupplier) throws SQLException;
    void updateName(int numSupplier,String nameSupplier) throws SQLException;
    void updateBankAccount(int numSupplier,String bankAccount) throws SQLException;
    void updatePayment(int numSupplier,String payment) throws SQLException;
    void updateTelephone(int numSupplier,String telephone) throws SQLException;
    void updateDeliverySending(int numSupplier,String deliverySending) throws SQLException;
    void updateContactNames(int numSupplier, LinkedList<String> contactNames) throws SQLException;
    void addDeliveryDays(int numSupplier, LinkedList<String> days) throws SQLException;
    void removeDeliveryDays(int numSupplier,String day) throws SQLException;
    void addAgreementToSup(int supplierNumber,int agree_id) throws SQLException;
    void removeAgreementToSup(int supplierNumber,int agree_id) throws SQLException;
    String getNameById(int supplierNumber) throws SQLException;
    //boolean isConstantSup(int supplierNumber ) throws SQLException;

}
