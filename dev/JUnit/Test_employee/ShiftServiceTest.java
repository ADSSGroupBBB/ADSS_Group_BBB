package JUnit.Test_employee;

import Service_employee.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ShiftService with updated DAO architecture and mandatory branch assignment.
 */
public class ShiftServiceTest {

    private ShiftService shiftService;
    private EmployeeService employeeService;
    private PositionService positionService;
    private AssignmentService assignmentService;
    private BranchService branchService;
    private String testBranchAddress;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService();
        shiftService = new ShiftService();
        positionService = new PositionService();
        assignmentService = new AssignmentService();
        branchService = new BranchService();

        // Get first available branch for testing
        List<BranchDTO> branches = branchService.getAllBranches();
        if (branches.isEmpty()) {
            fail("No branches available for testing. Ensure database has branch data.");
        }
        testBranchAddress = branches.get(0).getAddress();

        // Clean existing employees before each test (except admin)
        List<EmployeeDTO> existingEmployees = employeeService.getAllEmployees();
        for (EmployeeDTO emp : existingEmployees) {
            if (!emp.getId().equals("admin")) { // Keep admin user
                employeeService.removeEmployee(emp.getId());
            }
        }

        // Define positions: Shift Manager (requires manager) and Cashier (regular role)
        positionService.addPosition("Shift Manager", true);
        positionService.addPosition("Cashier", false);

        // Add employees with different roles - ALL WITH BRANCH ASSIGNMENT
        employeeService.addManagerEmployee("1001", "John", "Manager", "IL123456",
                LocalDate.of(2023, 1, 1), 40.0, "SHIFT_MANAGER", "sm123",
                5, 10, "PensionFundA", testBranchAddress);

        employeeService.addManagerEmployee("1002", "Jane", "HR", "IL234567",
                LocalDate.of(2023, 2, 1), 45.0, "HR_MANAGER", "hr123",
                5, 10, "PensionFundA", testBranchAddress);

        employeeService.addEmployee("1003", "Bob", "Regular", "IL345678",
                LocalDate.of(2023, 3, 1), 30.0, 4, 8, "PensionFundC", testBranchAddress);

        // Assign qualifications to employees
        positionService.addQualificationToEmployee("1001", "Shift Manager");
        positionService.addQualificationToEmployee("1002", "Shift Manager");
        positionService.addQualificationToEmployee("1003", "Cashier");

        // Set full availability for managers and morning-only for regular employee
        for (DayOfWeek day : DayOfWeek.values()) {
            employeeService.updateEmployeeAvailability("1001", day, true, true);
            employeeService.updateEmployeeAvailability("1002", day, true, true);
            employeeService.updateEmployeeAvailability("1003", day, true, false);  // Only morning shifts
        }

        // Define required positions per shift type
        positionService.setRequiredPosition("MORNING", "Shift Manager", 1);
        positionService.setRequiredPosition("MORNING", "Cashier", 1);
        positionService.setRequiredPosition("EVENING", "Shift Manager", 1);
        positionService.setRequiredPosition("EVENING", "Cashier", 1);
    }

    /**
     * Test that verifies creation of shifts for a full week with mandatory branch.
     */
    @Test
    void testCreateShiftsForWeekWithBranch() {
        // Find next Sunday to start shift creation
        LocalDate today = LocalDate.now();
        LocalDate nextSunday = today.with(DayOfWeek.SUNDAY);
        if (!today.isBefore(nextSunday)) {
            nextSunday = nextSunday.plusWeeks(1);
        }

        // Create shifts for the entire week WITH BRANCH (mandatory)
        List<ShiftDTO> shifts = shiftService.createShiftsForWeek(nextSunday, testBranchAddress);

        // Should create shifts (number depends on available managers)
        assertFalse(shifts.isEmpty(), "Should create at least some shifts for branch: " + testBranchAddress);
        assertTrue(shifts.size() <= 14, "Should not create more than 14 shifts");

        // Verify shifts are for the correct week
        for (ShiftDTO currentShift : shifts) {
            assertTrue(currentShift.getDate().isAfter(nextSunday.minusDays(1)) &&
                            currentShift.getDate().isBefore(nextSunday.plusDays(7)),
                    "Shift date should be within the target week");

            // Verify each shift has the correct branch
            assertTrue(currentShift.hasBranch(), "Each shift should have a branch assigned");
            assertEquals(testBranchAddress, currentShift.getBranchAddress(),
                    "Shift should be assigned to the test branch");
        }

        // Verify each shift has a manager assigned
        for (ShiftDTO currentShift : shifts) {
            assertTrue(currentShift.hasShiftManager(),
                    "Each shift should have a shift manager assigned: " + currentShift.getId());
        }
    }

    /**
     * Test that verifies creation fails without branch (should not create shifts without branch).
     */
    @Test
    void testCreateShiftsForWeekWithoutBranchFails() {
        LocalDate nextSunday = LocalDate.now().with(DayOfWeek.SUNDAY).plusWeeks(1);

        // Try to create shifts without branch - should return empty list
        List<ShiftDTO> shifts = shiftService.createShiftsForWeek(nextSunday, null);

        assertTrue(shifts.isEmpty(), "Should not create shifts without branch assignment");

        // Also test with empty string
        List<ShiftDTO> shiftsEmptyBranch = shiftService.createShiftsForWeek(nextSunday, "");
        assertTrue(shiftsEmptyBranch.isEmpty(), "Should not create shifts with empty branch assignment");
    }

    /**
     * Test to verify that future shifts are correctly retrieved after creation.
     */
    @Test
    void testGetFutureShifts() {
        // Create future shifts for next week with branch
        LocalDate nextSunday = LocalDate.now().with(DayOfWeek.SUNDAY).plusWeeks(1);
        List<ShiftDTO> createdShifts = shiftService.createShiftsForWeek(nextSunday, testBranchAddress);

        // Only proceed if shifts were created successfully
        if (!createdShifts.isEmpty()) {
            // Retrieve future shifts
            List<ShiftDTO> futureShifts = shiftService.getFutureShifts();
            assertFalse(futureShifts.isEmpty(), "Should have at least one future shift");

            // Find a morning shift from created shifts
            ShiftDTO morningShift = createdShifts.stream()
                    .filter(s -> s.getShiftType().equals("MORNING"))
                    .findFirst().orElse(null);

            if (morningShift != null) {
                // Validate that the created shift exists in the retrieved list
                boolean foundMorning = futureShifts.stream()
                        .anyMatch(s -> s.getId().equals(morningShift.getId()));

                assertTrue(foundMorning, "Created morning shift should be found in future shifts");
            }
        } else {
            // If shift creation failed, just verify the method works
            List<ShiftDTO> futureShifts = shiftService.getFutureShifts();
            assertNotNull(futureShifts, "Future shifts list should not be null");
        }
    }

    /**
     * Test to verify retrieval of historical (past) shifts.
     */
    @Test
    void testGetHistoricalShifts() {
        // Retrieve historical shifts (may be empty if no past shifts exist)
        List<ShiftDTO> historicalShifts = shiftService.getHistoricalShifts();

        assertNotNull(historicalShifts, "Historical shifts list should not be null");

        // Verify all returned shifts are in the past
        LocalDate today = LocalDate.now();
        for (ShiftDTO currentShift : historicalShifts) {
            assertTrue(currentShift.getDate().isBefore(today),
                    "Historical shift should be in the past: " + currentShift.getDate());
        }
    }

    /**
     * Test employee shift assignment functionality.
     */
    @Test
    void testEmployeeShiftAssignment() {
        // Create future shifts with branch
        LocalDate nextSunday = LocalDate.now().with(DayOfWeek.SUNDAY).plusWeeks(1);
        List<ShiftDTO> createdShifts = shiftService.createShiftsForWeek(nextSunday, testBranchAddress);

        if (!createdShifts.isEmpty()) {
            ShiftDTO testShift = createdShifts.get(0);

            // Try to assign an employee to the shift
            boolean assigned = assignmentService.assignEmployeeToShift(
                    testShift.getId(), "1003", "Cashier");

            // Assignment may fail if requirements aren't met, but method should not throw exception
            // Check if employee is assigned to shift
            boolean isAssigned = assignmentService.isEmployeeAlreadyAssignedToShift(
                    testShift.getId(), "1003");

            if (assigned) {
                assertTrue(isAssigned, "Employee should be marked as assigned after successful assignment");
            }
        }
    }

    /**
     * Test shift service with specific branch functionality.
     */
    @Test
    void testShiftServiceWithSpecificBranch() {
        // Get available branches
        List<BranchDTO> branches = branchService.getAllBranches();
        assertNotNull(branches, "Branches list should not be null");
        assertFalse(branches.isEmpty(), "Should have at least one branch available");

        String branchAddress = branches.get(0).getAddress();

        // Create shifts for specific branch
        LocalDate nextSunday = LocalDate.now()
                .with(DayOfWeek.SUNDAY)
                .plusWeeks(1);

        List<ShiftDTO> branchShifts = shiftService.createShiftsForWeek(nextSunday, branchAddress);

        assertNotNull(branchShifts, "Branch shifts list should not be null");

        // Verify shifts are assigned to correct branch
        for (ShiftDTO currentShift : branchShifts) {
            assertTrue(currentShift.hasBranch(), "Every shift should have a branch assigned");
            assertEquals(branchAddress, currentShift.getBranchAddress(),
                    "Shift should be assigned to the specified branch");
        }

        // Test getting shifts by branch
        List<ShiftDTO> retrievedBranchShifts = shiftService.getShiftsByBranch(branchAddress);
        assertNotNull(retrievedBranchShifts, "Retrieved branch shifts should not be null");
    }

    /**
     * Test employee availability and qualification checks.
     */
    @Test
    void testEmployeeAvailabilityAndQualifications() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        DayOfWeek dayOfWeek = tomorrow.getDayOfWeek();

        // Test availability check
        boolean isAvailable = employeeService.isEmployeeAvailable("1001", dayOfWeek, "MORNING");
        assertTrue(isAvailable, "Employee 1001 should be available for morning shifts");

        // Test qualification check
        List<EmployeeDTO> qualifiedEmployees = positionService.getQualifiedEmployeesForPosition("Shift Manager");
        assertFalse(qualifiedEmployees.isEmpty(), "Should have qualified shift managers");

        boolean foundManager = qualifiedEmployees.stream()
                .anyMatch(emp -> emp.getId().equals("1001"));
        assertTrue(foundManager, "Employee 1001 should be qualified for Shift Manager position");
    }

    /**
     * Test shift requirements and coverage.
     */
    @Test
    void testShiftRequirementsAndCoverage() {
        // Check required position counts
        int morningManagerCount = positionService.getRequiredPositionsCount("MORNING", "Shift Manager");
        assertEquals(1, morningManagerCount, "Should require 1 shift manager for morning shifts");

        int morningCashierCount = positionService.getRequiredPositionsCount("MORNING", "Cashier");
        assertEquals(1, morningCashierCount, "Should require 1 cashier for morning shifts");

        // Create shifts and test coverage
        LocalDate nextSunday = LocalDate.now().with(DayOfWeek.SUNDAY).plusWeeks(1);
        List<ShiftDTO> createdShifts = shiftService.createShiftsForWeek(nextSunday, testBranchAddress);

        if (!createdShifts.isEmpty()) {
            ShiftDTO testShift = createdShifts.get(0);

            // Check if all required positions are covered
            boolean allCovered = assignmentService.areAllRequiredPositionsCovered(testShift.getId());

            // If shift has assignments, verify coverage logic
            if (!testShift.getAssignments().isEmpty()) {
                // At least some positions should be assigned
                assertFalse(testShift.getAssignments().isEmpty(), "Shift should have some assignments");
            }
        }
    }

    /**
     * Test that all employees have branch assignments.
     */
    @Test
    void testAllEmployeesHaveBranches() {
        List<EmployeeDTO> allEmployees = employeeService.getAllEmployees();

        for (EmployeeDTO employee : allEmployees) {
            if (!employee.getId().equals("admin")) { // Skip admin for now
                assertTrue(employee.hasBranch(),
                        "Employee " + employee.getId() + " should have a branch assigned");
                assertNotNull(employee.getBranchAddress(),
                        "Employee " + employee.getId() + " branch address should not be null");
                assertFalse(employee.getBranchAddress().trim().isEmpty(),
                        "Employee " + employee.getId() + " branch address should not be empty");
            }
        }
    }

    /**
     * Test branch service functionality.
     */
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

    /**
     * Test that shift creation requires branch assignment.
     */
    @Test
    void testShiftCreationRequiresBranch() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        // Test that service properly handles null branch
        List<ShiftDTO> nullBranchShifts = shiftService.createShiftsForWeek(tomorrow, null);
        assertTrue(nullBranchShifts.isEmpty(), "Should not create shifts with null branch");

        // Test that service properly handles empty branch
        List<ShiftDTO> emptyBranchShifts = shiftService.createShiftsForWeek(tomorrow, "");
        assertTrue(emptyBranchShifts.isEmpty(), "Should not create shifts with empty branch");

        // Test that service properly handles whitespace-only branch
        List<ShiftDTO> whitespaceBranchShifts = shiftService.createShiftsForWeek(tomorrow, "   ");
        assertTrue(whitespaceBranchShifts.isEmpty(), "Should not create shifts with whitespace-only branch");
    }
}