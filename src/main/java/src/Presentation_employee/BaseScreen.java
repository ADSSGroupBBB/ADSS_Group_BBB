package src.Presentation_employee;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * BaseScreen serves as the abstract base class for all screens in the application.
 * It provides common UI functionality used across all screens including menu display,
 * user input handling, and message output.
 */
public abstract class BaseScreen {
    /**
     * Scanner object for reading user input from console.
     * Shared among all screen instances.
     */
    protected static final Scanner scanner = new Scanner(System.in);

    /**
     * Displays the screen content. Must be implemented by all concrete screen classes.
     * This is the main entry point for each screen and implements the Template Method pattern.
     */
    public abstract void display() throws SQLException;

    /**
     * Displays a menu with multiple options and prompts the user to make a selection.
     * Always adds a "Back" option (0) to return to the previous screen.
     */
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

    /**
     * Displays a formatted title for a section of the screen.
     */
    protected void displayTitle(String title) {
        System.out.println("\n===== " + title + " =====");
    }

    /**
     * Displays a general message to the user.
     */
    protected void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays an error message to the user.
     * The message is prefixed with "Error: " for clear identification.
     */
    protected void displayError(String error) {
        System.out.println("Error: " + error);
    }

    /**
     * Gets string input from the user with a prompt.
     * @return The string input provided by the user
     */
    protected String getInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    /**
     * Gets integer input from the user with a prompt.
     * Continues prompting until valid integer input is provided.
     * @return The integer input provided by the user
     */
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

    /**
     * Gets boolean input from the user with a prompt.
     * Accepts "yes", "y" as affirmative responses.
     *
     * @param prompt The prompt to display to the user
     * @return true if the user answered affirmatively, false otherwise
     */
    protected boolean getBooleanInput(String prompt) {
        System.out.print(prompt + " (yes/no): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("yes") || input.equals("y") ;
    }
}