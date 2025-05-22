package Domain_employee;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface for EmployeeManager defining operations for managing employees,
 * positions, and shifts in the system.
 */
public interface IEmployeeManager {
    // Employee operations
    boolean addEmployee(Employee employee);
    Employee removeEmployee(String employeeId);
    Employee getEmployee(String employeeId);
    List<Employee> getAllEmployees();
    boolean updateEmployeeAvailability(String employeeId, DayOfWeek dayOfWeek, boolean morningAvailable, boolean eveningAvailable);

    // Position operations
    boolean addPosition(Position position);
    Position getPosition(String positionName);
    List<Position> getAllPositions();
    boolean addRequiredPosition(ShiftType shiftType, String positionName, int count);
    List<Employee> getQualifiedEmployeesForPosition(Position position);
    boolean addQualificationToEmployee(String employeeId, String positionName);

    // Shift operations
    Shift createShift(LocalDate date, ShiftType shiftType);
    Shift getShift(LocalDate date, ShiftType shiftType);
    Shift getShift(String shiftId);
    List<Shift> getAllShifts();
//    boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName);
    boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName);
    boolean removeAssignmentFromShift(String shiftId, String positionName);
    boolean areAllRequiredPositionsCovered(String shiftId);
    boolean updateShiftHours(ShiftType shiftType, String newStartTime, String newEndTime);
    String[] getShiftHours(ShiftType shiftType);
    boolean deleteShift(String shiftId);

    // Required positions operations
    RequiredPositions getRequiredPositions();
    List<Employee> getAvailableEmployeesForShift(LocalDate date, ShiftType shiftType);
}