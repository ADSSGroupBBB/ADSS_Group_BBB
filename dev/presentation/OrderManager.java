package presentation;

import Service.OrderApplication;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class OrderManager {
    public  void OrderManagerPresentation(){
        Scanner scanner= new Scanner(System.in);
        int choice=0;
        boolean flag=true;
        while (flag){
        while (true) {
            System.out.println("Welcome Order Manager, What would you like to do?");
            System.out.println("Choose option 1-4");
            System.out.println("1.Add an order");
            System.out.println("2.Cancel an order");
            System.out.println("3.Search an order");
            System.out.println("4.Back to main menu");
            if(scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice >= 1 && choice <= 4) {
                    break;
                } else {
                    System.out.println("The number is invalid, please select again");
                    scanner.nextLine();

                }
            }
            else {
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
            if(!flag){
                return;
            }
        }
    }
    public void addOrder() {
        OrderApplication oa = new OrderApplication();
        Scanner scanner = new Scanner(System.in);
        int numSupplier;
        int numAgreement;
        int orderNumber;
        String address;
        String date;
        String contactPhone;
        int amount;
        String statusOrder;
        int am =0;

        System.out.println("Would you like to order manually or use an automatic order?");
        System.out.println("1. manual order");
        System.out.println("2. automatic order");
        if (scanner.hasNextInt()) {
            am = scanner.nextInt();
            scanner.nextLine();
        } else {
            System.out.println("This is not a number, please enter it again");
            scanner.nextLine();
        }
        if (am == 1){
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
                if (date.isEmpty()) {
                    System.out.println("The date cannot be empty, please enter again");
                } else {
                    break;
                }
            }

            while (true) {
                System.out.println("Enter the order number");
                if (scanner.hasNextInt()) {
                    orderNumber = scanner.nextInt();
                    scanner.nextLine();
                    if (!oa.orderExist(orderNumber)) {
                        break;
                    } else {
                        System.out.println("The order already exists in the system");
                    }
                } else {
                    System.out.println("This is not a number, please enter it again");
                    scanner.nextLine();
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

            int numS = 0;
            while (true) {
                System.out.println("Enter order's status:");
                System.out.println("1.canceled");
                System.out.println("2.deleted");
                System.out.println("3.arrived");
                if (scanner.hasNextInt()) {
                    numS = scanner.nextInt();
                    scanner.nextLine();
                    if (numS >= 1 && numS <= 3) {
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
            if (numS == 1) {
                statusOrder="canceled";
            } else if (numS == 2) {
                statusOrder= "deleted";
            } else {
                statusOrder= "arrived";
            }
            while (true) {
                System.out.println("Enter the agreement number from which you would like to place an order");
                if (scanner.hasNextInt()) {
                    numAgreement = scanner.nextInt();
                    scanner.nextLine();
                    AgreementsManager am = new AgreementsManager();
                    if (am.agreeExist(numSupplier, numAgreement)) {
                        break;
                    } else {
                        System.out.println("The Agreement not exists in the system");
                    }
                } else {
                    System.out.println("This is not a number, please enter it again");
                    scanner.nextLine();
                }
            }
            oa.addOrder(orderNumber,numSupplier,address,date,contactPhone,statusOrder);
            int numP = 0;
            LinkedList<Integer> numProducts=new LinkedList<Integer>();
            while (true) {
                while (true) {
                    System.out.println("Select a product to order from the following products:");
                    System.out.println(oa.printByAgree(numAgreement));
                    if (scanner.hasNextInt()) {
                        numP = scanner.nextInt();
                        scanner.nextLine();
                        if(numProducts.contains(numP)){
                            System.out.println("The product is already available on order.");
                            continue;
                        }
                        if (oa.numProAgreement(numAgreement) >= numP) {
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
                if(!oa.addItem(orderNumber, numAgreement, numP, amount)){
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
        }else if (am ==2){
            List<Integer> oos=OrderApplication.getOutOfStock();

        }

    }
    public void cancalOrder(){
        OrderApplication oa = new OrderApplication();
        Scanner scanner = new Scanner(System.in);
        int orderNumber;
        while (true) {
            System.out.println("Enter the order number");
            if (scanner.hasNextInt()) {
                orderNumber = scanner.nextInt();
                scanner.nextLine();
                if (!oa.orderExist(orderNumber)) {
                    break;
                } else {
                    System.out.println("The order already exists in the system");
                }
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
        oa.deleteOrder(orderNumber);
    }
    public void searchOrder(){
        OrderApplication oa = new OrderApplication();
        Scanner scanner = new Scanner(System.in);
        int orderNumber;
        while (true) {
            System.out.println("Enter the order number");
            if (scanner.hasNextInt()) {
                orderNumber = scanner.nextInt();
                scanner.nextLine();
                if (oa.orderExist(orderNumber)) {
                    break;
                } else {
                    System.out.println("The order not exists in the system");
                }
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
        System.out.println(oa.printOrder(orderNumber));
    }

}

