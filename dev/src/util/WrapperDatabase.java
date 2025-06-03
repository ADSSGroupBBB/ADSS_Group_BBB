package util;

import java.sql.*;

/**
 * EmployeeDatabase - Redirects to the unified Database class
 * This maintains compatibility with existing employee module code
 * while using the shared database
 */
public final class WrapperDatabase {

    private WrapperDatabase() {}

    /**
     * Get connection to the shared database
     * @return Connection to the unified database
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return Database.getConnection();
    }

    /**
     * Get all available branches from the shared locations table
     * @return ResultSet with branch information
     * @throws SQLException if query fails
     */
    public static ResultSet getBranches() throws SQLException {
        String sql = "SELECT DISTINCT address, contact_name, contact_num, zone_name FROM locations ORDER BY address";
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

    /**
     * Check if a branch address exists in the locations table
     * @param address Branch address to check
     * @return true if branch exists, false otherwise
     * @throws SQLException if query fails
     */
    public static boolean branchExists(String address) throws SQLException {
        String sql = "SELECT COUNT(*) FROM locations WHERE address = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, address);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}