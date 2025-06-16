package util;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.List;
import java.util.ArrayList;

/**
 * Unified Database class for both Employee and Delivery modules
 */
public final class Database_HR_DL {
    private static final String DB_URL = "jdbc:sqlite:company_system.db";
    private static boolean initializeWithSampleData = true; // Control sample data insertion

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            initializeDatabase();
        } catch (Exception e) {
            System.err.println("Error in connecting to database: " + e.getMessage());
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    private Database_HR_DL() {}

    /**
     * Call this method BEFORE first database access to start with empty tables
     */
    public static void initializeEmptyDatabase() {
        initializeWithSampleData = false;
        try {
            // Delete existing database file to start fresh
            deleteDatabase();
            // Force re-initialization without sample data
            initializeDatabase();
        } catch (SQLException e) {
            System.err.println("Error initializing empty database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Call this method to reset database with sample data
     */
    public static void resetWithSampleData() {
        initializeWithSampleData = true;
        try {
            deleteDatabase();
            initializeDatabase();
        } catch (SQLException e) {
            System.err.println("Error resetting database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Delete all data from tables (keep structure)
     */
    public static void clearAllData() throws SQLException {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            // Disable foreign keys temporarily
            st.execute("PRAGMA foreign_keys = OFF");

            // Delete data from all tables (order matters due to foreign keys)
            String[] tablesToClear = {
                    "shift_assignments", "employee_availability", "employee_qualifications",
                    "required_positions", "shifts", "employees", "positions",
                    "doc_locations", "doc_items", "documents", "locations",
                    "shipment_items", "trucks", "shipping_zones"
            };

            for (String table : tablesToClear) {
                st.executeUpdate("DELETE FROM " + table);
            }

            // Re-enable foreign keys
            st.execute("PRAGMA foreign_keys = ON");

            System.out.println("All data cleared from database tables.");
        }
    }

    private static void deleteDatabase() {
        try {
            java.io.File dbFile = new java.io.File("company_system.db");
            if (dbFile.exists()) {
                dbFile.delete();
            }
        } catch (Exception e) {
            System.err.println("Could not delete database file: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        // Enable foreign keys for this connection
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    private static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            // Enable foreign keys
            st.execute("PRAGMA foreign_keys = ON");

            // Create all tables
            createDeliveryTables(st);
            createEmployeeTables(st);
        }

        // Only insert sample data if flag is true AND data doesn't already exist
        if (initializeWithSampleData && !dataExists()) {
            insertDeliveryData();
            insertEmployeeData();
            System.out.println("Database initialized with sample data.");
        } else if (!initializeWithSampleData) {
            System.out.println("Database initialized with empty tables.");
        }
    }

    /**
     * Check if sample data already exists to prevent duplicates
     */
    private static boolean dataExists() {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM trucks")) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // If trucks table has data, assume all sample data exists
            }
        } catch (SQLException e) {
            // If table doesn't exist or error, assume no data
            return false;
        }
        return false;
    }

    private static void createDeliveryTables(Statement st) throws SQLException {
        st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS shipping_zones (
                    name TEXT PRIMARY KEY,
                    rank INTEGER NOT NULL
                );
            """);

        st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS trucks(
                    truck_id     TEXT PRIMARY KEY,
                    type         INTEGER NOT NULL,
                    truck_weight INTEGER NOT NULL,
                    curr_weight  INTEGER NOT NULL,
                    max_weight   INTEGER NOT NULL,
                    on_drive     INTEGER NOT NULL
                );
            """);

        st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS documents (
                    id            INTEGER PRIMARY KEY AUTOINCREMENT,
                    doc_id        TEXT NOT NULL,
                    date          TEXT NOT NULL,
                    truck_id      TEXT NOT NULL,
                    dep_hour      TEXT NOT NULL,
                    driver_id     TEXT NOT NULL,
                    dep_from      TEXT NOT NULL,
                    event_message TEXT NOT NULL,
                    FOREIGN KEY(truck_id) REFERENCES trucks(truck_id)
                );
            """);

        st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS shipment_items (
                    name         TEXT PRIMARY KEY,
                    weight       INTEGER NOT NULL
                );
            """);

        st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS doc_items (
                    doc_id       TEXT NOT NULL,
                    name         TEXT NOT NULL,
                    weight       INTEGER NOT NULL,
                    amount       INTEGER NOT NULL,
                    PRIMARY KEY (doc_id, name),  -- Composite key
                    FOREIGN KEY(name) REFERENCES shipment_items(name)
                );
            """);

        st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS locations (
                    address       TEXT PRIMARY KEY,
                    contact_name  TEXT NOT NULL,
                    contact_num   TEXT NOT NULL,
                    zone_name     TEXT NOT NULL,
                    zone_rank     TEXT NOT NULL,
                    FOREIGN KEY(zone_name) REFERENCES shipping_zones(name)
                );
            """);

        st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS doc_locations (
                        doc_id   TEXT NOT NULL,
                        address  TEXT NOT NULL,
                        PRIMARY KEY (doc_id, address),  -- Composite key
                        FOREIGN KEY(address) REFERENCES locations(address)
                );
            """);
    }

    private static void createEmployeeTables(Statement st) throws SQLException {
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

        st.executeUpdate("""
            CREATE TABLE IF NOT EXISTS drivers (
                id TEXT NOT NULL,
                license INTEGER NOT NULL,
                on_drive INTEGER NOT NULL,
                PRIMARY KEY(id, license),
                FOREIGN KEY(id) REFERENCES employees(id)
            );
        """);

        // Create positions table
        st.executeUpdate("""
            CREATE TABLE IF NOT EXISTS positions (
                name TEXT PRIMARY KEY,
                requires_shift_manager INTEGER NOT NULL
            );
        """);

        // Create employee qualifications table
        st.executeUpdate("""
            CREATE TABLE IF NOT EXISTS employee_qualifications (
                employee_id TEXT NOT NULL,
                position_name TEXT NOT NULL,
                PRIMARY KEY (employee_id, position_name),
                FOREIGN KEY(employee_id) REFERENCES employees(id) ON DELETE CASCADE,
                FOREIGN KEY(position_name) REFERENCES positions(name) ON DELETE CASCADE
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
                FOREIGN KEY(employee_id) REFERENCES employees(id) ON DELETE CASCADE
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
                FOREIGN KEY(shift_id) REFERENCES shifts(id) ON DELETE CASCADE,
                FOREIGN KEY(employee_id) REFERENCES employees(id) ON DELETE CASCADE,
                FOREIGN KEY(position_name) REFERENCES positions(name) ON DELETE CASCADE
            );
        """);

        // Create required positions table
        st.executeUpdate("""
            CREATE TABLE IF NOT EXISTS required_positions (
                shift_type TEXT NOT NULL,
                position_name TEXT NOT NULL,
                required_count INTEGER NOT NULL,
                PRIMARY KEY (shift_type, position_name),
                FOREIGN KEY(position_name) REFERENCES positions(name) ON DELETE CASCADE
            );
        """);
    }

    // Keep all your existing insert methods unchanged...
    private static void insertDeliveryData() throws SQLException {
        try (Connection conn = getConnection()) {
            // Insert shipping zones
            try (PreparedStatement ps = conn.prepareStatement("INSERT OR IGNORE INTO shipping_zones (name, rank) VALUES (?, ?)");) {
                String[] names = {"Downtown", "Airport", "Suburbs", "Industrial Park", "Shopping District"};
                int[] ranks = {1, 2, 3, 4, 5};
                for (int i = 0; i < names.length; i++) {
                    ps.setString(1, names[i]);
                    ps.setInt(2, ranks[i]);
                    ps.executeUpdate();
                }
            }

            // Insert trucks
            try (PreparedStatement ps = conn.prepareStatement("INSERT OR IGNORE INTO trucks (truck_id, type, truck_weight, curr_weight, max_weight, on_drive) VALUES (?, ?, ?, ?, ?, ?)");) {
                Object[][] trucks = {
                        {"T1", 111, 1, 5, 100, 0},
                        {"T2", 222, 10000, 20000, 30000, 0},
                        {"T3", 333, 10000, 19000, 30000, 0},
                        {"T4", 111, 10000, 16000, 30000, 0},
                        {"T5", 222, 10000, 30000, 40000, 0},
                };
                for (Object[] t : trucks) {
                    for (int i = 0; i < t.length; i++) ps.setObject(i + 1, t[i]);
                    ps.executeUpdate();
                }
            }

            // Insert one document (for simplicity)
            try (PreparedStatement ps = conn.prepareStatement("INSERT OR IGNORE INTO documents (doc_id, date, truck_id, dep_hour, driver_id, dep_from, event_message) VALUES (?, ?, ?, ?, ?, ?, ?)");) {
                ps.setString(1, "DOC-001");
                ps.setString(2, "2025-05-26");
                ps.setString(3, "T1");
                ps.setString(4, "08:00");
                ps.setString(5, "D1");
                ps.setString(6, "Headquarters");
                ps.setString(7, "Delivery finished.");
                ps.executeUpdate();
            }

            // Insert shipment items
            try (PreparedStatement ps = conn.prepareStatement("INSERT OR IGNORE INTO shipment_items (name, weight) VALUES (?, ?)");) {
                Object[][] items = {
                        {"Milk", 10},
                        {"Bread", 5},
                        {"Juice", 20},
                        {"Eggs", 2},
                        {"Meat", 15},
                };
                for (Object[] item : items) {
                    ps.setString(1, (String) item[0]);
                    ps.setInt(2, (int) item[1]);
                    ps.executeUpdate();
                }
            }

            // Insert locations - SAMPLE DATA for first run
            try (PreparedStatement ps = conn.prepareStatement("INSERT OR IGNORE INTO locations (address, contact_name, contact_num, zone_name, zone_rank) VALUES (?, ?, ?, ?, ?)")) {
                Object[][] locations = {
                        {"Headquarters", "Noam", "123-456-7890", "Downtown", 1},
                        {"Main Branch", "Sarah", "555-1111", "Downtown", 1},
                        {"Hazaz Street", "Ben", "555-5678", "Airport", 2},
                        {"Metzada Avenue", "Lior", "555-9012", "Suburbs", 3},
                        {"Bialik Boulevard", "Meir", "555-3456", "Industrial Park", 4},
                        {"Ben Gurion Road", "Ariel", "555-7890", "Shopping District", 5}
                };

                for (Object[] loc : locations) {
                    ps.setString(1, (String) loc[0]);
                    ps.setString(2, (String) loc[1]);
                    ps.setString(3, (String) loc[2]);
                    ps.setString(4, (String) loc[3]);
                    ps.setInt(5, (Integer) loc[4]);
                    ps.executeUpdate();
                }
            }
        }
    }

    // Keep all other insert methods exactly as they were...
    private static void insertEmployeeData() throws SQLException {
        try (Connection conn = getConnection()) {
            // Get available branches DYNAMICALLY from locations table
            List<String> branches = getAvailableBranches(conn);

            // Insert positions
            try (PreparedStatement ps = conn.prepareStatement("INSERT OR IGNORE INTO positions (name, requires_shift_manager) VALUES (?, ?)")) {
                String[][] positions = {
                        {"Cashier", "0"},
                        {"Stocker", "0"},
                        {"Security", "0"},
                        {"Customer Service", "0"},
                        {"Floor Manager", "1"},
                        {"Driver", "0"},
                        {"STORE_KEEPER", "0"}
                };

                for (String[] pos : positions) {
                    ps.setString(1, pos[0]);
                    ps.setInt(2, Integer.parseInt(pos[1]));
                    ps.executeUpdate();
                }
            }

            // Insert required positions
            try (PreparedStatement ps = conn.prepareStatement("INSERT OR IGNORE INTO required_positions (shift_type, position_name, required_count) VALUES (?, ?, ?)")) {
                String[][] requirements = {
                        {"MORNING", "Cashier", "2"}, {"MORNING", "Stocker", "1"}, {"MORNING", "Security", "1"},
                        {"MORNING", "Floor Manager", "1"}, {"MORNING", "Customer Service", "1"}, {"MORNING", "STORE_KEEPER", "1"},
                        {"EVENING", "Cashier", "3"}, {"EVENING", "Stocker", "2"}, {"EVENING", "Security", "1"},
                        {"EVENING", "Floor Manager", "1"}, {"EVENING", "Customer Service", "2"}, {"EVENING", "STORE_KEEPER", "1"},
                        {"MORNING", "Driver", "1"}, {"EVENING", "Driver", "1"}, {"EVENING", "STORE_KEEPER", "1"}
                };

                for (String[] req : requirements) {
                    ps.setString(1, req[0]);
                    ps.setString(2, req[1]);
                    ps.setInt(3, Integer.parseInt(req[2]));
                    ps.executeUpdate();
                }
            }

            // Insert employees with DYNAMIC branch assignment
            LocalDate today = LocalDate.now();
            try (PreparedStatement ps = conn.prepareStatement("INSERT OR IGNORE INTO employees (id, first_name, last_name, bank_account, start_date, salary, role, password, sick_days, vacation_days, pension_fund_name, branch_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                // Admin user - assign to first available branch
                String adminBranch = branches.isEmpty() ? null : branches.get(0);
                ps.setString(1, "admin");
                ps.setString(2, "Admin");
                ps.setString(3, "User");
                ps.setString(4, "123456");
                ps.setString(5, today.toString());
                ps.setDouble(6, 100.0);
                ps.setString(7, "HR_MANAGER");
                ps.setString(8, "admin123");
                ps.setInt(9, 50);
                ps.setInt(10, 50);
                ps.setString(11, "Migdal");
                ps.setString(12, adminBranch);
                ps.executeUpdate();

                // Employee data array
                Object[][] employees = {
                        // Regular employees
                        {"111", "Yossi", "Cohen", "12345", 40.0, "REGULAR_EMPLOYEE", "111", 10, 20, "Menora"},
                        {"222", "Sara", "Levi", "23456", 42.0, "REGULAR_EMPLOYEE", "222", 12, 15, "Harel"},
                        {"333", "David", "Israeli", "34567", 38.0, "REGULAR_EMPLOYEE", "333", 8, 10, "Migdal"},
                        {"444", "Noa", "Golan", "45678", 45.0, "REGULAR_EMPLOYEE", "444", 14, 18, "Phoenix"},
                        {"555", "Moshe", "Peretz", "56789", 40.0, "REGULAR_EMPLOYEE", "555", 7, 12, "Clal"},

                        // Shift manager
                        {"666", "Rachel", "Mizrahi", "67890", 55.0, "SHIFT_MANAGER", "666", 15, 20, "Menora"},

                        // Drivers
                        {"D1", "Avi", "Cohen", "654533", 44.0, "DRIVER", "D1", 25, 55, "Clal"},
                        {"D2", "Sarah", "Levi", "789012", 43.0, "DRIVER", "D2", 22, 50, "Harel"},
                        {"D3", "Dan", "Ron", "890123", 41.0, "DRIVER", "D3", 20, 45, "Migdal"},
                        {"D4", "Muhammad", "Younes", "901234", 39.0, "DRIVER", "D4", 18, 40, "Phoenix"},
                        {"D5", "Lionel", "Messi", "012345", 37.0, "DRIVER", "D5", 30, 60, "Menora"},

                        // Store keepers
                        {"SK1", "Tamar", "Shapiro", "111222", 38.0, "STORE_KEEPER", "SK1", 12, 25, "Clal"},
                        {"SK2", "Omar", "Khatib", "222333", 42.0, "STORE_KEEPER", "SK2", 15, 30, "Harel"},
                        {"SK3", "Ruth", "Ben-David", "333444", 40.0, "STORE_KEEPER", "SK3", 10, 22, "Migdal"},
                        {"SK4", "Ahmed", "Mansour", "444555", 45.0, "STORE_KEEPER", "SK4", 18, 35, "Phoenix"},
                        {"SK5", "Miriam", "Goldberg", "555666", 39.0, "STORE_KEEPER", "SK5", 13, 28, "Menora"}
                };

                // Insert employees with dynamic branch assignment
                for (int i = 0; i < employees.length; i++) {
                    Object[] emp = employees[i];
                    String branchForEmployee = branches.isEmpty() ? null : branches.get(i % branches.size());

                    ps.setString(1, (String) emp[0]); // id
                    ps.setString(2, (String) emp[1]); // first_name
                    ps.setString(3, (String) emp[2]); // last_name
                    ps.setString(4, (String) emp[3]); // bank_account
                    ps.setString(5, today.toString()); // start_date
                    ps.setDouble(6, (Double) emp[4]); // salary
                    ps.setString(7, (String) emp[5]); // role
                    ps.setString(8, (String) emp[6]); // password
                    ps.setInt(9, (Integer) emp[7]); // sick_days
                    ps.setInt(10, (Integer) emp[8]); // vacation_days
                    ps.setString(11, (String) emp[9]); // pension_fund_name
                    ps.setString(12, branchForEmployee); // branch_address
                    ps.executeUpdate();
                }
            }

            // Insert driver licenses into drivers table
            try (PreparedStatement ps = conn.prepareStatement("INSERT OR IGNORE INTO drivers (id, license, on_drive) VALUES (?, ?, ?)")) {
                // Driver license data
                Object[][] driverLicenses = {
                        {"D1", 111, 0}, {"D1", 222, 0}, {"D1", 333, 0},
                        {"D2", 111, 0}, {"D2", 222, 0}, {"D2", 333, 0},
                        {"D3", 222, 0}, {"D3", 333, 0},
                        {"D4", 111, 0}, {"D4", 444, 0},
                        {"D5", 111, 0}, {"D5", 222, 0}
                };

                for (Object[] license : driverLicenses) {
                    ps.setString(1, (String) license[0]); // driver id
                    ps.setInt(2, (Integer) license[1]); // license type
                    ps.setInt(3, (Integer) license[2]); // on_drive status
                    ps.executeUpdate();
                }
            }

            // Insert qualifications, availability, and shifts
            insertQualifications(conn);
            insertAvailability(conn);
            insertSampleShifts(conn, branches);
        }
    }

    private static List<String> getAvailableBranches(Connection conn) {
        List<String> branches = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT address FROM locations ORDER BY address";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    branches.add(rs.getString("address"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading branches: " + e.getMessage());
        }
        return branches;
    }

    private static void insertQualifications(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT OR IGNORE INTO employee_qualifications (employee_id, position_name) VALUES (?, ?)")) {
            String[][] qualifications = {
                    // Regular employees
                    {"111", "Cashier"}, {"111", "Customer Service"},
                    {"222", "Cashier"}, {"222", "Stocker"},
                    {"333", "Security"},
                    {"444", "Customer Service"}, {"444", "Cashier"},
                    {"555", "Stocker"},

                    // Shift manager
                    {"666", "Floor Manager"},

                    // Drivers - all qualified for Driver position
                    {"D1", "Driver"},
                    {"D2", "Driver"},
                    {"D3", "Driver"},
                    {"D4", "Driver"},
                    {"D5", "Driver"},

                    // Store keepers - all qualified for STORE_KEEPER position
                    {"SK1", "STORE_KEEPER"},
                    {"SK2", "STORE_KEEPER"},
                    {"SK3", "STORE_KEEPER"},
                    {"SK4", "STORE_KEEPER"},
                    {"SK5", "STORE_KEEPER"}
            };

            for (String[] qual : qualifications) {
                ps.setString(1, qual[0]);
                ps.setString(2, qual[1]);
                ps.executeUpdate();
            }
        }
    }



    private static void insertAvailability(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT OR IGNORE INTO employee_availability (employee_id, day_of_week, morning_available, evening_available) VALUES (?, ?, ?, ?)")) {
            // Include ALL employees: admin, regular employees, shift manager, drivers, and store keepers
            String[] employees = {
                    "admin", "111", "222", "333", "444", "555", "666",  // existing employees
                    "D1", "D2", "D3", "D4", "D5",  // drivers
                    "SK1", "SK2", "SK3", "SK4", "SK5"  // store keepers
            };
            String[] days = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};

            for (String empId : employees) {
                for (String day : days) {
                    ps.setString(1, empId);
                    ps.setString(2, day);
                    ps.setInt(3, 1);  // morning_available = true
                    ps.setInt(4, 1);  // evening_available = true
                    ps.executeUpdate();
                }
            }
        }
    }

    private static void insertSampleShifts(Connection conn, List<String> branches) throws SQLException {
        if (branches.isEmpty()) return;
        for (int i=0; i < branches.size(); i++){
            createShiftsForWeek(conn, LocalDate.now().minusWeeks(1), branches.get(i), false);
            createShiftsForWeek(conn, LocalDate.of(2025, 7, 20), branches.get(i), true);
        }
    }

    private static void createShiftsForWeek(Connection conn, LocalDate startDate, String branchAddress, boolean assignEmployees) throws SQLException {
        while (startDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
            startDate = startDate.minusDays(1);
        }

        String shiftSql = "INSERT OR IGNORE INTO shifts (id, date, shift_type, start_time, end_time, branch_address) VALUES (?, ?, ?, ?, ?, ?)";
        String assignmentSql = "INSERT OR IGNORE INTO shift_assignments (shift_id, employee_id, position_name, is_shift_manager) VALUES (?, ?, ?, ?)";

        try (PreparedStatement shiftPs = conn.prepareStatement(shiftSql);
             PreparedStatement assignPs = conn.prepareStatement(assignmentSql)) {

            LocalDate currentDate = startDate;
            for (int i = 0; i < 7; i++) {
                String branchSuffix = branchAddress != null ? "_" + branchAddress.replaceAll("\\s+", "") : "";

                String morningShiftId = currentDate.toString() + "_morning" + branchSuffix;
                shiftPs.setString(1, morningShiftId);
                shiftPs.setString(2, currentDate.toString());
                shiftPs.setString(3, "MORNING");
                shiftPs.setString(4, LocalTime.of(7, 0).toString());
                shiftPs.setString(5, LocalTime.of(14, 0).toString());
                shiftPs.setString(6, branchAddress);
                shiftPs.executeUpdate();

                String eveningShiftId = currentDate.toString() + "_evening" + branchSuffix;
                shiftPs.setString(1, eveningShiftId);
                shiftPs.setString(2, currentDate.toString());
                shiftPs.setString(3, "EVENING");
                shiftPs.setString(4, LocalTime.of(14, 0).toString());
                shiftPs.setString(5, LocalTime.of(21, 0).toString());
                shiftPs.setString(6, branchAddress);
                shiftPs.executeUpdate();

                if (assignEmployees) {
                    // Assign shift manager (existing code)
                    assignPs.setString(1, morningShiftId);
                    assignPs.setString(2, "666");
                    assignPs.setString(3, "Floor Manager");
                    assignPs.setInt(4, 1);
                    assignPs.executeUpdate();

                    assignPs.setString(1, eveningShiftId);
                    assignPs.setString(2, "666");
                    assignPs.setString(3, "Floor Manager");
                    assignPs.setInt(4, 1);
                    assignPs.executeUpdate();


                    if (currentDate.equals(LocalDate.of(2025, 7, 20))) {
                        assignDriversAndStoreKeepersForDate(conn, assignPs, morningShiftId, eveningShiftId, branchAddress);
                    }

                    if ("Headquarters".equals(branchAddress)) {
                        assignStoreKeepersForHeadquarters(conn, assignPs, morningShiftId, eveningShiftId, branchAddress);
                    }
                }

                currentDate = currentDate.plusDays(1);
            }
        }
    }

    /**
     * Helper method to assign store keepers to every shift at Headquarters
     */
    private static void assignStoreKeepersForHeadquarters(Connection conn, PreparedStatement assignPs,
                                                          String morningShiftId, String eveningShiftId,
                                                          String branchAddress) throws SQLException {

        // Get store keepers for Headquarters who are qualified for the STORE_KEEPER position
        String storeKeeperQuery = """
        SELECT e.id 
        FROM employees e
        INNER JOIN employee_qualifications eq ON e.id = eq.employee_id
        WHERE e.role = 'STORE_KEEPER' 
          AND e.branch_address = ?
          AND eq.position_name = 'STORE_KEEPER'
        """;

        try (PreparedStatement storeKeeperPs = conn.prepareStatement(storeKeeperQuery)) {
            storeKeeperPs.setString(1, branchAddress);
            try (ResultSet storeKeeperRs = storeKeeperPs.executeQuery()) {
                while (storeKeeperRs.next()) {
                    String storeKeeperId = storeKeeperRs.getString("id");

                    // Morning shift assignment for store keeper
                    assignPs.setString(1, morningShiftId);
                    assignPs.setString(2, storeKeeperId);
                    assignPs.setString(3, "STORE_KEEPER");
                    assignPs.setInt(4, 0); // not shift manager
                    assignPs.executeUpdate();

                    // Evening shift assignment for store keeper
                    assignPs.setString(1, eveningShiftId);
                    assignPs.setString(2, storeKeeperId);
                    assignPs.setString(3, "STORE_KEEPER");
                    assignPs.setInt(4, 0); // not shift manager
                    assignPs.executeUpdate();
                }
            }
        }
    }

    /**
     * Helper method to assign drivers and store keepers for a specific date
     * Only assigns employees to positions they are qualified for
     */
    private static void assignDriversAndStoreKeepersForDate(Connection conn, PreparedStatement assignPs,
                                                            String morningShiftId, String eveningShiftId,
                                                            String branchAddress) throws SQLException {

        // Get drivers for this branch who are qualified for the Driver position
        String driverQuery = """
        SELECT e.id 
        FROM employees e
        INNER JOIN employee_qualifications eq ON e.id = eq.employee_id
        WHERE e.role = 'DRIVER' 
          AND e.branch_address = ?
          AND eq.position_name = 'Driver'
        """;

        try (PreparedStatement driverPs = conn.prepareStatement(driverQuery)) {
            driverPs.setString(1, branchAddress);
            try (ResultSet driverRs = driverPs.executeQuery()) {
                while (driverRs.next()) {
                    String driverId = driverRs.getString("id");

                    // Morning shift assignment for driver
                    assignPs.setString(1, morningShiftId);
                    assignPs.setString(2, driverId);
                    assignPs.setString(3, "Driver");
                    assignPs.setInt(4, 0); // not shift manager
                    assignPs.executeUpdate();

                    // Evening shift assignment for driver
                    assignPs.setString(1, eveningShiftId);
                    assignPs.setString(2, driverId);
                    assignPs.setString(3, "Driver");
                    assignPs.setInt(4, 0); // not shift manager
                    assignPs.executeUpdate();
                }
            }
        }

        // Get store keepers for this branch who are qualified for the STORE_KEEPER position
        String storeKeeperQuery = """
        SELECT e.id 
        FROM employees e
        INNER JOIN employee_qualifications eq ON e.id = eq.employee_id
        WHERE e.role = 'STORE_KEEPER' 
          AND e.branch_address = ?
          AND eq.position_name = 'STORE_KEEPER'
        """;

        try (PreparedStatement storeKeeperPs = conn.prepareStatement(storeKeeperQuery)) {
            storeKeeperPs.setString(1, branchAddress);
            try (ResultSet storeKeeperRs = storeKeeperPs.executeQuery()) {
                while (storeKeeperRs.next()) {
                    String storeKeeperId = storeKeeperRs.getString("id");

                    // Morning shift assignment for store keeper
                    assignPs.setString(1, morningShiftId);
                    assignPs.setString(2, storeKeeperId);
                    assignPs.setString(3, "STORE_KEEPER");
                    assignPs.setInt(4, 0); // not shift manager
                    assignPs.executeUpdate();

                    // Evening shift assignment for store keeper
                    assignPs.setString(1, eveningShiftId);
                    assignPs.setString(2, storeKeeperId);
                    assignPs.setString(3, "STORE_KEEPER");
                    assignPs.setInt(4, 0); // not shift manager
                    assignPs.executeUpdate();
                }
            }
        }
    }


    /**
     * Get all available branches from the locations table
     * @return List of branch information as String arrays [address, contact_name, contact_num, zone_name]
     * @throws SQLException if query fails
     */
    public static List<String[]> getBranches() throws SQLException {
        String sql = "SELECT DISTINCT address, contact_name, contact_num, zone_name FROM locations ORDER BY address";
        List<String[]> branches = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                branches.add(new String[] {
                        rs.getString("address"),
                        rs.getString("contact_name"),
                        rs.getString("contact_num"),
                        rs.getString("zone_name")
                });
            }
        }

        return branches;
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
