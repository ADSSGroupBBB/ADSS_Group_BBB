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

            String dbPath;
            if (secondC == 1) {
                dbPath = "empty.db";
            } else {
                dbPath = "default_data.db";
            }

            util.DatabaseManager.connect(dbPath);

            util.Database.createTablesIfNotExist(util.DatabaseManager.getConnection());

            if (secondC == 2) {
                util.DataLoad.loadDefaultData(util.DatabaseManager.getConnection());
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
            e.printStackTrace();

        }
    }
}

