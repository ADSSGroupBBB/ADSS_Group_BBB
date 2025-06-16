

package src.Service_employee;

import src.DTO.EmployeeDTO;
import src.Domain_employee.ShiftManagementController;
import src.Domain_employee.AvailabilityManagementController;
import src.Domain_employee.PositionManagementController;
import src.Domain_employee.EmployeeManagementController;

import java.time.LocalDate;
import java.util.List;

/**
 * Thin service layer for shift assignment operations.
 * Acts as a bridge between presentation layer and specialized controllers.
 */
public class AssignmentService {
    private final ShiftManagementController shiftController;
    private final AvailabilityManagementController availabilityController;
    private final PositionManagementController positionController;
    private final EmployeeManagementController employeeController;

    public AssignmentService() {
        this.shiftController = new ShiftManagementController();
        this.availabilityController = new AvailabilityManagementController();
        this.positionController = new PositionManagementController();
        this.employeeController = new EmployeeManagementController();
    }

    // Shift Assignment Operations
    public boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName) {
        return shiftController.assignEmployeeToShift(shiftId, employeeId, positionName);
    }

    public boolean removeAssignmentFromShift(String shiftId, String positionName) {
        return shiftController.removeAssignmentFromShift(shiftId, positionName);
    }

    public boolean areAllRequiredPositionsCovered(String shiftId) {
        return shiftController.areAllRequiredPositionsCovered(shiftId);
    }

    public boolean isEmployeeAlreadyAssignedToShift(String shiftId, String employeeId) {
        return shiftController.isEmployeeAlreadyAssignedToShift(shiftId, employeeId);
    }

    public List<EmployeeDTO> getAvailableEmployeesForShift(LocalDate date, String shiftType) {
        return availabilityController.getAvailableEmployeesForShift(date, shiftType);
    }

    public int getRequiredPositionsCount(String shiftType, String positionName) {
        return positionController.getRequiredPositionsCount(shiftType, positionName);
    }

    // Branch-specific assignment operations
    public List<EmployeeDTO> getAvailableEmployeesForShiftByBranch(LocalDate date, String shiftType, String branchAddress) {
        // Get available employees and filter by branch
        return availabilityController.getAvailableEmployeesForShift(date, shiftType).stream()
                .filter(employee -> branchAddress == null ||
                        branchAddress.equals(employee.getBranchAddress()) ||
                        employee.getBranchAddress() == null) // Allow employees without specific branch
                .toList();
    }

    public List<EmployeeDTO> getQualifiedAndAvailableEmployees(LocalDate date, String shiftType, String positionName) {
        List<EmployeeDTO> availableEmployees = availabilityController.getAvailableEmployeesForShift(date, shiftType);
        List<EmployeeDTO> qualifiedEmployees = positionController.getQualifiedEmployeesForPosition(positionName);

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