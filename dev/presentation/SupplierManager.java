package presentation;



import java.util.LinkedList;
import java.util.Scanner;

public class SupplierManager {
    public static void SupplierManagerPresentation(){
            Scanner scanner= new Scanner(System.in);
            int choice=0;
            while (true) {
                System.out.println("Welcome Supplier Manager, What would you like to do?");
                System.out.println("Choose option 1-4");
                System.out.println("1.Add a Supplier");
                System.out.println("2.Delete a Supplier");
                System.out.println("3.Edit Supplier");
                System.out.println("4.Back to main menu");
                if(scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice >= 1 && choice <= 4) {
                        break;
                    } else {
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
                    addSupplier();
                    break;
                }
                case 2:{
                    deleteSupplier();
                }
                case 3:{
                    editSupplier();
                }
                case 4:{
                }


            }
        }
        public void addSupplier(){
            Scanner scanner= new Scanner(System.in);
            int numSupplier;
            String nameSupplier;
            String bankAccount;
            String payment;
             LinkedList<String> contactNames=new LinkedList<>();
             String telephone;
             LinkedList<String> deliveryDays=new LinkedList<>();;
             String deliverySending;
             LinkedList<Agreement> agreements;

            while (true) {
                System.out.println("Enter provider number");
                if (scanner.hasNextInt()) {
                    numSupplier = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("This is not a number, please enter it again.");
                    scanner.nextLine();
                }
            }
            while (true) {
                System.out.println("Enter provider name");
                nameSupplier = scanner.nextLine().trim();
                scanner.nextLine();
                if (nameSupplier.isEmpty()) {
                    System.out.println("The name cannot be empty, please enter again.");
                } else {
                    break;
                }
            }
            while (true) {
                System.out.println("Enter bank account number");
                bankAccount = scanner.nextLine().trim();
                scanner.nextLine();
                if (bankAccount.isEmpty()) {
                    System.out.println("The bankAccount cannot be empty, please enter again.");
                } else {
                    break;
                }
            }
            int choice=0;
            while (true) {
                System.out.println("Select possible payment methods:");
                System.out.println("1. Cash");
                System.out.println("2. Credit");
                if(scanner.hasNextInt()){
                    choice=scanner.nextInt();
                    scanner.nextLine();
                    if(choice>=1&&choice<=2){
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
            if(choice==1){
                payment="Cash";
            }
            else {
                payment="Credit";
            }
            while (true) {
                System.out.println("Enter a contact name");
                String nameCon = scanner.nextLine().trim();
                scanner.nextLine();
                if (nameCon.isEmpty()) {
                    System.out.println("The contact name cannot be empty, please enter again.");
                } else {
                    contactNames.add(nameCon);
                    while (true) {
                        System.out.println("Would you like to add another contact?");
                        System.out.println("1.Yes");
                        System.out.println("2.No");
                        if(scanner.hasNextInt()){
                            choice=scanner.nextInt();
                            scanner.nextLine();
                            if(choice>=1&&choice<=2){
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
                    if(choice==2){
                        break;
                    }
                }
            }
            while (true) {
                System.out.println("Enter a phone number");
                telephone = scanner.nextLine().trim();
                scanner.nextLine();
                if (telephone.isEmpty()) {
                    System.out.println("The phone number cannot be empty, please enter again.");
                } else {
                    break;
                }
            }
            int i=0;
            while (true) {
                System.out.println("Enter delivery days");
                System.out.println("1. Sunday");
                System.out.println("2. Monday");
                System.out.println("3. Tuesday");
                System.out.println("4. Wednesday");
                System.out.println("5. Thursday");
                System.out.println("6. Friday");
                if (i==0) {
                    System.out.println("7. No fixed delivery days");
                }
                if(scanner.hasNextInt()){
                    choice=scanner.nextInt();
                    scanner.nextLine();
                    if((choice>=1&&choice<=6)||(i==0&&choice==7)) {
                        switch (choice) {
                            case 1: {
                                deliveryDays.add("Sunday");
                                break;
                            }
                            case 2: {
                                deliveryDays.add("Monday");
                                break;
                            }
                            case 3: {
                                deliveryDays.add("Tuesday");
                                break;
                            }
                            case 4: {
                                deliveryDays.add("Wednesday");
                                break;
                            }
                            case 5: {
                                deliveryDays.add("Thursday");
                                break;
                            }
                            case 6: {
                                deliveryDays.add("Friday");
                                break;
                            }
                            case 7: {
                                break;
                            }
                        }
                        int secondC;
                        while (true) {
                            System.out.println("Do you want to add an extra delivery day?");
                            System.out.println("1.Yes");
                            System.out.println("2.No");
                            if(scanner.hasNextInt()){
                                 secondC=scanner.nextInt();
                                scanner.nextLine();
                                if(secondC>=1&&secondC<=2){
                                    break;
                                }
                                else {
                                    System.out.println("The number is invalid, please select again.");
                                }
                            }
                            else {
                                System.out.println("The number is invalid, please select again.");
                                scanner.nextLine();
                            }
                        }
                        if(secondC==2){
                            break;
                        }
                        else {
                            i=1;
                    }

                    }
                    else {
                        System.out.println("The number is invalid, please select again.");

                    }
                }
                else {
                    System.out.println("The number is invalid, please select again.");
                    scanner.nextLine();
                }

            }
            while (true) {
                System.out.println("Enter delivery method:");
                System.out.println("1.constant");
                System.out.println("2.invitation");
                System.out.println("3.self collection");
                if(scanner.hasNextInt()){
                    choice=scanner.nextInt();
                    scanner.nextLine();
                    if(choice>=1&&choice<=3){
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
            if(choice==1){
                deliverySending="constant";
            }
            else if(choice==2) {
                deliverySending="invitation";
            }
            else {
                deliverySending="selfCollection";
            }
        }


                System.out.println("Do you want to add an agreement?");
                System.out.println("1.Yes");
                System.out.println("2.No");



}
