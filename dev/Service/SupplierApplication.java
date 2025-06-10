package Service;

import Domain.*;

import java.sql.SQLException;
import java.util.LinkedList;

public class SupplierApplication {
    SupplierController sc = SupplierController.getInstance();
    public boolean existSupplier(int supplierNumber)throws SQLException {
        return sc.checkSup(supplierNumber);
    }
    public boolean existContactName(int supplierNumber,String name)throws SQLException{
        return sc.checkContactName(supplierNumber,name);
    }
    public boolean existDays(int supplierNumber,String day)throws SQLException{
        return sc.checkdaysSup(supplierNumber,day);
    }
    public int numContactName(int supplierNumber)throws SQLException{
        return sc.numContactNameSup(supplierNumber);
    }
    public void addSup(int supplierNumber,String supplierName,String bankAccount,String payment,LinkedList<String> contactNames,String telephone,LinkedList<String> deliveryDays,String deliverySending,String address,String contactPhone)throws SQLException{
        sc.addNewSupplier(supplierNumber,supplierName,bankAccount,payment,contactNames,telephone,deliveryDays,deliverySending,address,contactPhone);
    }
    public void deleteSupplier(int numSupplier)throws SQLException{
        sc.deleteSup(numSupplier);
    }
    public void setName(int numSupplier,String nameSupplier)throws SQLException{
        sc.setNameSup(numSupplier,nameSupplier);
    }
    public void setBankAccount(int numSupplier,String bankAccount)throws SQLException{
        sc.setBankAccountSup(numSupplier,bankAccount);
    }
    public void setPayment(int numSupplier,String payment)throws SQLException{
        sc.setPaymentSup(numSupplier,payment);
    }
    public void addContactNames(int numSupplier,LinkedList<String> contactNames)throws SQLException{
        sc.addContactNamesSup(numSupplier,contactNames);
    }
    public void deleteContactNames(int numSupplier,String contactName)throws SQLException{
        sc.deleteContactNamesSup(numSupplier,contactName);
    }
    public void setTelephone(int numSupplier,String telephone)throws SQLException{
        sc.setTelephoneSup(numSupplier,telephone);
    }
    public void setAddress(int numSupplier,String address)throws SQLException{
        sc.setAddressSup(numSupplier,address);
    }
    public void setContactPhone(int numSupplier,String contactPhone)throws SQLException{
        sc.setContactPhoneSup(numSupplier,contactPhone);
    }
    public void addDeliveryDays(int numSupplier,LinkedList<String> day)throws SQLException{
        sc.addDeliveryDaysSup(numSupplier,day);
    }
    public void deleteDeliveryDays(int numSupplier,String day)throws SQLException{
        sc.deleteDeliveryDaysSup(numSupplier,day);
    }
    public void setDeliverySending(int numSupplier,String deliverySending)throws SQLException{
        sc.setDeliverySending(numSupplier,deliverySending);
    }
    public boolean isConstantSupplier(int num)throws SQLException{
        return sc.isConstantSup(num);
    }



}
