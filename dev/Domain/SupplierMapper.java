package Domain;

import dto.SupplierDto;

import java.util.LinkedList;

public class SupplierMapper {
    public static SupplierDto transfer(Supplier sup) {
        LinkedList<Days> d = sup.getDeliveryDays();
        LinkedList<String> days =new LinkedList<>();
        for (Days day : d) {
            days.add(day.name());
        }
        LinkedList<Agreement> a=sup.getAgreements();
        LinkedList<Integer> aID= new LinkedList<>();
        for (Agreement agreement:a){
            aID.add(agreement.getIDNumber());
        }
        return new SupplierDto(sup.getSupplierNumber(),sup.getSupplierName(), sup.getBankAccount() ,(sup.getPayment()).name(),sup.getContactNames(),sup.getTelephone(),days,(sup.getDeliverySending()).name(),aID,sup.getAddress(),sup.getContactPhone());
    }
    public static Supplier toObject(SupplierDto sup) {

        LinkedList<Days> d=new LinkedList<>();
        for (String day:sup.deliveryDays()){
            d.add(StringToEnumDays(day));
        }
        return new Supplier(sup.supplierNumber(),sup.supplierName(),sup.bankAccount(),StringToEnumPaymentTerms(sup.payment()),sup.contactNames(),sup.telephone(),d,StringToEnumDelivery(sup.deliverySending()),new LinkedList<Agreement>(),sup.address(),sup.contactPhone());
    }


//turns a String to Days type
//parameter:String day
//returns type Days
public static Days StringToEnumDays(String day){
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
public static paymentTerms StringToEnumPaymentTerms(String pay){
    if(pay.equals("Cash")){
        return paymentTerms.Cash;
    }
    else if(pay.equals("Credit")) {
        return paymentTerms.Credit;
    }
    return null;
}
//turns String to Delivery
//parameters:String delivery
//returns type Delivery
public static Delivery StringToEnumDelivery(String delivery){
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