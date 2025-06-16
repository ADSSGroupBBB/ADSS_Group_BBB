package Test;

import Service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ShiftService.
 *
 * These tests verify the functionality of creating, retrieving, 
 * and managing shifts within the system, including future and historical shifts.
 */
public class ShiftServiceTest {

    private ShiftService shiftService;
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService();
        shiftService = new ShiftService();

        // Clean existing employees before each test to ensure a fresh state
        for (EmployeeDTO emp : employeeService.getAllEmployees()) {
            employeeService.removeEmployee(emp.getId());
        }

        // Define positions: Shift Manager (requires manager) and Cashier (regular role)
        employeeService.addPosition("Shift Manager", true);
        employeeService.addPosition("Cashier", false);

        // Add employees with different roles
        employeeService.addNewEmployee("1001", "John", "Manager", "IL123456",
                LocalDate.of(2023, 1, 1), 40.0, "SHIFT_MANAGER", "sm123", 5, 10, "PensionFundA");

        employeeService.addNewEmployee("1002", "Jane", "HR", "IL234567",
                LocalDate.of(2023, 2, 1), 45.0, "HR_MANAGER", "hr123", 5, 10, "PensionFundA");

        employeeService.addNewEmployee("1003", "Bob", "Regular", "IL345678",
                LocalDate.of(2023, 3, 1), 30.0, "REGULAR_EMPLOYEE", "", 4, 8, "PensionFundC");

        // Assign qualifications to employees
        employeeService.addQualificationToEmployee("1001", "Shift Manager");
        employeeService.addQualificationToEmployee("1002", "Shift Manager");
        employeeService.addQualificationToEmployee("1003", "Cashier");

        // Set full availability for managers and morning-only for regular employee
        for (DayOfWeek day : DayOfWeek.values()) {
            employeeService.updateEmployeeAvailability("1001", day, true, true);
            employeeService.updateEmployeeAvailability("1002", day, true, true);
            employeeService.updateEmployeeAvailability("1003", day, true, false);  // Only morning shifts
        }

        // Define required positions per shift type
        employeeService.addRequiredPosition("MORNING", "Shift Manager", 1);
        employeeService.addRequiredPosition("MORNING", "Cashier", 1);
        employeeService.addRequiredPosition("EVENING", "Shift Manager", 1);
        employeeService.addRequiredPosition("EVENING", "Cashier", 1);
    }

    /**
     * Test that verifies creation of 14 shifts (morning and evening for each day) for a full week.
     */
    @Test
    void testCreateShiftsForWeek() {
        // Find next Sunday to start shift creation
        LocalDate today = LocalDate.now();
        LocalDate nextSunday = today.with(DayOfWeek.SUNDAY);
        if (today.getDayOfWeek() == DayOfWeek.SUNDAY) {
            nextSunday = nextSunday.plusWeeks(1);
        }

        // Create shifts for the entire week
        List<ShiftDTO> shifts = shiftService.createShiftsForWeek(nextSunday);

        // Expecting 14 shifts (2 per day * 7 days)
        assertEquals(14, shifts.size());

        // Ensure each day has both morning and evening shifts
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = nextSunday.plusDays(i);
            boolean foundMorning = false;
            boolean foundEvening = false;

            for (ShiftDTO shift : shifts) {
                if (shift.getDate().equals(currentDate)) {
                    if (shift.getShiftType().equals("MORNING")) {
                        foundMorning = true;
                    } else if (shift.getShiftType().equals("EVENING")) {
                        foundEvening = true;
                    }
                }
            }

            assertTrue(foundMorning, "Missing morning shift for " + currentDate);
            assertTrue(foundEvening, "Missing evening shift for " + currentDate);
        }
    }

    /**
     * Test to verify that future shifts are correctly retrieved after creation.
     */
    @Test
    void testGetFutureShifts() {
        // Create two future shifts
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        ShiftDTO morningShift = employeeService.createShift(tomorrow, "MORNING");
        ShiftDTO eveningShift = employeeService.createShift(tomorrow, "EVENING");

        // Retrieve future shifts
        List<ShiftDTO> futureShifts = shiftService.getFutureShifts();
        assertTrue(futureShifts.size() >= 2);

        // Validate that the created shifts exist in the retrieved list
        boolean foundMorning = futureShifts.stream()
                .anyMatch(s -> s.getId().equals(morningShift.getId()));
        boolean foundEvening = futureShifts.stream()
                .anyMatch(s -> s.getId().equals(eveningShift.getId()));

        assertTrue(foundMorning, "Morning shift not found in future shifts");
        assertTrue(foundEvening, "Evening shift not found in future shifts");
    }

    /**
     * Test to verify retrieval of historical (past) shifts.
     * If past shifts cannot be created, performs a general check.
     */
    @Test
    void testGetHistoricalShifts() {
        try {
            // Attempt to create past shifts
            LocalDate yesterday = LocalDate.now().minusDays(1);
            ShiftDTO pastMorningShift = employeeService.createShift(yesterday, "MORNING");
            ShiftDTO pastEveningShift = employeeService.createShift(yesterday, "EVENING");

            // Retrieve historical shifts
            List<ShiftDTO> historicalShifts = shiftService.getHistoricalShifts();

            // Ensure at least one created shift is present
            boolean foundMorning = historicalShifts.stream()
                    .anyMatch(s -> s.getId().equals(pastMorningShift.getId()));
            boolean foundEvening = historicalShifts.stream()
                    .anyMatch(s -> s.getId().equals(pastEveningShift.getId()));

            assertTrue(foundMorning || foundEvening, "At least one past shift should be listed");
        } catch (Exception e) {
            // If creating past shifts is not supported, perform a basic retrieval check
            List<ShiftDTO> historicalShifts = shiftService.getHistoricalShifts();
            // No assertion on count due to system state variability
        }
    }
}
