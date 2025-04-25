package Domain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SupplierController {
    private static Map<Integer,Supplier> allSupplier;
    private static int num=0;
    public SupplierController(){
        if (num==0){
            allSupplier=new HashMap<>();
            num++;
        }
    }
    public boolean checkSup(int supplierNumber){
        if(allSupplier.containsKey(supplierNumber)){
            return true;
        }
        return false;
    }
    public boolean checkContactName(int supplierNumber,String name){
        if(allSupplier.get(supplierNumber).getContactNames().contains(name)){
            return true;
        }
        return false;
    }
    public int numContactNameSup(int supplierNumber){
        return allSupplier.get(supplierNumber).getContactNames().size();
    }
    public boolean checkdaysSup(int supplierNumber,String day){
        Days d=StringToEnumDays(day);
        if (d!=null) {
            if (allSupplier.get(supplierNumber).getDeliveryDays().contains(d)) {
                return true;
            }
        }
        return false;
    }
    public void setNameSup(int numSupplier,String nameSupplier){
        allSupplier.get(numSupplier).setSupplierName(nameSupplier);
    }
    public void setBankAccountSup(int numSupplier,String bankAccount){
        allSupplier.get(numSupplier).setBankAccount(bankAccount);
    }
    public void setPaymentSup(int numSupplier,String payment){
        paymentTerms p=StringToEnumPaymentTerms(payment);
        if (p!=null) {
            allSupplier.get(numSupplier).setPayment(p);
        }
    }
    public void addContactNamesSup(int numSupplier,LinkedList<String> contactNames){
        allSupplier.get(numSupplier).addContactNames(contactNames);
    }
    public void deleteContactNamesSup(int numSupplier,String contactName){
        allSupplier.get(numSupplier).deleteContactNames(contactName);
    }
    public void setTelephoneSup(int numSupplier,String telephone){
        allSupplier.get(numSupplier).setTelephone(telephone);
    }
    public void addDeliveryDaysSup(int numSupplier,LinkedList<String> days){
        LinkedList<Days> days_delivery=new LinkedList<>();
        for (String day:days){
            days_delivery.add(StringToEnumDays(day));
        }
        allSupplier.get(numSupplier).addDeliveryDays(days_delivery);
    }
    public void deleteDeliveryDaysSup(int numSupplier,String day){
        Days d= StringToEnumDays(day);
        allSupplier.get(numSupplier).deleteDeliveryDays(d);
    }
    public void setDeliverySending(int numSupplier,String deliverySending){
        Delivery d= StringToEnumDelivery(deliverySending);
        allSupplier.get(numSupplier).setDeliverySending(d);
    }

    public void addAgreement(int supplierNumber,Agreement agree){
        allSupplier.get(supplierNumber).addAgreements(agree);
    }

    public void deleteAgreement(int supplierNumber,Agreement agree){
        allSupplier.get(supplierNumber).removeAgreements(agree);
    }
    public String getName(int num){
        return allSupplier.get(num).getSupplierName();
    }


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

    public void deleteSup(int numSupplier){
        allSupplier.remove(numSupplier);
    }
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
    private paymentTerms StringToEnumPaymentTerms(String pay){
        if(pay.equals("Cash")){
            return paymentTerms.Cash;
        }
        else if(pay.equals("Credit")) {
            return paymentTerms.credit;
        }
        return null;
    }
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
