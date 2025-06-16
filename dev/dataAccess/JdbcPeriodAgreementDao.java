package dataAccess;

import Domain.QuantityAgreement;
import dto.*;
import util.Database;
import util.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class JdbcPeriodAgreementDao implements PeriodAgreementDao {
    public PeriodAgreementDto savePeriod (PeriodAgreementDto agree) throws SQLException {
    int generatedId=0;
        try {
            DatabaseManager.getConnection().setAutoCommit(false);
            String sqlS = """
                    INSERT INTO standardAgreements(supplierNumber, date, type) VALUES (?,?,?)
                    """;
            try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sqlS, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, agree.supplierNumber());
                ps.setString(2, agree.date());
                ps.setString(3, "period");
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                        String sqlPeriod = """
                                INSERT INTO periodAgreements(IDNumber, address, contactPhone) VALUES (?,?,?)
                                """;
                        try (PreparedStatement psPeriod = DatabaseManager.getConnection().prepareStatement(sqlPeriod)) {
                            psPeriod.setInt(1, generatedId);
                            psPeriod.setString(2, agree.address());
                            psPeriod.setString(3, agree.contactPhone());
                            psPeriod.executeUpdate();
                            String sqlP = """
                                    INSERT INTO quantityAgreements(IDNumber, prodId, price, catalogNumber,amountToDiscount,discount) VALUES (?,?,?,?,?,?)
                                    """;
                            try (PreparedStatement psPro = DatabaseManager.getConnection().prepareStatement(sqlP)) {
                                for (PeriodAgreementItemDto pro : agree.productsList()) {
                                    psPro.setInt(1, generatedId);
                                    psPro.setInt(2, pro.productAgreement().pro().productNumber());
                                    psPro.setDouble(3, pro.productAgreement().price());
                                    psPro.setInt(4, pro.productAgreement().catalogNumber());
                                    psPro.setInt(5, pro.productAgreement().amountToDiscount());
                                    psPro.setInt(6, pro.productAgreement().discount());
                                    psPro.executeUpdate();
                                }
                            }
                            String sqlItem = """
                                    INSERT INTO periodAgreementItems(IDNumber, prodId, amountToOrder) VALUES (?,?,?)
                                    """;
                            try (PreparedStatement psItem = DatabaseManager.getConnection().prepareStatement(sqlItem)) {
                                for (PeriodAgreementItemDto pro : agree.productsList()) {
                                    psItem.setInt(1, generatedId);
                                    psItem.setInt(2, pro.productAgreement().pro().productNumber());
                                    psItem.setInt(3, pro.amountToOrder());
                                    psItem.executeUpdate();
                                }
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
        return new PeriodAgreementDto(generatedId,agree.supplierNumber(),agree.productsList(),agree.date(),agree.address(),agree.contactPhone());
    }


    @Override
    public void addProById(int id, PeriodAgreementItemDto pro) throws SQLException {
        try {
            DatabaseManager.getConnection().setAutoCommit(false);
            String sqlP = """
                    INSERT INTO quantityAgreements(IDNumber, prodId, price, catalogNumber,amountToDiscount,discount) VALUES (?,?,?,?,?,?)
                    """;
            try (PreparedStatement psPro = DatabaseManager.getConnection().prepareStatement(sqlP)) {
                psPro.setInt(1, id);
                psPro.setInt(2, pro.productAgreement().pro().productNumber());
                psPro.setDouble(3, pro.productAgreement().price());
                psPro.setInt(4, pro.productAgreement().catalogNumber());
                psPro.setInt(5, pro.productAgreement().amountToDiscount());
                psPro.setInt(6, pro.productAgreement().discount());
                psPro.executeUpdate();
            }
            String sqlItem = """
                            INSERT INTO periodAgreementItems(IDNumber, prodId, amountToOrder) VALUES (?,?,?)
                            """;
            try (PreparedStatement psItem = DatabaseManager.getConnection().prepareStatement(sqlItem)) {
                    psItem.setInt(1, id);
                    psItem.setInt(2, pro.productAgreement().pro().productNumber());
                    psItem.setInt(3, pro.amountToOrder());
                    psItem.executeUpdate();
                }

            DatabaseManager.getConnection().commit();
        } catch (SQLException e) {
            DatabaseManager.getConnection().rollback();
            throw e;
        }
    }

    @Override
    public void removeProById(int numAgree, int productNumber) throws SQLException {
        try {
            DatabaseManager.getConnection().setAutoCommit(false);
            String sqlP = "DELETE FROM quantityAgreements WHERE IDNumber = ? AND prodId = ?";
            try (PreparedStatement psPro = DatabaseManager.getConnection().prepareStatement(sqlP)) {
                psPro.setInt(1, numAgree);
                psPro.setInt(2, productNumber);
                psPro.executeUpdate();
            }
            String sqlItem = "DELETE FROM periodAgreementItems WHERE IDNumber = ? AND prodId = ?";
            try (PreparedStatement psItem = DatabaseManager.getConnection().prepareStatement(sqlItem)) {
                psItem.setInt(1, numAgree);
                psItem.setInt(2, productNumber);
                psItem.executeUpdate();
            }
            DatabaseManager.getConnection().commit();
        } catch (SQLException e) {
            DatabaseManager.getConnection().rollback();
            throw e;
        }
    }

    @Override
    public void removePeriod(int numAgree) throws SQLException {
        try {
            DatabaseManager.getConnection().setAutoCommit(false);
            String sqlS = "DELETE FROM standardAgreements WHERE IDNumber = ?";
            try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sqlS)) {
                ps.setInt(1, numAgree);
                ps.executeUpdate();
            }
            String sqlPeriod = "DELETE FROM periodAgreements WHERE IDNumber = ?";
            try (PreparedStatement psPeriod = DatabaseManager.getConnection().prepareStatement(sqlPeriod)) {
                psPeriod.setInt(1, numAgree);
                psPeriod.executeUpdate();
            }
            String sqlP = "DELETE FROM quantityAgreements WHERE IDNumber = ?";
            try (PreparedStatement psPro = DatabaseManager.getConnection().prepareStatement(sqlP)) {
                psPro.setInt(1, numAgree);
                psPro.executeUpdate();
            }
            String sqlItem = "DELETE FROM periodAgreementItems WHERE IDNumber = ?";
            try (PreparedStatement psItem = DatabaseManager.getConnection().prepareStatement(sqlItem)) {
                psItem.setInt(1, numAgree);
                psItem.executeUpdate();
            }
            DatabaseManager.getConnection().commit();
        } catch (SQLException e) {
            DatabaseManager.getConnection().rollback();
            throw e;
        }
    }

    @Override
    public Optional<PeriodAgreementDto> findPeriodAgreeById(int numA) throws SQLException {
        try {
            DatabaseManager.getConnection().setAutoCommit(false);

            String sql = """
            SELECT sa.IDNumber, sa.supplierNumber, sa.date,
                   pa.address, pa.contactPhone,
                   qa.prodId, qa.price, qa.catalogNumber, qa.amountToDiscount, qa.discount,
                   p.productName, p.unitOfMeasure, p.manufacturer,
                   pai.amountToOrder
            FROM standardAgreements sa
            LEFT JOIN periodAgreements pa ON sa.IDNumber = pa.IDNumber
            LEFT JOIN quantityAgreements qa ON sa.IDNumber = qa.IDNumber
            LEFT JOIN products p ON qa.prodId = p.productNumber
            LEFT JOIN periodAgreementItems pai ON qa.IDNumber = pai.IDNumber AND qa.prodId = pai.prodId
            WHERE sa.IDNumber = ? AND sa.type = 'period'
        """;

            try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
                ps.setInt(1, numA);

                try (ResultSet rs = ps.executeQuery()) {
                    int idNum = -1;
                    int supplierNumber = -1;
                    String date = null;
                    String address = null;
                    String contactPhone = null;

                    LinkedList<PeriodAgreementItemDto> items = new LinkedList<>();

                    while (rs.next()) {
                        if (idNum == -1) {
                            idNum = rs.getInt("IDNumber");
                            supplierNumber = rs.getInt("supplierNumber");
                            date = rs.getString("date");
                            address = rs.getString("address");
                            contactPhone = rs.getString("contactPhone");
                        }

                        int prodId = rs.getInt("prodId");
                        if (!rs.wasNull()) {
                            ProductDto product = new ProductDto(
                                    rs.getString("productName"),
                                    prodId,
                                    rs.getString("unitOfMeasure"),
                                    rs.getString("manufacturer")
                            );

                            QuantityAgreementDto quantityDto = new QuantityAgreementDto(
                                    product,
                                    rs.getDouble("price"),
                                    rs.getInt("catalogNumber"),
                                    rs.getInt("amountToDiscount"),
                                    rs.getInt("discount")
                            );

                            int amountToOrder = rs.getInt("amountToOrder");

                            items.add(new PeriodAgreementItemDto(quantityDto, amountToOrder));
                        }
                    }

                    DatabaseManager.getConnection().commit();

                    if (idNum != -1) {
                        return Optional.of(new PeriodAgreementDto(idNum, supplierNumber, items, date, address, contactPhone));
                    }
                }
            }

        } catch (SQLException e) {
            DatabaseManager.getConnection().rollback();
            throw e;
        }
        return Optional.empty();
    }



    @Override
    public void updateAddressPeriodById(int numAgreement, String address) throws SQLException {
        String sql = "UPDATE periodAgreements SET address = ? WHERE IDNumber = ?";
        try (PreparedStatement ps = (DatabaseManager.getConnection().prepareStatement(sql))) {
            ps.setString(1, address);
            ps.setInt(2, numAgreement);
            ps.executeUpdate();
        }
    }

    @Override
    public void updateContactPhonePeriodById(int numAgreement, String ContactPhone) throws SQLException {
        String sql = "UPDATE periodAgreements SET contactPhone = ? WHERE IDNumber = ?";
        try (PreparedStatement ps = (DatabaseManager.getConnection().prepareStatement(sql))) {
            ps.setString(1, ContactPhone);
            ps.setInt(2, numAgreement);
            ps.executeUpdate();
        }
    }

    @Override
    public void updateAmountById(int numAgreement, int productNumber, int amount) throws SQLException {
        String sql = "UPDATE periodAgreementItems SET amount = ? WHERE IDNumber = ? AND prodId = ?";
        try (PreparedStatement ps = (DatabaseManager.getConnection().prepareStatement(sql))) {
            ps.setInt(1, amount);
            ps.setInt(2, numAgreement);
            ps.setInt(3, productNumber);
            ps.executeUpdate();
        }
    }

    @Override
    public List<PeriodAgreementDto> findAllPeriodAgreeBySupId(int numS) throws SQLException {
        try {
            DatabaseManager.getConnection().setAutoCommit(false);

            String sql = """
            SELECT sa.IDNumber, sa.supplierNumber, sa.date,
                   pa.address, pa.contactPhone,
                   qa.prodId, qa.price, qa.catalogNumber, qa.amountToDiscount, qa.discount,
                   p.productName, p.unitOfMeasure, p.manufacturer,
                   pai.amountToOrder
            FROM standardAgreements sa
            LEFT JOIN periodAgreements pa ON sa.IDNumber = pa.IDNumber
            LEFT JOIN quantityAgreements qa ON sa.IDNumber = qa.IDNumber
            LEFT JOIN products p ON qa.prodId = p.productNumber
            LEFT JOIN periodAgreementItems pai ON qa.IDNumber = pai.IDNumber AND qa.prodId = pai.prodId
            WHERE sa.supplierNumber = ? AND sa.type = 'period'
            ORDER BY sa.IDNumber
        """;

            try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
                ps.setInt(1, numS);

                try (ResultSet rs = ps.executeQuery()) {
                    Map<Integer, PeriodAgreementDto> agreementsMap = new LinkedHashMap<>();

                    while (rs.next()) {
                        int idNum = rs.getInt("IDNumber");

                        PeriodAgreementDto existingAgreement = agreementsMap.get(idNum);
                        if (existingAgreement == null) {
                            int supplierNumber = rs.getInt("supplierNumber");
                            String date = rs.getString("date");
                            String address = rs.getString("address");
                            String contactPhone = rs.getString("contactPhone");

                            existingAgreement = new PeriodAgreementDto(idNum, supplierNumber, new LinkedList<>(), date, address, contactPhone);
                            agreementsMap.put(idNum, existingAgreement);
                        }

                        int prodId = rs.getInt("prodId");
                        if (!rs.wasNull()) {
                            ProductDto product = new ProductDto(
                                    rs.getString("productName"),
                                    prodId,
                                    rs.getString("unitOfMeasure"),
                                    rs.getString("manufacturer")
                            );

                            QuantityAgreementDto quantityDto = new QuantityAgreementDto(
                                    product,
                                    rs.getDouble("price"),
                                    rs.getInt("catalogNumber"),
                                    rs.getInt("amountToDiscount"),
                                    rs.getInt("discount")
                            );

                            int amountToOrder = rs.getInt("amountToOrder");

                            PeriodAgreementItemDto item = new PeriodAgreementItemDto(quantityDto, amountToOrder);

                            existingAgreement.productsList().add(item);
                        }
                    }

                    DatabaseManager.getConnection().commit();

                    List<PeriodAgreementDto> result = new LinkedList<>(agreementsMap.values());
                    return result;
                }
            }

        } catch (SQLException e) {
            DatabaseManager.getConnection().rollback();
            throw e;
        }
    }
    public LinkedList<PeriodAgreementDto> findPeriodAgreementsToOrder(String todayDay, String todayDate) throws SQLException {
        LinkedList<PeriodAgreementDto> result = new LinkedList<>();
        try {
            DatabaseManager.getConnection().setAutoCommit(false);
            String sql = """
                        SELECT sa.IDNumber, sa.supplierNumber, sa.date,
                               pa.address, pa.contactPhone,
                               qa.prodId, qa.price, qa.catalogNumber, qa.amountToDiscount, qa.discount,
                               p.productName, p.unitOfMeasure, p.manufacturer,
                               pai.amountToOrder
                        FROM suppliers s
                        JOIN Supplier_Days sd ON s.supplierNumber = sd.supplierNumber
                        JOIN standardAgreements sa ON s.supplierNumber = sa.supplierNumber
                        JOIN periodAgreements pa ON sa.IDNumber = pa.IDNumber
                        LEFT JOIN quantityAgreements qa ON sa.IDNumber = qa.IDNumber
                        LEFT JOIN products p ON qa.prodId = p.productNumber
                        LEFT JOIN periodAgreementItems pai ON qa.IDNumber = pai.IDNumber AND qa.prodId = pai.prodId
                        WHERE sd.day = ?
                          AND sa.type = 'period'
                          AND sa.IDNumber NOT IN (
                              SELECT o.numAgreement
                              FROM orders o
                              WHERE o.date = ?
                          )
                    """;

            try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
                ps.setString(1, todayDay);
                ps.setString(2, todayDate);

                try (ResultSet rs = ps.executeQuery()) {
                    Map<Integer, PeriodAgreementDto> agreementsMap = new LinkedHashMap<>();

                    while (rs.next()) {
                        int idNum = rs.getInt("IDNumber");

                        PeriodAgreementDto existingAgreement = agreementsMap.get(idNum);
                        if (existingAgreement == null) {
                            int supplierNumber = rs.getInt("supplierNumber");
                            String date = rs.getString("date");
                            String address = rs.getString("address");
                            String contactPhone = rs.getString("contactPhone");

                            existingAgreement = new PeriodAgreementDto(
                                    idNum,
                                    supplierNumber,
                                    new LinkedList<>(),
                                    date,
                                    address,
                                    contactPhone
                            );
                            agreementsMap.put(idNum, existingAgreement);
                        }

                        int prodId = rs.getInt("prodId");
                        if (!rs.wasNull()) {
                            ProductDto product = new ProductDto(
                                    rs.getString("productName"),
                                    prodId,
                                    rs.getString("unitOfMeasure"),
                                    rs.getString("manufacturer")
                            );

                            QuantityAgreementDto quantityDto = new QuantityAgreementDto(
                                    product,
                                    rs.getDouble("price"),
                                    rs.getInt("catalogNumber"),
                                    rs.getInt("amountToDiscount"),
                                    rs.getInt("discount")
                            );

                            int amountToOrder = rs.getInt("amountToOrder");
                            PeriodAgreementItemDto item = new PeriodAgreementItemDto(quantityDto, amountToOrder);
                            existingAgreement.productsList().add(item);
                        }
                    }

                    result.addAll(agreementsMap.values());
                }
            }

            return result;
        } catch (SQLException e) {
            DatabaseManager.getConnection().rollback();
            throw e;
        }
    }

    }
