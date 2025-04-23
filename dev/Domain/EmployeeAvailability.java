package Domain;

import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.Map;

/**
 * The EmployeeAvailability class tracks an employee's availability for scheduling shifts.
   By default, employees are considered available for all shifts.
 */
public class EmployeeAvailability {
    private String employeeId; // ID of the employee this availability belongs to
    private Map<DayOfWeek, DailyAvailability> weeklyAvailability; // Map of availability for each day of the week

    /**
     * Constructs a new EmployeeAvailability for a specific employee.
     * By default, initializes the employee as available for all shifts on all days.
     *
     * @param employeeId The ID of the employee this availability belongs to
     */
    public EmployeeAvailability(String employeeId) {
        this.employeeId = employeeId;
        this.weeklyAvailability = new EnumMap<>(DayOfWeek.class);
        // Initialize all days to be available for both morning and evening shifts
        for (DayOfWeek day : DayOfWeek.values()) {
            weeklyAvailability.put(day, new DailyAvailability(true, true));
        }
    }

    /**
     * Updates the employee's availability for a specific day of the week.
     * This method allows setting both morning and evening availability at once.
     */
    public void updateAvailability(DayOfWeek day, boolean morningAvailable, boolean eveningAvailable) {
        weeklyAvailability.put(day, new DailyAvailability(morningAvailable, eveningAvailable));
    }

    /**
     * Updates the employee's availability for a specific shift type on a specific day.
     */
    public void updateAvailability(DayOfWeek day, ShiftType shiftType, boolean available) {
        // Get current availability or create a default one if not present
        DailyAvailability currentAvailability = weeklyAvailability.get(day);
        if (currentAvailability == null) {
            currentAvailability = new DailyAvailability(true, true);
        }

        // Preserve existing availability values
        boolean morningAvailable = currentAvailability.isMorningAvailable();
        boolean eveningAvailable = currentAvailability.isEveningAvailable();

        // Update only the specified shift type
        if (shiftType == ShiftType.MORNING) {
            morningAvailable = available;
        } else {
            eveningAvailable = available;
        }

        // Store the updated availability
        weeklyAvailability.put(day, new DailyAvailability(morningAvailable, eveningAvailable));
    }

    /**
     * Checks if the employee is available for a specific shift on a specific day.
     */
    public boolean isAvailable(DayOfWeek day, ShiftType shiftType) {
        DailyAvailability availability = weeklyAvailability.get(day);
        if (availability == null) {
            return false; // No availability information means not available
        }
        if (shiftType == ShiftType.EVENING) {
            return availability.isEveningAvailable();
        } else return availability.isMorningAvailable();
    }

    /**
     * Inner class representing an employee's availability for a single day.
     * Each day has separate availability flags for morning and evening shifts.
     */
    public static class DailyAvailability {
        private boolean morningAvailable; // Whether the employee is available for morning shifts
        private boolean eveningAvailable; // Whether the employee is available for evening shifts

        /**
         * Constructs a new DailyAvailability with specified availability for morning and evening shifts.
         */
        public DailyAvailability(boolean morningAvailable, boolean eveningAvailable) {
            this.morningAvailable = morningAvailable;
            this.eveningAvailable = eveningAvailable;
        }

        /**
         * @return true if the employee is available for morning shifts
         */
        public boolean isMorningAvailable() {
            return morningAvailable;
        }

        /**
         * @return true if the employee is available for evening shifts
         */
        public boolean isEveningAvailable() {
            return eveningAvailable;
        }


        @Override
        public String toString() {
            return (morningAvailable ? "Morning: Available" : "Morning: Unavailable") + ", " +
                    (eveningAvailable ? "Evening: Available" : "Evening: Unavailable");
        }
    }

    /**
     * Returns a formatted string representation of the employee's availability for the entire week.
     *
     * @return A formatted string with the employee's availability for each day of the week
     */
    @Override
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