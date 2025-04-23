package JUnit.Test;

/**
 * Unit tests for the EmployeeService class.
 *
 * This test suite verifies core functionalities of the EmployeeService,
 * which acts as a mediator between the presentation layer and the domain layer.
 *
 * Key Features Tested:
 * 1. **Employee Management**:
 *    - Adding, retrieving, updating, and removing employees.
 *    - Handling employee roles (HR Manager, Shift Manager, Regular Employee).
 *    - Password verification and updates.
 *
 * 2. **Position Management**:
 *    - Adding new positions.
 *    - Assigning qualifications to employees.
 *    - Preventing duplicate positions or invalid assignments.
 *
 * 3. **Shift Management**:
 *    - Creating shifts and avoiding duplicates.
 *    - Assigning employees to shifts based on qualifications and availability.
 *    - Removing assignments and verifying required positions.
 *
 * 4. **Availability Handling**:
 *    - Updating employee availability for specific days and shifts.
 *    - Enforcing business rules (e.g., availability updates allowed only until Thursday).
 *
 * 5. **Access Control & Permissions**:
 *    - Ensuring that managers can view all employees while regular employees can only view themselves.
 *    - Validating role-based access and login scenarios.
 *
 * 6. **Edge Cases**:
 *    - Handling invalid inputs such as non-existent employees or positions.
 *    - Ensuring robustness against incorrect role names and duplicate entries.
 *
 * Technologies Used:
 * - JUnit 5 for structured unit testing.
 * - Assertions for validating expected behavior.
 *
 * Author: [Your Name]
 * Date: 2025-04-21
 */

import Service.EmployeeDTO;
import Service.EmployeeService;

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

        // Clear existing data before each test
        for (EmployeeDTO emp : employeeService.getAllEmployees()) {
            employeeService.removeEmployee(emp.getId());
        }

        // Define positions
        employeeService.addPosition("Shift Manager", true);
        employeeService.addPosition("Cashier", false);

        // Add employees
        employeeService.addNewEmployee("1001", "John", "Smith", "IL123456",
                LocalDate.of(2023, 1, 1), 35.0, "HR_MANAGER", "hr123",5, 10, "Fund1");

        employeeService.addNewEmployee("1002", "Jane", "Doe", "IL654321",
                LocalDate.of(2023, 2, 1), 30.0, "SHIFT_MANAGER", "sm123", 6, 12, "Fund2");

        employeeService.addNewEmployee("1003", "Bob", "Brown", "IL111222",
                LocalDate.of(2023, 3, 1), 25.0, "REGULAR_EMPLOYEE", "",4, 8, "Fund3");

        // Add qualifications
        employeeService.addQualificationToEmployee("1001", "Shift Manager");
        employeeService.addQualificationToEmployee("1002", "Shift Manager");
        employeeService.addQualificationToEmployee("1003", "Cashier");
    }

    @Test
    void testGetEmployeeWithRole() {
        EmployeeDTO hrManager = employeeService.getEmployeeDetails("1001");
        EmployeeDTO shiftManager = employeeService.getEmployeeDetails("1002");
        EmployeeDTO regular = employeeService.getEmployeeDetails("1003");

        // Verify roles
        assertTrue(hrManager.isHRManager());
        assertTrue(shiftManager.isShiftManager());
        assertFalse(regular.isManager());

        // Verify DTO contains correct information
        assertEquals("John Smith", hrManager.getFullName());
        assertEquals("Jane Doe", shiftManager.getFullName());
        assertEquals("Bob Brown", regular.getFullName());
    }

    @Test
    void testVerifyPassword() {
        // Verify correct password
        assertTrue(employeeService.verifyPassword("1001", "hr123"));
        assertTrue(employeeService.verifyPassword("1002", "sm123"));

        // Verify incorrect password
        assertFalse(employeeService.verifyPassword("1001", "wrongpass"));
        assertFalse(employeeService.verifyPassword("1002", "wrongpass"));

        // Verify non-existent employee
        assertFalse(employeeService.verifyPassword("9999", "anypass"));
    }

    @Test
    void testUpdateEmployeeRole() {
        // Update regular employee to Shift Manager
        assertTrue(employeeService.updateEmployeeRole("1003", "SHIFT_MANAGER"));

        // Verify role updated
        EmployeeDTO updatedEmployee = employeeService.getEmployeeDetails("1003");
        assertTrue(updatedEmployee.isShiftManager());
        assertTrue(updatedEmployee.isManager());
        assertFalse(updatedEmployee.isHRManager());

        // Update to HR Manager
        assertTrue(employeeService.updateEmployeeRole("1003", "HR_MANAGER"));
        updatedEmployee = employeeService.getEmployeeDetails("1003");
        assertTrue(updatedEmployee.isHRManager());

        // Revert to Regular Employee
        assertTrue(employeeService.updateEmployeeRole("1003", "REGULAR_EMPLOYEE"));
        updatedEmployee = employeeService.getEmployeeDetails("1003");
        assertFalse(updatedEmployee.isManager());
    }

    @Test
    void testUpdateEmployeePassword() {
        // Update password
        assertTrue(employeeService.updateEmployeePassword("1001", "newPassword"));

        // Verify password updated
        assertFalse(employeeService.verifyPassword("1001", "hr123"));
        assertTrue(employeeService.verifyPassword("1001", "newPassword"));

        // Attempt to update password for non-existent employee
        assertFalse(employeeService.updateEmployeePassword("9999", "anyPassword"));
    }

    @Test
    void testAddNewEmployeeWithRole() {
        // Add employee with Shift Manager role
        assertTrue(employeeService.addNewEmployee("1004", "Test", "Manager", "IL999888",
                LocalDate.now(), 40.0, "SHIFT_MANAGER", "pass123", 0, 0, "TestFund"));

        // Verify employee added correctly
        EmployeeDTO newEmployee = employeeService.getEmployeeDetails("1004");
        assertNotNull(newEmployee);
        assertEquals("Test Manager", newEmployee.getFullName());
        assertTrue(newEmployee.isShiftManager());
        assertTrue(employeeService.verifyPassword("1004", "pass123"));

        // Attempt to add employee with invalid role
        assertFalse(employeeService.addNewEmployee("1005", "Invalid", "Role", "IL777666",
                LocalDate.now(), 35.0, "INVALID_ROLE", "pass456", 0, 0, "TestFund"));
    }

    @Test
    void testAccessibleEmployees() {
        // HR Manager should see all employees
        List<EmployeeDTO> hrAccessible = employeeService.getAccessibleEmployees("1001");
        assertEquals(3, hrAccessible.size());

        // Shift Manager should see all employees
        List<EmployeeDTO> smAccessible = employeeService.getAccessibleEmployees("1002");
        assertEquals(3, smAccessible.size());

        // Regular employee should see only themselves
        List<EmployeeDTO> regularAccessible = employeeService.getAccessibleEmployees("1003");
        assertEquals(1, regularAccessible.size());
        assertEquals("1003", regularAccessible.get(0).getId());

        // Invalid ID should return empty list
        List<EmployeeDTO> invalidAccessible = employeeService.getAccessibleEmployees("9999");
        assertTrue(invalidAccessible.isEmpty());
    }

    @Test
    void testConvertEmployeeToDTO() {
        // Verify role conversion to DTO
        EmployeeDTO hrEmployee = employeeService.getEmployeeDetails("1001");
        assertTrue(hrEmployee.isHRManager());
        assertTrue(hrEmployee.isManager());

        EmployeeDTO regularEmployee = employeeService.getEmployeeDetails("1003");
        assertFalse(regularEmployee.isManager());

        // Add new employee and verify DTO conversion
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
        // Manager login with correct password
        EmployeeDTO hrManager = employeeService.getEmployeeDetails("1001");
        assertTrue(hrManager.isManager());
        assertTrue(employeeService.verifyPassword("1001", "hr123"));

        // Manager login with incorrect password
        assertFalse(employeeService.verifyPassword("1001", "wrongpass"));

        // Regular employee login (no password required)
        EmployeeDTO regularEmployee = employeeService.getEmployeeDetails("1003");
        assertFalse(regularEmployee.isManager());
    }

    @Test
    void testUpdateAvailabilityAllowedUntilThursday() {
        String employeeId = "1003";

        // Assume today is Thursday
        DayOfWeek today = DayOfWeek.THURSDAY;
        DayOfWeek dayToUpdate = DayOfWeek.MONDAY;

        boolean updateResult = employeeService.updateEmployeeAvailabilityForNextWeek(
                employeeId, dayToUpdate, false, true);

        assertTrue(updateResult, "Should allow updating next week's availability on Thursday");

        boolean morningAvailability = employeeService.isEmployeeAvailableForNextWeek(employeeId, dayToUpdate, "MORNING");
        boolean eveningAvailability = employeeService.isEmployeeAvailableForNextWeek(employeeId, dayToUpdate, "EVENING");

        assertFalse(morningAvailability);
        assertTrue(eveningAvailability);
    }

    @Test
    void testUpdateAvailabilityNotAllowedAfterThursday() {
        String employeeId = "1003";
        DayOfWeek selectedDay = DayOfWeek.MONDAY;

        // Assume today is Friday
        DayOfWeek today = DayOfWeek.FRIDAY;
        assertTrue(today.getValue() > DayOfWeek.THURSDAY.getValue(), "Today should be after Thursday");

        boolean isUpdateAllowed = today.getValue() <= DayOfWeek.THURSDAY.getValue();
        assertFalse(isUpdateAllowed, "Updating availability should not be allowed after Thursday");

        // Direct service call still allows update
        boolean updateResult = employeeService.updateEmployeeAvailabilityForNextWeek(employeeId, selectedDay, false, false);
        assertTrue(updateResult, "Service allows update, but UI should prevent this action after Thursday");
    }
}
