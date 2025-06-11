package presentation;


import Service.*;

import java.sql.SQLException;
import java.util.*;

public class UserPresentation {
    OrderApplication oa = new OrderApplication();

    public void UserPresentationInit() {
            Scanner scanner = new Scanner(System.in);
            int choice = 0;
            int secondC = 0;
            boolean flag = true;
        try {
            while (true) {
                System.out.println("Choose your database option:");
                System.out.println("1. Empty database");
                System.out.println("2. Load database with default data");
                if (scanner.hasNextInt()) {
                    secondC = scanner.nextInt();
                    scanner.nextLine();
                    if (secondC == 1 || secondC == 2) break;
                }
                System.out.println("Invalid input. Please select 1 or 2.");
            }

            // קביעת נתיב DB לפי הבחירה
            String dbPath;
            if (secondC == 1) {
                dbPath = "data/empty.db";
            } else {
                dbPath = "data/default_data.db";
            }

            // התחברות ל-DB
            util.DatabaseManager.connect(dbPath);

            // יצירת טבלאות אם צריך
            util.Database.createTablesIfNotExist(util.DatabaseManager.getConnection());

            // אם אופציה 2 - טען נתוני דיפולט אם טרם נטענו
            if (secondC == 2) {
                util.DataLoad.loadDefaultData(util.DatabaseManager.getConnection());
            }


            if (secondC == 1) {
                SupplierApplication s = new SupplierApplication();
                ProductApplication p = new ProductApplication();
                AgreementsApplication a = new AgreementsApplication();
                OrderApplication o = new OrderApplication();
                LinkedList<String> contactNames1 = new LinkedList<>();
                contactNames1.add("John Doe");
                LinkedList<String> deliveryDays1 = new LinkedList<>();
                deliveryDays1.add("Monday");
                s.addSup(1, "sup1", "bank", "Cash", contactNames1, "0505050505", deliveryDays1, "constant","beer","m");
                LinkedList<String> contactNames2 = new LinkedList<>();
                contactNames1.add("dov");
                LinkedList<String> deliveryDays2 = new LinkedList<>();
                s.addSup(2, "sup2", "bank", "credit", contactNames2, "0505050508", deliveryDays2, "selfCollection","beer","m");
                p.addPro("p1", 1, "g", "man");
                p.addPro("p2", 2, "kg", "man");
                p.addPro("p3", 3, "g", "man");
                p.addPro("p4", 4, "kg", "ma");
                a.addStandardAgreement(1, "01-01-2025");
                a.addStandardAgreement(1, "02-02-2025");
                a.addProductToStandardAgreement(1, 1, 20.5, 1, 10, 10);
                a.addProductToStandardAgreement(2, 2, 11, 2, 8, 8);
                a.addStandardAgreement(2, "03-03-2025");
                a.addProductToStandardAgreement(3, 3, 12, 3, 5, 5);
                a.addProductToStandardAgreement(3, 4, 10, 4, 6, 5);
                o.addOrder(1, 2, "Beer", "01-03-2025", "tom");
                o.addItem(1, 1, 7);
                o.addItem(1, 2, 8);
            }

            while (flag) {
                while (true) {
                    System.out.println("Welcome, what kind of manager are you?");
                    System.out.println("Choose option 1-6");
                    System.out.println("1.Supplier Manager");
                    System.out.println("2.Product Manager");
                    System.out.println("3.Order Manager");
                    System.out.println("4.Agreements Manager");
                    System.out.println("5.Stock Manager");
                    System.out.println("6.Log out of the system");
                    if (scanner.hasNextInt()) {
                        choice = scanner.nextInt();
                        scanner.nextLine();
                        if (choice >= 1 && choice <= 6) {
                            break;
                        } else {
                            System.out.println("The number is invalid, please select again.");
                            scanner.nextLine();

                        }
                    } else {
                        System.out.println("The number is invalid, please select again.");
                        scanner.nextLine();

                    }
                }

                switch (choice) {
                    case 1: {
                        SupplierManager sm = new SupplierManager();
                        sm.SupplierManagerPresentation();
                        break;
                    }
                    case 2: {
                        ProductManager pm = new ProductManager();
                        pm.ProductManagerPresentation();
                        break;
                    }
                    case 3: {
                        OrderManager om = new OrderManager();
                        om.OrderManagerPresentation();
                        break;
                    }
                    case 4: {
                        AgreementsManager am = new AgreementsManager();
                        am.AgreementsManagerPresentation();
                        break;
                    }
                    case 5: {
                        StockManager sm = new StockManager();
                        sm.StockManagerPresentation();
                        break;
                    }
                    case 6: {
                        System.out.println("Exiting the system");
                        flag = false;
                        return;
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("Boot failed");
        }
    }
}

