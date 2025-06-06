package Domain;

import dto.OrderDto;
import dto.PeriodAgreementDto;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.util.Optional;

//a class for the manager (the controller) of Order
public class OrderController {
    private static OrderController instance; // the single instance
    //private Map<Integer, Order> allOrder;    // map of all orders
    private OrderRepository orderRepo;

    // private constructor to prevent external instantiation
    private OrderController() {
        orderRepo = OrderRepositoryImpl.getInstance();
    }

    // public method to get the single instance
    public static OrderController getInstance() {
        if (instance == null) {
            instance = new OrderController();
        }
        return instance;
    }
    public Order orderBYnum(int orderID){
        return allOrder.get(orderID);
    }
    //add order to supplier
    //parameters:int orderNumber,int numSupplier,String address,String date,String contactPhone,String statusOrder
    public int addNewOrder(int numAgree,int numSupplier,String address,String date,String contactPhone,String statusOrder) throws SQLException {
        return (this.orderRepo.addOrder(numAgree,numSupplier,address,date,contactPhone,statusOrder)).orderNumber();

    }
    //checks if a certain order exists
    //parameters: int orderNumber
    //returns bool
    public boolean existOrder(int orderNumber) throws SQLException{
        Optional<OrderDto> s=this.orderRepo.getOrder(orderNumber);
        if(s.isPresent()) {
                return true;
        }
        return false;
    }
    public Status StatusByID (int orderId) throws SQLException{
        return this.orderRepo.getOrder(orderId).statusOrder();
    }
    //prints all the products from a certain agreement
    //parameters:int numAgreement
    //returns String
    public String printProByAgree(int numAgreement) throws SQLException{
        return this.orderRepo.printProductsByAgree(numAgreement);
    }
    //finds the amount of product in an agreement
    //parameters:int numAgreement
    //returns int
    public int numProByAgree(int numAgreement) throws SQLException{
        AgreementsController ag=AgreementsController.getInstance();
        return ag.numProduct(numAgreement);
    }
    //add item to order
    //parameters:int orderNumber, int numAgreement, int numP,int amount
    //returns bool
    public boolean addItemOrder(int orderNumber, int numP,int amount) throws SQLException{
        AgreementsController ag=AgreementsController.getInstance();
        int numAgreement=allOrder.get(orderNumber).getNumAgreement();
        QuantityAgreement qa=ag.productFromAgreeByIndex(numAgreement,numP);
        return allOrder.get(orderNumber).addProductOrder(qa,amount);
    }
    //set an order's status as deleted
    //parameter:int orderNumber,String status
    public void statusDelete(int orderNumber,String status) throws SQLException{
        this.orderRepo.setStatus(orderNumber,status);
    }
    //prints order
    //parameter:int orderNumber
    //returns String
    public String StringOrder(int orderNumber) throws SQLException {
        (new Order(this.orderRepo.getOrder(orderNumber))).print_Order();
    }
    public String addPeriodOrder(){
        AgreementsController ac=AgreementsController.getInstance();
        PeriodAgreement todayPeriodOrder=ac.getPeriodOrderToday(todayDate);
        LocalDate todayDate = LocalDate.now();
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateAsString = todayDate.format(formatDate);
        for (PeriodAgreement agree: todayPeriodOrder){
            addNewOrder(agree.getIDNumber(),agree.getSupplierNumber(),agree.getAddress(),dateAsString,agree.getContactPhone(),"shipped");
        }
    }


    }


