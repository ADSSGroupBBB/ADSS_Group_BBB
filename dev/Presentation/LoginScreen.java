package Presentation;

import Domain.Employee;
import Service.EmployeeService;

import java.util.Scanner;

/**
 * Screen for system login
 * Implements high cohesion by focusing only on login functionality
 */
public class LoginScreen {
    private final EmployeeService employeeService;
    private final Scanner scanner;
    private Employee loggedInEmployee;

    /**
     * Constructor - receives employee service as dependency
     * @param employeeService The employee service
     */
    public LoginScreen(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.scanner = new Scanner(System.in);
        this.loggedInEmployee = null;
    }
    /**
     * Display the login screen
     */
    public void display() {
        displayTitle("Employee Management System - Super-Li");
        displayMessage("Welcome to the Employee Management System");

        String[] options = {
                "Login",
                "Exit"
        };
        int choice;
        do {
            choice = displayMenu("Login Menu", options);

            switch (choice) {
                case 1:
                    login();
                    break;
                case 0:
                    displayMessage("Exiting system...");
                    break;
            }
        } while (choice != 0 && loggedInEmployee == null);
    }

    /**
     * Process user login
     */
    private void login() {
        displayTitle("System Login");
        String id = getInput("Enter your ID");
        // Check if employee exists
        Employee employee = employeeService.getEmployee(id);
        if (employee == null) {
            displayError("No employee found with ID " + id);
            return;
        }
        // In a real system, we would check password here
        loggedInEmployee = employee;
        displayMessage("Welcome, " + employee.getFullName() + "!");
        // Check if employee is a manager
        boolean isManager = checkIfManager(employee);
        if (isManager) {
            displayMessage("You are logged in as a manager");
        } else {
            displayMessage("You are logged in as a regular employee");
        }
    }
    /**
     * Check if employee is a manager
     * @param employee Employee to check
     * @return True if employee is a manager
     */
    private boolean checkIfManager(Employee employee) {
        // Check if employee is qualified for shift manager position
        return employee.getQualifiedPositions().stream()
                .anyMatch(position -> position.isRequiresShiftManager());
    }
    /**
     * Get the logged in employee
     * @return The logged in employee or null if no one is logged in
     */
    public Employee getLoggedInEmployee() {
        return loggedInEmployee;
    }
    /**
     * Check if a user is logged in
     * @return True if a user is logged in
     */
    public boolean isLoggedIn() {// לבדוק?
        return loggedInEmployee != null;
    }
    /**
     * Log out the current user
     */
    public void logout() {
        if (loggedInEmployee != null) {
            displayMessage(loggedInEmployee.getFullName() + " has been logged out");
            loggedInEmployee = null;
        }
    }
    /**
     * Display a menu and get user choice
     * @param title Menu title
     * @param options Menu options
     * @return User's choice (0 for back/exit)
     */
    private int displayMenu(String title, String[] options) {
        displayTitle(title);

        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("0. Exit");

        int choice;
        do {
            choice = getIntInput("Select option");
        } while (choice < 0 || choice > options.length);

        return choice;
    }

    /**
     * Display a title with emphasis
     * @param title Title to display
     */
    private void displayTitle(String title) {
        System.out.println("\n===== " + title + " =====");
    }

    /**
     * Display a message to the user
     * @param message Message to display
     */
    private void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Display an error message to the user
     * @param error Error message
     */
    private void displayError(String error) {
        System.out.println("Error: " + error);
    }

    /**
     * Get text input from user
     * @param prompt Input prompt
     * @return Text entered by user
     */
    private String getInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    /**
     * Get integer input from user
     * @param prompt Input prompt
     * @return Integer entered by user
     */
    private int getIntInput(String prompt) {
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
     * Close resources when screen is no longer needed
     */
    public void close() {
        // Only the main application should close the scanner
    }
}