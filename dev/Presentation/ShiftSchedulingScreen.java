package Presentation;

import Domain.Employee;
import Domain.Position;
import Domain.Shift;
import Domain.ShiftType;
import Service.EmployeeService;
import Service.ShiftService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Screen for shift scheduling functionality
 */
public class ShiftSchedulingScreen {
    private final EmployeeService employeeService;
    private final ShiftService shiftService;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter;
    /**
     * Constructor - receives employee and shift services as dependencies
     * @param employeeService The employee service
     * @param shiftService The shift service
     */
    public ShiftSchedulingScreen(EmployeeService employeeService, ShiftService shiftService) {
        this.employeeService = employeeService;
        this.shiftService = shiftService;
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }
    /**
     * Display the main shift scheduling screen
     */
    public void display() {
        String[] options = {
                "Create Shifts for Week",
                "View Future Shifts",
                "Assign Employee to Shift",
                "Remove Employee from Shift",
                "View Available Employees for Shift",
                "View Missing Positions in Shifts"
        };
        int choice;
        do {
            choice = displayMenu("Shift Scheduling", options);
            switch (choice) {
                case 1:
                    createShiftsForWeek();
                    break;
                case 2:
                    viewFutureShifts();
                    break;
                case 3:
                    assignEmployeeToShift();
                    break;
                case 4:
                    removeEmployeeFromShift();
                    break;
                case 5:
                    viewAvailableEmployeesForShift();
                    break;
                case 6:
                    viewMissingPositions();
                    break;
                case 0:
                    // Return to previous menu
                    break;
            }
        } while (choice != 0);
    }
    /**
     * Create shifts for a week starting from a given date
     */
    private void createShiftsForWeek() {// לבדחוק???
        displayTitle("Create Shifts for Week");
        LocalDate startDate = null;
        while (startDate == null) {
            try {
                String dateStr = getInput("Enter start date (Sunday) in format DD/MM/YYYY");
                startDate = LocalDate.parse(dateStr, dateFormatter);
                // Verify that the date is a Sunday
                if (startDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    displayError("The date must be a Sunday");
                    startDate = null;
                }
            } catch (DateTimeParseException e) {
                displayError("Invalid date format. Please use DD/MM/YYYY");
            }
        }
        // Create shifts for the week
        List<Shift> createdShifts = shiftService.createShiftsForWeek(startDate);
        if (createdShifts.isEmpty()) {
            displayError("No shifts were created. Shifts may already exist for this week");
        } else {
            displayMessage(createdShifts.size() + " shifts were created successfully for week starting " +
                    startDate.format(dateFormatter));
        }
    }
    /**
     * View future shifts
     */
    private void viewFutureShifts() {
        displayTitle("Future Shifts");
        List<Shift> futureShifts = shiftService.getFutureShifts();
        if (futureShifts.isEmpty()) {
            displayMessage("No future shifts in the system");
            return;
        }
        // Sort shifts by date
        futureShifts.sort((s1, s2) -> s1.getDate().compareTo(s2.getDate()));
        for (Shift shift : futureShifts) {
            String shiftType = shift.getShiftType().toString();
            String hasManager = shift.hasShiftManager() ? "Yes" : "No";
            displayMessage(shift.getDate().format(dateFormatter) + " - " + shiftType +
                    " (Has Manager: " + hasManager + ", ID: " + shift.getId() + ")");
        }
    }
    /**
     * Assign an employee to a shift
     */
    private void assignEmployeeToShift() {
        displayTitle("Assign Employee to Shift");
        // Select shift
        Shift shift = selectFutureShift();
        if (shift == null) {
            return;
        }
        // Display shift details
        displayShiftDetails(shift);
        // Select position
        Position position = selectPosition();
        if (position == null) {
            return;
        }
        // Get qualified and available employees for this position and shift
        List<Employee> qualifiedEmployees = employeeService.getQualifiedEmployeesForPosition(position);
        List<Employee> availableEmployees = employeeService.getAvailableEmployeesForShift(shift.getDate(), shift.getShiftType());
        // Filter to get employees that are both qualified and available
        List<Employee> eligibleEmployees = new ArrayList<>();
        for (Employee employee : qualifiedEmployees) {
            if (availableEmployees.contains(employee)) {
                eligibleEmployees.add(employee);
            }
        }
        if (eligibleEmployees.isEmpty()) {
            displayError("No eligible employees for this position and shift");
            return;
        }
        // Select employee
        Employee employee = selectEmployeeFromList(eligibleEmployees);
        if (employee == null) {
            return;
        }
        // Perform assignment
        boolean success = employeeService.assignEmployeeToShift(
                shift.getId(), employee.getId(), position.getName());
        if (success) {
            displayMessage("Employee successfully assigned to shift");
            // Check if all required positions are now covered
            if (employeeService.areAllRequiredPositionsCovered(shift.getId())) {
                displayMessage("All required positions for this shift are now covered");
            }
        } else {
            displayError("Error assigning employee to shift");
        }
    }
    /**
     * Remove an employee from a shift
     */
    private void removeEmployeeFromShift() {
        displayTitle("Remove Employee from Shift");
        // Select shift
        Shift shift = selectFutureShift();
        if (shift == null) {
            return;
        }
        // Display shift details
        displayShiftDetails(shift);
        Map<Position, Employee> assignments = shift.getAllAssignedEmployees();
        if (assignments.isEmpty()) {
            displayError("No employees assigned to this shift");
            return;
        }
        // Build list of assigned positions
        List<Position> assignedPositions = new ArrayList<>(assignments.keySet());
        String[] positionNames = new String[assignedPositions.size()];
        for (int i = 0; i < assignedPositions.size(); i++) {
            Position position = assignedPositions.get(i);
            Employee employee = assignments.get(position);
            positionNames[i] = position.getName() + ": " + employee.getFullName();
        }
        int positionIndex = displayMenu("Select assignment to remove", positionNames);
        if (positionIndex == 0) {
            return;
        }
        Position selectedPosition = assignedPositions.get(positionIndex - 1);
        // Confirm removal
        Employee employee = assignments.get(selectedPosition);

        if (getBooleanInput("Are you sure you want to remove " +
                employee.getFullName() + " from position " + selectedPosition.getName() + "?")) {
            boolean success = employeeService.removeAssignmentFromShift(shift.getId(), selectedPosition.getName());
            if (success) {
                displayMessage("Assignment successfully removed");
            } else {
                displayError("Error removing assignment");
            }
        }
    }
    /**
     * View available employees for a shift
     */
    private void viewAvailableEmployeesForShift() {
        displayTitle("Available Employees for Shift");
        // Select or create a shift
        Shift shift = selectOrCreateShift();
        if (shift == null) {
            return;
        }
        // Get available employees
        List<Employee> availableEmployees = employeeService.getAvailableEmployeesForShift(
                shift.getDate(), shift.getShiftType());
        if (availableEmployees.isEmpty()) {
            displayMessage("No employees available for this shift");
            return;
        }
        displayTitle("Available Employees for " + shift.getDate().format(dateFormatter) +
                " - " + shift.getShiftType().toString());
        for (Employee employee : availableEmployees) {
            displayMessage("- " + employee.getFullName() + " (ID: " + employee.getId() + ")");
        }
        // Option to view qualified employees by position
        if (getBooleanInput("Do you want to view available employees by position?")) {
            Position position = selectPosition();
            if (position != null) {
                List<Employee> qualifiedEmployees = employeeService.getQualifiedEmployeesForPosition(position);
                // Filter to get employees that are both qualified and available
                List<Employee> eligibleEmployees = new ArrayList<>();
                for (Employee employee : qualifiedEmployees) {
                    if (availableEmployees.contains(employee)) {
                        eligibleEmployees.add(employee);
                    }
                }
                displayTitle("Available Employees for Position: " + position.getName());
                if (eligibleEmployees.isEmpty()) {
                    displayMessage("No available employees qualified for this position");
                } else {
                    for (Employee employee : eligibleEmployees) {
                        displayMessage("- " + employee.getFullName() + " (ID: " + employee.getId() + ")");
                    }
                }
            }
        }
    }
    /**
     * View missing positions in shifts
     */
    private void viewMissingPositions() {
        displayTitle("Missing Positions in Future Shifts");
        List<Shift> futureShifts = shiftService.getFutureShifts();
        if (futureShifts.isEmpty()) {
            displayMessage("No future shifts in the system");
            return;
        }
        boolean foundMissing = false;
        for (Shift shift : futureShifts) {
            List<Position> missingPositions = shiftService.getMissingPositionsForShift(shift.getId());
            if (!missingPositions.isEmpty()) {
                foundMissing = true;
                displayTitle(shift.getDate().format(dateFormatter) + " - " + shift.getShiftType().toString());
                for (Position position : missingPositions) {
                    displayMessage("- " + position.getName());
                }
            }
        }
        if (!foundMissing) {
            displayMessage("No missing positions in future shifts");
        }
    }
    /**
     * Display shift details
     * @param shift The shift to display
     */
    private void displayShiftDetails(Shift shift) {
        displayTitle("Shift Details: " + shift.getDate().format(dateFormatter) + " - " +
                shift.getShiftType().toString());

        displayMessage("Shift ID: " + shift.getId());
        displayMessage("Start Time: " + shift.getStartTime());
        displayMessage("End Time: " + shift.getEndTime());

        // Display shift manager
        Employee shiftManager = shift.getShiftManager();
        if (shiftManager != null) {
            displayMessage("Shift Manager: " + shiftManager.getFullName());
        } else {
            displayMessage("Shift Manager: Not assigned");
        }

        // Display assigned employees
        Map<Position, Employee> assignments = shift.getAllAssignedEmployees();

        if (assignments.isEmpty()) {
            displayMessage("Assigned Employees: None");
        } else {
            displayMessage("\nAssigned Employees:");
            for (Map.Entry<Position, Employee> entry : assignments.entrySet()) {
                Position position = entry.getKey();
                Employee employee = entry.getValue();
                displayMessage("- " + position.getName() + ": " + employee.getFullName());
            }
        }

        // Display missing positions
        List<Position> missingPositions = shiftService.getMissingPositionsForShift(shift.getId());
        if (!missingPositions.isEmpty()) {
            displayMessage("\nMissing Positions:");
            for (Position position : missingPositions) {
                displayMessage("- " + position.getName());
            }
        }
    }
    /**
     * Select a future shift
     * @return The selected shift or null if canceled
     */
    private Shift selectFutureShift() {
        List<Shift> futureShifts = shiftService.getFutureShifts();
        if (futureShifts.isEmpty()) {
            displayError("No future shifts in the system");
            return null;
        }
        // Sort shifts by date
        futureShifts.sort((s1, s2) -> s1.getDate().compareTo(s2.getDate()));
        // Build array of descriptions for display in menu
        String[] shiftDescriptions = new String[futureShifts.size()];
        for (int i = 0; i < futureShifts.size(); i++) {
            Shift shift = futureShifts.get(i);
            shiftDescriptions[i] = shift.getDate().format(dateFormatter) + " - " +
                    shift.getShiftType().toString();
        }
        int choice = displayMenu("Select Shift", shiftDescriptions);
        if (choice == 0) {
            return null; // User chose to go back
        }
        return futureShifts.get(choice - 1);
    }
    /**
     * Select a position from available positions
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
     * Select an employee from a provided list
     * @param employees List of employees to choose from
     * @return The selected employee or null if canceled
     */
    private Employee selectEmployeeFromList(List<Employee> employees) {
        if (employees.isEmpty()) {
            displayError("No employees to select from");
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
     * Select a shift or create a new one for a specific date
     * @return The selected or created shift, or null if canceled
     */
    private Shift selectOrCreateShift() {
        String[] options = {
                "Select existing shift",
                "Create new shift for specific date"
        };
        int choice = displayMenu("Shift Selection", options);
        switch (choice) {
            case 1:
                return selectFutureShift();
            case 2:
                return createShiftForDate();
            default:
                return null;
        }
    }
    /**
     * Create a shift for a specific date
     * @return The created shift or null if canceled
     */
    private Shift createShiftForDate() {
        LocalDate date = null;
        while (date == null) {
            try {
                String dateStr = getInput("Enter date (DD/MM/YYYY)");
                date = LocalDate.parse(dateStr, dateFormatter);
            } catch (DateTimeParseException e) {
                displayError("Invalid date format. Please use DD/MM/YYYY");
            }
        }
        String[] shiftOptions = {
                "Morning Shift",
                "Evening Shift"
        };

        int shiftChoice = displayMenu("Select Shift Type", shiftOptions);
        if (shiftChoice == 0) {
            return null;
        }
        ShiftType shiftType = (shiftChoice == 1) ? ShiftType.MORNING : ShiftType.EVENING;
        // Check if shift already exists
        Shift existingShift = employeeService.getShift(date, shiftType);
        if (existingShift != null) {
            displayMessage("Shift already exists for this date and type");
            return existingShift;
        }
        // Create new shift
        Shift newShift = employeeService.createShift(date, shiftType);
        if (newShift != null) {
            displayMessage("Shift created successfully");
            return newShift;
        } else {
            displayError("Error creating shift");
            return null;
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