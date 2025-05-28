package Service_employee;

import Domain_employee.EmployeeController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Thin service layer for shift operations.
 * Acts as a bridge between presentation layer and controller (business logic).
 */
public class ShiftService {
    private final EmployeeController employeeController;

    public ShiftService() {
        this.employeeController = new EmployeeController();
    }

    // Shift Creation and Management
    public List<ShiftDTO> createShiftsForWeek(LocalDate startDate) {
        return createShiftsForWeek(startDate, null);
    }

    public List<ShiftDTO> createShiftsForWeek(LocalDate startDate, String branchAddress) {
        List<ShiftDTO> createdShifts = new ArrayList<>();
        LocalDate currentDate = startDate;

        // Get manager positions for validation
        List<PositionDTO> managerPositions = employeeController.getAllPositions().stream()
                .filter(PositionDTO::isRequiresShiftManager)
                .toList();

        if (managerPositions.isEmpty()) {
            System.out.println("No shift manager positions defined in the system.");
            return createdShifts;
        }

        // Create shifts for 7 days (a week)
        for (int i = 0; i < 7; i++) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

            // Create morning shift
            ShiftDTO morningShift = employeeController.createShift(currentDate, "MORNING", branchAddress);
            if (morningShift != null) {
                boolean managerAssigned = assignManagerToShift(morningShift, managerPositions, dayOfWeek, "MORNING");
                if (managerAssigned) {
                    createdShifts.add(morningShift);
                } else {
                    // Delete shift if no manager could be assigned
                    System.out.println("No available shift manager for morning shift on " + currentDate + ". Shift not created.");
                }
            }

            // Create evening shift
            ShiftDTO eveningShift = employeeController.createShift(currentDate, "EVENING", branchAddress);
            if (eveningShift != null) {
                boolean managerAssigned = assignManagerToShift(eveningShift, managerPositions, dayOfWeek, "EVENING");
                if (managerAssigned) {
                    createdShifts.add(eveningShift);
                } else {
                    // Delete shift if no manager could be assigned
                    System.out.println("No available shift manager for evening shift on " + currentDate + ". Shift not created.");
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        return createdShifts;
    }

    private boolean assignManagerToShift(ShiftDTO shift, List<PositionDTO> managerPositions,
                                         DayOfWeek dayOfWeek, String shiftType) {
        // Get all employees
        List<EmployeeDTO> allEmployees = employeeController.getAllEmployees();

        // Try to find an available manager
        for (EmployeeDTO employee : allEmployees) {
            // Check if employee is available for this shift
            if (employeeController.isEmployeeAvailable(employee.getId(), dayOfWeek, shiftType)) {
                // Check if employee is qualified for any manager position
                for (PositionDTO managerPosition : managerPositions) {
                    if (employee.getQualifiedPositions().contains(managerPosition.getName())) {
                        // Try to assign the employee to this manager position
                        if (employeeController.assignEmployeeToShift(shift.getId(),
                                employee.getId(), managerPosition.getName())) {
                            return true; // Successfully assigned manager
                        }
                    }
                }
            }
        }
        return false; // No manager could be assigned
    }

    public List<ShiftDTO> getFutureShifts() {
        return employeeController.getFutureShifts();
    }

    public List<ShiftDTO> getHistoricalShifts() {
        return employeeController.getHistoricalShifts();
    }

    public List<ShiftDTO> getEmployeeShiftHistory(String employeeId) {
        // Get all historical shifts and filter by employee
        return employeeController.getHistoricalShifts().stream()
                .filter(shift -> shift.getAssignments().values().stream()
                        .anyMatch(employeeName -> {
                            EmployeeDTO employee = employeeController.getEmployee(employeeId);
                            return employee != null && employee.getFullName().equals(employeeName);
                        }))
                .toList();
    }

    public List<ShiftDTO> getEmployeeFutureShifts(String employeeId) {
        // Get all future shifts and filter by employee
        return employeeController.getFutureShifts().stream()
                .filter(shift -> shift.getAssignments().values().stream()
                        .anyMatch(employeeName -> {
                            EmployeeDTO employee = employeeController.getEmployee(employeeId);
                            return employee != null && employee.getFullName().equals(employeeName);
                        }))
                .toList();
    }

    public List<PositionDTO> getMissingPositionsForShift(String shiftId) {
        return employeeController.getMissingPositionsForShift(shiftId);
    }

    public ShiftDTO getShiftById(String shiftId) {
        return employeeController.getAllShifts().stream()
                .filter(shift -> shift.getId().equals(shiftId))
                .findFirst()
                .orElse(null);
    }

    public boolean deleteShift(String shiftId) {
        return employeeController.removeAssignmentFromShift(shiftId, null); // TODO: Implement proper shift deletion
    }

    // Branch-specific shift operations
    public List<ShiftDTO> getShiftsByBranch(String branchAddress) {
        return employeeController.getShiftsByBranch(branchAddress);
    }

    public List<ShiftDTO> getFutureShiftsByBranch(String branchAddress) {
        return employeeController.getShiftsByBranch(branchAddress).stream()
                .filter(shift -> !shift.getDate().isBefore(LocalDate.now()))
                .toList();
    }

    public List<ShiftDTO> getHistoricalShiftsByBranch(String branchAddress) {
        return employeeController.getShiftsByBranch(branchAddress).stream()
                .filter(shift -> shift.getDate().isBefore(LocalDate.now()))
                .toList();
    }


    public List<ShiftDTO> getAllShifts() {
        return employeeController.getAllShifts();
    }
}