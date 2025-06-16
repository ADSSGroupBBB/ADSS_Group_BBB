package Domain;

import dataAccess.JdbcOrderDao;
import dataAccess.OrderDao;
import dto.*;
import util.Database;
import util.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class OrderRepositoryImpl implements OrderRepository{
    private static OrderRepositoryImpl instance;
    private OrderDao orDao;
    private Map<Integer, Order> orderList;

    // Private constructor
    private OrderRepositoryImpl() {
        this.orDao = new JdbcOrderDao();
        this.orderList = new HashMap<>();
    }

    // Public accessor for singleton instance
    public static OrderRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new OrderRepositoryImpl();
        }
        return instance;
    }

    public Optional<OrderDto> getOrder(int orderNumber) throws SQLException {
        if(this.orderList.containsKey(orderNumber)){
            return Optional.of(OrderMapper.transfer(this.orderList.get(orderNumber)));
        }
        Optional<OrderDto> optionalOrder = this.orDao.findOrderById(orderNumber);

        if (optionalOrder.isPresent()) {
            OrderDto ord = optionalOrder.get();
            orderList.put(orderNumber, OrderMapper.toObject(ord));
        }
        return optionalOrder;
    }

    @Override
    public OrderDto addOrder(int numAgree, int numSupplier, String address, String date, String contactPhone, String statusOrder) throws SQLException {
        try {
            DatabaseManager.getConnection().setAutoCommit(false);
            SupplierRepository s = SupplierRepositoryImpl.getInstance();
            String name = s.getNameById(numSupplier);
            Status st = OrderMapper.stringToEnumStatus(statusOrder);
            OrderDto ordto= this.orDao.saveOrder(new OrderDto(-1,numAgree,name,numSupplier,address,date,contactPhone,new LinkedList<ItemOrderDto>(),statusOrder)); //-1 -fictive value
            Order o = new Order(ordto.orderNumber(),numAgree, name, numSupplier, address, date, contactPhone, st);
            this.orderList.put(ordto.orderNumber(), o);
            DatabaseManager.getConnection().commit();
            return ordto;
        }
     catch (SQLException e) {
        if ( DatabaseManager.getConnection() != null) {
            DatabaseManager.getConnection().rollback();
        }
        throw e;
    }
        finally {
            DatabaseManager.getConnection().setAutoCommit(true);
        }
}

    @Override
    public Optional<ItemOrderDto> addProductOrder(int numOrder,int numP, int amount) throws SQLException {
        try {
            DatabaseManager.getConnection().setAutoCommit(false);
            Optional<OrderDto> orderOpt = getOrder(numOrder);
            if (orderOpt.isEmpty()) {
                DatabaseManager.getConnection().rollback();
                return Optional.empty();
            }
            int numAgree = orderOpt.get().numAgreement();
            StandardAgreementRepository agree = StandardAgreementRepositoryImpl.getInstance();
            QuantityAgreementDto pro = agree.proFromAgreeByIndex(numAgree, numP);
            QuantityAgreement q = QuantityAgreementMapper.toObject(pro, numAgree);
            if(this.orderList.containsKey(numOrder)) {
                boolean bool = this.orderList.get(numOrder).addProductOrder(q, amount);
                if (!bool) {
                    DatabaseManager.getConnection().rollback();
                    return Optional.empty();
                }
            }
            ItemOrder it = new ItemOrder(q, amount, numOrder);
            Optional<ItemOrderDto> result = this.orDao.addProById(numAgree, ItemOrderMapper.transfer(it));
            DatabaseManager.getConnection().commit();
            return result;
        }
        catch (SQLException e) {
            if ( DatabaseManager.getConnection() != null) {
                DatabaseManager.getConnection().rollback();
            }
            throw e;
        }
        finally {
            DatabaseManager.getConnection().setAutoCommit(true);
        }
    }

    public Optional<ItemOrderDto> addProductOrderAutomat(int numOrder,int numP, int amount) throws SQLException {
        try {
            DatabaseManager.getConnection().setAutoCommit(false);
            Optional<OrderDto> orderOpt = getOrder(numOrder);
            if (orderOpt.isEmpty()) {
                DatabaseManager.getConnection().rollback();
                return Optional.empty();
            }
            int numAgree = orderOpt.get().numAgreement();
            StandardAgreementRepository agree = StandardAgreementRepositoryImpl.getInstance();
            QuantityAgreementDto pro = agree.searchPro(numAgree, numP);
            QuantityAgreement q = QuantityAgreementMapper.toObject(pro, numAgree);
            if(this.orderList.containsKey(numOrder)) {
                boolean bool = this.orderList.get(numOrder).addProductOrder(q, amount);
                if (!bool) {
                    DatabaseManager.getConnection().rollback();
                    return Optional.empty();
                }
            }
            ItemOrder it = new ItemOrder(q, amount, numOrder);
            Optional<ItemOrderDto> result = this.orDao.addProById(numAgree, ItemOrderMapper.transfer(it));
            DatabaseManager.getConnection().commit();
            return result;
        }
        catch (SQLException e) {
            if ( DatabaseManager.getConnection() != null) {
                DatabaseManager.getConnection().rollback();
            }
            throw e;
        }
        finally {
            DatabaseManager.getConnection().setAutoCommit(true);
        }
    }

    @Override
    public void updateStatus(int orderNumber, String status) throws SQLException{
        if(this.orderList.containsKey(orderNumber)) {
            this.orderList.get(orderNumber).setStatusOrder(OrderMapper.stringToEnumStatus(status));
        }
        this.orDao.updateStatusById( orderNumber, status);
    }


}
