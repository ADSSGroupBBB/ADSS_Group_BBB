package Domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * The Shift class represents a work shift in the organization.
 * Each shift has a specific date, type (morning or evening), and assigned employees for various positions.
 */
public class Shift {
    private String id; // Unique identifier for the shift
    private LocalDate date; // Date of the shift
    private ShiftType shiftType; // Type of shift (morning or evening)
    private Map<Position, Employee> assignedEmployees; // Employees assigned to positions
    private Employee shiftManager; // Employee designated as the shift manager
    private String startTime; // Shift start time in "HH:MM" format
    private String endTime; // Shift end time in "HH:MM" format

    /**
     * Constructs a new Shift with the specified ID, date, and shift type.
     * Automatically sets the start and end times based on the configured shift hours.
     *
     * @param id The unique identifier for the shift
     * @param date The date on which the shift occurs
     * @param shiftType The type of shift (MORNING or EVENING)
     */
    public Shift(String id, LocalDate date, ShiftType shiftType) {
        this.id = id;
        this.date = date;
        this.shiftType = shiftType;
        this.assignedEmployees = new HashMap<>();
        this.shiftManager = null;
        String[] hours = EmployeeManager.getInstance().getShiftHours(shiftType);
        this.startTime = hours[0];
        this.endTime = hours[1];
    }

    /**
     * Gets the employee designated as the shift manager.
     *
     * @return The shift manager employee, or null if no shift manager is assigned
     */
    public Employee getShiftManager() {
        return shiftManager;
    }

    /**
     * Gets a defensive copy of all assigned employees and their positions.
     *
     * @return A map of positions to assigned employees
     */
    public Map<Position, Employee> getAllAssignedEmployees() {
        return new HashMap<>(assignedEmployees);
    }

    /**
     * Gets the shift start time as a LocalTime object.
     *
     * @return The start time of the shift
     */
    public LocalTime getStartTime() {
        return LocalTime.parse(startTime);
    }

    /**
     * Gets the shift end time as a LocalTime object.
     *
     * @return The end time of the shift
     */
    public LocalTime getEndTime() {
        return LocalTime.parse(endTime);
    }

    /**
     * Gets the date of the shift.
     *
     * @return The date on which this shift occurs
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns a string representation of the shift.
     *
     * @return A string describing the shift type and date
     */
    @Override
    public String toString() {
        return "shift " + getShiftTypeString() + " on date " + date;
    }

    /**
     * Gets a human-readable representation of the shift type.
     *
     * @return "Morning" or "Evening" depending on the shift type
     */
    public String getShiftTypeString() {
        return shiftType == ShiftType.EVENING ? "Evening" : "Morning";
    }

    /**
     * Assigns an employee to a specific position in this shift.
     * This method performs several validation checks before making the assignment:
     * If the position requires shift manager privileges, the employee is also designated as the shift manager.
     *
     * @param position The position to assign the employee to
     * @param employee The employee to assign
     * @return true if the assignment was successful, false if any validation check failed
     */
    public boolean assignEmployee(Position position, Employee employee) {
        // Check if the employee is already assigned to this shift
        if (assignedEmployees.containsValue(employee)) {
            return false;
        }

        // Check if the employee is qualified for the position
        if (!employee.isQualifiedFor(position)) {
            return false;
        }

        // Check if the employee is available for this shift
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (!employee.getAvailability().isAvailable(dayOfWeek, shiftType)) {
            return false;
        }

        // If position requires shift manager, set this employee as shift manager
        if (position.isRequiresShiftManager()) {
            shiftManager = employee;
        }

        // Assign the employee to the position
        assignedEmployees.put(position, employee);
        return true;
    }

    /**
     * Removes an employee assignment from a specific position in this shift.
     *
     * @param position The position to remove the assignment from
     * @return true if an assignment was removed, false if the position had no assignment
     */
    public boolean removeAssignment(Position position) {
        Employee removedEmployee = assignedEmployees.remove(position);

        if (removedEmployee != null && position.isRequiresShiftManager()) {
            shiftManager = null;
        }

        return removedEmployee != null;
    }

    /**
     * Gets the unique identifier of the shift.
     *
     * @return The shift ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the shift type (MORNING or EVENING).
     *
     * @return The shift type
     */
    public ShiftType getShiftType() {
        return shiftType;
    }
}