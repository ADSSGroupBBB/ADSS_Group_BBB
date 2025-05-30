package util;

import java.sql.*;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:super.db";
    private static Connection conn;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);

            try (Statement st = conn.createStatement()) {
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS suppliers(
                        supplierNumber INTEGER PRIMARY KEY ,
                        supplierName TEXT NOT NULL,
                        bankAccount TEXT NOT NULL,
                        payment TEXT NOT NULL,
                        contactNames TEXT,
                        telephone TEXT NOT NULL,
                        deliverySending TEXT NOT NULL
                    );
                """);
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Supplier_Agreements(
                        supplierNumber INTEGER NOT NULL,
                        agreementId INTEGER NOT NULL,
                        PRIMARY KEY (supplierNumber, agreementId),
                        FOREIGN KEY (supplierNumber) REFERENCES suppliers(supplierNumber),
                        FOREIGN KEY (agreementId) REFERENCES agreements(id)
                    );
                """);
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Supplier_Days(
                        supplierNumber INTEGER NOT NULL,
                        day TEXT NOT NULL,
                        PRIMARY KEY (supplierNumber, day),
                        FOREIGN KEY (supplierNumber) REFERENCES suppliers(supplierNumber)
                    );
                """);
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Database() {}

    public static Connection getConnection() throws SQLException {
        return conn;
    }
}

