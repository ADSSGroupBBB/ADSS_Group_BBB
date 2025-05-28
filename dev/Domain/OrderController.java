package Domain;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

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
    public int addNewOrder(int numAgree,int numSupplier,String address,String date,String contactPhone,String statusOrder){
        SupplierController s=SupplierController.getInstance();
        String name= s.getName(numSupplier);
        Status st;
        if (statusOrder.equals("shipped")){
            st=Status.shipped;
        }
        else if(statusOrder.equals("deleted")){
            st=Status.deleted;
        }
        else {
            st=Status.arrived;
        }
        Order o=new Order(numAgree,name,numSupplier,address,date,contactPhone,st);
        allOrder.put(o.getOrderNumber(),o);
        return o.getOrderNumber();
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
    public boolean addItemOrder(int orderNumber, int numP,int amount){
        AgreementsController ag=AgreementsController.getInstance();
        int numAgreement=allOrder.get(orderNumber).getNumAgreement();
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
    public String addPeriodOrder(){
        AgreementsController ac=AgreementsController.getInstance();
        PeriodAgreement todayPeriodOrder=ac.getPeriodOrderToday();
        LocalDate todayDate = LocalDate.now(); // התאריך של היום
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateAsString = todayDate.format(formatDate);
        for (PeriodAgreement agree: todayPeriodOrder){
            addNewOrder(agree.getIDNumber(),agree.getSupplierNumber(),agree.getAddress(),dateAsString,agree.getContactPhone(),"shipped");
        }
    }
    public Agreement estimatePrice (){

    }

    }


