package Presentation;

import Domain.Employee;
import Domain.ShiftType;
import Service.EmployeeService;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Scanner;

/**
 * Screen for managing employee availability
 */
public class EmployeeAvailabilityScreen {
    private final EmployeeService employeeService;
    private final Scanner scanner;

    private static final String[] DAYS_IN_HEBREW = {
            "Sunday", "Monday", "Tuesday", "Wednesday", "Wednesday", "Friday", "Saturday"
    };

    // Array of DayOfWeek values
    private static final DayOfWeek[] DAYS_OF_WEEK = {
            DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
    };

    /**
     * Constructor - receives employee service as dependency
     * @param employeeService The employee service
     */
    public EmployeeAvailabilityScreen(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Display the main availability management screen
     */
    public void display() {
        String[] options = {
                "Display Employee Availability",
                "Update Employee Availability",
                "Generate Weekly Availability Report",
                "Reset Employee Availability"
        };

        int choice;
        do {
            choice = displayMenu("Employee Availability Management", options);

            switch (choice) {
                case 1:
                    displayEmployeeAvailability();
                    break;
                case 2:
                    updateEmployeeAvailability();
                    break;
                case 3:
                    generateWeeklyAvailabilityReport();
                    break;
                case 4:
                    resetEmployeeAvailability();
                    break;
                case 0:
                    // Return to previous menu
                    break;
            }
        } while (choice != 0);
    }

    /**
     * Display availability for a specific employee
     */
    private void displayEmployeeAvailability() {
        Employee employee = selectEmployee();

        if (employee == null) {
            return; // User canceled
        }

        displayTitle("Employee Availability: " + employee.getFullName());

        // Display availability for each day of the week
        for (int i = 0; i < DAYS_OF_WEEK.length; i++) {
            DayOfWeek day = DAYS_OF_WEEK[i];
            String dayName = DAYS_IN_HEBREW[i];

            boolean morningAvailable = employee.getAvailability().isAvailable(day, ShiftType.MORNING);
            boolean eveningAvailable = employee.getAvailability().isAvailable(day, ShiftType.EVENING);

            String morningStatus = morningAvailable ? "Available" : "Unavailable";
            String eveningStatus = eveningAvailable ? "Available" : "Unavailable";

            displayMessage(dayName + ": Morning Shift - " + morningStatus + ", Evening Shift - " + eveningStatus);
        }
    }

    /**
     * Update availability for an employee
     */
    private void updateEmployeeAvailability() {
        Employee employee = selectEmployee();

        if (employee == null) {
            return; // User canceled
        }

        displayTitle("Update Availability for: " + employee.getFullName());

        // Select day to update
        int dayIndex = selectDayOfWeek();

        if (dayIndex < 0) {
            return; // User canceled
        }

        DayOfWeek selectedDay = DAYS_OF_WEEK[dayIndex];
        String dayName = DAYS_IN_HEBREW[dayIndex];

        displayTitle("Update Availability for " + dayName);

        // Get current availability for display
        boolean currentMorningAvailable = employee.getAvailability().isAvailable(selectedDay, ShiftType.MORNING);
        boolean currentEveningAvailable = employee.getAvailability().isAvailable(selectedDay, ShiftType.EVENING);

        displayMessage("Current availability: Morning - " + (currentMorningAvailable ? "Available" : "Unavailable") +
                ", Evening - " + (currentEveningAvailable ? "Available" : "Unavailable"));

        // Get new availability from user
        boolean morningAvailable = getBooleanInput("Is employee available for morning shift?");
        boolean eveningAvailable = getBooleanInput("Is employee available for evening shift?");

        // Update availability through employee service
        boolean success = employeeService.updateEmployeeAvailability(
                employee.getId(), selectedDay, morningAvailable, eveningAvailable);

        if (success) {
            displayMessage("Employee availability updated successfully");
        } else {
            displayError("Error updating employee availability");
        }
    }

    /**
     * Generate weekly availability report for all employees
     */
    private void generateWeeklyAvailabilityReport() {
        displayTitle("Weekly Availability Report");

        // Get all employees from the system
        List<Employee> employees = employeeService.getAllEmployees();

        if (employees.isEmpty()) {
            displayError("No employees in the system");
            return;
        }

        // For each day of the week, display available employees for each shift
        for (int i = 0; i < DAYS_OF_WEEK.length; i++) {
            DayOfWeek day = DAYS_OF_WEEK[i];
            String dayName = DAYS_IN_HEBREW[i];

            displayTitle(dayName);

            displayMessage("Morning Shift:");
            boolean foundMorning = false;
            for (Employee employee : employees) {
                if (employee.getAvailability().isAvailable(day, ShiftType.MORNING)) {
                    displayMessage("- " + employee.getFullName());
                    foundMorning = true;
                }
            }
            if (!foundMorning) {
                displayMessage("No available employees");
            }

            displayMessage("\nEvening Shift:");
            boolean foundEvening = false;
            for (Employee employee : employees) {
                if (employee.getAvailability().isAvailable(day, ShiftType.EVENING)) {
                    displayMessage("- " + employee.getFullName());
                    foundEvening = true;
                }
            }
            if (!foundEvening) {
                displayMessage("No available employees");
            }

            displayMessage(""); // Empty line between days
        }
    }

    /**
     * Reset employee availability to default (available for all shifts)
     */
    private void resetEmployeeAvailability() {
        Employee employee = selectEmployee();

        if (employee == null) {
            return; // User canceled
        }

        displayTitle("Reset Availability for: " + employee.getFullName());

        if (getBooleanInput("Are you sure you want to reset employee availability? (Employee will be available for all shifts)")) {
            // Reset availability for each day of the week
            boolean success = true;
            for (DayOfWeek day : DAYS_OF_WEEK) {
                boolean result = employeeService.updateEmployeeAvailability(
                        employee.getId(), day, true, true);
                if (!result) {
                    success = false;
                }
            }

            if (success) {
                displayMessage("Employee availability was reset successfully");
            } else {
                displayError("Error resetting employee availability");
            }
        }
    }

    /**
     * Select an employee from the list of employees
     * @return The selected employee or null if canceled
     */
    private Employee selectEmployee() {
        displayTitle("Select Employee");

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
     * Select a day of the week
     * @return Index of the selected day or -1 if canceled
     */
    private int selectDayOfWeek() {
        int choice = displayMenu("Select Day of Week", DAYS_IN_HEBREW);

        if (choice == 0) {
            return -1; // User chose to go back
        }

        return choice - 1;
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