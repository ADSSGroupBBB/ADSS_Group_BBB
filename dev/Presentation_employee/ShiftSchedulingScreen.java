package Presentation_employee;

import dto.BranchDTO;
import dto.EmployeeDTO;
import dto.PositionDTO;
import dto.ShiftDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Updated ShiftSchedulingScreen with branch support.
 * Provides the user interface for managing and scheduling shifts.
 */
public class ShiftSchedulingScreen extends BaseScreen {
    private final NavigationManager navigationManager;
    private final DateTimeFormatter dateFormatter;
    private final EmployeeDTO loggedInEmployee;

    /**
     * Constructor that takes navigation manager and logged-in employee for permission checking.
     */
    public ShiftSchedulingScreen(NavigationManager navigationManager, EmployeeDTO loggedInEmployee) {
        this.navigationManager = navigationManager;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.loggedInEmployee = loggedInEmployee;
    }

    /**
     * Displays the shift scheduling screen if the user has appropriate permissions.
     */

    public void display() {
        // Permission check - only managers can access this screen
        if (!loggedInEmployee.isManager()) {
            displayError("Access denied. Only managers can access this functionality.");
            return;
        }

        String[] options = {
                "Create Shifts for Week by Branch",
                "View Future Shifts",
                "View Future Shifts by Branch",
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
                    createShiftsForWeekByBranch();
                    break;
                case 2:
                    viewFutureShifts();
                    break;
                case 3:
                    viewFutureShiftsByBranch();
                    break;
                case 4:
                    assignEmployeeToShift();
                    break;
                case 5:
                    removeEmployeeFromShift();
                    break;
                case 6:
                    viewMissingPositions();
                    break;
                case 7:
                    manageShiftHours();
                    break;
                case 0:
                    // Return to previous menu
                    break;
            }
        } while (choice != 0);
    }


    /**
     * Creates shifts for a specific branch - הצגה נקייה של תוצאות
     */
    private void createShiftsForWeekByBranch() {
        displayTitle("Create Shifts for Week by Branch");

        BranchDTO selectedBranch = selectBranch();
        if (selectedBranch == null) return;

        LocalDate startDate = getStartDate();
        if (startDate == null) return;

        if (!validateManagerPositions()) return;

        // Create shifts for specific branch
        List<ShiftDTO> createdShifts = navigationManager.getShiftService()
                .createShiftsForWeek(startDate, selectedBranch.getAddress());
        if (createdShifts.isEmpty()) {
            displayError("No shifts were created for branch " + selectedBranch.getAddress());
            displayError("Reason: No available shift managers found for any of the requested shifts");
        } else {
            displayMessage("✓ " + createdShifts.size() + " shifts were created successfully for branch " +
                    selectedBranch.getAddress() + " for week starting " + startDate.format(dateFormatter));
        }
    }

    /**
     * Displays a list of future shifts sorted by date.
     */
    private void viewFutureShifts() {
        displayTitle("Future Shifts (All Branches)");
        List<ShiftDTO> futureShifts = navigationManager.getShiftService().getFutureShifts();

        if (futureShifts.isEmpty()) {
            displayMessage("No future shifts in the system");
            return;
        }

        displayShiftsList(futureShifts);
    }

    /**
     * Displays future shifts filtered by branch.
     */
    private void viewFutureShiftsByBranch() {
        displayTitle("Future Shifts by Branch");

        BranchDTO selectedBranch = selectBranch();
        if (selectedBranch == null) return;

        List<ShiftDTO> futureShifts = navigationManager.getShiftService()
                .getFutureShiftsByBranch(selectedBranch.getAddress());

        if (futureShifts.isEmpty()) {
            displayMessage("No future shifts found for branch: " + selectedBranch.getAddress());
            return;
        }

        displayTitle("Future Shifts at " + selectedBranch.getAddress());
        displayShiftsList(futureShifts);
    }

    /**
     * Helper method to display a list of shifts.
     */

    private void displayShiftsList(List<ShiftDTO> shifts) {
        List<ShiftDTO> mutableShifts = new ArrayList<>(shifts);

        // Sort shifts by date AND shift type for consistent ordering
        mutableShifts.sort(Comparator
                .comparing(ShiftDTO::getDate)
                .thenComparing(ShiftDTO::getShiftType));

        for (ShiftDTO shift : mutableShifts) {
            String hasManager = shift.hasShiftManager() ? "Yes" : "No";
            String branchInfo = shift.hasBranch() ? " at " + shift.getBranchAddress() : "";
            displayMessage(shift.getDate().format(dateFormatter) + " - " + shift.getShiftType() +
                    branchInfo + " (Has Manager: " + hasManager + ", ID: " + shift.getId() + ")");
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

        // Find eligible employees based on shift branch
        List<EmployeeDTO> eligibleEmployees = getEligibleEmployeesForShift(shift, position.getName());

        if (eligibleEmployees.isEmpty()) {
            displayError("No eligible employees for this position and shift");
            displayMessage("Consider:");
            displayMessage("1. Adding qualifications to employees");
            displayMessage("2. Updating employee availability");
            displayMessage("3. Assigning employees to the shift's branch");
            return;
        }

        // Select employee
        EmployeeDTO employee = selectEmployeeFromList(eligibleEmployees, shift.getBranchAddress());
        if (employee == null) return;

        // Check if employee is already assigned
        if (navigationManager.getAssignmentService().isEmployeeAlreadyAssignedToShift(shift.getId(), employee.getId())) {
            displayError("This employee is already assigned to this shift.");
            return;
        }

        // Try to assign employee
        boolean success = navigationManager.getAssignmentService()
                .assignEmployeeToShift(shift.getId(), employee.getId(), position.getName());

        if (success) {
            displayMessage("Employee successfully assigned to shift");
            if (navigationManager.getAssignmentService().areAllRequiredPositionsCovered(shift.getId())) {
                displayMessage("All required positions for this shift are now covered");
            }
        } else {
            int requiredCount = navigationManager.getAssignmentService()
                    .getRequiredPositionsCount(shift.getShiftType(), position.getName());
            if (requiredCount == 0) {
                displayError("Cannot assign employee: No required positions defined for '" +
                        position.getName() + "' in this shift.");
            } else {
                displayError("Failed to assign employee. Possible reasons: all positions are filled, " +
                        "lack of qualification, or availability issues.");
            }
        }
    }

    /**
     * Gets eligible employees for a shift considering branch assignment.
     */
    private List<EmployeeDTO> getEligibleEmployeesForShift(ShiftDTO shift, String positionName) {

        // Shift has specific branch - get employees for that branch
        return navigationManager.getAssignmentService()
                .getQualifiedAndAvailableEmployees(shift.getDate(), shift.getShiftType(), positionName);
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

        PositionDTO positionDTO = navigationManager.getPositionService().getPosition(selectedPosition);
        if (positionDTO != null && positionDTO.isRequiresShiftManager()) {
            displayError("Cannot remove a shift manager from the shift.");
            displayError("Every shift must have a shift manager assigned.");
            return;
        }

        // Confirm removal
        String employee = assignments.get(selectedPosition);

        if (getBooleanInput("Are you sure you want to remove " +
                employee + " from position " + selectedPosition + "?")) {
            boolean success = navigationManager.getAssignmentService()
                    .removeAssignmentFromShift(shift.getId(), selectedPosition);

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

        // Option to view by branch or all
        String[] options = {"All Branches", "Specific Branch"};
        int viewChoice = displayMenu("View Missing Positions", options);

        if (viewChoice == 0) {
            return;
        }

        List<ShiftDTO> futureShifts;
        String title;

        if (viewChoice == 1) {
            futureShifts = navigationManager.getShiftService().getFutureShifts();
            title = "Missing Positions in All Future Shifts";
        } else {
            BranchDTO selectedBranch = selectBranch();
            if (selectedBranch == null) return;

            futureShifts = navigationManager.getShiftService()
                    .getFutureShiftsByBranch(selectedBranch.getAddress());
            title = "Missing Positions at " + selectedBranch.getAddress();
        }

        if (futureShifts.isEmpty()) {
            displayMessage("No future shifts found");
            return;
        }

        displayTitle(title);
        boolean foundMissing = false;

        for (ShiftDTO shift : futureShifts) {
            List<PositionDTO> missingPositions = navigationManager.getShiftService()
                    .getMissingPositionsForShift(shift.getId());
            if (!missingPositions.isEmpty()) {
                foundMissing = true;
                String shiftTitle = shift.getDate().format(dateFormatter) + " - " + shift.getShiftType();
                if (shift.hasBranch()) {
                    shiftTitle += " at " + shift.getBranchAddress();
                }
                displayTitle(shiftTitle);
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
        String shiftTitle = "Shift Details: " + shift.getDate().format(dateFormatter) + " - " + shift.getShiftType();
        if (shift.hasBranch()) {
            shiftTitle += " at " + shift.getBranchAddress();
        }
        displayTitle(shiftTitle);

        displayMessage("Shift ID: " + shift.getId());
        displayMessage("Start Time: " + shift.getStartTime());
        displayMessage("End Time: " + shift.getEndTime());

        if (shift.hasBranch()) {
            displayMessage("Branch: " + shift.getBranchAddress());
        }

        if (shift.hasShiftManager()) {
            displayMessage("Shift Manager: " + shift.getShiftManagerName());
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
                displayMessage("- " + entry.getKey() + ": " + entry.getValue());
            }
        }

        // Display missing positions (for managers only)
        if (loggedInEmployee.isManager()) {
            List<PositionDTO> missingPositions = navigationManager.getShiftService()
                    .getMissingPositionsForShift(shift.getId());
            if (!missingPositions.isEmpty()) {
                displayMessage("\nMissing Positions:");
                for (PositionDTO position : missingPositions) {
                    displayMessage("- " + position.getName());
                }
            }
        }
    }

    /**
     * Helper method to get start date from user input.
     */
    public LocalDate getStartDate() {
        LocalDate startDate = null;
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
        return startDate;
    }

    /**
     * Validates that manager positions exist.
     */
    private boolean validateManagerPositions() {
        List<PositionDTO> managerPositions = navigationManager.getPositionService().getAllPositions().stream()
                .filter(PositionDTO::isRequiresShiftManager)
                .toList();

        if (managerPositions.isEmpty()) {
            displayError("Cannot create shifts: No shift manager positions defined in the system.");
            displayError("Please create a shift manager position first.");
            return false;
        }

        boolean hasQualifiedManagers = false;
        for (PositionDTO position : managerPositions) {
            List<EmployeeDTO> qualifiedEmployees = navigationManager.getPositionService()
                    .getQualifiedEmployeesForPosition(position.getName());
            if (!qualifiedEmployees.isEmpty()) {
                hasQualifiedManagers = true;
                break;
            }
        }

        if (!hasQualifiedManagers) {
            displayError("Cannot create shifts: No employees are qualified for shift manager positions.");
            displayError("Please assign shift manager qualification to at least one employee first.");
            return false;
        }

        return true;
    }

    /**
     * Helper method to select a branch.
     */
    private BranchDTO selectBranch() {
        List<BranchDTO> branches = navigationManager.getBranchService().getAllBranches();

        if (branches.isEmpty()) {
            displayError("No branches available in the system");
            return null;
        }

        String[] branchNames = new String[branches.size()];
        for (int i = 0; i < branches.size(); i++) {
            BranchDTO branch = branches.get(i);
            branchNames[i] = branch.getAddress() + " (" + branch.getZoneName() + ")";
        }

        int choice = displayMenu("Select Branch", branchNames);

        if (choice == 0) {
            return null;
        }

        return branches.get(choice - 1);
    }

    /**
     * Displays a menu for selecting a future shift from those available in the system.
     */
    private ShiftDTO selectFutureShift() {
        List<ShiftDTO> futureShifts = navigationManager.getShiftService().getFutureShifts();
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
            String description = shift.getDate().format(dateFormatter) + " - " + shift.getShiftType();
            if (shift.hasBranch()) {
                description += " at " + shift.getBranchAddress();
            }
            shiftDescriptions[i] = description;
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
        List<PositionDTO> positions = navigationManager.getPositionService().getAllPositions();
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
    private EmployeeDTO selectEmployeeFromList(List<EmployeeDTO> employees, String address) {
        if (employees.isEmpty()) {
            displayError("No employees to select from");
            return null;
        }

        // Build array of names for display in menu
        List<EmployeeDTO> newList = new ArrayList<>();
        for (EmployeeDTO emp : employees) {
            if (emp.getBranchAddress().equals(address)) {
                newList.add(emp);
            }
        }
        String[] employeeNames = new String[newList.size()];

        for (int i = 0; i < newList.size(); i++) {
            EmployeeDTO emp = employees.get(i);
            String branchInfo = emp.hasBranch() ? " [" + emp.getBranchAddress() + "]" : " [No Branch]";
            employeeNames[i] = emp.getFullName() + " (ID: " + emp.getId() + ")" + branchInfo;
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

        boolean success = navigationManager.getEmployeeService().updateShiftHours(shiftTypeStr, newStart, newEnd);

        if (success) {
            displayMessage("Shift hours updated successfully for " + shiftTypes[choice - 1]);
        } else {
            displayError("Failed to update shift hours. Please check the time format (HH:mm).");
        }
    }
}