package DataAccessDE.EmployeeDAO;

import DataAccessDE.EmployeeInterface.QualificationDAO;
import util.Database_HR_DL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QualificationDAOImpl implements QualificationDAO {

    @Override
    public boolean addQualification(String employeeId, String positionName) throws SQLException {
        String sql = "INSERT OR IGNORE INTO employee_qualifications (employee_id, position_name) VALUES (?, ?)";

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employeeId);
            ps.setString(2, positionName);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    @Override
    public boolean removeQualification(String employeeId, String positionName) throws SQLException {
        String sql = "DELETE FROM employee_qualifications WHERE employee_id = ? AND position_name = ?";

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employeeId);
            ps.setString(2, positionName);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    @Override
    public List<String> getEmployeeQualifications(String employeeId) throws SQLException {
        List<String> qualifications = new ArrayList<>();
        String sql = "SELECT position_name FROM employee_qualifications WHERE employee_id = ? ORDER BY position_name";

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    qualifications.add(rs.getString("position_name"));
                }
            }
        }
        return qualifications;
    }

    @Override
    public List<String> getQualifiedEmployees(String positionName) throws SQLException {
        List<String> employeeIds = new ArrayList<>();
        String sql = """
            SELECT eq.employee_id 
            FROM employee_qualifications eq
            JOIN employees e ON eq.employee_id = e.id
            WHERE eq.position_name = ?
            ORDER BY e.first_name, e.last_name
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, positionName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employeeIds.add(rs.getString("employee_id"));
                }
            }
        }
        return employeeIds;
    }

    @Override
    public boolean hasQualification(String employeeId, String positionName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM employee_qualifications WHERE employee_id = ? AND position_name = ?";

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employeeId);
            ps.setString(2, positionName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}