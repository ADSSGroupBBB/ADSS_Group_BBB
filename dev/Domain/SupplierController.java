package Domain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
//a class for the manager (the controller) of supplier
public class SupplierController {
    private static Map<Integer,Supplier> allSupplier;   //a map of all the suppliers
    private static int num=0;   //a num to check if there is at least 1 supplier
    //a default constructor
    public SupplierController(){
        if (num==0){
            allSupplier=new HashMap<>();
            num++;
        }
    }
    //check if a certain supplier exists
    //parameters:int supplierNumber
    //returns a boolean value . if it exists it returns true ,and false otherwise
    public boolean checkSup(int supplierNumber){
        if(allSupplier.containsKey(supplierNumber)){
            return true;
        }
        return false;
    }
    //check if supplier has a certain name in its contact list
    //returns a boolean value
    public boolean checkContactName(int supplierNumber,String name){
        if(allSupplier.get(supplierNumber).getContactNames().contains(name)){
            return true;
        }
        return false;
    }
    //get amount of contact list
    public int numContactNameSup(int supplierNumber){
        return allSupplier.get(supplierNumber).getContactNames().size();
    }
    //check if supplier comes in certain days or only if ordered
    //returns a boolean value
    public boolean checkdaysSup(int supplierNumber,String day){
        Days d=StringToEnumDays(day);
        if (d!=null) {
            if (allSupplier.get(supplierNumber).getDeliveryDays().contains(d)) {
                return true;
            }
        }
        return false;
    }
    //NameSup setter
    public void setNameSup(int numSupplier,String nameSupplier){
        allSupplier.get(numSupplier).setSupplierName(nameSupplier);
    }
    //BankAccountSup setter
    public void setBankAccountSup(int numSupplier,String bankAccount){
        allSupplier.get(numSupplier).setBankAccount(bankAccount);
    }
    //PaymentSup setter
    public void setPaymentSup(int numSupplier,String payment){
        paymentTerms p=StringToEnumPaymentTerms(payment);
        if (p!=null) {
            allSupplier.get(numSupplier).setPayment(p);
        }
    }
    //add contact name to supplier
    //parameters :int numSupplier,LinkedList<String> contactNames
    public void addContactNamesSup(int numSupplier,LinkedList<String> contactNames){
        allSupplier.get(numSupplier).addContactNames(contactNames);
    }
    //delete contact name from supplier
    //parameters :int numSupplier,String contactName
    public void deleteContactNamesSup(int numSupplier,String contactName){
        allSupplier.get(numSupplier).deleteContactNames(contactName);
    }
    //TelephoneSup setter
    public void setTelephoneSup(int numSupplier,String telephone){
        allSupplier.get(numSupplier).setTelephone(telephone);
    }
    //add delivery days to supplier
    //parameters:int numSupplier,LinkedList<String> days
    public void addDeliveryDaysSup(int numSupplier,LinkedList<String> days){
        LinkedList<Days> days_delivery=new LinkedList<>();
        for (String day:days){
            days_delivery.add(StringToEnumDays(day));
        }
        allSupplier.get(numSupplier).addDeliveryDays(days_delivery);
    }
    //removes delivery days from supplier
    //parameters:int numSupplier,String day
    public void deleteDeliveryDaysSup(int numSupplier,String day){
        Days d= StringToEnumDays(day);
        allSupplier.get(numSupplier).deleteDeliveryDays(d);
    }
    //DeliverySending setter
    public void setDeliverySending(int numSupplier,String deliverySending){
        Delivery d= StringToEnumDelivery(deliverySending);
        allSupplier.get(numSupplier).setDeliverySending(d);
    }
    //add agreement to supplier
    //parameters:int supplierNumber,Agreement agree
    public void addAgreement(int supplierNumber,Agreement agree){
        allSupplier.get(supplierNumber).addAgreements(agree);
    }

    //deletes agreement from supplier
    //parameters:int supplierNumber,Agreement agree
    public void deleteAgreement(int supplierNumber,Agreement agree){
        allSupplier.get(supplierNumber).removeAgreements(agree);
    }
    //name getter
    public String getName(int num){
        return allSupplier.get(num).getSupplierName();
    }

    //adds a new supplier
    //parameters:int supplierNumber,String supplierName,String bankAccount,String payment,LinkedList<String> contactNames,String telephone,LinkedList<String> deliveryDays,String deliverySending
    public void addNewSupplier(int supplierNumber,String supplierName,String bankAccount,String payment,LinkedList<String> contactNames,String telephone,LinkedList<String> deliveryDays,String deliverySending){
        paymentTerms pay;
        Delivery del;
        LinkedList<Days> days_delivery=new LinkedList<>();
        pay=StringToEnumPaymentTerms(payment);
        for (String day:deliveryDays){
            days_delivery.add(StringToEnumDays(day));
            }

        if (deliverySending.equals("constant")){
            del=Delivery.constant;
        }
        else if(deliverySending.equals("invitation")){
            del=Delivery.invitation;
        }
        else {
            del=Delivery.selfCollection;
        }

        Supplier sup=new Supplier(supplierNumber,supplierName,bankAccount,pay,contactNames,telephone,days_delivery,del,new LinkedList<Agreement>());
        allSupplier.put(supplierNumber,sup);
    }
    //delete supplier
    //parameter:int numSupplier
    public void deleteSup(int numSupplier){
        allSupplier.remove(numSupplier);
    }
    //turns a String to Days type
    //parameter:String day
    //returns type Days
    private Days StringToEnumDays(String day){
        if(day.equals("Sunday")){
            return Days.Sunday;
        }
        else if(day.equals("Monday")){
            return Days.Monday;
        }
        else if(day.equals("Tuesday")){
            return Days.Tuesday;
        }
        else if(day.equals("Wednesday")){
            return Days.Wednesday;
        }
        else if(day.equals("Thursday")){
            return Days.Thursday;
        }
        else if(day.equals("Friday")){
            return Days.Friday;
        }
        else {
            return null;
        }
    }
    //turns String to PaymentTerms
    //parameters:String pay
    //returns type paymentTerms
    private paymentTerms StringToEnumPaymentTerms(String pay){
        if(pay.equals("Cash")){
            return paymentTerms.Cash;
        }
        else if(pay.equals("Credit")) {
            return paymentTerms.credit;
        }
        return null;
    }
    //turns String to Delivery
    //parameters:String delivery
    //returns type Delivery
    private Delivery StringToEnumDelivery(String delivery){
        if (delivery.equals("constant")){
            return Delivery.constant;
        }
        else if(delivery.equals("invitation")){
            return Delivery.invitation;
        }
        else if(delivery.equals("selfCollection")){
            return Delivery.selfCollection;
        }
        return null;
    }

}
