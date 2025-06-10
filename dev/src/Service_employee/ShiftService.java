package Service_employee;

import DTO.EmployeeDTO;
import DTO.PositionDTO;
import DTO.ShiftDTO;
import Domain_employee.*;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShiftService {
    private final ShiftManagementController shiftManagementController;
    private final PositionManagementController positionManagementController;
    private final EmployeeManagementController employeeManagementController;
    private final AvailabilityManagementController availabilityManagementController;

    public ShiftService() {
        this.shiftManagementController = new ShiftManagementController();
        this.positionManagementController = new PositionManagementController();
        this.employeeManagementController = new EmployeeManagementController();
        this.availabilityManagementController = new AvailabilityManagementController();
    }

    public List<ShiftDTO> createShiftsForWeek(LocalDate startDate, String branchAddress) {
        if (branchAddress == null || branchAddress.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<ShiftDTO> createdShifts = new ArrayList<>();
        LocalDate currentDate = startDate;
        List<PositionDTO> managerPositions = positionManagementController.getAllPositions().stream()
                .filter(PositionDTO::isRequiresShiftManager)
                .toList();

        if (managerPositions.isEmpty()) {
            return createdShifts;
        }

        for (int i = 0; i < 7; i++) {
            if (hasAvailableManagerForShift(currentDate, "MORNING", branchAddress, managerPositions)) {
                ShiftDTO morningShift = shiftManagementController.createShift(currentDate, "MORNING", branchAddress);
                if (morningShift != null) {
                    ShiftDTO updatedShift = shiftManagementController.getShiftById(morningShift.getId());
                    createdShifts.add(updatedShift != null ? updatedShift : morningShift);
                }
            }
            if (hasAvailableManagerForShift(currentDate, "EVENING", branchAddress, managerPositions)) {
                ShiftDTO eveningShift = shiftManagementController.createShift(currentDate, "EVENING", branchAddress);
                if (eveningShift != null) {
                    ShiftDTO updatedShift = shiftManagementController.getShiftById(eveningShift.getId());
                    createdShifts.add(updatedShift != null ? updatedShift : eveningShift);
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        return createdShifts;
    }

    private boolean hasAvailableManagerForShift(LocalDate date, String shiftType, String branchAddress,
                                                List<PositionDTO> managerPositions) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        List<EmployeeDTO> branchEmployees = employeeManagementController.getEmployeesByBranch(branchAddress);

        for (EmployeeDTO employee : branchEmployees) {
            if (!employee.isManager()) {
                continue;
            }
            if (!availabilityManagementController.isEmployeeAvailable(employee.getId(), dayOfWeek, shiftType)) {
                continue;
            }
            for (PositionDTO managerPosition : managerPositions) {
                if (employee.getQualifiedPositions().contains(managerPosition.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
    public List<ShiftDTO> getFutureShifts() {
        return shiftManagementController.getFutureShifts();
    }

    public List<ShiftDTO> getHistoricalShifts() {
        return shiftManagementController.getHistoricalShifts();
    }

    public List<ShiftDTO> getEmployeeShiftHistory(String employeeId) {
        List<ShiftDTO> historicalShifts = shiftManagementController.getHistoricalShifts();
        List<ShiftDTO> employeeShifts = new ArrayList<>();
        for (ShiftDTO shift : historicalShifts) {
            boolean employeeInShift = shift.getAssignments().values().stream().anyMatch(employeeName -> {
                EmployeeDTO employee = employeeManagementController.getEmployee(employeeId);
                return employee != null && employee.getFullName().equals(employeeName);
            });
            if (employeeInShift) {
                employeeShifts.add(shift);
            }
        }
        return employeeShifts;
    }

    public List<ShiftDTO> getEmployeeFutureShifts(String employeeId) {
        List<ShiftDTO> futureShifts = shiftManagementController.getFutureShifts();
        List<ShiftDTO> employeeFutureShifts = new ArrayList<>();
        for (ShiftDTO shift : futureShifts) {
            boolean employeeInShift = shift.getAssignments().values().stream().anyMatch(employeeName -> {
                EmployeeDTO employee = employeeManagementController.getEmployee(employeeId);
                return employee != null && employee.getFullName().equals(employeeName);
            });
            if (employeeInShift) {
                employeeFutureShifts.add(shift);
            }
        }
        return employeeFutureShifts;
    }

    public List<PositionDTO> getMissingPositionsForShift(String shiftId) {
        return positionManagementController.getMissingPositionsForShift(shiftId);
    }

    public ShiftDTO getShiftById(String shiftId) {
        return shiftManagementController.getShiftById(shiftId);
    }

    public List<ShiftDTO> getShiftsByBranch(String branchAddress) {
        return shiftManagementController.getShiftsByBranch(branchAddress);
    }

    public List<ShiftDTO> getFutureShiftsByBranch(String branchAddress) {
        List<ShiftDTO> branchShifts = shiftManagementController.getShiftsByBranch(branchAddress);
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
        List<ShiftDTO> branchShifts = shiftManagementController.getShiftsByBranch(branchAddress);
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
        return shiftManagementController.getShiftIdByTime(startDate, shiftTime, branchAddress);
    }

    public List<ShiftDTO> getAllShifts() {
        return shiftManagementController.getAllShifts();
    }
}