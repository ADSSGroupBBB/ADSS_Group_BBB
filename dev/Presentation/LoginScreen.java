package Presentation;

import Service.EmployeeDTO;
import Service.EmployeeService;

/**
 * Screen for system login
 * Implements high cohesion by focusing only on login functionality
 */
public class LoginScreen extends BaseScreen {
    private final EmployeeService employeeService;
    private EmployeeDTO loggedInEmployee;


    public LoginScreen(EmployeeService employeeService) {
        this.employeeService = employeeService;
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


    private void login() {
        displayTitle("System Login");
        String id = getInput("Enter your ID");

        // Check if employee exists
        EmployeeDTO employee = employeeService.getEmployee(id);
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

    private boolean checkIfManager(EmployeeDTO employee) {
        // Check if employee is qualified for shift manager position
        // In DTO we have position names as strings, not Position objects
        for (String position : employee.getQualifiedPositions()) {
            // Here we need to check if any of the positions is a manager position
            // This would ideally check with the service, but for simplicity:
            if (position.toLowerCase().contains("manager")) {
                return true;
            }
        }
        return false;
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