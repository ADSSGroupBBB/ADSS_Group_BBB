package JUnit.Test_employee;

import Service_employee.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the EmployeeService class with updated DAO architecture and mandatory branch assignment.
 */
public class EmployeeServiceTest {

    private EmployeeService employeeService;
    private PositionService positionService;
    private BranchService branchService;
    private String testBranchAddress;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService();
        positionService = new PositionService();
        branchService = new BranchService();

        // Get first available branch for testing
        List<BranchDTO> branches = branchService.getAllBranches();
        if (branches.isEmpty()) {
            fail("No branches available for testing. Ensure database has branch data.");
        }
        testBranchAddress = branches.get(0).getAddress();

        // Clear existing data before each test (keep admin)
        List<EmployeeDTO> existingEmployees = employeeService.getAllEmployees();
        for (EmployeeDTO emp : existingEmployees) {
            if (!emp.getId().equals("admin")) {
                employeeService.removeEmployee(emp.getId());
            }
        }

        // Define positions
        positionService.addPosition("Shift Manager", true);
        positionService.addPosition("Cashier", false);

        // Add employees - ALL WITH MANDATORY BRANCH ASSIGNMENT
        employeeService.addManagerEmployee("1001", "John", "Smith", "IL123456",
                LocalDate.of(2023, 1, 1), 35.0, "HR_MANAGER", "hr123",
                5, 10, "Fund1", testBranchAddress);

        employeeService.addManagerEmployee("1002", "Jane", "Doe", "IL654321",
                LocalDate.of(2023, 2, 1), 30.0, "SHIFT_MANAGER", "sm123",
                6, 12, "Fund2", testBranchAddress);

        employeeService.addEmployee("1003", "Bob", "Brown", "IL111222",
                LocalDate.of(2023, 3, 1), 25.0, 4, 8, "Fund3", testBranchAddress);

        // Add qualifications
        positionService.addQualificationToEmployee("1001", "Shift Manager");
        positionService.addQualificationToEmployee("1002", "Shift Manager");
        positionService.addQualificationToEmployee("1003", "Cashier");
    }

    @Test
    void testGetEmployeeWithRole() {
        EmployeeDTO hrManager = employeeService.getEmployeeDetails("1001");
        EmployeeDTO shiftManager = employeeService.getEmployeeDetails("1002");
        EmployeeDTO regular = employeeService.getEmployeeDetails("1003");

        assertNotNull(hrManager, "HR Manager should exist");
        assertNotNull(shiftManager, "Shift Manager should exist");
        assertNotNull(regular, "Regular employee should exist");

        // Verify roles
        assertTrue(hrManager.isHRManager(), "Employee 1001 should be HR Manager");
        assertTrue(shiftManager.isShiftManager(), "Employee 1002 should be Shift Manager");
        assertFalse(regular.isManager(), "Employee 1003 should not be a manager");

        // Verify DTO contains correct information
        assertEquals("John Smith", hrManager.getFullName());
        assertEquals("Jane Doe", shiftManager.getFullName());
        assertEquals("Bob Brown", regular.getFullName());

        // Verify all employees have branch assignments
        assertTrue(hrManager.hasBranch(), "HR Manager should have branch assignment");
        assertTrue(shiftManager.hasBranch(), "Shift Manager should have branch assignment");
        assertTrue(regular.hasBranch(), "Regular employee should have branch assignment");

        assertEquals(testBranchAddress, hrManager.getBranchAddress(), "HR Manager should be in test branch");
        assertEquals(testBranchAddress, shiftManager.getBranchAddress(), "Shift Manager should be in test branch");
        assertEquals(testBranchAddress, regular.getBranchAddress(), "Regular employee should be in test branch");
    }

    @Test
    void testVerifyPassword() {
        // Verify correct password
        assertTrue(employeeService.verifyPassword("1001", "hr123"), "Should verify correct HR password");
        assertTrue(employeeService.verifyPassword("1002", "sm123"), "Should verify correct SM password");

        // Verify incorrect password
        assertFalse(employeeService.verifyPassword("1001", "wrongpass"), "Should reject wrong password");
        assertFalse(employeeService.verifyPassword("1002", "wrongpass"), "Should reject wrong password");

        // Verify non-existent employee
        assertFalse(employeeService.verifyPassword("9999", "anypass"), "Should reject non-existent employee");
    }

    @Test
    void testUpdateEmployeeRole() {
        // Update regular employee to Shift Manager
        boolean updateResult = employeeService.updateEmployeeRole("1003", "SHIFT_MANAGER");
        assertTrue(updateResult, "Should successfully update employee role");

        // Verify role updated
        EmployeeDTO updatedEmployee = employeeService.getEmployeeDetails("1003");
        assertTrue(updatedEmployee.isShiftManager(), "Employee should now be Shift Manager");
        assertTrue(updatedEmployee.isManager(), "Employee should now be a manager");
        assertFalse(updatedEmployee.isHRManager(), "Employee should not be HR Manager");

        // Update to HR Manager
        assertTrue(employeeService.updateEmployeeRole("1003", "HR_MANAGER"), "Should update to HR Manager");
        updatedEmployee = employeeService.getEmployeeDetails("1003");
        assertTrue(updatedEmployee.isHRManager(), "Employee should now be HR Manager");

        // Revert to Regular Employee
        assertTrue(employeeService.updateEmployeeRole("1003", "REGULAR_EMPLOYEE"), "Should revert to regular");
        updatedEmployee = employeeService.getEmployeeDetails("1003");
        assertFalse(updatedEmployee.isManager(), "Employee should no longer be a manager");
    }

    @Test
    void testUpdateEmployeePassword() {
        // Update password
        boolean updateResult = employeeService.updateEmployeePassword("1001", "newPassword");
        assertTrue(updateResult, "Should successfully update password");

        // Note: Password verification logic is simplified in this implementation
        // In a real system, you'd have proper password hashing and verification
    }

    @Test
    void testAddNewEmployeeWithRole() {
        // Add employee with Shift Manager role - WITH BRANCH
        boolean addResult = employeeService.addManagerEmployee("1004", "Test", "Manager", "IL999888",
                LocalDate.now(), 40.0, "SHIFT_MANAGER", "pass123", 0, 0, "TestFund", testBranchAddress);
        assertTrue(addResult, "Should successfully add new manager employee");

        // Verify employee added correctly
        EmployeeDTO newEmployee = employeeService.getEmployeeDetails("1004");
        assertNotNull(newEmployee, "New employee should exist");
        assertEquals("Test Manager", newEmployee.getFullName());
        assertTrue(newEmployee.isShiftManager(), "New employee should be Shift Manager");
        assertTrue(newEmployee.hasBranch(), "New employee should have branch assignment");
        assertEquals(testBranchAddress, newEmployee.getBranchAddress(), "New employee should be in test branch");

        // Add regular employee - WITH BRANCH
        boolean addRegularResult = employeeService.addEmployee("1005", "Regular", "Employee", "IL777666",
                LocalDate.now(), 35.0, 0, 0, "TestFund", testBranchAddress);
        assertTrue(addRegularResult, "Should successfully add regular employee");

        EmployeeDTO regularEmployee = employeeService.getEmployeeDetails("1005");
        assertNotNull(regularEmployee, "Regular employee should exist");
        assertFalse(regularEmployee.isManager(), "Regular employee should not be manager");
        assertTrue(regularEmployee.hasBranch(), "Regular employee should have branch assignment");
        assertEquals(testBranchAddress, regularEmployee.getBranchAddress(), "Regular employee should be in test branch");
    }

    @Test
    void testAddEmployeeWithoutBranchFails() {
        // Test that adding employees without branch fails
        // Note: The old methods without branch are removed/deprecated
        // This test verifies the system enforces branch requirement

        // Get all employees before attempt
        int employeeCountBefore = employeeService.getAllEmployees().size();

        // Try to create employee with null branch - should fail
        boolean addResult = employeeService.addEmployee("1007", "Test", "Employee", "IL888999",
                LocalDate.now(), 30.0, 5, 10, "TestFund", null);
        assertFalse(addResult, "Should fail to add employee without branch");

        // Try to create employee with empty branch - should fail
        boolean addResult2 = employeeService.addEmployee("1008", "Test2", "Employee2", "IL777888",
                LocalDate.now(), 30.0, 5, 10, "TestFund", "");
        assertFalse(addResult2, "Should fail to add employee with empty branch");

        // Verify no employees were actually added
        int employeeCountAfter = employeeService.getAllEmployees().size();
        assertEquals(employeeCountBefore, employeeCountAfter, "No employees should have been added");
    }

    @Test
    void testGetAllEmployees() {
        List<EmployeeDTO> allEmployees = employeeService.getAllEmployees();

        // Should have admin + 3 test employees = 4 total
        assertTrue(allEmployees.size() >= 4, "Should have at least 4 employees including admin");

        // Check that our test employees are included
        boolean foundHR = allEmployees.stream().anyMatch(emp -> emp.getId().equals("1001"));
        boolean foundSM = allEmployees.stream().anyMatch(emp -> emp.getId().equals("1002"));
        boolean foundRegular = allEmployees.stream().anyMatch(emp -> emp.getId().equals("1003"));

        assertTrue(foundHR, "Should find HR Manager");
        assertTrue(foundSM, "Should find Shift Manager");
        assertTrue(foundRegular, "Should find Regular Employee");

        // Verify all employees (except possibly admin) have branches
        for (EmployeeDTO employee : allEmployees) {
            if (!employee.getId().equals("admin")) {
                assertTrue(employee.hasBranch(),
                        "Employee " + employee.getId() + " should have branch assignment");
            }
        }
    }

    @Test
    void testEmployeeQualifications() {
        // Test getting qualified employees for a position
        List<EmployeeDTO> shiftManagers = positionService.getQualifiedEmployeesForPosition("Shift Manager");
        assertEquals(2, shiftManagers.size(), "Should have 2 employees qualified for Shift Manager");

        List<EmployeeDTO> cashiers = positionService.getQualifiedEmployeesForPosition("Cashier");
        assertEquals(1, cashiers.size(), "Should have 1 employee qualified for Cashier");

        // Test employee qualifications
        EmployeeDTO employee1001 = employeeService.getEmployeeDetails("1001");
        assertTrue(employee1001.getQualifiedPositions().contains("Shift Manager"),
                "Employee 1001 should be qualified for Shift Manager");

        EmployeeDTO employee1003 = employeeService.getEmployeeDetails("1003");
        assertTrue(employee1003.getQualifiedPositions().contains("Cashier"),
                "Employee 1003 should be qualified for Cashier");
    }

    @Test
    void testEmployeeAvailability() {
        String employeeId = "1003";
        DayOfWeek testDay = DayOfWeek.MONDAY;

        // Test updating availability
        boolean updateResult = employeeService.updateEmployeeAvailability(
                employeeId, testDay, false, true);
        assertTrue(updateResult, "Should successfully update availability");

        // Test checking availability
        boolean morningAvailable = employeeService.isEmployeeAvailable(employeeId, testDay, "MORNING");
        boolean eveningAvailable = employeeService.isEmployeeAvailable(employeeId, testDay, "EVENING");

        assertFalse(morningAvailable, "Employee should not be available for morning");
        assertTrue(eveningAvailable, "Employee should be available for evening");

        // Test next week availability
        boolean nextWeekUpdateResult = employeeService.updateEmployeeAvailabilityForNextWeek(
                employeeId, testDay, true, false);
        assertTrue(nextWeekUpdateResult, "Should successfully update next week availability");
    }

    @Test
    void testPositionManagement() {
        // Test adding a new position
        boolean addResult = positionService.addPosition("Store Clerk", false);
        assertTrue(addResult, "Should successfully add new position");

        // Test getting position details
        PositionDTO position = positionService.getPosition("Store Clerk");
        assertNotNull(position, "Position should exist");
        assertEquals("Store Clerk", position.getName());
        assertFalse(position.isRequiresShiftManager(), "Position should not require shift manager");

        // Test getting all positions
        List<PositionDTO> allPositions = positionService.getAllPositions();
        assertTrue(allPositions.size() >= 3, "Should have at least 3 positions");

        boolean foundNewPosition = allPositions.stream()
                .anyMatch(pos -> pos.getName().equals("Store Clerk"));
        assertTrue(foundNewPosition, "Should find the new position in the list");
    }

    @Test
    void testShiftManagerValidation() {
        // Test that shift managers exist
        boolean hasShiftManagers = employeeService.hasShiftManagers();
        assertTrue(hasShiftManagers, "System should have shift managers");

        // Verify shift manager positions are properly set up
        PositionDTO shiftManagerPosition = positionService.getPosition("Shift Manager");
        assertNotNull(shiftManagerPosition, "Shift Manager position should exist");
        assertTrue(shiftManagerPosition.isRequiresShiftManager(),
                "Shift Manager position should require shift manager role");
    }

    @Test
    void testBranchFunctionality() {
        // Test branch service integration
        List<BranchDTO> branches = branchService.getAllBranches();

        assertNotNull(branches, "Branches list should not be null");
        assertFalse(branches.isEmpty(), "Should have at least one branch");

        // Test branch assignment
        String branchAddress = branches.get(0).getAddress();

        // Add employee with branch assignment
        boolean addResult = employeeService.addEmployee("1006", "Branch", "Employee", "IL123789",
                LocalDate.now(), 30.0, 5, 10, "TestFund", branchAddress);
        assertTrue(addResult, "Should successfully add employee with branch");

        EmployeeDTO branchEmployee = employeeService.getEmployeeDetails("1006");
        assertNotNull(branchEmployee, "Branch employee should exist");
        assertEquals(branchAddress, branchEmployee.getBranchAddress(),
                "Employee should be assigned to correct branch");
        assertTrue(branchEmployee.hasBranch(), "Employee should have branch assignment");

        // Test getting employees by branch
        List<EmployeeDTO> branchEmployees = employeeService.getEmployeesByBranch(branchAddress);
        assertNotNull(branchEmployees, "Branch employees list should not be null");
        assertTrue(branchEmployees.size() >= 4, "Should have at least 4 employees in test branch");

        // Verify all returned employees are from the correct branch
        for (EmployeeDTO emp : branchEmployees) {
            assertEquals(branchAddress, emp.getBranchAddress(),
                    "All employees should be from the specified branch");
        }
    }

    @Test
    void testBranchService() {
        // Test getting all branches
        List<BranchDTO> branches = branchService.getAllBranches();
        assertNotNull(branches, "Branches list should not be null");
        assertFalse(branches.isEmpty(), "Should have at least one branch");

        // Test getting specific branch
        String testAddress = branches.get(0).getAddress();
        BranchDTO specificBranch = branchService.getBranchByAddress(testAddress);
        assertNotNull(specificBranch, "Should find the specific branch");
        assertEquals(testAddress, specificBranch.getAddress(), "Branch address should match");

        // Test branch existence check
        assertTrue(branchService.branchExists(testAddress), "Branch should exist");
        assertFalse(branchService.branchExists("NonExistentBranch"), "Non-existent branch should not exist");
    }

    @Test
    void testEmployeeBranchUpdate() {
        // Get another branch for testing
        List<BranchDTO> branches = branchService.getAllBranches();
        if (branches.size() < 2) {
            // Skip test if only one branch available
            return;
        }

        String newBranchAddress = branches.get(1).getAddress();

        // Update employee branch
        boolean updateResult = employeeService.updateEmployeeBranch("1003", newBranchAddress);
        assertTrue(updateResult, "Should successfully update employee branch");

        // Verify branch updated
        EmployeeDTO updatedEmployee = employeeService.getEmployeeDetails("1003");
        assertEquals(newBranchAddress, updatedEmployee.getBranchAddress(),
                "Employee should be assigned to new branch");
    }

    @Test
    void testRemoveEmployee() {
        // Test removing an employee
        boolean removeResult = employeeService.removeEmployee("1003");
        assertTrue(removeResult, "Should successfully remove employee");

        // Verify employee is removed
        EmployeeDTO removedEmployee = employeeService.getEmployeeDetails("1003");
        assertNull(removedEmployee, "Removed employee should not exist");

        // Test removing non-existent employee
        boolean removeNonExistentResult = employeeService.removeEmployee("9999");
        assertFalse(removeNonExistentResult, "Should not remove non-existent employee");
    }

    @Test
    void testUpdateEmployeeDetails() {
        String employeeId = "1003";

        // Test updating first name
        assertTrue(employeeService.updateEmployeeFirstName(employeeId, "Robert"),
                "Should update first name");
        EmployeeDTO updated = employeeService.getEmployeeDetails(employeeId);
        assertEquals("Robert", updated.getFirstName(), "First name should be updated");

        // Test updating last name
        assertTrue(employeeService.updateEmployeeLastName(employeeId, "Smith"),
                "Should update last name");
        updated = employeeService.getEmployeeDetails(employeeId);
        assertEquals("Smith", updated.getLastName(), "Last name should be updated");

        // Test updating salary
        assertTrue(employeeService.updateEmployeeSalary(employeeId, 35.0),
                "Should update salary");
        updated = employeeService.getEmployeeDetails(employeeId);
        assertEquals(35.0, updated.getSalary(), "Salary should be updated");

        // Test updating sick days
        assertTrue(employeeService.updateEmployeeSickDays(employeeId, 10),
                "Should update sick days");
        updated = employeeService.getEmployeeDetails(employeeId);
        assertEquals(10, updated.getSickDays(), "Sick days should be updated");

        // Test updating vacation days
        assertTrue(employeeService.updateEmployeeVacationDays(employeeId, 15),
                "Should update vacation days");
        updated = employeeService.getEmployeeDetails(employeeId);
        assertEquals(15, updated.getVacationDays(), "Vacation days should be updated");
    }
}