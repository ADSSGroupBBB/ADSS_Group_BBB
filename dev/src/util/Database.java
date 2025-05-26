package util;

import java.sql.*;
import java.util.*;

public final class Database {
    private static final String DB_URL = "jdbc:sqlite:stocks.db";
    private static Connection conn;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);

            try (Statement st = conn.createStatement()) {
                // Only one correct definition of locations
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
                        doc_id        INTEGER PRIMARY KEY,
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
                        id           INTEGER PRIMARY KEY AUTOINCREMENT,
                        document_id  INTEGER NOT NULL,
                        name         TEXT NOT NULL,
                        weight       INTEGER NOT NULL,
                        amount       INTEGER NOT NULL,
                        FOREIGN KEY(document_id) REFERENCES documents(doc_id)
                    );
                """);

                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS locations (
                        id            INTEGER PRIMARY KEY AUTOINCREMENT,
                        document_id   INTEGER NOT NULL,
                        address       TEXT NOT NULL,
                        contact_name  TEXT NOT NULL,
                        contact_num   TEXT NOT NULL,
                        zone_name     TEXT NOT NULL,
                        FOREIGN KEY(document_id) REFERENCES documents(doc_id),
                        FOREIGN KEY(zone_name) REFERENCES shipping_zones(name)
                    );
                """);
            }

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
                ps.setInt(1, 1);
                ps.setString(2, "2025-05-26");
                ps.setString(3, "T1");
                ps.setString(4, "08:00");
                ps.setString(5, "D1");
                ps.setString(6, "HQ");
                ps.setString(7, "Departure scheduled");
                ps.executeUpdate();
            }

            // Insert shipment items
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO shipment_items (document_id, name, weight, amount) VALUES (?, ?, ?, ?)");) {
                Object[][] items = {
                        {"Milk", 10, 1},
                        {"Bread", 5, 1},
                        {"Juice", 20, 1},
                        {"Eggs", 2, 1},
                        {"Meat", 15, 1},
                };
                for (Object[] item : items) {
                    ps.setInt(1, 1);
                    ps.setString(2, (String) item[0]);
                    ps.setInt(3, (int) item[1]);
                    ps.setInt(4, (int) item[2]);
                    ps.executeUpdate();
                }
            }

            // Insert locations
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO locations (document_id, address, contact_name, contact_num, zone_name) VALUES (?, ?, ?, ?, ?)");) {
                Object[][] locs = {
                        {"Rager", "Omer", "555-1234", "Downtown"},
                        {"Hazaz", "Ben", "555-5678", "Airport"},
                        {"Metzada", "Lior", "555-9012", "Suburbs"},
                        {"Bialik", "Meir", "555-3456", "Industrial Park"},
                        {"Bengurion", "Ariel", "555-7890", "Shopping District"},
                        {"Headquarters", "Noam", "123-456", "Downtown"},
                };
                for (Object[] loc : locs) {
                    ps.setInt(1, 1);
                    ps.setString(2, (String) loc[0]);
                    ps.setString(3, (String) loc[1]);
                    ps.setString(4, (String) loc[2]);
                    ps.setString(5, (String) loc[3]);
                    ps.executeUpdate();
                }
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
