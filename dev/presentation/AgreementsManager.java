package presentation;


import Service.AgreementsApplication;
import Service.ProductApplication;

import java.sql.SQLException;
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

                    flag=false;
                    break;
                }
            }
            if(!flag){
                return;
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
                System.out.println("The payment date cannot be empty, please enter again");
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
    public String enterAdress(){
        String address;
        Scanner scanner=new Scanner(System.in);
        while (true) {
            System.out.println("Enter the destination address");
            address = scanner.nextLine().trim();
            if (address.isEmpty()) {
                System.out.println("The address cannot be empty, please enter again");
            } else {
                break;
            }
        }
        return address;
    }
    public String enterContactPhone(){
        String contactPhone;
        Scanner scanner=new Scanner(System.in);
        while (true) {
            System.out.println("Enter contact phone number");
            contactPhone = scanner.nextLine().trim();
            if (contactPhone.isEmpty()) {
                System.out.println("The contact phone number cannot be empty, please enter again");
            } else {
                break;
            }
        }
        return contactPhone;
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
    public int enterAmountToOrder() {
        Scanner scanner = new Scanner(System.in);
        int amountToOrder;
        while (true) {
            System.out.println("Enter the fixed quantity you would like to order of the product");
            if (scanner.hasNextInt()) {
                amountToOrder = scanner.nextInt();
                scanner.nextLine();
                return amountToOrder;
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
    }

    public void edit2(AgreementsApplication aa,int numSup,int numAgree,int typeAgreement) {
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
                            try {
                                while (true) {
                                System.out.println("Enter the product number you want to delete");
                                if (scanner.hasNextInt()) {
                                    productNumber = scanner.nextInt();
                                    scanner.nextLine();
                                    if(typeAgreement==0||typeAgreement==1) {
                                        if (productExistStandardAgre(productNumber, numAgree)) {
                                            break;
                                        }
                                    }
                                    else {
                                        if (productExistPeriodAgre(productNumber, numAgree)) {
                                            break;
                                        }
                                    }
                                        System.out.println("The product not exists in this agreement");
                                        scanner.nextLine();
                                } else {
                                    System.out.println("This is not a number, please enter it again");
                                    scanner.nextLine();
                                }
                            }
                                if (typeAgreement == 0) {
                                    aa.deleteProFromStandardAgree(numSup, numAgree, productNumber);
                                } else {
                                    aa.deleteProFromPeriodAgree(numSup, numAgree, productNumber);
                                }
                            }
                         catch (SQLException e) {
                            System.out.println("The deleted failed");
                            return;
                        }
                            return;
                        }
                        case 2: {
                            ProductManager pm = new ProductManager();
                            int numPro = pm.addProduct();
                            try {

                                if (aa.existPRegular(numSup, numPro) || aa.existPPeriod(numSup, numPro)) {
                                    System.out.println("The product already exists in the agreement");
                                } else {
                                    double price = enterPrice();
                                    int catalogNumber = enterCatalogNumber();
                                    int amountToDiscount = enterAmountToDiscount();
                                    int discount = enterDiscount();
                                    if (typeAgreement == 0 || typeAgreement == 1) {
                                        aa.addProductToStandardAgreement(numAgree, numPro, price, catalogNumber, amountToDiscount, discount);
                                    } else {
                                        int amountToOrder = enterAmountToOrder();
                                        aa.addProductToPeriodAgreement(numAgree, numPro, price, catalogNumber, amountToDiscount, discount, amountToOrder);
                                    }
                                }
                            }
                         catch (SQLException e) {
                             System.out.println("The add failed");
                            return;
                        }
                            return;
                        }
                        case 3: {
                            int productNumber;
                            while (true) {
                                System.out.println("Enter the product number whose details you want to edit");
                                if (scanner.hasNextInt()) {
                                    productNumber = scanner.nextInt();
                                    scanner.nextLine();
                                    try {
                                        if(typeAgreement==0||typeAgreement==1) {
                                            if (productExistStandardAgre(productNumber, numAgree)) {
                                                break;
                                            }
                                        }
                                        else {
                                            if (productExistPeriodAgre(productNumber, numAgree)) {
                                                break;
                                            }
                                        }
                                    }
                                    catch (SQLException e) {
                                        System.out.println("The edit failed");
                                        return;
                                    }

                                    System.out.println("The product not exists in this agreement");
                                    scanner.nextLine();
                                } else {
                                    System.out.println("This is not a number, please enter it again");
                                    scanner.nextLine();
                                }
                            }
                            if(typeAgreement!=2) {
                                editProductStandard(scanner,aa,numAgree,productNumber);
                            }
                            else {
                                editProductPeriod(scanner,aa,numAgree,productNumber);
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
    public void editProductStandard(Scanner scanner,AgreementsApplication aa,int numAgree,int productNumber){
        int choiceS=0;
        double price;
        int catalogNumber;
        int amountToDiscount;
        int discount;
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
                            try {
                                catalogNumber = enterCatalogNumber();
                                aa.setCatalogNumber(productNumber, numAgree, catalogNumber);
                                return;
                            }
                            catch (SQLException e) {
                                System.out.println("The edit failed");
                                return;
                            }

                        }
                        case 2: {
                            try {
                                price = enterPrice();
                                aa.setPrice(productNumber, numAgree, price);
                                return;
                            }
                            catch (SQLException e) {
                                System.out.println("The edit failed");
                                return;
                            }

                        }
                        case 3: {
                            try {
                                amountToDiscount = enterAmountToDiscount();
                            aa.setAmountToDiscount(productNumber, numAgree, amountToDiscount);
                            return;
                        }
                            catch (SQLException e) {
                                System.out.println("The edit failed");
                                return;
                            }
                        }
                        case 4: {
                            try {
                                discount = enterDiscount();
                            aa.setDiscount(productNumber, numAgree, discount);
                            return;
                        }
                            catch (SQLException e) {
                                System.out.println("The edit failed");
                                return;
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


        public void editProductPeriod(Scanner scanner,AgreementsApplication aa,int numAgree,int productNumber){
            int choiceS=0;
            double price;
            int catalogNumber;
            int amountToDiscount;
            int discount;
        while (true) {
            System.out.println("What detail would you like to edit?");
            System.out.println("1. Product catalog number");
            System.out.println("2. Product price");
            System.out.println("3. Quantity from which discount will be applied");
            System.out.println("4. Discount to be applied for the given quantity");
            System.out.println("5. quantity you would like to order of the product");
            if (scanner.hasNextInt()) {
                choiceS = scanner.nextInt();
                scanner.nextLine();
                if ((choiceS >= 1 && choiceS <= 5)) {
                    switch (choiceS) {
                        case 1: {
                            try {
                                catalogNumber = enterCatalogNumber();
                                aa.setCatalogNumber(productNumber, numAgree, catalogNumber);
                                return;
                            }
                            catch (SQLException e) {
                                System.out.println("The edit failed");
                                return;
                            }

                        }
                        case 2: {
                            try {
                                price = enterPrice();
                                aa.setPrice(productNumber, numAgree, price);
                                return;
                            }
                            catch (SQLException e) {
                                System.out.println("The edit failed");
                                return;
                            }

                        }
                        case 3: {
                            try {
                                amountToDiscount = enterAmountToDiscount();
                                aa.setAmountToDiscount(productNumber, numAgree, amountToDiscount);
                                return;
                            }
                            catch (SQLException e) {
                                System.out.println("The edit failed");
                                return;
                            }

                        }
                        case 4: {
                            try {
                                discount = enterDiscount();
                                aa.setDiscount(productNumber, numAgree, discount);
                                return;
                            }
                            catch (SQLException e) {
                                System.out.println("The edit failed");
                                return;
                            }


                        }
                        case 5: {
                            try {
                                int amount = enterAmountToOrder();
                                aa.setPeriodAmountToOrder(productNumber, numAgree, amount);
                                return;
                            }
                            catch (SQLException e) {
                                System.out.println("The edit failed");
                                return;
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



    public void addAgreement() {
        AgreementsApplication aa = new AgreementsApplication();
        Scanner scanner = new Scanner(System.in);
        int supplierNumber;
        try {
            while (true) {
                System.out.println("Enter the supplier number you want to add an agreement to");
                if (scanner.hasNextInt()) {
                    supplierNumber = scanner.nextInt();
                    scanner.nextLine();
                    SupplierManager sm = new SupplierManager();
                    if (sm.supplierExist(supplierNumber)) {
                        break;
                    } else {
                        System.out.println("The supplier not exists in the system");
                    }
                } else {
                    System.out.println("This is not a number, please enter it again");
                    scanner.nextLine();
                }
            }
            addAgreementBySup(supplierNumber);
        }
        catch (SQLException e) {
            System.out.println("Add agreement failed");
            return;
        }
    }
    public void addAgreementBySup(int supplierNumber){
        try {

            Scanner scanner = new Scanner(System.in);
        AgreementsApplication aa = new AgreementsApplication();
        SupplierManager sm = new SupplierManager();
        String date;
        double price;
        int catalogNumber;
        int amountToDiscount;
        int discount;
        int typeChoice = constantSupplier(supplierNumber,scanner);

        date=enterDate();
        int id;
        try {
            if (typeChoice!=2) {
                id = aa.addStandardAgreement(supplierNumber, date);
            }
            else {
                String address=enterAdress();
                String contactPhone=enterContactPhone();
                id= aa.addPeriodAgreement(supplierNumber, date,address,contactPhone);
            }
        }
        catch (SQLException e) {
            System.out.println("The add failed");
            return;
        }


        int choice=0;

        ProductManager pm=new ProductManager();
            while (true) {
                int numPro = pm.addProduct();
                if (aa.existPRegular(supplierNumber, numPro) || aa.existPPeriod(supplierNumber, numPro)) {
                    System.out.println("The product already exists in the agreement");
                    continue;
                } else {
                    price = enterPrice();
                    catalogNumber = enterCatalogNumber();
                    amountToDiscount = enterAmountToDiscount();
                    discount = enterDiscount();
                    if (typeChoice == 0 || typeChoice == 1) {
                        aa.addProductToStandardAgreement(id, numPro, price, catalogNumber, amountToDiscount, discount);
                    } else {
                        int amountToOrder = enterAmountToOrder();
                        aa.addProductToPeriodAgreement(id, numPro, price, catalogNumber, amountToDiscount, discount, amountToOrder);
                    }

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
            }
        }
            catch (SQLException e) {
                System.out.println("Add agreement failed");
                return;
            }


    }
    public void editAgreement() {
        try {

            AgreementsApplication aa = new AgreementsApplication();
            Scanner scanner = new Scanner(System.in);
            int supplierNumber;
            while (true) {
                System.out.println("Enter the supplier number of the agreement you would like to edit");
                if (scanner.hasNextInt()) {
                    supplierNumber = scanner.nextInt();
                    scanner.nextLine();
                    SupplierManager sm = new SupplierManager();
                    if (sm.supplierExist(supplierNumber)) {
                        break;
                    } else {
                        System.out.println("The supplier not exists in the system");
                    }
                } else {
                    System.out.println("This is not a number, please enter it again");
                    scanner.nextLine();
                }
            }
            editAgreementBySup(supplierNumber);
        } catch (SQLException e) {
            System.out.println("Edit agreement failed");
            return;
        }
    }
    public void editAgreementBySup(int supplierNumber){
        try {


            AgreementsApplication aa = new AgreementsApplication();
            Scanner scanner = new Scanner(System.in);
            int numAgreement;
            String date;
            int typeChoice = constantSupplier(supplierNumber, scanner);
            while (true) {
                System.out.println("Enter the agreement number you would like to edit");
                if (scanner.hasNextInt()) {
                    numAgreement = scanner.nextInt();
                    scanner.nextLine();
                    if (typeChoice == 0 || typeChoice == 1) {
                        if (regularAgreeExist(supplierNumber, numAgreement)) {
                            break;
                        } else {
                            System.out.println("The standard agreement not exists in the system");
                        }
                    } else {
                        if (!PeriodAgreeExist(supplierNumber, numAgreement)) {
                            System.out.println("The periodic agreement not exists in the system");
                            return;
                        }
                        if (!aa.periodAgreementCanEdit(supplierNumber)) {
                            System.out.println("The agreement cannot be edited on the same day");
                            return;
                        }
                        break;
                    }
                } else {
                    System.out.println("This is not a number, please enter it again");
                    scanner.nextLine();
                }
            }
            int choice = 0;
            if (typeChoice != 2) {
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
                                    aa.setDate(supplierNumber, numAgreement, date);
                                    return;

                                }
                                case 2: {
                                    edit2(aa, supplierNumber, numAgreement, typeChoice);
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
            } else {
                while (true) {
                    System.out.println("What would you like to edit?");
                    System.out.println("1. The date");
                    System.out.println("2. The address");
                    System.out.println("3. The contactPhone");
                    System.out.println("4. Product List");

                    if (scanner.hasNextInt()) {
                        choice = scanner.nextInt();
                        scanner.nextLine();
                        if (choice >= 1 && choice <= 4) {
                            switch (choice) {
                                case 1: {
                                    date = enterDate();
                                    aa.setDate(supplierNumber, numAgreement, date);
                                    return;

                                }
                                case 2: {
                                    String address = enterAdress();
                                    aa.setPeriodaddress(numAgreement, address);
                                    return;
                                }
                                case 3: {
                                    String ContactPhone = enterContactPhone();
                                    aa.setPeriodContactPhone(numAgreement, ContactPhone);
                                    return;
                                }
                                case 4: {
                                    edit2(aa, supplierNumber, numAgreement, typeChoice);
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
        }
        catch (SQLException e) {
            System.out.println("The edit failed");
            return;
        }
    }

    public void cancelAgreement() {
        try {
            AgreementsApplication aa = new AgreementsApplication();
            Scanner scanner = new Scanner(System.in);
            int supplierNumber;
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
                    }
                } else {
                    System.out.println("This is not a number, please enter it again");
                    scanner.nextLine();
                }
            }
        } catch (SQLException e) {
            System.out.println("The cancel failed");
            return;
        }
    }
    public void cancelAgreementBySup(int supplierNumber){
        AgreementsApplication aa = new AgreementsApplication();
        try {
        Scanner scanner = new Scanner(System.in);
        int numAgreement;
        int typeChoice=constantSupplier(supplierNumber,scanner);
        while (true) {
            System.out.println("Enter the agreement number you would like to cancel");
            if (scanner.hasNextInt()) {
                numAgreement = scanner.nextInt();
                scanner.nextLine();
                if (typeChoice == 0||typeChoice==1) {
                    if (regularAgreeExist(supplierNumber, numAgreement)) {
                        break;
                    } else {
                        System.out.println("The standard agreement not exists in the system");
                    }
                }
                else {
                    if (PeriodAgreeExist(supplierNumber, numAgreement)) {
                        break;
                    } else {
                        System.out.println("The periodic agreement not exists in the system");
                    }
                }
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }

            if (typeChoice != 2) {
                aa.deleteStandardAgreement(supplierNumber, numAgreement);
            } else {
                aa.deletePeriodAgreement(supplierNumber, numAgreement);
            }
        }
        catch (SQLException e) {
            System.out.println("The cancel failed");
            return;
        }
    }


    public int constantSupplier(int supplierNumber,Scanner scanner ) throws SQLException{
        SupplierManager sm = new SupplierManager();
        int typeChoice=1;
        if (sm.constantSupplier(supplierNumber)) {
            while (true) {
                System.out.println("What type of agreement would you like to edit?");
                System.out.println("1.Standard Agreement");
                System.out.println("2.Periodic Agreement");
                if (scanner.hasNextInt()) {
                    typeChoice = scanner.nextInt();
                    scanner.nextLine();
                    if (typeChoice >= 1 && typeChoice <= 2) {
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
        }
        return typeChoice;
    }

    public boolean productExistStandardAgre(int numP,int numA) throws SQLException{
    AgreementsApplication aa = new AgreementsApplication();
    if (aa.existProductStandardAgre(numP,numA)) {
        return true;
    }
    return false;
}
    public boolean productExistPeriodAgre(int numP,int numA) throws SQLException{
        AgreementsApplication aa = new AgreementsApplication();
        if (aa.existProductPeriodAgre(numP,numA)) {
            return true;
        }
        return false;
    }
    public boolean regularAgreeExist(int numS,int numA) throws SQLException{
        AgreementsApplication aa = new AgreementsApplication();
        if (aa.existRegularAgree(numS,numA)) {
            return true;
        }
        return false;
    }
    public boolean PeriodAgreeExist(int numS,int numA) throws SQLException{
        AgreementsApplication aa = new AgreementsApplication();
        if (aa.existConstantAgree(numS,numA)) {
            return true;
        }
        return false;
    }

}
