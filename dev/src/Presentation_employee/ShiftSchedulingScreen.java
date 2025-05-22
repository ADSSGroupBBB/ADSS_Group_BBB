package Presentation_employee;

import Controller_employee.AssignmentController;
import Controller_employee.EmployeeController;
import Controller_employee.ShiftController;
import Service_employee.EmployeeDTO;
import Service_employee.PositionDTO;
import Service_employee.ShiftDTO;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * ShiftSchedulingScreen provides the user interface for managing and scheduling shifts.
 */
public class ShiftSchedulingScreen extends BaseScreen {
    private final EmployeeController employeeController;
    private final ShiftController shiftController;
    private final AssignmentController assignmentController;
    private final DateTimeFormatter dateFormatter;
    private final EmployeeDTO loggedInEmployee;

    /**
     * Constructor that takes controllers and logged-in employee for permission checking.
     */
    public ShiftSchedulingScreen(EmployeeController employeeController, ShiftController shiftController,
                                 AssignmentController assignmentController, EmployeeDTO loggedInEmployee) {
        this.employeeController = employeeController;
        this.shiftController = shiftController;
        this.assignmentController = assignmentController;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.loggedInEmployee = loggedInEmployee;
    }

    /**
     * Displays the shift scheduling screen if the user has appropriate permissions.
     */
    @Override
    public void display() {
        // Permission check - only managers can access this screen
        if (!loggedInEmployee.isManager()) {
            displayError("Access denied. Only managers can access this functionality.");
            return;
        }

        String[] options = {
                "Create Shifts for Week",
                "View Future Shifts",
                "Assign Employee to Shift",
                "Remove Employee from Shift",
                "View Missing Positions in Shifts",
                "Manage Shift Hours"
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
                    viewMissingPositions();
                    break;
                case 6:
                    manageShiftHours();
                    break;
                case 0:
                    // Return to previous menu
                    break;
            }
        } while (choice != 0);
    }

    /**
     * Creates shifts for an entire week starting from a specified Sunday date.
     */
    private void createShiftsForWeek() {
        displayTitle("Create Shifts for Week");
        LocalDate startDate = null;


        List<PositionDTO> managerPositions = employeeController.getAllPositions().stream().filter(PositionDTO::isRequiresShiftManager).collect(java.util.stream.Collectors.toList());

        if (managerPositions.isEmpty()) {
            displayError("Cannot create shifts: No shift manager positions defined in the system.");
            displayError("Please create a shift manager position first.");
            return;
        }


        boolean hasQualifiedManagers = false;
        for (PositionDTO position : managerPositions) {
            List<EmployeeDTO> qualifiedEmployees = employeeController.getQualifiedEmployeesForPosition(position.getName());
            if (!qualifiedEmployees.isEmpty()) {
                hasQualifiedManagers = true;
                break;
            }
        }

        if (!hasQualifiedManagers) {
            displayError("Cannot create shifts: No employees are qualified for shift manager positions.");
            displayError("Please assign shift manager qualification to at least one employee first.");
            return;
        }

        while (startDate == null) {
            try {
                String dateStr = getInput("Enter start date (Sunday) in format DD/MM/YYYY");
                startDate = LocalDate.parse(dateStr, dateFormatter);
                if (startDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    displayError("The date must be a Sunday");
                    startDate = null;
                }
            } catch (DateTimeParseException e) {
                displayError("Invalid date format. Please use DD/MM/YYYY");
            }
        }

        // Create shifts for the week
        List<ShiftDTO> createdShifts = shiftController.createShiftsForWeek(startDate);

        if (createdShifts.isEmpty()) {
            displayError("No shifts were created. This may be because:");
            displayError("1. Shifts already exist for this week");
            displayError("2. No shift managers are available for the required shifts");
        } else {
            displayMessage(createdShifts.size() + " shifts were created successfully for week starting " + startDate.format(dateFormatter));
            displayMessage("Shift managers have been automatically assigned to each shift.");
        }
    }

    /**
     * Displays a list of future shifts sorted by date.
     */
    private void viewFutureShifts() {
        displayTitle("Future Shifts");
        List<ShiftDTO> futureShifts = shiftController.getFutureShifts();
        if (futureShifts.isEmpty()) {
            displayMessage("No future shifts in the system");
            return;
        }
        // Sort shifts by date
        futureShifts.sort(Comparator.comparing(ShiftDTO::getDate));
        for (ShiftDTO shift : futureShifts) {
            String hasManager = shift.hasShiftManager() ? "Yes" : "No";
            displayMessage(shift.getDate().format(dateFormatter) + " - " + shift.getShiftType() + " (Has Manager: " + hasManager + ", ID: " + shift.getId() + ")");
        }
    }

    /**
     * Assigns an employee to a specific position in a shift.
     */
    private void assignEmployeeToShift() {
        displayTitle("Assign Employee to Shift");

        // Select shift
        ShiftDTO shift = selectFutureShift();
        if (shift == null) return;

        displayShiftDetails(shift);

        // Select position
        PositionDTO position = selectPosition();
        if (position == null) return;

        // Find eligible employees (qualified and available)
        List<EmployeeDTO> qualifiedEmployees = employeeController.getQualifiedEmployeesForPosition(position.getName());
        List<EmployeeDTO> availableEmployees = assignmentController.getAvailableEmployeesForShift(shift.getDate(), shift.getShiftType());

        List<EmployeeDTO> eligibleEmployees = new ArrayList<>();
        for (EmployeeDTO employee : qualifiedEmployees) {
            if (availableEmployees.stream().anyMatch(e -> e.getId().equals(employee.getId()))) {
                eligibleEmployees.add(employee);
            }
        }

        if (eligibleEmployees.isEmpty()) {
            displayError("No eligible employees for this position and shift");
            return;
        }

        // Select employee
        EmployeeDTO employee = selectEmployeeFromList(eligibleEmployees);
        if (employee == null) return;

        // Check if employee is already assigned
        if (assignmentController.isEmployeeAlreadyAssignedToShift(shift.getId(), employee.getId())) {
            displayError("This employee is already assigned to this shift.");
            return;
        }

        // Try to assign employee
        boolean success = assignmentController.assignEmployeeToShift(shift.getId(), employee.getId(), position.getName());

        if (success) {
            displayMessage("Employee successfully assigned to shift");
            if (assignmentController.areAllRequiredPositionsCovered(shift.getId())) {
                displayMessage("All required positions for this shift are now covered");
            }
        } else {// Check the specific reason for failure
            int requiredCount = assignmentController.getRequiredPositionsCount(shift.getShiftType(), position.getName());
            if (requiredCount == 0) {
                displayError("Cannot assign employee: No required positions defined for '" + position.getName() + "' in this shift.");
            } else {
                displayError("Failed to assign employee. Possible reasons: all positions are filled, lack of qualification, or availability issues.");
            }
        }
    }

    /**
     * Removes an employee assignment from a shift.
     */
    private void removeEmployeeFromShift() {
        displayTitle("Remove Employee from Shift");

        // Select shift
        ShiftDTO shift = selectFutureShift();
        if (shift == null) {
            return;
        }

        // Display shift details
        displayShiftDetails(shift);

        Map<String, String> assignments = shift.getAssignments();
        if (assignments.isEmpty()) {
            displayError("No employees assigned to this shift");
            return;
        }

        // Build list of assigned positions
        List<String> assignedPositions = new ArrayList<>(assignments.keySet());
        String[] positionNames = new String[assignedPositions.size()];
        for (int i = 0; i < assignedPositions.size(); i++) {
            String position = assignedPositions.get(i);
            String employee = assignments.get(position);
            positionNames[i] = position + ": " + employee;
        }

        int positionIndex = displayMenu("Select assignment to remove", positionNames);
        if (positionIndex == 0) {
            return;
        }

        String selectedPosition = assignedPositions.get(positionIndex - 1);


        PositionDTO positionDTO = employeeController.getPositionDetails(selectedPosition);
        if (positionDTO != null && positionDTO.isRequiresShiftManager()) {
            displayError("Cannot remove a shift manager from the shift.");
            displayError("Every shift must have a shift manager assigned.");
            return;
        }

        // Confirm removal
        String employee = assignments.get(selectedPosition);

        if (getBooleanInput("Are you sure you want to remove " +
                employee + " from position " + selectedPosition + "?")) {
            boolean success = assignmentController.removeAssignmentFromShift(shift.getId(), selectedPosition);

            if (success) {
                displayMessage("Assignment successfully removed");
            } else {
                displayError("Error removing assignment");
            }
        }
    }

    /**
     * Displays a list of missing positions for future shifts.
     */
    private void viewMissingPositions() {
        displayTitle("Missing Positions in Future Shifts");
        List<ShiftDTO> futureShifts = shiftController.getFutureShifts();
        if (futureShifts.isEmpty()) {
            displayMessage("No future shifts in the system");
            return;
        }
        boolean foundMissing = false;
        for (ShiftDTO shift : futureShifts) {
            List<PositionDTO> missingPositions = shiftController.getMissingPositionsForShift(shift.getId());
            if (!missingPositions.isEmpty()) {
                foundMissing = true;
                displayTitle(shift.getDate().format(dateFormatter) + " - " + shift.getShiftType());
                for (PositionDTO position : missingPositions) {
                    displayMessage("- " + position.getName());
                }
            }
        }
        if (!foundMissing) {
            displayMessage("No missing positions in future shifts");
        }
    }

    /**
     * Displays detailed information about a shift.
     */
    private void displayShiftDetails(ShiftDTO shift) {
        displayTitle("Shift Details: " + shift.getDate().format(dateFormatter) + " - " + shift.getShiftType());

        displayMessage("Shift ID: " + shift.getId());
        displayMessage("Start Time: " + shift.getStartTime());
        displayMessage("End Time: " + shift.getEndTime());

        if (shift.hasShiftManager()) {
            displayMessage("Shift Manager: " + shift.getShiftManagerName());
        } else {
            displayMessage("Shift Manager: Not assigned");
        }

        // Display assigned employees:
        Map<String, String> assignments = shift.getAssignments();

        if (assignments.isEmpty()) {
            displayMessage("Assigned Employees: None");
        } else {
            displayMessage("\nAssigned Employees:");

            // For future shifts, always show all assignments
            if (!shift.getDate().isBefore(LocalDate.now())) {
                for (Map.Entry<String, String> entry : assignments.entrySet()) {
                    displayMessage("- " + entry.getKey() + ": " + entry.getValue());
                }
            }
            // For historical shifts, display based on user role
            else {
                if (loggedInEmployee.isManager()) {
                    for (Map.Entry<String, String> entry : assignments.entrySet()) {
                        displayMessage("- " + entry.getKey() + ": " + entry.getValue());
                    }
                } else {
                    // Regular employees see only themselves
                    boolean found = false;
                    for (Map.Entry<String, String> entry : assignments.entrySet()) {
                        if (entry.getValue().equals(loggedInEmployee.getFullName())) {
                            displayMessage("- " + entry.getKey() + ": " + entry.getValue());
                            found = true;
                        }
                    }
                    if (!found) {
                        displayMessage("You were not assigned to this shift.");
                    }
                }
            }
        }

        // Display missing positions (for managers only)
        if (loggedInEmployee.isManager()) {
            List<PositionDTO> missingPositions = shiftController.getMissingPositionsForShift(shift.getId());
            if (!missingPositions.isEmpty()) {
                displayMessage("\nMissing Positions:");
                for (PositionDTO position : missingPositions) {
                    displayMessage("- " + position.getName());
                }
            }
        }
    }

    /**
     * Displays a menu for selecting a future shift from those available in the system.
     */
    private ShiftDTO selectFutureShift() {
        List<ShiftDTO> futureShifts = shiftController.getFutureShifts();
        if (futureShifts.isEmpty()) {
            displayError("No future shifts in the system");
            return null;
        }
        // Sort shifts by date
        futureShifts.sort(Comparator.comparing(ShiftDTO::getDate));
        // Build array of descriptions for display in menu
        String[] shiftDescriptions = new String[futureShifts.size()];
        for (int i = 0; i < futureShifts.size(); i++) {
            ShiftDTO shift = futureShifts.get(i);
            shiftDescriptions[i] = shift.getDate().format(dateFormatter) + " - " + shift.getShiftType();
        }
        int choice = displayMenu("Select Shift", shiftDescriptions);
        if (choice == 0) {
            return null; // User chose to go back
        }
        return futureShifts.get(choice - 1);
    }

    /**
     * Displays a menu for selecting a position from those available in the system.
     */
    private PositionDTO selectPosition() {
        List<PositionDTO> positions = employeeController.getAllPositions();
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

    /**
     * Displays a menu for selecting an employee from a provided list.
     */
    private EmployeeDTO selectEmployeeFromList(List<EmployeeDTO> employees) {
        if (employees.isEmpty()) {
            displayError("No employees to select from");
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
     * Manages the configuration of shift hours.
     */
    private void manageShiftHours() {
        displayTitle("Manage Shift Hours");

        String[] shiftTypes = { "Morning Shift", "Evening Shift" };
        int choice = displayMenu("Select Shift Type to Update", shiftTypes);

        if (choice == 0) return;

        String shiftTypeStr = (choice == 1) ? "MORNING" : "EVENING";

        String newStart = getInput("Enter new start time (HH:mm)");
        String newEnd = getInput("Enter new end time (HH:mm)");

        boolean success = employeeController.updateShiftHours(shiftTypeStr, newStart, newEnd);

        if (success) {
            displayMessage("Shift hours updated successfully for " + shiftTypes[choice - 1]);
        } else {
            displayError("Failed to update shift hours. Please check the time format (HH:mm).");
        }
    }
}