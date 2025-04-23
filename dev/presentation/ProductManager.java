package presentation;



import java.util.Scanner;

public class ProductManager {
    public void ProductManagerPresentation(){
        Scanner scanner= new Scanner(System.in);
        int choice=0;
        while (true) {
            System.out.println("Welcome Product Manager, What would you like to do?");
            System.out.println("Choose option 1-3");
            System.out.println("1.Add a Product");
            System.out.println("2.Edit Product");
            System.out.println("3.Back to main menu");
            if(scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice >= 1 && choice <= 3) {
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
                addProduct();
                break;
            }
            case 2:{
                editProduct();
            }
            case 3:{
            }



        }
    }
    public void addProduct(){
        Scanner scanner= new Scanner(System.in);
        String productName;
         int productNumber;
         String unitOfMeasure;
         String manufacturer;
        while (true) {
            System.out.println("Enter product name");
            productName = scanner.nextLine().trim();
            scanner.nextLine();
            if (productName.isEmpty()) {
                System.out.println("The name cannot be empty, please enter again");
            } else {
                break;
            }
        }
        while (true) {
            System.out.println("Enter product number");
            if (scanner.hasNextInt()) {
                productNumber = scanner.nextInt();
                scanner.nextLine();
                break;
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
        int choice=0;
        while (true) {
            System.out.println("Select the product's units of measure:");
            System.out.println("1. kg");
            System.out.println("2. g");
            System.out.println("3. ml");
            System.out.println("4. liter");
            if(scanner.hasNextInt()){
                choice=scanner.nextInt();
                scanner.nextLine();
                if(choice>=1&&choice<=4){
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
        if(choice==1){
            unitOfMeasure="kg";
        }
        else if(choice==2){
            unitOfMeasure="g";
        }
        else if(choice==3){
            unitOfMeasure="ml";

        }
        else {
            unitOfMeasure="liter";
        }
        while (true) {
            System.out.println("Enter the product's manufacturer ");
            manufacturer = scanner.nextLine().trim();
            scanner.nextLine();
            if (manufacturer.isEmpty()) {
                System.out.println("The product's manufacturer cannot be empty, please enter again");
            } else {
                break;
            }
        }

    }
    public void editProduct(){
        Scanner scanner= new Scanner(System.in);
        String productName;
        int productNumber;
        String unitOfMeasure;
        String manufacturer;

        while (true) {
            System.out.println("Enter the product number you would like to edit");
            if (scanner.hasNextInt()) {
                productNumber = scanner.nextInt();
                scanner.nextLine();
                break;
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
        //בדיקה שקיים בכלל
        int choice=0;
        while (true) {
            System.out.println("What would you like to edit?");
            System.out.println("1. Product name");
            System.out.println("2. Item units");
            System.out.println("3. Item manufacturer name");
            if(scanner.hasNextInt()){
                choice=scanner.nextInt();
                scanner.nextLine();
                if(choice>=1&&choice<=3){
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
        if(choice==1){
            while (true) {
                System.out.println("Enter the new name");
                String newName = scanner.nextLine().trim();
                scanner.nextLine();
                if (newName.isEmpty()) {
                    System.out.println("The name cannot be empty, please enter again");
                } else {
                    break;
                }
            }
        }
        else if(choice==2){
            int unitNum;
            while (true) {
                System.out.println("Select the product's units of measure:");
                System.out.println("1. kg");
                System.out.println("2. g");
                System.out.println("3. ml");
                System.out.println("4. liter");
                if(scanner.hasNextInt()){
                    unitNum=scanner.nextInt();
                    scanner.nextLine();
                    if(unitNum>=1&&unitNum<=4){
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
            if(unitNum==1){
                unitOfMeasure="kg";
            }
            else if(unitNum==2){
                unitOfMeasure="g";
            }
            else if(unitNum==3){
                unitOfMeasure="ml";

            }
            else {
                unitOfMeasure="liter";
            }
        }
        else if(choice==3){
            while (true) {
                System.out.println("Enter the manufacturer's name");
                String newName = scanner.nextLine().trim();
                scanner.nextLine();
                if (newName.isEmpty()) {
                    System.out.println("The name cannot be empty, please enter again");
                } else {
                    break;
                }
            }
        }
    }
}


