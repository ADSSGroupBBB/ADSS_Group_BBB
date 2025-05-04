package Domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

// singleton

/**
 * The EmployeeManager class is responsible for managing all employee-related operations in the system.
 * It implements the Singleton pattern to ensure only one instance exists throughout the application.
 *
 * This class manages:
 * - Employee records and their qualifications
 * - Position definitions and requirements
 * - Shift scheduling and assignments
 * - Shift hours configuration
 */
public class EmployeeManager {
    private static EmployeeManager instance = null;
    private Map<String, Employee> employees;
    private Map<String, Position> positions;
    private RequiredPositions requiredPositions;
    private Map<ShiftType, String[]> shiftHoursMap;// Configuration of shift hours
    private Map<String, Shift> shifts;  // Shift mapping by shift ID
    private static final String[] DEFAULT_MORNING_SHIFT = { "07:00", "14:00" };
    private static final String[] DEFAULT_EVENING_SHIFT = { "14:00", "21:00" };

    /**
     * Private constructor to enforce the Singleton pattern.
     * Initializes all collections and default shift hours.
     */
    private EmployeeManager() {
        employees = new HashMap<>();
        positions = new HashMap<>();
        requiredPositions = new RequiredPositions();
        shifts = new HashMap<>();
        shiftHoursMap = new HashMap<>();
        // Initialize default shift hours
        shiftHoursMap.put(ShiftType.MORNING, DEFAULT_MORNING_SHIFT);
        shiftHoursMap.put(ShiftType.EVENING, DEFAULT_EVENING_SHIFT);
    }


    /**
     * Returns the singleton instance of the EmployeeManager.
     * If the instance doesn't exist yet, it will be created.
     *
     * @return The singleton instance of EmployeeManager
     */
    public static EmployeeManager getInstance() {
        if (instance == null) {
            instance = new EmployeeManager();
        }
        return instance;
    }



    /**
     * Adds a new employee to the system.
     *
     * @param employee The employee to be added
     * @return true if the employee was successfully added, false if the employee is null or already exists
     */
    public boolean addEmployee(Employee employee) {
        if (employee == null || employees.containsKey(employee.getId())) {
            return false;
        }
        employees.put(employee.getId(), employee);
        return true;
    }


    /**
     * Retrieves the configured hours for a specific shift type.
     *
     * @param shiftType The shift type (MORNING or EVENING)
     * @return An array containing start time and end time strings in format "HH:MM"
     */
    public String[] getShiftHours(ShiftType shiftType) {
        return shiftHoursMap.getOrDefault(shiftType, shiftType == ShiftType.MORNING ? DEFAULT_MORNING_SHIFT : DEFAULT_EVENING_SHIFT);
    }

    /**
     * Updates the configured hours for a specific shift type.
     * Both times must be in valid "HH:MM" format.
     * @return true if the update was successful, false if the time format is invalid
     */
    public boolean updateShiftHours(ShiftType shiftType, String newStartTime, String newEndTime) {
        if (!isValidTimeFormat(newStartTime) || !isValidTimeFormat(newEndTime)) {
            return false;
        }
        shiftHoursMap.put(shiftType, new String[]{ newStartTime, newEndTime });
        return true;
    }


    /**
     * Validates that a time string matches the "HH:MM" format.
     *
     * @param time The time string to validate
     * @return true if the format is valid, false otherwise
     */
    private boolean isValidTimeFormat(String time) {
        try {
            java.time.LocalTime.parse(time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Removes an employee from the system.
     * This operation will fail if the employee is assigned to any future shifts.
     *
     * @param employeeId The ID of the employee to remove
     * @return The removed Employee object, or null if removal failed
     */
    public Employee removeEmployee(String employeeId) {
        // Check if the employee is assigned to any future shifts
        LocalDate today = LocalDate.now();
        boolean isAssignedToFutureShift = shifts.values().stream()
                .filter(shift -> shift.getDate().isAfter(today))
                .anyMatch(shift -> shift.getAllAssignedEmployees().values().stream()
                        .anyMatch(emp -> emp.getId().equals(employeeId)));

        if (isAssignedToFutureShift) {   // Cannot remove an employee who is assigned to future shifts
            return null;
        }

        return employees.remove(employeeId);
    }






    public Employee getEmployee(String employeeId) {
        return employees.get(employeeId);
    }

    /**
     * Retrieves all employees in the system.
     *
     * @return A list containing all employees
     */
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees.values());
    }

    /**
     * Retrieves all employees who are qualified for a specific position.
     *
     * @param position The position to check qualifications for
     * @return A list of employees qualified for the given position
     */
    public List<Employee> getQualifiedEmployeesForPosition(Position position) {// ger all workers that qualified for a specific role
        return employees.values().stream().filter(employee -> employee.isQualifiedFor(position)).collect(Collectors.toList());
    }

    /**
     * Retrieves all employees who are available for a specific shift.
     * An employee is considered available if:
     * 1. They are available on the day and shift type according to their availability settings
     * 2. They are not already assigned to this shift
     * 3. They are qualified for at least one of the required positions for this shift
     *
     * @param date The date of the shift
     * @param shiftType The type of the shift (MORNING or EVENING)
     * @return A list of available employees who meet all criteria
     */
    public List<Employee> getAvailableEmployeesForShift(LocalDate date, ShiftType shiftType) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        Shift shift = getShift(date, shiftType);

        if (shift == null) {
            return new ArrayList<>();
        }

        // Get the required positions for this shift type
        Map<Position, Integer> requiredPositionsForShift = requiredPositions.getRequiredPositionsMap(shiftType);

        return employees.values().stream()
                .filter(employee ->
                        // Available on this day and shift type
                        employee.getAvailability().isAvailable(dayOfWeek, shiftType) &&
                                // Not already assigned to this shift
                                !shift.getAllAssignedEmployees().containsValue(employee) &&
                                // Qualified for at least one of the required positions
                                employee.getQualifiedPositions().stream()
                                        .anyMatch(requiredPositionsForShift::containsKey)
                )
                .collect(Collectors.toList());
    }




    /**
     * Adds a qualification for a specific position to an employee.
     *
     * @param employeeId The ID of the employee
     * @param positionName The name of the position to add as a qualification
     * @return true if the qualification was successfully added, false if the employee or position doesn't exist
     */
    public boolean addQualificationToEmployee(String employeeId, String positionName) {
        Employee employee = employees.get(employeeId);
        Position position = positions.get(positionName);

        if (employee == null || position == null) {
            return false;
        }

        return employee.addQualifiedPosition(position);
    }

    /**
     * Updates an employee's availability for a specific day of the week.
     *
     * @param employeeId The ID of the employee
     * @param dayOfWeek The day of the week to update
     * @param morningAvailable Whether the employee is available for morning shifts
     * @param eveningAvailable Whether the employee is available for evening shifts
     * @return true if the update was successful, false if the employee doesn't exist
     */
    public boolean updateEmployeeAvailability(String employeeId, DayOfWeek dayOfWeek,
                                              boolean morningAvailable, boolean eveningAvailable) {
        Employee employee = employees.get(employeeId);
        if (employee == null) {
            return false;
        }

        employee.getAvailability().updateAvailability(dayOfWeek, morningAvailable, eveningAvailable);
        return true;
    }


    /**
     * Adds a new position to the system.
     *
     * @param position The position to add
     * @return true if the position was successfully added, false if the position is null or already exists
     */
//    public boolean addPosition(Position position) {
//        if (position == null || positions.containsKey(position.getName())) {
//            return false;
//        }
//        positions.put(position.getName(), position);
//        return true;
//    }
    public boolean addPosition(Position position) {
        if (position == null || positions.containsKey(position.getName())) {
            return false;
        }
        positions.put(position.getName(), position);

        // אם זה תפקיד מנהל משמרת, הוסף אוטומטית דרישה למשמרות
        if (position.isRequiresShiftManager()) {
            requiredPositions.setRequiredPosition(ShiftType.MORNING, position, 1);
            requiredPositions.setRequiredPosition(ShiftType.EVENING, position, 1);
        }

        return true;
    }

    public Position getPosition(String positionName) {
        return positions.get(positionName);
    }

    /**
     * Retrieves all positions in the system.
     *
     * @return A list containing all positions
     */
    public List<Position> getAllPositions() {
        return new ArrayList<>(positions.values());
    }


    /**
     * Defines a required position for a shift type.
     * This specifies how many employees with a specific qualification are needed for each shift type.
     *
     * @param shiftType The type of shift (MORNING or EVENING)
     * @param positionName The name of the required position
     * @param count The number of employees required for this position
     * @return true if the requirement was successfully added, false if the position doesn't exist or count is invalid
     */
    public boolean addRequiredPosition(ShiftType shiftType, String positionName, int count) {
        Position position = positions.get(positionName);
        if (position == null || count <= 0) {
            return false;
        }

        requiredPositions.setRequiredPosition(shiftType, position, count);
        return true;
    }


    public RequiredPositions getRequiredPositions() {
        return requiredPositions;
    }



    /**
     * Creates a new shift for a specific date and shift type.
     * Each shift has a unique ID based on its date and type.
     *
     * @param date The date for the shift
     * @param shiftType The type of shift (MORNING or EVENING)
     * @return The newly created Shift object, or null if a shift already exists for this date and type
     */
    public Shift createShift(LocalDate date, ShiftType shiftType) {
        String shiftId = date.toString() + "_" + (shiftType == ShiftType.EVENING ? "evening" : "morning");
        if (shifts.containsKey(shiftId)) {
            return null;
        }

        Shift newShift = new Shift(shiftId, date, shiftType);
        shifts.put(shiftId, newShift);
        return newShift;
    }




    public Shift getShift(LocalDate date, ShiftType shiftType) {
        String shiftId = date.toString() + "_" + (shiftType == ShiftType.EVENING ? "evening" : "morning");
        return shifts.get(shiftId);
    }

    public Shift getShift(String shiftId) {
        return shifts.get(shiftId);
    }

    public List<Shift> getAllShifts() {
        return new ArrayList<>(shifts.values());
    }

    /**
     * Assigns an employee to a specific position in a shift.
     *
     * @param shiftId The ID of the shift
     * @param employeeId The ID of the employee
     * @param positionName The name of the position to assign
     * @return true if the assignment was successful, false if the shift, employee, or position doesn't exist
     */
    public boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName) {
        Shift shift = shifts.get(shiftId);
        Employee employee = employees.get(employeeId);
        Position position = positions.get(positionName);

        if (shift == null || employee == null || position == null) {
            return false;
        }

        return shift.assignEmployee(position, employee);
    }


    /**
     * Removes an employee assignment from a specific position in a shift.
     *
     * @param shiftId The ID of the shift
     * @param positionName The name of the position to remove the assignment from
     * @return true if the assignment was successfully removed, false if the shift or position doesn't exist
     */
    public boolean removeAssignmentFromShift(String shiftId, String positionName) {
        Shift shift = shifts.get(shiftId);
        Position position = positions.get(positionName);

        if (shift == null || position == null) {
            return false;
        }

        return shift.removeAssignment(position);
    }


    /**
     * Checks if all required positions for a shift are covered by assigned employees.
     *
     * @param shiftId The ID of the shift to check
     * @return true if all required positions are covered, false otherwise
     */
    public boolean areAllRequiredPositionsCovered(String shiftId) {
        Shift shift = shifts.get(shiftId);
        if (shift == null) {
            return false;
        }

        return requiredPositions.areAllRequiredPositionsCovered(shift.getShiftType(), shift.getAllAssignedEmployees());
    }

    public boolean deleteShift(String shiftId) {
        return shifts.remove(shiftId) != null;
    }


}