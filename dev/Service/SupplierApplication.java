package Service;

import Domain.*;

import java.util.LinkedList;

public class SupplierApplication {
    SupplierController uc= new SupplierController();
    public boolean existSupplier(int supplierNumber){
        return uc.checkSup(supplierNumber);
    }
    public boolean existContactName(int supplierNumber,String name){
        return uc.checkContactName(supplierNumber,name);
    }
    public boolean existDays(int supplierNumber,String day){
        return uc.checkdaysSup(supplierNumber,day);
    }
    public int numContactName(int supplierNumber){
        return uc.numContactNameSup(supplierNumber);
    }
    public void addSup(int supplierNumber,String supplierName,String bankAccount,String payment,LinkedList<String> contactNames,String telephone,LinkedList<String> deliveryDays,String deliverySending){
        uc.addNewSupplier(supplierNumber,supplierName,bankAccount,payment,contactNames,telephone,deliveryDays,deliverySending);
    }
    public void deleteSupplier(int numSupplier){
        uc.deleteSup(numSupplier);
    }
    public void setName(int numSupplier,String nameSupplier){
        uc.setNameSup(numSupplier,nameSupplier);
    }
    public void setBankAccount(int numSupplier,String bankAccount){
        uc.setBankAccountSup(numSupplier,bankAccount);
    }
    public void setPayment(int numSupplier,String payment){
        uc.setPaymentSup(numSupplier,payment);
    }
    public void addContactNames(int numSupplier,LinkedList<String> contactNames){
        uc.addContactNamesSup(numSupplier,contactNames);
    }
    public void deleteContactNames(int numSupplier,String contactName){
        uc.deleteContactNamesSup(numSupplier,contactName);
    }
    public void setTelephone(int numSupplier,String telephone){
        uc.setTelephoneSup(numSupplier,telephone);
    }
    public void addDeliveryDays(int numSupplier,LinkedList<String> day){
        uc.addDeliveryDaysSup(numSupplier,day);
    }
    public void deleteDeliveryDays(int numSupplier,String day){
        uc.deleteDeliveryDaysSup(numSupplier,day);
    }
    public void setDeliverySending(int numSupplier,String deliverySending){
        uc.setDeliverySending(numSupplier,deliverySending);
    }


}
