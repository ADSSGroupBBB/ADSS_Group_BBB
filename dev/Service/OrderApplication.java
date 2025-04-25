package Service;

import Domain.AgreementsController;
import Domain.OrderController;

public class OrderApplication {
    OrderController oc=new OrderController();
    public void addOrder(int orderNumber,int numSupplier,String address,String date,String contactPhone,String statusOrder){
         oc.addNewOrder(orderNumber,numSupplier,address,date,contactPhone,statusOrder);
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
    public boolean addItem(int orderNumber, int numAgreement, int numP,int amount){
        return oc.addItemOrder(orderNumber, numAgreement, numP, amount);
    }
    public void deleteOrder(int orderNumber){
        oc.statusDelete(orderNumber);
    }
    public String printOrder(int orderNumber){
        return oc.StringOrder(orderNumber);
    }
}
