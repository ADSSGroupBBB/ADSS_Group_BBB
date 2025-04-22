package Domain;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;


public class Shift {
    private String id;
    private LocalDate date;
    private ShiftType shiftType;
    private Map<Position, Employee> assignedEmployees;
    private Employee shiftManager;
    private String startTime;
    private String endTime;


    public Shift(String id, LocalDate date, ShiftType shiftType) {
        this.id = id;
        this.date = date;
        this.shiftType = shiftType;
        this.assignedEmployees = new HashMap<>();
        this.shiftManager = null;

        // קבלת שעות משמרת דינאמיות
        String[] hours = EmployeeManager.getInstance().getShiftHours(shiftType);
        this.startTime = hours[0];
        this.endTime = hours[1];
    }


    public Employee getShiftManager() {
        return shiftManager;
    }


//    public Employee getAssignedEmployee(Position position) {
//
//        return assignedEmployees.get(position);
//    }


    public Map<Position, Employee> getAllAssignedEmployees() {

        return new HashMap<>(assignedEmployees);
    }


    public LocalTime getStartTime() {
        return LocalTime.parse(startTime);  // המרה ל-LocalTime
    }

    public LocalTime getEndTime() {
        return LocalTime.parse(endTime);    // המרה ל-LocalTime
    }

    public LocalDate getDate() {
        return date;
    }


    @Override
    public String toString() {
        return "shift " + getShiftTypeString() + " on date " + date;
    }

    public String getShiftTypeString() {
        return shiftType == ShiftType.EVENING ? "Evening" : "Morning";
    }
    public boolean assignEmployee(Position position, Employee employee) {
        // בדיקה שהעובד כבר לא משובץ למשמרת
        if (assignedEmployees.containsValue(employee)) {
            return false;  // העובד כבר משובץ למשמרת זו
        }

        // בדיקה שהעובד מוסמך לתפקיד
        if (!employee.isQualifiedFor(position)) {
            return false;
        }

        // בדיקת זמינות העובד למשמרת
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (!employee.getAvailability().isAvailable(dayOfWeek, shiftType)) {
            return false;
        }

        // אם זה תפקיד מנהל משמרת, עדכן את מנהל המשמרת
        if (position.isRequiresShiftManager()) {
            shiftManager = employee;
        }

        // שיבוץ העובד לתפקיד
        assignedEmployees.put(position, employee);
        return true;
    }



//    public boolean assignEmployee(Position position, Employee employee) {
//        // בדיקה שהעובד מוסמך לתפקיד
//        if (!employee.isQualifiedFor(position)) {
//            return false;
//        }
//
//        // בדיקת זמינות העובד למשמרת
//        DayOfWeek dayOfWeek = date.getDayOfWeek();
//        if (!employee.getAvailability().isAvailable(dayOfWeek, shiftType)) {
//            return false;
//        }
//
//        // אם זה תפקיד מנהל משמרת, עדכן את מנהל המשמרת
//        if (position.isRequiresShiftManager()) {
//            shiftManager = employee;
//        }
//
//        // שיבוץ העובד לתפקיד
//        assignedEmployees.put(position, employee);
//        return true;
//    }


    public boolean removeAssignment(Position position) {
        Employee removedEmployee = assignedEmployees.remove(position);

        // אם הוסר מנהל משמרת, עדכן את השדה המתאים
        if (removedEmployee != null && position.isRequiresShiftManager()) {
            shiftManager = null;
        }

        return removedEmployee != null;
    }
    public String getId() {

        return id;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }
}


