package DataAccessDE.EmployeeDAO;

import DataAccessDE.EmployeeInterface.PositionDAO;
import util.Database_HR_DL;
import dto.PositionDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionDAOImpl implements PositionDAO {

    @Override
    public Optional<PositionDTO> findByName(String name) throws SQLException {
        String sql = "SELECT name, requires_shift_manager FROM positions WHERE name = ?";

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PositionDTO position = new PositionDTO(
                            rs.getString("name"),
                            rs.getInt("requires_shift_manager") == 1
                    );
                    return Optional.of(position);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<PositionDTO> findAll() throws SQLException {
        List<PositionDTO> positions = new ArrayList<>();
        String sql = "SELECT name, requires_shift_manager FROM positions ORDER BY name";

        try (Connection conn = Database_HR_DL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PositionDTO position = new PositionDTO(
                        rs.getString("name"),
                        rs.getInt("requires_shift_manager") == 1
                );
                positions.add(position);
            }
        }
        return positions;
    }

    @Override
    public PositionDTO save(PositionDTO position) throws SQLException {
        String sql = "INSERT OR REPLACE INTO positions (name, requires_shift_manager) VALUES (?, ?)";

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, position.getName());
            ps.setInt(2, position.isRequiresShiftManager() ? 1 : 0);
            ps.executeUpdate();
        }

        return position;
    }

    @Override
    public boolean deleteByName(String name) throws SQLException {
        // First check if position is used by any employees
        String checkSql = "SELECT COUNT(*) FROM employee_qualifications WHERE position_name = ?";
        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {

            checkPs.setString(1, name);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // Cannot delete position that is assigned to employees
                }
            }
        }

        // Check if position is required for shifts
        String checkRequiredSql = "SELECT COUNT(*) FROM required_positions WHERE position_name = ?";
        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkRequiredSql)) {

            checkPs.setString(1, name);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // Cannot delete position that is required for shifts
                }
            }
        }

        // Delete position
        String sql = "DELETE FROM positions WHERE name = ?";
        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }
}