package Service;

import Domain.*;
import java.util.*;



public class OrderApplication {
    OrderController oc=OrderController.getInstance();
    automaticOrder autoO = new automaticOrder();

    public boolean addStandardAutoOrder(int numAgree , int numSupplier, String address, String date, String contactPhone, String statusOrder){
        return autoO.standardAutoOrder(numAgree,numSupplier,address,date,contactPhone,statusOrder);
    }

    public int addOrder(int numAgree , int numSupplier, String address, String date, String contactPhone, String statusOrder){
         return oc.addNewOrder(numAgree,numSupplier,address,date,contactPhone,statusOrder);
    }
    public boolean orderExist(int orderNumber){
        return oc.existOrder(orderNumber);
    }
    public String printByAgree(int numAgreement){
        return oc.printProByAgree(numAgreement);
    }
    public int numProAgreement(int numAgreement){
        return oc.numProByAgree(numAgreement);
    }
    public boolean addItem(int orderNumber, int numP,int amount){
        return oc.addItemOrder(orderNumber, numP, amount);
    }
    public void deleteOrder(int orderNumber){
        oc.statusDelete(orderNumber);
    }
    public String printOrder(int orderNumber){
        return oc.StringOrder(orderNumber);
    }

}
