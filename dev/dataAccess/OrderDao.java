package dataAccess;

import Domain.ItemOrder;
import dto.ItemOrderDto;
import dto.OrderDto;
import dto.PeriodAgreementDto;
import dto.QuantityAgreementDto;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Optional;

public interface OrderDao {
    OrderDto saveOrder(OrderDto o)  throws SQLException;
    Optional<OrderDto> findOrderById(int orderNumber)  throws SQLException;
    Optional<ItemOrderDto> addProById(int numAgree, ItemOrderDto it) throws SQLException;
    void updateStatusById(int orderNumber,String status)  throws SQLException;
    }
