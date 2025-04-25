package presentation;


import Service.AgreementsApplication;

import java.util.Scanner;

public class AgreementsManager {
    public  void AgreementsManagerPresentation() {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        boolean flag = true;
        while (flag) {
            while (true) {
                System.out.println("Welcome Agreements Manager, What would you like to do?");
                System.out.println("Choose option 1-4");
                System.out.println("1.Add a Agreement");
                System.out.println("2.Edit Agreement");
                System.out.println("3.cancel Agreement");
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
                    addAgreement();
                    break;
                }
                case 2: {
                    editAgreement();
                    break;
                }
                case 3: {
                    cancelAgreement();
                    break;
                }
                case 4: {
                    UserPresentation up=new UserPresentation();
                    up.UserPresentationInit();
                    flag=false;
                    break;
                }

            }
        }
    }
    public String enterDate(){
        Scanner scanner = new Scanner(System.in);
        String date;
        while (true) {
            System.out.println("Enter the payment date");
            date = scanner.nextLine().trim();
            if (date.isEmpty()) {
                System.out.println("The product's manufacturer cannot be empty, please enter again");
            } else {
                return date;
            }
        }
    }
    public double enterPrice() {
        Scanner scanner = new Scanner(System.in);
        double price;
        while (true) {
            System.out.println("Enter the price of the product");
            if (scanner.hasNextDouble()) {
                price = scanner.nextDouble();
                scanner.nextLine();
                return price;
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
    }
    public int enterCatalogNumber() {
        Scanner scanner = new Scanner(System.in);
        int catalogNumber;
        while (true) {
            System.out.println("Enter the product catalog number");
            if (scanner.hasNextInt()) {
                catalogNumber = scanner.nextInt();
                scanner.nextLine();
                return catalogNumber;
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
    }
    public int enterAmountToDiscount() {
        Scanner scanner = new Scanner(System.in);
        int amountToDiscount;
        while (true) {
            System.out.println("Enter the quantity from which the discount will be applied");
            if (scanner.hasNextInt()) {
                amountToDiscount = scanner.nextInt();
                scanner.nextLine();
                return amountToDiscount;
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
    }
    public int enterDiscount() {
        Scanner scanner = new Scanner(System.in);
        int discount;
        while (true) {
            System.out.println("Enter the discount to be applied to the selected quantity");
            if (scanner.hasNextInt()) {
                discount = scanner.nextInt();
                scanner.nextLine();
                return discount;
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
    }

    public void edit2(AgreementsApplication aa,int numSup,int numAgree) {
        Scanner scanner = new Scanner(System.in);
        int choice=0;
        while (true) {
            System.out.println("What would you like to do?");
            System.out.println("1. Delete a product");
            System.out.println("2. Add a new product");
            System.out.println("3. Edit existing product details");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice >= 1 && choice <= 3) {
                    switch (choice) {
                        case 1: {
                            int productNumber;
                            while (true) {
                                System.out.println("Enter the product number you want to delete");
                                if (scanner.hasNextInt()) {
                                    productNumber = scanner.nextInt();
                                    scanner.nextLine();
                                    if (productExistAgre(productNumber,numSup,numAgree)) {
                                        break;
                                    } else {
                                        System.out.println("The product not exists in this agreement");
                                        scanner.nextLine();
                                    }
                                } else {
                                    System.out.println("This is not a number, please enter it again");
                                    scanner.nextLine();
                                }
                            }
                            aa.deleteProFromAgree(numSup,numAgree,productNumber);
                            return;
                        }
                        case 2: {
                            ProductManager pm = new ProductManager();
                            int numPro = pm.addProduct();
                            if (numPro != -1) {
                                double price = enterPrice();
                                int catalogNumber = enterCatalogNumber();
                                int amountToDiscount = enterAmountToDiscount();
                                int discount = enterDiscount();
                                aa.addProductToAgreement(numAgree, numPro, price, catalogNumber, amountToDiscount, discount);
                                return;
                            }
                            return;
                        }
                        case 3: {
                            double price;
                            int catalogNumber;
                            int amountToDiscount;
                            int discount;
                            int productNumber;
                            while (true) {
                                System.out.println("Enter the product number whose details you want to edit");
                                if (scanner.hasNextInt()) {
                                    productNumber = scanner.nextInt();
                                    scanner.nextLine();
                                    if (productExistAgre(productNumber, numSup, numAgree)) {
                                        break;
                                    } else {
                                        System.out.println("The product not exists in this agreement\"");
                                        //לחזור להתחלה
                                    }
                                } else {
                                    System.out.println("This is not a number, please enter it again");
                                    scanner.nextLine();
                                }
                            }
                            int choiceS = 0;
                            while (true) {
                                System.out.println("What detail would you like to edit?");
                                System.out.println("1. Product catalog number");
                                System.out.println("2. Product price");
                                System.out.println("3. Quantity from which discount will be applied");
                                System.out.println("4. Discount to be applied for the given quantity");
                                if (scanner.hasNextInt()) {
                                    choiceS = scanner.nextInt();
                                    scanner.nextLine();
                                    if ((choiceS >= 1 && choiceS <= 4)) {
                                        switch (choiceS) {
                                            case 1: {
                                                catalogNumber = enterCatalogNumber();
                                                aa.setCatalogNumber(productNumber, numAgree, catalogNumber);
                                                return;
                                            }
                                            case 2: {
                                                price = enterPrice();
                                                aa.setPrice(productNumber, numAgree, price);
                                                return;
                                            }
                                            case 3: {
                                                amountToDiscount = enterAmountToDiscount();
                                                aa.setAmountToDiscount(productNumber, numAgree, amountToDiscount);
                                                return;
                                            }
                                            case 4: {
                                                discount = enterDiscount();
                                                aa.setDiscount(productNumber, numAgree, discount);//לשנות להוספה. מחיקה
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
                }
            } else {
                System.out.println("The number is invalid, please select again");
                scanner.nextLine();
            }
        }
    }



    public void addAgreement() {
        AgreementsApplication aa = new AgreementsApplication();
        Scanner scanner = new Scanner(System.in);
        int supplierNumber;
        String date;
        double price;
        int catalogNumber;
        int amountToDiscount;
        int discount;
        int choice = 0;
        while (true) {
            System.out.println("Enter the supplier number you want to add an agreement to");
            if (scanner.hasNextInt()) {
                supplierNumber = scanner.nextInt();
                scanner.nextLine();
                SupplierManager am = new SupplierManager();
                if (am.supplierExist(supplierNumber)) {
                    break;
                } else {
                    System.out.println("The supplier not exists in the system");
                    //לצאת חזרה
                }
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
        date=enterDate();
        int id= aa.addAgreement(supplierNumber,date);

        //לבדוק שהוא קיים

        ProductManager pm=new ProductManager();
        while (true) {
            int numPro= pm.addProduct();
            if (numPro != -1) {
                price=enterPrice();
                catalogNumber=enterCatalogNumber();
                amountToDiscount=enterAmountToDiscount();
                discount=enterDiscount();
                aa.addProductToAgreement(id,numPro, price,catalogNumber,amountToDiscount,discount);

                while (true) {
                    System.out.println("Would you like to add another product?");
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
                    break;
                }

            }
            //לחזור לראשי
        }

    }
    public void editAgreement(){
        AgreementsApplication aa = new AgreementsApplication();
        Scanner scanner = new Scanner(System.in);
        int supplierNumber;
        int numAgreement;
        String date;
        while (true) {
            System.out.println("Enter the supplier number of the agreement you would like to edit");
            if (scanner.hasNextInt()) {
                supplierNumber = scanner.nextInt();
                scanner.nextLine();
                SupplierManager am = new SupplierManager();
                if (am.supplierExist(supplierNumber)) {
                    break;
                } else {
                    System.out.println("The supplier not exists in the system");
                    //לצאת חזרה
                }
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
        while (true) {
            System.out.println("Enter the agreement number you would like to edit");
            if (scanner.hasNextInt()) {
                numAgreement = scanner.nextInt();
                scanner.nextLine();
                if (agreeExist(supplierNumber,numAgreement)) {
                    break;
                } else {
                    System.out.println("The Agreement not exists in the system");
                    //לצאת חזרה
                }
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
        int choice = 0;
        while (true) {
            System.out.println("What would you like to edit?");
            System.out.println("1. The date");
            System.out.println("2. Product List");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice >= 1 && choice <= 2) {
                    switch (choice) {
                        case 1: {
                            date = enterDate();
                            aa.setDate(supplierNumber,numAgreement,date);
                            return;
                        }
                        case 2: {
                            edit2(aa,supplierNumber,numAgreement);
                            return;
                        }
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

    public void cancelAgreement(){
        AgreementsApplication aa = new AgreementsApplication();
        Scanner scanner = new Scanner(System.in);
        int supplierNumber;
        int numAgreement;
        String date;
        while (true) {
            System.out.println("Enter the supplier number of the agreement you would like to edit");
            if (scanner.hasNextInt()) {
                supplierNumber = scanner.nextInt();
                scanner.nextLine();
                SupplierManager am = new SupplierManager();
                if (am.supplierExist(supplierNumber)) {
                    break;
                } else {
                    System.out.println("The supplier not exists in the system");
                    //לצאת חזרה
                }
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
        while (true) {
            System.out.println("Enter the agreement number you would like to cancel");
            if (scanner.hasNextInt()) {
                numAgreement = scanner.nextInt();
                scanner.nextLine();
                if (agreeExist(supplierNumber,numAgreement)) {
                    break;
                } else {
                    System.out.println("The Agreement not exists in the system");
                    //לצאת חזרה
                }
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
        aa.deleteAgreement(supplierNumber,numAgreement);
    }

    public boolean productExistAgre(int numP,int numS,int numA){
    AgreementsApplication aa = new AgreementsApplication();
    if (aa.existProductAgre(numP,numA)) {
        return true;
    }
    return false;
}
    public boolean agreeExist(int numS,int numA){
        AgreementsApplication aa = new AgreementsApplication();
        if (aa.existAgree(numS,numA)) {
            return true;
        }
        return false;
    }

}
