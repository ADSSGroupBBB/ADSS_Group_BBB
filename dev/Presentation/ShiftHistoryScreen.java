package Presentation;

import Domain.Employee;
import Domain.Position;
import Domain.Shift;
import Service.EmployeeService;
import Service.ShiftService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Screen for viewing shift history
 * Implements high cohesion by focusing only on displaying shift history
 */
public class ShiftHistoryScreen {
    private final EmployeeService employeeService;
    private final ShiftService shiftService;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter;

    /**
     * Constructor - receives employee and shift services as dependencies
     * @param employeeService The employee service
     * @param shiftService The shift service
     */
    public ShiftHistoryScreen(EmployeeService employeeService, ShiftService shiftService) {
        this.employeeService = employeeService;
        this.shiftService = shiftService;
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    /**
     * Display the main shift history screen
     */
    public void display() {
        String[] options = {
                "View All Historic Shifts",
                "View Employee Shift History",
                "View Shift Details",
                "Generate Shift History Report"
        };

        int choice;
        do {
            choice = displayMenu("Shift History", options);

            switch (choice) {
                case 1:
                    displayHistoricShifts();
                    break;
                case 2:
                    displayEmployeeShiftHistory();
                    break;
                case 3:
                    displayShiftDetails();
                    break;
                case 4:
                    generateShiftHistoryReport();
                    break;
                case 0:
                    // Return to previous menu
                    break;
            }
        } while (choice != 0);
    }

    /**
     * Display all historic shifts (shifts from the past)
     */
    private void displayHistoricShifts() {
        displayTitle("Historic Shifts");
        List<Shift> historicShifts = shiftService.getHistoricalShifts();
        if (historicShifts.isEmpty()) {
            displayMessage("No historic shifts in the system");
            return;
        }
        // Sort shifts by date
        historicShifts.sort((s1, s2) -> s2.getDate().compareTo(s1.getDate())); // Newest first
        // Display shifts in list format
        for (Shift shift : historicShifts) {
            String shiftType = shift.getShiftType().toString();
            displayMessage(shift.getDate().format(dateFormatter) + " - " + shiftType +
                    " (ID: " + shift.getId() + ")");
        }
    }
    /**
     * Display shift history for a specific employee
     */
    private void displayEmployeeShiftHistory() {
        displayTitle("Employee Shift History");
        // Select employee
        Employee employee = selectEmployee();
        if (employee == null) {
            return;
        }
        // Get shift history for employee
        List<Shift> employeeShifts = shiftService.getEmployeeShiftHistory(employee.getId());

        if (employeeShifts.isEmpty()) {
            displayMessage("No shift history found for employee: " + employee.getFullName());
            return;
        }
        // Sort shifts by date
        employeeShifts.sort((s1, s2) -> s2.getDate().compareTo(s1.getDate())); // Newest first
        displayTitle("Shift History for " + employee.getFullName());
        // Display shifts
        for (Shift shift : employeeShifts) {
            String shiftType = shift.getShiftType().toString();
            // Find position for this employee in this shift
            String position = "Unknown";
            for (Map.Entry<Position, Employee> entry : shift.getAllAssignedEmployees().entrySet()) {
                if (entry.getValue().getId().equals(employee.getId())) {
                    position = entry.getKey().getName();
                    break;
                }
            }
            displayMessage(shift.getDate().format(dateFormatter) + " - " + shiftType +
                    " - Position: " + position);
        }
    }
    /**
     * Display details for a specific shift
     */
    private void displayShiftDetails() {
        displayTitle("Shift Details");
        // Select shift
        Shift shift = selectShift();
        if (shift == null) {
            return;
        }
        displayShiftDetailsFull(shift);
    }
    /**
     * Generate a shift history report
     */
    private void generateShiftHistoryReport() {
        displayTitle("Generate Shift History Report");
        // Define report period
        LocalDate startDate = getDateInput("Enter start date (DD/MM/YYYY)");
        if (startDate == null) {
            return;
        }
        LocalDate endDate = getDateInput("Enter end date (DD/MM/YYYY)");
        if (endDate == null || endDate.isBefore(startDate)) {
            displayError("End date must be after start date");
            return;
        }
        // Get all shifts
        List<Shift> allShifts = employeeService.getAllShifts();
        // Filter shifts for the period
        List<Shift> periodShifts = allShifts.stream()
                .filter(shift -> !shift.getDate().isBefore(startDate) && !shift.getDate().isAfter(endDate))
                .sorted((s1, s2) -> s1.getDate().compareTo(s2.getDate()))
                .toList();
        if (periodShifts.isEmpty()) {
            displayMessage("No shifts found for the selected period");
            return;
        }
        displayTitle("Shift History Report: " + startDate.format(dateFormatter) +
                " to " + endDate.format(dateFormatter));
        // Count shifts by day
        Map<LocalDate, Long> shiftCountByDay = periodShifts.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Shift::getDate, java.util.stream.Collectors.counting()));
        // Display summary
        displayMessage("Total shifts in period: " + periodShifts.size());
        displayMessage("Shifts by date:");
        for (Map.Entry<LocalDate, Long> entry : shiftCountByDay.entrySet()) {
            displayMessage("- " + entry.getKey().format(dateFormatter) + ": " + entry.getValue() + " shifts");
        }
        // Ask if detailed report is wanted
        if (getBooleanInput("Display detailed shift information?")) {
            for (Shift shift : periodShifts) {
                displayShiftDetailsFull(shift);
            }
        }
    }
    /**
     * Display full details for a shift
     * @param shift The shift to display
     */
    private void displayShiftDetailsFull(Shift shift) {
        displayTitle("Shift Details: " + shift.getDate().format(dateFormatter) + " - " +
                shift.getShiftType().toString());
        displayMessage("Shift ID: " + shift.getId());
        displayMessage("Date: " + shift.getDate().format(dateFormatter));
        displayMessage("Type: " + shift.getShiftType().toString());
        displayMessage("Start Time: " + shift.getStartTime());
        displayMessage("End Time: " + shift.getEndTime());

        // Display shift manager
        Employee shiftManager = shift.getShiftManager();
        if (shiftManager != null) {
            displayMessage("Shift Manager: " + shiftManager.getFullName() + " (ID: " + shiftManager.getId() + ")");
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
                displayMessage("- " + position.getName() + ": " + employee.getFullName() + " (ID: " + employee.getId() + ")");
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
     * Select a shift from the list of shifts
     * @return The selected shift or null if canceled
     */
    private Shift selectShift() {
        List<Shift> shifts = employeeService.getAllShifts();

        if (shifts.isEmpty()) {
            displayError("No shifts defined in the system");
            return null;
        }
        // Sort shifts by date
        shifts.sort((s1, s2) -> s2.getDate().compareTo(s1.getDate())); // Newest first
        // Build array of descriptions for display in menu
        String[] shiftDescriptions = new String[shifts.size()];
        for (int i = 0; i < shifts.size(); i++) {
            Shift shift = shifts.get(i);
            shiftDescriptions[i] = shift.getDate().format(dateFormatter) + " - " +
                    shift.getShiftType().toString();
        }
        int choice = displayMenu("Select Shift", shiftDescriptions);
        if (choice == 0) {
            return null; // User chose to go back
        }
        return shifts.get(choice - 1);
    }
    /**
     * Get a date input from the user
     * @param prompt The prompt to display
     * @return The entered date or null if invalid
     */
    private LocalDate getDateInput(String prompt) {
        try {
            String dateStr = getInput(prompt);
            return LocalDate.parse(dateStr, dateFormatter);
        } catch (Exception e) {
            displayError("Invalid date format. Please use DD/MM/YYYY");
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