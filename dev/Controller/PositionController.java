package Controller;

import Service.EmployeeDTO;
import Service.EmployeeService;
import Service.PositionDTO;

import java.util.List;

/**
 * Controller responsible for handling position and qualification operations.
 * Acts as an intermediary between the presentation layer and the service layer.
 */
public class PositionController {
    private final EmployeeService employeeService;

    public PositionController() {
        this.employeeService = new EmployeeService();
    }

    /**
     * Adds a new position to the system.
     */
    public boolean addPosition(String name, boolean isShiftManagerRole) {
        return employeeService.addPosition(name, isShiftManagerRole);
    }

    /**
     * Gets all positions defined in the system.
     */
    public List<PositionDTO> getAllPositions() {
        return employeeService.getAllPositions();
    }

    /**
     * Gets a position by name.
     */
    public PositionDTO getPosition(String name) {
        return employeeService.getPositionDetails(name);
    }

    /**
     * Assigns a qualification to an employee.
     */


    public boolean addQualificationToEmployee(String employeeId, String positionName) {
        boolean success = employeeService.addQualificationToEmployee(employeeId, positionName);

        if (success) {
            // בדוק אם מדובר בתפקיד של מנהל משמרת
            PositionDTO position = employeeService.getPositionDetails(positionName);
            EmployeeDTO employee = employeeService.getEmployeeDetails(employeeId);

            if (position != null && position.isRequiresShiftManager() && employee != null) {
                // עדכן את תפקיד העובד ל-SHIFT_MANAGER
                return employeeService.updateEmployeeRole(employeeId, "SHIFT_MANAGER");
            }
        }

        return success;
    }

    /**
     * Removes a qualification from an employee.
     */
    public boolean removeQualificationFromEmployee(String employeeId, String positionName) {
        return employeeService.removeQualificationFromEmployee(employeeId, positionName);
    }

    /**
     * Gets employees qualified for a specific position.
     */
    public List<EmployeeDTO> getQualifiedEmployeesForPosition(String positionName) {
        return employeeService.getQualifiedEmployeesForPosition(positionName);
    }

    /**
     * Sets requirements for a position in shifts.
     */
    public boolean setRequiredPosition(String shiftType, String positionName, int count) {
        return employeeService.addRequiredPosition(shiftType, positionName, count);
    }

    /**
     * Gets all employees.
     */
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
}