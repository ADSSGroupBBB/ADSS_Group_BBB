package Domain;

import java.util.LinkedList;

public class Supplier {
    private int supplierNumber;
    private String supplierName;
    private  String bankAccount;
    //
    // תנאי תשלום?
    private LinkedList<String> contactNames;
    private String telephone;
    private LinkedList<Days> deliveryDays;
    private LinkedList<Agreement> agreements;

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setAgreements(LinkedList<Agreement> agreements) {
        this.agreements = agreements;
    }

    public void setDeliveryDays(LinkedList<Days> deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    public void setContactNames(LinkedList<String> contactNames) {
        this.contactNames = contactNames;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Supplier(int supplierNumber, String supplierName, String bankAccount, LinkedList<String> contactNames, String telephone, LinkedList<Days> deliveryDays){
        this.supplierNumber=supplierNumber;
        this.supplierName=supplierName;
        this.bankAccount=bankAccount;
        this.contactNames=contactNames;
        this.telephone=telephone;
        this.deliveryDays=deliveryDays;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setSupplierNumber(int supplierNumber) {
        this.supplierNumber = supplierNumber;
    }
}
