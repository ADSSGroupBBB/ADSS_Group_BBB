package presentation;

import Service.ProductApplication;

import java.sql.SQLException;
import java.util.Scanner;

public class ProductManager {
    public void ProductManagerPresentation() {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        boolean flag=true;
        while (flag) {
            while (true) {
                System.out.println("Welcome Product Manager, What would you like to do?");
                System.out.println("Choose option 1-3");
                System.out.println("1.Add a Product");
                System.out.println("2.Edit Product");
                System.out.println("3.Back to main menu");
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
            switch (choice) {
                case 1: {
                    addProduct();
                    break;
                }
                case 2: {
                    editProduct();
                    break;
                }
                case 3: {
                    //UserPresentation up = new UserPresentation();
                    //up.UserPresentationInit();
                    flag = false;
                    break;
                }
            }
            if(!flag){
                return;
            }
        }
    }

    public String enterName() {
        Scanner scanner = new Scanner(System.in);
        String productName;
        while (true) {
            System.out.println("Enter product name");
            productName = scanner.nextLine().trim();
            if (productName.isEmpty()) {
                System.out.println("The name cannot be empty, please enter again");
            } else {
                return productName;
            }
        }
    }

    public String enterUnit() {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        while (true) {
            System.out.println("Select the product's units of measure:");
            System.out.println("1. kg");
            System.out.println("2. g");
            System.out.println("3. ml");
            System.out.println("4. liter");
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
        if (choice == 1) {
            return "kg";
        } else if (choice == 2) {
            return "g";
        } else if (choice == 3) {
            return "ml";

        } else {
            return "liter";
        }
    }

    public String enterManufacturer() {
        Scanner scanner = new Scanner(System.in);
        String manufacturer;
        while (true) {
            System.out.println("Enter the product's manufacturer ");
            manufacturer = scanner.nextLine().trim();
            if (manufacturer.isEmpty()) {
                System.out.println("The product's manufacturer cannot be empty, please enter again");
            } else {
                return manufacturer;
            }
        }
    }

    public int addProduct() {
        ProductApplication pa = new ProductApplication();
        Scanner scanner = new Scanner(System.in);
        String productName;
        int productNumber;
        String unitOfMeasure;
        String manufacturer;
        try {


            while (true) {
                System.out.println("Enter product number");
                if (scanner.hasNextInt()) {
                    productNumber = scanner.nextInt();
                    scanner.nextLine();
                    if (!productExist(productNumber)) {
                        break;
                    } else {
                        System.out.println("The product already exists in the system");
                        return productNumber;
                    }
                } else {
                    System.out.println("This is not a number, please enter it again");
                    scanner.nextLine();
                }
            }
            productName = enterName();
            unitOfMeasure = enterUnit();
            manufacturer = enterManufacturer();
            pa.addPro(productName, productNumber, unitOfMeasure, manufacturer);
            return productNumber;

    } catch (SQLException e) {
        System.out.println("Add product failed");
        return -1; //error
    }
    }

    public void editProduct() {
        ProductApplication pa = new ProductApplication();
        Scanner scanner = new Scanner(System.in);
        String productName;
        int productNumber;
        String unitOfMeasure;
        String manufacturer;
        try {

        while (true) {
            System.out.println("Enter the product number you would like to edit");
            if (scanner.hasNextInt()) {
                productNumber = scanner.nextInt();
                scanner.nextLine();
                if (productExist(productNumber)) {
                    break;
                } else {
                    System.out.println("The product not exists in the system");
                    scanner.nextLine();
                }
            } else {
                System.out.println("This is not a number, please enter it again");
                scanner.nextLine();
            }
        }
            int choice = 0;
            while (true) {
                System.out.println("What would you like to edit?");
                System.out.println("1. Product name");
                System.out.println("2. Item units");
                System.out.println("3. Item manufacturer name");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice >= 1 && choice <= 3) {
                        switch (choice) {
                            case 1: {
                                productName = enterName();
                                pa.setName(productNumber,productName);
                                break;
                            }
                            case 2: {
                                unitOfMeasure = enterUnit();
                                pa.setunitOfMeasure(productNumber,unitOfMeasure);
                                break;
                            }
                            case 3: {
                                manufacturer = enterManufacturer();
                                pa.setManufacturer(productNumber,manufacturer);
                                break;
                            }

                        }
                        int secondC=0;
                        while (true) {
                            System.out.println("Would you like to edit anything else?");
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
                    } else {
                        System.out.println("The number is invalid, please select again");
                        scanner.nextLine();
                    }
                } else {
                    System.out.println("The number is invalid, please select again");
                    scanner.nextLine();

                }
            }
        } catch (SQLException e) {
            System.out.println("Edit product failed");
            return;
        }

        }

        public boolean productExist ( int num) throws SQLException {
            ProductApplication ua = new ProductApplication();
            if (ua.existProduct(num)) {
                return true;
            }
            return false;
        }
    }



