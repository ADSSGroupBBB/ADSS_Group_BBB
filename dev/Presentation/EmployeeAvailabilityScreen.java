
package Presentation;

import Service.EmployeeDTO;
import Service.EmployeeService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;



/**
 * EmployeeAvailabilityScreen is responsible for managing the UI related to employee availability.
 * It provides different functionality depending on the user's role (manager or regular employee).
 *
 * This screen supports:
 * - Viewing availability for individual employees
 * - Updating availability for specific days and shift types
 * - Generating weekly availability reports
 * - Resetting employee availability
 */
// Screen for managing employee availability
public class EmployeeAvailabilityScreen extends BaseScreen {
    private final EmployeeService employeeService;
    private final EmployeeDTO loggedInEmployee;

    private static final String[] DAYS_IN_HEBREW = {
            "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    };
    // Array of DayOfWeek values
    /**
     * Array of DayOfWeek enum values in the same order as the day names
     */
    private static final DayOfWeek[] DAYS_OF_WEEK = {
            DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
    };

    /**
     * Constructs an EmployeeAvailabilityScreen with the employee service and logged-in user.
     *
     * @param employeeService The service for accessing employee data
     * @param loggedInEmployee The currently logged-in employee
     */
    public EmployeeAvailabilityScreen(EmployeeService employeeService, EmployeeDTO loggedInEmployee) {
        this.employeeService = employeeService;
        this.loggedInEmployee = loggedInEmployee;
    }


    /**
     * Displays the screen content. Shows different options depending on the user's role.
     * Managers have full access to all availability management features, while
     * regular employees can only view and update their own availability.
     */
    @Override
    public void display() {
        if (loggedInEmployee.isManager()) {
            displayManagerOptions();
        } else {
            displayEmployeeOptions();
        }
    }


    /**
     * Displays the menu options available to managers.
     * Managers can view and modify availability for all employees.
     */
    private void displayManagerOptions() {
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
     * Displays the menu options available to regular employees.
     * Regular employees can only view and update their own availability.
     */
    private void displayEmployeeOptions() {
        String[] options = {
                "Display My Availability",
                "Update My Availability"
        };

        int choice;
        do {
            choice = displayMenu("My Availability Management", options);

            switch (choice) {
                case 1:
                    displayMyAvailability();
                    break;
                case 2:
                    updateMyAvailability();
                    break;
                case 0:
                    // Return to previous menu
                    break;
            }
        } while (choice != 0);
    }

    /**
     * Displays availability for a specific employee selected by the user.
     * This option is only available to managers.
     */
    // Display availability for a specific employee
    private void displayEmployeeAvailability() {
        EmployeeDTO employee = selectEmployee();

        if (employee == null) {
            return; // User canceled
        }

        displayEmployeeAvailabilityInfo(employee);
    }


    /**
     * Displays the availability for the currently logged-in employee.
     * This option is available to all users.
     */
    // Display availability for the logged-in employee
    private void displayMyAvailability() {
        displayEmployeeAvailabilityInfo(loggedInEmployee);
    }

    private void displayEmployeeAvailabilityInfo(EmployeeDTO employee) {
        displayTitle("Employee Availability: " + employee.getFullName());

        // Display availability for each day of the week
        for (int i = 0; i < DAYS_OF_WEEK.length; i++) {
            DayOfWeek day = DAYS_OF_WEEK[i];
            String dayName = DAYS_IN_HEBREW[i];

            boolean morningAvailable = employeeService.isEmployeeAvailable(employee.getId(), day, "MORNING");
            boolean eveningAvailable = employeeService.isEmployeeAvailable(employee.getId(), day, "EVENING");

            String morningStatus = morningAvailable ? "Available" : "Unavailable";
            String eveningStatus = eveningAvailable ? "Available" : "Unavailable";

            displayMessage(dayName + ": Morning Shift - " + morningStatus + ", Evening Shift - " + eveningStatus);
        }
    }

    /**
     * Allows managers to update availability for a specific employee.
     * This option is only available to managers.
     */
    private void updateEmployeeAvailability() {
        EmployeeDTO employee = selectEmployee();
        if (employee == null) {
            return; // User canceled
        }
        updateEmployeeAvailabilityForEmployee(employee);
    }
    private void updateMyAvailability() {
        updateEmployeeAvailabilityForEmployee(loggedInEmployee);
    }


    /**
     * Helper method that updates availability for a specific employee.
     * Validates that updates are only allowed until Thursday for the next week's schedule.
     *
     * @param employee The employee whose availability will be updated
     */
    private void updateEmployeeAvailabilityForEmployee(EmployeeDTO employee) {
        displayTitle("Update Availability for: " + employee.getFullName());

        DayOfWeek today = LocalDate.now().getDayOfWeek();
        if (today == DayOfWeek.FRIDAY || today == DayOfWeek.SATURDAY) {
            displayError("You can only update next week's availability until Thursday.");
            return;
        }

        int dayIndex = selectDayOfWeek();
        if (dayIndex < 0) {
            return;
        }

        DayOfWeek selectedDay = DAYS_OF_WEEK[dayIndex];
        String dayName = DAYS_IN_HEBREW[dayIndex];
        displayTitle("Update Availability for " + dayName + " (Next Week)");

        boolean currentMorningAvailable = employeeService.isEmployeeAvailableForNextWeek(employee.getId(), selectedDay, "MORNING");
        boolean currentEveningAvailable = employeeService.isEmployeeAvailableForNextWeek(employee.getId(), selectedDay, "EVENING");

        displayMessage("Current availability: Morning - " + (currentMorningAvailable ? "Available" : "Unavailable")
                + ", Evening - " + (currentEveningAvailable ? "Available" : "Unavailable"));

        boolean morningAvailable = getBooleanInput("Is employee available for morning shift?");
        boolean eveningAvailable = getBooleanInput("Is employee available for evening shift?");

        boolean success = employeeService.updateEmployeeAvailabilityForNextWeek(
                employee.getId(), selectedDay, morningAvailable, eveningAvailable);

        if (success) {
            displayMessage("Employee availability updated successfully for next week");
        } else {
            displayError("Error updating availability");
        }
    }

    /**
     * Generates a weekly report showing which employees are available for each day and shift type.
     * This option is only available to managers.
     */
    private void generateWeeklyAvailabilityReport() {
        displayTitle("Weekly Availability Report");
        List<EmployeeDTO> employees = employeeService.getAllEmployees();

        if (employees.isEmpty()) {
            displayError("No employees in the system");
            return;
        }

        for (int i = 0; i < DAYS_OF_WEEK.length; i++) {
            DayOfWeek day = DAYS_OF_WEEK[i];
            String dayName = DAYS_IN_HEBREW[i];
            displayTitle(dayName);
            displayMessage("Morning Shift:");
            boolean foundMorning = false;
            for (EmployeeDTO employee : employees) {
                if (employeeService.isEmployeeAvailable(employee.getId(), day, "MORNING")) {
                    displayMessage("- " + employee.getFullName());
                    foundMorning = true;
                }
            }
            if (!foundMorning) {
                displayMessage("No available employees");
            }

            displayMessage("\nEvening Shift:");
            boolean foundEvening = false;
            for (EmployeeDTO employee : employees) {
                if (employeeService.isEmployeeAvailable(employee.getId(), day, "EVENING")) {
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
     * Resets an employee's availability to be available for all shifts on all days.
     * This option is only available to managers.
     */
    private void resetEmployeeAvailability() {
        EmployeeDTO employee = selectEmployee();

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
     * Displays a menu for selecting an employee from the system.
     * Filters out the admin user from the selection.
     *
     * @return The selected employee DTO, or null if selection was canceled
     */
    private EmployeeDTO selectEmployee() {
        displayTitle("Select Employee");

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
     * Displays a menu for selecting a day of the week.
     *
     * @return The index of the selected day in the DAYS_OF_WEEK array,
     *         or -1 if selection was canceled
     */
    private int selectDayOfWeek() {
        int choice = displayMenu("Select Day of Week", DAYS_IN_HEBREW);

        if (choice == 0) {
            return -1; // User chose to go back
        }

        return choice - 1; // -1 to skip 0
    }
}