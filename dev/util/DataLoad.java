package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                        ps.setString(1, (String)p[0]);
                        ps.setInt(2, (int)p[1]);
                        ps.setString(3, (String)p[2]);
                        ps.setString(4, (String)p[3]);
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
                        psSup.executeUpdate();

                        String day = switch (i) {
                            case 1 -> "Sunday";
                            case 2 -> "Monday";
                            case 3 -> "Tuesday";
                            case 4 -> "Wednesday";
                            default -> "Thursday";
                        };
                        psDay.setInt(1, i);
                        psDay.setString(2, day);
                        psDay.executeUpdate();
                    }
                }

                int prodCursor = 0;

                for (int supId = 1; supId <= 5; supId++) {
                    int agreementId;
                    try (PreparedStatement psStd = conn.prepareStatement("""
                        INSERT INTO standardAgreements(supplierNumber, date, type) VALUES (?, ?, 'standard');
                    """, PreparedStatement.RETURN_GENERATED_KEYS)) {

                        psStd.setInt(1, supId);
                        psStd.setString(2, "2025-01-" + String.format("%02d", supId));
                        psStd.executeUpdate();

                        try (ResultSet rs = psStd.getGeneratedKeys()) {
                            if (rs.next()) {
                                agreementId = rs.getInt(1);
                            } else {
                                throw new SQLException("Failed to generate standard agreement ID");
                            }
                        }
                    }

                    try (PreparedStatement psQty = conn.prepareStatement("""
                        INSERT INTO quantityAgreements(IDNumber, prodId, price, catalogNumber, amountToDiscount, discount)
                        VALUES (?, ?, ?, ?, ?, ?);
                    """);
                         PreparedStatement psSupAgr = conn.prepareStatement("""
                        INSERT INTO Supplier_Agreements(supplierNumber, agreementId) VALUES (?, ?);
                    """)) {

                        int pid = prodCursor + 1;
                        psQty.setInt(1, agreementId);
                        psQty.setInt(2, pid);
                        psQty.setDouble(3, 10.0 + supId);
                        psQty.setInt(4, 1000 + agreementId);
                        psQty.setInt(5, 5);
                        psQty.setInt(6, 2);
                        psQty.executeUpdate();

                        psSupAgr.setInt(1, supId);
                        psSupAgr.setInt(2, agreementId);
                        psSupAgr.executeUpdate();

                        prodCursor++;
                    }
                }
                int agreementId;
                try (PreparedStatement psStd = conn.prepareStatement("""
                        INSERT INTO standardAgreements(supplierNumber, date, type) VALUES (?, ?, 'standard');
                    """, PreparedStatement.RETURN_GENERATED_KEYS)) {

                    psStd.setInt(1, 2);
                    psStd.setString(2, "2025-01-" + String.format("%02d", 2));
                    psStd.executeUpdate();

                    try (ResultSet rs = psStd.getGeneratedKeys()) {
                        if (rs.next()) {
                            agreementId = rs.getInt(1);
                        } else {
                            throw new SQLException("Failed to generate standard agreement ID");
                        }
                    }
                }

                try (PreparedStatement psQty = conn.prepareStatement("""
                        INSERT INTO quantityAgreements(IDNumber, prodId, price, catalogNumber, amountToDiscount, discount)
                        VALUES (?, ?, ?, ?, ?, ?);
                    """);
                     PreparedStatement psSupAgr = conn.prepareStatement("""
                        INSERT INTO Supplier_Agreements(supplierNumber, agreementId) VALUES (?, ?);
                    """)) {

                    psQty.setInt(1, agreementId);
                    psQty.setInt(2, 1);
                    psQty.setDouble(3, 9);
                    psQty.setInt(4, 1000 + agreementId);
                    psQty.setInt(5, 5);
                    psQty.setInt(6, 5);
                    psQty.executeUpdate();

                    psSupAgr.setInt(1, 2);
                    psSupAgr.setInt(2, agreementId);
                    psSupAgr.executeUpdate();

                }


                for (int supId = 1; supId <= 5; supId++) {
                    int count = (supId == 5) ? 3 : 1;
                    for (int k = 0; k < count; k++) {
                        try (PreparedStatement psPer = conn.prepareStatement("""
                            INSERT INTO standardAgreements(supplierNumber, date, type) VALUES (?, ?, 'period');
                        """, PreparedStatement.RETURN_GENERATED_KEYS)) {

                            psPer.setInt(1, supId);
                            psPer.setString(2, "2025-02-" + String.format("%02d", prodCursor + 1));
                            psPer.executeUpdate();

                            try (ResultSet rs = psPer.getGeneratedKeys()) {
                                if (rs.next()) {
                                    agreementId = rs.getInt(1);
                                } else {
                                    throw new SQLException("Failed to generate period agreement ID");
                                }
                            }
                        }

                        try (PreparedStatement psDet = conn.prepareStatement("""
                            INSERT INTO periodAgreements(IDNumber, address, contactPhone) VALUES (?, ?, ?);
                        """);
                             PreparedStatement psQty = conn.prepareStatement("""
                            INSERT INTO quantityAgreements(IDNumber, prodId, price, catalogNumber, amountToDiscount, discount)
                            VALUES (?, ?, ?, ?, ?, ?);
                        """);
                             PreparedStatement psItem = conn.prepareStatement("""
                            INSERT INTO periodAgreementItems(IDNumber, prodId, amountToOrder)
                            VALUES (?, ?, ?);
                        """);
                             PreparedStatement psSupAgr = conn.prepareStatement("""
                            INSERT INTO Supplier_Agreements(supplierNumber, agreementId) VALUES (?, ?);
                        """)) {

                            psDet.setInt(1, agreementId);
                            psDet.setString(2, "City" + supId);
                            psDet.setString(3, "050111111" + supId);
                            psDet.executeUpdate();

                            int pid = prodCursor + 1;
                            psQty.setInt(1, agreementId);
                            psQty.setInt(2, pid);
                            psQty.setDouble(3, 8.0 + supId + k);
                            psQty.setInt(4, 2000 + agreementId);
                            psQty.setInt(5, 7);
                            psQty.setInt(6, 3);
                            psQty.executeUpdate();

                            psItem.setInt(1, agreementId);
                            psItem.setInt(2, pid);
                            psItem.setInt(3, 20 + k * 5);
                            psItem.executeUpdate();

                            psSupAgr.setInt(1, supId);
                            psSupAgr.setInt(2, agreementId);
                            psSupAgr.executeUpdate();

                            prodCursor++;
                        }
                    }
                }

                // 5. הזמנות
                try (PreparedStatement psOrder = conn.prepareStatement("""
                    INSERT INTO orders(numAgreement, supplierName, supplierNumber, address, date, contactPhone, statusOrder)
                    VALUES (?, ?, ?, ?, ?, ?, ?);
                """, PreparedStatement.RETURN_GENERATED_KEYS);
                     PreparedStatement psItem = conn.prepareStatement("""
                    INSERT INTO itemOrders(IDNumber, prodId, numOrder, amountOrder, finalPrice, initialPrice)
                    VALUES (?, ?, ?, ?, ?, ?);
                """)) {

                    int orderId;
                    psOrder.setInt(1, 1);
                    psOrder.setString(2, "Supplier1");
                    psOrder.setInt(3, 1);
                    psOrder.setString(4, "City1");
                    psOrder.setString(5, "2025-06-10");
                    psOrder.setString(6, "0501111111");
                    psOrder.setString(7, "shipped");
                    psOrder.executeUpdate();

                    try (ResultSet rs = psOrder.getGeneratedKeys()) {
                        if (rs.next()) {
                            orderId = rs.getInt(1);
                        } else throw new SQLException("Failed to get generated order ID");
                    }

                    psItem.setInt(1, 1);
                    psItem.setInt(2, 1);
                    psItem.setInt(3, orderId);
                    psItem.setInt(4, 10);
                    psItem.setDouble(5, 110);
                    psItem.setDouble(6, 100);
                    psItem.executeUpdate();

                    psItem.setInt(1, 1);
                    psItem.setInt(2, 2);
                    psItem.setInt(3, orderId);
                    psItem.setInt(4, 5);
                    psItem.setDouble(5, 55);
                    psItem.setDouble(6, 50);
                    psItem.executeUpdate();

                    psOrder.setInt(1, 2);
                    psOrder.setString(2, "Supplier2");
                    psOrder.setInt(3, 2);
                    psOrder.setString(4, "City2");
                    psOrder.setString(5, "2025-06-11");
                    psOrder.setString(6, "0501111112");
                    psOrder.setString(7, "shipped");
                    psOrder.executeUpdate();

                    try (ResultSet rs = psOrder.getGeneratedKeys()) {
                        if (rs.next()) {
                            orderId = rs.getInt(1);
                        } else throw new SQLException("Failed to get generated order ID");
                    }

                    psItem.setInt(1, 2);
                    psItem.setInt(1, 2);
                    psItem.setInt(3, orderId);
                    psItem.setInt(4, 7);
                    psItem.setDouble(5, 77.0);
                    psItem.setDouble(6, 70.0);
                    psItem.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
