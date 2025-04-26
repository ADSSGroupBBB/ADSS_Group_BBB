package Domain;

import java.util.HashMap;
import java.util.Map;
//a class for the manager (the controller) of Order
public class OrderController {
    private static Map<Integer,Order> allOrder; //Map of all the orders
    private static int num=0;   // tells if there is at least 1 order
    //default constructor
    public OrderController(){
        if (num==0){
            allOrder=new HashMap<>();
            num++;
        }
    }
    //add order to supplier
    //parameters:int orderNumber,int numSupplier,String address,String date,String contactPhone,String statusOrder
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
    //checks if a certain order exists
    //parameters: int orderNumber
    //returns bool
    public boolean existOrder(int orderNumber){
        if (allOrder.containsKey(orderNumber)){
            return true;
        }
        return false;
    }
    //prints all the products from a certain agreement
    //parameters:int numAgreement
    //returns String
    public String printProByAgree(int numAgreement){
        AgreementsController ag=new AgreementsController();
        return ag.printProduct(numAgreement);
    }
    //finds the amount of product in an agreement
    //parameters:int numAgreement
    //returns int
    public int numProByAgree(int numAgreement){
        AgreementsController ag=new AgreementsController();
        return ag.numProduct(numAgreement);
    }
    //add item to order
    //parameters:int orderNumber, int numAgreement, int numP,int amount
    //returns bool
    public boolean addItemOrder(int orderNumber, int numAgreement, int numP,int amount){
        AgreementsController ag=new AgreementsController();
        QuantityAgreement qa=ag.productFromAgreeByIndex(numAgreement,numP);
        return allOrder.get(orderNumber).addProductOrder(qa,amount);
    }
    //set an order's status as deleted
    //parameter:int orderNumber
    public void statusDelete(int orderNumber){
        Status s=Status.deleted;
        allOrder.get(orderNumber).setStatusOrder(s);
    }
    //prints order
    //parameter:int orderNumber
    //returns String
    public String StringOrder(int orderNumber) {
        return allOrder.get(orderNumber).print_Order();
    }

    }
