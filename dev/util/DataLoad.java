package util;

import java.sql.*;

public class DataLoad {

    public static void loadDefaultData(Connection conn) throws SQLException {
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement("""
                    INSERT OR IGNORE INTO products(productName, productNumber, unitOfMeasure, manufacturer)
                    VALUES (?, ?, ?, ?);
                """)) {
                Object[][] prods = {
                        {"Milk", 1, "liter", "Tnuva"},
                        {"Bread", 2, "kg", "Angel"},
                        {"Cheese", 3, "kg", "Tnuva"},
                        {"Eggs", 4, "g", "Yehuda"},
                        {"Butter", 5, "g", "Tara"},
                        {"Juice", 6, "liter", "Prigat"},
                        {"Rice", 7, "kg", "Osem"},
                        {"Pasta", 8, "g", "Barilla"},
                        {"Yogurt", 9, "liter", "Danone"},
                        {"Tea", 10, "g", "Wissotzky"},
                        {"Flour", 11, "g", "Sugat"},
                        {"Oil", 12, "liter", "Yad Mordechai"}
                };
                for (Object[] p : prods) {
                    ps.setString(1, (String) p[0]);
                    ps.setInt(2, (int) p[1]);
                    ps.setString(3, (String) p[2]);
                    ps.setString(4, (String) p[3]);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            try (PreparedStatement psSup = conn.prepareStatement("""
                    INSERT OR IGNORE INTO suppliers(supplierNumber, supplierName, bankAccount, payment, contactNames, telephone, deliverySending, address, contactPhone)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
                """);
                 PreparedStatement psDay = conn.prepareStatement("""
                    INSERT OR IGNORE INTO Supplier_Days(supplierNumber, day) VALUES (?, ?);
                """)) {

                for (int i = 1; i <= 5; i++) {
                    psSup.setInt(1, i);
                    psSup.setString(2, "Supplier" + i);
                    psSup.setString(3, "Bank" + i);
                    psSup.setString(4, (i % 2 == 0) ? "Credit" : "Cash");
                    psSup.setString(5, "Contact" + i);
                    psSup.setString(6, "050000000" + i);
                    psSup.setString(7, "constant");
                    psSup.setString(8, "City" + i);
                    psSup.setString(9, "050111111" + i);
                    psSup.addBatch();

                    String day = switch (i) {
                        case 1 -> "Sunday";
                        case 2 -> "Monday";
                        case 3 -> "Tuesday";
                        case 4 -> "Wednesday";
                        default -> "Thursday";
                    };
                    psDay.setInt(1, i);
                    psDay.setString(2, day);
                    psDay.addBatch();
                }
                psSup.executeBatch();
                psDay.executeBatch();
            }

            int prodCursor = 0;

            for (int supId = 1; supId <= 5; supId++) {
                String date = "2025-01-" + String.format("%02d", supId);
                int agreementId = insertStandardAgreementIfNotExists(conn, supId, date);

                insertQuantityAgreementIfNotExists(conn, agreementId, prodCursor + 1, 10.0 + supId, 1000 + agreementId, 5, 2);
                insertSupplierAgreementIfNotExists(conn, supId, agreementId);

                prodCursor++;
            }


            int agreementId2 = insertStandardAgreementIfNotExists(conn, 2, "2025-02-02");
            insertQuantityAgreementIfNotExists(conn, agreementId2, 1, 9, 1000 + agreementId2, 5, 5);
            insertSupplierAgreementIfNotExists(conn, 2, agreementId2);

            for (int supId = 1; supId <= 5; supId++) {
                int count = (supId == 5) ? 3 : 1;
                for (int k = 0; k < count; k++) {
                    String date = "2025-02-" + String.format("%02d", prodCursor + 1);
                    int periodAgreementId = insertPeriodAgreementIfNotExists(conn, supId, date, "City" + supId, "050111111" + supId);

                    insertQuantityAgreementIfNotExists(conn, periodAgreementId, prodCursor + 1, 8.0 + supId + k, 2000 + periodAgreementId, 7, 3);
                    insertPeriodAgreementItemIfNotExists(conn, periodAgreementId, prodCursor + 1, 20 + k * 5);
                    insertSupplierAgreementIfNotExists(conn, supId, periodAgreementId);

                    prodCursor++;
                }
            }

            insertOrderWithItemsIfNotExists(conn,
                    1, "Supplier1", 1, "City1", "2025-06-10", "0501111111", "shipped",
                    new int[]{1, 2}, new int[]{10, 5}, new double[]{110, 55}, new double[]{100, 50});

            insertOrderWithItemsIfNotExists(conn,
                    2, "Supplier2", 2, "City2", "2025-06-11", "0501111112", "shipped",
                    new int[]{2}, new int[]{7}, new double[]{77}, new double[]{70});

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }


    private static int insertStandardAgreementIfNotExists(Connection conn, int supplierNumber, String date) throws SQLException {
        try (PreparedStatement psCheck = conn.prepareStatement("""
                SELECT IDNumber FROM standardAgreements WHERE supplierNumber = ? AND date = ? AND type = 'standard';
            """)) {
            psCheck.setInt(1, supplierNumber);
            psCheck.setString(2, date);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("IDNumber");
                }
            }
        }

        try (PreparedStatement psInsert = conn.prepareStatement("""
                INSERT INTO standardAgreements(supplierNumber, date, type) VALUES (?, ?, 'standard');
            """, PreparedStatement.RETURN_GENERATED_KEYS)) {
            psInsert.setInt(1, supplierNumber);
            psInsert.setString(2, date);
            psInsert.executeUpdate();
            try (ResultSet rs = psInsert.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Failed to generate standard agreement ID");
                }
            }
        }
    }

    private static int insertPeriodAgreementIfNotExists(Connection conn, int supplierNumber, String date, String address, String contactPhone) throws SQLException {
        try (PreparedStatement psCheck = conn.prepareStatement("""
                SELECT IDNumber FROM standardAgreements WHERE supplierNumber = ? AND date = ? AND type = 'period';
            """)) {
            psCheck.setInt(1, supplierNumber);
            psCheck.setString(2, date);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    int existingId = rs.getInt("IDNumber");
                    try (PreparedStatement psCheckDet = conn.prepareStatement("""
                            SELECT IDNumber FROM periodAgreements WHERE IDNumber = ?;
                        """)) {
                        psCheckDet.setInt(1, existingId);
                        try (ResultSet rsDet = psCheckDet.executeQuery()) {
                            if (rsDet.next()) {
                                return existingId;
                            }
                        }
                    }
                }
            }
        }

        try (PreparedStatement psInsert = conn.prepareStatement("""
                INSERT INTO standardAgreements(supplierNumber, date, type) VALUES (?, ?, 'period');
            """, PreparedStatement.RETURN_GENERATED_KEYS)) {
            psInsert.setInt(1, supplierNumber);
            psInsert.setString(2, date);
            psInsert.executeUpdate();
            try (ResultSet rs = psInsert.getGeneratedKeys()) {
                if (rs.next()) {
                    int newId = rs.getInt(1);

                    try (PreparedStatement psDet = conn.prepareStatement("""
                            INSERT INTO periodAgreements(IDNumber, address, contactPhone) VALUES (?, ?, ?);
                        """)) {
                        psDet.setInt(1, newId);
                        psDet.setString(2, address);
                        psDet.setString(3, contactPhone);
                        psDet.executeUpdate();
                    }

                    return newId;
                } else {
                    throw new SQLException("Failed to generate period agreement ID");
                }
            }
        }
    }

    private static void insertQuantityAgreementIfNotExists(Connection conn, int agreementId, int prodId, double price, int catalogNumber, int amountToDiscount, int discount) throws SQLException {
        try (PreparedStatement psCheck = conn.prepareStatement("""
                SELECT 1 FROM quantityAgreements WHERE IDNumber = ? AND prodId = ?;
            """)) {
            psCheck.setInt(1, agreementId);
            psCheck.setInt(2, prodId);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    return;
                }
            }
        }

        try (PreparedStatement psInsert = conn.prepareStatement("""
                INSERT INTO quantityAgreements(IDNumber, prodId, price, catalogNumber, amountToDiscount, discount)
                VALUES (?, ?, ?, ?, ?, ?);
            """)) {
            psInsert.setInt(1, agreementId);
            psInsert.setInt(2, prodId);
            psInsert.setDouble(3, price);
            psInsert.setInt(4, catalogNumber);
            psInsert.setInt(5, amountToDiscount);
            psInsert.setInt(6, discount);
            psInsert.executeUpdate();
        }
    }

    private static void insertSupplierAgreementIfNotExists(Connection conn, int supplierNumber, int agreementId) throws SQLException {
        try (PreparedStatement psCheck = conn.prepareStatement("""
                SELECT 1 FROM Supplier_Agreements WHERE supplierNumber = ? AND agreementId = ?;
            """)) {
            psCheck.setInt(1, supplierNumber);
            psCheck.setInt(2, agreementId);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) return; // קיים
            }
        }

        try (PreparedStatement psInsert = conn.prepareStatement("""
                INSERT INTO Supplier_Agreements(supplierNumber, agreementId) VALUES (?, ?);
            """)) {
            psInsert.setInt(1, supplierNumber);
            psInsert.setInt(2, agreementId);
            psInsert.executeUpdate();
        }
    }

    private static void insertPeriodAgreementItemIfNotExists(Connection conn, int agreementId, int prodId, int amount) throws SQLException {
        try (PreparedStatement psCheck = conn.prepareStatement("""
                SELECT 1 FROM periodAgreementItems WHERE IDNumber = ? AND prodId = ?;
            """)) {
            psCheck.setInt(1, agreementId);
            psCheck.setInt(2, prodId);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) return;
            }
        }

        try (PreparedStatement psInsert = conn.prepareStatement("""
                INSERT INTO periodAgreementItems(IDNumber, prodId, amountToOrder) VALUES (?, ?, ?);
            """)) {
            psInsert.setInt(1, agreementId);
            psInsert.setInt(2, prodId);
            psInsert.setInt(3, amount);
            psInsert.executeUpdate();
        }
    }

    private static void insertOrderWithItemsIfNotExists(Connection conn, int numAgreement, String supplierName, int supplierNumber,
                                                        String address, String date, String contactPhone, String statusOrder,
                                                        int[] prodIds, int[] amounts, double[] prices, double[] discounts) throws SQLException {
        // קודם בודקים אם כבר קיימת הזמנה עם אותו numAgreement (או לפי צורך שדה אחר)
        try (PreparedStatement psCheck = conn.prepareStatement("""
            SELECT 1 FROM orders WHERE numAgreement = ?;
        """)) {
            psCheck.setInt(1, numAgreement);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) return;
            }
        }

        int generatedOrderId;

        try (PreparedStatement psInsertOrder = conn.prepareStatement("""
            INSERT INTO orders(numAgreement, supplierName, supplierNumber, address, date, contactPhone, statusOrder)
            VALUES (?, ?, ?, ?, ?, ?, ?);
        """, Statement.RETURN_GENERATED_KEYS)) {

            psInsertOrder.setInt(1, numAgreement);
            psInsertOrder.setString(2, supplierName);
            psInsertOrder.setInt(3, supplierNumber);
            psInsertOrder.setString(4, address);
            psInsertOrder.setString(5, date);
            psInsertOrder.setString(6, contactPhone);
            psInsertOrder.setString(7, statusOrder);

            psInsertOrder.executeUpdate();

            try (ResultSet rs = psInsertOrder.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedOrderId = rs.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve generated orderNumber.");
                }
            }
        }

        try (PreparedStatement psInsertItem = conn.prepareStatement("""
            INSERT INTO itemOrders (IDNumber,prodId, numOrder, amountOrder, finalPrice, initialPrice) VALUES (?, ?, ?, ?, ?, ?);
        """)) {
            for (int i = 0; i < prodIds.length; i++) {
                psInsertItem.setInt(1, numAgreement);
                psInsertItem.setInt(2, prodIds[i]);
                psInsertItem.setInt(3, generatedOrderId);
                psInsertItem.setInt(4, amounts[i]);
                psInsertItem.setDouble(5, prices[i]);
                psInsertItem.setDouble(6, discounts[i]);
                psInsertItem.addBatch();
            }
            psInsertItem.executeBatch();
        }
    }

}


