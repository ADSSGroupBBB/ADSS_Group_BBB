package presentation;



import Service.SupplierApplication;

import java.util.LinkedList;
import java.util.Scanner;

public class SupplierManager {
    public  void SupplierManagerPresentation() {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        boolean flag = true;
        while (flag) {
            while (true) {
                System.out.println("Welcome Supplier Manager, What would you like to do?");
                System.out.println("Choose option 1-4");
                System.out.println("1.Add a Supplier");
                System.out.println("2.Delete a Supplier");
                System.out.println("3.Edit Supplier");
                System.out.println("4.Back to main menu");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice >= 1 && choice <= 4) {
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
                    addSupplier();
                    break;
                }
                case 2: {
                    deleteSupplier();
                    break;
                }
                case 3: {
                    editSupplier();
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

    private String enterNameSupplier() {
        Scanner scanner = new Scanner(System.in);
        String nameSupplier;
        while (true) {
            System.out.println("Enter supplier name");
            nameSupplier = scanner.nextLine().trim();
            if (nameSupplier.isEmpty()) {
                System.out.println("The name cannot be empty, please enter again");
            } else {
                return nameSupplier;
            }
        }
    }

    private String enterBankAccount() {
        Scanner scanner = new Scanner(System.in);
        String bankAccount;
        while (true) {
            System.out.println("Enter bank account number");
            bankAccount = scanner.nextLine().trim();
            if (bankAccount.isEmpty()) {
                System.out.println("The bankAccount cannot be empty, please enter again");
            } else {
                return bankAccount;
            }
        }
    }

    private String enterPayment() {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        while (true) {
            System.out.println("Select possible payment methods:");
            System.out.println("1. Cash");
            System.out.println("2. Credit");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice >= 1 && choice <= 2) {
                    break;
                } else {
                    System.out.println("The number is invalid, please select again");
                }
            } else {
                System.out.println("The number is invalid, please select again");
                scanner.nextLine();

            }
        }
        if (choice == 1) {
            return "Cash";
        } else {
            return "Credit";
        }
    }

    private LinkedList<String> enterContactNames() {
        LinkedList<String> contactNames = new LinkedList<>();
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        while (true) {
            System.out.println("Enter a contact name");
            String nameCon = scanner.nextLine().trim();
            if (nameCon.isEmpty()) {
                System.out.println("The contact name cannot be empty, please enter again");
            } else {
                contactNames.add(nameCon);
                while (true) {
                    System.out.println("Would you like to add another contact?");
                    System.out.println("1.Yes");
                    System.out.println("2.No");
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
                if (choice == 2) {
                    return contactNames;
                }
            }
        }
    }

    private String enterTelephone() {
        Scanner scanner = new Scanner(System.in);
        String telephone;
        while (true) {
            System.out.println("Enter a phone number");
            telephone = scanner.nextLine().trim();
            if (telephone.isEmpty()) {
                System.out.println("The phone number cannot be empty, please enter again");
            } else {
                return telephone;
            }
        }
    }

    private LinkedList<String> enterDeliveryDays() {
        Scanner scanner = new Scanner(System.in);
        LinkedList<String> deliveryDays = new LinkedList<>();
        int choice = 0;
        int i = 0;
        while (true) {
            System.out.println("Enter delivery days");
            System.out.println("1. Sunday");
            System.out.println("2. Monday");
            System.out.println("3. Tuesday");
            System.out.println("4. Wednesday");
            System.out.println("5. Thursday");
            System.out.println("6. Friday");
            if (i == 0) {
                System.out.println("7. No fixed delivery days");
            }
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                boolean fl=true;
                if ((choice >= 1 && choice <= 6) || (i == 0 && choice == 7)) {
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
                            fl=false;
                            break;

                        }
                    }
                    int secondC=2;
                    while (fl) {
                        System.out.println("Do you want to add an extra delivery day?");
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
                        return deliveryDays;
                    } else {
                        i = 1;
                    }

                } else {
                    System.out.println("The number is invalid, please select again");

                }
            } else {
                System.out.println("The number is invalid, please select again");
                scanner.nextLine();
            }

        }
    }
    private String enterDeliverySending() {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        while (true) {
            System.out.println("Enter delivery method:");
            System.out.println("1.constant");
            System.out.println("2.invitation");
            System.out.println("3.self collection");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice >= 1 && choice <= 3) {
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
        if (choice == 1) {
            return "constant";
        } else if (choice == 2) {
            return "invitation";
        } else {
            return "selfCollection";
        }
    }
    private void editContactNames(SupplierApplication ua,int numSupplier) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        while (true) {
            System.out.println("What would you like to do?");
            System.out.println("1.Add a contact");
            System.out.println("2.Delete a contact");
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
        if (choice == 1) {
            LinkedList<String> newContact = enterContactNames();
            ua.addContactNames(numSupplier, newContact);
        } else {
            if (ua.numContactName(numSupplier) <= 1) {
                System.out.println("The single contact of the supplier cannot be deleted");
            } else {
                System.out.println("Enter the name you want to delete");
                String nameCon = scanner.nextLine().trim();
                if (ua.existContactName(numSupplier, nameCon)) {
                    ua.deleteContactNames(numSupplier, nameCon);
                } else {
                    System.out.println("The name Contact not exists");
                }
            }
        }
    }

    public void editDeliveryDays(SupplierApplication ua,int numSupplier){
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        while (true) {
            System.out.println("What would you like to do?");
            System.out.println("1.Add a day");
            System.out.println("2.Delete a day");
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
        if (choice == 1) {
            LinkedList<String> days = enterDeliveryDays();
            ua.addDeliveryDays(numSupplier, days);
        } else {

                System.out.println("Enter the day you want to delete");
                String day = scanner.nextLine().trim();
                if (ua.existDays(numSupplier, day)) {
                    ua.deleteDeliveryDays(numSupplier, day);
                }
                else {
                    System.out.println("The day not exists");
                }
            }
        }

    public void addSupplier() {
        SupplierApplication sa = new SupplierApplication();
        Scanner scanner = new Scanner(System.in);
        int numSupplier;
        String nameSupplier;
        String bankAccount;
        String payment;
        LinkedList<String> contactNames = new LinkedList<>();
        String telephone;
        LinkedList<String> deliveryDays = new LinkedList<>();
        String deliverySending;

        while (true) {
            System.out.println("Enter supplier number");
            if (scanner.hasNextInt()) {
                numSupplier = scanner.nextInt();
                scanner.nextLine();
                if (!supplierExist(numSupplier)) {
                    break;
                } else {
                    System.out.println("The supplier already exists in the system");
                    return;
                }
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
        nameSupplier = enterNameSupplier();
        bankAccount = enterBankAccount();
        payment = enterPayment();
        contactNames = enterContactNames();
        telephone=enterTelephone();
        deliveryDays=enterDeliveryDays();
        deliverySending=enterDeliverySending();

        sa.addSup(numSupplier, nameSupplier, bankAccount, payment, contactNames, telephone, deliveryDays, deliverySending);
        while (true) {
            int choice=0;
            System.out.println("Do you want to add an agreement?");
            System.out.println("1.Yes");
            System.out.println("2.No");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice >= 1 && choice <= 2) {
                    if (choice == 2) {
                        break;
                    } else {
                        AgreementsManager am = new AgreementsManager();
                        am.addAgreementBySup(numSupplier);
                    }
                } else {
                    System.out.println("The number is invalid, please select again");
                    scanner.nextLine();
                }
            } else {
                System.out.println("The number is invalid, please select again");
                scanner.nextLine();
            }
        }

    }
    public void deleteSupplier(){
        SupplierApplication ua = new SupplierApplication();
        Scanner scanner = new Scanner(System.in);
        int numSupplier;
        while (true) {
            System.out.println("Enter supplier number");
            if (scanner.hasNextInt()) {
                numSupplier = scanner.nextInt();
                scanner.nextLine();
                if (supplierExist(numSupplier)) {
                    ua.deleteSupplier(numSupplier);
                    break;
                } else {
                    System.out.println("The supplier not exists in the system");
                    return;
                }
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
    }
    public void editSupplier(){
        SupplierApplication ua = new SupplierApplication();
        Scanner scanner = new Scanner(System.in);
        int numSupplier;
        String nameSupplier;
        String bankAccount;
        String payment;
        String telephone;
        String deliverySending;
        while (true) {
            System.out.println("Enter the supplier number whose details you want to edit");
            if (scanner.hasNextInt()) {
                numSupplier = scanner.nextInt();
                scanner.nextLine();
                if (supplierExist(numSupplier)) {
                    break;
                } else {
                    System.out.println("The supplier is not exists in the system");
                    return;
                }
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
        int choice=0;
        while (true) {
            System.out.println("What detail would you like to edit?");
            System.out.println("1. Supplier name");
            System.out.println("2. Bank account number");
            System.out.println("3. Payment method");
            System.out.println("4. Contact names");
            System.out.println("5. Phone");
            System.out.println("6. Delivery dates");
            System.out.println("7. Delivery method");
            System.out.println("8. Agreements");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if ((choice >= 1 && choice <= 8)) {
                    switch (choice) {
                        case 1: {
                            nameSupplier = enterNameSupplier();
                            ua.setName(numSupplier,nameSupplier);
                            return;
                        }
                        case 2: {
                            bankAccount = enterBankAccount();
                            ua.setBankAccount(numSupplier,bankAccount);
                            return;
                        }
                        case 3: {
                            payment = enterPayment();
                            ua.setPayment(numSupplier,payment);
                            return;
                        }
                        case 4: {
                            editContactNames(ua,numSupplier);
                            return;
                        }
                        case 5: {
                            telephone=enterTelephone();
                            ua.setTelephone(numSupplier,telephone);
                            return;
                        }
                        case 6: {
                            editDeliveryDays(ua,numSupplier);  //לשנות להוספה. מחיקה
                            return;
                        }
                        case 7: {
                            deliverySending=enterDeliverySending();
                            ua.setDeliverySending(numSupplier,deliverySending);
                            return;
                        }
                        case 8: {
                            int c = 0;
                            AgreementsManager am = new AgreementsManager();
                            while (true) {
                                System.out.println("What would you like to do?");
                                System.out.println("1.Add Agreement");
                                System.out.println("2. Edit Agreement");
                                System.out.println("3. Delete Agreement");
                                if (scanner.hasNextInt()) {
                                    c = scanner.nextInt();
                                    scanner.nextLine();
                                    if ((c >= 1 && c <= 3)) {
                                        switch (c) {
                                            case 1: {
                                                am.addAgreementBySup(numSupplier);
                                                return;
                                            }
                                            case 2: {
                                                am.editAgreementBySup(numSupplier);
                                                return;
                                            }
                                            case 3: {
                                                am.cancelAgreementBySup(numSupplier);
                                                return;
                                            }
                                        }
                                    } else {
                                        System.out.println("The number is invalid, please select again");

                                    }
                                } else {
                                    System.out.println("The number is invalid, please select again");
                                    scanner.nextLine();
                                }

                            }
                        }
                    }

                } else {
                    System.out.println("The number is invalid, please select again");

                }
            } else {
                System.out.println("The number is invalid, please select again");
                scanner.nextLine();
            }

        }
    }
    public boolean supplierExist(int num) {
        SupplierApplication sa = new SupplierApplication();
        if (sa.existSupplier(num)) {
            return true;
        }
        return false;
    }
    public boolean constantSupplier(int num){
        SupplierApplication sa = new SupplierApplication();
        if (sa.isConstantSupplier(num)){
            return true;
        }
        return false;
    }


}




