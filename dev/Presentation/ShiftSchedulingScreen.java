

package Presentation;

import Service.EmployeeDTO;
import Service.EmployeeService;
import Service.PositionDTO;
import Service.ShiftDTO;
import Service.ShiftService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class ShiftSchedulingScreen extends BaseScreen {
    private final EmployeeService employeeService;
    private final ShiftService shiftService;
    private final DateTimeFormatter dateFormatter;
    private final EmployeeDTO loggedInEmployee;

    public ShiftSchedulingScreen(EmployeeService employeeService, ShiftService shiftService) {
        this.employeeService = employeeService;
        this.shiftService = shiftService;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.loggedInEmployee = null;
    }

    public ShiftSchedulingScreen(EmployeeService employeeService, ShiftService shiftService, EmployeeDTO loggedInEmployee) {
        this.employeeService = employeeService;
        this.shiftService = shiftService;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.loggedInEmployee = loggedInEmployee;
    }

    @Override
    public void display() {
        // בדיקת הרשאות - רק מנהלים יכולים לגשת למסך זה
        if (loggedInEmployee != null && !loggedInEmployee.isManager()) {
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

    private void createShiftsForWeek() {
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
        List<ShiftDTO> createdShifts = shiftService.createShiftsForWeek(startDate);
        if (createdShifts.isEmpty()) {
            displayError("No shifts were created. Shifts may already exist for this week");
        } else {
            displayMessage(createdShifts.size() + " shifts were created successfully for week starting " +
                    startDate.format(dateFormatter));
        }
    }


    private void viewFutureShifts() {
        displayTitle("Future Shifts");
        List<ShiftDTO> futureShifts = shiftService.getFutureShifts();
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
    private void assignEmployeeToShift() {
        displayTitle("Assign Employee to Shift");

        // בחירת משמרת
        ShiftDTO shift = selectFutureShift();
        if (shift == null) return;

        displayShiftDetails(shift);

        // בחירת תפקיד
        PositionDTO position = selectPosition();
        if (position == null) return;

        // מציאת עובדים מתאימים
        List<EmployeeDTO> qualifiedEmployees = employeeService.getQualifiedEmployeesForPosition(position.getName());
        List<EmployeeDTO> availableEmployees = employeeService.getAvailableEmployeesForShift(shift.getDate(), shift.getShiftType());

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

        // בחירת עובד
        EmployeeDTO employee = selectEmployeeFromList(eligibleEmployees);
        if (employee == null) return;

        // בדיקה אם העובד כבר משובץ
        if (employeeService.isEmployeeAlreadyAssignedToShift(shift.getId(), employee.getId())) {
            displayError("This employee is already assigned to this shift.");
            return;
        }

        // ניסיון לשבץ את העובד
        boolean success = employeeService.assignEmployeeToShift(shift.getId(), employee.getId(), position.getName());

        if (success) {
            displayMessage("Employee successfully assigned to shift");
            if (employeeService.areAllRequiredPositionsCovered(shift.getId())) {
                displayMessage("All required positions for this shift are now covered");
            }
        } else {
            // כאן נוסיף את הבדיקה המדויקת לגבי חוסר דרישה
            int requiredCount = employeeService.getRequiredPositionsCount(shift.getShiftType(), position.getName());
            if (requiredCount == 0) {
                displayError("Cannot assign employee: No required positions defined for '" + position.getName() + "' in this shift.");
            } else {
                displayError("Failed to assign employee. Possible reasons: all positions are filled, lack of qualification, or availability issues.");
            }
        }
    }

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
        // Confirm removal
        String employee = assignments.get(selectedPosition);

        if (getBooleanInput("Are you sure you want to remove " +
                employee + " from position " + selectedPosition + "?")) {
            boolean success = employeeService.removeAssignmentFromShift(shift.getId(), selectedPosition);
            if (success) {
                displayMessage("Assignment successfully removed");
            } else {
                displayError("Error removing assignment");
            }
        }
    }

//    private void viewAvailableEmployeesForShift() {
//        displayTitle("Available Employees for Shift");
//        // Select or create a shift
//        ShiftDTO shift = selectOrCreateShift();
//        if (shift == null) {
//            return;
//        }
//        // Get available employees
//        List<EmployeeDTO> availableEmployees = employeeService.getAvailableEmployeesForShift(
//                shift.getDate(), shift.getShiftType());
//        if (availableEmployees.isEmpty()) {
//            displayMessage("No employees available for this shift");
//            return;
//        }
//        displayTitle("Available Employees for " + shift.getDate().format(dateFormatter) +
//                " - " + shift.getShiftType());
//        for (EmployeeDTO employee : availableEmployees) {
//            displayMessage("- " + employee.getFullName() + " (ID: " + employee.getId() + ")");
//        }
//        // Option to view qualified employees by position
//        if (getBooleanInput("Do you want to view available employees by position?")) {
//            PositionDTO position = selectPosition();
//            if (position != null) {
//                List<EmployeeDTO> qualifiedEmployees = employeeService.getQualifiedEmployeesForPosition(position.getName());
//                // Filter to get employees that are both qualified and available
//                List<EmployeeDTO> eligibleEmployees = new ArrayList<>();
//                for (EmployeeDTO employee : qualifiedEmployees) {
//                    if (availableEmployees.stream().anyMatch(e -> e.getId().equals(employee.getId()))) {
//                        eligibleEmployees.add(employee);
//                    }
//                }
//                displayTitle("Available Employees for Position: " + position.getName());
//                if (eligibleEmployees.isEmpty()) {
//                    displayMessage("No available employees qualified for this position");
//                } else {
//                    for (EmployeeDTO employee : eligibleEmployees) {
//                        displayMessage("- " + employee.getFullName() + " (ID: " + employee.getId() + ")");
//                    }
//                }
//            }
//        }
//    }

    private void viewMissingPositions() {
        displayTitle("Missing Positions in Future Shifts");
        List<ShiftDTO> futureShifts = shiftService.getFutureShifts();
        if (futureShifts.isEmpty()) {
            displayMessage("No future shifts in the system");
            return;
        }
        boolean foundMissing = false;
        for (ShiftDTO shift : futureShifts) {
            List<PositionDTO> missingPositions = shiftService.getMissingPositionsForShift(shift.getId());
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

        // הצגת עובדים משובצים:
        Map<String, String> assignments = shift.getAssignments();

        if (assignments.isEmpty()) {
            displayMessage("Assigned Employees: None");
        } else {
            displayMessage("\nAssigned Employees:");

            // אם זו משמרת עתידית -> להציג תמיד את כל השיבוצים
            if (!shift.getDate().isBefore(LocalDate.now())) {
                for (Map.Entry<String, String> entry : assignments.entrySet()) {
                    displayMessage("- " + entry.getKey() + ": " + entry.getValue());
                }
            }
            // משמרת היסטורית - מגבלות צפייה
            else {
                if (loggedInEmployee.isManager()) {
                    for (Map.Entry<String, String> entry : assignments.entrySet()) {
                        displayMessage("- " + entry.getKey() + ": " + entry.getValue());
                    }
                } else {
                    // עובד רגיל רואה רק את עצמו
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

        // הצגת משרות חסרות (רק למנהלים)
        if (loggedInEmployee.isManager()) {
            List<PositionDTO> missingPositions = shiftService.getMissingPositionsForShift(shift.getId());
            if (!missingPositions.isEmpty()) {
                displayMessage("\nMissing Positions:");
                for (PositionDTO position : missingPositions) {
                    displayMessage("- " + position.getName());
                }
            }
        }
    }


    private ShiftDTO selectFutureShift() {
        List<ShiftDTO> futureShifts = shiftService.getFutureShifts();
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


    private ShiftDTO selectOrCreateShift() {
        String[] options = {
                "Select existing shift", "Create new shift for specific date"
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


    private ShiftDTO createShiftForDate() {
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
        String shiftType = (shiftChoice == 1) ? "MORNING" : "EVENING";
        // Check if shift already exists
        ShiftDTO existingShift = employeeService.getShift(date, shiftType);
        if (existingShift != null) {
            displayMessage("Shift already exists for this date and type");
            return existingShift;
        }
        // Create new shift
        ShiftDTO newShift = employeeService.createShift(date, shiftType);
        if (newShift != null) {
            displayMessage("Shift created successfully");
            return newShift;
        } else {
            displayError("Error creating shift");
            return null;
        }
    }
    private void manageShiftHours() {
        displayTitle("Manage Shift Hours");

        String[] shiftTypes = { "Morning Shift", "Evening Shift" };
        int choice = displayMenu("Select Shift Type to Update", shiftTypes);

        if (choice == 0) return;

        String shiftTypeStr = (choice == 1) ? "MORNING" : "EVENING";

        String newStart = getInput("Enter new start time (HH:mm)");
        String newEnd = getInput("Enter new end time (HH:mm)");

        boolean success = employeeService.updateShiftHours(shiftTypeStr, newStart, newEnd);

        if (success) {
            displayMessage("Shift hours updated successfully for " + shiftTypes[choice - 1]);
        } else {
            displayError("Failed to update shift hours. Please check the time format (HH:mm).");
        }
    }

}