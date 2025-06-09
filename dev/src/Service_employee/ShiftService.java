package Service_employee;

import DTO.EmployeeDTO;
import DTO.PositionDTO;
import DTO.ShiftDTO;
import Domain_employee.ShiftManagementController;
import Domain_employee.EmployeeManagementController;
import Domain_employee.PositionManagementController;
import Domain_employee.AvailabilityManagementController;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Thin service layer for shift operations.
 * Acts as a bridge between presentation layer and specialized controllers.
 */
public class ShiftService {
    private final ShiftManagementController shiftController;
    private final EmployeeManagementController employeeController;
    private final PositionManagementController positionController;
    private final AvailabilityManagementController availabilityController;

    public ShiftService() {
        this.shiftController = new ShiftManagementController();
        this.employeeController = new EmployeeManagementController();
        this.positionController = new PositionManagementController();
        this.availabilityController = new AvailabilityManagementController();
    }

    public List<ShiftDTO> createShiftsForWeek(LocalDate startDate, String branchAddress) {

        if (branchAddress == null || branchAddress.trim().isEmpty()) {
            System.out.println("Error: Branch address is required for creating shifts.");
            return new ArrayList<>();
        }

        List<ShiftDTO> createdShifts = new ArrayList<>();
        LocalDate currentDate = startDate;

        // Get manager positions for validation
        List<PositionDTO> managerPositions = positionController.getAllPositions().stream()
                .filter(PositionDTO::isRequiresShiftManager)
                .toList();

        if (managerPositions.isEmpty()) {
            System.out.println("No shift manager positions defined in the system.");
            return createdShifts;
        }

        for (int i = 0; i < 7; i++) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

            // Create morning shift - ללא הדפסות ביניים
            ShiftDTO morningShift = shiftController.createShift(currentDate, "MORNING", branchAddress);
            if (morningShift != null) {
                createdShifts.add(morningShift);
            }

            // Create evening shift - ללא הדפסות ביניים
            ShiftDTO eveningShift = shiftController.createShift(currentDate, "EVENING", branchAddress);
            if (eveningShift != null) {
                createdShifts.add(eveningShift);
            }

            currentDate = currentDate.plusDays(1);
        }

        return createdShifts;
    }

    public List<ShiftDTO> getFutureShifts() {
        return shiftController.getFutureShifts();
    }

    public List<ShiftDTO> getHistoricalShifts() {
        return shiftController.getHistoricalShifts();
    }

    public List<ShiftDTO> getEmployeeShiftHistory(String employeeId) {
        List<ShiftDTO> historicalShifts = shiftController.getHistoricalShifts();
        List<ShiftDTO> employeeShifts = new ArrayList<>();
        for (ShiftDTO shift : historicalShifts) {
            boolean employeeInShift = shift.getAssignments().values().stream().anyMatch(employeeName -> {
                EmployeeDTO employee = employeeController.getEmployee(employeeId);
                return employee != null && employee.getFullName().equals(employeeName);
            });
            if (employeeInShift) {
                employeeShifts.add(shift);
            }
        }
        return employeeShifts;
    }

    public List<ShiftDTO> getEmployeeFutureShifts(String employeeId) {
        List<ShiftDTO> futureShifts = shiftController.getFutureShifts();
        List<ShiftDTO> employeeFutureShifts = new ArrayList<>();
        for (ShiftDTO shift : futureShifts) {
            boolean employeeInShift = shift.getAssignments().values().stream().anyMatch(employeeName -> {
                EmployeeDTO employee = employeeController.getEmployee(employeeId);
                return employee != null && employee.getFullName().equals(employeeName);
            });
            if (employeeInShift) {
                employeeFutureShifts.add(shift);
            }
        }
        return employeeFutureShifts;
    }

    public List<PositionDTO> getMissingPositionsForShift(String shiftId) {
        return positionController.getMissingPositionsForShift(shiftId);
    }

    public ShiftDTO getShiftById(String shiftId) {
        return shiftController.getAllShifts().stream()
                .filter(shift -> shift.getId().equals(shiftId))
                .findFirst()
                .orElse(null);
    }

    // Branch-specific shift operations
    public List<ShiftDTO> getShiftsByBranch(String branchAddress) {
        return shiftController.getShiftsByBranch(branchAddress);
    }

    public List<ShiftDTO> getFutureShiftsByBranch(String branchAddress) {
        List<ShiftDTO> branchShifts = shiftController.getShiftsByBranch(branchAddress);
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
        List<ShiftDTO> branchShifts = shiftController.getShiftsByBranch(branchAddress);
        List<ShiftDTO> historicalShifts = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (ShiftDTO shift : branchShifts) {
            if (shift.getDate().isBefore(today)) {
                historicalShifts.add(shift);
            }
        }

        return historicalShifts;
    }

    public String getShiftIdByTime(LocalDate startDate, String shiftTime) throws SQLException {
        return shiftController.getShiftIdByTime(startDate, shiftTime);
    }

    public List<ShiftDTO> getAllShifts() {
        return shiftController.getAllShifts();
    }
}