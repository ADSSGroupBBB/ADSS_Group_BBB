package Service;

import Domain.*;

import java.util.LinkedList;

public class SupplierApplication {
    SupplierController sc = SupplierController.getInstance();
    public boolean existSupplier(int supplierNumber){
        return sc.checkSup(supplierNumber);
    }
    public boolean existContactName(int supplierNumber,String name){
        return sc.checkContactName(supplierNumber,name);
    }
    public boolean existDays(int supplierNumber,String day){
        return sc.checkdaysSup(supplierNumber,day);
    }
    public int numContactName(int supplierNumber){
        return sc.numContactNameSup(supplierNumber);
    }
    public void addSup(int supplierNumber,String supplierName,String bankAccount,String payment,LinkedList<String> contactNames,String telephone,LinkedList<String> deliveryDays,String deliverySending){
        sc.addNewSupplier(supplierNumber,supplierName,bankAccount,payment,contactNames,telephone,deliveryDays,deliverySending);
    }
    public void deleteSupplier(int numSupplier){
        sc.deleteSup(numSupplier);
    }
    public void setName(int numSupplier,String nameSupplier){
        sc.setNameSup(numSupplier,nameSupplier);
    }
    public void setBankAccount(int numSupplier,String bankAccount){
        sc.setBankAccountSup(numSupplier,bankAccount);
    }
    public void setPayment(int numSupplier,String payment){
        sc.setPaymentSup(numSupplier,payment);
    }
    public void addContactNames(int numSupplier,LinkedList<String> contactNames){
        sc.addContactNamesSup(numSupplier,contactNames);
    }
    public void deleteContactNames(int numSupplier,String contactName){
        sc.deleteContactNamesSup(numSupplier,contactName);
    }
    public void setTelephone(int numSupplier,String telephone){
        sc.setTelephoneSup(numSupplier,telephone);
    }
    public void addDeliveryDays(int numSupplier,LinkedList<String> day){
        sc.addDeliveryDaysSup(numSupplier,day);
    }
    public void deleteDeliveryDays(int numSupplier,String day){
        sc.deleteDeliveryDaysSup(numSupplier,day);
    }
    public void setDeliverySending(int numSupplier,String deliverySending){
        sc.setDeliverySending(numSupplier,deliverySending);
    }


}
