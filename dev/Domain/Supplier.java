package Domain;

import dto.SupplierDto;

import java.util.ArrayList;
import java.util.LinkedList;
//a class to represent the supplier
public class Supplier {
    private int supplierNumber; //the supplier ID unique for each supplier
    private String supplierName;    //the name of a supplier
    private  String bankAccount;    //bank account number for transaction
    private paymentTerms payment;   //method of payment
    private LinkedList<String> contactNames;    //a list contact of the supplier
    private String telephone;   //the contact information of the supplier
    private LinkedList<Days> deliveryDays;  //the days delivery according to the agreement
    private Delivery deliverySending;  // repeating the order
    private LinkedList<Agreement> agreements;   //list of the agreements with the supplier
    private String address; //the address for automaticOrder
    private String contactPhone; //the contactPhone for automaticOrder


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    //supplier constructor
    //gets:supplierNumber,supplierName,bankAccount,payment,contactNames,telephone,deliveryDays,deliverySending,agreements
    public Supplier(int supplierNumber, String supplierName, String bankAccount, paymentTerms payment, LinkedList<String> contactNames, String telephone, LinkedList<Days> deliveryDays, Delivery deliverySending, LinkedList<Agreement> agreements,String address,String contactPhone){
        this.supplierNumber=supplierNumber;
        this.supplierName=supplierName;
        this.bankAccount=bankAccount;
        this.payment=payment;
        this.contactNames=contactNames;
        this.telephone=telephone;
        this.deliveryDays=deliveryDays;
        this.deliverySending=deliverySending;
        this.agreements=agreements;
        this.address=address;
        this.contactPhone=contactPhone;
    }
    //supplierName getter
    public String getSupplierName() {
        return supplierName;
    }

    //set payment method
    //gets paymentTerms
    public void setPayment(paymentTerms payment) {
        this.payment = payment;
    }


    //set supplierName field
    //gets String
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    //set supplierNumber field
    //gets type int
    public void setSupplierNumber(int supplierNumber) {
        this.supplierNumber = supplierNumber;
    }
    //set telephone field
    //gets type String
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    //a method to check iif a certain product exists
    //gets type int (the product number)
    //and returns a boolean value-either the product exists or not
    public boolean existProduct(int numP){
        for (Agreement agr: agreements){
            for (QuantityAgreement p:agr.getProductsList()){
                if (p.getNumberProAgreement()==numP)
                    return true;
            }
        }
        return false;
    }
    //a method to add an agreement to the supplier
    //gets type Agreement (object agreement)
    public void addAgreements(Agreement agreement) {
        this.agreements.add(agreement);
    }
    //a method to remove agreement from the supplier
    //gets type Agreement (object agreement)
    public void removeAgreements(Agreement agreement) {
        this.agreements.remove(agreement);
    }

    //set deliverySending field
    //gets type Delivery
    public void setDeliverySending(Delivery deliverySending) {
        this.deliverySending = deliverySending;
    }

    //add to the deliveryDays list
    //gets a linked list with objects of type days in it
    public void addDeliveryDays(LinkedList<Days> deliveryDays) {
        this.deliveryDays.addAll(deliveryDays);
    }
    //a method to delete from deliveryDays
    //gets a type Days
    public void deleteDeliveryDays(Days day) {
        this.deliveryDays.remove(day);
    }

    //a method to add to contactNames
    //gets a linked list with objects of type String in it
    public void addContactNames(LinkedList<String> contactName) {
        this.contactNames.addAll(contactName);
    }
    //a method to delete from contactNames
    //gets a type String
    public void deleteContactNames(String contactName) {
        this.contactNames.remove(contactName);
    }
    //set bankAccount field
    //gets type String
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
    //deliveryDays getter
    public LinkedList<Days> getDeliveryDays() {
        return deliveryDays;
    }

    //contactNames getter
    public LinkedList<String> getContactNames() {
        return contactNames;
    }

    //a method to print all the agreement with this supplier
    //returns a String
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

    public ArrayList<Integer> listNumAgree() {
        int i=1;
        ArrayList<Integer> numbersA=new ArrayList<Integer>();
        for (Agreement agreement:this.agreements){
                numbersA.add(agreement.getIDNumber());
        }
        return numbersA;
    }


    public void setContactNames(LinkedList<String> contactNames) {
        this.contactNames = contactNames;
    }

    public void setDeliveryDays(LinkedList<Days> deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    public void setAgreements(LinkedList<Agreement> agreements) {
        this.agreements = agreements;
    }


    public int getSupplierNumber() {
        return supplierNumber;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public paymentTerms getPayment() {
        return payment;
    }

    public String getTelephone() {
        return telephone;
    }

    public Delivery getDeliverySending() {
        return deliverySending;
    }

    public LinkedList<Agreement> getAgreements() {
        return agreements;
    }

    //a method to print all the products from the supplier
    //returns a String
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

