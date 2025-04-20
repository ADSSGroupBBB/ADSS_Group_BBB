package Presentation;
import java.util.Scanner;

//Base class for all screens in the application.
public abstract class BaseScreen {
    protected static final Scanner scanner = new Scanner(System.in);


    public abstract void display();

    protected int displayMenu(String title, String[] options) {
        displayTitle(title);

        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("0. Back");

        int choice;
        do {
            choice = getIntInput("Select option");
        } while (choice < 0 || choice > options.length);

        return choice;
    }


    protected void displayTitle(String title) {
        System.out.println("\n===== " + title + " =====");
    }


    protected void displayMessage(String message) {
        System.out.println(message);
    }
    protected void displayError(String error) {
        System.out.println("Error: " + error);
    }
    protected String getInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    protected int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                displayError("Please enter a valid number");
            }
        }
    }

    protected boolean getBooleanInput(String prompt) {
        System.out.print(prompt + " (yes/no): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("yes") || input.equals("y") || input.equals("כן");
    }
}