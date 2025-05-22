package Controller_employee;

import Service_employee.EmployeeDTO;
import Service_employee.EmployeeService;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller responsible for handling shift assignments.
 * Acts as an intermediary between the presentation layer and the service layer.
 */
public class AssignmentController {
    private final EmployeeService employeeService;

    public AssignmentController() {
        this.employeeService = new EmployeeService();
    }

    /**
     * Assigns an employee to a shift.
     */
    public boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName) {
        return employeeService.assignEmployeeToShift(shiftId, employeeId, positionName);
    }

    /**
     * Removes an employee assignment from a shift.
     */
    public boolean removeAssignmentFromShift(String shiftId, String positionName) {
        return employeeService.removeAssignmentFromShift(shiftId, positionName);
    }

    /**
     * Checks if all required positions are covered in a shift.
     */
    public boolean areAllRequiredPositionsCovered(String shiftId) {
        return employeeService.areAllRequiredPositionsCovered(shiftId);
    }

    /**
     * Checks if employee is already assigned to a shift.
     */
    public boolean isEmployeeAlreadyAssignedToShift(String shiftId, String employeeId) {
        return employeeService.isEmployeeAlreadyAssignedToShift(shiftId, employeeId);
    }

    /**
     * Gets available employees for a specific shift.
     */
    public List<EmployeeDTO> getAvailableEmployeesForShift(LocalDate date, String shiftType) {
        return employeeService.getAvailableEmployeesForShift(date, shiftType);
    }

    /**
     * Gets the required count for a position in a shift type.
     */
    public int getRequiredPositionsCount(String shiftType, String positionName) {
        return employeeService.getRequiredPositionsCount(shiftType, positionName);
    }
}