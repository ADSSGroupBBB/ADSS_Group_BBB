package presentation;

import Service.OrderApplication;

import java.sql.SQLException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderManager {
    public void OrderManagerPresentation() {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        boolean flag = true;
        while (flag) {
            while (true) {
                System.out.println("Welcome Order Manager, What would you like to do?");
                System.out.println("Choose option 1-4");
                System.out.println("1.Add an order");
                System.out.println("2.Cancel an order");
                System.out.println("3.Search an order");
                System.out.println("4.Back to main menu");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice >= 1 && choice <= 4) {
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
                    addOrder();
                    break;
                }
                case 2: {
                    cancalOrder();
                    break;
                }
                case 3: {
                    searchOrder();
                    break;
                }
                case 4: {
                    flag = false;
                    break;
                }
            }
            if (!flag) {
                return;
            }
        }
    }

    public void addOrder() {
        OrderApplication oa = new OrderApplication();
        Scanner scanner = new Scanner(System.in);
        int numSupplier;
        int numAgreement;
        String address;
        String date;
        String contactPhone;
        int amount;
        int a = 0;
        while (true) {
            System.out.println("Would you like to order manually or use an automatic order?");
            System.out.println("1. manual order");
            System.out.println("2. automatic order");
            if (scanner.hasNextInt()) {
                a = scanner.nextInt();
                scanner.nextLine();
                if (a >= 1 && a <= 2) {
                    break;
                } else {
                    System.out.println("The number is invalid, please select again");
                    scanner.nextLine();
                }
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
        try {

            if (a == 1) {
            while (true) {
                System.out.println("Enter the number of the supplier you would like to order from");
                if (scanner.hasNextInt()) {
                    numSupplier = scanner.nextInt();
                    scanner.nextLine();
                    SupplierManager sm = new SupplierManager();
                    if (sm.supplierExist(numSupplier)) {
                        break;
                    } else {
                        System.out.println("The supplier not exists in the system");
                    }
                } else {
                    System.out.println("This is not a number, please enter it again");
                    scanner.nextLine();
                }
            }

            while (true) {
                System.out.println("Enter the destination address");
                address = scanner.nextLine().trim();
                if (address.isEmpty()) {
                    System.out.println("The address cannot be empty, please enter again");
                } else {
                    break;
                }
            }
            while (true) {
                System.out.println("Enter the booking date");
                date = scanner.nextLine().trim();
                LocalDate today = LocalDate.now();
                LocalDate tomorrow = today.plusDays(1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String strTomorrow = tomorrow.format(formatter);
                String strToday = today.format(formatter);
                if (date.isEmpty()) {
                    System.out.println("The date cannot be empty, please enter again");
                } else if (date.equals(strToday)) {
                    System.out.println("The supplier won't be able to prepare your order on such short notice,");
                    System.out.println("please choose the date " + strTomorrow + " and forward");
                } else {
                    break;
                }
            }
            while (true) {
                System.out.println("Enter contact phone number");
                contactPhone = scanner.nextLine().trim();
                if (contactPhone.isEmpty()) {
                    System.out.println("The contact phone number cannot be empty, please enter again");
                } else {
                    break;
                }
            }

            while (true) {
                System.out.println("Enter the agreement number from which you would like to place an order");
                if (scanner.hasNextInt()) {
                    numAgreement = scanner.nextInt();
                    scanner.nextLine();
                    AgreementsManager am = new AgreementsManager();

                    if (am.regularAgreeExist(numSupplier, numAgreement)) {
                        break;
                    } else {
                        System.out.println("The Agreement not exists in the system");
                    }
                } else {
                    System.out.println("This is not a number, please enter it again");
                    scanner.nextLine();
                }
            }
                int orderNumber = oa.addOrder(numAgreement, numSupplier, address, date, contactPhone);


                int numP = 0;
                LinkedList<Integer> numProducts = new LinkedList<Integer>();
                while (true) {
                    while (true) {
                        System.out.println("Select a product to order from the following products:");
                        System.out.println(oa.printByAgree(numAgreement));
                        if (scanner.hasNextInt()) {
                            numP = scanner.nextInt();
                            scanner.nextLine();
                            if (numProducts.contains(numP)) {
                                System.out.println("The product is already available on order.");
                                continue;
                            }
                            if (oa.numProAgreement(numAgreement) >= numP) { //Exceeds the range of options
                                numProducts.add(numP);
                                break;
                            } else {
                                System.out.println("This is not a valid number.");
                            }
                        } else {
                            System.out.println("This is not a number, please enter it again");
                            scanner.nextLine();
                        }
                    }
                    while (true) {
                        System.out.println("Enter the quantity of this item");
                        if (scanner.hasNextInt()) {
                            amount = scanner.nextInt();
                            scanner.nextLine();
                            break;
                        } else {
                            System.out.println("This is not a number, please enter it again");
                            scanner.nextLine();
                        }
                    }
                    if (!oa.addItem(orderNumber, numP, amount)) {
                        System.out.println("The product is already available on order.");
                        continue;
                    }
                    int secondC = 0;
                    while (true) {
                        System.out.println("Do you want to add another product?");
                        System.out.println("1.Yes");
                        System.out.println("2.No");
                        if (scanner.hasNextInt()) {
                            secondC = scanner.nextInt();
                            scanner.nextLine();
                            if (secondC >= 1 && secondC <= 2) {
                                break;
                            } else {
                                System.out.println("The number is invalid, please select again");
                            }
                        } else {
                            System.out.println("The number is invalid, please select again");
                            scanner.nextLine();
                        }
                    }
                    if (secondC == 2) {
                        return;
                    }
                }

        }

        else {
            int choiceType;
            while (true) {
                System.out.println("What type of automatic order would you like to place?");
                System.out.println("1. Periodic Order");
                System.out.println("2. Order For Missing Products");
                if (scanner.hasNextInt()) {
                    choiceType = scanner.nextInt();
                    scanner.nextLine();
                    if (choiceType >= 1 && choiceType <= 2) {
                        break;
                    } else {
                        System.out.println("The number is invalid, please select again");
                        scanner.nextLine();
                    }
                } else {
                    System.out.println("This is not a number, please enter it again");
                    scanner.nextLine();
                }
            }
            if (choiceType == 1) {
                try {
                    String orders = oa.createPeriodOrder();
                    System.out.println(orders);
                } catch (SQLException e) {
                    System.out.println("failed to create automatic order");
                    return;
                }

            } else {
                try {
                    String orders = oa.createMissOrder();
                    System.out.println(orders);
                } catch (SQLException e) {
                    System.out.println("failed to create automatic order");
                    return;
                }
                //try {
                //int orderID = oa.addOrder(numAgreement, numSupplier, address, date, contactPhone);
                //if (orderID != -1) {
                //  System.out.println("automatic order created successfully!");
                //} else {
                //  System.out.println("failed to create automatic order");
                //}

            }
        }
        } catch (SQLException e) {
            System.out.println("The order was unsuccessful");
        }
    }

    public void cancalOrder() {
        OrderApplication oa = new OrderApplication();
        Scanner scanner = new Scanner(System.in);
        int orderNumber;
        while (true) {
            System.out.println("Enter the order number");
            if (scanner.hasNextInt()) {
                orderNumber = scanner.nextInt();
                scanner.nextLine();
                try {
                    if (!oa.orderExist(orderNumber)) {
                        break;
                    } else {
                        System.out.println("The order already exists in the system");
                    }
                } catch (SQLException e) {
                    System.out.println("The cancel failed");
                    return;
                }
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
        try {
            oa.cancelStatusOrder(orderNumber);
        } catch (SQLException e) {
            System.out.println("The cancel failed");
            return;
        }
    }

    public void searchOrder() {
        OrderApplication oa = new OrderApplication();
        Scanner scanner = new Scanner(System.in);
        int orderNumber;
        int choiceType;
        while (true) {
            System.out.println("Enter the order number");
            if (scanner.hasNextInt()) {
                orderNumber = scanner.nextInt();
                scanner.nextLine();
                try {
                    if (oa.orderExist(orderNumber)) {
                        break;
                    } else {
                        System.out.println("The order not exists in the system");
                    }
                } catch (SQLException e) {
                    System.out.println("The search failed");
                }
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
        while (true) {
            System.out.println("What would you like to do to the order?");
            System.out.println("1. print");
            System.out.println("2. update status");
            if (scanner.hasNextInt()) {
                choiceType = scanner.nextInt();
                scanner.nextLine();
                if (choiceType >= 1 && choiceType <= 2) {
                    break;
                } else {
                    System.out.println("The number is invalid, please select again");
                    scanner.nextLine();
                }
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
        try {
            if (choiceType == 1) {
                System.out.println(oa.printOrder(orderNumber));
            } else {
                oa.arriveStatusOrder(orderNumber);
            }
        } catch (SQLException e) {
            System.out.println("The operation failed");
        }
    }
}

