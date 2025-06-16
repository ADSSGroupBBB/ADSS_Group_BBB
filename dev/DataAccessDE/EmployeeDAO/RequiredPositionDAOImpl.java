package DataAccessDE.EmployeeDAO;

import DataAccessDE.EmployeeInterface.RequiredPositionDAO;
import util.Database_HR_DL;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class RequiredPositionDAOImpl implements RequiredPositionDAO {

    @Override
    public boolean setRequiredPosition(String shiftType, String positionName, int count) throws SQLException {
        String sql = """
            INSERT OR REPLACE INTO required_positions (shift_type, position_name, required_count)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shiftType.toUpperCase());
            ps.setString(2, positionName);
            ps.setInt(3, count);

            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    @Override
    public int getRequiredCount(String shiftType, String positionName) throws SQLException {
        String sql = """
            SELECT required_count 
            FROM required_positions 
            WHERE shift_type = ? AND position_name = ?
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shiftType.toUpperCase());
            ps.setString(2, positionName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("required_count");
                }
            }
        }
        return 0; // Default to 0 if no requirement found
    }

    @Override
    public Map<String, Integer> getRequiredPositions(String shiftType) throws SQLException {
        Map<String, Integer> requirements = new HashMap<>();
        String sql = """
            SELECT position_name, required_count 
            FROM required_positions 
            WHERE shift_type = ?
            ORDER BY position_name
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shiftType.toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    requirements.put(
                            rs.getString("position_name"),
                            rs.getInt("required_count")
                    );
                }
            }
        }
        return requirements;
    }

    @Override
    public boolean removeRequiredPosition(String shiftType, String positionName) throws SQLException {
        String sql = "DELETE FROM required_positions WHERE shift_type = ? AND position_name = ?";

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shiftType.toUpperCase());
            ps.setString(2, positionName);

            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    /**
     * Get all shift types that have requirements
     */
    public java.util.Set<String> getAllShiftTypes() throws SQLException {
        java.util.Set<String> shiftTypes = new java.util.HashSet<>();
        String sql = "SELECT DISTINCT shift_type FROM required_positions";

        try (Connection conn = Database_HR_DL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                shiftTypes.add(rs.getString("shift_type"));
            }
        }
        return shiftTypes;
    }
}