package dataAccess;

import Domain.ItemOrder;
import dto.*;
import util.Database;
import util.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class JdbcOrderDao implements OrderDao{
    @Override
    public OrderDto saveOrder(OrderDto o) throws SQLException {
        int generatedId=-1;
        try {
            DatabaseManager.getConnection().setAutoCommit(false);
            String sqlS = """
                    INSERT INTO orders(numAgreement, supplierName, supplierNumber,address,date,contactPhone,statusOrder) VALUES (?,?,?,?,?,?,?)
                    """;
            try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sqlS, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, o.numAgreement());
                ps.setString(2, o.supplierName());
                ps.setInt(3, o.supplierNumber());
                ps.setString(4, o.address());
                ps.setString(5, o.date());
                ps.setString(6, o.contactPhone());
                ps.setString(7, o.statusOrder());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                        String sqlItem = """
                                INSERT INTO itemOrders(IDNumber, prodId, numOrder, amountOrder, finalPrice, initialPrice) VALUES (?,?,?,?,?,?)
                                """;
                        try (PreparedStatement psItems = DatabaseManager.getConnection().prepareStatement(sqlItem)) {
                            for (ItemOrderDto it : o.items()) {
                                psItems.setInt(1, generatedId);
                                psItems.setInt(2, it.itemDto().pro().productNumber());
                                psItems.setInt(3, o.numAgreement());
                                psItems.setInt(4, it.amountOrder());
                                psItems.setDouble(5, it.finalPrice());
                                psItems.setDouble(6, it.initialPrice());
                                psItems.executeUpdate();
                            }
                        }
                    }
                }
            }
            DatabaseManager.getConnection().commit();
        } catch (SQLException e) {
            DatabaseManager.getConnection().rollback();
            throw e;
        }
        return new OrderDto(generatedId,o.numAgreement(),o.supplierName(),o.supplierNumber(),o.address(),o.date(),o.contactPhone(),o.items(),o.statusOrder());
    }

    @Override
    public Optional<OrderDto> findOrderById(int orderNumber) throws SQLException {
        try {
            DatabaseManager.getConnection().setAutoCommit(false);

            String sqlOrder = "SELECT * FROM orders WHERE orderNumber = ?";
            try (PreparedStatement psOrder = DatabaseManager.getConnection().prepareStatement(sqlOrder)) {
                psOrder.setInt(1, orderNumber);
                try (ResultSet rsOrder = psOrder.executeQuery()) {
                    if (rsOrder.next()) {
                        int orderNum = rsOrder.getInt("orderNumber");
                        int numAgreement = rsOrder.getInt("numAgreement");
                        String supplierName = rsOrder.getString("supplierName");
                        int supplierNumber = rsOrder.getInt("supplierNumber");
                        String address = rsOrder.getString("address");
                        String date = rsOrder.getString("date");
                        String contactPhone = rsOrder.getString("contactPhone");
                        String statusOrder = rsOrder.getString("statusOrder");

                        LinkedList<ItemOrderDto> items = new LinkedList<>();

                        String sqlItems = """
                        SELECT oi.prodId, oi.amountOrder, oi.finalPrice, oi.initialPrice, oi.numOrder,
                               qa.price, qa.catalogNumber, qa.amountToDiscount, qa.discount,
                               p.productName, p.unitOfMeasure, p.manufacturer
                        FROM orderItems oi
                        LEFT JOIN quantityAgreements qa ON oi.prodId = qa.prodId
                        LEFT JOIN products p ON oi.prodId = p.productNumber
                        WHERE oi.orderNumber = ?
                    """;

                        try (PreparedStatement psItems = DatabaseManager.getConnection().prepareStatement(sqlItems)) {
                            psItems.setInt(1, orderNumber);
                            try (ResultSet rsItems = psItems.executeQuery()) {
                                while (rsItems.next()) {
                                    QuantityAgreementDto quantityDto = new QuantityAgreementDto(
                                            new ProductDto(
                                                    rsItems.getString("productName"),
                                                    rsItems.getInt("prodId"),
                                                    rsItems.getString("unitOfMeasure"),
                                                    rsItems.getString("manufacturer")
                                            ),
                                            rsItems.getDouble("price"),
                                            rsItems.getInt("catalogNumber"),
                                            rsItems.getInt("amountToDiscount"),
                                            rsItems.getInt("discount")
                                    );

                                    ItemOrderDto itemOrderDto = new ItemOrderDto(
                                            quantityDto,
                                            rsItems.getInt("amountOrder"),
                                            rsItems.getDouble("finalPrice"),
                                            rsItems.getDouble("initialPrice"),
                                            rsItems.getInt("numOrder")
                                    );

                                    items.add(itemOrderDto);
                                }
                            }
                        }

                        DatabaseManager.getConnection().commit();

                        OrderDto order = new OrderDto(
                                orderNum,
                                numAgreement,
                                supplierName,
                                supplierNumber,
                                address,
                                date,
                                contactPhone,
                                items,
                                statusOrder
                        );

                        return Optional.of(order);
                    }
                }
            }
            DatabaseManager.getConnection().commit();
        } catch (SQLException e) {
            DatabaseManager.getConnection().rollback();
            throw e;
        }
        return Optional.empty();
    }



    @Override
    public Optional<ItemOrderDto> addProById(int numAgree,ItemOrderDto it ) throws SQLException{
        try {
            DatabaseManager.getConnection().setAutoCommit(false);
            String sqlP = """
                    INSERT INTO itemOrders(IDNumber, prodId, numOrder, amountOrder, finalPrice, initialPrice) VALUES (?,?,?,?,?,?)
                    """;
            try (PreparedStatement psPro = DatabaseManager.getConnection().prepareStatement(sqlP)) {
                psPro.setInt(1, numAgree);
                psPro.setInt(2,it.itemDto().pro().productNumber());
                psPro.setInt(3, it.numOrder());
                psPro.setInt(4, it.amountOrder());
                psPro.setDouble(5, it.finalPrice());
                psPro.setDouble(6, it.initialPrice());
                psPro.executeUpdate();
            }

            DatabaseManager.getConnection().commit();
        } catch (SQLException e) {
            DatabaseManager.getConnection().rollback();
            throw e;
        }
        return Optional.of(it);
    }

    @Override
    public void updateStatusById(int orderNumber, String status) throws SQLException {
        String sql = "UPDATE orders SET statusOrder = ? WHERE orderNumber = ?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderNumber);
           ps.executeUpdate();
            DatabaseManager.getConnection().commit();
        } catch (SQLException e) {
            DatabaseManager.getConnection().rollback();
            throw e;
        }
    }



}
