
package Presentation;

import Service.EmployeeDTO;
import Service.EmployeeService;
import Service.ShiftDTO;
import Service.ShiftService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ShiftViewScreen provides the user interface for viewing both historical and future shifts.
 * It adapts its display based on the user's role, providing appropriate access levels.
 *
 * This screen allows:
 * - Managers to view complete shift history and details for all employees
 * - Regular employees to view only their own shift history and assignments
 * - All users to view future shifts with appropriate details based on role

 */
public class ShiftViewScreen extends BaseScreen {
    private final EmployeeService employeeService;
    private final ShiftService shiftService;
    private final DateTimeFormatter dateFormatter;
    private final EmployeeDTO loggedInEmployee;

    public ShiftViewScreen(EmployeeService employeeService, ShiftService shiftService, EmployeeDTO loggedInEmployee) {
        this.employeeService = employeeService;
        this.shiftService = shiftService;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.loggedInEmployee = loggedInEmployee;
    }

    @Override
    public void display() {
        // Check if the user is a manager or regular employee
        if (loggedInEmployee.isManager()) {
            displayManagerOptions();
        } else {
            displayEmployeeOptions();
        }
    }


    /**
     * Displays the menu options available to managers.
     * Managers can view all shifts and employee histories.
     */
    private void displayManagerOptions() {
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
     * Displays the menu options available to regular employees.
     * Regular employees can only view their own shift history.
     */
    private void displayEmployeeOptions() {
        String[] options = {
                "View My Shift History",
        };

        int choice;
        do {
            choice = displayMenu("My Shift History", options);

            switch (choice) {
                case 1:
                    displayMyShiftHistory();
                    break;
                case 0:
                    // Return to previous menu
                    break;
            }
        } while (choice != 0);
    }

    /**
     * Displays a list of all historical shifts sorted by date (newest first).
     * This option is only available to managers.
     */
    private void displayHistoricShifts() {
        displayTitle("Historic Shifts");
        List<ShiftDTO> historicShifts = shiftService.getHistoricalShifts();
        if (historicShifts.isEmpty()) {
            displayMessage("No historic shifts in the system");
            return;
        }
        // Sort shifts by date
        historicShifts.sort((s1, s2) -> s2.getDate().compareTo(s1.getDate())); // Newest first
        // Display shifts in list format
        for (ShiftDTO shift : historicShifts) {
            displayMessage(shift.getDate().format(dateFormatter) + " - " + shift.getShiftType() +
                    " (ID: " + shift.getId() + ")");
        }
    }

    /**
     * Displays shift history for a specific employee selected by the user.
     * This option is only available to managers.
     */
    private void displayEmployeeShiftHistory() {
        displayTitle("Employee Shift History");
        // Select employee
        EmployeeDTO employee = selectEmployee();
        if (employee == null) {
            return;
        }

        displayEmployeeShiftHistoryInfo(employee);
    }

    private void displayMyShiftHistory() {
        displayEmployeeShiftHistoryInfo(loggedInEmployee);
    }
    /**
     * Helper method that displays shift history information for a specific employee.
     * Shows all shifts the employee has been assigned to, including position.
     *
     * @param employee The employee whose shift history will be displayed
     */

    private void displayEmployeeShiftHistoryInfo(EmployeeDTO employee) {
        // Get shift history for employee
        List<ShiftDTO> employeeShifts = shiftService.getEmployeeShiftHistory(employee.getId());

        if (employeeShifts.isEmpty()) {
            displayMessage("No shift history found for employee: " + employee.getFullName());
            return;
        }
        // Sort shifts by date
        employeeShifts.sort((s1, s2) -> s2.getDate().compareTo(s1.getDate())); // Newest first
        displayTitle("Shift History for " + employee.getFullName());
        // Display shifts
        for (ShiftDTO shift : employeeShifts) {
            // Find position for this employee in this shift
            String position = "Unknown";
            for (Map.Entry<String, String> entry : shift.getAssignments().entrySet()) {
                if (entry.getValue().equals(employee.getFullName())) {
                    position = entry.getKey();
                    break;
                }
            }
            displayMessage(shift.getDate().format(dateFormatter) + " - " + shift.getShiftType() + " - Position: " + position);
        }
    }

    /**
     * Displays detailed information about a specific shift selected by the user.
     * This option is only available to managers.
     */
    private void displayShiftDetails() {
        displayTitle("Shift Details");
        // Select shift
        ShiftDTO shift = selectShift();
        if (shift == null) {
            return;
        }
        displayShiftDetailsFull(shift);
    }


    /**
     * Generates a report of shift history for a specific date range.
     * This option is only available to managers.
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
        List<ShiftDTO> allShifts = employeeService.getAllShiftsAsDTO();
        // Filter shifts for the period
        List<ShiftDTO> periodShifts = allShifts.stream()
                .filter(shift -> !shift.getDate().isBefore(startDate) && !shift.getDate().isAfter(endDate))
                .sorted(Comparator.comparing(ShiftDTO::getDate))
                .collect(Collectors.toList());

        if (periodShifts.isEmpty()) {
            displayMessage("No shifts found for the selected period");
            return;
        }

        displayTitle("Shift History Report: " + startDate.format(dateFormatter) + " to " + endDate.format(dateFormatter));
        // Count shifts by day
        Map<LocalDate, Long> shiftCountByDay = periodShifts.stream()
                .collect(Collectors.groupingBy(ShiftDTO::getDate, Collectors.counting()));

        // Display summary
        displayMessage("Total shifts in period: " + periodShifts.size());
        displayMessage("Shifts by date:");
        for (Map.Entry<LocalDate, Long> entry : shiftCountByDay.entrySet()) {
            displayMessage("- " + entry.getKey().format(dateFormatter) + ": " + entry.getValue() + " shifts");
        }

        // Ask if detailed report is wanted
        if (getBooleanInput("Display detailed shift information?")) {
            for (ShiftDTO shift : periodShifts) {
                displayShiftDetailsFull(shift);
            }
        }
    }


    /**
     * Helper method that displays comprehensive details about a shift.
     * Shows shift date, time, assigned employees, and manager information.
     *
     * @param shift The shift to display details for
     */
    private void displayShiftDetailsFull(ShiftDTO shift) {
        displayTitle("Shift Details: " + shift.getDate().format(dateFormatter) + " - " +
                shift.getShiftType());
        displayMessage("Shift ID: " + shift.getId());
        displayMessage("Date: " + shift.getDate().format(dateFormatter));
        displayMessage("Type: " + shift.getShiftType());
        displayMessage("Start Time: " + shift.getStartTime());
        displayMessage("End Time: " + shift.getEndTime());

        // Display shift manager
        if (shift.hasShiftManager()) {
            displayMessage("Shift Manager: " + shift.getShiftManagerName() + " (ID: " + shift.getShiftManagerId() + ")");
        } else {
            displayMessage("Shift Manager: Not assigned");
        }

        // Display assigned employees
        Map<String, String> assignments = shift.getAssignments();

        if (assignments.isEmpty()) {
            displayMessage("Assigned Employees: None");
        } else {
            displayMessage("\nAssigned Employees:");
            for (Map.Entry<String, String> entry : assignments.entrySet()) {
                String position = entry.getKey();
                String employee = entry.getValue();
                displayMessage("- " + position + ": " + employee);
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
     * Displays a menu for selecting a shift from those available in the system.
     *
     * @return The selected shift DTO, or null if selection was canceled
     */
    private ShiftDTO selectShift() {
        List<ShiftDTO> shifts = employeeService.getAllShiftsAsDTO();

        if (shifts.isEmpty()) {
            displayError("No shifts defined in the system");
            return null;
        }
        // Sort shifts by date
        shifts.sort((s1, s2) -> s2.getDate().compareTo(s1.getDate())); // Newest first
        // Build array of descriptions for display in menu
        String[] shiftDescriptions = new String[shifts.size()];
        for (int i = 0; i < shifts.size(); i++) {
            ShiftDTO shift = shifts.get(i);
            shiftDescriptions[i] = shift.getDate().format(dateFormatter) + " - " +
                    shift.getShiftType();
        }
        int choice = displayMenu("Select Shift", shiftDescriptions);
        if (choice == 0) {
            return null; // User chose to go back
        }
        return shifts.get(choice - 1);
    }


    /**
     * Helper method to get a date input from the user.
     * @param prompt The prompt to display to the user
     * @return The parsed LocalDate, or null if input was invalid
     */
    private LocalDate getDateInput(String prompt) {
        try {
            String dateStr = getInput(prompt);
            return LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            displayError("Invalid date format. Please use DD/MM/YYYY");
            return null;
        }
    }

    /**
     * Entry point for displaying shift history.
     * Shows different options based on user role.
     */
    public void displayShiftHistory() {
        if (loggedInEmployee.isManager()) {
            displayManagerOptions();
        } else {
            displayEmployeeOptions();
        }
    }

    /**
     * Displays future shifts for the current user.
     * Managers see all future shifts, while regular employees see only their own assignments.
     */
//    public void displayFutureShifts() {
//        displayTitle("Future Shifts");
//
//        List<ShiftDTO> futureShifts;
//
//        if (loggedInEmployee.isManager()) {
//            futureShifts = shiftService.getFutureShifts();
//        } else {
//            futureShifts = shiftService.getEmployeeFutureShifts(loggedInEmployee.getId());    // Regular employees see only their own
//        }
//
//        if (futureShifts.isEmpty()) {
//            displayMessage("No future shifts found.");
//            return;
//        }
//
//        futureShifts.sort(Comparator.comparing(ShiftDTO::getDate));
//
//        for (ShiftDTO shift : futureShifts) {
//            displayMessage(shift.getDate().format(dateFormatter) + " - " + shift.getShiftType());
//            displayMessage("Start Time: " + shift.getStartTime() + ", End Time: " + shift.getEndTime());
//
//            if (shift.hasShiftManager()) {
//                displayMessage("Shift Manager: " + shift.getShiftManagerName());
//            } else {
//                displayMessage("Shift Manager: Not assigned");
//            }
//
//            Map<String, String> assignments = shift.getAssignments();
//            if (assignments.isEmpty()) {
//                displayMessage("Assigned Employees: None");
//            } else {
//                displayMessage("Assigned Employees:");
//                for (Map.Entry<String, String> entry : assignments.entrySet()) {
//                    displayMessage("- " + entry.getKey() + ": " + entry.getValue());
//                }
//            }
//
//            displayMessage("------------------------------------");
//        }

    public void displayFutureShifts() {
        displayTitle("Future Shifts");

        // בדיקה אם יש מנהל משמרת זמין במערכת (רק SHIFT_MANAGER)
        boolean hasShiftManager = false;
        for (EmployeeDTO employee : employeeService.getAllEmployees()) {
            if (employee.isShiftManager()) { // בודק רק אם העובד הוא מנהל משמרת
                hasShiftManager = true;
                break;
            }
        }

        if (!hasShiftManager) {
            displayError("אין אפשרות לראות משמרות עתידיות: אין מנהל משמרת זמין במערכת.");
            return;
        }

        List<ShiftDTO> futureShifts;

        if (loggedInEmployee.isManager()) {
            futureShifts = shiftService.getFutureShifts();
        } else {
            futureShifts = shiftService.getEmployeeFutureShifts(loggedInEmployee.getId());
        }

        if (futureShifts.isEmpty()) {
            displayMessage("No future shifts found.");
            return;
        }

        futureShifts.sort(Comparator.comparing(ShiftDTO::getDate));

        for (ShiftDTO shift : futureShifts) {
            displayMessage(shift.getDate().format(dateFormatter) + " - " + shift.getShiftType());
            displayMessage("Start Time: " + shift.getStartTime() + ", End Time: " + shift.getEndTime());

            if (shift.hasShiftManager()) {
                displayMessage("Shift Manager: " + shift.getShiftManagerName());
            } else {
                displayMessage("Shift Manager: Not assigned");
            }

            Map<String, String> assignments = shift.getAssignments();
            if (assignments.isEmpty()) {
                displayMessage("Assigned Employees: None");
            } else {
                displayMessage("Assigned Employees:");
                for (Map.Entry<String, String> entry : assignments.entrySet()) {
                    displayMessage("- " + entry.getKey() + ": " + entry.getValue());
                }
            }

            displayMessage("------------------------------------");
        }
    }

}