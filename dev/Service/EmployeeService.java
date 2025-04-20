package Service;

import Domain.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmployeeService {
    private EmployeeManager employeeManager;

    public EmployeeService() {
        this.employeeManager = EmployeeManager.getInstance();
    }

    public boolean addEmployee(Employee employee) {
        return employeeManager.addEmployee(employee);
    }

    /**
     * Removes an employee from the system.
     * @param employeeId The employee's ID number.
     * @return The removed employee, or null if the removal failed.
     */
    public Employee removeEmployee(String employeeId) {
        return employeeManager.removeEmployee(employeeId);
    }

    /**
     * Retrieves an employee by their ID.
     * @param employeeId The employee's ID number.
     * @return The employee, or null if not found.
     */
    public Employee getEmployee(String employeeId) {
        return employeeManager.getEmployee(employeeId);
    }

    /**
     * Retrieves all employees in the system.
     * @return A list of all employees.
     */
    public List<Employee> getAllEmployees() {
        return employeeManager.getAllEmployees();
    }

    // Employee availability updates

    /**
     * Updates an employee's availability for a specific day.
     * @param employeeId The employee's ID number.
     * @param dayOfWeek The day of the week.
     * @param morningAvailable Whether available for morning shift.
     * @param eveningAvailable Whether available for evening shift.
     * @return True if the update succeeded, false otherwise.
     */
    public boolean updateEmployeeAvailability(String employeeId, DayOfWeek dayOfWeek,
                                              boolean morningAvailable, boolean eveningAvailable) {
        return employeeManager.updateEmployeeAvailability(employeeId, dayOfWeek, morningAvailable, eveningAvailable);
    }

    /**
     * Updates an employee's availability for a specific shift.
     * @param employeeId The employee's ID number.
     * @param dayOfWeek The day of the week.
     * @param shiftType The type of shift.
     * @param available Whether available for the shift.
     * @return True if the update succeeded, false otherwise.
     */
    public boolean updateEmployeeAvailability(String employeeId, DayOfWeek dayOfWeek,
                                              ShiftType shiftType, boolean available) {
        return employeeManager.updateEmployeeAvailability(employeeId, dayOfWeek, shiftType, available);
    }

    // Position management

    /**
     * Adds a new position to the system.
     * @param position The position to add.
     * @return True if the addition succeeded, false otherwise.
     */
    public boolean addPosition(Position position) {
        return employeeManager.addPosition(position);
    }

    /**
     * Retrieves a position by name.
     * @param positionName The name of the position.
     * @return The position, or null if not found.
     */
    public Position getPosition(String positionName) {
        return employeeManager.getPosition(positionName);
    }

    /**
     * Retrieves all positions in the system.
     * @return A list of all positions.
     */
    public List<Position> getAllPositions() {
        return employeeManager.getAllPositions();
    }

    /**
     * Adds a qualification for a position to an employee.
     * @param employeeId The employee's ID number.
     * @param positionName The name of the position.
     * @return True if the addition succeeded, false otherwise.
     */
    public boolean addQualificationToEmployee(String employeeId, String positionName) {
        return employeeManager.addQualificationToEmployee(employeeId, positionName);
    }

    /**
     * Retrieves employees qualified for a specific position.
     * @param position The position.
     * @return A list of qualified employees.
     */
    public List<Employee> getQualifiedEmployeesForPosition(Position position) {
        return employeeManager.getQualifiedEmployeesForPosition(position);
    }

    // Required positions management for shifts

    /**
     * Adds a required position for a shift.
     * @param shiftType The type of shift.
     * @param positionName The name of the position.
     * @param count The required number of employees.
     * @return True if the addition succeeded, false otherwise.
     */
    public boolean addRequiredPosition(ShiftType shiftType, String positionName, int count) {
        return employeeManager.addRequiredPosition(shiftType, positionName, count);
    }

    /**
     * Retrieves information about required positions.
     * @return An object containing the required positions.
     */
    public RequiredPositions getRequiredPositions() {
        return employeeManager.getRequiredPositions();
    }

    // Shift management

    /**
     * Creates a new shift.
     * @param date The date of the shift.
     * @param shiftType The type of shift.
     * @return The new shift, or null if creation failed.
     */
    public Shift createShift(LocalDate date, ShiftType shiftType) {
        return employeeManager.createShift(date, shiftType);
    }

    /**
     * Retrieves a shift by date and type.
     * @param date The date of the shift.
     * @param shiftType The type of shift.
     * @return The shift, or null if not found.
     */
    public Shift getShift(LocalDate date, ShiftType shiftType) {
        return employeeManager.getShift(date, shiftType);
    }

    /**
     * Retrieves all shifts in the system.
     * @return A list of all shifts.
     */
    public List<Shift> getAllShifts() {
        return employeeManager.getAllShifts();
    }

    /**
     * Retrieves a list of employees available for a shift.
     * @param date The date of the shift.
     * @param shiftType The type of shift.
     * @return A list of available employees.
     */
    public List<Employee> getAvailableEmployeesForShift(LocalDate date, ShiftType shiftType) {
        return employeeManager.getAvailableEmployeesForShift(date, shiftType);
    }

    /**
     * Assigns an employee to a shift in a specific position.
     * @param shiftId The shift ID.
     * @param employeeId The employee's ID number.
     * @param positionName The name of the position.
     * @return True if the assignment succeeded, false otherwise.
     */
    public boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName) {
        return employeeManager.assignEmployeeToShift(shiftId, employeeId, positionName);
    }

    /**
     * Removes an employee assignment from a shift.
     * @param shiftId The shift ID.
     * @param positionName The name of the position.
     * @return True if the removal succeeded, false otherwise.
     */
    public boolean removeAssignmentFromShift(String shiftId, String positionName) {
        return employeeManager.removeAssignmentFromShift(shiftId, positionName);
    }

    /**
     * Checks if all required positions in a shift are covered.
     * @param shiftId The shift ID.
     * @return True if all positions are covered, false otherwise.
     */
    public boolean areAllRequiredPositionsCovered(String shiftId) {
        return employeeManager.areAllRequiredPositionsCovered(shiftId);
    }

    /**
     * Checks if a shift has a shift manager.
     * @param shiftId The shift ID.
     * @return True if there is a shift manager, false otherwise.
     */
    public boolean hasShiftManager(String shiftId) {
        return employeeManager.hasShiftManager(shiftId);
    }

    /**
     * Resets the system (mainly for testing purposes).
     */
    public void resetAll() {
        employeeManager.resetAll();
    }
}
