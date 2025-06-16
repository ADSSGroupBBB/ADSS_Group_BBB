package Domain;

import dataAccess.JdbcSupplierDao;
import dataAccess.SupplierDao;
import dto.AgreementDto;
import dto.SupplierDto;

import java.sql.SQLException;
import java.util.*;

public class SupplierRepositoryImpl implements SupplierRepository{
    private static SupplierRepositoryImpl instance;
    private SupplierDao supDao;
    private Map<Integer, Supplier> supList;

    private SupplierRepositoryImpl() {
        this.supDao = new JdbcSupplierDao();
        this.supList = new HashMap<>();
    }


    public static SupplierRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new SupplierRepositoryImpl();
        }
        return instance;
    }

    @Override
    public SupplierDto addSupplier(int supplierNumber,String supplierName,String bankAccount,String payment,LinkedList<String> contactNames,String telephone,LinkedList<String> deliveryDays,String deliverySending,String address,String contactPhone) throws SQLException {
        paymentTerms pay =SupplierMapper.StringToEnumPaymentTerms(payment);
        Delivery del=SupplierMapper.StringToEnumDelivery(deliverySending);
        LinkedList<Days> d= new LinkedList<>();
        for (String day:deliveryDays){
            d.add(SupplierMapper.StringToEnumDays(day));
        }
        Supplier sup= new Supplier(supplierNumber, supplierName,  bankAccount,  pay, contactNames,  telephone,  d,  del, new LinkedList<Agreement>(),address,contactPhone);
        SupplierDto dto = this.supDao.saveSup(new SupplierDto(supplierNumber, supplierName, bankAccount, payment, contactNames, telephone, deliveryDays, deliverySending, new LinkedList<>(), address, contactPhone));
        supList.put(supplierNumber, sup);
        return dto;
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
            return Optional.of(SupplierMapper.transfer(supList.get(id)));
        }
        Optional<SupplierDto> optionalSup = this.supDao.findSupById(id);

        if (optionalSup.isPresent()) {
            SupplierDto sup = optionalSup.get();
            supList.put(id, SupplierMapper.toObject(sup));
        }
        return optionalSup;
    }
    public LinkedList<String> getDaysById(int numSupplier) throws SQLException{
        Optional<SupplierDto> supDto=getSupplier(numSupplier);
        if (supDto.isPresent()) {
            return supDto.get().deliveryDays();
        }
        return new LinkedList<>();
    }

    public String ContactName(int supplierNumber) throws SQLException{
        if(supList.containsKey(supplierNumber)){
            String contactsString = String.join(",", supList.get(supplierNumber).getContactNames());
            return contactsString;
        }
         return this.supDao.getContactNameById(supplierNumber);
    }

    public void updateName(int numSupplier,String nameSupplier) throws SQLException{
        this.supDao.updateNameSupById(numSupplier,nameSupplier);
        if(supList.containsKey(numSupplier)){
            (supList.get(numSupplier)).setSupplierName(nameSupplier);
        }
    }
    public void updateAddress(int numSupplier,String address) throws SQLException{
        this.supDao.updateAddressSupById(numSupplier,address);
        if(supList.containsKey(numSupplier)){
            (supList.get(numSupplier)).setAddress(address);
        }
    }
    public void updateContactPhone(int numSupplier,String contactPhone) throws SQLException{
        this.supDao.updateContactPhoneSupById(numSupplier,contactPhone);
        if(supList.containsKey(numSupplier)){
            (supList.get(numSupplier)).setContactPhone(contactPhone);
        }
    }
    public void updateBankAccount(int numSupplier,String bankAccount) throws SQLException{
        this.supDao.updateBankAccountSupById(numSupplier,bankAccount);
        if(supList.containsKey(numSupplier)){
            (supList.get(numSupplier)).setBankAccount(bankAccount);
        }
    }
    public void updatePayment(int numSupplier,String payment) throws SQLException{
        this.supDao.updatePaymentSupById(numSupplier,payment);
        if(supList.containsKey(numSupplier)){
            (supList.get(numSupplier)).setPayment(SupplierMapper.StringToEnumPaymentTerms(payment));
        }
    }
    public void updateTelephone(int numSupplier,String telephone) throws SQLException{
        this.supDao.updateTelephoneSupById(numSupplier,telephone);
        if(supList.containsKey(numSupplier)){
            (supList.get(numSupplier)).setTelephone(telephone);
        }
    }

    public void updateDeliverySending(int numSupplier, String deliverySending) throws SQLException{
        this.supDao.updateDeliverySendingSupById(numSupplier,deliverySending);
        if(supList.containsKey(numSupplier)){
            (supList.get(numSupplier)).setDeliverySending(SupplierMapper.StringToEnumDelivery(deliverySending));
        }
    }
    public void updateContactNames(int numSupplier, LinkedList<String> contactNames) throws SQLException{
        this.supDao.saveContactNamesSupById(numSupplier,contactNames);
        if(supList.containsKey(numSupplier)){
            (supList.get(numSupplier)).setContactNames(contactNames);
        }
    }

    public void addDeliveryDays(int numSupplier, LinkedList<String> days) throws SQLException{
        if(supList.containsKey(numSupplier)) {
            LinkedList<Days> d = new LinkedList<>();
            for (String day : days) {
                d.add(SupplierMapper.StringToEnumDays(day));
            }
            (supList.get(numSupplier)).addDeliveryDays(d);
        }
        this.supDao.saveDeliveryDaysSupById(numSupplier,days);
    }
    public void removeDeliveryDays(int numSupplier,String day) throws SQLException{
        if(supList.containsKey(numSupplier)){
            (supList.get(numSupplier)).deleteDeliveryDays(SupplierMapper.StringToEnumDays(day));
        }
        this.supDao.removeDaysById(numSupplier,day);
    }
    public void addAgreementToSup(int supplierNumber,int agree_id) throws SQLException{
      this.supDao.saveAgreeById(supplierNumber,agree_id);
    }
    public void removeAgreementToSup(int supplierNumber,int agree_id) throws SQLException{
        this.supDao.removeAgreeById(supplierNumber,agree_id);
    }
    public  String getNameById(int supplierNumber) throws SQLException{
        Optional<SupplierDto> supDto = getSupplier(supplierNumber);
        if (supDto.isPresent()) {
            return supDto.get().supplierName();
        }
        return "";
    }
}
