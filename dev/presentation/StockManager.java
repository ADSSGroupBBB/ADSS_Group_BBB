package presentation;

import Service.StockApplication;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StockManager {
    public void StockManagerPresentation() {
        try {
            Scanner scanner = new Scanner(System.in);
            int choice = 0;
            boolean flag = true;
            while (flag) {
                while (true) {
                    System.out.println("Welcome Agreements Manager, What would you like to do?");
                    System.out.println("Choose option 1-2");
                    System.out.println("1.sell");
                    System.out.println("2.Back to main menu");
                    if (scanner.hasNextInt()) {
                        choice = scanner.nextInt();
                        scanner.nextLine();
                        if (choice >= 1 && choice <= 2) {
                            break;
                        } else {
                            System.out.println("The number is invalid, please select again");
                            scanner.nextLine();

                        }
                    } else {
                        System.out.println("The number is invalid, please select again");
                        scanner.nextLine();

                    }
                }
                switch (choice) {
                    case 1: {
                        StockApplication stockApp = new StockApplication();
                        int num = 0;
                         scanner = new Scanner(System.in);
                        Map<Integer, Integer> soldPro = new HashMap<>();
                        while (true) {
                            int proAmount = 0;
                            int proId = 0;
                            while (true) {
                                System.out.println("which item are you selling?");
                                System.out.println("Please enter Product Number");
                                if (scanner.hasNextInt()) {
                                    proId = scanner.nextInt();
                                    scanner.nextLine();
                                    ProductManager pm = new ProductManager();
                                    if (pm.productExist(proId)) {
                                        break;
                                    } else {
                                        System.out.println("The product not exists in the system");
                                    }
                                } else {
                                    System.out.println("This is not a number, please enter it again");
                                    scanner.nextLine();
                                }
                            }
                            while (true) {
                                System.out.println("how many items of this product are you selling?");
                                if (scanner.hasNextInt()) {
                                    proAmount = scanner.nextInt();
                                    scanner.nextLine();
                                    if (proAmount > 0) {
                                        if (stockApp.getAmountByPid(proId) >= proAmount) {
                                            break;
                                        } else {
                                            System.out.println(""+stockApp.getAmountByPid(proId));
                                            System.out.println("You cannot buy more than the product quantity");
                                        }
                                    } else {
                                        System.out.println("The number is invalid, please select again");
                                    }
                                } else {
                                    System.out.println("This is not a number, please enter it again");
                                    scanner.nextLine();
                                }
                            }
                            System.out.println("Are there any more items?");
                            System.out.println("1. Yes");
                            System.out.println("2. No");
                            if (scanner.hasNextInt()) {
                                num = scanner.nextInt();
                                scanner.nextLine();
                                if (num == 2) {
                                    soldPro.put(proId, proAmount);
                                    break;
                                } else if (num == 1) {
                                    soldPro.put(proId, proAmount);
                                    continue;
                                } else {
                                    System.out.println("The number is invalid, please select again");
                                }
                            } else {
                                System.out.println("The number is invalid, please select again");
                                scanner.nextLine();
                            }
                        }
                        stockApp.selling(soldPro);
                        break;
                    }
                    case 2: {

                        flag=false;
                        break;
                    }
                }
                if(!flag){
                    return;
                }
            }
        }
        catch (SQLException e) {
            System.out.println("failed to move to stock Manager");
            return;
        }
    }
}