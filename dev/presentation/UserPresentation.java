package presentation;


//import Domain.*;
import Service.AgreementsApplication;
import Service.OrderApplication;
import Service.ProductApplication;
import Service.SupplierApplication;

import java.util.*;

public class UserPresentation {
    OrderApplication oa =new OrderApplication();
    public void UserPresentationInit(){
        Scanner scanner= new Scanner(System.in);
        int choice=0;
        int choose =0;
        int secondC=0;
        boolean flag=true;
        boolean det = true;
        while (true) {
            System.out.println("Would you like to log in with existing data?");
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
        if (secondC == 1) {
            SupplierApplication s = new SupplierApplication();
            ProductApplication p =new ProductApplication();
            AgreementsApplication a = new AgreementsApplication();
            OrderApplication o = new OrderApplication();
            LinkedList<String> contactNames1 = new LinkedList<>();
            contactNames1.add("John Doe");
            LinkedList<String> deliveryDays1 = new LinkedList<>();
            deliveryDays1.add("Monday");
            s.addSup(1,"sup1","bank","Cash",contactNames1,"0505050505",deliveryDays1,"constant");
            LinkedList<String> contactNames2 = new LinkedList<>();
            contactNames1.add("dov");
            LinkedList<String> deliveryDays2 = new LinkedList<>();
            s.addSup(2,"sup2","bank","credit",contactNames2,"0505050508",deliveryDays2,"selfCollection");
            p.addPro("p1",1,"g","man");
            p.addPro("p2",2,"kg","man");
            p.addPro("p3",3,"g","man");
            p.addPro("p4",4,"kg","ma");
            a.addAgreement(1,"01-01-2025");
            a.addAgreement(1,"02-02-2025");
            a.addProductToAgreement(1,1,20.5,1,10,10);
            a.addProductToAgreement(2,2,11,2,8,8);
            a.addAgreement(2,"03-03-2025");
            a.addProductToAgreement(3,3,12,3,5,5);
            a.addProductToAgreement(3,4,10,4,6,5);
            o.addOrder(1,2,"Beer","01-03-2025","tom","arrived");
            o.addItem(1,3,1,7);
            o.addItem(1,3,2,8);
        }
        while (true){
            System.out.println("Welcome, what would you like to do?");
            System.out.println("1. Sell");
            System.out.println("2. Edit System");
            if (scanner.hasNextInt()) {
                choose = scanner.nextInt();
                scanner.nextLine();
                if (choose >= 1 && choose <= 2) {
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


        if (choose ==2) {
            while (flag) {
                while (true) {
                    System.out.println("Welcome, what kind of manager are you?");
                    System.out.println("Choose option 1-5");
                    System.out.println("1.Supplier Manager");
                    System.out.println("2.Product Manager");
                    System.out.println("3.Order Manager");
                    System.out.println("4.Agreements Manager");
                    System.out.println("5.Log out of the system");
                    if (scanner.hasNextInt()) {
                        choice = scanner.nextInt();
                        scanner.nextLine();
                        if (choice >= 1 && choice <= 5) {
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
                        System.out.println("Exiting the system");
                        flag = false;
                        return;
                    }
                }

            }
        }else {
            int num = 0;
            Map<Integer,Integer> soldPro= new HashMap<>();
            while (true) {
                int proAmount=0;
                int proId=0;
                System.out.println("which item are you selling?");
                System.out.println("Please enter Product Number");
                if (scanner.hasNextInt()) {
                    proId = scanner.nextInt();
                    scanner.nextLine();
                } else {
                    System.out.println("The number is invalid, please select again.");
                    scanner.nextLine();
                }
                System.out.println("how many items of this product are you selling?");
                if (scanner.hasNextInt()) {
                    proAmount = scanner.nextInt();
                    scanner.nextLine();
                } else {
                    System.out.println("The number is invalid, please select again.");
                    scanner.nextLine();
                }
                System.out.println("Are there any more items?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                if (scanner.hasNextInt()) {
                    num = scanner.nextInt();
                    scanner.nextLine();
                    if (num == 2) {
                        break;
                    } else if (num == 1) {
                        soldPro.put(proId,proAmount);
                        continue;
                    } else {
                        System.out.println("The number is invalid, please select again");
                    }
                } else {
                    System.out.println("The number is invalid, please select again");
                    scanner.nextLine();
                }
            }
            oa.automatic_order(soldPro);
        }
    }
}
