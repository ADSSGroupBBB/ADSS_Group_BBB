package presentation;
import Service.*;

import java.util.Scanner;

public class UserPresentation {
    public void UserPresentationInit(){
        Scanner scanner= new Scanner(System.in);
        int choice=0;
        while (true) {
            System.out.println("Welcome, what kind of manager are you?");
            System.out.println("Choose option 1-5");
            System.out.println("1.Supplier Manager");
            System.out.println("2.Product Manager");
            System.out.println("3.Order Manager");
            System.out.println("4.Agreements Manager");
            System.out.println("5.Log out of the system");
            if(scanner.hasNextInt()){
                choice=scanner.nextInt();
                scanner.nextLine();
                if(choice>=1&&choice<=5){
                    break;
                }
                else {
                    System.out.println("The number is invalid, please select again.");
                    scanner.nextLine();

                }
            }
            else {
                System.out.println("The number is invalid, please select again.");
                scanner.nextLine();

            }
        }
        switch (choice) {
            case 1: {
                SupplierManager.SupplierManagerPresentation();
                break;
            }
            case 2:{
                ProductManager.ProductManagerPresentation();
            }
            case 3:{
                OrderManager.OrderManagerPresentation();
            }
            case 4:{
                AgreementsManager.AgreementsManagerPresentation();
            }
            case 5:{}


        }
    }
}
