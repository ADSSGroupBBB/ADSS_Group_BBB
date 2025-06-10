package Domain;

import dto.SupplierDto;
import util.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

//a class for the manager (the controller) of supplier
public class SupplierController {
    private static SupplierController instance; // single instance
    //private Map<Integer, Supplier> allSupplier; // map of all suppliers
    private SupplierRepository supRepo;

    // private constructor to prevent direct instantiation
    private SupplierController() {
        this.supRepo =  SupplierRepositoryImpl.getInstance();
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
    public boolean checkSup(int supplierNumber) throws SQLException {
        return this.supRepo.getSupplier(supplierNumber).isPresent();
    }
    //check if supplier has a certain name in its contact list
    //returns a boolean value
    public boolean checkContactName(int supplierNumber,String name) throws SQLException{
        String contact=this.supRepo.ContactName(supplierNumber);
        LinkedList<String> contactNamesList = new LinkedList<>(Arrays.asList(contact.split(",")));
        for (String con:contactNamesList) {
            if (con.trim().equalsIgnoreCase(name.trim())){
                return true;
            }
        }
        return false;
    }
    //get amount of contact list
    public int numContactNameSup(int supplierNumber) throws SQLException{
        int count=0;
        String contact=this.supRepo.ContactName(supplierNumber);
        LinkedList<String> contactNamesList = new LinkedList<>(Arrays.asList(contact.split(",")));
        for (String con:contactNamesList){
            count++;
        }
        return count;
    }
    //check if supplier comes in certain days or only if ordered
    //returns a boolean value
    public boolean checkdaysSup(int supplierNumber,String day) throws SQLException{
        return getDays(supplierNumber).contains(day);
    }
    //NameSup setter
    public void setNameSup(int numSupplier,String nameSupplier) throws SQLException{
        this.supRepo.updateName(numSupplier,nameSupplier);
    }
    //BankAccountSup setter
    public void setBankAccountSup(int numSupplier,String bankAccount) throws SQLException{
        this.supRepo.updateBankAccount(numSupplier,bankAccount);
    }
    //PaymentSup setter
    public void setPaymentSup(int numSupplier,String payment) throws SQLException{
        this.supRepo.updatePayment(numSupplier,payment);
    }
    //add contact name to supplier
    //parameters :int numSupplier,LinkedList<String> contactNames
    public void addContactNamesSup(int numSupplier,LinkedList<String> contactNames) throws SQLException{
        String contact=this.supRepo.ContactName(numSupplier);
        LinkedList<String> contactNamesList = new LinkedList<>(Arrays.asList(contact.split(",")));
        contactNames.addAll(contactNamesList);
        this.supRepo.updateContactNames(numSupplier,contactNames);
    }
    //delete contact name from supplier
    //parameters :int numSupplier,String contactName
    public void deleteContactNamesSup(int numSupplier,String contactName) throws SQLException{
        String contact=this.supRepo.ContactName(numSupplier);
        LinkedList<String> contactNamesList = new LinkedList<>(Arrays.asList(contact.split(",")));
        contactNamesList.remove(contactName);
        this.supRepo.updateContactNames(numSupplier,contactNamesList);
    }

    //TelephoneSup setter
    public void setTelephoneSup(int numSupplier,String telephone) throws SQLException{
        this.supRepo.updateTelephone(numSupplier,telephone);
    }
    //add delivery days to supplier
    //parameters:int numSupplier,LinkedList<String> days
    public void addDeliveryDaysSup(int numSupplier,LinkedList<String> days) throws SQLException{
        this.supRepo.addDeliveryDays(numSupplier,days);
    }
    //removes delivery days from supplier
    //parameters:int numSupplier,String day
    public void deleteDeliveryDaysSup(int numSupplier,String day) throws SQLException{
        this.supRepo.removeDeliveryDays(numSupplier,day);
    }
    //DeliverySending setter
    public void setDeliverySending(int numSupplier,String deliverySending) throws SQLException{
       this.supRepo.updateDeliverySending(numSupplier,deliverySending);
    }
    public void setAddressSup(int numSupplier,String address) throws SQLException{
        this.supRepo.updateAddress(numSupplier,address);
    }
    public void setContactPhoneSup(int numSupplier,String contactPhone) throws SQLException{
        this.supRepo.updateContactPhone(numSupplier,contactPhone);
    }
    //add agreement to supplier
    //parameters:int supplierNumber,Agreement agree
    public void addAgreement(int supplierNumber,Agreement agree) throws SQLException{
        this.supRepo.addAgreementToSup(supplierNumber,agree.getIDNumber());
    }

    //deletes agreement from supplier
    //parameters:int supplierNumber,Agreement agree
    public void deleteAgreement(int supplierNumber,int agreeId) throws SQLException{
        this.supRepo.removeAgreementToSup(supplierNumber,agreeId);
    }
    //name getter
    public String getName(int num) throws SQLException{
        Optional<SupplierDto> supDto=this.supRepo.getSupplier(num);
        if (supDto.isPresent()) {
            return supDto.get().supplierName();
        }
        return "";
    }
    public String getAddress(int num) throws SQLException{
        Optional<SupplierDto> supDto=this.supRepo.getSupplier(num);
        if (supDto.isPresent()) {
            return supDto.get().address();
        }
        return "";
    }
    public String getContactPhone(int num) throws SQLException{
        Optional<SupplierDto> supDto=this.supRepo.getSupplier(num);
        if (supDto.isPresent()) {
            return supDto.get().contactPhone();
        }
        return "";
    }

    //adds a new supplier
    //parameters:int supplierNumber,String supplierName,String bankAccount,String payment,LinkedList<String> contactNames,String telephone,LinkedList<String> deliveryDays,String deliverySending
    public void addNewSupplier(int supplierNumber,String supplierName,String bankAccount,String payment,LinkedList<String> contactNames,String telephone,LinkedList<String> deliveryDays,String deliverySending,String address,String contactPhone) throws SQLException{
        this.supRepo.addSupplier(supplierNumber,supplierName,bankAccount,payment,contactNames,telephone,deliveryDays,deliverySending,address,contactPhone);
    }
    //delete supplier
    //parameter:int numSupplier
    public void deleteSup(int numSupplier) throws SQLException{
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
    public boolean isConstantSup(int numSupplier) throws SQLException{
        if(getDays(numSupplier).size()!=0) {
            return true;
        }
        return false;
    }
    public LinkedList<String> getDays(int numSupplier) throws SQLException{
        return this.supRepo.getDaysById(numSupplier);
    }


}
