package Presentation;

import Domain.Employee;
import Service.EmployeeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Screen for managing employees
 */
public class EmployeeManagementScreen {
    private final EmployeeService employeeService;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter;

    /**
     * Constructor - receives employee service as dependency
     * @param employeeService The employee service
     */
    public EmployeeManagementScreen(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    /**
     * Display main employee management screen
     */
    public void display() {
        String[] options = {
                "Add New Employee",
                "View All Employees",
                "Search Employee by ID",
                "Update Employee",
                "Remove Employee"
        };

        int choice;
        do {
            choice = displayMenu("Employee Management", options);

            switch (choice) {
                case 1:
                    addNewEmployee();
                    break;
                case 2:
                    displayAllEmployees();
                    break;
                case 3:
                    findEmployeeById();
                    break;
                case 4:
                    updateEmployee();
                    break;
                case 5:
                    removeEmployee();
                    break;
                case 0:
                    // Return to previous menu
                    break;
            }
        } while (choice != 0);
    }

    /**
     * Add a new employee to the system
     */
    private void addNewEmployee() {
        displayTitle("Add New Employee");

        String id = getInput("Enter ID");

        // Check if employee already exists
        if (employeeService.getEmployee(id) != null) {
            displayError("Employee with this ID already exists");
            return;
        }
        String firstName = getInput("Enter First Name");
        String lastName = getInput("Enter Last Name");
        String bankAccount = getInput("Enter Bank Account Number");

        LocalDate startDate = null;
        while (startDate == null) {
            try {
                String dateStr = getInput("Enter Start Date (DD/MM/YYYY)");
                startDate = LocalDate.parse(dateStr, dateFormatter);
            } catch (DateTimeParseException e) {
                displayError("Invalid date format. Please use DD/MM/YYYY");
            }
        }

        double salary = 0;
        boolean validSalary = false;
        while (!validSalary) {
            try {
                String salaryStr = getInput("Enter Hourly Salary");
                salary = Double.parseDouble(salaryStr);
                validSalary = true;
            } catch (NumberFormatException e) {
                displayError("Invalid salary. Please enter a number");
            }
        }

        // Create new employee
        Employee newEmployee = new Employee(id, firstName, lastName, bankAccount, startDate, salary);

        // Add employee to the system
        if (employeeService.addEmployee(newEmployee)) {
            displayMessage("Employee added successfully!");
        } else {
            displayError("Error adding employee");
        }
    }

    /**
     * Display all employees in the system
     */
    private void displayAllEmployees() {
        displayTitle("All Employees");

        List<Employee> employees = employeeService.getAllEmployees();

        if (employees.isEmpty()) {
            displayMessage("No employees in the system");
            return;
        }

        for (Employee employee : employees) {
            displayMessage(String.format("%s: %s %s (ID: %s, Start Date: %s)",
                    employee.getId(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getId(),
                    employee.getStartDate().format(dateFormatter)));
        }
    }

    /**
     * Find employee by ID
     */
    private void findEmployeeById() {
        displayTitle("Search Employee");

        String id = getInput("Enter ID to search");

        Employee employee = employeeService.getEmployee(id);

        if (employee == null) {
            displayError("No employee found with ID " + id);
            return;
        }

        displayEmployeeDetails(employee);
    }

    /**
     * Display detailed information about an employee
     * @param employee The employee to display
     */
    private void displayEmployeeDetails(Employee employee) {
        displayTitle("Employee Details: " + employee.getFullName());

        displayMessage("ID: " + employee.getId());
        displayMessage("Full Name: " + employee.getFullName());
        displayMessage("Bank Account: " + employee.getBankAccount());
        displayMessage("Start Date: " + employee.getStartDate().format(dateFormatter));
        displayMessage("Hourly Salary: " + employee.getSalary());

        // Display qualified positions
        if (employee.getQualifiedPositions().isEmpty()) {
            displayMessage("Qualified Positions: None");
        } else {
            displayMessage("Qualified Positions:");
            employee.getQualifiedPositions().forEach(position ->
                    displayMessage("- " + position.getName())
            );
        }
    }

    /**
     * Update employee information
     */
    private void updateEmployee() { // לשים לב
        displayTitle("Update Employee");

        String id = getInput("Enter ID of employee to update");

        Employee employee = employeeService.getEmployee(id);

        if (employee == null) {
            displayError("No employee found with ID " + id);
            return;
        }

        displayEmployeeDetails(employee);

        String[] options = {
                "Update First Name",
                "Update Last Name",
                "Update Bank Account",
                "Update Salary"
        };

        int choice = displayMenu("Select field to update", options);

        switch (choice) {
            case 1:
                String firstName = getInput("Enter new first name");
                employee.setFirstName(firstName);
                displayMessage("First name updated successfully");
                break;
            case 2:
                String lastName = getInput("Enter new last name");
                employee.setLastName(lastName);
                displayMessage("Last name updated successfully");
                break;
            case 3:
                String bankAccount = getInput("Enter new bank account");
                employee.setBankAccount(bankAccount);
                displayMessage("Bank account updated successfully");
                break;
            case 4:
                try {
                    double salary = Double.parseDouble(getInput("Enter new hourly salary"));
                    employee.setSalary(salary);
                    displayMessage("Salary updated successfully");
                } catch (NumberFormatException e) {
                    displayError("Invalid salary. Update canceled");
                }
                break;
            case 0:
                // Return to menu
                break;
        }
    }

    /**
     * Remove an employee from the system
     */
    private void removeEmployee() {
        displayTitle("Remove Employee");

        String id = getInput("Enter ID of employee to remove");

        Employee employee = employeeService.getEmployee(id);

        if (employee == null) {
            displayError("No employee found with ID " + id);
            return;
        }

        displayEmployeeDetails(employee);

        if (getBooleanInput("Are you sure you want to remove this employee?")) {
            Employee removedEmployee = employeeService.removeEmployee(id);

            if (removedEmployee != null) {
                displayMessage("Employee successfully removed from the system");
            } else {
                displayError("Cannot remove the employee. They may be assigned to future shifts");
            }
        }
    }

    /**
     * Display a menu and get user choice
     * @param title Menu title
     * @param options Menu options
     * @return User's choice (0 for back)
     */
    private int displayMenu(String title, String[] options) {
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
     * Get boolean input from user (yes/no)
     * @param prompt Input prompt
     * @return true if user entered "yes", false otherwise
     */
    private boolean getBooleanInput(String prompt) {
        System.out.print(prompt + " (yes/no): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("yes") || input.equals("y") || input.equals("כן");
    }

    /**
     * Close resources when screen is no longer needed
     */
    public void close() {
        // Note: Only the main application should close the scanner
        // to prevent premature closing
    }
}