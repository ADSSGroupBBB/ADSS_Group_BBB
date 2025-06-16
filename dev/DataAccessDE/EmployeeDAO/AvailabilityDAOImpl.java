package DataAccessDE.EmployeeDAO;

import DataAccessDE.EmployeeInterface.AvailabilityDAO;
import util.Database_HR_DL;

import java.sql.*;
import java.time.DayOfWeek;

public class AvailabilityDAOImpl implements AvailabilityDAO {

    @Override
    public boolean updateAvailability(String employeeId, DayOfWeek dayOfWeek,
                                      boolean morningAvailable, boolean eveningAvailable) throws SQLException {
        String sql = """
            INSERT OR REPLACE INTO employee_availability 
            (employee_id, day_of_week, morning_available, evening_available)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employeeId);
            ps.setString(2, dayOfWeek.toString());
            ps.setInt(3, morningAvailable ? 1 : 0);
            ps.setInt(4, eveningAvailable ? 1 : 0);

            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    @Override
    public boolean isAvailable(String employeeId, DayOfWeek dayOfWeek, String shiftType) throws SQLException {
        String sql = """
            SELECT morning_available, evening_available 
            FROM employee_availability 
            WHERE employee_id = ? AND day_of_week = ?
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employeeId);
            ps.setString(2, dayOfWeek.toString());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if ("MORNING".equalsIgnoreCase(shiftType)) {
                        return rs.getInt("morning_available") == 1;
                    } else if ("EVENING".equalsIgnoreCase(shiftType)) {
                        return rs.getInt("evening_available") == 1;
                    }
                }
            }
        }
        return false; // Default to not available if no record found
    }

    @Override
    public boolean setDefaultAvailability(String employeeId) throws SQLException {
        String sql = """
            INSERT OR IGNORE INTO employee_availability 
            (employee_id, day_of_week, morning_available, evening_available)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = Database_HR_DL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set default availability for all days of the week (available for all shifts)
            String[] days = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};

            for (String day : days) {
                ps.setString(1, employeeId);
                ps.setString(2, day);
                ps.setInt(3, 1); // morning available
                ps.setInt(4, 1); // evening available
                ps.addBatch();
            }

            int[] results = ps.executeBatch();
            return results.length > 0;
        }
    }

    /**
     * Get availability info for display purposes
     */
    public boolean getMorningAvailability(String employeeId, DayOfWeek dayOfWeek) throws SQLException {
        return isAvailable(employeeId, dayOfWeek, "MORNING");
    }

    /**
     * Get availability info for display purposes
     */
    public boolean getEveningAvailability(String employeeId, DayOfWeek dayOfWeek) throws SQLException {
        return isAvailable(employeeId, dayOfWeek, "EVENING");
    }
}