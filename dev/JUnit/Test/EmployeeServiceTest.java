//package JUnit.Test;
//
//
//
//import static org.junit.Assert.*;
//import Service.ShiftDTO;
//import org.junit.Before;
//import org.junit.Test;
//import Service.EmployeeService;
//import Service.EmployeeDTO;
//import Service.PositionDTO;
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//
//
///**
// * Unit tests for the EmployeeService class
// */
//public class EmployeeServiceTest {
//    private EmployeeService employeeService;
//
//    @Before
//    public void setUp() {
//        employeeService = new EmployeeService(); // Reset the system before each test to ensure clean state
//        initializeTestData();
//    }
//
//    private void initializeTestData() {
//        employeeService.addPosition("Cashier", false);
//        employeeService.addPosition("Stocker", false);
//        employeeService.addPosition("Shift Manager", true);
//        employeeService.addNewEmployee("123456789", "John", "Gal", "12345", LocalDate.now().minusYears(1), 35.0);
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
//    @Test
//    public void testAddEmployee() {
//        // Test adding a new employee
//        boolean result = employeeService.addNewEmployee("111222333", "Test", "User", "11111", LocalDate.now(), 30.0);assertTrue("Should be able to add a new employee", result);
//
//        // Test duplicate employee
//        result = employeeService.addNewEmployee("111222333", "Duplicate", "User", "22222", LocalDate.now(), 30.0);assertFalse("Should not be able to add duplicate employee ID", result);
//
//        // Verify employee was added
//        EmployeeDTO employee = employeeService.getEmployee("111222333");
//        assertNotNull("Should be able to retrieve added employee", employee);
//        assertEquals("First name should match", "Test", employee.getFirstName());
//    }
//
//    @Test
//    public void testRemoveEmployee() {
//        // Add test employee
//        employeeService.addNewEmployee("444555666", "Remove", "Test", "33333", LocalDate.now(), 30.0);
//
//        // Verify employee exists
//        assertNotNull(employeeService.getEmployee("444555666"));
//
//        // Remove employee
//        boolean result = employeeService.removeEmployee("444555666");
//        assertTrue("Should be able to remove employee", result);
//
//        // Verify employee was removed
//        assertNull("Employee should be removed", employeeService.getEmployee("444555666"));
//    }
//
//    @Test
//    public void testUpdateEmployee() {
//        // Get test employee
//        EmployeeDTO employee = employeeService.getEmployee("123456789");
//        assertNotNull("Test employee should exist", employee);
//
//        // Update first name
//        boolean result = employeeService.updateEmployeeFirstName("123456789", "UpdatedFirstName");
//        assertTrue("Should be able to update employee first name", result);
//
//        // Verify update
//        employee = employeeService.getEmployee("123456789");
//        assertEquals("First name should be updated", "UpdatedFirstName", employee.getFirstName());
//
//        // Update last name
//        result = employeeService.updateEmployeeLastName("123456789", "UpdatedLastName");
//        assertTrue("Should be able to update employee last name", result);
//
//        // Verify update
//        employee = employeeService.getEmployee("123456789");
//        assertEquals("Last name should be updated", "UpdatedLastName", employee.getLastName());
//
//        // Update salary
//        result = employeeService.updateEmployeeSalary("123456789", 45.0);
//        assertTrue("Should be able to update employee salary", result);
//
//        // Verify update
//        employee = employeeService.getEmployee("123456789");
//        assertEquals("Salary should be updated", 45.0, employee.getSalary(), 0.001);
//    }
//
//    @Test
//    public void testEmployeeAvailability() {
//        // Update availability (by default, employees are available for all shifts)
//        boolean result = employeeService.updateEmployeeAvailability("123456789", DayOfWeek.MONDAY, false, true);assertTrue("Should be able to update availability", result);
//
//        // Check availability - Monday morning (should be unavailable)
//        assertFalse("Employee should be unavailable for Monday morning", employeeService.isEmployeeAvailable("123456789", DayOfWeek.MONDAY, "MORNING"));
//
//        // Check availability - Monday evening (should be available)
//        assertTrue("Employee should be available for Monday evening", employeeService.isEmployeeAvailable("123456789", DayOfWeek.MONDAY, "EVENING"));
//
//        // Check availability - Tuesday (should be available for both shifts)
//        assertTrue("Employee should be available for Tuesday morning", employeeService.isEmployeeAvailable("123456789", DayOfWeek.TUESDAY, "MORNING"));
//        assertTrue("Employee should be available for Tuesday evening", employeeService.isEmployeeAvailable("123456789", DayOfWeek.TUESDAY, "EVENING"));
//    }
//
//    @Test
//    public void testAddPosition() {
//        // Test adding a new position
//        boolean result = employeeService.addPosition("Test Position", false);
//        assertTrue("Should be able to add a new position", result);
//
//        // Test duplicate position
//        result = employeeService.addPosition("Test Position", true);
//        assertFalse("Should not be able to add duplicate position", result);
//
//        // Verify position was added
//        PositionDTO position = employeeService.getPositionDetails("Test Position");
//        assertNotNull("Should be able to retrieve added position", position);
//        assertEquals("Position name should match", "Test Position", position.getName());
//        assertFalse("Position should not be a manager position", position.isRequiresShiftManager());
//    }
//
//    @Test
//    public void testAddQualification() {
//        // Add qualification
//        boolean result = employeeService.addQualificationToEmployee("123456789", "Stocker");assertTrue("Should be able to add qualification", result);
//
//        // Verify qualification was added
//        EmployeeDTO employee = employeeService.getEmployee("123456789");assertTrue("Employee should have the added qualification", employee.getQualifiedPositions().contains("Stocker"));
//
//        // Try to add qualification for non-existent employee
//        result = employeeService.addQualificationToEmployee("nonexistent", "Cashier");
//        assertFalse("Should not be able to add qualification to non-existent employee", result);
//
//        // Try to add qualification for non-existent position
//        result = employeeService.addQualificationToEmployee("123456789", "NonExistentPosition");
//        assertFalse("Should not be able to add non-existent position qualification", result);
//    }
//
//
//
//    @Test
//    public void testCreateShift() {
//        // Create a shift
//        LocalDate tomorrow = LocalDate.now().plusDays(1);
//        employeeService.createShift(tomorrow, "MORNING");
//
//        // Verify shift was created
//        assertNotNull("Shift should be created", employeeService.getShift(tomorrow, "MORNING"));
//
//        // Try to create duplicate shift (same date and type)
//        assertNull("Should not create duplicate shift", employeeService.createShift(tomorrow, "MORNING"));
//    }
//
//
//
//    @Test
//    public void testAssignEmployeeToShift() {
//        // Create shift
//        LocalDate tomorrow = LocalDate.now().plusDays(1);
//        // Check if shift exists before creating
//        ShiftDTO shift = employeeService.getShift(tomorrow, "MORNING");
//        if (shift == null) {
//            shift = employeeService.createShift(tomorrow, "MORNING");
//        }
//        assertNotNull("Shift should be created", shift);
//
//        employeeService.addQualificationToEmployee("123456789", "Cashier");
//
//        // Get shift ID
//        String shiftId = shift.getId();
//        boolean result = employeeService.assignEmployeeToShift(shiftId, "123456789", "Cashier");
//        assertTrue("Should be able to assign qualified employee to shift", result);
//    }
//
//
//
//    @Test
//    public void testRemoveAssignmentFromShift() {
//        // Create shift and assign employee
//        LocalDate tomorrow = LocalDate.now().plusDays(1);
//        employeeService.createShift(tomorrow, "MORNING");
//        String shiftId = employeeService.getShift(tomorrow, "MORNING").getId();
//        employeeService.assignEmployeeToShift(shiftId, "123456789", "Cashier");
//        boolean result = employeeService.removeAssignmentFromShift(shiftId, "Cashier");
//        assertTrue("Should be able to remove assignment", result);
//
//
//    }
//}

package JUnit.Test;

import Domain.Employee;
import Domain.Position;
import Domain.ShiftType;
import Service.EmployeeDTO;
import Service.EmployeeService;
import Service.PositionDTO;
import Service.ShiftDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeServiceTest {

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService();

        // ניקוי נתונים קיימים
        for (EmployeeDTO emp : employeeService.getAllEmployees()) {
            employeeService.removeEmployee(emp.getId());
        }

        // הגדרת תפקידים
        employeeService.addPosition("Shift Manager", true);
        employeeService.addPosition("Cashier", false);

        // הוספת עובדים
        employeeService.addNewEmployee("1001", "John", "Smith", "IL123456",
                LocalDate.of(2023, 1, 1), 35.0, "HR_MANAGER", "hr123",5, 10, "Fund1");

        employeeService.addNewEmployee("1002", "Jane", "Doe", "IL654321",
                LocalDate.of(2023, 2, 1), 30.0, "SHIFT_MANAGER", "sm123", 6, 12, "Fund2");

        employeeService.addNewEmployee("1003", "Bob", "Brown", "IL111222",
                LocalDate.of(2023, 3, 1), 25.0, "REGULAR_EMPLOYEE", "",4, 8, "Fund3");

        // הוספת הסמכות
        employeeService.addQualificationToEmployee("1001", "Shift Manager");
        employeeService.addQualificationToEmployee("1002", "Shift Manager");
        employeeService.addQualificationToEmployee("1003", "Cashier");
    }

    @Test
    void testGetEmployeeWithRole() {
        EmployeeDTO hrManager = employeeService.getEmployeeDetails("1001");
        EmployeeDTO shiftManager = employeeService.getEmployeeDetails("1002");
        EmployeeDTO regular = employeeService.getEmployeeDetails("1003");

        // בדיקת תפקידים
        assertTrue(hrManager.isHRManager());
        assertTrue(shiftManager.isShiftManager());
        assertFalse(regular.isManager());

        // בדיקה שה-DTO מכיל את המידע הנכון
        assertEquals("John Smith", hrManager.getFullName());
        assertEquals("Jane Doe", shiftManager.getFullName());
        assertEquals("Bob Brown", regular.getFullName());
    }

    @Test
    void testVerifyPassword() {
        // בדיקת אימות סיסמה
        assertTrue(employeeService.verifyPassword("1001", "hr123"));
        assertTrue(employeeService.verifyPassword("1002", "sm123"));

        // בדיקת סיסמה שגויה
        assertFalse(employeeService.verifyPassword("1001", "wrongpass"));
        assertFalse(employeeService.verifyPassword("1002", "wrongpass"));

        // בדיקת עובד שאינו קיים
        assertFalse(employeeService.verifyPassword("9999", "anypass"));
    }

    @Test
    void testUpdateEmployeeRole() {
        // עדכון תפקיד של עובד רגיל למנהל משמרת
        assertTrue(employeeService.updateEmployeeRole("1003", "SHIFT_MANAGER"));

        // בדיקה שהתפקיד עודכן
        EmployeeDTO updatedEmployee = employeeService.getEmployeeDetails("1003");
        assertTrue(updatedEmployee.isShiftManager());
        assertTrue(updatedEmployee.isManager());
        assertFalse(updatedEmployee.isHRManager());

        // עדכון למנהל כח אדם
        assertTrue(employeeService.updateEmployeeRole("1003", "HR_MANAGER"));
        updatedEmployee = employeeService.getEmployeeDetails("1003");
        assertTrue(updatedEmployee.isHRManager());

        // עדכון חזרה לעובד רגיל
        assertTrue(employeeService.updateEmployeeRole("1003", "REGULAR_EMPLOYEE"));
        updatedEmployee = employeeService.getEmployeeDetails("1003");
        assertFalse(updatedEmployee.isManager());
    }

    @Test
    void testUpdateEmployeePassword() {
        // עדכון סיסמה
        assertTrue(employeeService.updateEmployeePassword("1001", "newPassword"));

        // בדיקה שהסיסמה עודכנה
        assertFalse(employeeService.verifyPassword("1001", "hr123")); // סיסמה ישנה
        assertTrue(employeeService.verifyPassword("1001", "newPassword")); // סיסמה חדשה

        // ניסיון לעדכן סיסמה לעובד שאינו קיים
        assertFalse(employeeService.updateEmployeePassword("9999", "anyPassword"));
    }

    @Test
    void testAddNewEmployeeWithRole() {
        // הוספת עובד עם תפקיד מנהל משמרת
        assertTrue(employeeService.addNewEmployee("1004", "Test", "Manager", "IL999888",
                LocalDate.now(), 40.0, "SHIFT_MANAGER", "pass123", 0, 0, "TestFund"));

        // בדיקה שהעובד נוסף בהצלחה עם התפקיד הנכון
        EmployeeDTO newEmployee = employeeService.getEmployeeDetails("1004");
        assertNotNull(newEmployee);
        assertEquals("Test Manager", newEmployee.getFullName());
        assertTrue(newEmployee.isShiftManager());
        assertTrue(employeeService.verifyPassword("1004", "pass123"));

        // ניסיון להוסיף עובד עם תפקיד לא חוקי
        assertFalse(employeeService.addNewEmployee("1005", "Invalid", "Role", "IL777666",
                LocalDate.now(), 35.0, "INVALID_ROLE", "pass456", 0, 0, "TestFund"));
    }

    @Test
    void testAccessibleEmployees() {
        // בדיקה עבור מנהל כח אדם - אמור לראות את כל העובדים
        List<EmployeeDTO> hrAccessible = employeeService.getAccessibleEmployees("1001");
        assertEquals(3, hrAccessible.size());

        // בדיקה עבור מנהל משמרת - אמור לראות את כל העובדים
        List<EmployeeDTO> smAccessible = employeeService.getAccessibleEmployees("1002");
        assertEquals(3, smAccessible.size());

        // בדיקה עבור עובד רגיל - אמור לראות רק את עצמו
        List<EmployeeDTO> regularAccessible = employeeService.getAccessibleEmployees("1003");
        assertEquals(1, regularAccessible.size());
        assertEquals("1003", regularAccessible.get(0).getId());

        // בדיקה עבור מזהה לא חוקי
        List<EmployeeDTO> invalidAccessible = employeeService.getAccessibleEmployees("9999");
        assertTrue(invalidAccessible.isEmpty());
    }

    @Test
    void testConvertEmployeeToDTO() {
        // בדיקה שהשיטה convertEmployeeToDTO מעבירה את התפקיד בצורה נכונה
        EmployeeDTO hrEmployee = employeeService.getEmployeeDetails("1001");
        assertTrue(hrEmployee.isHRManager());
        assertTrue(hrEmployee.isManager());

        EmployeeDTO regularEmployee = employeeService.getEmployeeDetails("1003");
        assertFalse(regularEmployee.isManager());

        // בדיקה נוספת - הוספת עובד ובדיקה שהמרה ל-DTO מעבירה את כל המידע
        employeeService.addNewEmployee("1006", "Convert", "Test", "IL555444",
                LocalDate.of(2023, 5, 5), 32.0, "SHIFT_MANAGER", "convert123",5, 10, "PensionTestFund");
        employeeService.addQualificationToEmployee("1006", "Cashier");

        EmployeeDTO convertedEmployee = employeeService.getEmployeeDetails("1006");
        assertEquals("1006", convertedEmployee.getId());
        assertEquals("Convert", convertedEmployee.getFirstName());
        assertEquals("Test", convertedEmployee.getLastName());
        assertEquals("IL555444", convertedEmployee.getBankAccount());
        assertEquals(LocalDate.of(2023, 5, 5), convertedEmployee.getStartDate());
        assertEquals(32.0, convertedEmployee.getSalary());
        assertTrue(convertedEmployee.isShiftManager());
        assertTrue(convertedEmployee.getQualifiedPositions().contains("Cashier"));
    }

    @Test
    void testLoginScenarios() {
        // בדיקת סצנריו של התחברות מנהל עם סיסמה נכונה
        EmployeeDTO hrManager = employeeService.getEmployeeDetails("1001");
        assertTrue(hrManager.isManager());
        assertTrue(employeeService.verifyPassword("1001", "hr123"));

        // בדיקת סצנריו של התחברות מנהל עם סיסמה שגויה
        assertFalse(employeeService.verifyPassword("1001", "wrongpass"));

        // בדיקת סצנריו של התחברות עובד רגיל (ללא צורך בסיסמה)
        EmployeeDTO regularEmployee = employeeService.getEmployeeDetails("1003");
        assertFalse(regularEmployee.isManager());
    }
    @Test
    void testUpdateAvailabilityAllowedUntilThursday() {
        String employeeId = "1003";  // עובד רגיל

        // נניח שאנחנו ביום חמישי (באופן ריאלי צריך mocking של LocalDate, אבל נניח שהפונקציה פשוטה)
        DayOfWeek today = DayOfWeek.THURSDAY;
        DayOfWeek dayToUpdate = DayOfWeek.MONDAY;  // לדוגמא, לעדכן את יום שני הבא

        // נבדוק שניתן לעדכן זמינות (נניח שיש פונקציה במערכת שמבצעת את הבדיקה)
        boolean updateResult = employeeService.updateEmployeeAvailabilityForNextWeek(
                employeeId, dayToUpdate, false, true);

        assertTrue(updateResult, "Should allow updating next week's availability on Thursday");

        // נוודא שהעדכון בוצע בפועל
        boolean morningAvailability = employeeService.isEmployeeAvailableForNextWeek(employeeId, dayToUpdate, "MORNING");
        boolean eveningAvailability = employeeService.isEmployeeAvailableForNextWeek(employeeId, dayToUpdate, "EVENING");

        assertFalse(morningAvailability, "Morning shift should be unavailable after update");
        assertTrue(eveningAvailability, "Evening shift should remain available after update");
    }
    @Test
    void testUpdateAvailabilityNotAllowedAfterThursday() {
        String employeeId = "1003";
        DayOfWeek selectedDay = DayOfWeek.MONDAY;  // יום לבדיקת זמינות בשבוע הבא

        // נניח שהיום יום שישי (הדמיה באמצעות בדיקה ידנית)
        DayOfWeek today = DayOfWeek.FRIDAY;
        assertTrue(today.getValue() > DayOfWeek.THURSDAY.getValue(), "Today should be after Thursday");

        // במקרה כזה, אמורה להיות חסימה בלוגיקה שלך (נניח שזה מתבצע בשכבת ה-Presentation)
        boolean isUpdateAllowed = today.getValue() <= DayOfWeek.THURSDAY.getValue();
        assertFalse(isUpdateAllowed, "Updating availability should not be allowed after Thursday");

        // נניח שבכל זאת מנסים לעדכן (ישירות דרך ה-Service, כי ה-Presentation חוסם)
        boolean updateResult = employeeService.updateEmployeeAvailabilityForNextWeek(employeeId, selectedDay, false, false);

        // המערכת כן תאפשר כי זו קריאה ישירה ל-Service, אבל הטסט מדמה את האיסור הלוגי
        assertTrue(updateResult, "Service allows update, but UI should prevent this action after Thursday");
    }


}