package JUnit.Test;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Service.EmployeeService;
import Service.ShiftService;
import Service.ShiftDTO;
import Service.PositionDTO;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Unit tests for the ShiftService class
 */
public class ShiftServiceTest {
    private EmployeeService employeeService;
    private ShiftService shiftService;

    @Before
    public void setUp() {
        employeeService = new EmployeeService();
        shiftService = new ShiftService();
        // Reset the system before each test to ensure clean state
        initializeTestData();
    }

    /**
     * Initialize test data for tests
     */
    private void initializeTestData() {
        // Add test positions
        employeeService.addPosition("Cashier", false);
        employeeService.addPosition("Stocker", false);
        employeeService.addPosition("Shift Manager", true);

        // Add test employees
        employeeService.addNewEmployee("123456789", "John", "Doe", "12345", LocalDate.now().minusYears(1), 35.0);
        employeeService.addNewEmployee("987654321", "Jane", "Smith", "67890", LocalDate.now().minusMonths(6), 40.0);

        // Add qualifications
        employeeService.addQualificationToEmployee("123456789", "Cashier");
        employeeService.addQualificationToEmployee("987654321", "Cashier");
        employeeService.addQualificationToEmployee("987654321", "Shift Manager");

        // Add required positions
        employeeService.addRequiredPosition("MORNING", "Cashier", 2);
        employeeService.addRequiredPosition("MORNING", "Stocker", 1);
        employeeService.addRequiredPosition("EVENING", "Cashier", 1);
        employeeService.addRequiredPosition("EVENING", "Shift Manager", 1);
    }



    @Test
    public void testCreateShiftsForWeek() {
        // Find next Sunday to start the week
        LocalDate startDate = LocalDate.now();
        while (startDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
            startDate = startDate.plusDays(1);
        }

        // Create shifts for a week
        List<ShiftDTO> createdShifts = shiftService.createShiftsForWeek(startDate);
        assertFalse("Should create shifts for a week", createdShifts.isEmpty());
        assertTrue("Should create multiple shifts for a week", createdShifts.size() > 1);

        Set<LocalDate> datesWithShifts = createdShifts.stream().map(ShiftDTO::getDate).collect(Collectors.toSet());
        assertTrue("Should have shifts for multiple days", datesWithShifts.size() > 1);
    }

    @Test
    public void testGetFutureShifts() {
        // Create shifts for today and future
        LocalDate today = LocalDate.now();
        employeeService.createShift(today, "MORNING");
        employeeService.createShift(today.plusDays(1), "MORNING");
        employeeService.createShift(today.plusDays(2), "EVENING");

        // Get future shifts
        List<ShiftDTO> futureShifts = shiftService.getFutureShifts();

        // Should include today and future shifts
        assertTrue("Should have at least 3 future shifts", futureShifts.size() >= 3);

        // Verify all shifts are today or in the future
        for (ShiftDTO shift : futureShifts) {
            assertTrue("Shift should be today or in the future", !shift.getDate().isBefore(today));
        }
    }

    @Test
    public void testGetHistoricalShifts() {
        // Create a past shift
        LocalDate yesterday = LocalDate.now().minusDays(1);
        employeeService.createShift(yesterday, "MORNING");

        // Create today's shift
        LocalDate today = LocalDate.now();
        employeeService.createShift(today, "MORNING");

        // Get historical shifts
        List<ShiftDTO> historicalShifts = shiftService.getHistoricalShifts();

        // Should include only past shifts
        assertFalse("Historical shifts should not be empty", historicalShifts.isEmpty());

        // Verify all shifts are in the past
        for (ShiftDTO shift : historicalShifts) {
            assertTrue("Shift should be in the past", shift.getDate().isBefore(LocalDate.now()));
        }
    }

    @Test
    public void testGetEmployeeShiftHistory() {
        // Create shifts
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        employeeService.createShift(yesterday, "MORNING");
        employeeService.createShift(today, "EVENING");
        employeeService.createShift(tomorrow, "MORNING");

        // Assign employee to shifts
        String yesterdayShiftId = employeeService.getShift(yesterday, "MORNING").getId();
        String todayShiftId = employeeService.getShift(today, "EVENING").getId();

        employeeService.assignEmployeeToShift(yesterdayShiftId, "123456789", "Cashier");
        employeeService.assignEmployeeToShift(todayShiftId, "123456789", "Cashier");

        // Get employee shift history
        List<ShiftDTO> employeeShifts = shiftService.getEmployeeShiftHistory("123456789");

        // Should have 2 shifts
        assertEquals("Should have 2 shifts in employee history", 2, employeeShifts.size());
    }

    @Test
    public void testGetEmployeeFutureShifts() {        // Create shifts
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        employeeService.createShift(yesterday, "MORNING");
        employeeService.createShift(today, "EVENING");
        employeeService.createShift(tomorrow, "MORNING");

        // Assign employee to shifts
        String yesterdayShiftId = employeeService.getShift(yesterday, "MORNING").getId();
        String todayShiftId = employeeService.getShift(today, "EVENING").getId();
        String tomorrowShiftId = employeeService.getShift(tomorrow, "MORNING").getId();

        employeeService.assignEmployeeToShift(yesterdayShiftId, "123456789", "Cashier");
        employeeService.assignEmployeeToShift(todayShiftId, "123456789", "Cashier");
        employeeService.assignEmployeeToShift(tomorrowShiftId, "123456789", "Cashier");

        // Get employee future shifts
        List<ShiftDTO> futureShifts = shiftService.getEmployeeFutureShifts("123456789");

        // Should have 2 shifts (today and tomorrow)
        assertEquals("Should have 2 future shifts", 2, futureShifts.size());

        // Verify all shifts are today or in the future
        for (ShiftDTO shift : futureShifts) {
            assertTrue("Shift should be today or in the future", !shift.getDate().isBefore(LocalDate.now()));
        }
    }

    @Test
    public void testGetMissingPositionsForShift() {
        // Create a shift
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        employeeService.createShift(tomorrow, "MORNING");

        // Get shift ID
        String shiftId = employeeService.getShift(tomorrow, "MORNING").getId();

        // Get missing positions
        List<PositionDTO> missingPositions = shiftService.getMissingPositionsForShift(shiftId);

        // Should have 3 missing positions (2 Cashiers, 1 Stocker)
        assertEquals("Should have 3 missing positions", 3, missingPositions.size());

        // Assign one cashier
        employeeService.assignEmployeeToShift(shiftId, "123456789", "Cashier");

        // Get missing positions again
        missingPositions = shiftService.getMissingPositionsForShift(shiftId);

        // Should have 2 missing positions (1 Cashier, 1 Stocker)
        assertEquals("Should have 2 missing positions after assignment", 2, missingPositions.size());
    }
}
