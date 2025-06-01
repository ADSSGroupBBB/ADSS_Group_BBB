package dataAccess;

import dto.AgreementDto;
import util.*;
import util.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcStandardAgreementDao {
    public AgreementDto saveStandardAgreement (AgreementDto agree) throws SQLException {
        try {
            Database.getConnection().setAutoCommit(false);
            String sql= """
                INSERT INTO standardAgreements(orderId, supplierNumber, date, type) VALUES (?,?,?,?)
                """;
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1,agree.IDNumber());
            ps.setInt(2,agree.supplierNumber());
            ps.setString(3, agree.date());
            ps.setString(4,"standard");
            ps.executeUpdate();
        }
            Database.getConnection().commit();
        } catch (SQLException e) {
            Database.getConnection().rollback();
            throw e;
        }
       return agree;
    }



}
