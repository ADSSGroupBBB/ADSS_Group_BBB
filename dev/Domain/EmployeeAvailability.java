
package Domain;

import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.Map;

public class EmployeeAvailability {
    private String employeeId;
    private Map<DayOfWeek, DailyAvailability> weeklyAvailability;

    public EmployeeAvailability(String employeeId) {
        this.employeeId = employeeId;
        this.weeklyAvailability = new EnumMap<>(DayOfWeek.class);

        // איתחול זמינות ברירת מחדל - זמין לכל המשמרות
        for (DayOfWeek day : DayOfWeek.values()) {
            weeklyAvailability.put(day, new DailyAvailability(true, true));
        }
    }

    public void updateAvailability(DayOfWeek day, boolean morningAvailable, boolean eveningAvailable) {
        weeklyAvailability.put(day, new DailyAvailability(morningAvailable, eveningAvailable));
    }

    public void updateAvailability(DayOfWeek day, ShiftType shiftType, boolean available) {
        DailyAvailability currentAvailability = weeklyAvailability.get(day);
        if (currentAvailability == null) {
            // אם אין רשומת זמינות ליום זה, נאתחל עם ערכי ברירת מחדל
            currentAvailability = new DailyAvailability(true, true);
        }

        boolean morningAvailable = currentAvailability.isMorningAvailable();
        boolean eveningAvailable = currentAvailability.isEveningAvailable();

        if (shiftType == ShiftType.MORNING) {
            morningAvailable = available;
        } else {
            eveningAvailable = available;
        }

        weeklyAvailability.put(day, new DailyAvailability(morningAvailable, eveningAvailable));
    }

    public boolean isAvailable(DayOfWeek day, ShiftType shiftType) {
        DailyAvailability availability = weeklyAvailability.get(day);
        if (availability == null) {
            return false;
        }
        if (shiftType == ShiftType.EVENING) {
            return availability.isEveningAvailable();
        } else return availability.isMorningAvailable();
    }


//
//    public String getEmployeeId() {
//        return employeeId;
//    }

    public static class DailyAvailability {
        private boolean morningAvailable;
        private boolean eveningAvailable;

        public DailyAvailability(boolean morningAvailable, boolean eveningAvailable) {
            this.morningAvailable = morningAvailable;
            this.eveningAvailable = eveningAvailable;
        }

        public boolean isMorningAvailable() {
            return morningAvailable;
        }

        public boolean isEveningAvailable() {
            return eveningAvailable;
        }

        @Override
        public String toString() {
            return (morningAvailable ? "Morning: Available" : ":Morning: Unavailable") + ", " + (eveningAvailable ? "Evening:Available" : "Evening: Unavailable");
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Employee Availability " + employeeId + ":\n");
        for (DayOfWeek day : DayOfWeek.values()) {
            DailyAvailability availability = weeklyAvailability.get(day);
            if (availability != null) {
                sb.append(day).append(": ").append(availability.toString()).append("\n");
            }
        }
        return sb.toString();
    }
}