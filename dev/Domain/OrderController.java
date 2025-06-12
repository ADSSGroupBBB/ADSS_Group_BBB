package Domain;

import dto.AgreementDto;
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
        StockController s= StockController.getInstance();
        AgreementsController ag=AgreementsController.getInstance();
        int numAgreement=this.orderRepo.getOrder(orderNumber).get().numAgreement();
        QuantityAgreement qa=ag.productFromAgreeByIndex(numAgreement,numP);
        Order o=OrderMapper.toObject(this.orderRepo.getOrder(orderNumber).get());
        boolean success= o.addProductOrder(qa,amount);
        if(success){
            this.orderRepo.addProductOrder(orderNumber,numP,amount);
            s.updateStatusOrderPro(numP,amount);
        }
        return success;
    }
    public boolean addItemOrderAutomat(int orderNumber, int numP,int amount) throws SQLException{
        StockController s= StockController.getInstance();
        AgreementsController ag=AgreementsController.getInstance();
        int numAgreement=this.orderRepo.getOrder(orderNumber).get().numAgreement();
        QuantityAgreement qa=ag.searchPro(numAgreement,numP);
        Order o=OrderMapper.toObject(this.orderRepo.getOrder(orderNumber).get());
        boolean success= o.addProductOrder(qa,amount);
        if(success){
            this.orderRepo.addProductOrderAutomat(orderNumber,numP,amount);
            s.updateStatusOrderPro(numP,amount);
        }
        return success;
    }
    //set an order's status as status
    //parameter:int orderNumber,String status
    public void cancelOrder (int orderNumber) throws SQLException{
        StockController s= StockController.getInstance();
        this.orderRepo.updateStatus(orderNumber,"deleted");
        s.cancelStock(orderNumber);
    }

    public void arriveOrder (int orderNumber) throws SQLException {
        this.orderRepo.updateStatus(orderNumber, "arrived");
        StockController s = StockController.getInstance();
        s.updateStock(orderNumber);
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
        StockController s= StockController.getInstance();
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
                    addItemOrderAutomat(numOrder,productNum,amount);
                }
        }
        String orderString= count+" orders created";
        return orderString;
    }
    public String addMissOrder() throws SQLException{
        int count=0;
        AgreementDto a;
        int numOrder;
        String address;
        String contactPhone;
        LocalDate todayDate = LocalDate.now();
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateAsString = todayDate.format(formatDate);
        StockController s= StockController.getInstance();
        Map<Integer, Integer> agreeForOrder=new HashMap<>();
        AgreementsController ac=AgreementsController.getInstance();
        SupplierController sc=SupplierController.getInstance();
        Map<Integer, ProductStock> allMiss=s.getMissProducts();
        for(ProductStock pro:allMiss.values()){
            a= ac.agreementMostEffectivePrice(pro.getNumProduct(),pro.getMinimumCount());
             if(a==null){
                 continue;
             }
             if (!agreeForOrder.containsKey(a.IDNumber())) {
                 address = sc.getAddress(a.supplierNumber());
                 contactPhone = sc.getContactPhone(a.supplierNumber());
                 numOrder = addNewOrder(a.IDNumber(), a.supplierNumber(), address, dateAsString, contactPhone);
                 agreeForOrder.put(a.IDNumber(),numOrder);
             }
             boolean b=addItemOrderAutomat(agreeForOrder.get(a.IDNumber()),pro.getNumProduct(),pro.getMinimumCount());
             if(b==false) {
                 if (this.orderRepo.getOrder(agreeForOrder.get(a.IDNumber())).isPresent()) {
                     if (this.orderRepo.getOrder(agreeForOrder.get(a.IDNumber())).get().items().size() == 0) {
                         cancelOrder(agreeForOrder.get(a.IDNumber()));
                     }
                 }
             }
        }

        String orderString= agreeForOrder.size()+" orders created";
        return orderString;
    }


}


