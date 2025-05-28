package Service_employee;

import Domain_employee.EmployeeController;

import java.time.LocalDate;
import java.util.List;

/**
 * Thin service layer for shift assignment operations.
 * Acts as a bridge between presentation layer and controller (business logic).
 */
public class AssignmentService {
    private final EmployeeController employeeController;

    public AssignmentService() {
        this.employeeController = new EmployeeController();
    }

    // Shift Assignment Operations
    public boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName) {
        return employeeController.assignEmployeeToShift(shiftId, employeeId, positionName);
    }

    public boolean removeAssignmentFromShift(String shiftId, String positionName) {
        return employeeController.removeAssignmentFromShift(shiftId, positionName);
    }

    public boolean areAllRequiredPositionsCovered(String shiftId) {
        return employeeController.areAllRequiredPositionsCovered(shiftId);
    }

    public boolean isEmployeeAlreadyAssignedToShift(String shiftId, String employeeId) {
        return employeeController.isEmployeeAlreadyAssignedToShift(shiftId, employeeId);
    }

    public List<EmployeeDTO> getAvailableEmployeesForShift(LocalDate date, String shiftType) {
        return employeeController.getAvailableEmployeesForShift(date, shiftType);
    }

    public int getRequiredPositionsCount(String shiftType, String positionName) {
        return employeeController.getRequiredPositionsCount(shiftType, positionName);
    }

    // Branch-specific assignment operations
    public List<EmployeeDTO> getAvailableEmployeesForShiftByBranch(LocalDate date, String shiftType, String branchAddress) {
        // Get available employees and filter by branch
        return employeeController.getAvailableEmployeesForShift(date, shiftType).stream()
                .filter(employee -> branchAddress == null ||
                        branchAddress.equals(employee.getBranchAddress()) ||
                        employee.getBranchAddress() == null) // Allow employees without specific branch
                .toList();
    }

    public List<EmployeeDTO> getQualifiedAndAvailableEmployees(LocalDate date, String shiftType, String positionName) {
        List<EmployeeDTO> availableEmployees = employeeController.getAvailableEmployeesForShift(date, shiftType);
        List<EmployeeDTO> qualifiedEmployees = employeeController.getQualifiedEmployeesForPosition(positionName);

        // Return intersection of available and qualified employees
        return availableEmployees.stream()
                .filter(emp -> qualifiedEmployees.stream()
                        .anyMatch(qualified -> qualified.getId().equals(emp.getId())))
                .toList();
    }

    public List<EmployeeDTO> getQualifiedAndAvailableEmployeesByBranch(LocalDate date, String shiftType,
                                                                       String positionName, String branchAddress) {
        return getQualifiedAndAvailableEmployees(date, shiftType, positionName).stream()
                .filter(employee -> branchAddress == null ||
                        branchAddress.equals(employee.getBranchAddress()) ||
                        employee.getBranchAddress() == null) // Allow employees without specific branch
                .toList();
    }
}