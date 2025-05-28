package Service_employee;

import Domain_employee.EmployeeController;

import java.util.List;

/**
 * Thin service layer for position operations.
 * Acts as a bridge between presentation layer and controller (business logic).
 */
public class PositionService {
    private final EmployeeController employeeController;

    public PositionService() {
        this.employeeController = new EmployeeController();
    }

    // Position Management
    public boolean addPosition(String name, boolean isShiftManagerRole) {
        return employeeController.addPosition(name, isShiftManagerRole);
    }

    public List<PositionDTO> getAllPositions() {
        return employeeController.getAllPositions();
    }

    public PositionDTO getPosition(String name) {
        return employeeController.getPositionDetails(name);
    }

    // Qualification Management
    public boolean addQualificationToEmployee(String employeeId, String positionName) {
        return employeeController.addQualificationToEmployee(employeeId, positionName);
    }

    public boolean removeQualificationFromEmployee(String employeeId, String positionName) {
        return employeeController.removeQualificationFromEmployee(employeeId, positionName);
    }

    public List<EmployeeDTO> getQualifiedEmployeesForPosition(String positionName) {
        return employeeController.getQualifiedEmployeesForPosition(positionName);
    }

    // Required Positions for Shifts
    public boolean setRequiredPosition(String shiftType, String positionName, int count) {
        return employeeController.addRequiredPosition(shiftType, positionName, count);
    }

    public int getRequiredPositionsCount(String shiftType, String positionName) {
        return employeeController.getRequiredPositionsCount(shiftType, positionName);
    }

    // Employee Access (for position management screens)
    public List<EmployeeDTO> getAllEmployees() {
        return employeeController.getAllEmployees();
    }
}