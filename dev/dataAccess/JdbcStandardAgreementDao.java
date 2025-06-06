package dataAccess;

import dto.AgreementDto;
import dto.QuantityAgreementDto;
import dto.SupplierDto;
import util.*;
import util.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
                        psPro.setInt(2, pro.prodId());
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
                psPro.setInt(2, pro.prodId());
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
        int idNum;
        int supplierNumber;
        String date;
        String type;
        String sql = "SELECT * FROM standardAgreements WHERE IDNumber = ? AND type = ?";
        try (PreparedStatement psAgree = Database.getConnection().prepareStatement(sql)) {
            psAgree.setInt(1, numA);
            psAgree.setString(2, "standard");
            try (ResultSet agree = psAgree.executeQuery()) {
                if (agree.next()) {
                    idNum = agree.getInt("IDNumber");
                    supplierNumber = agree.getInt("supplierNumber");
                    date = agree.getString("date");
                    type = agree.getString("type");
                    LinkedList<QuantityAgreementDto> products = new LinkedList<>();
                    String sqlP = "SELECT * FROM quantityAgreements WHERE IDNumber= ?";
                    try (PreparedStatement psPro = Database.getConnection().prepareStatement(sqlP)) {
                        psPro.setInt(1, numA);
                        try (ResultSet pro = psPro.executeQuery()) {
                            while (pro.next()) {
                                products.add(new QuantityAgreementDto(pro.getInt("prodId"), pro.getDouble("price"), pro.getInt("catalogNumber"), pro.getInt("amountToDiscount"), pro.getInt("discount")));
                            }
                        }
                    }
                    Database.getConnection().commit();
                    return Optional.of(new AgreementDto(idNum, supplierNumber,products, date));
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
    public List<Optional<AgreementDto>> findAllStandardAgreeBySupId(int numS) throws SQLException{
        try {
            Database.getConnection().setAutoCommit(false);
            int idNum;
            int supplierNumber;
            String date;
            String type;
            List<Optional<AgreementDto>> agreements=new LinkedList<>();
            String sql = "SELECT * FROM standardAgreements WHERE supplierNumber = ? AND type = ?";
            try (PreparedStatement psAgree = Database.getConnection().prepareStatement(sql)) {
                psAgree.setInt(1, numS);
                psAgree.setString(2, "standard");
                try (ResultSet agree = psAgree.executeQuery()) {
                    while (agree.next()) {
                        idNum = agree.getInt("IDNumber");
                        supplierNumber = agree.getInt("supplierNumber");
                        date = agree.getString("date");
                        type = agree.getString("type");
                        LinkedList<QuantityAgreementDto> products = new LinkedList<>();
                        String sqlP = "SELECT * FROM quantityAgreements WHERE IDNumber= ?";
                        try (PreparedStatement psPro = Database.getConnection().prepareStatement(sqlP)) {
                            psPro.setInt(1, idNum);
                            try (ResultSet pro = psPro.executeQuery()) {
                                while (pro.next()) {
                                    products.add(new QuantityAgreementDto(pro.getInt("prodId"), pro.getDouble("price"), pro.getInt("catalogNumber"), pro.getInt("amountToDiscount"), pro.getInt("discount")));
                                }
                            }
                        }
                        agreements.add(Optional.of(new AgreementDto(idNum, supplierNumber,products, date)));
                    }
                }
            }
            Database.getConnection().commit();
            return agreements;
        } catch (SQLException e) {
            Database.getConnection().rollback();
            throw e;
        }
    }
}






