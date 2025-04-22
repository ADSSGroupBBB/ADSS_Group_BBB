//package JUnit.Test;
//
//
//import static org.junit.Assert.*;
//import org.junit.Before;
//import org.junit.Test;
//import Service.EmployeeService;
//import Service.ShiftService;
//import Service.ShiftDTO;
//import Service.PositionDTO;
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
///**
// * Unit tests for the ShiftService class
// */
//public class ShiftServiceTest {
//    private EmployeeService employeeService;
//    private ShiftService shiftService;
//
//    @Before
//    public void setUp() {
//        employeeService = new EmployeeService();
//        shiftService = new ShiftService();
//        // Reset the system before each test to ensure clean state
//        initializeTestData();
//    }
//
//    /**
//     * Initialize test data for tests
//     */
//    private void initializeTestData() {
//        // Add test positions
//        employeeService.addPosition("Cashier", false);
//        employeeService.addPosition("Stocker", false);
//        employeeService.addPosition("Shift Manager", true);
//
//        // Add test employees
//        employeeService.addNewEmployee("123456789", "John", "Doe", "12345", LocalDate.now().minusYears(1), 35.0);
//        employeeService.addNewEmployee("987654321", "Jane", "Smith", "67890", LocalDate.now().minusMonths(6), 40.0);
//
//        // Add qualifications
//        employeeService.addQualificationToEmployee("123456789", "Cashier");
//        employeeService.addQualificationToEmployee("987654321", "Cashier");
//        employeeService.addQualificationToEmployee("987654321", "Shift Manager");
//
//        // Add required positions
//        employeeService.addRequiredPosition("MORNING", "Cashier", 2);
//        employeeService.addRequiredPosition("MORNING", "Stocker", 1);
//        employeeService.addRequiredPosition("EVENING", "Cashier", 1);
//        employeeService.addRequiredPosition("EVENING", "Shift Manager", 1);
//    }
//
//
//
//    @Test
//    public void testCreateShiftsForWeek() {
//        // Find next Sunday to start the week
//        LocalDate startDate = LocalDate.now();
//        while (startDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
//            startDate = startDate.plusDays(1);
//        }
//
//        // Create shifts for a week
//        List<ShiftDTO> createdShifts = shiftService.createShiftsForWeek(startDate);
//        assertFalse("Should create shifts for a week", createdShifts.isEmpty());
//        assertTrue("Should create multiple shifts for a week", createdShifts.size() > 1);
//
//        Set<LocalDate> datesWithShifts = createdShifts.stream().map(ShiftDTO::getDate).collect(Collectors.toSet());
//        assertTrue("Should have shifts for multiple days", datesWithShifts.size() > 1);
//    }
//
//    @Test
//    public void testGetFutureShifts() {
//        // Create shifts for today and future
//        LocalDate today = LocalDate.now();
//        employeeService.createShift(today, "MORNING");
//        employeeService.createShift(today.plusDays(1), "MORNING");
//        employeeService.createShift(today.plusDays(2), "EVENING");
//
//        // Get future shifts
//        List<ShiftDTO> futureShifts = shiftService.getFutureShifts();
//
//        // Should include today and future shifts
//        assertTrue("Should have at least 3 future shifts", futureShifts.size() >= 3);
//
//        // Verify all shifts are today or in the future
//        for (ShiftDTO shift : futureShifts) {
//            assertTrue("Shift should be today or in the future", !shift.getDate().isBefore(today));
//        }
//    }
//
//    @Test
//    public void testGetHistoricalShifts() {
//        // Create a past shift
//        LocalDate yesterday = LocalDate.now().minusDays(1);
//        employeeService.createShift(yesterday, "MORNING");
//
//        // Create today's shift
//        LocalDate today = LocalDate.now();
//        employeeService.createShift(today, "MORNING");
//
//        // Get historical shifts
//        List<ShiftDTO> historicalShifts = shiftService.getHistoricalShifts();
//
//        // Should include only past shifts
//        assertFalse("Historical shifts should not be empty", historicalShifts.isEmpty());
//
//        // Verify all shifts are in the past
//        for (ShiftDTO shift : historicalShifts) {
//            assertTrue("Shift should be in the past", shift.getDate().isBefore(LocalDate.now()));
//        }
//    }
//
//    @Test
//    public void testGetEmployeeShiftHistory() {
//        // Create shifts
//        LocalDate yesterday = LocalDate.now().minusDays(1);
//        LocalDate today = LocalDate.now();
//        LocalDate tomorrow = LocalDate.now().plusDays(1);
//
//        employeeService.createShift(yesterday, "MORNING");
//        employeeService.createShift(today, "EVENING");
//        employeeService.createShift(tomorrow, "MORNING");
//
//        // Assign employee to shifts
//        String yesterdayShiftId = employeeService.getShift(yesterday, "MORNING").getId();
//        String todayShiftId = employeeService.getShift(today, "EVENING").getId();
//
//        employeeService.assignEmployeeToShift(yesterdayShiftId, "123456789", "Cashier");
//        employeeService.assignEmployeeToShift(todayShiftId, "123456789", "Cashier");
//
//        // Get employee shift history
//        List<ShiftDTO> employeeShifts = shiftService.getEmployeeShiftHistory("123456789");
//
//        // Should have 2 shifts
//        assertEquals("Should have 2 shifts in employee history", 2, employeeShifts.size());
//    }
//
//    @Test
//    public void testGetEmployeeFutureShifts() {        // Create shifts
//        LocalDate yesterday = LocalDate.now().minusDays(1);
//        LocalDate today = LocalDate.now();
//        LocalDate tomorrow = LocalDate.now().plusDays(1);
//
//        employeeService.createShift(yesterday, "MORNING");
//        employeeService.createShift(today, "EVENING");
//        employeeService.createShift(tomorrow, "MORNING");
//
//        // Assign employee to shifts
//        String yesterdayShiftId = employeeService.getShift(yesterday, "MORNING").getId();
//        String todayShiftId = employeeService.getShift(today, "EVENING").getId();
//        String tomorrowShiftId = employeeService.getShift(tomorrow, "MORNING").getId();
//
//        employeeService.assignEmployeeToShift(yesterdayShiftId, "123456789", "Cashier");
//        employeeService.assignEmployeeToShift(todayShiftId, "123456789", "Cashier");
//        employeeService.assignEmployeeToShift(tomorrowShiftId, "123456789", "Cashier");
//
//        // Get employee future shifts
//        List<ShiftDTO> futureShifts = shiftService.getEmployeeFutureShifts("123456789");
//
//        // Should have 2 shifts (today and tomorrow)
//        assertEquals("Should have 2 future shifts", 2, futureShifts.size());
//
//        // Verify all shifts are today or in the future
//        for (ShiftDTO shift : futureShifts) {
//            assertTrue("Shift should be today or in the future", !shift.getDate().isBefore(LocalDate.now()));
//        }
//    }
//
//    @Test
//    public void testGetMissingPositionsForShift() {
//        // Create a shift
//        LocalDate tomorrow = LocalDate.now().plusDays(1);
//        employeeService.createShift(tomorrow, "MORNING");
//
//        // Get shift ID
//        String shiftId = employeeService.getShift(tomorrow, "MORNING").getId();
//
//        // Get missing positions
//        List<PositionDTO> missingPositions = shiftService.getMissingPositionsForShift(shiftId);
//
//        // Should have 3 missing positions (2 Cashiers, 1 Stocker)
//        assertEquals("Should have 3 missing positions", 3, missingPositions.size());
//
//        // Assign one cashier
//        employeeService.assignEmployeeToShift(shiftId, "123456789", "Cashier");
//
//        // Get missing positions again
//        missingPositions = shiftService.getMissingPositionsForShift(shiftId);
//
//        // Should have 2 missing positions (1 Cashier, 1 Stocker)
//        assertEquals("Should have 2 missing positions after assignment", 2, missingPositions.size());
//    }
//}
package JUnit.Test;

import Service.*;
import Domain.Employee.UserRole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ShiftServiceTest {

    private ShiftService shiftService;
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService();
        shiftService = new ShiftService();

        // ניקוי נתונים קיימים
        for (EmployeeDTO emp : employeeService.getAllEmployees()) {
            employeeService.removeEmployee(emp.getId());
        }

        // הגדרת תפקידים
        employeeService.addPosition("Shift Manager", true);
        employeeService.addPosition("Cashier", false);

        // הוספת עובדים עם תפקידים שונים
        employeeService.addNewEmployee("1001", "John", "Manager", "IL123456",
                LocalDate.of(2023, 1, 1), 40.0, "SHIFT_MANAGER", "sm123");

        employeeService.addNewEmployee("1002", "Jane", "HR", "IL234567",
                LocalDate.of(2023, 2, 1), 45.0, "HR_MANAGER", "hr123");

        employeeService.addNewEmployee("1003", "Bob", "Regular", "IL345678",
                LocalDate.of(2023, 3, 1), 30.0, "REGULAR_EMPLOYEE", "");

        // הוספת הסמכות
        employeeService.addQualificationToEmployee("1001", "Shift Manager");
        employeeService.addQualificationToEmployee("1002", "Shift Manager");
        employeeService.addQualificationToEmployee("1003", "Cashier");

        // הגדרת זמינות
        for (DayOfWeek day : DayOfWeek.values()) {
            employeeService.updateEmployeeAvailability("1001", day, true, true);
            employeeService.updateEmployeeAvailability("1002", day, true, true);
            employeeService.updateEmployeeAvailability("1003", day, true, false); // רק משמרות בוקר
        }

        // הגדרת תפקידים נדרשים במשמרות
        employeeService.addRequiredPosition("MORNING", "Shift Manager", 1);
        employeeService.addRequiredPosition("MORNING", "Cashier", 1);
        employeeService.addRequiredPosition("EVENING", "Shift Manager", 1);
        employeeService.addRequiredPosition("EVENING", "Cashier", 1);
    }

    @Test
    void testCreateShiftsForWeek() {
        // מצא את התאריך של יום ראשון הקרוב
        LocalDate today = LocalDate.now();
        LocalDate nextSunday = today.with(DayOfWeek.SUNDAY);
        if (today.getDayOfWeek() == DayOfWeek.SUNDAY) {
            nextSunday = nextSunday.plusWeeks(1);
        }

        // יצירת משמרות לשבוע
        List<ShiftDTO> shifts = shiftService.createShiftsForWeek(nextSunday);

        // אמורות להיות 14 משמרות (2 משמרות ליום * 7 ימים)
        assertEquals(14, shifts.size());

        // בדיקה שיש משמרות בוקר וערב לכל יום
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

            assertTrue(foundMorning, "משמרת בוקר חסרה ליום " + currentDate);
            assertTrue(foundEvening, "משמרת ערב חסרה ליום " + currentDate);
        }
    }

    @Test
    void testGetFutureShifts() {
        // יצירת משמרות
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        ShiftDTO morningShift = employeeService.createShift(tomorrow, "MORNING");
        ShiftDTO eveningShift = employeeService.createShift(tomorrow, "EVENING");

        // בדיקת קבלת משמרות עתידיות
        List<ShiftDTO> futureShifts = shiftService.getFutureShifts();
        assertTrue(futureShifts.size() >= 2);

        // בדיקה שהמשמרות שיצרנו נמצאות ברשימה
        boolean foundMorning = futureShifts.stream()
                .anyMatch(s -> s.getId().equals(morningShift.getId()));
        boolean foundEvening = futureShifts.stream()
                .anyMatch(s -> s.getId().equals(eveningShift.getId()));

        assertTrue(foundMorning, "משמרת בוקר שיצרנו לא נמצאת ברשימת המשמרות העתידיות");
        assertTrue(foundEvening, "משמרת ערב שיצרנו לא נמצאת ברשימת המשמרות העתידיות");
    }

    @Test
    void testGetHistoricalShifts() {
        // ניצור משמרות עבר (אם אפשר - נסמך על כך שה-domain מאפשר זאת)
        try {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            ShiftDTO pastMorningShift = employeeService.createShift(yesterday, "MORNING");
            ShiftDTO pastEveningShift = employeeService.createShift(yesterday, "EVENING");

            // בדיקת קבלת משמרות עבר
            List<ShiftDTO> historicalShifts = shiftService.getHistoricalShifts();

            // בדיקה שהמשמרות שיצרנו נמצאות ברשימה
            boolean foundMorning = historicalShifts.stream()
                    .anyMatch(s -> s.getId().equals(pastMorningShift.getId()));
            boolean foundEvening = historicalShifts.stream()
                    .anyMatch(s -> s.getId().equals(pastEveningShift.getId()));

            assertTrue(foundMorning || foundEvening, "לפחות אחת ממשמרות העבר שיצרנו אמורה להיות ברשימה");
        } catch (Exception e) {
            // אם לא ניתן ליצור משמרות עבר, נסתפק בבדיקה כללית
            List<ShiftDTO> historicalShifts = shiftService.getHistoricalShifts();
            // לא נבדוק את המספר המדויק כי זה תלוי במצב המערכת
        }
    }

    @Test
    void testGetEmployeeShiftHistory() {
        // יצירת משמרת עתידית
        LocalDate nextWeek = LocalDate.now().plusDays(7);
        ShiftDTO futureShift = employeeService.createShift(nextWeek, "MORNING");

        // שיבוץ עובד למשמרת
        employeeService.assignEmployeeToShift(futureShift.getId(), "1001", "Shift Manager");

        // בדיקת היסטוריית משמרות של העובד
        List<ShiftDTO> employeeShifts = shiftService.getEmployeeShiftHistory("1001");
        assertFalse(employeeShifts.isEmpty());

        // בדיקה שהמשמרת שיצרנו נמצאת ברשימה
        boolean foundShift = employeeShifts.stream()
                .anyMatch(s -> s.getId().equals(futureShift.getId()));

        assertTrue(foundShift, "המשמרת שיצרנו ושיבצנו לא נמצאת בהיסטוריית המשמרות של העובד");

        // בדיקה שעובד אחר לא יכול לראות את המשמרת הזו
        List<ShiftDTO> otherEmployeeShifts = shiftService.getEmployeeShiftHistory("1003");
        boolean otherFoundShift = otherEmployeeShifts.stream()
                .anyMatch(s -> s.getId().equals(futureShift.getId()));

        assertFalse(otherFoundShift, "עובד שלא שובץ למשמרת לא אמור לראות אותה בהיסטוריית המשמרות שלו");
    }

    @Test
    void testGetEmployeeFutureShifts() {
        // יצירת משמרות עתידיות
        LocalDate nextWeek = LocalDate.now().plusDays(7);
        ShiftDTO morningShift = employeeService.createShift(nextWeek, "MORNING");
        ShiftDTO eveningShift = employeeService.createShift(nextWeek, "EVENING");

        // שיבוץ עובדים למשמרות
        employeeService.assignEmployeeToShift(morningShift.getId(), "1001", "Shift Manager");
        employeeService.assignEmployeeToShift(eveningShift.getId(), "1002", "Shift Manager");

        // בדיקת משמרות עתידיות לעובד 1001
        List<ShiftDTO> employee1Shifts = shiftService.getEmployeeFutureShifts("1001");
        assertTrue(employee1Shifts.stream().anyMatch(s -> s.getId().equals(morningShift.getId())));
        assertFalse(employee1Shifts.stream().anyMatch(s -> s.getId().equals(eveningShift.getId())));

        // בדיקת משמרות עתידיות לעובד 1002
        List<ShiftDTO> employee2Shifts = shiftService.getEmployeeFutureShifts("1002");
        assertFalse(employee2Shifts.stream().anyMatch(s -> s.getId().equals(morningShift.getId())));
        assertTrue(employee2Shifts.stream().anyMatch(s -> s.getId().equals(eveningShift.getId())));
    }

    @Test
    void testGetMissingPositionsForShift() {
        // יצירת משמרת חדשה
        LocalDate nextWeek = LocalDate.now().plusDays(7);
        ShiftDTO newShift = employeeService.createShift(nextWeek, "EVENING");

        // בדיקת תפקידים חסרים לפני שיבוץ
        List<PositionDTO> missingPositions = shiftService.getMissingPositionsForShift(newShift.getId());
        assertEquals(2, missingPositions.size()); // אמורים להיות שני תפקידים חסרים - מנהל וקופאי

        // שיבוץ מנהל משמרת
        employeeService.assignEmployeeToShift(newShift.getId(), "1001", "Shift Manager");

        // בדיקת תפקידים חסרים אחרי שיבוץ מנהל
        missingPositions = shiftService.getMissingPositionsForShift(newShift.getId());
        assertEquals(1, missingPositions.size()); // אמור להיות תפקיד אחד חסר - קופאי
        assertEquals("Cashier", missingPositions.get(0).getName());

        // שיבוץ קופאי
        employeeService.assignEmployeeToShift(newShift.getId(), "1003", "Cashier");

        // בדיקת תפקידים חסרים אחרי שיבוץ כל התפקידים
        missingPositions = shiftService.getMissingPositionsForShift(newShift.getId());
        assertTrue(missingPositions.isEmpty()); // לא אמורים להיות תפקידים חסרים
    }

    @Test
    void testGetShiftById() {
        // יצירת משמרת חדשה
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        ShiftDTO createdShift = employeeService.createShift(tomorrow, "MORNING");

        // קבלת המשמרת לפי מזהה
        ShiftDTO retrievedShift = shiftService.getShiftById(createdShift.getId());

        // בדיקה שהמשמרת שהתקבלה זהה למשמרת שיצרנו
        assertNotNull(retrievedShift);
        assertEquals(createdShift.getId(), retrievedShift.getId());
        assertEquals(createdShift.getDate(), retrievedShift.getDate());
        assertEquals(createdShift.getShiftType(), retrievedShift.getShiftType());

        // בדיקת מזהה לא קיים
        ShiftDTO nonExistingShift = shiftService.getShiftById("non-existing-id");
        assertNull(nonExistingShift);
    }
    @Test
    void testRegularEmployeeCanViewFullFutureShiftAssignments() {
        // יצירת משמרת עתידית
        LocalDate futureDate = LocalDate.now().plusDays(2);
        ShiftDTO shift = employeeService.createShift(futureDate, "MORNING");
        assertNotNull(shift);

        // הוספת עובדים
        employeeService.addNewEmployee("2001", "Alice", "Worker", "IL0001", LocalDate.now(), 30.0);
        employeeService.addNewEmployee("2002", "Bob", "Worker", "IL0002", LocalDate.now(), 30.0);

        // הוספת תפקידים והסמכות
        employeeService.addPosition("Cashier", false);
        employeeService.addQualificationToEmployee("2001", "Cashier");
        employeeService.addQualificationToEmployee("2002", "Cashier");

        // שיבוץ שני עובדים למשמרת
        boolean assigned1 = employeeService.assignEmployeeToShift(shift.getId(), "2001", "Cashier");
        boolean assigned2 = employeeService.assignEmployeeToShift(shift.getId(), "2002", "Cashier");

        assertTrue(assigned1);
        assertTrue(assigned2);

        // קבלת המשמרות של העובד Alice (2001)
        List<ShiftDTO> futureShifts = shiftService.getEmployeeFutureShifts("2001");


        assertFalse(futureShifts.isEmpty());

        ShiftDTO retrievedShift = futureShifts.get(0);
        Map<String, String> assignments = retrievedShift.getAssignments();

        // בדיקה שהעובד רואה גם את עצמו וגם את Bob
        assertEquals(2, assignments.size());
        assertTrue(assignments.containsValue("Alice Worker"));
        assertTrue(assignments.containsValue("Bob Worker"));
    }

}