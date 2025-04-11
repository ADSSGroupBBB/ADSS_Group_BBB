package Domain;
import java.util.stream.Collectors;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class EmployeeManager {
    private static EmployeeManager instance = null;
    private Map<String, Employee> employees; // מיפוי לפי ת"ז
    private Map<String, Position> positions; // מיפוי לפי מזהה תפקיד
    private RequiredPositions requiredPositions;
    private Map<String, Shift> shifts; // מיפוי לפי מזהה משמרת


    private EmployeeManager() {
        employees = new HashMap<>();
        positions = new HashMap<>();
        requiredPositions = new RequiredPositions();
        shifts = new HashMap<>();
    }


    public static EmployeeManager getInstance() {
        if (instance == null) {
            instance = new EmployeeManager();
        }
        return instance;
    }


    public boolean addEmployee(Employee employee) {
        if (employee == null || employees.containsKey(employee.getId())) {
            return false;
        }
        employees.put(employee.getId(), employee);
        return true;
    }


    public Employee removeEmployee(String employeeId) {
        // יש לבדוק שהעובד לא משובץ למשמרות עתידיות
        LocalDate today = LocalDate.now();
        boolean isAssignedToFutureShift = shifts.values().stream()
                .filter(shift -> shift.getDate().isAfter(today))
                .anyMatch(shift -> shift.getAllAssignedEmployees().values().stream()
                        .anyMatch(emp -> emp.getId().equals(employeeId)));

        if (isAssignedToFutureShift) {
            // אי אפשר להסיר עובד שמשובץ למשמרות עתידיות
            return null;
        }

        return employees.remove(employeeId);
    }


    public Employee getEmployee(String employeeId) {
        return employees.get(employeeId);
    }


    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees.values());
    }

    public List<Employee> getQualifiedEmployeesForPosition(Position position) {
        return employees.values().stream().filter(employee -> employee.isQualifiedFor(position)).collect(Collectors.toList());
    }

    public List<Employee> getAvailableEmployeesForShift(LocalDate date, ShiftType shiftType) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return employees.values().stream().filter(employee -> employee.getAvailability().isAvailable(dayOfWeek, shiftType)).collect(Collectors.toList());
    }

    public boolean addQualificationToEmployee(String employeeId, String positionId) {
        Employee employee = employees.get(employeeId);
        Position position = positions.get(positionId);

        if (employee == null || position == null) {
            return false;
        }

        return employee.addQualifiedPosition(position);
    }

    public boolean addPosition(Position position) {
        if (position == null || positions.containsKey(position.getName())) {
            return false;
        }
        positions.put(position.getName(), position);
        return true;
    }

    public List<Position> getAllPositions() {
        return new ArrayList<>(positions.values());
    }


    public boolean addRequiredPosition(ShiftType shiftType, String positionId, int count) {
        Position position = positions.get(positionId);
        if (position == null || count <= 0) {
            return false;
        }

        requiredPositions.setRequiredPosition(shiftType, position, count);
        return true;
    }

    public RequiredPositions getRequiredPositions() {
        return requiredPositions;
    }

    public Shift createShift(LocalDate date, ShiftType shiftType) {
        // יצירת מזהה ייחודי למשמרת
        String shiftId = date.toString() + "_" + (shiftType == ShiftType.EVENING ? "evening" : "morning");

        // בדיקה אם המשמרת כבר קיימת
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


    public boolean assignEmployeeToShift(String shiftId, String employeeId, String positionId) {
        Shift shift = shifts.get(shiftId);
        Employee employee = employees.get(employeeId);
        Position position = positions.get(positionId);

        if (shift == null || employee == null || position == null) {
            return false;
        }

        return shift.assignEmployee(position, employee); /////?????
    }

    public boolean removeAssignmentFromShift(String shiftId, String positionId) {
        Shift shift = shifts.get(shiftId);
        Position position = positions.get(positionId);

        if (shift == null || position == null) {
            return false;
        }

        return shift.removeAssignment(position);
    }


    public boolean updateEmployeeAvailability(String employeeId, DayOfWeek dayOfWeek, boolean morningAvailable, boolean eveningAvailable) {
        Employee employee = employees.get(employeeId);
        if (employee == null) {
            return false;
        }

        employee.getAvailability().updateAvailability(dayOfWeek, morningAvailable, eveningAvailable);
        return true;
    }

    public boolean updateEmployeeAvailability(String employeeId, DayOfWeek dayOfWeek,
                                              ShiftType shiftType, boolean available) {
        Employee employee = employees.get(employeeId);
        if (employee == null) {
            return false;
        }

        employee.getAvailability().updateAvailability(dayOfWeek, shiftType, available);
        return true;
    }


    public void resetAll() {
        employees.clear();
        positions.clear();
        shifts.clear();
        requiredPositions = new RequiredPositions();
    }
}