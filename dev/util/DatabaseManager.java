package util;

import java.sql.*;

public class DatabaseManager {
    private static Connection conn;
    private static String currentDbPath;

    public static void connect(String dbPath) throws SQLException, ClassNotFoundException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
        Class.forName("org.sqlite.JDBC");
        currentDbPath = dbPath;
        conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        System.out.println("Connected to DB: " + dbPath);
    }

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            throw new SQLException("No connection. Please connect to a database first.");
        }
        return conn;
    }

    public static String getCurrentDbPath() {
        return currentDbPath;
    }
    private DatabaseManager() {}


    public static void close() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}