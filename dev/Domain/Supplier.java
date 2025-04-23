package Domain;

import java.util.LinkedList;

public class Supplier {
    private int supplierNumber;
    private String supplierName;
    private  String bankAccount;
    private paymentTerms payment;
    private LinkedList<String> contactNames;
    private String telephone;
    private LinkedList<Days> deliveryDays;
    private Delivery deliverySending;
    private LinkedList<Agreement> agreements;



    public Supplier(int supplierNumber, String supplierName, String bankAccount, paymentTerms payment,LinkedList<String> contactNames, String telephone, LinkedList<Days> deliveryDays, Delivery deliverySending,LinkedList<Agreement> agreements){
        this.supplierNumber=supplierNumber;
        this.supplierName=supplierName;
        this.bankAccount=bankAccount;
        this.payment=payment;
        this.contactNames=contactNames;
        this.telephone=telephone;
        this.deliveryDays=deliveryDays;
        this.deliverySending=deliverySending;
        this.agreements=agreements;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setSupplierNumber(int supplierNumber) {
        this.supplierNumber = supplierNumber;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean existProduct(int numP){
        for (Agreement agr: agreements){
            for (QuantityAgreement p:agr.getProductsList()){
                if (p.getNumberAgreement()==numP)
                    return true;
            }
        }
        return false;

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
    public void printProducts(){

    }

}
