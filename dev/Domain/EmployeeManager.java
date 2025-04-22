package Domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

// singleton
public class EmployeeManager {
    private static EmployeeManager instance = null;
    private Map<String, Employee> employees; // מיפוי לפי ת"ז
    private Map<String, Position> positions; // מיפוי לפי שם תפקיד
    private RequiredPositions requiredPositions;
    private Map<ShiftType, String[]> shiftHoursMap;
    private Map<String, Shift> shifts; // מיפוי לפי מזהה משמרת
    private static final String[] DEFAULT_MORNING_SHIFT = { "07:00", "14:00" };
    private static final String[] DEFAULT_EVENING_SHIFT = { "14:00", "21:00" };


    private EmployeeManager() {
        employees = new HashMap<>();
        positions = new HashMap<>();
        requiredPositions = new RequiredPositions();
        shifts = new HashMap<>();
        shiftHoursMap = new HashMap<>();   //

        // אתחול שעות ברירת מחדל
        shiftHoursMap.put(ShiftType.MORNING, DEFAULT_MORNING_SHIFT);
        shiftHoursMap.put(ShiftType.EVENING, DEFAULT_EVENING_SHIFT);
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
    public String[] getShiftHours(ShiftType shiftType) {
        return shiftHoursMap.getOrDefault(shiftType, shiftType == ShiftType.MORNING ? DEFAULT_MORNING_SHIFT : DEFAULT_EVENING_SHIFT);
    }

    //  עדכון שעות משמרת
    public boolean updateShiftHours(ShiftType shiftType, String newStartTime, String newEndTime) {
        if (!isValidTimeFormat(newStartTime) || !isValidTimeFormat(newEndTime)) {
            return false;  // פורמט שגוי
        }
        shiftHoursMap.put(shiftType, new String[]{ newStartTime, newEndTime });
        return true;
    }
    // בדיקת תקינות פורמט שעה (HH:mm)
    private boolean isValidTimeFormat(String time) {
        try {
            java.time.LocalTime.parse(time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public Employee removeEmployee(String employeeId) {
        // בדיקה שהעובד לא משובץ למשמרות עתידיות
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


    public List<Employee> getQualifiedEmployeesForPosition(Position position) {// ger all workers that qualified for a specific role
        return employees.values().stream().filter(employee -> employee.isQualifiedFor(position)).collect(Collectors.toList());
    }


//    public List<Employee> getAvailableEmployeesForShift(LocalDate date, ShiftType shiftType) {
//        DayOfWeek dayOfWeek = date.getDayOfWeek();
//        return employees.values().stream()
//                .filter(employee -> employee.getAvailability().isAvailable(dayOfWeek, shiftType))
//                .collect(Collectors.toList());
//    }
public List<Employee> getAvailableEmployeesForShift(LocalDate date, ShiftType shiftType) {
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    Shift shift = getShift(date, shiftType);

    if (shift == null) {
        return new ArrayList<>();
    }

    // מקבל את רשימת התפקידים הנדרשים למשמרת זו
    Map<Position, Integer> requiredPositionsForShift = requiredPositions.getRequiredPositionsMap(shiftType);

    return employees.values().stream()
            .filter(employee ->
                    // זמינות ביום ובסוג משמרת
                    employee.getAvailability().isAvailable(dayOfWeek, shiftType) &&
                            // לא שובץ כבר למשמרת
                            !shift.getAllAssignedEmployees().containsValue(employee) &&
                            // מוסמך לפחות לאחד מהתפקידים הנדרשים
                            employee.getQualifiedPositions().stream()
                                    .anyMatch(requiredPositionsForShift::containsKey)
            )
            .collect(Collectors.toList());
}


    public boolean addQualificationToEmployee(String employeeId, String positionName) {
        Employee employee = employees.get(employeeId);
        Position position = positions.get(positionName);

        if (employee == null || position == null) {
            return false;
        }

        return employee.addQualifiedPosition(position);
    }


    public boolean updateEmployeeAvailability(String employeeId, DayOfWeek dayOfWeek,
                                              boolean morningAvailable, boolean eveningAvailable) {
        Employee employee = employees.get(employeeId);
        if (employee == null) {
            return false;
        }

        employee.getAvailability().updateAvailability(dayOfWeek, morningAvailable, eveningAvailable);
        return true;
    }



    public boolean addPosition(Position position) {
        if (position == null || positions.containsKey(position.getName())) {
            return false;
        }
        positions.put(position.getName(), position);
        return true;
    }


    public Position getPosition(String positionName) {
        return positions.get(positionName);
    }

    public List<Position> getAllPositions() {
        return new ArrayList<>(positions.values());
    }

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


    public boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName) {
        Shift shift = shifts.get(shiftId);
        Employee employee = employees.get(employeeId);
        Position position = positions.get(positionName);

        if (shift == null || employee == null || position == null) {
            return false;
        }

        return shift.assignEmployee(position, employee);
    }

    public boolean removeAssignmentFromShift(String shiftId, String positionName) {
        Shift shift = shifts.get(shiftId);
        Position position = positions.get(positionName);

        if (shift == null || position == null) {
            return false;
        }

        return shift.removeAssignment(position);
    }


    public boolean areAllRequiredPositionsCovered(String shiftId) {
        Shift shift = shifts.get(shiftId);
        if (shift == null) {
            return false;
        }

        return requiredPositions.areAllRequiredPositionsCovered(shift.getShiftType(), shift.getAllAssignedEmployees());
    }


}