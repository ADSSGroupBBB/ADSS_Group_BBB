//package Presentation;
//
//import Service.EmployeeService;
//import Service.EmployeeDTO;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//import java.util.List;
//
////Screen for managing employees
//public class EmployeeManagementScreen extends BaseScreen {
//    private final EmployeeService employeeService;
//    private final DateTimeFormatter dateFormatter;
//
//    //Constructor - receives employee service as dependency
//    public EmployeeManagementScreen(EmployeeService employeeService) {
//        this.employeeService = employeeService;
//        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//    }
//
//    @Override
//    public void display() {
//        String[] options = {
//                "Add New Employee",
//                "View All Employees",
//                "Search Employee by ID",
//                "Update Employee",
//                "Remove Employee"
//        };
//
//        int choice;
//        do {
//            choice = displayMenu("Employee Management", options);
//
//            switch (choice) {
//                case 1:
//                    addNewEmployee();
//                    break;
//                case 2:
//                    displayAllEmployees();
//                    break;
//                case 3:
//                    findEmployeeById();
//                    break;
//                case 4:
//                    updateEmployee();
//                    break;
//                case 5:
//                    removeEmployee();
//                    break;
//                case 0:
//                    // Return to previous menu
//                    break;
//            }
//        } while (choice != 0);
//    }
//
//
//    private void addNewEmployee() { // Add a new employee to the system
//        displayTitle("Add New Employee");
//
//        String id = getInput("Enter ID");
//
//        // Check if employee already exists
//        if (employeeService.getEmployee(id) != null) {
//            displayError("Employee with this ID already exists");
//            return;
//        }
//
//        String firstName = getInput("Enter First Name");
//        String lastName = getInput("Enter Last Name");
//        String bankAccount = getInput("Enter Bank Account Number");
//
//        LocalDate startDate = null;
//        while (startDate == null) {
//            try {
//                String dateStr = getInput("Enter Start Date (DD/MM/YYYY)");
//                startDate = LocalDate.parse(dateStr, dateFormatter);
//            } catch (DateTimeParseException e) {
//                displayError("Invalid date format. Please use DD/MM/YYYY");
//            }
//        }
//
//        double salary = 0;
//        boolean validSalary = false;
//        while (!validSalary) {
//            try {
//                String salaryStr = getInput("Enter Hourly Salary");
//                salary = Double.parseDouble(salaryStr);
//                validSalary = true;
//            } catch (NumberFormatException e) {
//                displayError("Invalid salary. Please enter a number");
//            }
//        }
//
//        // Add employee through service
//        boolean success = employeeService.addNewEmployee(id, firstName, lastName, bankAccount, startDate, salary);
//
//        if (success) {
//            displayMessage("Employee added successfully!");
//        } else {
//            displayError("Error adding employee");
//        }
//    }
//
//   //Display all employees in the system
//    private void displayAllEmployees() {
//        displayTitle("All Employees");
//
//        List<EmployeeDTO> employees = employeeService.getAllEmployees();
//
//        if (employees.isEmpty()) {
//            displayMessage("No employees in the system");
//            return;
//        }
//
//        for (EmployeeDTO employee : employees) {
//            displayMessage(String.format("%s: %s %s (Start Date: %s)",
//                    employee.getId(),
//                    employee.getFirstName(),
//                    employee.getLastName(),
//                    employee.getStartDate().format(dateFormatter)));
//        }
//    }
//
//    private void findEmployeeById() {
//        displayTitle("Search Employee");
//
//        String id = getInput("Enter ID to search");
//
//        EmployeeDTO employee = employeeService.getEmployeeDetails(id);
//
//        if (employee == null) {
//            displayError("No employee found with ID " + id);
//            return;
//        }
//
//        displayEmployeeDetails(employee);
//    }
//
//    private void displayEmployeeDetails(EmployeeDTO employee) {
//        displayTitle("Employee Details: " + employee.getFullName());
//
//        displayMessage("ID: " + employee.getId());
//        displayMessage("Full Name: " + employee.getFullName());
//        displayMessage("Bank Account: " + employee.getBankAccount());
//        displayMessage("Start Date: " + employee.getStartDate().format(dateFormatter));
//        displayMessage("Hourly Salary: " + employee.getSalary());
//
//        // Display qualified positions
//        if (employee.getQualifiedPositions().isEmpty()) {
//            displayMessage("Qualified Positions: None");
//        } else {
//            displayMessage("Qualified Positions:");
//            for (String position : employee.getQualifiedPositions()) {
//                displayMessage("- " + position);
//            }
//        }
//    }
//
//    private void updateEmployee() {
//        displayTitle("Update Employee");
//
//        String id = getInput("Enter ID of employee to update");
//
//        EmployeeDTO employee = employeeService.getEmployeeDetails(id);
//
//        if (employee == null) {
//            displayError("No employee found with ID " + id);
//            return;
//        }
//
//        displayEmployeeDetails(employee);
//
//        String[] options = {
//                "Update First Name",
//                "Update Last Name",
//                "Update Bank Account",
//                "Update Salary"
//        };
//
//        int choice = displayMenu("Select field to update", options);
//
//        switch (choice) {
//            case 1:
//                String firstName = getInput("Enter new first name");
//                if (employeeService.updateEmployeeFirstName(id, firstName)) {
//                    displayMessage("First name updated successfully");
//                } else {
//                    displayError("Error updating first name");
//                }
//                break;
//            case 2:
//                String lastName = getInput("Enter new last name");
//                if (employeeService.updateEmployeeLastName(id, lastName)) {
//                    displayMessage("Last name updated successfully");
//                } else {
//                    displayError("Error updating last name");
//                }
//                break;
//            case 3:
//                String bankAccount = getInput("Enter new bank account");
//                if (employeeService.updateEmployeeBankAccount(id, bankAccount)) {
//                    displayMessage("Bank account updated successfully");
//                } else {
//                    displayError("Error updating bank account");
//                }
//                break;
//            case 4:
//                try {
//                    double salary = Double.parseDouble(getInput("Enter new hourly salary"));
//                    if (employeeService.updateEmployeeSalary(id, salary)) {
//                        displayMessage("Salary updated successfully");
//                    } else {
//                        displayError("Error updating salary");
//                    }
//                } catch (NumberFormatException e) {
//                    displayError("Invalid salary. Update canceled");
//                }
//                break;
//            case 0:
//                // Return to menu
//                break;
//        }
//    }
//
//    private void removeEmployee() {
//        displayTitle("Remove Employee");
//
//        String id = getInput("Enter ID of employee to remove");
//
//        EmployeeDTO employee = employeeService.getEmployeeDetails(id);
//
//        if (employee == null) {
//            displayError("No employee found with ID " + id);
//            return;
//        }
//
//        displayEmployeeDetails(employee);
//
//        if (getBooleanInput("Are you sure you want to remove this employee?")) {
//            boolean success = employeeService.removeEmployee(id);
//
//            if (success) {
//                displayMessage("Employee successfully removed from the system");
//            } else {
//                displayError("Cannot remove the employee. They may be assigned to future shifts");
//            }
//        }
//    }
//}


package Presentation;

import Service.EmployeeService;
import Service.EmployeeDTO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

//Screen for managing employees
public class EmployeeManagementScreen extends BaseScreen {
    private final EmployeeService employeeService;
    private final DateTimeFormatter dateFormatter;
    private final EmployeeDTO loggedInEmployee;

    //Constructor - receives employee service as dependency
    public EmployeeManagementScreen(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.loggedInEmployee = null; // אין בדיקת הרשאות במחלקה המקורית
    }

    // קונסטרוקטור חדש שמקבל גם את המשתמש המחובר
    public EmployeeManagementScreen(EmployeeService employeeService, EmployeeDTO loggedInEmployee) {
        this.employeeService = employeeService;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.loggedInEmployee = loggedInEmployee;
    }

    //    @Override
//    public void display() {
//        // אם נשלח אובייקט של משתמש מחובר (הקונסטרוקטור השני)
//        // ואם המשתמש הוא לא מנהל כח אדם, אז אין גישה
//        if (loggedInEmployee != null && !loggedInEmployee.isHRManager()) {
//            displayError("Access denied. Only HR Managers can access this functionality.");
//            return;
//        }
//
//        String[] options = {
//                "Add New Employee",
//                "View All Employees",
//                "Search Employee by ID",
//                "Update Employee",
//                "Remove Employee"
//        };
//
//        int choice;
//        do {
//            choice = displayMenu("Employee Management", options);
//
//            switch (choice) {
//                case 1:
//                    addNewEmployee();
//                    break;
//                case 2:
//                    displayAllEmployees();
//                    break;
//                case 3:
//                    findEmployeeById();
//                    break;
//                case 4:
//                    updateEmployee();
//                    break;
//                case 5:
//                    removeEmployee();
//                    break;
//                case 0:
//                    // Return to previous menu
//                    break;
//            }
//        } while (choice != 0);
//    }
    @Override
    public void display() {
        // בדיקת הרשאות אם צריך
        if (loggedInEmployee != null && !loggedInEmployee.isHRManager()) {
            displayError("Access denied. Only HR Managers can access this functionality.");
            return;
        }

        String[] options = {
                "Add New Employee",
                "View All Employees",
                "Search Employee by ID",
                "Update Employee",
                "Remove Employee",
                "Manage Employee Roles"  // אפשרות חדשה
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
                case 6:
                    manageEmployeeRoles();  // קריאה לפונקציה החדשה
                    break;
                case 0:
                    // Return to previous menu
                    break;
            }
        } while (choice != 0);
    }

//    private void addNewEmployee() { // Add a new employee to the system
//        displayTitle("Add New Employee");
//
//        String id = getInput("Enter ID");
//
//        // Check if employee already exists
//        if (employeeService.getEmployee(id) != null) {
//            displayError("Employee with this ID already exists");
//            return;
//        }
//
//        String firstName = getInput("Enter First Name");
//        String lastName = getInput("Enter Last Name");
//        String bankAccount = getInput("Enter Bank Account Number");
//
//        LocalDate startDate = null;
//        while (startDate == null) {
//            try {
//                String dateStr = getInput("Enter Start Date (DD/MM/YYYY)");
//                startDate = LocalDate.parse(dateStr, dateFormatter);
//            } catch (DateTimeParseException e) {
//                displayError("Invalid date format. Please use DD/MM/YYYY");
//            }
//        }
//
//        double salary = 0;
//        boolean validSalary = false;
//        while (!validSalary) {
//            try {
//                String salaryStr = getInput("Enter Hourly Salary");
//                salary = Double.parseDouble(salaryStr);
//                validSalary = true;
//            } catch (NumberFormatException e) {
//                displayError("Invalid salary. Please enter a number");
//            }
//        }
//
//        // שאל אם העובד הוא מנהל
//        String[] roleOptions = {
//                "Regular Employee",
//                "Shift Manager",
//                "HR Manager"
//        };
//
//        int roleChoice = displayMenu("Select Role", roleOptions);
//        if (roleChoice == 0) {
//            return; // ביטול
//        }
//
//        String role = "REGULAR_EMPLOYEE";
//        if (roleChoice == 2) {
//            role = "SHIFT_MANAGER";
//        } else if (roleChoice == 3) {
//            role = "HR_MANAGER";
//        }
//
//        // קבל סיסמה אם העובד הוא מנהל
//        String password = "";
//        if (roleChoice > 1) {
//            password = getInput("Enter password for manager");
//            if (password.isEmpty()) {
//                displayError("Password cannot be empty for managers");
//                return;
//            }
//        }
//
//        // Add employee through service
//        boolean success = employeeService.addNewEmployee(id, firstName, lastName, bankAccount, startDate, salary, role, password);
//
//        if (success) {
//            displayMessage("Employee added successfully!");
//        } else {
//            displayError("Error adding employee");
//        }
//    }

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

        // הוסף עובד רגיל בלבד (ללא פרמטר role ו-password)
        boolean success = employeeService.addNewEmployee(id, firstName, lastName, bankAccount, startDate, salary);

        if (success) {
            displayMessage("Employee added successfully!");
        } else {
            displayError("Error adding employee");
        }
    }

    //Display all employees in the system
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

    private void displayEmployeeDetails(EmployeeDTO employee) {
        displayTitle("Employee Details: " + employee.getFullName());

        displayMessage("ID: " + employee.getId());
        displayMessage("Full Name: " + employee.getFullName());
        displayMessage("Bank Account: " + employee.getBankAccount());
        displayMessage("Start Date: " + employee.getStartDate().format(dateFormatter));
        displayMessage("Hourly Salary: " + employee.getSalary());

        // הצג תפקיד
        String roleStr = "Regular Employee";
        if (employee.isHRManager()) {
            roleStr = "HR Manager";
        } else if (employee.isShiftManager()) {
            roleStr = "Shift Manager";
        }
        displayMessage("Role: " + roleStr);

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
                "Update Role",
                "Update Password"
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
                updateEmployeeRole(id);
                break;
            case 6:
                updateEmployeePassword(id);
                break;
            case 0:
                // Return to menu
                break;
        }
    }

    private void updateEmployeeRole(String employeeId) {
        String[] roleOptions = {
                "Regular Employee",
                "Shift Manager",
                "HR Manager"
        };

        int roleChoice = displayMenu("Select New Role", roleOptions);
        if (roleChoice == 0) {
            return; // ביטול
        }

        String role = "REGULAR_EMPLOYEE";
        if (roleChoice == 2) {
            role = "SHIFT_MANAGER";
        } else if (roleChoice == 3) {
            role = "HR_MANAGER";
        }

        if (employeeService.updateEmployeeRole(employeeId, role)) {
            displayMessage("Role updated successfully");
        } else {
            displayError("Error updating role");
        }
    }

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

    private void manageEmployeeRoles() {
        displayTitle("Manage Employee Roles");

        // בחר עובד
        String id = getInput("Enter ID of employee to update role");
        EmployeeDTO employee = employeeService.getEmployeeDetails(id);

        if (employee == null) {
            displayError("No employee found with ID " + id);
            return;
        }

        // הצג פרטי עובד
        displayEmployeeDetails(employee);

        // הצג אפשרויות תפקיד
        String[] roleOptions = {
                "Regular Employee",
                "Shift Manager"
        };

        int choice = displayMenu("Select New Role", roleOptions);
        if (choice == 0) {
            return; // ביטול
        }

        String role = "REGULAR_EMPLOYEE";
        if (choice == 2) {
            role = "SHIFT_MANAGER";

            // בקש סיסמה אם מעלים לתפקיד מנהל
            String password = getInput("Enter password for shift manager");
            if (password.isEmpty()) {
                displayError("Password cannot be empty for managers");
                return;
            }

            // עדכן סיסמה
            if (!employeeService.updateEmployeePassword(id, password)) {
                displayError("Error updating password");
                return;
            }
        }

        // עדכן תפקיד
        if (employeeService.updateEmployeeRole(id, role)) {
            displayMessage("Role updated successfully");
        } else {
            displayError("Error updating role");
        }
    }
}