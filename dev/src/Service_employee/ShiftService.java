package Service_employee;

import DTO.EmployeeDTO;
import DTO.PositionDTO;
import DTO.ShiftDTO;
import Domain_employee.EmployeeController;

import java.sql.SQLException;
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



    public List<ShiftDTO> createShiftsForWeek(LocalDate startDate, String branchAddress) {

        if (branchAddress == null || branchAddress.trim().isEmpty()) {
            System.out.println("Error: Branch address is required for creating shifts.");
            return new ArrayList<>();
        }

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

        for (int i = 0; i < 7; i++) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

            // Create morning shift
            ShiftDTO morningShift = employeeController.createShift(currentDate, "MORNING", branchAddress);
            if (morningShift != null) {
                boolean managerAssigned = assignManagerToShift(morningShift, managerPositions, dayOfWeek, "MORNING");
                if (managerAssigned) {
                    createdShifts.add(morningShift);
                } else {
                    System.out.println("No available shift manager for morning shift on " + currentDate +
                            " at " + branchAddress + ". Shift not created.");
                }
            }

            // Create evening shift
            ShiftDTO eveningShift = employeeController.createShift(currentDate, "EVENING", branchAddress);
            if (eveningShift != null) {
                boolean managerAssigned = assignManagerToShift(eveningShift, managerPositions, dayOfWeek, "EVENING");
                if (managerAssigned) {
                    createdShifts.add(eveningShift);
                } else {
                    System.out.println("No available shift manager for evening shift on " + currentDate +
                            " at " + branchAddress + ". Shift not created.");
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
        List<ShiftDTO> historicalShifts = employeeController.getHistoricalShifts();
        List<ShiftDTO> employeeShifts = new ArrayList<>();
        for (ShiftDTO shift : historicalShifts) {
            boolean employeeInShift = shift.getAssignments().values().stream().anyMatch(employeeName -> {EmployeeDTO employee = employeeController.getEmployee(employeeId);return employee != null && employee.getFullName().equals(employeeName);});
            if (employeeInShift) {
                employeeShifts.add(shift);
            }
        }
        return employeeShifts;
    }




    public List<ShiftDTO> getEmployeeFutureShifts(String employeeId) {
        List<ShiftDTO> futureShifts = employeeController.getFutureShifts();
        List<ShiftDTO> employeeFutureShifts = new ArrayList<>();
        for (ShiftDTO shift : futureShifts) {
            boolean employeeInShift = shift.getAssignments().values().stream().anyMatch(employeeName -> {EmployeeDTO employee = employeeController.getEmployee(employeeId);return employee != null && employee.getFullName().equals(employeeName);});
            if (employeeInShift) {
                employeeFutureShifts.add(shift);
            }
        }
        return employeeFutureShifts;
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


    // Branch-specific shift operations
    public List<ShiftDTO> getShiftsByBranch(String branchAddress) {
        return employeeController.getShiftsByBranch(branchAddress);
    }


    public List<ShiftDTO> getFutureShiftsByBranch(String branchAddress) {
        List<ShiftDTO> branchShifts = employeeController.getShiftsByBranch(branchAddress);
        List<ShiftDTO> futureShifts = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (ShiftDTO shift : branchShifts) {
            if (!shift.getDate().isBefore(today)) {
                futureShifts.add(shift);
            }
        }

        return futureShifts;
    }


    public List<ShiftDTO> getHistoricalShiftsByBranch(String branchAddress) {
        List<ShiftDTO> branchShifts = employeeController.getShiftsByBranch(branchAddress);
        List<ShiftDTO> historicalShifts = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (ShiftDTO shift : branchShifts) {
            if (shift.getDate().isBefore(today)) {
                historicalShifts.add(shift);
            }
        }

        return historicalShifts;
    }

    public String getShiftIdByTime(LocalDate startDate, String shiftTime, String branchAddress) throws SQLException {
        return employeeController.getShiftIdByTime(startDate, shiftTime, branchAddress);
    }

    public List<ShiftDTO> getAllShifts() {
        return employeeController.getAllShifts();
    }
}