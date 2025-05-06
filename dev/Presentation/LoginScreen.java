package Presentation;

import Controller.EmployeeController;
import Service.EmployeeDTO;

/**
 * Screen for system login
 * Implements high cohesion by focusing only on login functionality
 */
public class LoginScreen extends BaseScreen {
    private final EmployeeController employeeController;
    private final NavigationManager navigationManager;
    private EmployeeDTO loggedInEmployee;

    public LoginScreen(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
        this.employeeController = navigationManager.getEmployeeController();
        this.loggedInEmployee = null;
    }

    @Override
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
     * Handles the user login process.
     * Authenticates the user by verifying their ID and, for managers, their password.
     * Sets the loggedInEmployee if authentication is successful.
     * Displays appropriate welcome messages and role information upon successful login.
     */
    private void login() {
        displayTitle("System Login");
        String id = getInput("Enter your ID");

        // Check if employee exists
        EmployeeDTO employee = employeeController.getEmployee(id);
        if (employee == null) {
            displayError("No employee found with ID " + id);
            return;
        }

        // Check if the employee is a manager (requires password)
        if (employee.isManager()) {
            String password = getInput("Enter your password");
            // Verify password
            if (!employeeController.verifyEmployeeCredentials(id, password)) {
                displayError("Invalid password");
                return;
            }
        }

        loggedInEmployee = employee;
        displayMessage("Welcome, " + employee.getFullName() + "!");

        String roleMessage = "";
        if (employee.isHRManager()) {
            roleMessage = "HR Manager";
        } else if (employee.isShiftManager()) {
            roleMessage = "Shift Manager";
        } else {
            roleMessage = "Regular Employee";
        }

        displayMessage("You are logged in as: " + roleMessage);
    }

    public EmployeeDTO getLoggedInEmployee() {
        return loggedInEmployee;
    }

    public boolean isLoggedIn() {
        return loggedInEmployee != null;
    }

    public void logout() {
        if (loggedInEmployee != null) {
            displayMessage(loggedInEmployee.getFullName() + " has been logged out");
            loggedInEmployee = null;
        }
    }
}