package Domain;

import java.util.HashMap;
import java.util.Map;

public class OrderController {
    private static Map<Integer,Order> allOrder;
    private static int num=0;
    public OrderController(){
        if (num==0){
            allOrder=new HashMap<>();
            num++;
        }
    }
    public void addNewOrder(int orderNumber,int numSupplier,String address,String date,String contactPhone,String statusOrder){
        SupplierController s=new SupplierController();
        String name= s.getName(numSupplier);
        Status st;
        if (statusOrder.equals("canceled")){
            st=Status.canceled;
        }
        else if(statusOrder.equals("deleted")){
            st=Status.deleted;
        }
        else {
            st=Status.arrived;
        }
        Order o=new Order(orderNumber,name,numSupplier,address,date,contactPhone,st);
        allOrder.put(orderNumber,o);
    }
    public boolean existOrder(int orderNumber){
        if (allOrder.containsKey(orderNumber)){
            return true;
        }
        return false;
    }
    public String printProByAgree(int numAgreement){
        AgreementsController ag=new AgreementsController();
        return ag.printProduct(numAgreement);
    }
    public int numProByAgree(int numAgreement){
        AgreementsController ag=new AgreementsController();
        return ag.numProduct(numAgreement);
    }
    public boolean addItemOrder(int orderNumber, int numAgreement, int numP,int amount){
        AgreementsController ag=new AgreementsController();
        QuantityAgreement qa=ag.productFromAgreeByIndex(numAgreement,numP);
        return allOrder.get(orderNumber).addProductOrder(qa,amount);
    }
    public void statusDelete(int orderNumber){
        Status s=Status.deleted;
        allOrder.get(orderNumber).setStatusOrder(s);
    }
    public String StringOrder(int orderNumber) {
        return allOrder.get(orderNumber).print_Order();
    }

    }
