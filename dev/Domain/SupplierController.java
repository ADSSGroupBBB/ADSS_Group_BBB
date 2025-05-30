package Domain;

import dto.SupplierDto;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
//a class for the manager (the controller) of supplier
public class SupplierController {
    private static SupplierController instance; // single instance
    //private Map<Integer, Supplier> allSupplier; // map of all suppliers
    private SupplierRepository supRepo;

    // private constructor to prevent direct instantiation
    private SupplierController() {
        this.supRepo = new SupplierRepositoryImpl();
    }

    // public method to access the single instance
    public static SupplierController getInstance() {
        if (instance == null) {
            instance = new SupplierController();
        }
        return instance;
    }

    //check if a certain supplier exists
    //parameters:int supplierNumber
    //returns a boolean value . if it exists it returns true ,and false otherwise
    public boolean checkSup(int supplierNumber){
        return this.supRepo.getSupplier(supplierNumber).isPresent();
    }
    //check if supplier has a certain name in its contact list
    //returns a boolean value
    public boolean checkContactName(int supplierNumber,String name){
        if(this.supRepo.existsContactName(supplierNumber,name)){
            return true;
        }
        return false;
    }
    //get amount of contact list
    public int numContactNameSup(int supplierNumber){
        return this.supRepo.numContactName(supplierNumber);
    }
    //check if supplier comes in certain days or only if ordered
    //returns a boolean value
    public boolean checkdaysSup(int supplierNumber,String day){
        return this.supRepo.existsDay(supplierNumber,day);
    }
    //NameSup setter
    public void setNameSup(int numSupplier,String nameSupplier){
        this.supRepo.updateName(numSupplier,nameSupplier);
    }
    //BankAccountSup setter
    public void setBankAccountSup(int numSupplier,String bankAccount){
        this.supRepo.updateBankAccount(numSupplier,bankAccount);
    }
    //PaymentSup setter
    public void setPaymentSup(int numSupplier,String payment){
        this.supRepo.updatePayment(numSupplier,payment);
    }
    //add contact name to supplier
    //parameters :int numSupplier,LinkedList<String> contactNames
    public void addContactNamesSup(int numSupplier,LinkedList<String> contactNames){
        this.supRepo.addContactNames(numSupplier,contactNames);
    }
    //delete contact name from supplier
    //parameters :int numSupplier,String contactName
    public void deleteContactNamesSup(int numSupplier,String contactName){
        this.supRepo.removeContactNames(numSupplier,contactName);
    }
    //TelephoneSup setter
    public void setTelephoneSup(int numSupplier,String telephone){
        this.supRepo.updateTelephone(numSupplier,telephone);
    }
    //add delivery days to supplier
    //parameters:int numSupplier,LinkedList<String> days
    public void addDeliveryDaysSup(int numSupplier,LinkedList<String> days){
        this.supRepo.addDeliveryDays(numSupplier,days);
    }
    //removes delivery days from supplier
    //parameters:int numSupplier,String day
    public void deleteDeliveryDaysSup(int numSupplier,String day){
        this.supRepo.removeDeliveryDays(numSupplier,day);
    }
    //DeliverySending setter
    public void setDeliverySending(int numSupplier,String deliverySending){
       this.supRepo.updateDeliverySending(numSupplier,deliverySending);
    }
    //add agreement to supplier
    //parameters:int supplierNumber,Agreement agree
    public void addAgreement(int supplierNumber,Agreement agree){
        this.supRepo.addAgreementToSup(supplierNumber,agree.getIDNumber());
    }

    //deletes agreement from supplier
    //parameters:int supplierNumber,Agreement agree
    public void deleteAgreement(int supplierNumber,Agreement agree){
        this.supRepo.removeAgreementToSup(supplierNumber,agree.getIDNumber());
    }
    //name getter
    public String getName(int num){
        Optional<SupplierDto> supDto=this.supRepo.getSupplier(num);
        if (supDto.isPresent()) {
            return supDto.get().supplierName();
        }
        return "";
    }

    //adds a new supplier
    //parameters:int supplierNumber,String supplierName,String bankAccount,String payment,LinkedList<String> contactNames,String telephone,LinkedList<String> deliveryDays,String deliverySending
    public void addNewSupplier(int supplierNumber,String supplierName,String bankAccount,String payment,LinkedList<String> contactNames,String telephone,LinkedList<String> deliveryDays,String deliverySending){
        this.supRepo.addSupplier(supplierNumber,supplierName,bankAccount,payment,contactNames,telephone,deliveryDays,deliverySending);
    }
    //delete supplier
    //parameter:int numSupplier
    public void deleteSup(int numSupplier){
        AgreementsController ac=AgreementsController.getInstance();
        Optional<SupplierDto> supDto=this.supRepo.getSupplier(numSupplier);
        if (supDto.isPresent()) {
            LinkedList<Integer> agrees= supDto.get().agreementsId();
            for (int numAgree: agrees){
                if (ac.existConstantAgreement(numSupplier,numAgree)){
                    ac.deleteStandardAgree(numSupplier,numAgree);
                }
                else {
                    ac.deletePeriodAgree(numSupplier,numAgree);
                }
            }
        }
        this.supRepo.removeSupplier(numSupplier);
    }
    public boolean isConstantSup(int numSupplier){
        return this.supRepo.isConstantSup(numSupplier);
    }
    public LinkedList<String> getDays(int numSupplier){
        Optional<SupplierDto> supDto=this.supRepo.getSupplier(numSupplier);
        if (supDto.isPresent()) {
            return supDto.get().deliveryDays();
        }
        return new LinkedList<>();
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
