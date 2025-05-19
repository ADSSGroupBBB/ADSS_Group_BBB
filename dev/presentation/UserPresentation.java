package presentation;


import Domain.AgreementsController;
import Domain.OrderController;
import Domain.ProductController;
import Domain.SupplierController;

import java.util.LinkedList;
import java.util.Scanner;

public class UserPresentation {
    public void UserPresentationInit(){
        Scanner scanner= new Scanner(System.in);
        int choice=0;
        int secondC=0;
        boolean flag=true;
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
            SupplierController s = new SupplierController();
            ProductController p = new ProductController();
            AgreementsController a = new AgreementsController();
            OrderController o = new OrderController();
            LinkedList<String> contactNames1 = new LinkedList<>();
            contactNames1.add("John Doe");
            LinkedList<String> deliveryDays1 = new LinkedList<>();
            deliveryDays1.add("Monday");
            s.addNewSupplier(1,"sup1","bank","Cash",contactNames1,"0505050505",deliveryDays1,"constant");
            LinkedList<String> contactNames2 = new LinkedList<>();
            contactNames1.add("dov");
            LinkedList<String> deliveryDays2 = new LinkedList<>();
            s.addNewSupplier(2,"sup2","bank","credit",contactNames2,"0505050508",deliveryDays2,"selfCollection");
            p.addNewProduct("p1",1,"g","man");
            p.addNewProduct("p2",2,"kg","man");
            p.addNewProduct("p3",3,"g","man");
            p.addNewProduct("p4",4,"kg","ma");
            a.addNewAgreement(1,"01-01-2025");
            a.addNewAgreement(1,"02-02-2025");
            a.addProToAgreement(1,1,20.5,1,10,10);
            a.addProToAgreement(2,2,11,2,8,8);
            a.addNewAgreement(2,"03-03-2025");
            a.addProToAgreement(3,3,12,3,5,5);
            a.addProToAgreement(3,4,10,4,6,5);
            o.addNewOrder(1,2,"Beer","01-03-2025","tom","arrived");
            o.addItemOrder(1,3,1,7);
            o.addItemOrder(1,3,2,8);
        }
        while (flag){
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
                flag=false;
                return;
            }
        }

        }
    }
}
