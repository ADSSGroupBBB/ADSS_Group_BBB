package Presentation;

import Domain.Employee;
import Domain.Position;
import Service.EmployeeService;

import java.util.List;
import java.util.Scanner;

/**
 * Screen for managing employee qualifications
 */
public class QualificationManagementScreen {
    private final EmployeeService employeeService;
    private final Scanner scanner;

    /**
     * Constructor - receives employee service as dependency
     * @param employeeService The employee service
     */
    public QualificationManagementScreen(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Display the main qualification management screen
     */
    public void display() {
        String[] options = {
                "Add New Position",
                "View All Positions",
                "Add Qualification to Employee",
                "View Qualified Employees for Position",
                "Update Required Positions for Shifts"
        };

        int choice;
        do {
            choice = displayMenu("Qualification Management", options);

            switch (choice) {
                case 1:
                    addNewPosition();
                    break;
                case 2:
                    displayAllPositions();
                    break;
                case 3:
                    addEmployeeQualification();
                    break;
                case 4:
                    displayQualifiedEmployees();
                    break;
                case 5:
                    updateRequiredPositions();
                    break;
                case 0:
                    // Return to previous menu
                    break;
            }
        } while (choice != 0);
    }

    /**
     * Add a new position to the system
     */
    private void addNewPosition() {
        displayTitle("Add New Position");

        String name = getInput("Enter Position Name");

        // Check if position already exists
        if (employeeService.getPosition(name) != null) {
            displayError("Position with this name already exists");
            return;
        }

        String description = getInput("Enter Position Description");
        boolean isShiftManagerRole = getBooleanInput("Is this a shift manager position?");

        // Create new position
        Position newPosition = new Position(name, isShiftManagerRole);

        // Add position to the system
        if (employeeService.addPosition(newPosition)) {
            displayMessage("Position added successfully!");
        }
    }
    /**
     * Display all positions in the system
     */
    private void displayAllPositions() {
        displayTitle("All Positions");

        List<Position> positions = employeeService.getAllPositions();

        if (positions.isEmpty()) {
            displayMessage("No positions defined in the system");
            return;
        }

        for (Position position : positions) {
            String isManager = position.isRequiresShiftManager() ? " (Shift Manager Position)" : "";
            displayMessage(position.getName() + isManager);
        }
    }

    /**
     * Add qualification to an employee
     */
    private void addEmployeeQualification() {
        displayTitle("Add Qualification to Employee");

        // Select employee
        Employee employee = selectEmployee();
        if (employee == null) {
            return;
        }

        // Display current qualifications
        displayMessage("Current qualifications for " + employee.getFullName() + ":");
        if (employee.getQualifiedPositions().isEmpty()) {
            displayMessage("None");
        } else {
            employee.getQualifiedPositions().forEach(position ->
                    displayMessage("- " + position.getName())
            );
        }
        // Select position
        Position position = selectPosition();
        if (position == null) {
            return;
        }
        // Check if already qualified
        if (employee.isQualifiedFor(position)) {
            displayError("Employee is already qualified for this position");
            return;
        }
        // Add qualification
        boolean success = employeeService.addQualificationToEmployee(employee.getId(), position.getName());

        if (success) {
            displayMessage("Qualification added successfully");
        } else {
            displayError("Error adding qualification");
        }
    }
    /**
     * Display employees qualified for a specific position
     */
    private void displayQualifiedEmployees() {
        displayTitle("Qualified Employees for Position");

        // Select position
        Position position = selectPosition();
        if (position == null) {
            return;
        }
        // Get qualified employees
        List<Employee> qualifiedEmployees = employeeService.getQualifiedEmployeesForPosition(position);
        if (qualifiedEmployees.isEmpty()) {
            displayMessage("No employees are qualified for position: " + position.getName());
            return;
        }
        displayMessage("Employees qualified for position: " + position.getName());
        for (Employee employee : qualifiedEmployees) {
            displayMessage("- " + employee.getFullName() + " (ID: " + employee.getId() + ")");
        }
    }
    /**
     * Update required positions for shifts
     */
    private void updateRequiredPositions() {
        displayTitle("Update Required Positions for Shifts");

        // Select position
        Position position = selectPosition();
        if (position == null) {
            return;
        }
        updateRequiredPositionsForPosition(position);
    }
    /**
     * Update required positions for a specific position
     * @param position The position to update requirements for
     */
    private void updateRequiredPositionsForPosition(Position position) {
        displayTitle("Update Shift Requirements for Position: " + position.getName());

        // Update for morning shift
        int morningCount = getIntInput("Number of employees required for morning shift");

        if (morningCount >= 0) {
            boolean success = employeeService.addRequiredPosition(Domain.ShiftType.MORNING, position.getName(), morningCount);

            if (success) {
                displayMessage("Morning shift requirements updated successfully");
            } else {
                displayError("Error updating morning shift requirements");
            }
        }

        // Update for evening shift
        int eveningCount = getIntInput("Number of employees required for evening shift");

        if (eveningCount >= 0) {
            boolean success = employeeService.addRequiredPosition(Domain.ShiftType.EVENING, position.getName(), eveningCount);

            if (success) {
                displayMessage("Evening shift requirements updated successfully");
            } else {
                displayError("Error updating evening shift requirements");
            }
        }
    }

    /**
     * Select an employee from the list of employees
     * @return The selected employee or null if canceled
     */
    private Employee selectEmployee() {
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees.isEmpty()) {
            displayError("No employees in the system");
            return null;
        }
        // Build array of names for display in menu
        String[] employeeNames = new String[employees.size()];
        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            employeeNames[i] = emp.getFullName() + " (ID: " + emp.getId() + ")";
        }
        int choice = displayMenu("Select Employee", employeeNames);
        if (choice == 0) {
            return null; // User chose to go back
        }
        return employees.get(choice - 1);
    }
    /**
     * Select a position from the list of positions
     * @return The selected position or null if canceled
     */
    private Position selectPosition() {
        List<Position> positions = employeeService.getAllPositions();

        if (positions.isEmpty()) {
            displayError("No positions defined in the system");
            return null;
        }
        // Build array of names for display in menu
        String[] positionNames = new String[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            Position pos = positions.get(i);
            String isManager = pos.isRequiresShiftManager() ? " (Shift Manager)" : "";
            positionNames[i] = pos.getName() + isManager;
        }
        int choice = displayMenu("Select Position", positionNames);
        if (choice == 0) {
            return null; // User chose to go back
        }
        return positions.get(choice - 1);
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