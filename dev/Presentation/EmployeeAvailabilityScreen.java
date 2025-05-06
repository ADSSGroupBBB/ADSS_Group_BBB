package Presentation;

import Controller.EmployeeController;
import Service.EmployeeDTO;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * EmployeeAvailabilityScreen is responsible for managing the UI related to employee availability.
 */
public class EmployeeAvailabilityScreen extends BaseScreen {
    private final EmployeeController employeeController;
    private final EmployeeDTO loggedInEmployee;

    private static final String[] DAYS_IN_HEBREW = {
            "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    };
    // Array of DayOfWeek values
    private static final DayOfWeek[] DAYS_OF_WEEK = {
            DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
    };

    /**
     * Constructs an EmployeeAvailabilityScreen with the employee controller and logged-in user.
     */
    public EmployeeAvailabilityScreen(EmployeeController employeeController, EmployeeDTO loggedInEmployee) {
        this.employeeController = employeeController;
        this.loggedInEmployee = loggedInEmployee;
    }

    @Override
    public void display() {
        if (loggedInEmployee.isManager()) {
            displayManagerOptions();
        } else {
            displayEmployeeOptions();
        }
    }

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
                case 0:      // Return to previous menu
                    break;
            }
        } while (choice != 0);
    }

    private void displayEmployeeAvailability() {
        EmployeeDTO employee = selectEmployee();

        if (employee == null) {
            return; // User canceled
        }

        displayEmployeeAvailabilityInfo(employee);
    }

    private void displayMyAvailability() {
        displayEmployeeAvailabilityInfo(loggedInEmployee);
    }

    private void displayEmployeeAvailabilityInfo(EmployeeDTO employee) {
        displayTitle("Employee Availability: " + employee.getFullName());

        // Display availability for each day of the week
        for (int i = 0; i < DAYS_OF_WEEK.length; i++) {
            DayOfWeek day = DAYS_OF_WEEK[i];
            String dayName = DAYS_IN_HEBREW[i];

            boolean morningAvailable = employeeController.isEmployeeAvailable(employee.getId(), day, "MORNING");
            boolean eveningAvailable = employeeController.isEmployeeAvailable(employee.getId(), day, "EVENING");

            String morningStatus = morningAvailable ? "Available" : "Unavailable";
            String eveningStatus = eveningAvailable ? "Available" : "Unavailable";

            displayMessage(dayName + ": Morning Shift - " + morningStatus + ", Evening Shift - " + eveningStatus);
        }
    }

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

        boolean currentMorningAvailable = employeeController.isEmployeeAvailableForNextWeek(employee.getId(), selectedDay, "MORNING");
        boolean currentEveningAvailable = employeeController.isEmployeeAvailableForNextWeek(employee.getId(), selectedDay, "EVENING");

        displayMessage("Current availability: Morning - " + (currentMorningAvailable ? "Available" : "Unavailable")
                + ", Evening - " + (currentEveningAvailable ? "Available" : "Unavailable"));

        boolean morningAvailable = getBooleanInput("Is employee available for morning shift?");
        boolean eveningAvailable = getBooleanInput("Is employee available for evening shift?");

        boolean success = employeeController.updateEmployeeAvailabilityForNextWeek(
                employee.getId(), selectedDay, morningAvailable, eveningAvailable);

        if (success) {
            displayMessage("Employee availability updated successfully for next week");
        } else {
            displayError("Error updating availability");
        }
    }

    private void generateWeeklyAvailabilityReport() {
        displayTitle("Weekly Availability Report");
        List<EmployeeDTO> employees = employeeController.getAllEmployees();

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
                if (employeeController.isEmployeeAvailable(employee.getId(), day, "MORNING")) {
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
                if (employeeController.isEmployeeAvailable(employee.getId(), day, "EVENING")) {
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
                boolean result = employeeController.updateEmployeeAvailability(
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

    private EmployeeDTO selectEmployee() {
        displayTitle("Select Employee");

        List<EmployeeDTO> employees = employeeController.getAllEmployees().stream()
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

    private int selectDayOfWeek() {
        int choice = displayMenu("Select Day of Week", DAYS_IN_HEBREW);

        if (choice == 0) {
            return -1; // User chose to go back
        }

        return choice - 1; // -1 to skip 0
    }
}