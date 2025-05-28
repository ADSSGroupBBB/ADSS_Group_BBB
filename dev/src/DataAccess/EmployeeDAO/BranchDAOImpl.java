package DataAccess.EmployeeDAO;

import DataAccess.EmployeeInterface.BranchDAO;
import util.EmployeeDatabase;
import Service_employee.BranchDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BranchDAOImpl implements BranchDAO {

    @Override
    public List<BranchDTO> findAllBranches() throws SQLException {
        List<BranchDTO> branches = new ArrayList<>();
        String sql = "SELECT DISTINCT address, contact_name, contact_num, zone_name FROM locations ORDER BY address";

        try (Connection conn = EmployeeDatabase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                BranchDTO branch = new BranchDTO(
                        rs.getString("address"),
                        rs.getString("contact_name"),
                        rs.getString("contact_num"),
                        rs.getString("zone_name")
                );
                branches.add(branch);
            }
        }
        return branches;
    }

    @Override
    public Optional<BranchDTO> findBranchByAddress(String address) throws SQLException {
        String sql = "SELECT DISTINCT address, contact_name, contact_num, zone_name FROM locations WHERE address = ?";

        try (Connection conn = EmployeeDatabase.getConnection();
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
        }
        return Optional.empty();
    }

    @Override
    public boolean branchExists(String address) throws SQLException {
        String sql = "SELECT COUNT(*) FROM locations WHERE address = ?";

        try (Connection conn = EmployeeDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, address);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}