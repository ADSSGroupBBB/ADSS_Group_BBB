package Domain;

import dto.OrderDto;
import dto.PeriodAgreementDto;
import dto.PeriodAgreementItemDto;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDate;

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
    public Order orderBYnum(int orderID) throws SQLException{
        return OrderMapper.toObject(this.orderRepo.getOrder(orderID).get());
    }
    //add order to supplier
    //parameters:int orderNumber,int numSupplier,String address,String date,String contactPhone,String statusOrder
    public int addNewOrder(int numAgree,int numSupplier,String address,String date,String contactPhone) throws SQLException {
        return (this.orderRepo.addOrder(numAgree,numSupplier,address,date,contactPhone,"shipped")).orderNumber();

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

    //prints all the products from a certain agreement
    //parameters:int numAgreement
    //returns String
    public String printProByAgree(int numAgreement) throws SQLException{
        AgreementsController ag= AgreementsController.getInstance();
        return ag.printProduct(numAgreement);
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
        int numAgreement=this.orderRepo.getOrder(orderNumber).get().numAgreement();
        QuantityAgreement qa=ag.productFromAgreeByIndex(numAgreement,numP);
        Order o=OrderMapper.toObject(this.orderRepo.getOrder(orderNumber).get());
        boolean success= o.addProductOrder(qa,amount);
        if(success){
            this.orderRepo.addProductOrder(orderNumber,numP,amount);
        }
        return success;
    }
    //set an order's status as status
    //parameter:int orderNumber,String status
    public void updateStatus(int orderNumber,String status) throws SQLException{
        this.orderRepo.updateStatus(orderNumber,status);
    }
    //prints order
    //parameter:int orderNumber
    //returns String
    public String StringOrder(int orderNumber) throws SQLException {
        return (OrderMapper.toObject(this.orderRepo.getOrder(orderNumber).get())).print_Order();
    }

    public String addPeriodOrder() throws SQLException{
        AgreementsController ac=AgreementsController.getInstance();
        int count=0;
        Stock s= Stock.getInstance();
        LocalDate todayDate = LocalDate.now();
        List<PeriodAgreementDto> todayPeriodOrder=ac.getAllPeriodToOrder(); //לבדוק שמעל המלאי ולפני עריכה תופעל הפונקציה
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateAsString = todayDate.format(formatDate);
        for (PeriodAgreementDto agree:todayPeriodOrder){
                int numOrder=addNewOrder(agree.IDNumber(), agree.supplierNumber(), agree.address(), dateAsString, agree.contactPhone());
                count++;
                for (PeriodAgreementItemDto it:agree.productsList()){
                    int amount=it.amountToOrder();
                    int productNum=it.productAgreement().pro().productNumber();
                    if(!(s.getCurrentAmount(productNum)+it.amountToOrder()>=s.getMinimumAmount(productNum))){
                        amount=amount+s.getMinimumAmount(it.productAgreement().pro().productNumber());
                    }
                    addItemOrder(numOrder,productNum,amount);
                    s.getProductStock().get(productNum).setBeOrdered(true);
                }
        }
        String orderString= count+" orders created";
        return orderString;
    }


    }


