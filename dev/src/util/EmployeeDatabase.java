package util;

import java.sql.*;

public final class EmployeeDatabase {
    private static final String DB_URL = "jdbc:sqlite:deliveries.db"; // Same DB as deliveries

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            // Initialize database and create tables
            initializeDatabase();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private EmployeeDatabase() {}

    public static Connection getConnection() throws SQLException {
        // Create a new connection each time instead of reusing static connection
        return DriverManager.getConnection(DB_URL);
    }

    private static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            // Create employees table
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS employees (
                    id TEXT PRIMARY KEY,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    bank_account TEXT NOT NULL,
                    start_date TEXT NOT NULL,
                    salary REAL NOT NULL,
                    role TEXT NOT NULL,
                    password TEXT NOT NULL,
                    sick_days INTEGER NOT NULL,
                    vacation_days INTEGER NOT NULL,
                    pension_fund_name TEXT NOT NULL,
                    branch_address TEXT,
                    FOREIGN KEY(branch_address) REFERENCES locations(address)
                );
            """);

            // Create positions table
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS positions (
                    name TEXT PRIMARY KEY,
                    requires_shift_manager INTEGER NOT NULL
                );
            """);

            // Create employee qualifications table (many-to-many relationship)
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS employee_qualifications (
                    employee_id TEXT NOT NULL,
                    position_name TEXT NOT NULL,
                    PRIMARY KEY (employee_id, position_name),
                    FOREIGN KEY(employee_id) REFERENCES employees(id),
                    FOREIGN KEY(position_name) REFERENCES positions(name)
                );
            """);

            // Create employee availability table
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS employee_availability (
                    employee_id TEXT NOT NULL,
                    day_of_week TEXT NOT NULL,
                    morning_available INTEGER NOT NULL,
                    evening_available INTEGER NOT NULL,
                    PRIMARY KEY (employee_id, day_of_week),
                    FOREIGN KEY(employee_id) REFERENCES employees(id)
                );
            """);

            // Create shifts table
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS shifts (
                    id TEXT PRIMARY KEY,
                    date TEXT NOT NULL,
                    shift_type TEXT NOT NULL,
                    start_time TEXT NOT NULL,
                    end_time TEXT NOT NULL,
                    branch_address TEXT,
                    FOREIGN KEY(branch_address) REFERENCES locations(address)
                );
            """);

            // Create shift assignments table
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS shift_assignments (
                    shift_id TEXT NOT NULL,
                    employee_id TEXT NOT NULL,
                    position_name TEXT NOT NULL,
                    is_shift_manager INTEGER NOT NULL DEFAULT 0,
                    PRIMARY KEY (shift_id, position_name),
                    FOREIGN KEY(shift_id) REFERENCES shifts(id),
                    FOREIGN KEY(employee_id) REFERENCES employees(id),
                    FOREIGN KEY(position_name) REFERENCES positions(name)
                );
            """);

            // Create required positions table
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS required_positions (
                    shift_type TEXT NOT NULL,
                    position_name TEXT NOT NULL,
                    required_count INTEGER NOT NULL,
                    PRIMARY KEY (shift_type, position_name),
                    FOREIGN KEY(position_name) REFERENCES positions(name)
                );
            """);
        }

        // Insert default admin user if not exists
        insertDefaultData();
    }

    private static void insertDefaultData() throws SQLException {
        // Insert default admin user if employees table is empty
        String checkSql = "SELECT COUNT(*) FROM employees";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(checkSql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next() && rs.getInt(1) == 0) {
                // Insert admin user
                String insertAdmin = """
                    INSERT INTO employees (id, first_name, last_name, bank_account, start_date, 
                                         salary, role, password, sick_days, vacation_days, pension_fund_name)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
                try (PreparedStatement adminPs = conn.prepareStatement(insertAdmin)) {
                    adminPs.setString(1, "admin");
                    adminPs.setString(2, "Admin");
                    adminPs.setString(3, "User");
                    adminPs.setString(4, "123456");
                    adminPs.setString(5, java.time.LocalDate.now().toString());
                    adminPs.setDouble(6, 100.0);
                    adminPs.setString(7, "HR_MANAGER");
                    adminPs.setString(8, "admin123");
                    adminPs.setInt(9, 50);
                    adminPs.setInt(10, 50);
                    adminPs.setString(11, "Migdal");
                    adminPs.executeUpdate();
                }

                // Insert default availability for admin (available all days)
                String insertAvailability = """
                    INSERT INTO employee_availability (employee_id, day_of_week, morning_available, evening_available)
                    VALUES (?, ?, ?, ?)
                """;
                try (PreparedStatement availPs = conn.prepareStatement(insertAvailability)) {
                    String[] days = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
                    for (String day : days) {
                        availPs.setString(1, "admin");
                        availPs.setString(2, day);
                        availPs.setInt(3, 1); // morning available
                        availPs.setInt(4, 1); // evening available
                        availPs.addBatch();
                    }
                    availPs.executeBatch();
                }
            }
        }
    }

    /**
     * Get all available branches from the deliveries module's locations table
     * This reads dynamically from the database since locations can be added/removed
     */
    public static ResultSet getBranches() throws SQLException {
        String sql = "SELECT DISTINCT address, contact_name, contact_num, zone_name FROM locations ORDER BY address";
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

    /**
     * Check if a branch address exists in the locations table
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