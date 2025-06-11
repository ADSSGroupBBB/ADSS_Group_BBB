package dataAccess;

import dto.AgreementDto;
import dto.ProductDto;
import dto.QuantityAgreementDto;
import dto.SupplierDto;
import util.*;
import util.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcStandardAgreementDao implements StandardAgreementDAO{
    public AgreementDto saveStandard (AgreementDto agree) throws SQLException {
        try {
            Database.getConnection().setAutoCommit(false);
            String sqlS= """
                INSERT INTO standardAgreements(IDNumber, supplierNumber, date, type) VALUES (?,?,?,?)
                """;
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sqlS)) {
            ps.setInt(1,agree.IDNumber());
            ps.setInt(2,agree.supplierNumber());
            ps.setString(3, agree.date());
            ps.setString(4,"standard");
            ps.executeUpdate();
                String sqlP= """
                INSERT INTO quantityAgreements(IDNumber, prodId, price, catalogNumber,amountToDiscount,discount) VALUES (?,?,?,?,?,?)
                """;
                try (PreparedStatement psPro = Database.getConnection().prepareStatement(sqlP)) {
                    for (QuantityAgreementDto pro : agree.productsList()) {
                        psPro.setInt(1, agree.IDNumber());
                        psPro.setInt(2, pro.pro().productNumber());
                        psPro.setDouble(3, pro.price());
                        psPro.setInt(4, pro.catalogNumber());
                        psPro.setInt(5, pro.amountToDiscount());
                        psPro.setInt(6, pro.discount());
                        psPro.executeUpdate();
                    }
                }
        }
            Database.getConnection().commit();
        } catch (SQLException e) {
            Database.getConnection().rollback();
            throw e;
        }
       return agree;
    }
    public void addProById(int id, QuantityAgreementDto pro)throws SQLException {
        try {
            Database.getConnection().setAutoCommit(false);
            String sqlP = """
                    INSERT INTO quantityAgreements(IDNumber, prodId, price, catalogNumber,amountToDiscount,discount) VALUES (?,?,?,?,?,?)
                    """;
            try (PreparedStatement psPro = Database.getConnection().prepareStatement(sqlP)) {
                psPro.setInt(1, id);
                psPro.setInt(2, pro.pro().productNumber());
                psPro.setDouble(3, pro.price());
                psPro.setInt(4, pro.catalogNumber());
                psPro.setInt(5, pro.amountToDiscount());
                psPro.setInt(6, pro.discount());
                psPro.executeUpdate();
            }
            Database.getConnection().commit();
        } catch (SQLException e) {
            Database.getConnection().rollback();
            throw e;
        }
    }
    public void removeProById(int numAgree, int productNumber)throws SQLException{
        try {
            Database.getConnection().setAutoCommit(false);
            String sqlP = "DELETE FROM quantityAgreements WHERE IDNumber = ? AND prodId = ?";
            try (PreparedStatement psPro = Database.getConnection().prepareStatement(sqlP)) {
                psPro.setInt(1, numAgree);
                psPro.setInt(2, productNumber);
                psPro.executeUpdate();
            }
            Database.getConnection().commit();
        } catch (SQLException e) {
            Database.getConnection().rollback();
            throw e;
        }
    }
    public void updateDateById(int numAgreement,String date) throws SQLException{
        String sql = "UPDATE standardAgreements SET date = ? WHERE IDNumber = ?";
        try (PreparedStatement ps = (Database.getConnection().prepareStatement(sql))) {
            ps.setString(1, date);
            ps.setInt(2, numAgreement);
            ps.executeUpdate();
        }
    }
    public void removeStandard(int numAgree)throws SQLException{
        try {
            Database.getConnection().setAutoCommit(false);
            String sqlS = "DELETE FROM standardAgreements WHERE IDNumber = ?";
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sqlS)) {
                ps.setInt(1, numAgree);
                ps.executeUpdate();
            }
            String sqlP = "DELETE FROM quantityAgreements WHERE IDNumber = ?";
            try (PreparedStatement psPro = Database.getConnection().prepareStatement(sqlP)) {
                psPro.setInt(1, numAgree);
                psPro.executeUpdate();
            }
            Database.getConnection().commit();
        } catch (SQLException e) {
            Database.getConnection().rollback();
            throw e;
        }
    }
    public Optional<AgreementDto> findStandardAgreeById(int numA) throws SQLException {
        try {
            Database.getConnection().setAutoCommit(false);

            String sql = """
            SELECT sa.IDNumber, sa.supplierNumber, sa.date,
                   qa.prodId, qa.price, qa.catalogNumber, qa.amountToDiscount, qa.discount,
                   p.productNumber, p.productName, p.unitOfMeasure, p.manufacturer
            FROM standardAgreements sa
            LEFT JOIN quantityAgreements qa ON sa.IDNumber = qa.IDNumber
            LEFT JOIN products p ON qa.prodId = p.productNumber
            WHERE sa.IDNumber = ? AND sa.type = 'standard'
        """;

            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
                ps.setInt(1, numA);

                try (ResultSet rs = ps.executeQuery()) {
                    LinkedList<QuantityAgreementDto> products = new LinkedList<>();
                    int idNum = -1;
                    int supplierNumber = -1;
                    String date = null;

                    while (rs.next()) {
                        if (idNum == -1) {
                            idNum = rs.getInt("IDNumber");
                            supplierNumber = rs.getInt("supplierNumber");
                            date = rs.getString("date");
                        }

                        if (rs.getObject("prodId") == null) {
                            continue;
                        }

                        ProductDto product = new ProductDto(
                                rs.getString("productName"),
                                rs.getInt("productNumber"),
                                rs.getString("unitOfMeasure"),
                                rs.getString("manufacturer")
                        );

                        QuantityAgreementDto qProduct = new QuantityAgreementDto(
                                product,
                                rs.getDouble("price"),
                                rs.getInt("catalogNumber"),
                                rs.getInt("amountToDiscount"),
                                rs.getInt("discount")
                        );

                        products.add(qProduct);
                    }

                    Database.getConnection().commit();

                    if (idNum != -1) {
                        return Optional.of(new AgreementDto(idNum, supplierNumber, products, date));
                    }
                }
            }

            Database.getConnection().commit();
        } catch (SQLException e) {
            Database.getConnection().rollback();
            throw e;
        }

        return Optional.empty();
    }
    public Optional<AgreementDto> getAgreement(int numAgree) throws SQLException{
        try {
            Database.getConnection().setAutoCommit(false);

            String sql = """
            SELECT sa.IDNumber, sa.supplierNumber, sa.date,
                   qa.prodId, qa.price, qa.catalogNumber, qa.amountToDiscount, qa.discount,
                   p.productNumber, p.productName, p.unitOfMeasure, p.manufacturer
            FROM standardAgreements sa
            LEFT JOIN quantityAgreements qa ON sa.IDNumber = qa.IDNumber
            LEFT JOIN products p ON qa.prodId = p.productNumber
            WHERE sa.IDNumber = ?
        """;

            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
                ps.setInt(1, numAgree);

                try (ResultSet rs = ps.executeQuery()) {
                    LinkedList<QuantityAgreementDto> products = new LinkedList<>();
                    int idNum = -1;
                    int supplierNumber = -1;
                    String date = null;

                    while (rs.next()) {
                        if (idNum == -1) {
                            idNum = rs.getInt("IDNumber");
                            supplierNumber = rs.getInt("supplierNumber");
                            date = rs.getString("date");
                        }

                        if (rs.getObject("prodId") == null) {
                            continue;
                        }

                        ProductDto product = new ProductDto(
                                rs.getString("productName"),
                                rs.getInt("productNumber"),
                                rs.getString("unitOfMeasure"),
                                rs.getString("manufacturer")
                        );

                        QuantityAgreementDto qProduct = new QuantityAgreementDto(
                                product,
                                rs.getDouble("price"),
                                rs.getInt("catalogNumber"),
                                rs.getInt("amountToDiscount"),
                                rs.getInt("discount")
                        );

                        products.add(qProduct);
                    }

                    Database.getConnection().commit();

                    if (idNum != -1) {
                        return Optional.of(new AgreementDto(idNum, supplierNumber, products, date));
                    }
                }
            }
            Database.getConnection().commit();
        } catch (SQLException e) {
            Database.getConnection().rollback();
            throw e;
        }

        return Optional.empty();
    }


    public void updateCatalogById(int numAgree,int productNumber,int catalogNumber) throws SQLException{
        String sql = "UPDATE quantityAgreements SET catalogNumber = ? WHERE IDNumber = ? AND prodId = ?";
        try (PreparedStatement ps = (Database.getConnection().prepareStatement(sql))) {
            ps.setInt(1, catalogNumber);
            ps.setInt(2, numAgree);
            ps.setInt(3, productNumber);
            ps.executeUpdate();
        }
    }
    public void updatePriceById(int numAgree,int productNumber,double price) throws SQLException{
        String sql = "UPDATE quantityAgreements SET price = ? WHERE IDNumber = ? AND prodId = ?";
        try (PreparedStatement ps = (Database.getConnection().prepareStatement(sql))) {
            ps.setDouble(1, price);
            ps.setInt(2, numAgree);
            ps.setInt(3, productNumber);
            ps.executeUpdate();
        }
    }
    public void updateAmountToDiscountById(int numAgree,int productNumber,int amountToDiscount) throws SQLException{
        String sql = "UPDATE quantityAgreements SET amountToDiscount = ? WHERE IDNumber = ? AND prodId = ?";
        try (PreparedStatement ps = (Database.getConnection().prepareStatement(sql))) {
            ps.setDouble(1, amountToDiscount);
            ps.setInt(2, numAgree);
            ps.setInt(3, productNumber);
            ps.executeUpdate();
        }
    }
    public void updateDiscountById(int numAgree,int productNumber,int discount) throws SQLException {
        String sql = "UPDATE quantityAgreements SET discount = ? WHERE IDNumber = ? AND prodId = ?";
        try (PreparedStatement ps = (Database.getConnection().prepareStatement(sql))) {
            ps.setDouble(1, discount);
            ps.setInt(2, numAgree);
            ps.setInt(3, productNumber);
            ps.executeUpdate();
        }
    }
    public List<AgreementDto> findAllStandardAgreeBySupId(int supplierId) throws SQLException {
        String sql = """
        SELECT sa.IDNumber, sa.supplierNumber, sa.date,
               qa.prodId, qa.price, qa.catalogNumber, qa.amountToDiscount, qa.discount,
               p.productNumber, p.productName, p.unitOfMeasure, p.manufacturer
        FROM standardAgreements sa
        LEFT JOIN quantityAgreements qa ON sa.IDNumber = qa.IDNumber
        LEFT JOIN products p ON qa.prodId = p.productNumber
        WHERE sa.supplierNumber = ? AND sa.type = 'standard'
        ORDER BY sa.IDNumber
    """;

        try {
            Database.getConnection().setAutoCommit(false);

            Map<Integer, AgreementDto> agreementsMap = new LinkedHashMap<>();

            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
                ps.setInt(1, supplierId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int idNum = rs.getInt("IDNumber");
                        AgreementDto agreement = agreementsMap.get(idNum);

                        if (agreement == null) {
                            agreement = new AgreementDto(
                                    idNum,
                                    rs.getInt("supplierNumber"),
                                    new LinkedList<>(),
                                    rs.getString("date")
                            );
                            agreementsMap.put(idNum, agreement);
                        }

                        if (rs.getObject("prodId") != null) {
                            ProductDto product = new ProductDto(
                                    rs.getString("productName"),
                                    rs.getInt("productNumber"),
                                    rs.getString("unitOfMeasure"),
                                    rs.getString("manufacturer")
                            );

                            QuantityAgreementDto quantityProduct = new QuantityAgreementDto(
                                    product,
                                    rs.getDouble("price"),
                                    rs.getInt("catalogNumber"),
                                    rs.getInt("amountToDiscount"),
                                    rs.getInt("discount")
                            );

                            agreement.productsList().add(quantityProduct);
                        }
                    }
                }
            }

            Database.getConnection().commit();

            return new ArrayList<>(agreementsMap.values());


        } catch (SQLException e) {
            Database.getConnection().rollback();
            throw e;
        }
    }
    public List<AgreementDto> findAllStandardAgreeByProId(int numP) throws SQLException {
            String sql = """
                        SELECT sa.IDNumber, sa.supplierNumber, sa.date,
                               qa.prodId, qa.price, qa.catalogNumber, qa.amountToDiscount, qa.discount,
                               p.productNumber, p.productName, p.unitOfMeasure, p.manufacturer
                        FROM standardAgreements sa
                        JOIN quantityAgreements qa ON sa.IDNumber = qa.IDNumber
                        JOIN products p ON qa.prodId = p.productNumber
                        WHERE sa.type = 'standard'
                          AND sa.IDNumber IN (
                              SELECT IDNumber
                              FROM quantityAgreements
                              WHERE prodId = ?
                          )
                        ORDER BY sa.IDNumber
                    """;

            try {
                Database.getConnection().setAutoCommit(false);

                Map<Integer, AgreementDto> agreementsMap = new LinkedHashMap<>();

                try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
                    ps.setInt(1, numP);

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int idNum = rs.getInt("IDNumber");

                            AgreementDto agreement = agreementsMap.get(idNum);
                            if (agreement == null) {
                                agreement = new AgreementDto(
                                        idNum,
                                        rs.getInt("supplierNumber"),
                                        new LinkedList<>(),
                                        rs.getString("date")
                                );
                                agreementsMap.put(idNum, agreement);
                            }

                            ProductDto product = new ProductDto(
                                    rs.getString("productName"),
                                    rs.getInt("productNumber"),
                                    rs.getString("unitOfMeasure"),
                                    rs.getString("manufacturer")
                            );

                            QuantityAgreementDto quantityProduct = new QuantityAgreementDto(
                                    product,
                                    rs.getDouble("price"),
                                    rs.getInt("catalogNumber"),
                                    rs.getInt("amountToDiscount"),
                                    rs.getInt("discount")
                            );

                            agreement.productsList().add(quantityProduct);
                        }
                    }
                }

                Database.getConnection().commit();
                return new ArrayList<>(agreementsMap.values());

            } catch (SQLException e) {
                Database.getConnection().rollback();
                throw e;
            }
        }
}







