package Presentation_employee;

import Service.DriversApplication;
import DTO.EmployeeDTO;
import DTO.BranchDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Updated EmployeeManagementScreen with branch support.
 * This screen allows HR managers to add, view, search, update, and remove employees.
 */
public class EmployeeManagementScreen extends BaseScreen {
    private final NavigationManager navigationManager;
    private final DateTimeFormatter dateFormatter;
    private final EmployeeDTO loggedInEmployee;
    private static DriversApplication dra = new DriversApplication();

    /**
     * Constructor that takes the navigation manager.
     */
    public EmployeeManagementScreen(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.loggedInEmployee = navigationManager.getLoggedInEmployee();
    }

    /**
     * Displays the employee management screen if the user has appropriate permissions.
     * Only HR Managers can access this functionality.
     */
    @Override
    public void display() throws SQLException {
        // Check permissions
        if (!loggedInEmployee.isHRManager()) {
            displayError("Access denied. Only HR Managers can access this functionality.");
            return;
        }

        String[] options = {
                "Add New Employee",
                "View All Employees",
                "View Employees by Branch",
                "Search Employee by ID",
                "Update Employee",
                "Update Employee Branch",
                "Remove Employee",
                "Remove Driver"
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
                    displayEmployeesByBranch();
                    break;
                case 4:
                    findEmployeeById();
                    break;
                case 5:
                    updateEmployee();
                    break;
                case 6:
                    updateEmployeeBranch();
                    break;
                case 7:
                    removeEmployee();
                    break;
                case 8:
                    removeDriver();
                case 0:
                    // Return to previous menu
                    break;
            }
        } while (choice != 0);
    }

    /**
     * Displays a form for adding a new employee to the system with branch selection.
     */


    private void addNewEmployee() throws SQLException {
        displayTitle("Add New Employee");

        String id = getInput("Enter ID");
        // Check if employee already exists
        if (navigationManager.getEmployeeService().getEmployeeDetails(id) != null) {
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


        String branchAddress = selectBranchForEmployee();
        if (branchAddress == null) {
            displayError("Employee creation canceled - branch assignment is required");
            return;
        }

        // Check if should be a manager
        boolean isManager = getBooleanInput("Is this employee a manager?");
        boolean success;

        if (isManager) {
            String[] roles = {"SHIFT_MANAGER", "HR_MANAGER"};
            int roleChoice = displayMenu("Select Manager Role", roles);

            if (roleChoice == 0) {
                return; // User canceled
            }

            String role = roles[roleChoice - 1];
            String password = getInput("Enter password for manager");

            success = navigationManager.getEmployeeService().addManagerEmployee(
                    id, firstName, lastName, bankAccount, startDate, salary,
                    role, password, sickDays, vacationDays, pensionFundName, branchAddress
            );
        } else {
            boolean isDriver = getBooleanInput("Is this employee a driver?");
            boolean isStoreKeeper = false;
            if (!isDriver){
                isStoreKeeper = getBooleanInput("Is this employee a storekeeper?");
            }

            if (isDriver){
                // Prompt for number of licenses, ensuring a valid non-negative integer is provided
                int numLicenses = 1;
                while (true) {
                    System.out.print("Enter number of licenses: ");
                    try {
                        numLicenses = Integer.parseInt(scanner.nextLine().trim());
                        if (numLicenses < 1) throw new NumberFormatException();
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a non-negative integer.");
                    }
                }

                // Collect the list of licenses for the driver
                List<Integer> licenseList = new ArrayList<>();
                for (int i = 0; i < numLicenses; i++) {
                    while (true) {
                        System.out.print("Enter license #" + (i + 1) + ": ");
                        try {
                            int license = Integer.parseInt(scanner.nextLine().trim());
                            licenseList.add(license);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid int.");
                        }
                    }
                }

                // Call the service method to insert the new driver
                success = navigationManager.getEmployeeService().addDriver(
                        id, firstName, lastName, bankAccount, startDate, salary,
                        sickDays, vacationDays, pensionFundName, branchAddress, licenseList
                );
                if (success){
                    success = navigationManager.getPositionService()
                            .addQualificationToEmployee(id, "Driver");
                }


            } else if (isStoreKeeper) {
                success = navigationManager.getEmployeeService().addStoreKeeper(
                        id, firstName, lastName, bankAccount, startDate, salary,
                        sickDays, vacationDays, pensionFundName, branchAddress
                );
                if (success){
                    success = navigationManager.getPositionService()
                            .addQualificationToEmployee(id, "STORE_KEEPER");
                }
            }
            else {
                success = navigationManager.getEmployeeService().addEmployee(
                        id, firstName, lastName, bankAccount, startDate, salary,
                        sickDays, vacationDays, pensionFundName, branchAddress
                );
            }
        }

        if (success) {
            displayMessage("Employee added successfully! Assigned to branch: " + branchAddress);
        } else {
            displayError("Error adding employee");
        }
    }

    /**
     * Displays a list of all employees in the system.
     */
    private void displayAllEmployees() {
        displayTitle("All Employees");

        List<EmployeeDTO> employees = navigationManager.getEmployeeService().getAllEmployees();

        if (employees.isEmpty()) {
            displayMessage("No employees in the system");
            return;
        }

        for (EmployeeDTO employee : employees) {
            displayEmployeeSummary(employee);
        }
    }

    /**
     * Displays employees filtered by branch.
     */


    private void displayEmployeesByBranch() {
        displayTitle("Employees by Branch");

        List<BranchDTO> branches = navigationManager.getBranchService().getAllBranches();

        if (branches.isEmpty()) {
            displayError("No branches available in the system");
            return;
        }


        String[] options = new String[branches.size()];
        for (int i = 0; i < branches.size(); i++) {
            options[i] = branches.get(i).getAddress() + " (" + branches.get(i).getZoneName() + ")";
        }

        int choice = displayMenu("Select Branch", options);

        if (choice == 0) {
            return;
        }

        BranchDTO selectedBranch = branches.get(choice - 1);
        List<EmployeeDTO> employees = navigationManager.getEmployeeService()
                .getEmployeesByBranch(selectedBranch.getAddress());

        String title = "Employees at " + selectedBranch.getAddress();

        displayTitle(title);
        if (employees.isEmpty()) {
            displayMessage("No employees found at this branch");
        } else {
            for (EmployeeDTO employee : employees) {
                displayEmployeeSummary(employee);
            }
        }
    }



    /**
     * Updates an employee's branch assignment.
     */
    private void updateEmployeeBranch() {
        displayTitle("Update Employee Branch");

        String id = getInput("Enter ID of employee to update");
        EmployeeDTO employee = navigationManager.getEmployeeService().getEmployeeDetails(id);

        if (employee == null) {
            displayError("No employee found with ID " + id);
            return;
        }

        displayEmployeeDetails(employee);

        displayMessage("Current branch: " + (employee.hasBranch() ? employee.getBranchAddress() : "No branch assigned"));

        String newBranch = selectBranchForEmployee();

        if (getBooleanInput("Are you sure you want to update the branch assignment?")) {
            boolean success = navigationManager.getEmployeeService().updateEmployeeBranch(id, newBranch);

            if (success) {
                displayMessage("Employee branch updated successfully to: " +
                        (newBranch != null ? newBranch : "No branch"));
            } else {
                displayError("Error updating employee branch");
            }
        }
    }

    /**
     * Helper method to select a branch for employee assignment.
     */


    private String selectBranchForEmployee() {
        List<BranchDTO> branches = navigationManager.getBranchService().getAllBranches();

        if (branches.isEmpty()) {
            displayError("Cannot create employee: No branches available in the system.");
            displayError("Please ensure branches exist in the delivery module first.");
            return null;
        }


        String[] options = new String[branches.size()];
        for (int i = 0; i < branches.size(); i++) {
            BranchDTO branch = branches.get(i);
            options[i] = branch.getAddress() + " (" + branch.getZoneName() + ")";
        }

        int choice = displayMenu("Select Branch (Required)", options);

        if (choice == 0) {
            return null; // User canceled
        } else {
            return branches.get(choice - 1).getAddress();
        }
    }


    /**
     * Helper method to display employee summary with branch info.
     */


    private void displayEmployeeSummary(EmployeeDTO employee) {
        String roleStr = "";
        if (employee.isHRManager()) {
            roleStr = " (HR Manager)";
        } else if (employee.isShiftManager()) {
            roleStr = " (Shift Manager)";
        }

        String branchInfo = " - Branch: " + employee.getBranchAddress();

        displayMessage(String.format("%s: %s %s%s (Start Date: %s)%s",
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                roleStr,
                employee.getStartDate().format(dateFormatter),
                branchInfo));
    }

    /**
     * Allows searching for an employee by their ID and displays their details.
     */
    private void findEmployeeById() {
        displayTitle("Search Employee");

        String id = getInput("Enter ID to search");

        EmployeeDTO employee = navigationManager.getEmployeeService().getEmployeeDetails(id);

        if (employee == null) {
            displayError("No employee found with ID " + id);
            return;
        }

        displayEmployeeDetails(employee);
    }

    /**
     * Displays detailed information about a specific employee.
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

        // Display branch assignment
        displayMessage("Branch: " + (employee.hasBranch() ? employee.getBranchAddress() : "No branch assigned"));

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
     */
    private void updateEmployee() {
        displayTitle("Update Employee");

        String id = getInput("Enter ID of employee to update");
        EmployeeDTO employee = navigationManager.getEmployeeService().getEmployeeDetails(id);
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
                "Update Sick Days",
                "Update Vacation Days",
                "Update Pension Fund",
                "Update Branch Assignment"
        };

        int choice = displayMenu("Select field to update", options);

        switch (choice) {
            case 1:
                String firstName = getInput("Enter new first name");
                if (navigationManager.getEmployeeService().updateEmployeeFirstName(id, firstName)) {
                    displayMessage("First name updated successfully");
                } else {
                    displayError("Error updating first name");
                }
                break;
            case 2:
                String lastName = getInput("Enter new last name");
                if (navigationManager.getEmployeeService().updateEmployeeLastName(id, lastName)) {
                    displayMessage("Last name updated successfully");
                } else {
                    displayError("Error updating last name");
                }
                break;
            case 3:
                String bankAccount = getInput("Enter new bank account");
                if (navigationManager.getEmployeeService().updateEmployeeBankAccount(id, bankAccount)) {
                    displayMessage("Bank account updated successfully");
                } else {
                    displayError("Error updating bank account");
                }
                break;
            case 4:
                try {
                    double salary = Double.parseDouble(getInput("Enter new hourly salary"));
                    if (navigationManager.getEmployeeService().updateEmployeeSalary(id, salary)) {
                        displayMessage("Salary updated successfully");
                    } else {
                        displayError("Error updating salary");
                    }
                } catch (NumberFormatException e) {
                    displayError("Invalid salary. Update canceled");
                }
                break;
            case 5:
                try {
                    int sickDays = Integer.parseInt(getInput("Enter new number of sick days"));
                    if (navigationManager.getEmployeeService().updateEmployeeSickDays(id, sickDays)) {
                        displayMessage("Sick days updated successfully");
                    } else {
                        displayError("Error updating sick days");
                    }
                } catch (NumberFormatException e) {
                    displayError("Invalid number. Update canceled");
                }
                break;
            case 6:
                try {
                    int vacationDays = Integer.parseInt(getInput("Enter new number of vacation days"));
                    if (navigationManager.getEmployeeService().updateEmployeeVacationDays(id, vacationDays)) {
                        displayMessage("Vacation days updated successfully");
                    } else {
                        displayError("Error updating vacation days");
                    }
                } catch (NumberFormatException e) {
                    displayError("Invalid number. Update canceled");
                }
                break;
            case 7:
                String fund = getInput("Enter new pension fund name");
                if (navigationManager.getEmployeeService().updateEmployeePensionFund(id, fund)) {
                    displayMessage("Pension fund updated successfully");
                } else {
                    displayError("Error updating pension fund");
                }
                break;
            case 8:
                updateEmployeeBranch();
                return; // updateEmployeeBranch handles its own flow
            case 0:
                // Return to menu
                break;
        }
    }

    /**
     * Removes an employee from the system after confirmation.
     */
    private void removeEmployee() {
        displayTitle("Remove Employee");

        String id = getInput("Enter ID of employee to remove");

        EmployeeDTO employee = navigationManager.getEmployeeService().getEmployeeDetails(id);

        if (employee == null) {
            displayError("No employee found with ID " + id);
            return;
        }

        displayEmployeeDetails(employee);

        if (getBooleanInput("Are you sure you want to remove this employee?")) {
            boolean success = navigationManager.getEmployeeService().removeEmployee(id);

            if (success) {
                displayMessage("Employee successfully removed from the system");
            } else {
                displayError("Cannot remove the employee. They may be assigned to future shifts");
            }
        }
    }
    private void removeDriver() throws SQLException {
        displayTitle("Remove Driver");
        System.out.println(dra.printDrivers());
        String id = getInput("Enter ID of driver to remove");
        EmployeeDTO employee = navigationManager.getEmployeeService().getEmployeeDetails(id);

        if (employee == null) {
            displayError("No driver found with ID " + id);
            return;
        }

        displayEmployeeDetails(employee);

        if (getBooleanInput("Are you sure you want to remove this driver?")) {
            boolean success = navigationManager.getEmployeeService().removeDriver(id);

            if (success) {
                displayMessage("Driver successfully removed from the system");
            } else {
                displayError("Cannot remove the driver. They may be assigned to future shifts");
            }
        }
    }
}
