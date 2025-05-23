package Domain;

import java.util.HashMap;
import java.util.Map;
//a class for the manager (the controller) of Order
public class OrderController {
    private static OrderController instance; // the single instance
    private Map<Integer, Order> allOrder;    // map of all orders

    // private constructor to prevent external instantiation
    private OrderController() {
        allOrder = new HashMap<>();
    }

    // public method to get the single instance
    public static OrderController getInstance() {
        if (instance == null) {
            instance = new OrderController();
        }
        return instance;
    }
    //add order to supplier
    //parameters:int orderNumber,int numSupplier,String address,String date,String contactPhone,String statusOrder
    public void addNewOrder(int orderNumber,int numSupplier,String address,String date,String contactPhone,String statusOrder){
        SupplierController s=SupplierController.getInstance();
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
        AgreementsController ag=AgreementsController.getInstance();
        return ag.printProduct(numAgreement);
    }
    //finds the amount of product in an agreement
    //parameters:int numAgreement
    //returns int
    public int numProByAgree(int numAgreement){
        AgreementsController ag=AgreementsController.getInstance();
        return ag.numProduct(numAgreement);
    }
    //add item to order
    //parameters:int orderNumber, int numAgreement, int numP,int amount
    //returns bool
    public boolean addItemOrder(int orderNumber, int numAgreement, int numP,int amount){
        AgreementsController ag=AgreementsController.getInstance();
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


