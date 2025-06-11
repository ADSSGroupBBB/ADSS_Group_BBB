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
                        contactNames TEXT NOT NULL,
                        telephone TEXT NOT NULL,
                        deliverySending TEXT NOT NULL,
                        address TEXT NOT NULL,
                        contactPhone TEXT NOT NULL
                    );
                """);
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Supplier_Agreements(
                        supplierNumber INTEGER NOT NULL,
                        agreementId INTEGER NOT NULL,
                        PRIMARY KEY (supplierNumber, agreementId),
                        FOREIGN KEY (supplierNumber) REFERENCES suppliers(supplierNumber),
                        FOREIGN KEY (agreementId) REFERENCES standardAgreements(IDNumber)
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
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS products(
                        productName TEXT NOT NULL,
                        productNumber INTEGER PRIMARY KEY,
                        unitOfMeasure TEXT NOT NULL,
                        manufacturer TEXT NOT NULL
                    );
                """);
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS standardAgreements(
                        IDNumber INTEGER PRIMARY KEY,
                        supplierNumber INTEGER ,
                        date TEXT NOT NULL,
                        type TEXT NOT NULL CHECK(type IN ('standard', 'period')),
                        FOREIGN KEY (supplierNumber) REFERENCES suppliers(supplierNumber)
                    );
                """);
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS quantityAgreements(
                        IDNumber INTEGER NOT NULL,
                        prodId INTEGER NOT NULL,
                        price Real NOT NULL,
                        catalogNumber INTEGER NOT NULL,
                        amountToDiscount INTEGER NOT NULL,
                        discount INTEGER NOT NULL,
                        PRIMARY KEY (IDNumber, prodId),
                        FOREIGN KEY (IDNumber) REFERENCES standardAgreements(IDNumber),
                        FOREIGN KEY (prodId) REFERENCES products(productNumber)
                    );
                """);
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS periodAgreements(
                        IDNumber INTEGER PRIMARY KEY,
                        address TEXT NOT NULL,
                        contactPhone TEXT NOT NULL,
                        FOREIGN KEY (IDNumber) REFERENCES standardAgreements(IDNumber)
                    );
                """);
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS periodAgreementItems(
                        IDNumber INTEGER NOT NULL,
                        prodId INTEGER NOT NULL,
                        amountToOrder INTEGER NOT NULL,
                        PRIMARY KEY (IDNumber, prodId),
                        FOREIGN KEY (IDNumber, prodId) REFERENCES quantityAgreements(IDNumber, prodId)
                    );
                """);
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS orders(
                    orderNumber INTEGER PRIMARY KEY,
                    numAgreement INTEGER NOT NULL,
                    supplierName TEXT NOT NULL,
                    supplierNumber INTEGER NOT NULL,
                    address TEXT NOT NULL,
                    date TEXT NOT NULL,
                    contactPhone TEXT NOT NULL,
                    statusOrder TEXT NOT NULL CHECK(statusOrder IN ('shipped','deleted', 'arrived'))
                    );
                """);
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS itemOrders(
                    IDNumber INTEGER NOT NULL,
                    prodId INTEGER NOT NULL,
                    numOrder INTEGER NOT NULL,
                    amountOrder INTEGER NOT NULL,
                    finalPrice Real NOT NULL,
                    initialPrice Real NOT NULL,
                    PRIMARY KEY (IDNumber, prodId,numOrder),
                    FOREIGN KEY (IDNumber, prodId) REFERENCES quantityAgreements(IDNumber, prodId),
                    FOREIGN KEY (numOrder) REFERENCES orders(orderNumber)

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

