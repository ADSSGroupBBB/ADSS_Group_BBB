package presentation;

import Domain.Product;
import Domain.QuantityAgreement;

import java.util.LinkedList;
import java.util.Scanner;

public class AgreementsManager {
    public static void AgreementsManagerPresentation(){
        Scanner scanner= new Scanner(System.in);
        int choice=0;
        while (true) {
            System.out.println("Welcome Agreements Manager, What would you like to do?");
            System.out.println("Choose option 1-3");
            System.out.println("1.Add a Agreement");
            System.out.println("2.Edit Agreement");
            System.out.println("3.cancel Agreement");
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
                addAgreement();
                break;
            }
            case 2:{
                editAgreement();
            }
            case 3:{
                cancelAgreement();
            }
            case 4:{}

        }
    }
    public void addAgreement(){
         int supplierNumber;
         int IDNumber;
         LinkedList<QuantityAgreement> productsList;
         String date;
         int numAgreement;
         Product prod;
         double price;
         int catalogNumber;
         int amountToDiscount;
          int discount;
          int choice;
        Scanner scanner= new Scanner(System.in);
        while (true) {
            System.out.println("Enter the supplier number you want to add an agreement to");
            if (scanner.hasNextInt()) {
                supplierNumber = scanner.nextInt();
                scanner.nextLine();
                break;
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
        //לבדוק שהוא קיים
        ProductManager pm=new ProductManager();
        while (true) {
            pm.addProduct();
            while (true) {
                System.out.println("Enter the price of the product");
                if (scanner.hasNextInt()) {
                    price = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("This is not a number, please enter it again");
                    scanner.nextLine();
                }
            }
            while (true) {
                System.out.println("Enter the product catalog number");
                if (scanner.hasNextInt()) {
                    catalogNumber = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("This is not a number, please enter it again");
                    scanner.nextLine();
                }
            }
            while (true) {
                System.out.println("Enter the quantity from which the discount will be applied");
                if (scanner.hasNextInt()) {
                    amountToDiscount = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("This is not a number, please enter it again");
                    scanner.nextLine();
                }
            }
            while (true) {
                System.out.println("Enter the discount to be applied to the selected quantity");
                if (scanner.hasNextInt()) {
                    discount = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("This is not a number, please enter it again");
                    scanner.nextLine();
                }
            }
            while (true) {
                System.out.println("Would you like to add another product?");
                System.out.println("1.Yes");
                System.out.println("2.No");
                if(scanner.hasNextInt()){
                    choice=scanner.nextInt();
                    scanner.nextLine();
                    if(choice>=1&&choice<=2){
                        break;
                    }
                    else {
                        System.out.println("The number is invalid, please select again");
                        scanner.nextLine();
                    }
                }
                else {
                    System.out.println("The number is invalid, please select again");
                    scanner.nextLine();
                }
            }
            if(choice==2){
                break;
            }

        }



    }

}
