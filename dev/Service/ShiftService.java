package Service;

import Domain.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Dedicated service for managing shifts and assignments.
 * Focuses on shift-specific operations, separating responsibility from EmployeeService.
 */
public class ShiftService {
    private EmployeeManager employeeManager;

    public ShiftService() {
        this.employeeManager = EmployeeManager.getInstance();
    }

    /**
     * Creates shifts for a given week.
     * @param startDate The start date of the week.
     * @return A list of the created shifts.
     */
    public List<Shift> createShiftsForWeek(LocalDate startDate) {
        List<Shift> createdShifts = new ArrayList<>();
        LocalDate currentDate = startDate;

        // Create shifts for 7 days (a week)
        for (int i = 0; i < 7; i++) {
            // Create morning shift
            Shift morningShift = employeeManager.createShift(currentDate, ShiftType.MORNING);
            if (morningShift != null) {
                createdShifts.add(morningShift);
            }

            // Create evening shift
            Shift eveningShift = employeeManager.createShift(currentDate, ShiftType.EVENING);
            if (eveningShift != null) {
                createdShifts.add(eveningShift);
            }

            // Move to the next day
            currentDate = currentDate.plusDays(1);
        }

        return createdShifts;
    }

    /**
     * Retrieves all future shifts in the system (from today onwards).
     * @return A list of future shifts.
     */
    public List<Shift> getFutureShifts() {
        LocalDate today = LocalDate.now();
        return employeeManager.getAllShifts().stream()
                .filter(shift -> shift.getDate().isEqual(today) || shift.getDate().isAfter(today))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all historical shifts in the system (before today).
     * @return A list of historical shifts.
     */
    public List<Shift> getHistoricalShifts() {
        LocalDate today = LocalDate.now();
        return employeeManager.getAllShifts().stream()
                .filter(shift -> shift.getDate().isBefore(today))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the shift history for a specific employee.
     * @param employeeId The employee's ID number.
     * @return A list of shifts the employee was assigned to.
     */
    public List<Shift> getEmployeeShiftHistory(String employeeId) {
        return employeeManager.getAllShifts().stream()
                .filter(shift -> {
                    Map<Position, Employee> assignedEmployees = shift.getAllAssignedEmployees();
                    return assignedEmployees.values().stream()
                            .anyMatch(employee -> employee.getId().equals(employeeId));
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the future shifts for a specific employee.
     * @param employeeId The employee's ID number.
     * @return A list of future shifts the employee is assigned to.
     */
    public List<Shift> getEmployeeFutureShifts(String employeeId) {
        LocalDate today = LocalDate.now();
        return getEmployeeShiftHistory(employeeId).stream()
                .filter(shift -> shift.getDate().isEqual(today) || shift.getDate().isAfter(today))
                .collect(Collectors.toList());
    }

    /**
     * Checks if an employee can be assigned to a shift in a specific position.
     * @param shiftId The shift ID.
     * @param employeeId The employee's ID number.
     * @param positionName The name of the position.
     * @return True if the employee can be assigned (qualified and available), false otherwise.
     */
    public boolean canAssignEmployeeToShift(String shiftId, String employeeId, String positionName) {
        Shift shift = employeeManager.getShift(shiftId);
        Employee employee = employeeManager.getEmployee(employeeId);
        Position position = employeeManager.getPosition(positionName);

        if (shift == null || employee == null || position == null) {
            return false;
        }

        // Check if employee is qualified for the position
        if (!employee.isQualifiedFor(position)) {
            return false;
        }

        // Check availability
        return employee.getAvailability().isAvailable(shift.getDate().getDayOfWeek(), shift.getShiftType());
    }

    /**
     * Retrieves a list of all missing positions for a shift (required positions that are not filled).
     * @param shiftId The shift ID.
     * @return A list of missing positions.
     */
    public List<Position> getMissingPositionsForShift(String shiftId) {
        Shift shift = employeeManager.getShift(shiftId);
        if (shift == null) {
            return new ArrayList<>();
        }

        RequiredPositions requiredPositions = employeeManager.getRequiredPositions();
        Map<Position, Integer> required = requiredPositions.getRequiredPositionsMap(shift.getShiftType());
        Map<Position, Employee> assigned = shift.getAllAssignedEmployees();

        // Create a list of missing positions
        List<Position> missingPositions = new ArrayList<>();

        for (Map.Entry<Position, Integer> entry : required.entrySet()) {
            Position position = entry.getKey();
            int requiredCount = entry.getValue();

            // Number of employees currently assigned to this position
            long currentCount = assigned.keySet().stream()
                    .filter(p -> p.getName().equals(position.getName()))
                    .count();

            // If there are missing employees for this position, add to the list
            if (currentCount < requiredCount) {
                for (int i = 0; i < (requiredCount - currentCount); i++) {
                    missingPositions.add(position);
                }
            }
        }

        return missingPositions;
    }
}
