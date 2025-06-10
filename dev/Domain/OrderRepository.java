package Domain;

import dto.ItemOrderDto;
import dto.OrderDto;
import dto.ProductDto;
import dto.QuantityAgreementDto;

import java.sql.SQLException;
import java.util.Optional;

public interface OrderRepository {
    Optional<OrderDto> getOrder(int orderNumber) throws SQLException;
    OrderDto addOrder(int numAgree,int numSupplier,String address,String date,String contactPhone,String statusOrder) throws SQLException;
    Optional<ItemOrderDto> addProductOrder(int numOrder, int numP, int amount) throws SQLException;
    void updateStatus(int orderNumber,String status)throws SQLException;
}
