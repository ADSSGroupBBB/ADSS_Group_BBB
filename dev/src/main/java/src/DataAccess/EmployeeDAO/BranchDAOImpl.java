package src.DataAccess.EmployeeDAO;

import src.DataAccess.EmployeeInterface.BranchDAO;
import src.util.Database;
import src.DTO.BranchDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BranchDAOImpl implements BranchDAO {

    @Override
    public List<BranchDTO> findAllBranches() throws SQLException {
        List<BranchDTO> branches = new ArrayList<>();
        // CORRECTED: Only query fields that actually exist in delivery module's locations table
        String sql = "SELECT DISTINCT address, contact_name, contact_num, zone_name FROM locations ORDER BY address";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                BranchDTO branch = new BranchDTO(
                        rs.getString("address"),
                        rs.getString("contact_name"),
                        rs.getString("contact_num"),
                        rs.getString("zone_name")  // Only zone_name exists, not zone_rank
                );
                branches.add(branch);
            }
        } catch (SQLException e) {
            System.out.println("Could not read branches from delivery module: " + e.getMessage());
        }
        return branches;
    }

    @Override
    public Optional<BranchDTO> findBranchByAddress(String address) throws SQLException {
        String sql = "SELECT DISTINCT address, contact_name, contact_num, zone_name FROM locations WHERE address = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, address);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BranchDTO branch = new BranchDTO(
                            rs.getString("address"),
                            rs.getString("contact_name"),
                            rs.getString("contact_num"),
                            rs.getString("zone_name")
                    );
                    return Optional.of(branch);
                }
            }
        } catch (SQLException e) {
            System.out.println("Could not read branch from delivery module: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean branchExists(String address) throws SQLException {
        String sql = "SELECT COUNT(*) FROM locations WHERE address = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, address);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Could not check branch existence: " + e.getMessage());
            return false;
        }
    }
}