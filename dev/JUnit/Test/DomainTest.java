package JUnit.Test;
/**
 * Comprehensive test suite for core domain classes in the employee and shift management system.
 *
 * This file includes tests for:
 * 1. **EmployeeManager** - Managing employees, positions, qualifications, and availability.
 * 2. **Employee** - Validating employee attributes, role management, and equality logic.
 * 3. **EmployeeAvailability** - Ensuring correct handling of shift availability per day.
 * 4. **Shift** - Testing shift creation, employee assignment based on qualifications and availability.
 *
 * Key Features Tested:
 * - Adding, removing, and retrieving employees.
 * - Managing employee roles, passwords, and qualifications.
 * - Handling employee availability for different days and shifts.
 * - Creating shifts, assigning employees, and enforcing role/availability constraints.
 * - Validation of business rules like preventing duplicate shifts or invalid assignments.
 *
 * Each test class focuses on a specific domain component to ensure modularity and clarity.
 * The tests aim to cover both standard use-cases and edge cases.
 *
 * Technologies Used:
 * - JUnit 5 for structured unit testing.
 * - Assertions to validate expected behaviors.
 */

import Domain.*;
import Domain.Employee.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DomainTest {
    private IEmployeeManager employeeManager;
    private Employee employee1;
    private Employee employee2;
    private Position managerPosition;
    private Position cashierPosition;

    @BeforeEach
    void setUp() {
        // השתמש ב-EmployeeManagerFactory במקום להשתמש ב-getInstance
        employeeManager = EmployeeManagerFactory.getEmployeeManager();

        // Clear any existing data
        for (Employee emp : employeeManager.getAllEmployees()) {
            employeeManager.removeEmployee(emp.getId());
        }

        // Create test positions
        managerPosition = new Position("Shift Manager", true);
        cashierPosition = new Position("Cashier", false);

        employeeManager.addPosition(managerPosition);
        employeeManager.addPosition(cashierPosition);

        // Create test employees - הוספת פרמטרים של תפקיד וסיסמה
        employee1 = new Employee("123456789", "Sapir", "aa", "IL12-1234-1234-1234",
                LocalDate.of(2023, 1, 1), 35.0, UserRole.SHIFT_MANAGER, "password1",
                5, 10, "Menorah");

        employee2 = new Employee("987654321", "Batel", "bb", "IL12-5678-5678-5678",
                LocalDate.of(2023, 2, 1), 30.0, UserRole.REGULAR_EMPLOYEE, "",
                3, 12, "Migdal");


        // Add qualifications
        employee1.addQualifiedPosition(managerPosition);
        employee1.addQualifiedPosition(cashierPosition);
        employee2.addQualifiedPosition(cashierPosition);

        // Set availability - employee1 available for all shifts, employee2 only for morning shifts
        employee2.getAvailability().updateAvailability(DayOfWeek.MONDAY, true, false);

        // Add employees to manager
        employeeManager.addEmployee(employee1);
        employeeManager.addEmployee(employee2);

        // Set required positions
        employeeManager.addRequiredPosition(ShiftType.MORNING, "Shift Manager", 1);
        employeeManager.addRequiredPosition(ShiftType.MORNING, "Cashier", 2);
        employeeManager.addRequiredPosition(ShiftType.EVENING, "Shift Manager", 1);
        employeeManager.addRequiredPosition(ShiftType.EVENING, "Cashier", 1);
    }

    @Test
    void testAddEmployee() {
        Employee newEmployee = new Employee("111222333", "New", "Employee", "IL12-9999-9999-9999",
                LocalDate.now(), 25.0, UserRole.REGULAR_EMPLOYEE, "", 2, 8, "Migdal");
        assertTrue(employeeManager.addEmployee(newEmployee));
        assertEquals(newEmployee, employeeManager.getEmployee("111222333"));

        // Test adding duplicate employee
        assertFalse(employeeManager.addEmployee(newEmployee));
    }

    @Test
    void testRemoveEmployee() {
        // Create a new employee that is not assigned to any shift
        Employee tempEmployee = new Employee("555555555", "Temp", "Employee", "IL12-3333-3333-3333",
                LocalDate.now(), 28.0, UserRole.REGULAR_EMPLOYEE, "", 1, 5, "Harel");
        employeeManager.addEmployee(tempEmployee);

        // Test removing employee
        Employee removed = employeeManager.removeEmployee("555555555");
        assertEquals(tempEmployee, removed);
        assertNull(employeeManager.getEmployee("555555555"));

        // Test removing non-existent employee
        assertNull(employeeManager.removeEmployee("000000000"));
    }

    @Test
    void testGetQualifiedEmployeesForPosition() {
        List<Employee> qualifiedForManager = employeeManager.getQualifiedEmployeesForPosition(managerPosition);
        assertEquals(1, qualifiedForManager.size());
        assertTrue(qualifiedForManager.contains(employee1));

        List<Employee> qualifiedForCashier = employeeManager.getQualifiedEmployeesForPosition(cashierPosition);
        assertEquals(2, qualifiedForCashier.size());
        assertTrue(qualifiedForCashier.contains(employee1));
        assertTrue(qualifiedForCashier.contains(employee2));
    }


    @Test
    void testAddQualificationToEmployee() {
        // Create new position and employee
        Position cookPosition = new Position("Cook", false);
        employeeManager.addPosition(cookPosition);

        Employee newEmployee = new Employee("444444444", "New", "Cook", "IL12-7777-7777-7777",
                LocalDate.now(), 32.0, UserRole.REGULAR_EMPLOYEE, "", 3, 6, "Menora");
        employeeManager.addEmployee(newEmployee);

        // Add qualification
        assertTrue(employeeManager.addQualificationToEmployee("444444444", "Cook"));
        assertTrue(newEmployee.isQualifiedFor(cookPosition));

        // Test adding non-existent position
        assertFalse(employeeManager.addQualificationToEmployee("444444444", "NonExistentPosition"));

        // Test adding to non-existent employee
        assertFalse(employeeManager.addQualificationToEmployee("999999999", "Cook"));
    }

    @Test
    void testUpdateEmployeeAvailability() {
        // Update employee1's availability for Tuesday
        assertTrue(employeeManager.updateEmployeeAvailability(employee1.getId(), DayOfWeek.TUESDAY, true, false));

        // Check that employee1 is available for Tuesday morning but not evening
        assertTrue(employee1.getAvailability().isAvailable(DayOfWeek.TUESDAY, ShiftType.MORNING));
        assertFalse(employee1.getAvailability().isAvailable(DayOfWeek.TUESDAY, ShiftType.EVENING));

        // Test updating non-existent employee
        assertFalse(employeeManager.updateEmployeeAvailability("999999999", DayOfWeek.TUESDAY, true, true));
    }

    @Test
    void testCreateShift() {
        LocalDate nextMonday = LocalDate.now().plusDays(7).with(DayOfWeek.MONDAY);

        // Create a morning shift
        Shift morningShift = employeeManager.createShift(nextMonday, ShiftType.MORNING);
        assertNotNull(morningShift);
        assertEquals(nextMonday, morningShift.getDate());
        assertEquals(ShiftType.MORNING, morningShift.getShiftType());

        // Create an evening shift
        Shift eveningShift = employeeManager.createShift(nextMonday, ShiftType.EVENING);
        assertNotNull(eveningShift);
        assertEquals(ShiftType.EVENING, eveningShift.getShiftType());

        // Try to create duplicate shift - should return null
        assertNull(employeeManager.createShift(nextMonday, ShiftType.MORNING));
    }

    //  test for verifying the role-based permissions system
    @Test
    void testEmployeeRoles() {
        // Create an employee with the role of Shift Manager
        Employee shiftManager = new Employee("111111", "Shift", "Manager", "IL12-1111-1111-1111",
                LocalDate.now(), 45.0, UserRole.SHIFT_MANAGER, "sm123", 5, 12, "Migdal");
        // Create an employee with the role of HR Manager
        Employee hrManager = new Employee("222222", "HR", "Manager", "IL12-2222-2222-2222",
                LocalDate.now(), 50.0, UserRole.HR_MANAGER, "hr123", 7, 14, "Menora");
        // Create a regular employee with no managerial role
        Employee regular = new Employee("333333", "Regular", "Employee", "IL12-3333-3333-3333",
                LocalDate.now(), 30.0, UserRole.REGULAR_EMPLOYEE, "", 3, 10, "Clal");

        assertTrue(shiftManager.isShiftManager());
        assertFalse(shiftManager.isHRManager());
        assertTrue(shiftManager.isManager());

        assertTrue(hrManager.isHRManager());
        assertFalse(hrManager.isShiftManager());
        assertTrue(hrManager.isManager());

        assertFalse(regular.isManager());
        assertFalse(regular.isHRManager());
        assertFalse(regular.isShiftManager());
    }

    // Test for password authentication and update functionality
    @Test
    void testPasswordAuthentication() {
        // Create an employee with an initial password
        Employee manager = new Employee("444444", "Password", "Test", "IL12-4444-4444-4444",
                LocalDate.now(), 40.0, UserRole.HR_MANAGER, "securePass123", 6, 14, "Menora");

        // Verify that the initial password is correctly set
        assertEquals("securePass123", manager.getPassword());
        // Update the employee's password
        manager.setPassword("newPassword456");
        assertEquals("newPassword456", manager.getPassword());
    }
}

class EmployeeTest {
    private Employee employee;
    private Position managerPosition;
    private Position cashierPosition;

    @BeforeEach
    void setUp() {
        // Initialize a regular employee with sample data before each test
        employee = new Employee("123456789", "John", "Doe", "IL12-1234-1234-1234",
                LocalDate.of(2023, 1, 1), 35.0, Employee.UserRole.REGULAR_EMPLOYEE, "", 5, 12, "Harel");

        managerPosition = new Position("Shift Manager", true);
        cashierPosition = new Position("Cashier", false);
    }

    @Test
    void testEmployeeCreation() {
        assertEquals("123456789", employee.getId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals("John Doe", employee.getFullName());
        assertEquals("IL12-1234-1234-1234", employee.getBankAccount());
        assertEquals(LocalDate.of(2023, 1, 1), employee.getStartDate());
        assertEquals(35.0, employee.getSalary());
        assertEquals(Employee.UserRole.REGULAR_EMPLOYEE, employee.getRole());
        assertEquals("", employee.getPassword());
    }
    // This test ensures that all setter methods properly update the employee's attributes,
    // including name, bank account, salary, role, and password.
    @Test
    void testEmployeeSetters() {
        employee.setFirstName("Jane");
        assertEquals("Jane", employee.getFirstName());

        employee.setLastName("Smith");
        assertEquals("Smith", employee.getLastName());
        assertEquals("Jane Smith", employee.getFullName());

        employee.setBankAccount("IL12-9999-9999-9999");
        assertEquals("IL12-9999-9999-9999", employee.getBankAccount());

        employee.setSalary(40.0);
        assertEquals(40.0, employee.getSalary());

        employee.setRole(Employee.UserRole.SHIFT_MANAGER);
        assertEquals(Employee.UserRole.SHIFT_MANAGER, employee.getRole());

        employee.setPassword("newPass123");
        assertEquals("newPass123", employee.getPassword());
    }

    @Test
    void testAddQualifiedPosition() {
        // Initially employee has no qualifications
        assertTrue(employee.getQualifiedPositions().isEmpty());
        assertFalse(employee.isQualifiedFor(managerPosition));

        // Add qualification
        assertTrue(employee.addQualifiedPosition(managerPosition));
        assertTrue(employee.isQualifiedFor(managerPosition));

        // Add same qualification again - should return false as it's already added
        assertFalse(employee.addQualifiedPosition(managerPosition));

        // Add second qualification
        assertTrue(employee.addQualifiedPosition(cashierPosition));
        assertTrue(employee.isQualifiedFor(cashierPosition));

        // Check all qualifications
        Set<Position> qualifications = employee.getQualifiedPositions();
        assertEquals(2, qualifications.size());
        assertTrue(qualifications.contains(managerPosition));
        assertTrue(qualifications.contains(cashierPosition));
    }

    @Test
    void testEmployeeEquality() {
        // Same ID should be equal
        Employee sameIdEmployee = new Employee("123456789", "Different", "Name", "IL12-5555-5555-5555",
                LocalDate.now(), 25.0, Employee.UserRole.REGULAR_EMPLOYEE, "", 5, 10, "Harel");
        assertEquals(employee, sameIdEmployee);

        // Different ID should not be equal
        Employee differentIdEmployee = new Employee("987654321", "John", "Doe", "IL12-1234-1234-1234",
                LocalDate.of(2023, 1, 1), 35.0, Employee.UserRole.REGULAR_EMPLOYEE, "", 6, 12, "Migdal");
        assertNotEquals(employee, differentIdEmployee);
    }

    // Test for verifying role management in the Employee class.
    // This test ensures that changing the employee's role correctly updates
    // their managerial status, and that role-checking methods behave as expected.
    //
    @Test
    void testRoleManagement() {
        // Initially, the employee should not be a manager
        assertFalse(employee.isManager());

        // Change role to SHIFT_MANAGER and verify role behavior
        employee.setRole(Employee.UserRole.SHIFT_MANAGER);
        assertTrue(employee.isManager());
        assertTrue(employee.isShiftManager());
        assertFalse(employee.isHRManager());

        // Change role to HR_MANAGER and verify role behavior
        employee.setRole(Employee.UserRole.HR_MANAGER);
        assertTrue(employee.isManager());
        assertTrue(employee.isHRManager());
        assertFalse(employee.isShiftManager());

        // Change role back to REGULAR_EMPLOYEE and verify
        employee.setRole(Employee.UserRole.REGULAR_EMPLOYEE);
        assertFalse(employee.isManager());
        assertFalse(employee.isHRManager());
        assertFalse(employee.isShiftManager());
    }
}

class EmployeeAvailabilityTest {
    private EmployeeAvailability availability;

    @BeforeEach
    void setUp() {
        availability = new EmployeeAvailability("123456789");
    }

    @Test
    void testDefaultAvailability() {
        // By default, employees should be available for all shifts
        for (DayOfWeek day : DayOfWeek.values()) {
            assertTrue(availability.isAvailable(day, ShiftType.MORNING));
            assertTrue(availability.isAvailable(day, ShiftType.EVENING));
        }
    }

    @Test
    void testUpdateAvailability() {
        // Update Monday - not available for morning, available for evening
        availability.updateAvailability(DayOfWeek.MONDAY, false, true);

        // Check Monday
        assertFalse(availability.isAvailable(DayOfWeek.MONDAY, ShiftType.MORNING));
        assertTrue(availability.isAvailable(DayOfWeek.MONDAY, ShiftType.EVENING));

        // Other days should remain unchanged
        assertTrue(availability.isAvailable(DayOfWeek.TUESDAY, ShiftType.MORNING));
        assertTrue(availability.isAvailable(DayOfWeek.TUESDAY, ShiftType.EVENING));
    }

    @Test
    void testUpdateSpecificShiftAvailability() {
        // Update Tuesday evening only
        availability.updateAvailability(DayOfWeek.TUESDAY, ShiftType.EVENING, false);

        // Check Tuesday
        assertTrue(availability.isAvailable(DayOfWeek.TUESDAY, ShiftType.MORNING));
        assertFalse(availability.isAvailable(DayOfWeek.TUESDAY, ShiftType.EVENING));
    }
}

class ShiftTest {
    private Shift morningShift;
    private Shift eveningShift;
    private Employee employee1;
    private Employee employee2;
    private Position managerPosition;
    private Position cashierPosition;
    private LocalDate testDate;
    private IEmployeeManager employeeManager;

    @BeforeEach
    void setUp() {
        // השתמש ב-EmployeeManagerFactory במקום להשתמש ב-getInstance
        employeeManager = EmployeeManagerFactory.getEmployeeManager();

        testDate = LocalDate.of(2025, 4, 15); // A fixed test date

        morningShift = new Shift("2025-04-15_morning", testDate, ShiftType.MORNING);
        eveningShift = new Shift("2025-04-15_evening", testDate, ShiftType.EVENING);

        managerPosition = new Position("Shift Manager", true);
        cashierPosition = new Position("Cashier", false);

        // עדכון הקונסטרקטורים
        employee1 = new Employee("123456789", "John", "Doe", "IL12-1234-1234-1234",
                LocalDate.of(2023, 1, 1), 35.0, Employee.UserRole.SHIFT_MANAGER, "pass1", 5, 12, "Harel");
        employee2 = new Employee("987654321", "Jane", "Smith", "IL12-5678-5678-5678",
                LocalDate.of(2023, 2, 1), 30.0, Employee.UserRole.REGULAR_EMPLOYEE, "", 7, 10, "Migdal");

        // Set qualifications
        employee1.addQualifiedPosition(managerPosition);
        employee1.addQualifiedPosition(cashierPosition);
        employee2.addQualifiedPosition(cashierPosition);

        // Set availability - both available for all shifts
        for (DayOfWeek day : DayOfWeek.values()) {
            employee1.getAvailability().updateAvailability(day, true, true);
            employee2.getAvailability().updateAvailability(day, true, true);
        }
    }

    @Test
    void testShiftCreation() {
        // קבלת שעות משמרת דינמיות מהמנהל - שים לב לשימוש ב-employeeManager במקום ב-getInstance
        String[] morningHours = employeeManager.getShiftHours(ShiftType.MORNING);
        String[] eveningHours = employeeManager.getShiftHours(ShiftType.EVENING);

        // בדיקות עבור משמרת בוקר
        assertEquals("2025-04-15_morning", morningShift.getId());
        assertEquals(testDate, morningShift.getDate());
        assertEquals(ShiftType.MORNING, morningShift.getShiftType());
        assertEquals(java.time.LocalTime.parse(morningHours[0]), morningShift.getStartTime());
        assertEquals(java.time.LocalTime.parse(morningHours[1]), morningShift.getEndTime());

        // בדיקות עבור משמרת ערב
        assertEquals("2025-04-15_evening", eveningShift.getId());
        assertEquals(ShiftType.EVENING, eveningShift.getShiftType());
        assertEquals(java.time.LocalTime.parse(eveningHours[0]), eveningShift.getStartTime());
        assertEquals(java.time.LocalTime.parse(eveningHours[1]), eveningShift.getEndTime());
    }

    @Test
    void testAssignEmployee() {
        // Assign manager to morning shift
        assertTrue(morningShift.assignEmployee(managerPosition, employee1));
        assertEquals(employee1, morningShift.getShiftManager());
        assertEquals(employee1, morningShift.getAllAssignedEmployees().get(managerPosition));

        // Assign cashier to evening shift
        assertTrue(eveningShift.assignEmployee(cashierPosition, employee2));
        assertNull(eveningShift.getShiftManager()); // Not a manager position
        assertEquals(employee2, eveningShift.getAllAssignedEmployees().get(cashierPosition));
    }

    @Test
    void testAssignUnqualifiedEmployee() {
        // Create a position employee2 is not qualified for
        Position cookPosition = new Position("Cook", false);

        // Try to assign employee2 to cook position
        assertFalse(morningShift.assignEmployee(cookPosition, employee2));
        assertTrue(morningShift.getAllAssignedEmployees().isEmpty());
    }

    @Test
    void testAssignUnavailableEmployee() {
        // Make employee1 unavailable for morning shifts on Tuesday
        employee1.getAvailability().updateAvailability(DayOfWeek.TUESDAY, false, true);

        // Create a Tuesday morning shift
        LocalDate tuesday = LocalDate.of(2025, 4, 15); // Assuming this is a Tuesday
        Shift tuesdayMorning = new Shift("tuesday_morning", tuesday, ShiftType.MORNING);

        // Try to assign employee1 who is not available
        assertFalse(tuesdayMorning.assignEmployee(managerPosition, employee1));
        assertTrue(tuesdayMorning.getAllAssignedEmployees().isEmpty());
    }
}