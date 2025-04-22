//package JUnit.Test;
//
//
//
//import Domain.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class DomainTest {
//    private EmployeeManager employeeManager;
//    private Employee employee1;
//    private Employee employee2;
//    private Position managerPosition;
//    private Position cashierPosition;
//
//    @BeforeEach
//    void setUp() {
//        employeeManager = EmployeeManager.getInstance();
//        // Clear any existing data
//        for (Employee emp : employeeManager.getAllEmployees()) {
//            employeeManager.removeEmployee(emp.getId());
//        }
//
//        // Create test positions
//        managerPosition = new Position("Shift Manager", true);
//        cashierPosition = new Position("Cashier", false);
//
//        employeeManager.addPosition(managerPosition);
//        employeeManager.addPosition(cashierPosition);
//
//        // Create test employees
//        employee1 = new Employee("123456789", "John", "Doe", "IL12-1234-1234-1234",
//                LocalDate.of(2023, 1, 1), 35.0);
//        employee2 = new Employee("987654321", "Jane", "Smith", "IL12-5678-5678-5678",
//                LocalDate.of(2023, 2, 1), 30.0);
//
//        // Add qualifications
//        employee1.addQualifiedPosition(managerPosition);
//        employee1.addQualifiedPosition(cashierPosition);
//        employee2.addQualifiedPosition(cashierPosition);
//
//        // Set availability - employee1 available for all shifts, employee2 only for morning shifts
//        employee2.getAvailability().updateAvailability(DayOfWeek.MONDAY, true, false);
//
//        // Add employees to manager
//        employeeManager.addEmployee(employee1);
//        employeeManager.addEmployee(employee2);
//
//        // Set required positions
//        employeeManager.addRequiredPosition(ShiftType.MORNING, "Shift Manager", 1);
//        employeeManager.addRequiredPosition(ShiftType.MORNING, "Cashier", 2);
//        employeeManager.addRequiredPosition(ShiftType.EVENING, "Shift Manager", 1);
//        employeeManager.addRequiredPosition(ShiftType.EVENING, "Cashier", 1);
//    }
//
//    @Test
//    void testAddEmployee() {
//        Employee newEmployee = new Employee("111222333", "New", "Employee", "IL12-9999-9999-9999",
//                LocalDate.now(), 25.0);
//        assertTrue(employeeManager.addEmployee(newEmployee));
//        assertEquals(newEmployee, employeeManager.getEmployee("111222333"));
//
//        // Test adding duplicate employee
//        assertFalse(employeeManager.addEmployee(newEmployee));
//    }
//
//    @Test
//    void testRemoveEmployee() {
//        // Create a new employee that is not assigned to any shift
//        Employee tempEmployee = new Employee("555555555", "Temp", "Employee", "IL12-3333-3333-3333",
//                LocalDate.now(), 28.0);
//        employeeManager.addEmployee(tempEmployee);
//
//        // Test removing employee
//        Employee removed = employeeManager.removeEmployee("555555555");
//        assertEquals(tempEmployee, removed);
//        assertNull(employeeManager.getEmployee("555555555"));
//
//        // Test removing non-existent employee
//        assertNull(employeeManager.removeEmployee("000000000"));
//    }
//
//    @Test
//    void testCannotRemoveEmployeeAssignedToFutureShift() {
//        // Create a shift in the future
//        LocalDate futureDate = LocalDate.now().plusDays(7);
//        Shift morningShift = employeeManager.createShift(futureDate, ShiftType.MORNING);
//
//        // Assign employee1 to the shift
//        assertTrue(employeeManager.assignEmployeeToShift(morningShift.getId(), employee1.getId(), "Shift Manager"));
//
//        // Try to remove employee1 - should fail because they're assigned to a future shift
//        assertNull(employeeManager.removeEmployee(employee1.getId()));
//
//        // Employee should still exist
//        assertNotNull(employeeManager.getEmployee(employee1.getId()));
//    }
//
//    @Test
//    void testGetQualifiedEmployeesForPosition() {
//        List<Employee> qualifiedForManager = employeeManager.getQualifiedEmployeesForPosition(managerPosition);
//        assertEquals(1, qualifiedForManager.size());
//        assertTrue(qualifiedForManager.contains(employee1));
//
//        List<Employee> qualifiedForCashier = employeeManager.getQualifiedEmployeesForPosition(cashierPosition);
//        assertEquals(2, qualifiedForCashier.size());
//        assertTrue(qualifiedForCashier.contains(employee1));
//        assertTrue(qualifiedForCashier.contains(employee2));
//    }
//
//    @Test
//    void testGetAvailableEmployeesForShift() {
//        // Monday morning shift - both employees should be available
//        List<Employee> availableForMondayMorning = employeeManager.getAvailableEmployeesForShift(
//                LocalDate.now().with(DayOfWeek.MONDAY), ShiftType.MORNING);
//        assertEquals(2, availableForMondayMorning.size());
//
//        // Monday evening shift - only employee1 should be available
//        List<Employee> availableForMondayEvening = employeeManager.getAvailableEmployeesForShift(
//                LocalDate.now().with(DayOfWeek.MONDAY), ShiftType.EVENING);
//        assertEquals(1, availableForMondayEvening.size());
//        assertEquals(employee1, availableForMondayEvening.get(0));
//    }
//
//    @Test
//    void testAddQualificationToEmployee() {
//        // Create new position and employee
//        Position cookPosition = new Position("Cook", false);
//        employeeManager.addPosition(cookPosition);
//
//        Employee newEmployee = new Employee("444444444", "New", "Cook", "IL12-7777-7777-7777",
//                LocalDate.now(), 32.0);
//        employeeManager.addEmployee(newEmployee);
//
//        // Add qualification
//        assertTrue(employeeManager.addQualificationToEmployee("444444444", "Cook"));
//        assertTrue(newEmployee.isQualifiedFor(cookPosition));
//
//        // Test adding non-existent position
//        assertFalse(employeeManager.addQualificationToEmployee("444444444", "NonExistentPosition"));
//
//        // Test adding to non-existent employee
//        assertFalse(employeeManager.addQualificationToEmployee("999999999", "Cook"));
//    }
//
//    @Test
//    void testUpdateEmployeeAvailability() {
//        // Update employee1's availability for Tuesday
//        assertTrue(employeeManager.updateEmployeeAvailability(employee1.getId(), DayOfWeek.TUESDAY, true, false));
//
//        // Check that employee1 is available for Tuesday morning but not evening
//        assertTrue(employee1.getAvailability().isAvailable(DayOfWeek.TUESDAY, ShiftType.MORNING));
//        assertFalse(employee1.getAvailability().isAvailable(DayOfWeek.TUESDAY, ShiftType.EVENING));
//
//        // Test updating non-existent employee
//        assertFalse(employeeManager.updateEmployeeAvailability("999999999", DayOfWeek.TUESDAY, true, true));
//    }
//
//    @Test
//    void testCreateShift() {
//        LocalDate nextMonday = LocalDate.now().plusDays(7).with(DayOfWeek.MONDAY);
//
//        // Create a morning shift
//        Shift morningShift = employeeManager.createShift(nextMonday, ShiftType.MORNING);
//        assertNotNull(morningShift);
//        assertEquals(nextMonday, morningShift.getDate());
//        assertEquals(ShiftType.MORNING, morningShift.getShiftType());
//
//        // Create an evening shift
//        Shift eveningShift = employeeManager.createShift(nextMonday, ShiftType.EVENING);
//        assertNotNull(eveningShift);
//        assertEquals(ShiftType.EVENING, eveningShift.getShiftType());
//
//        // Try to create duplicate shift - should return null
//        assertNull(employeeManager.createShift(nextMonday, ShiftType.MORNING));
//    }
//
//    @Test
//    void testAssignEmployeeToShift() {
//        // Create a shift
//        LocalDate nextTuesday = LocalDate.now().plusDays(7).with(DayOfWeek.TUESDAY);
//        Shift morningShift = employeeManager.createShift(nextTuesday, ShiftType.MORNING);
//
//        // Update employee availability to ensure they're available
//        employee1.getAvailability().updateAvailability(nextTuesday.getDayOfWeek(), true, true);
//
//        // Assign employee to shift as manager
//        assertTrue(employeeManager.assignEmployeeToShift(morningShift.getId(), employee1.getId(), "Shift Manager"));
//
//        // Check that employee is assigned and is shift manager
//        Map<Position, Employee> assignedEmployees = morningShift.getAllAssignedEmployees();
//        assertEquals(employee1, assignedEmployees.get(managerPosition));
//        assertEquals(employee1, morningShift.getShiftManager());
//
//        // Try to assign employee to position they're not qualified for
//        Position cookPosition = new Position("Cook", false);
//        employeeManager.addPosition(cookPosition);
//        assertFalse(employeeManager.assignEmployeeToShift(morningShift.getId(), employee1.getId(), "Cook"));
//    }
//
//    @Test
//    void testAssignUnavailableEmployeeToShift() {
//        // Create a shift for Monday evening
//        LocalDate nextMonday = LocalDate.now().plusDays(7).with(DayOfWeek.MONDAY);
//        Shift eveningShift = employeeManager.createShift(nextMonday, ShiftType.EVENING);
//
//        // Try to assign employee2 who is not available for evening shifts on Monday
//        assertFalse(employeeManager.assignEmployeeToShift(eveningShift.getId(), employee2.getId(), "Cashier"));
//
//        // Make employee2 available for Monday evening
//        employee2.getAvailability().updateAvailability(DayOfWeek.MONDAY, true, true);
//
//        // Now the assignment should succeed
//        assertTrue(employeeManager.assignEmployeeToShift(eveningShift.getId(), employee2.getId(), "Cashier"));
//    }
//
//    @Test
//    void testRemoveAssignmentFromShift() {
//        // Create a shift and assign employee
//        LocalDate nextWednesday = LocalDate.now().plusDays(7).with(DayOfWeek.WEDNESDAY);
//        Shift morningShift = employeeManager.createShift(nextWednesday, ShiftType.MORNING);
//        employeeManager.assignEmployeeToShift(morningShift.getId(), employee1.getId(), "Shift Manager");
//
//        // Remove assignment
//        assertTrue(employeeManager.removeAssignmentFromShift(morningShift.getId(), "Shift Manager"));
//
//        // Check that employee is no longer assigned and no shift manager exists
//        assertTrue(morningShift.getAllAssignedEmployees().isEmpty());
//        assertNull(morningShift.getShiftManager());
//
//        // Try to remove non-existent assignment
//        assertFalse(employeeManager.removeAssignmentFromShift(morningShift.getId(), "Cashier"));
//    }
//
//    @Test
//    void testAreAllRequiredPositionsCovered() {
//        // Create a shift
//        LocalDate nextThursday = LocalDate.now().plusDays(7).with(DayOfWeek.THURSDAY);
//        Shift morningShift = employeeManager.createShift(nextThursday, ShiftType.MORNING);
//
//        // Check that positions are not covered initially
//        assertFalse(employeeManager.areAllRequiredPositionsCovered(morningShift.getId()));
//
//        // Assign manager (but still need 2 cashiers)
//        employeeManager.assignEmployeeToShift(morningShift.getId(), employee1.getId(), "Shift Manager");
//        assertFalse(employeeManager.areAllRequiredPositionsCovered(morningShift.getId()));
//
//        // Assign one cashier (still need one more)
//        employeeManager.assignEmployeeToShift(morningShift.getId(), employee2.getId(), "Cashier");
//        assertFalse(employeeManager.areAllRequiredPositionsCovered(morningShift.getId()));
//
//        // Create and assign another cashier to fulfill requirements
//        Employee employee3 = new Employee("333333333", "Third", "Employee", "IL12-8888-8888-8888",
//                LocalDate.now(), 28.0);
//        employee3.addQualifiedPosition(cashierPosition);
//        employeeManager.addEmployee(employee3);
//
//        // Now assign the third employee
//        employeeManager.assignEmployeeToShift(morningShift.getId(), employee3.getId(), "Cashier");
//
//        // Check that all positions are now covered
//        assertTrue(employeeManager.areAllRequiredPositionsCovered(morningShift.getId()));
//    }
//}
//
//class EmployeeTest {
//    private Employee employee;
//    private Position managerPosition;
//    private Position cashierPosition;
//
//    @BeforeEach
//    void setUp() {
//        employee = new Employee("123456789", "John", "Doe", "IL12-1234-1234-1234",
//                LocalDate.of(2023, 1, 1), 35.0);
//
//        managerPosition = new Position("Shift Manager", true);
//        cashierPosition = new Position("Cashier", false);
//    }
//
//    @Test
//    void testEmployeeCreation() {
//        assertEquals("123456789", employee.getId());
//        assertEquals("John", employee.getFirstName());
//        assertEquals("Doe", employee.getLastName());
//        assertEquals("John Doe", employee.getFullName());
//        assertEquals("IL12-1234-1234-1234", employee.getBankAccount());
//        assertEquals(LocalDate.of(2023, 1, 1), employee.getStartDate());
//        assertEquals(35.0, employee.getSalary());
//    }
//
//    @Test
//    void testEmployeeSetters() {
//        employee.setFirstName("Jane");
//        assertEquals("Jane", employee.getFirstName());
//
//        employee.setLastName("Smith");
//        assertEquals("Smith", employee.getLastName());
//        assertEquals("Jane Smith", employee.getFullName());
//
//        employee.setBankAccount("IL12-9999-9999-9999");
//        assertEquals("IL12-9999-9999-9999", employee.getBankAccount());
//
//        employee.setSalary(40.0);
//        assertEquals(40.0, employee.getSalary());
//    }
//
//    @Test
//    void testAddQualifiedPosition() {
//        // Initially employee has no qualifications
//        assertTrue(employee.getQualifiedPositions().isEmpty());
//        assertFalse(employee.isQualifiedFor(managerPosition));
//
//        // Add qualification
//        assertTrue(employee.addQualifiedPosition(managerPosition));
//        assertTrue(employee.isQualifiedFor(managerPosition));
//
//        // Add same qualification again - should return false as it's already added
//        assertFalse(employee.addQualifiedPosition(managerPosition));
//
//        // Add second qualification
//        assertTrue(employee.addQualifiedPosition(cashierPosition));
//        assertTrue(employee.isQualifiedFor(cashierPosition));
//
//        // Check all qualifications
//        Set<Position> qualifications = employee.getQualifiedPositions();
//        assertEquals(2, qualifications.size());
//        assertTrue(qualifications.contains(managerPosition));
//        assertTrue(qualifications.contains(cashierPosition));
//    }
//
//    @Test
//    void testEmployeeEquality() {
//        // Same ID should be equal
//        Employee sameIdEmployee = new Employee("123456789", "Different", "Name", "IL12-5555-5555-5555",
//                LocalDate.now(), 25.0);
//        assertEquals(employee, sameIdEmployee);
//
//        // Different ID should not be equal
//        Employee differentIdEmployee = new Employee("987654321", "John", "Doe", "IL12-1234-1234-1234",
//                LocalDate.of(2023, 1, 1), 35.0);
//        assertNotEquals(employee, differentIdEmployee);
//    }
//}
//
//class EmployeeAvailabilityTest {
//    private EmployeeAvailability availability;
//
//    @BeforeEach
//    void setUp() {
//        availability = new EmployeeAvailability("123456789");
//    }
//
//    @Test
//    void testDefaultAvailability() {
//        // By default, employees should be available for all shifts
//        for (DayOfWeek day : DayOfWeek.values()) {
//            assertTrue(availability.isAvailable(day, ShiftType.MORNING));
//            assertTrue(availability.isAvailable(day, ShiftType.EVENING));
//        }
//    }
//
//    @Test
//    void testUpdateAvailability() {
//        // Update Monday - not available for morning, available for evening
//        availability.updateAvailability(DayOfWeek.MONDAY, false, true);
//
//        // Check Monday
//        assertFalse(availability.isAvailable(DayOfWeek.MONDAY, ShiftType.MORNING));
//        assertTrue(availability.isAvailable(DayOfWeek.MONDAY, ShiftType.EVENING));
//
//        // Other days should remain unchanged
//        assertTrue(availability.isAvailable(DayOfWeek.TUESDAY, ShiftType.MORNING));
//        assertTrue(availability.isAvailable(DayOfWeek.TUESDAY, ShiftType.EVENING));
//    }
//
//    @Test
//    void testUpdateSpecificShiftAvailability() {
//        // Update Tuesday evening only
//        availability.updateAvailability(DayOfWeek.TUESDAY, ShiftType.EVENING, false);
//
//        // Check Tuesday
//        assertTrue(availability.isAvailable(DayOfWeek.TUESDAY, ShiftType.MORNING));
//        assertFalse(availability.isAvailable(DayOfWeek.TUESDAY, ShiftType.EVENING));
//    }
//}
//
//class ShiftTest {
//    private Shift morningShift;
//    private Shift eveningShift;
//    private Employee employee1;
//    private Employee employee2;
//    private Position managerPosition;
//    private Position cashierPosition;
//    private LocalDate testDate;
//
//    @BeforeEach
//    void setUp() {
//        testDate = LocalDate.of(2025, 4, 15); // A fixed test date
//
//        morningShift = new Shift("2025-04-15_morning", testDate, ShiftType.MORNING);
//        eveningShift = new Shift("2025-04-15_evening", testDate, ShiftType.EVENING);
//
//        managerPosition = new Position("Shift Manager", true);
//        cashierPosition = new Position("Cashier", false);
//
//        employee1 = new Employee("123456789", "John", "Doe", "IL12-1234-1234-1234",
//                LocalDate.of(2023, 1, 1), 35.0);
//        employee2 = new Employee("987654321", "Jane", "Smith", "IL12-5678-5678-5678",
//                LocalDate.of(2023, 2, 1), 30.0);
//
//        // Set qualifications
//        employee1.addQualifiedPosition(managerPosition);
//        employee1.addQualifiedPosition(cashierPosition);
//        employee2.addQualifiedPosition(cashierPosition);
//
//        // Set availability - both available for all shifts
//        for (DayOfWeek day : DayOfWeek.values()) {
//            employee1.getAvailability().updateAvailability(day, true, true);
//            employee2.getAvailability().updateAvailability(day, true, true);
//        }
//    }
//
//    @Test
//    void testShiftCreation() {
//        assertEquals("2025-04-15_morning", morningShift.getId());
//        assertEquals(testDate, morningShift.getDate());
//        assertEquals(ShiftType.MORNING, morningShift.getShiftType());
//        assertEquals(Shift.MORNING_SHIFT_START, morningShift.getStartTime());
//        assertEquals(Shift.MORNING_SHIFT_END, morningShift.getEndTime());
//
//        assertEquals("2025-04-15_evening", eveningShift.getId());
//        assertEquals(ShiftType.EVENING, eveningShift.getShiftType());
//        assertEquals(Shift.EVENING_SHIFT_START, eveningShift.getStartTime());
//        assertEquals(Shift.EVENING_SHIFT_END, eveningShift.getEndTime());
//    }
//
//    @Test
//    void testAssignEmployee() {
//        // Assign manager to morning shift
//        assertTrue(morningShift.assignEmployee(managerPosition, employee1));
//        assertEquals(employee1, morningShift.getShiftManager());
//        assertEquals(employee1, morningShift.getAllAssignedEmployees().get(managerPosition));
//
//        // Assign cashier to evening shift
//        assertTrue(eveningShift.assignEmployee(cashierPosition, employee2));
//        assertNull(eveningShift.getShiftManager()); // Not a manager position
//        assertEquals(employee2, eveningShift.getAllAssignedEmployees().get(cashierPosition));
//    }
//
//    @Test
//    void testAssignUnqualifiedEmployee() {
//        // Create a position employee2 is not qualified for
//        Position cookPosition = new Position("Cook", false);
//
//        // Try to assign employee2 to cook position
//        assertFalse(morningShift.assignEmployee(cookPosition, employee2));
//        assertTrue(morningShift.getAllAssignedEmployees().isEmpty());
//    }
//
//    @Test
//    void testAssignUnavailableEmployee() {
//        // Make employee1 unavailable for morning shifts on Tuesday
//        employee1.getAvailability().updateAvailability(DayOfWeek.TUESDAY, false, true);
//
//        // Create a Tuesday morning shift
//        LocalDate tuesday = LocalDate.of(2025, 4, 15); // Assuming this is a Tuesday
//        Shift tuesdayMorning = new Shift("tuesday_morning", tuesday, ShiftType.MORNING);
//
//        // Try to assign employee1 who is not available
//        assertFalse(tuesdayMorning.assignEmployee(managerPosition, employee1));
//        assertTrue(tuesdayMorning.getAllAssignedEmployees().isEmpty());
//    }
//
//}
package JUnit.Test;

import Domain.*;
import Domain.Employee.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DomainTest {
    private EmployeeManager employeeManager;
    private Employee employee1;
    private Employee employee2;
    private Position managerPosition;
    private Position cashierPosition;

    @BeforeEach
    void setUp() {
        employeeManager = EmployeeManager.getInstance();
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
        employee1 = new Employee("123456789", "John", "Doe", "IL12-1234-1234-1234",
                LocalDate.of(2023, 1, 1), 35.0, UserRole.SHIFT_MANAGER, "password1");
        employee2 = new Employee("987654321", "Jane", "Smith", "IL12-5678-5678-5678",
                LocalDate.of(2023, 2, 1), 30.0, UserRole.REGULAR_EMPLOYEE, "");

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
                LocalDate.now(), 25.0, UserRole.REGULAR_EMPLOYEE, "");
        assertTrue(employeeManager.addEmployee(newEmployee));
        assertEquals(newEmployee, employeeManager.getEmployee("111222333"));

        // Test adding duplicate employee
        assertFalse(employeeManager.addEmployee(newEmployee));
    }

    @Test
    void testRemoveEmployee() {
        // Create a new employee that is not assigned to any shift
        Employee tempEmployee = new Employee("555555555", "Temp", "Employee", "IL12-3333-3333-3333",
                LocalDate.now(), 28.0, UserRole.REGULAR_EMPLOYEE, "");
        employeeManager.addEmployee(tempEmployee);

        // Test removing employee
        Employee removed = employeeManager.removeEmployee("555555555");
        assertEquals(tempEmployee, removed);
        assertNull(employeeManager.getEmployee("555555555"));

        // Test removing non-existent employee
        assertNull(employeeManager.removeEmployee("000000000"));
    }

//    @Test
//    void testCannotRemoveEmployeeAssignedToFutureShift() {
//        // Create a shift in the future
//        LocalDate futureDate = LocalDate.now().plusDays(7);
//        Shift morningShift = employeeManager.createShift(futureDate, ShiftType.MORNING);
//
//        // Assign employee1 to the shift
//        assertTrue(employeeManager.assignEmployeeToShift(morningShift.getId(), employee1.getId(), "Shift Manager"));
//
//        // Try to remove employee1 - should fail because they're assigned to a future shift
//        assertNull(employeeManager.removeEmployee(employee1.getId()));
//
//        // Employee should still exist
//        assertNotNull(employeeManager.getEmployee(employee1.getId()));
//    }


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
    void testGetAvailableEmployeesForShift() {
        // Monday morning shift - both employees should be available
        List<Employee> availableForMondayMorning = employeeManager.getAvailableEmployeesForShift(
                LocalDate.now().with(DayOfWeek.MONDAY), ShiftType.MORNING);
        assertEquals(2, availableForMondayMorning.size());

        // Monday evening shift - only employee1 should be available
        List<Employee> availableForMondayEvening = employeeManager.getAvailableEmployeesForShift(
                LocalDate.now().with(DayOfWeek.MONDAY), ShiftType.EVENING);
        assertEquals(1, availableForMondayEvening.size());
        assertEquals(employee1, availableForMondayEvening.get(0));
    }

    @Test
    void testAddQualificationToEmployee() {
        // Create new position and employee
        Position cookPosition = new Position("Cook", false);
        employeeManager.addPosition(cookPosition);

        Employee newEmployee = new Employee("444444444", "New", "Cook", "IL12-7777-7777-7777",
                LocalDate.now(), 32.0, UserRole.REGULAR_EMPLOYEE, "");
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

    @Test
    void testAssignEmployeeToShift() {
        // Create a shift
        LocalDate nextTuesday = LocalDate.now().plusDays(7).with(DayOfWeek.TUESDAY);
        Shift morningShift = employeeManager.createShift(nextTuesday, ShiftType.MORNING);

        // Update employee availability to ensure they're available
        employee1.getAvailability().updateAvailability(nextTuesday.getDayOfWeek(), true, true);

        // Assign employee to shift as manager
        assertTrue(employeeManager.assignEmployeeToShift(morningShift.getId(), employee1.getId(), "Shift Manager"));

        // Check that employee is assigned and is shift manager
        Map<Position, Employee> assignedEmployees = morningShift.getAllAssignedEmployees();
        assertEquals(employee1, assignedEmployees.get(managerPosition));
        assertEquals(employee1, morningShift.getShiftManager());

        // Try to assign employee to position they're not qualified for
        Position cookPosition = new Position("Cook", false);
        employeeManager.addPosition(cookPosition);
        assertFalse(employeeManager.assignEmployeeToShift(morningShift.getId(), employee1.getId(), "Cook"));
    }

    @Test
    void testAssignUnavailableEmployeeToShift() {
        // Create a shift for Monday evening
        LocalDate nextMonday = LocalDate.now().with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.MONDAY));
        Shift eveningShift = employeeManager.createShift(nextMonday, ShiftType.EVENING);

        // Try to assign employee2 who is not available for evening shifts on Monday
        assertFalse(employeeManager.assignEmployeeToShift(eveningShift.getId(), employee2.getId(), "Cashier"));

        // Make employee2 available for Monday evening
        employee2.getAvailability().updateAvailability(DayOfWeek.MONDAY, true, true);

        // Now the assignment should succeed
        assertTrue(employeeManager.assignEmployeeToShift(eveningShift.getId(), employee2.getId(), "Cashier"));
    }

    @Test
    void testRemoveAssignmentFromShift() {
        // Create a shift and assign employee
        LocalDate nextWednesday = LocalDate.now().plusDays(7).with(DayOfWeek.WEDNESDAY);
        Shift morningShift = employeeManager.createShift(nextWednesday, ShiftType.MORNING);
        employeeManager.assignEmployeeToShift(morningShift.getId(), employee1.getId(), "Shift Manager");

        // Remove assignment
        assertTrue(employeeManager.removeAssignmentFromShift(morningShift.getId(), "Shift Manager"));

        // Check that employee is no longer assigned and no shift manager exists
        assertTrue(morningShift.getAllAssignedEmployees().isEmpty());
        assertNull(morningShift.getShiftManager());

        // Try to remove non-existent assignment
        assertFalse(employeeManager.removeAssignmentFromShift(morningShift.getId(), "Cashier"));
    }

    //    @Test
//    void testAreAllRequiredPositionsCovered() {
//        // Create a shift
//        LocalDate nextThursday = LocalDate.now().plusDays(7).with(DayOfWeek.THURSDAY);
//        Shift morningShift = employeeManager.createShift(nextThursday, ShiftType.MORNING);
//
//        // Check that positions are not covered initially
//        assertFalse(employeeManager.areAllRequiredPositionsCovered(morningShift.getId()));
//
//        // Assign manager (but still need 2 cashiers)
//        employeeManager.assignEmployeeToShift(morningShift.getId(), employee1.getId(), "Shift Manager");
//        assertFalse(employeeManager.areAllRequiredPositionsCovered(morningShift.getId()));
//
//        // Assign one cashier (still need one more)
//        employeeManager.assignEmployeeToShift(morningShift.getId(), employee2.getId(), "Cashier");
//        assertFalse(employeeManager.areAllRequiredPositionsCovered(morningShift.getId()));
//
//        // Create and assign another cashier to fulfill requirements
//        Employee employee3 = new Employee("333333333", "Third", "Employee", "IL12-8888-8888-8888",
//                LocalDate.now(), 28.0, UserRole.REGULAR_EMPLOYEE, "");
//        employee3.addQualifiedPosition(cashierPosition);
//        employeeManager.addEmployee(employee3);
//
//        // Now assign the third employee
//        employeeManager.assignEmployeeToShift(morningShift.getId(), employee3.getId(), "Cashier");
//
//        // Check that all positions are now covered
//        assertTrue(employeeManager.areAllRequiredPositionsCovered(morningShift.getId()));
//    }
    @Test
    void testAreAllRequiredPositionsCovered() {
        // Create a shift
        LocalDate nextThursday = LocalDate.now().plusDays(7).with(DayOfWeek.THURSDAY);
        Shift morningShift = employeeManager.createShift(nextThursday, ShiftType.MORNING);

        // Check that positions are not covered initially
        assertFalse(employeeManager.areAllRequiredPositionsCovered(morningShift.getId()));

        // Assign manager (but still need 2 cashiers)
        employeeManager.assignEmployeeToShift(morningShift.getId(), employee1.getId(), "Shift Manager");
        assertFalse(employeeManager.areAllRequiredPositionsCovered(morningShift.getId()));

        // Assign one cashier (still need one more)
        employeeManager.assignEmployeeToShift(morningShift.getId(), employee2.getId(), "Cashier");
        assertFalse(employeeManager.areAllRequiredPositionsCovered(morningShift.getId()));

        // Create and assign another cashier to fulfill requirements
        Employee employee3 = new Employee("333333333", "Third", "Employee", "IL12-8888-8888-8888",
                LocalDate.now(), 28.0, UserRole.REGULAR_EMPLOYEE, "");
        employee3.addQualifiedPosition(cashierPosition);
        employeeManager.addEmployee(employee3);

        // Now assign the third employee
        employeeManager.assignEmployeeToShift(morningShift.getId(), employee3.getId(), "Cashier");

        // הוספת הדפסות לניפוי שגיאות
        System.out.println("========= DEBUG INFO =========");
        System.out.println("Required Positions: " + employeeManager.getRequiredPositions().getRequiredPositionsMap(ShiftType.MORNING));
        System.out.println("Assigned Employees: " + morningShift.getAllAssignedEmployees());

        // הדפסת שמות התפקידים במפת התפקידים הנדרשים
        System.out.println("Required Position Names:");
        for (Position pos : employeeManager.getRequiredPositions().getRequiredPositionsMap(ShiftType.MORNING).keySet()) {
            System.out.println("  - " + pos.getName());
        }

        // הדפסת שמות התפקידים במפת התפקידים המוקצים
        System.out.println("Assigned Position Names:");
        for (Position pos : morningShift.getAllAssignedEmployees().keySet()) {
            System.out.println("  - " + pos.getName());
        }

        // Check that all positions are now covered
        assertTrue(employeeManager.areAllRequiredPositionsCovered(morningShift.getId()));
    }

    // בדיקה חדשה למערכת ההרשאות
    @Test
    void testEmployeeRoles() {
        // הגדרת עובד כמנהל משמרת
        Employee shiftManager = new Employee("111111", "Shift", "Manager", "IL12-1111-1111-1111",
                LocalDate.now(), 45.0, UserRole.SHIFT_MANAGER, "sm123");

        // הגדרת עובד כמנהל כח אדם
        Employee hrManager = new Employee("222222", "HR", "Manager", "IL12-2222-2222-2222",
                LocalDate.now(), 50.0, UserRole.HR_MANAGER, "hr123");

        // הגדרת עובד רגיל
        Employee regular = new Employee("333333", "Regular", "Employee", "IL12-3333-3333-3333",
                LocalDate.now(), 30.0, UserRole.REGULAR_EMPLOYEE, "");

        // בדיקת תפקידים
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

    // בדיקה לאימות סיסמה
    @Test
    void testPasswordAuthentication() {
        // הגדרת עובד עם סיסמה
        Employee manager = new Employee("444444", "Password", "Test", "IL12-4444-4444-4444",
                LocalDate.now(), 40.0, UserRole.HR_MANAGER, "securePass123");

        // בדיקת סיסמה נכונה
        assertEquals("securePass123", manager.getPassword());

        // עדכון סיסמה
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
        // עדכון הקונסטרקטור להתאמה למחלקה המעודכנת
        employee = new Employee("123456789", "John", "Doe", "IL12-1234-1234-1234",
                LocalDate.of(2023, 1, 1), 35.0, Employee.UserRole.REGULAR_EMPLOYEE, "");

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

        // בדיקת עדכון תפקיד וסיסמה
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
                LocalDate.now(), 25.0, Employee.UserRole.REGULAR_EMPLOYEE, "");
        assertEquals(employee, sameIdEmployee);

        // Different ID should not be equal
        Employee differentIdEmployee = new Employee("987654321", "John", "Doe", "IL12-1234-1234-1234",
                LocalDate.of(2023, 1, 1), 35.0, Employee.UserRole.REGULAR_EMPLOYEE, "");
        assertNotEquals(employee, differentIdEmployee);
    }

    @Test
    void testRoleManagement() {
        // בדיקת ברירת מחדל
        assertFalse(employee.isManager());

        // שינוי תפקיד למנהל משמרת
        employee.setRole(Employee.UserRole.SHIFT_MANAGER);
        assertTrue(employee.isManager());
        assertTrue(employee.isShiftManager());
        assertFalse(employee.isHRManager());

        // שינוי תפקיד למנהל כח אדם
        employee.setRole(Employee.UserRole.HR_MANAGER);
        assertTrue(employee.isManager());
        assertTrue(employee.isHRManager());
        assertFalse(employee.isShiftManager());

        // חזרה לעובד רגיל
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

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2025, 4, 15); // A fixed test date

        morningShift = new Shift("2025-04-15_morning", testDate, ShiftType.MORNING);
        eveningShift = new Shift("2025-04-15_evening", testDate, ShiftType.EVENING);

        managerPosition = new Position("Shift Manager", true);
        cashierPosition = new Position("Cashier", false);

        // עדכון הקונסטרקטורים
        employee1 = new Employee("123456789", "John", "Doe", "IL12-1234-1234-1234",
                LocalDate.of(2023, 1, 1), 35.0, Employee.UserRole.SHIFT_MANAGER, "pass1");
        employee2 = new Employee("987654321", "Jane", "Smith", "IL12-5678-5678-5678",
                LocalDate.of(2023, 2, 1), 30.0, Employee.UserRole.REGULAR_EMPLOYEE, "");

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
        // קבלת שעות משמרת דינמיות מהמנהל
        String[] morningHours = EmployeeManager.getInstance().getShiftHours(ShiftType.MORNING);
        String[] eveningHours = EmployeeManager.getInstance().getShiftHours(ShiftType.EVENING);

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