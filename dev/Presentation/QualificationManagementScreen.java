package Presentation;

import Service.EmployeeDTO;
import Service.EmployeeService;
import Service.PositionDTO;
import java.util.List;
import java.util.stream.Collectors;

/**
 * QualificationManagementScreen provides the user interface for managing positions and
 * employee qualifications in the system.
 *
 * This screen allows managers to:
 * - Create and view positions
 * - Assign qualifications to employees
 * - Remove qualifications from employees
 * - View employees qualified for specific positions
 * - Configure position requirements for different shift types
 */
public class QualificationManagementScreen extends BaseScreen {
    private final EmployeeService employeeService;
    private final EmployeeDTO loggedInEmployee;

    /**
     * Constructor for backward compatibility.
     * Does not perform permission checking.
     *
     * @param employeeService The service for accessing employee and position data
     */
    public QualificationManagementScreen(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.loggedInEmployee = null;
    }

    /**
     * Constructor that takes the logged-in employee for permission checking.
     *
     * @param employeeService The service for accessing employee and position data
     * @param loggedInEmployee The currently logged-in employee
     */
    public QualificationManagementScreen(EmployeeService employeeService, EmployeeDTO loggedInEmployee) {
        this.employeeService = employeeService;
        this.loggedInEmployee = loggedInEmployee;
    }

    /**
     * Displays the qualification management screen if the user has appropriate permissions.
     * Only managers (shift managers and HR managers) can access this functionality.
     */
    @Override
    public void display() {
        // Permission check - only managers can access this screen
        if (loggedInEmployee != null && !loggedInEmployee.isManager()) {
            displayError("Access denied. Only managers can access this functionality.");
            return;
        }

        String[] options = {
                "Add New Position",
                "View All Positions",
                "Add Qualification to Employee",
                "Remove Qualification from Employee",
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
                    removeEmployeeQualification();
                    break;
                case 5:
                    displayQualifiedEmployees();
                    break;
                case 6:
                    updateRequiredPositions();
                    break;
                case 0:
                    // Return to previous menu
                    break;
            }
        } while (choice != 0);
    }

    /**
     * Displays a form for adding a new position to the system.
     * Collects position name and whether it requires shift manager privileges.
     */
    private void addNewPosition() {
        displayTitle("Add New Position");
        String name = getInput("Enter Position Name");

        // Check if position already exists
        if (employeeService.getPositionDetails(name) != null) {
            displayError("Position with this name already exists");
            return;
        }

        boolean isShiftManagerRole = getBooleanInput("Is this a shift manager position?");

        // Add position to the system
        if (employeeService.addPosition(name, isShiftManagerRole)) {
            displayMessage("Position added successfully!");
        } else {
            displayError("Error adding position");
        }
    }

    /**
     * Displays a list of all positions defined in the system.
     * Indicates which positions require shift manager privileges.
     */
    private void displayAllPositions() {
        displayTitle("All Positions");

        List<PositionDTO> positions = employeeService.getAllPositions();

        if (positions.isEmpty()) {
            displayMessage("No positions defined in the system");
            return;
        }

        for (PositionDTO position : positions) {
            String isManager = position.isRequiresShiftManager() ? " (Shift Manager Position)" : "";
            displayMessage(position.getName() + isManager);
        }
    }

    /**
     * Adds a qualification for a specific position to an employee.
     */
    private void addEmployeeQualification() {
        displayTitle("Add Qualification to Employee");

        // Select employee
        EmployeeDTO employee = selectEmployee();
        if (employee == null) {
            return;
        }

        // Display current qualifications
        displayMessage("Current qualifications for " + employee.getFullName() + ":");
        if (employee.getQualifiedPositions().isEmpty()) {
            displayMessage("None");
        } else {
            for (String position : employee.getQualifiedPositions()) {
                displayMessage("- " + position);
            }
        }

        // Select position
        PositionDTO position = selectPosition();
        if (position == null) {
            return;
        }

        // Check if already qualified
        if (employee.getQualifiedPositions().contains(position.getName())) {
            displayError("Employee is already qualified for this position");
            return;
        }

        // Add qualification
        boolean success = employeeService.addQualificationToEmployee(employee.getId(), position.getName());

        if (success) {
            displayMessage("Qualification added successfully");

            // If position requires shift manager privileges and employee is not already a manager
            if (position.isRequiresShiftManager() && !employee.isManager()) {
                // Automatically update the employee's role to SHIFT_MANAGER
                if (employeeService.updateEmployeeRole(employee.getId(), "SHIFT_MANAGER")) {
                    // Set password to the employee's ID
                    employeeService.updateEmployeePassword(employee.getId(), employee.getId());
                    displayMessage("Employee role updated to Shift Manager automatically");
                    displayMessage("Password set to employee ID: " + employee.getId());
                } else {
                    displayError("Failed to update employee role to Shift Manager");
                }
            }
        } else {
            displayError("Error adding qualification");
        }
    }

    /**
     * Removes a qualification from an employee.
     */
    private void removeEmployeeQualification() {
        displayTitle("Remove Qualification from Employee");

        // Select employee
        EmployeeDTO employee = selectEmployee();
        if (employee == null) {
            return;
        }

        // Display current qualifications
        displayMessage("Current qualifications for " + employee.getFullName() + ":");
        if (employee.getQualifiedPositions().isEmpty()) {
            displayMessage("None");
            return;  // If no qualifications, return
        } else {
            for (String position : employee.getQualifiedPositions()) {
                displayMessage("- " + position);
            }
        }

        // Build array of qualification names for menu display
        String[] positionNames = new String[employee.getQualifiedPositions().size()];
        employee.getQualifiedPositions().toArray(positionNames);

        int choice = displayMenu("Select Qualification to Remove", positionNames);
        if (choice == 0) {
            return;  // User canceled
        }

        String positionToRemove = positionNames[choice - 1];

        // Remove qualification through service
        boolean success = employeeService.removeQualificationFromEmployee(employee.getId(), positionToRemove);

        if (success) {
            displayMessage("Qualification successfully removed");
        } else {
            displayError("Error removing qualification");
        }
    }

    /**
     * Displays a list of employees qualified for a specific position.
     */
    private void displayQualifiedEmployees() {
        displayTitle("Qualified Employees for Position");

        // Select position
        PositionDTO position = selectPosition();
        if (position == null) {
            return;
        }

        // Get qualified employees
        List<EmployeeDTO> qualifiedEmployees = employeeService.getQualifiedEmployeesForPosition(position.getName());

        if (qualifiedEmployees.isEmpty()) {
            displayMessage("No employees are qualified for position: " + position.getName());
            return;
        }

        displayMessage("Employees qualified for position: " + position.getName());
        for (EmployeeDTO employee : qualifiedEmployees) {
            displayMessage("- " + employee.getFullName() + " (ID: " + employee.getId() + ")");
        }
    }

    /**
     * Updates the required number of employees for a specific position in shifts.
     * Allows selecting a position and defining requirements for morning and evening shifts.
     */
    private void updateRequiredPositions() {
        displayTitle("Update Required Positions for Shifts");

        // Select position
        PositionDTO position = selectPosition();
        if (position == null) {
            return;
        }

        updateRequiredPositionsForPosition(position);
    }

    /**
     * Helper method that updates shift requirements for a specific position.
     * Sets how many employees with this qualification are needed for morning and evening shifts.
     *
     * @param position The position to update requirements for
     */
    private void updateRequiredPositionsForPosition(PositionDTO position) {
        displayTitle("Update Shift Requirements for Position: " + position.getName());

        // Update for morning shift
        int morningCount = getIntInput("Number of employees required for morning shift");

        if (morningCount >= 0) {
            boolean success = employeeService.addRequiredPosition("MORNING", position.getName(), morningCount);

            if (success) {
                displayMessage("Morning shift requirements updated successfully");
            } else {
                displayError("Error updating morning shift requirements");
            }
        }

        // Update for evening shift
        int eveningCount = getIntInput("Number of employees required for evening shift");

        if (eveningCount >= 0) {
            boolean success = employeeService.addRequiredPosition("EVENING", position.getName(), eveningCount);

            if (success) {
                displayMessage("Evening shift requirements updated successfully");
            } else {
                displayError("Error updating evening shift requirements");
            }
        }
    }

    /**
     * Displays a menu for selecting an employee from the system.
     * @return The selected employee DTO, or null if selection was canceled
     */
    private EmployeeDTO selectEmployee() {
        // Get list of employees but filter out the admin user
        List<EmployeeDTO> employees = employeeService.getAllEmployees().stream()
                .filter(emp -> !emp.getId().equals("admin"))
                .collect(Collectors.toList());

        if (employees.isEmpty()) {
            displayError("No employees in the system");
            return null;
        }

        // Build array of names for display in menu
        String[] employeeNames = new String[employees.size()];
        for (int i = 0; i < employees.size(); i++) {
            EmployeeDTO emp = employees.get(i);
            employeeNames[i] = emp.getFullName() + " (ID: " + emp.getId() + ")";
        }

        int choice = displayMenu("Select Employee", employeeNames);
        if (choice == 0) {
            return null; // User chose to go back
        }

        return employees.get(choice - 1);
    }

    /**
     * Displays a menu for selecting a position from the system.
     *
     * @return The selected position DTO, or null if selection was canceled
     */
    private PositionDTO selectPosition() {
        List<PositionDTO> positions = employeeService.getAllPositions();

        if (positions.isEmpty()) {
            displayError("No positions defined in the system");
            return null;
        }

        // Build array of names for display in menu
        String[] positionNames = new String[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            PositionDTO pos = positions.get(i);
            String isManager = pos.isRequiresShiftManager() ? " (Shift Manager)" : "";
            positionNames[i] = pos.getName() + isManager;
        }

        int choice = displayMenu("Select Position", positionNames);
        if (choice == 0) {
            return null; // User chose to go back
        }

        return positions.get(choice - 1);
    }
}