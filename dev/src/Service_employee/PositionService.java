

package Service_employee;

import DTO.EmployeeDTO;
import DTO.PositionDTO;
import Domain_employee.PositionManagementController;
import Domain_employee.EmployeeManagementController;

import java.util.List;

/**
 * Thin service layer for position operations.
 * Acts as a bridge between presentation layer and specialized controllers.
 */
public class PositionService {
    private final PositionManagementController positionController;
    private final EmployeeManagementController employeeController;

    public PositionService() {
        this.positionController = new PositionManagementController();
        this.employeeController = new EmployeeManagementController();
    }

    // Position Management
    public boolean addPosition(String name, boolean isShiftManagerRole) {
        return positionController.addPosition(name, isShiftManagerRole);
    }

    public List<PositionDTO> getAllPositions() {
        return positionController.getAllPositions();
    }

    public PositionDTO getPosition(String name) {
        return positionController.getPositionDetails(name);
    }

    // Qualification Management
    public boolean addQualificationToEmployee(String employeeId, String positionName) {
        return positionController.addQualificationToEmployee(employeeId, positionName);
    }

    public boolean removeQualificationFromEmployee(String employeeId, String positionName) {
        return positionController.removeQualificationFromEmployee(employeeId, positionName);
    }

    public List<EmployeeDTO> getQualifiedEmployeesForPosition(String positionName) {
        return positionController.getQualifiedEmployeesForPosition(positionName);
    }

    // Required Positions for Shifts
    public boolean setRequiredPosition(String shiftType, String positionName, int count) {
        return positionController.addRequiredPosition(shiftType, positionName, count);
    }

    public int getRequiredPositionsCount(String shiftType, String positionName) {
        return positionController.getRequiredPositionsCount(shiftType, positionName);
    }

    // Employee Access (for position management screens)
    public List<EmployeeDTO> getAllEmployees() {
        return employeeController.getAllEmployees();
    }
}