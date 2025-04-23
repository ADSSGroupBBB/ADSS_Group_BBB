package Presentation;

import Service.EmployeeService;
import Service.EmployeeDTO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * EmployeeManagementScreen provides the user interface for managing employees in the system.
 * This screen allows HR managers to add, view, search, update, and remove employees.

 * The screen provides comprehensive employee management capabilities including:
 * - Adding new employees with all necessary details
 * - Viewing lists of all employees
 * - Searching for specific employees by ID
 * - Updating employee details including personal information and benefits
 * - Safely removing employees from the system
 */
public class EmployeeManagementScreen extends BaseScreen {
    private final EmployeeService employeeService;
    private final DateTimeFormatter dateFormatter;
    private final EmployeeDTO loggedInEmployee;

    /**
     * Constructor used for backward compatibility with the original class.
     * @param employeeService The service for accessing employee data
     */
    public EmployeeManagementScreen(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.loggedInEmployee = null; // No permissions checks in the original class
    }

    /**
     * Constructor that takes a logged-in user for permission checking.
     *
     * @param employeeService The service for accessing employee data
     * @param loggedInEmployee The currently logged-in employee
     */
    public EmployeeManagementScreen(EmployeeService employeeService, EmployeeDTO loggedInEmployee) {
        this.employeeService = employeeService;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.loggedInEmployee = loggedInEmployee;
    }

    /**
     * Displays the employee management screen if the user has appropriate permissions.
     * Only HR Managers can access this functionality.
     */
    @Override
    public void display() {
        // Check permissions if needed
        if (loggedInEmployee != null && !loggedInEmployee.isHRManager()) {
            displayError("Access denied. Only HR Managers can access this functionality.");
            return;
        }

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
     * Displays a form for adding a new employee to the system.
     * Collects all required information such as personal details, salary,
     * benefits, and employment dates.
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
        int sickDays = getIntInput("Enter number of sick days");
        int vacationDays = getIntInput("Enter number of vacation days");
        String pensionFundName = getInput("Enter pension fund name");

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

        // Add a regular employee only (without role and password parameters)
        boolean success = employeeService.addNewEmployee(
                id, firstName, lastName, bankAccount,
                startDate, salary, sickDays, vacationDays, pensionFundName
        );

        if (success) {
            displayMessage("Employee added successfully!");
        } else {
            displayError("Error adding employee");
        }
    }

    /**
     * Displays a list of all employees in the system.
     * For each employee, shows ID, name, role, and start date.
     */
    private void displayAllEmployees() {
        displayTitle("All Employees");

        List<EmployeeDTO> employees = employeeService.getAllEmployees();

        if (employees.isEmpty()) {
            displayMessage("No employees in the system");
            return;
        }

        for (EmployeeDTO employee : employees) {
            String roleStr = "";
            if (employee.isHRManager()) {
                roleStr = " (HR Manager)";
            } else if (employee.isShiftManager()) {
                roleStr = " (Shift Manager)";
            }

            displayMessage(String.format("%s: %s %s%s (Start Date: %s)",
                    employee.getId(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    roleStr,
                    employee.getStartDate().format(dateFormatter)));
        }
    }

    /**
     * Allows searching for an employee by their ID and displays their details.
     */
    private void findEmployeeById() {
        displayTitle("Search Employee");

        String id = getInput("Enter ID to search");

        EmployeeDTO employee = employeeService.getEmployeeDetails(id);

        if (employee == null) {
            displayError("No employee found with ID " + id);
            return;
        }

        displayEmployeeDetails(employee);
    }

    /**
     * Displays detailed information about a specific employee including personal
     * details, role, benefits, and qualifications.
     *
     * @param employee The employee whose details will be displayed
     */
    private void displayEmployeeDetails(EmployeeDTO employee) {
        displayTitle("Employee Details: " + employee.getFullName());
        displayMessage("ID: " + employee.getId());
        displayMessage("Full Name: " + employee.getFullName());
        displayMessage("Bank Account: " + employee.getBankAccount());
        displayMessage("Start Date: " + employee.getStartDate().format(dateFormatter));
        displayMessage("Hourly Salary: " + employee.getSalary());
        // Display role
        String roleStr = "Regular Employee";
        if (employee.isHRManager()) {
            roleStr = "HR Manager";
        } else if (employee.isShiftManager()) {
            roleStr = "Shift Manager";
        }
        displayMessage("Role: " + roleStr);
        displayMessage("Sick Days: " + employee.getSickDays());
        displayMessage("Vacation Days: " + employee.getVacationDays());
        displayMessage("Pension Fund: " + employee.getPensionFundName());
        // Display qualified positions
        if (employee.getQualifiedPositions().isEmpty()) {
            displayMessage("Qualified Positions: None");
        } else {
            displayMessage("Qualified Positions:");
            for (String position : employee.getQualifiedPositions()) {
                displayMessage("- " + position);
            }
        }
    }

    /**
     * Displays a form for updating various details of an existing employee.
     * Allows updating personal information, salary, benefits, and password.
     */
    private void updateEmployee() {
        displayTitle("Update Employee");

        String id = getInput("Enter ID of employee to update");
        EmployeeDTO employee = employeeService.getEmployeeDetails(id);
        if (employee == null) {
            displayError("No employee found with ID " + id);
            return;
        }
        displayEmployeeDetails(employee);
        String[] options = {
                "Update First Name",
                "Update Last Name",
                "Update Bank Account",
                "Update Salary",
                "Update Password",
                "Update Sick Days",
                "Update Vacation Days",
                "Update Pension Fund"
        };
        int choice = displayMenu("Select field to update", options);

        switch (choice) {
            case 1:
                String firstName = getInput("Enter new first name");
                if (employeeService.updateEmployeeFirstName(id, firstName)) {
                    displayMessage("First name updated successfully");
                } else {
                    displayError("Error updating first name");
                }
                break;
            case 2:
                String lastName = getInput("Enter new last name");
                if (employeeService.updateEmployeeLastName(id, lastName)) {
                    displayMessage("Last name updated successfully");
                } else {
                    displayError("Error updating last name");
                }
                break;
            case 3:
                String bankAccount = getInput("Enter new bank account");
                if (employeeService.updateEmployeeBankAccount(id, bankAccount)) {
                    displayMessage("Bank account updated successfully");
                } else {
                    displayError("Error updating bank account");
                }
                break;
            case 4:
                try {
                    double salary = Double.parseDouble(getInput("Enter new hourly salary"));
                    if (employeeService.updateEmployeeSalary(id, salary)) {
                        displayMessage("Salary updated successfully");
                    } else {
                        displayError("Error updating salary");
                    }
                } catch (NumberFormatException e) {
                    displayError("Invalid salary. Update canceled");
                }
                break;
            case 5:
                updateEmployeePassword(id);
                break;
            case 6:
                try {
                    int sickDays = Integer.parseInt(getInput("Enter new number of sick days"));
                    if (employeeService.updateEmployeeSickDays(id, sickDays)) {
                        displayMessage("Sick days updated successfully");
                    } else {
                        displayError("Error updating sick days");
                    }
                } catch (NumberFormatException e) {
                    displayError("Invalid number. Update canceled");
                }
                break;
            case 7:
                try {
                    int vacationDays = Integer.parseInt(getInput("Enter new number of vacation days"));
                    if (employeeService.updateEmployeeVacationDays(id, vacationDays)) {
                        displayMessage("Vacation days updated successfully");
                    } else {
                        displayError("Error updating vacation days");
                    }
                } catch (NumberFormatException e) {
                    displayError("Invalid number. Update canceled");
                }
                break;
            case 8:
                String fund = getInput("Enter new pension fund name");
                if (employeeService.updateEmployeePensionFund(id, fund)) {
                    displayMessage("Pension fund updated successfully");
                } else {
                    displayError("Error updating pension fund");
                }
                break;
            case 0:
                // Return to menu
                break;
        }
    }

    /**
     * Updates the password for a specific employee.
     * @param employeeId The ID of the employee whose password will be updated
     */
    private void updateEmployeePassword(String employeeId) {
        String password = getInput("Enter new password");
        if (password.isEmpty()) {
            displayError("Password cannot be empty for managers");
            return;
        }

        if (employeeService.updateEmployeePassword(employeeId, password)) {
            displayMessage("Password updated successfully");
        } else {
            displayError("Error updating password");
        }
    }

    /**
     * Removes an employee from the system after confirmation.
     * Checks for future shift assignments to prevent removal of employees
     * who are scheduled for future work.
     */
    private void removeEmployee() {
        displayTitle("Remove Employee");

        String id = getInput("Enter ID of employee to remove");

        EmployeeDTO employee = employeeService.getEmployeeDetails(id);

        if (employee == null) {
            displayError("No employee found with ID " + id);
            return;
        }

        displayEmployeeDetails(employee);

        if (getBooleanInput("Are you sure you want to remove this employee?")) {
            boolean success = employeeService.removeEmployee(id);

            if (success) {
                displayMessage("Employee successfully removed from the system");
            } else {
                displayError("Cannot remove the employee. They may be assigned to future shifts");
            }
        }
    }
}