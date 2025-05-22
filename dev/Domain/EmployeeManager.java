package Domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of IEmployeeManager interface.
 * Responsible for managing employees, positions, shifts, and all their relationships.
 */
public class EmployeeManager implements IEmployeeManager {
    private Map<String, Employee> employees;
    private Map<String, Position> positions;
    private RequiredPositions requiredPositions;
    private Map<ShiftType, String[]> shiftHoursMap;
    private Map<String, Shift> shifts;
    private static final String[] DEFAULT_MORNING_SHIFT = { "07:00", "14:00" };
    private static final String[] DEFAULT_EVENING_SHIFT = { "14:00", "21:00" };

    /**
     * Constructor initializes all collections and default shift hours.
     */
    public EmployeeManager() {
        employees = new HashMap<>();
        positions = new HashMap<>();
        requiredPositions = new RequiredPositions();
        shifts = new HashMap<>();
        shiftHoursMap = new HashMap<>();
        // Initialize default shift hours
        shiftHoursMap.put(ShiftType.MORNING, DEFAULT_MORNING_SHIFT);
        shiftHoursMap.put(ShiftType.EVENING, DEFAULT_EVENING_SHIFT);
    }

    @Override
    public boolean addEmployee(Employee employee) {
        if (employee == null || employees.containsKey(employee.getId())) {
            return false;
        }
        employees.put(employee.getId(), employee);
        return true;
    }

    @Override
    public Employee removeEmployee(String employeeId) {
        // Check if the employee is assigned to any future shifts
        LocalDate today = LocalDate.now();
        boolean isAssignedToFutureShift = shifts.values().stream().filter(shift -> shift.getDate().isAfter(today)).anyMatch(shift -> shift.getAllAssignedEmployees().values().stream().anyMatch(emp -> emp.getId().equals(employeeId)));
        if (isAssignedToFutureShift) {   // Cannot remove an employee who is assigned to future shifts
            return null;
        }

        return employees.remove(employeeId);
    }

    @Override
    public Employee getEmployee(String employeeId) {
        return employees.get(employeeId);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees.values());
    }

    @Override
    public boolean updateEmployeeAvailability(String employeeId, DayOfWeek dayOfWeek, boolean morningAvailable, boolean eveningAvailable) {
        Employee employee = employees.get(employeeId);
        if (employee == null) {
            return false;
        }

        employee.getAvailability().updateAvailability(dayOfWeek, morningAvailable, eveningAvailable);
        return true;
    }

    @Override
    public boolean addPosition(Position position) {
        if (position == null || positions.containsKey(position.getName())) {
            return false;
        }
        positions.put(position.getName(), position);

        if (position.isRequiresShiftManager()) {
            requiredPositions.setRequiredPosition(ShiftType.MORNING, position, 1);
            requiredPositions.setRequiredPosition(ShiftType.EVENING, position, 1);
        }

        return true;
    }

    @Override
    public Position getPosition(String positionName) {
        return positions.get(positionName);
    }

    @Override
    public List<Position> getAllPositions() {
        return new ArrayList<>(positions.values());
    }

    @Override
    public boolean addRequiredPosition(ShiftType shiftType, String positionName, int count) {
        Position position = positions.get(positionName);
        if (position == null || count <= 0) {
            return false;
        }

        requiredPositions.setRequiredPosition(shiftType, position, count);
        return true;
    }

    @Override
    public List<Employee> getQualifiedEmployeesForPosition(Position position) {
        return employees.values().stream().filter(employee -> employee.isQualifiedFor(position)).collect(Collectors.toList());
    }

    @Override
    public boolean addQualificationToEmployee(String employeeId, String positionName) {
        Employee employee = employees.get(employeeId);
        Position position = positions.get(positionName);

        if (employee == null || position == null) {
            return false;
        }

        return employee.addQualifiedPosition(position);
    }

    @Override
    public Shift createShift(LocalDate date, ShiftType shiftType) {
        String shiftId = date.toString() + "_" + (shiftType == ShiftType.EVENING ? "evening" : "morning");
        if (shifts.containsKey(shiftId)) {
            return null;
        }

        Shift newShift = new Shift(shiftId, date, shiftType);
        shifts.put(shiftId, newShift);
        return newShift;
    }

    @Override
    public Shift getShift(LocalDate date, ShiftType shiftType) {
        String shiftId = date.toString() + "_" + (shiftType == ShiftType.EVENING ? "evening" : "morning");
        return shifts.get(shiftId);
    }

    @Override
    public Shift getShift(String shiftId) {
        return shifts.get(shiftId);
    }

    @Override
    public List<Shift> getAllShifts() {
        return new ArrayList<>(shifts.values());
    }

//    @Override
//    public boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName) {
//        Shift shift = shifts.get(shiftId);
//        Employee employee = employees.get(employeeId);
//        Position position = positions.get(positionName);
//
//        if (shift == null || employee == null || position == null) {
//            return false;
//        }
//
//        return shift.assignEmployee(position, employee);
//    }

    @Override
    public boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName) {
        Shift shift = shifts.get(shiftId);
        Employee employee = employees.get(employeeId);
        Position position = positions.get(positionName);

        if (shift == null || employee == null || position == null) {
            return false;
        }

        // בדיקת תקן
        RequiredPositions requiredPositions = getRequiredPositions();
        int requiredCount = requiredPositions.getRequiredCount(shift.getShiftType(), position);

        if (requiredCount == 0) {
            return false;
        }

        // בדיקת כמה עובדים כבר משובצים לתפקיד זה
        long currentAssigned = shift.getAllAssignedEmployees().entrySet().stream()
                .filter(entry -> entry.getKey().equals(position))
                .count();

        if (currentAssigned >= requiredCount) {
            return false;
        }

        // וידוא שהעובד מוסמך (אם לא, הוסף הסמכה)
        if (!employee.isQualifiedFor(position)) {
            employee.addQualifiedPosition(position);
        }

        // וידוא זמינות (אם לא זמין, עדכן זמינות)
        DayOfWeek dayOfWeek = shift.getDate().getDayOfWeek();
        if (!employee.getAvailability().isAvailable(dayOfWeek, shift.getShiftType())) {
            employee.getAvailability().updateAvailability(dayOfWeek, true, true);
        }

        // ביצוע השיבוץ
        return shift.assignEmployee(position, employee);
    }

    @Override
    public boolean removeAssignmentFromShift(String shiftId, String positionName) {
        Shift shift = shifts.get(shiftId);
        Position position = positions.get(positionName);

        if (shift == null || position == null) {
            return false;
        }

        return shift.removeAssignment(position);
    }

    @Override
    public boolean areAllRequiredPositionsCovered(String shiftId) {
        Shift shift = shifts.get(shiftId);
        if (shift == null) {
            return false;
        }

        return requiredPositions.areAllRequiredPositionsCovered(shift.getShiftType(), shift.getAllAssignedEmployees());
    }

    @Override
    public boolean updateShiftHours(ShiftType shiftType, String newStartTime, String newEndTime) {
        if (!isValidTimeFormat(newStartTime) || !isValidTimeFormat(newEndTime)) {
            return false;
        }
        shiftHoursMap.put(shiftType, new String[]{ newStartTime, newEndTime });
        return true;
    }

    @Override
    public String[] getShiftHours(ShiftType shiftType) {
        return shiftHoursMap.getOrDefault(shiftType, shiftType == ShiftType.MORNING ? DEFAULT_MORNING_SHIFT : DEFAULT_EVENING_SHIFT);
    }

    private boolean isValidTimeFormat(String time) {
        try {
            java.time.LocalTime.parse(time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteShift(String shiftId) {
        return shifts.remove(shiftId) != null;
    }

    @Override
    public RequiredPositions getRequiredPositions() {
        return requiredPositions;
    }

    @Override
    public List<Employee> getAvailableEmployeesForShift(LocalDate date, ShiftType shiftType) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        Shift shift = getShift(date, shiftType);

        if (shift == null) {
            return new ArrayList<>();
        }

        Map<Position, Integer> requiredPositionsForShift = requiredPositions.getRequiredPositionsMap(shiftType);

        return employees.values().stream().filter(employee -> employee.getAvailability().isAvailable(dayOfWeek, shiftType) &&
                        !shift.getAllAssignedEmployees().containsValue(employee) &&
                        employee.getQualifiedPositions().stream()
                                .anyMatch(requiredPositionsForShift::containsKey)
                )
                .collect(Collectors.toList());
    }
}