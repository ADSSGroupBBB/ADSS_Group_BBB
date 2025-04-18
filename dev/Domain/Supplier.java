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

    public Supplier(int supplierNumber,String supplierName,String bankAccount,LinkedList<String> contactNames,String telephone,LinkedList<Days> deliveryDays){
        this.supplierNumber=supplierNumber;
        this.supplierName=supplierName;
        this.bankAccount=bankAccount;
        this.contactNames=contactNames;
        this.telephone=telephone;
        this.deliveryDays=deliveryDays;
    }

}
