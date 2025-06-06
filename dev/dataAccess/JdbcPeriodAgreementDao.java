package dataAccess;

import Domain.QuantityAgreement;
import dto.AgreementDto;
import dto.PeriodAgreementDto;
import dto.PeriodAgreementItemDto;
import dto.QuantityAgreementDto;
import util.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class JdbcPeriodAgreementDao implements PeriodAgreementDao {
    public PeriodAgreementDto savePeriod (PeriodAgreementDto agree) throws SQLException {
        try {
            Database.getConnection().setAutoCommit(false);
            String sqlS = """
                    INSERT INTO standardAgreements(IDNumber, supplierNumber, date, type) VALUES (?,?,?,?)
                    """;
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sqlS)) {
                ps.setInt(1, agree.IDNumber());
                ps.setInt(2, agree.supplierNumber());
                ps.setString(3, agree.date());
                ps.setString(4, "period");
                ps.executeUpdate();
                String sqlPeriod = """
                        INSERT INTO periodAgreements(IDNumber, address, contactPhone) VALUES (?,?,?)
                        """;
                try (PreparedStatement psPeriod = Database.getConnection().prepareStatement(sqlPeriod)) {
                    psPeriod.setInt(1, agree.IDNumber());
                    psPeriod.setString(2, agree.address());
                    psPeriod.setString(3, agree.contactPhone());
                    psPeriod.executeUpdate();
                    String sqlP = """
                            INSERT INTO quantityAgreements(IDNumber, prodId, price, catalogNumber,amountToDiscount,discount) VALUES (?,?,?,?,?,?)
                            """;
                    try (PreparedStatement psPro = Database.getConnection().prepareStatement(sqlP)) {
                        for (PeriodAgreementItemDto pro : agree.productsList()) {
                            psPro.setInt(1, agree.IDNumber());
                            psPro.setInt(2, pro.productAgreement().prodId());
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
                    try (PreparedStatement psItem = Database.getConnection().prepareStatement(sqlItem)) {
                        for (PeriodAgreementItemDto pro : agree.productsList()) {
                            psItem.setInt(1, agree.IDNumber());
                            psItem.setInt(2, pro.productAgreement().prodId());
                            psItem.setInt(3, pro.amountToOrder());
                            psItem.executeUpdate();
                        }
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


    @Override
    public void addProById(int id, PeriodAgreementItemDto pro) throws SQLException {
        try {
            Database.getConnection().setAutoCommit(false);
            String sqlP = """
                    INSERT INTO quantityAgreements(IDNumber, prodId, price, catalogNumber,amountToDiscount,discount) VALUES (?,?,?,?,?,?)
                    """;
            try (PreparedStatement psPro = Database.getConnection().prepareStatement(sqlP)) {
                psPro.setInt(1, id);
                psPro.setInt(2, pro.productAgreement().prodId());
                psPro.setDouble(3, pro.productAgreement().price());
                psPro.setInt(4, pro.productAgreement().catalogNumber());
                psPro.setInt(5, pro.productAgreement().amountToDiscount());
                psPro.setInt(6, pro.productAgreement().discount());
                psPro.executeUpdate();
            }
            String sqlItem = """
                            INSERT INTO periodAgreementItems(IDNumber, prodId, amountToOrder) VALUES (?,?,?)
                            """;
            try (PreparedStatement psItem = Database.getConnection().prepareStatement(sqlItem)) {
                    psItem.setInt(1, id);
                    psItem.setInt(2, pro.productAgreement().prodId());
                    psItem.setInt(3, pro.amountToOrder());
                    psItem.executeUpdate();
                }

            Database.getConnection().commit();
        } catch (SQLException e) {
            Database.getConnection().rollback();
            throw e;
        }
    }

    @Override
    public void removeProById(int numAgree, int productNumber) throws SQLException {
        try {
            Database.getConnection().setAutoCommit(false);
            String sqlP = "DELETE FROM quantityAgreements WHERE IDNumber = ? AND prodId = ?";
            try (PreparedStatement psPro = Database.getConnection().prepareStatement(sqlP)) {
                psPro.setInt(1, numAgree);
                psPro.setInt(2, productNumber);
                psPro.executeUpdate();
            }
            String sqlItem = "DELETE FROM periodAgreementItems WHERE IDNumber = ? AND prodId = ?";
            try (PreparedStatement psItem = Database.getConnection().prepareStatement(sqlItem)) {
                psItem.setInt(1, numAgree);
                psItem.setInt(2, productNumber);
                psItem.executeUpdate();
            }
            Database.getConnection().commit();
        } catch (SQLException e) {
            Database.getConnection().rollback();
            throw e;
        }
    }

    @Override
    public void removePeriod(int numAgree) throws SQLException {
        try {
            Database.getConnection().setAutoCommit(false);
            String sqlS = "DELETE FROM standardAgreements WHERE IDNumber = ?";
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sqlS)) {
                ps.setInt(1, numAgree);
                ps.executeUpdate();
            }
            String sqlPeriod = "DELETE FROM periodAgreements WHERE IDNumber = ?";
            try (PreparedStatement psPeriod = Database.getConnection().prepareStatement(sqlPeriod)) {
                psPeriod.setInt(1, numAgree);
                psPeriod.executeUpdate();
            }
            String sqlP = "DELETE FROM quantityAgreements WHERE IDNumber = ?";
            try (PreparedStatement psPro = Database.getConnection().prepareStatement(sqlP)) {
                psPro.setInt(1, numAgree);
                psPro.executeUpdate();
            }
            String sqlItem = "DELETE FROM periodAgreementItems WHERE IDNumber = ?";
            try (PreparedStatement psItem = Database.getConnection().prepareStatement(sqlItem)) {
                psItem.setInt(1, numAgree);
                psItem.executeUpdate();
            }
            Database.getConnection().commit();
        } catch (SQLException e) {
            Database.getConnection().rollback();
            throw e;
        }
    }

    @Override
    public Optional<PeriodAgreementDto> findPeriodAgreeById(int numA) throws SQLException {
        try {
            Database.getConnection().setAutoCommit(false);
            int idNum;
            int supplierNumber;
            int proId;
            double price;
            int catalogNumber;
            int amountToDiscount;
            int discount;
            String date;
            String type;
            String address;
            String contactPhone;
            String sql = "SELECT * FROM standardAgreements WHERE IDNumber = ? AND type = ?";
            try (PreparedStatement psAgree = Database.getConnection().prepareStatement(sql)) {
                psAgree.setInt(1, numA);
                psAgree.setString(2, "period");
                try (ResultSet agree = psAgree.executeQuery()) {
                    if (agree.next()) {
                        idNum = agree.getInt("IDNumber");
                        supplierNumber = agree.getInt("supplierNumber");
                        date = agree.getString("date");
                        type = agree.getString("type");
                        LinkedList<PeriodAgreementItemDto> items = new LinkedList<>();
                        String sqlPeriod = " SELECT * FROM periodAgreements WHERE IDNumber = ?";
                        try (PreparedStatement psPeriod = Database.getConnection().prepareStatement(sqlPeriod)) {
                            psPeriod.setInt(1, numA);
                            try (ResultSet per = psAgree.executeQuery()) {
                                if (per.next()) {
                                    per.getInt("IDNumber");
                                    address = per.getString("address");
                                    contactPhone = per.getString("contactPhone");
                                    String sqlP = "SELECT * FROM quantityAgreements WHERE IDNumber= ?";
                                    try (PreparedStatement psPro = Database.getConnection().prepareStatement(sqlP)) {
                                        psPro.setInt(1, numA);
                                        try (ResultSet pro = psPro.executeQuery()) {
                                            while (pro.next()) {
                                                proId = pro.getInt("prodId");
                                                price = pro.getDouble("price");
                                                catalogNumber = pro.getInt("catalogNumber");
                                                amountToDiscount = pro.getInt("amountToDiscount");
                                                discount = pro.getInt("discount");
                                                String sqlI = "SELECT * FROM periodAgreementItems WHERE IDNumber= ? AND prodId = ?";
                                                try (PreparedStatement psItem = Database.getConnection().prepareStatement(sqlI)) {
                                                    psItem.setInt(1, numA);
                                                    psItem.setInt(2, proId);
                                                    try (ResultSet item = psItem.executeQuery()) {
                                                        if (item.next()) {
                                                            items.add(new PeriodAgreementItemDto(new QuantityAgreementDto(proId, price, catalogNumber, amountToDiscount, discount), item.getInt("amountToOrder")));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Database.getConnection().commit();
                                    return Optional.of(new PeriodAgreementDto(idNum, supplierNumber, items, date, address, contactPhone));
                                }
                            }
                        }

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

    @Override
    public void updateAddressPeriodById(int numAgreement, String address) throws SQLException {
        String sql = "UPDATE periodAgreements SET address = ? WHERE IDNumber = ?";
        try (PreparedStatement ps = (Database.getConnection().prepareStatement(sql))) {
            ps.setString(1, address);
            ps.setInt(2, numAgreement);
            ps.executeUpdate();
        }
    }

    @Override
    public void updateContactPhonePeriodById(int numAgreement, String ContactPhone) throws SQLException {
        String sql = "UPDATE periodAgreements SET contactPhone = ? WHERE IDNumber = ?";
        try (PreparedStatement ps = (Database.getConnection().prepareStatement(sql))) {
            ps.setString(1, ContactPhone);
            ps.setInt(2, numAgreement);
            ps.executeUpdate();
        }
    }

    @Override
    public void updateAmountById(int numAgreement, int productNumber, int amount) throws SQLException {
        String sql = "UPDATE periodAgreementItems SET amount = ? WHERE IDNumber = ? AND prodId = ?";
        try (PreparedStatement ps = (Database.getConnection().prepareStatement(sql))) {
            ps.setInt(1, amount);
            ps.setInt(2, numAgreement);
            ps.setInt(3, productNumber);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Optional<PeriodAgreementDto>> findAllPeriodAgreeBySupId(int numS) throws SQLException {
        try {
            Database.getConnection().setAutoCommit(false);
            int idNum;
            int supplierNumber;
            int proId;
            double price;
            int catalogNumber;
            int amountToDiscount;
            int discount;
            String date;
            String type;
            String address="";
            String contactPhone="";
            List<Optional<PeriodAgreementDto>> agreements=new LinkedList<>();
            String sql = "SELECT * FROM standardAgreements WHERE supplierNumber = ? AND type = ?";
            try (PreparedStatement psAgree = Database.getConnection().prepareStatement(sql)) {
                psAgree.setInt(1, numS);
                psAgree.setString(2, "period");
                try (ResultSet agree = psAgree.executeQuery()) {
                    while (agree.next()) {
                        idNum = agree.getInt("IDNumber");
                        supplierNumber = agree.getInt("supplierNumber");
                        date = agree.getString("date");
                        type = agree.getString("type");
                       LinkedList<PeriodAgreementItemDto> items = new LinkedList<>();
                        String sqlPeriod = " SELECT * FROM periodAgreements WHERE IDNumber = ?";
                        try (PreparedStatement psPeriod = Database.getConnection().prepareStatement(sqlPeriod)) {
                            psPeriod.setInt(1, idNum);
                            try (ResultSet per = psPeriod.executeQuery()) {
                                if (per.next()) {
                                    per.getInt("IDNumber");
                                    address = per.getString("address");
                                    contactPhone = per.getString("contactPhone");
                                    String sqlP = "SELECT * FROM quantityAgreements WHERE IDNumber= ?";
                                    try (PreparedStatement psPro = Database.getConnection().prepareStatement(sqlP)) {
                                        psPro.setInt(1, idNum);
                                        try (ResultSet pro = psPro.executeQuery()) {
                                            while (pro.next()) {
                                                proId = pro.getInt("prodId");
                                                price = pro.getDouble("price");
                                                catalogNumber = pro.getInt("catalogNumber");
                                                amountToDiscount = pro.getInt("amountToDiscount");
                                                discount = pro.getInt("discount");
                                                String sqlI = "SELECT * FROM periodAgreementItems WHERE IDNumber= ? AND prodId = ?";
                                                try (PreparedStatement psItem = Database.getConnection().prepareStatement(sqlI)) {
                                                    psItem.setInt(1, idNum);
                                                    psItem.setInt(2, proId);
                                                    try (ResultSet item = psItem.executeQuery()) {
                                                        if (item.next()) {
                                                            items.add(new PeriodAgreementItemDto(new QuantityAgreementDto(proId, price, catalogNumber, amountToDiscount, discount), item.getInt("amountToOrder")));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        agreements.add(Optional.of(new PeriodAgreementDto(idNum, supplierNumber,items, date, address, contactPhone)));
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
