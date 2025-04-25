package Domain;

import java.util.LinkedList;

public class Supplier {
    private int supplierNumber;
    private String supplierName;
    private  String bankAccount;
    private paymentTerms payment;

    public String getSupplierName() {
        return supplierName;
    }

    private LinkedList<String> contactNames;
    private String telephone;
    private LinkedList<Days> deliveryDays;
    private Delivery deliverySending;
    private LinkedList<Agreement> agreements;


    public void setPayment(paymentTerms payment) {
        this.payment = payment;
    }

    public Supplier(int supplierNumber, String supplierName, String bankAccount, paymentTerms payment, LinkedList<String> contactNames, String telephone, LinkedList<Days> deliveryDays, Delivery deliverySending, LinkedList<Agreement> agreements){
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
                if (p.getNumberProAgreement()==numP)
                    return true;
            }
        }
        return false;

    }
    public void addAgreements(Agreement agreement) {
        this.agreements.add(agreement);
    }
    public void removeAgreements(Agreement agreement) {
        this.agreements.remove(agreement);
    }

    public void setDeliverySending(Delivery deliverySending) {
        this.deliverySending = deliverySending;
    }

    public void addDeliveryDays(LinkedList<Days> deliveryDays) {
        this.deliveryDays.addAll(deliveryDays);
    }
    public void deleteDeliveryDays(Days day) {
        this.deliveryDays.remove(day);
    }

    public void addContactNames(LinkedList<String> contactName) {
        this.contactNames.addAll(contactName);
    }
    public void deleteContactNames(String contactName) {
        this.contactNames.remove(contactName);
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public LinkedList<Days> getDeliveryDays() {
        return deliveryDays;
    }

    public LinkedList<String> getContactNames() {
        return contactNames;
    }
    public String numAgree() {
        String printAll="(";
        int i=1;
        for (Agreement agreement:this.agreements){
            if(i==this.agreements.size()){
                printAll = printAll+agreement.getIDNumber()+")";
            }
            else {
                printAll=printAll+agreement.getIDNumber()+",";
                i++;
            }
        }
        return printAll;
    }

    public String printProducts(){
        String printAll="";
        int i=0;
        for (Agreement agreement:this.agreements){
            if(i==0){
            printAll =printAll+ agreement.printListProducts();
            i++;
            }
            else {
                printAll=printAll+"\n"+agreement.printListProducts();
            }
        }
        return printAll;
    }
    }

