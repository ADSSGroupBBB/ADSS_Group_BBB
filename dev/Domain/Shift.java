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


    public static final LocalTime MORNING_SHIFT_START = LocalTime.of(7, 0);  // 7:00
    public static final LocalTime MORNING_SHIFT_END = LocalTime.of(14, 0);   // 14:00
    public static final LocalTime EVENING_SHIFT_START = LocalTime.of(14, 0); // 14:00
    public static final LocalTime EVENING_SHIFT_END = LocalTime.of(21, 0);   // 21:00


    public Shift(String id, LocalDate date, ShiftType shiftType) {
        this.id = id;
        this.date = date;
        this.shiftType = shiftType;
        this.assignedEmployees = new HashMap<>();
        this.shiftManager = null;
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

    public LocalTime getEndTime() {

        return shiftType == ShiftType.EVENING ? EVENING_SHIFT_END : MORNING_SHIFT_END;
    }

    public LocalTime getStartTime() {
        return shiftType == ShiftType.EVENING ? EVENING_SHIFT_START : MORNING_SHIFT_START;
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



