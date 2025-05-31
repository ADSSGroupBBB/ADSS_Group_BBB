package dataAccess;

import Domain.Supplier;
import dto.SupplierDto;
import util.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcSupplierDao implements SupplierDao{
    @Override
    public SupplierDto saveSup(SupplierDto sup) throws SQLException {
        try {
            Database.getConnection().setAutoCommit(false);
        String sql= """
                INSERT INTO suppliers(supplierNumber, supplierName, bankAccount, payment, contactNames, telephone, deliverySending) VALUES (?,?,?,?,?,?,?,?,?)
                """;
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, sup.supplierNumber());
            ps.setString(2, sup.supplierName());
            ps.setString(3, sup.bankAccount());
            ps.setString(4, sup.payment());
            String contacts = String.join(",", sup.contactNames().stream().map(String::valueOf).toList());
            ps.setString(5, contacts);
            ps.setString(6, sup.telephone());
            ps.setString(7, sup.deliverySending());
            ps.executeUpdate();
            int supplierId = sup.supplierNumber();
            String sqlDay = "INSERT INTO Supplier_Days(supplierNumber, day) VALUES (?,?)";
            try (PreparedStatement psDay = Database.getConnection().prepareStatement(sqlDay)) {
                for (String day : sup.deliveryDays()) {
                    psDay.setInt(1, supplierId);
                    psDay.setString(2, day);
                    psDay.executeUpdate();
                }
            }
            String sqlAgreeId = "INSERT INTO Supplier_Agreements(supplierNumber, agreementId) VALUES (?,?)";
            try (PreparedStatement psAgree = Database.getConnection().prepareStatement(sqlAgreeId)) {
                for (int id : sup.agreementsId()) {
                    psAgree.setInt(1, supplierId);
                    psAgree.setInt(2, id);
                    psAgree.executeUpdate();
                }
            }
        }
            Database.getConnection().commit();
        } catch (SQLException e) {
            Database.getConnection().rollback();
            throw e;
        }
        return sup;
    }

    @Override
    public Optional<SupplierDto> findSupById(int id)  throws SQLException{
        String sql= "SELECT * FROM suppliers WHERE supplierNumber = ?";
        try (PreparedStatement psSupplier=Database.getConnection().prepareStatement(sql)){
            psSupplier.setInt(1,id);
            try (ResultSet sup=psSupplier.executeQuery()){
                if(sup.next()){
                    int supplierNumber=sup.getInt("supplierNumber");
                    String supplierName=sup.getString("supplierName");
                    String bankAccount=sup.getString("bankAccount");
                    String payment=sup.getString("payment");
                    String contactName=sup.getString("contactNames");
                    LinkedList<String> contactNames= new LinkedList<>(Arrays.asList(contactName.split(",")));
                    String telephone=sup.getString("telephone");
                    String deliverySending=sup.getString("deliverySending");
                    LinkedList<String> days=new LinkedList<>();
                    String sql_day= "SELECT day FROM Supplier_Days WHERE supplierNumber = ?";
                    try(PreparedStatement ps_days=Database.getConnection().prepareStatement(sql_day)) {
                        ps_days.setInt(1,id);
                        try(ResultSet day_sup=ps_days.executeQuery()) {
                            while (day_sup.next()){
                                days.add(day_sup.getString("day"));
                            }
                        }
                    }
                    LinkedList<Integer> agreementsId=new LinkedList<>();
                    String sql_agreeId= "SELECT agreementId FROM Supplier_Agreements WHERE supplierNumber = ?";
                    try(PreparedStatement ps_agreeId=Database.getConnection().prepareStatement(sql_agreeId)) {
                        ps_agreeId.setInt(1,id);
                        try(ResultSet agreeId_sup=ps_agreeId.executeQuery()) {
                            while (agreeId_sup.next()){
                                agreementsId.add(agreeId_sup.getInt("agreementId"));
                            }
                        }
                    }
                    return Optional.of(new SupplierDto(supplierNumber,supplierName,bankAccount,payment,contactNames,telephone,days,deliverySending,agreementsId)) ;
                }
            }

        }
        return Optional.empty();
    }

    public String getContactNameById(int supplierNumber) throws SQLException{
        String sql= "SELECT contactNames FROM suppliers WHERE supplierNumber = ?";
        try (PreparedStatement psSupplier=Database.getConnection().prepareStatement(sql)){
            psSupplier.setInt(1,supplierNumber);
            try (ResultSet contactNames=psSupplier.executeQuery()){
                if(contactNames.next()){
                     return contactNames.getString("contactNames");
                    }
                }
            }
        return null;
    }

    public boolean findDayById(int supplierNumber, String day) throws SQLException {
        String sqlDay = "SELECT 1 FROM supplier_delivery_days WHERE supplierNumber = ? AND day = ?";
        try (PreparedStatement ps = (Database.getConnection().prepareStatement(sqlDay))) {
            ps.setInt(1, supplierNumber);
            ps.setString(2, day);

            try (ResultSet resD = ps.executeQuery()) {
                return resD.next();
            }
        }
    }
    public void updateNameSupById(int numSupplier,String nameSupplier) throws SQLException {
        String sql = "UPDATE suppliers SET supplierName = ? WHERE supplierNumber = ?";

        try (PreparedStatement ps = (Database.getConnection().prepareStatement(sql))) {
            ps.setString(1, nameSupplier);
            ps.setInt(2, numSupplier);
            ps.executeUpdate();
        }
    }
    public void updateBankAccountSupById(int numSupplier,String bankAccount) throws SQLException{
    String sql = "UPDATE suppliers SET bankAccount = ? WHERE supplierNumber = ?";
        try (PreparedStatement ps = (Database.getConnection().prepareStatement(sql))) {
        ps.setString(1, bankAccount);
        ps.setInt(2, numSupplier);
        ps.executeUpdate();
    }
}
    public void updatePaymentSupById(int numSupplier,String payment) throws SQLException{
        String sql = "UPDATE suppliers SET payment = ? WHERE supplierNumber = ?";
        try (PreparedStatement ps = (Database.getConnection().prepareStatement(sql))) {
            ps.setString(1, payment);
            ps.setInt(2, numSupplier);
            ps.executeUpdate();
        }
    }
    public void updateTelephoneSupById(int numSupplier,String telephone) throws SQLException{
        String sql = "UPDATE suppliers SET telephone = ? WHERE supplierNumber = ?";
        try (PreparedStatement ps = (Database.getConnection().prepareStatement(sql))) {
            ps.setString(1, telephone);
            ps.setInt(2, numSupplier);
            ps.executeUpdate();
        }
    }
    public void updateDeliverySendingSupById(int numSupplier,String deliverySending) throws SQLException{
        String sql = "UPDATE suppliers SET deliverySending = ? WHERE supplierNumber = ?";
        try (PreparedStatement ps = (Database.getConnection().prepareStatement(sql))) {
            ps.setString(1, deliverySending);
            ps.setInt(2, numSupplier);
            ps.executeUpdate();
        }
    }
    public void saveContactNamesSupById(int numSupplier, LinkedList<String> contactNames) throws SQLException{
            String sql = "UPDATE suppliers SET contactNames =  ? WHERE supplierNumber = ?";
            String contacts = String.join(",", contactNames.stream().map(String::valueOf).toList());
            try (PreparedStatement ps = (Database.getConnection().prepareStatement(sql))) {
            ps.setString(1, contacts);
                ps.setInt(2, numSupplier);
                ps.executeUpdate();
            }
    }

    public void saveDeliveryDaysSupById(int numSupplier, LinkedList<String> days) throws SQLException{
        String sqlDay = "INSERT INTO Supplier_Days(supplierNumber, day) VALUES (?,?)";
        try (PreparedStatement psDay = Database.getConnection().prepareStatement(sqlDay)) {
            for (String day : days) {
                psDay.setInt(1, numSupplier);
                psDay.setString(2, day);
                psDay.executeUpdate();
            }
        }
    }
    public void removeDaysById(int numSupplier,String day) throws SQLException{
        String sqlDay = "DELETE FROM supplier_delivery_days WHERE supplierNumber = ? AND day = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sqlDay)) {
            ps.setInt(1, numSupplier);
            ps.setString(2, day);
            ps.executeUpdate();
        }
    }
    public void saveAgreeById(int supplierNumber,int agree_id) throws SQLException{
        String sqlAgreeId = "INSERT INTO Supplier_Agreements(supplierNumber, agreementId) VALUES (?,?)";
        try (PreparedStatement psAgree = Database.getConnection().prepareStatement(sqlAgreeId)) {
            psAgree.setInt(1, supplierNumber);
            psAgree.setInt(2, agree_id);
            psAgree.executeUpdate();
            }
    }
    public void removeAgreeById(int supplierNumber,int agree_id) throws SQLException{
        String sql = "DELETE FROM Supplier_Agreements WHERE supplierNumber = ? AND agreementId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, supplierNumber);
            ps.setInt(2, agree_id);
            ps.executeUpdate();
        }
    }
    public boolean isConstantById(int supplierNumber ) throws SQLException{
        String sql = "SELECT 1 FROM supplier_delivery_days WHERE supplierNumber = ? LIMIT 1";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, supplierNumber);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }
    public void removeSupById(int id) throws SQLException {
        try {
            Database.getConnection().setAutoCommit(false);

            String deleteAgreementsSql = "DELETE FROM Supplier_Agreements WHERE supplierNumber = ?";
            try (PreparedStatement ps = Database.getConnection().prepareStatement(deleteAgreementsSql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            String deleteDaysSql = "DELETE FROM Supplier_Days WHERE supplierNumber = ?";
            try (PreparedStatement ps = Database.getConnection().prepareStatement(deleteDaysSql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            String sql = "DELETE FROM suppliers WHERE supplierNumber = ?";
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            Database.getConnection().commit();
        } catch (SQLException e) {
            Database.getConnection().rollback();
            throw e;
        }
    }
}
