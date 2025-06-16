package DataAccessDE.EmployeeDAO;

import DataAccessDE.EmployeeInterface.ShiftAssignmentDAO;
import util.Database_HR_DL;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ShiftAssignmentDAOImpl implements ShiftAssignmentDAO {

    @Override
    public boolean assignEmployee(String shiftId, String employeeId, String positionName, boolean isShiftManager) throws SQLException {
        String sql = """
            INSERT OR REPLACE INTO shift_assignments 
            (shift_id, employee_id, position_name, is_shift_manager)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shiftId);
            ps.setString(2, employeeId);
            ps.setString(3, positionName);
            ps.setInt(4, isShiftManager ? 1 : 0);

            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    @Override
    public boolean removeAssignment(String shiftId, String positionName) throws SQLException {
        String sql = "DELETE FROM shift_assignments WHERE shift_id = ? AND position_name = ?";

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shiftId);
            ps.setString(2, positionName);

            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    @Override
    public Map<String, String> getShiftAssignments(String shiftId) throws SQLException {
        Map<String, String> assignments = new HashMap<>();
        String sql = """
            SELECT sa.position_name, e.first_name, e.last_name
            FROM shift_assignments sa
            JOIN employees e ON sa.employee_id = e.id
            WHERE sa.shift_id = ?
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shiftId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String position = rs.getString("position_name");
                    String employeeName = rs.getString("first_name") + " " + rs.getString("last_name");
                    assignments.put(position, employeeName);
                }
            }
        }
        return assignments;
    }

    @Override
    public boolean isEmployeeAssigned(String shiftId, String employeeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM shift_assignments WHERE shift_id = ? AND employee_id = ?";

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shiftId);
            ps.setString(2, employeeId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public String getShiftManager(String shiftId) throws SQLException {
        String sql = """
            SELECT employee_id 
            FROM shift_assignments 
            WHERE shift_id = ? AND is_shift_manager = 1
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shiftId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("employee_id");
                }
            }
        }
        return null;
    }

    /**
     * Get assignment details for a specific position in a shift
     */
    @Override
    public String getAssignedEmployee(String shiftId, String positionName) throws SQLException {
        String sql = "SELECT employee_id FROM shift_assignments WHERE shift_id = ? AND position_name = ?";

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shiftId);
            ps.setString(2, positionName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("employee_id");
                }
            }
        }
        return null;
    }

    /**
     * Get all positions assigned in a shift
     */
    public java.util.Set<String> getAssignedPositions(String shiftId) throws SQLException {
        java.util.Set<String> positions = new java.util.HashSet<>();
        String sql = "SELECT DISTINCT position_name FROM shift_assignments WHERE shift_id = ?";

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shiftId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    positions.add(rs.getString("position_name"));
                }
            }
        }
        return positions;
    }

    /**
     * Count assignments for a specific position in a shift
     */
    public int countAssignmentsForPosition(String shiftId, String positionName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM shift_assignments WHERE shift_id = ? AND position_name = ?";

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shiftId);
            ps.setString(2, positionName);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}