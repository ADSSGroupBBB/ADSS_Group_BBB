package Domain;

import dataAccess.JdbcSupplierDao;
import dataAccess.SupplierDao;
import dto.SupplierDto;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class SupplierRepositoryImpl implements SupplierRepository{
    private SupplierDao supDao;
    private Map<Integer,Supplier> supList;
    public SupplierRepositoryImpl(){
        this.supDao=new JdbcSupplierDao();
        this.supList=new HashMap<>();
    }

    @Override
    public SupplierDto addSupplier(int supplierNumber,String supplierName,String bankAccount,String payment,LinkedList<String> contactNames,String telephone,LinkedList<String> deliveryDays,String deliverySending) throws SQLException {
        paymentTerms pay =StringToEnumPaymentTerms(payment);
        Delivery del=StringToEnumDelivery(deliverySending);
        LinkedList<Days> d= new LinkedList<>();
        for (String day:deliveryDays){
            d.add(StringToEnumDays(day));
        }
        Supplier sup= new Supplier(supplierNumber, supplierName,  bankAccount,  pay, contactNames,  telephone,  d,  del, new LinkedList<Agreement>());
        supList.put(supplierNumber,sup);
        return this.supDao.saveSup(new SupplierDto(supplierNumber, supplierName,  bankAccount,  payment, contactNames,  telephone,  deliveryDays,  deliverySending, new LinkedList<Integer>());
    }
    public void removeSupplier(int supplierNumber) throws SQLException{
        if(supList.containsKey(supplierNumber)){
            supList.remove(supplierNumber); //בדיקת הסכמים
        }
        this.supDao.removeSupById(supplierNumber);
    }

    @Override
    public Optional<SupplierDto> getSupplier(int id) throws SQLException{
        if(supList.containsKey(id)){
            return Optional.of(supList.get(id).transfer());
        }
        Optional<SupplierDto> optionalSup = this.supDao.findSupById(id);
        if (optionalSup.isPresent()) {
            SupplierDto sup = optionalSup.get();
            supList.put(id, sup);
        }
        return optionalSup;
    }
    public boolean existsContactName(int supplierNumber, String contactName) throws SQLException{
        if(supList.containsKey(supplierNumber)){
            if(supList.get(supplierNumber).contactNames().contains(contactName)){
                return true;
            }
            return false;
        }
        return this.supDao.findContactName(supplierNumber,contactName);
    }
    public int numContactName(int supplierNumber) throws SQLException{
        if(supList.containsKey(supplierNumber)){
            return (supList.get(supplierNumber)).contactNames().size();
        }
        return this.supDao.getNumContactNameById(supplierNumber);
    }
    public boolean existsDay(int supplierNumber, String day) throws SQLException{
        if(supList.containsKey(supplierNumber)){
            if(supList.get(supplierNumber).deliveryDays().contains(day)){
                return true;
            }
            return false;
        }
        return this.supDao.findDayById(supplierNumber,day);
    }
    public void updateName(int numSupplier,String nameSupplier) throws SQLException{
        if(supList.containsKey(numSupplier)){
            if(supList.get(numSupplier).s)){
                return true;
            }
            return false;
        }
        this.supDao.updateNameSupById(numSupplier,nameSupplier);
    }
    public void updateBankAccount(int numSupplier,String bankAccount) throws SQLException{
        this.supDao.updateBankAccountSupById(numSupplier,bankAccount);
    }
    public void updatePayment(int numSupplier,String payment) throws SQLException{
        this.supDao.updatePaymentSupById(numSupplier,payment);
    }
    public void updateTelephone(int numSupplier,String telephone) throws SQLException{
        this.supDao.updateTelephoneSupById(numSupplier,telephone);
    }

    public void updateDeliverySending(int numSupplier, String deliverySending) throws SQLException{
        this.supDao.updateDeliverySendingSupById(numSupplier,deliverySending);
    }
    public void addContactNames(int numSupplier, LinkedList<String> contactNames) throws SQLException{
        this.supDao.saveContactNamesSupById(numSupplier,contactNames);
    }
    public void removeContactNames(int numSupplier, String contactName) throws SQLException{
        this.supDao.removeContactById(numSupplier,contactName);
    }
    public void addDeliveryDays(int numSupplier, LinkedList<String> days) throws SQLException{
        this.supDao.saveDeliveryDaysSupById(numSupplier,days);
    }
    public void removeDeliveryDays(int numSupplier,String day) throws SQLException{
        this.supDao.removeDaysById(numSupplier,day);
    }
    public void addAgreementToSup(int supplierNumber,int agree_id) throws SQLException{
      this.supDao.saveAgreeById(supplierNumber,agree_id);
    }
    public void removeAgreementToSup(int supplierNumber,int agree_id) throws SQLException{
        this.supDao.removeAgreeById(supplierNumber,agree_id);
    }
    public boolean isConstantSup(int supplierNumber ) throws SQLException{
        return this.supDao.isConstantById(supplierNumber);
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
