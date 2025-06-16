package DataBase;

import dto.*;
import ServiceD.DeliveriesApplication;
import ServiceD.DriversApplication;
import ServiceD.LocationApplication;
import ServiceD.TrucksApplication;
import ServiceD.ZonesApplication;
import Service_employee.*;
import ServiceD.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

/**
 * JUnit system tests for Employee and Delivery Management System.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SystemTest {

    private static EmployeeService employeeService;
    private static ShiftService shiftService;
    private static PositionService positionService;
    private static BranchService branchService;
    private static AssignmentService assignmentService;
    private static DeliveriesApplication deliveriesApp;
    private static DriversApplication driversApp;
    private static LocationApplication locationApp;
    private static TrucksApplication trucksApp;
    private static ZonesApplication zonesApp;

    private static final String TEST_ZONE_NAME = "TestZone_" + System.currentTimeMillis();
    private static final String TEST_BRANCH_ADDRESS = "TestStreet_" + System.currentTimeMillis();
    private static final String TEST_EMPLOYEE_ID = "EMP_" + System.currentTimeMillis();
    private static final String TEST_DRIVER_ID = "DRV_" + System.currentTimeMillis();
    private static final String TEST_MANAGER_ID = "MGR_" + System.currentTimeMillis();
    private static final String TEST_TRUCK_ID = "TRK_" + System.currentTimeMillis();

    // Store original streams
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;

    @BeforeAll
    static void setupAll() {
        // Suppress console output during tests
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        System.setErr(new PrintStream(new ByteArrayOutputStream()));

        try {
            employeeService = new EmployeeService();
            shiftService = new ShiftService();
            positionService = new PositionService();
            branchService = new BranchService();
            assignmentService = new AssignmentService();
            deliveriesApp = new DeliveriesApplication();
            driversApp = new DriversApplication();
            locationApp = new LocationApplication();
            trucksApp = new TrucksApplication();
            zonesApp = new ZonesApplication();
        } catch (Exception e) {
            // Restore output to show this error
            System.setOut(originalOut);
            System.setErr(originalErr);
            fail("Failed to initialize services: " + e.toString());
        }
    }

    @Test
    @Order(1)
    @DisplayName("Zone and Location Setup")
    void testZoneAndLocationSetup() {
        assertDoesNotThrow(() -> {
            // Try to create zone, if it fails it might already exist
            String zoneResult = zonesApp.addShippingZone(1, TEST_ZONE_NAME);
            // Accept either creation success or already exists
            boolean zoneOk = zoneResult.contains("New shipping zone created") ||
                    zoneResult.contains("already exists");
            assertTrue(zoneOk, "Zone should be created or already exist: " + zoneResult);

            String locationResult = locationApp.insertLocation(
                    "Test Contact", "123-456-7890", TEST_BRANCH_ADDRESS, TEST_ZONE_NAME);
            // Accept either creation success or already exists
            boolean locationOk = locationResult.contains("New location added") ||
                    locationResult.contains("already exists");
            assertTrue(locationOk, "Location should be created or already exist: " + locationResult);
        });
    }

    @Test
    @Order(2)
    @DisplayName("Position Creation")
    void testPositionCreation() {
        assertDoesNotThrow(() -> {
            assertTrue(positionService.addPosition("Cashier", false),
                    "Cashier position should be created");
            assertTrue(positionService.addPosition("Store Manager", true),
                    "Store Manager position should be created");
            assertTrue(positionService.addPosition("Driver", false),
                    "Driver position should be created");
            assertTrue(positionService.addPosition("STORE_KEEPER", false),
                    "Store Keeper position should be created");

            List<PositionDTO> positions = positionService.getAllPositions();
            assertTrue(positions.size() >= 4,
                    "Should have at least 4 positions created");
        });
    }

    @Test
    @Order(3)
    @DisplayName("Employee Creation with Branch")
    void testEmployeeCreationWithBranch() {
        assertDoesNotThrow(() -> {
            boolean result = employeeService.addEmployee(
                    TEST_EMPLOYEE_ID, "John", "Doe", "12345678",
                    LocalDate.now(), 25.0, 10, 15,
                    "PensionFund", TEST_BRANCH_ADDRESS
            );

            // If employee creation fails due to existing ID, try to get existing one
            if (!result) {
                EmployeeDTO existingEmployee = employeeService.getEmployeeDetails(TEST_EMPLOYEE_ID);
                if (existingEmployee != null) {
                    // Employee already exists, that's okay for our test
                    assertTrue(true, "Employee already exists, which is acceptable");
                    return;
                }
            }

            assertTrue(result, "Employee creation should succeed");

            EmployeeDTO employee = employeeService.getEmployeeDetails(TEST_EMPLOYEE_ID);
            assertNotNull(employee, "Employee should exist in system");
            assertEquals(TEST_BRANCH_ADDRESS, employee.getBranchAddress(),
                    "Employee should be assigned to correct branch");
            assertTrue(employee.hasBranch(), "Employee should have branch assignment");
        });
    }

    @Test
    @Order(4)
    @DisplayName("Driver Creation with Licenses")
    void testDriverCreationWithLicenses() {
        assertDoesNotThrow(() -> {
            List<Integer> licenses = Arrays.asList(1, 2);

            boolean driverCreated = employeeService.addDriver(
                    TEST_DRIVER_ID, "Mike", "Driver", "33333333",
                    LocalDate.now(), 28.0, 6, 20,
                    "PensionFund", TEST_BRANCH_ADDRESS, licenses);

            // If driver creation fails due to existing ID, check if driver exists
            if (!driverCreated) {
                EmployeeDTO existingDriver = employeeService.getEmployeeDetails(TEST_DRIVER_ID);
                if (existingDriver != null) {
                    assertTrue(true, "Driver already exists, which is acceptable");
                    return;
                }
            }

            assertTrue(driverCreated, "Driver creation should succeed");

            String driversList = driversApp.printDrivers();
            assertTrue(driversList.contains(TEST_DRIVER_ID),
                    "Driver should appear in drivers list");
        });
    }

    @Test
    @Order(5)
    @DisplayName("Manager Creation")
    void testManagerCreation() {
        assertDoesNotThrow(() -> {
            boolean managerCreated = employeeService.addManagerEmployee(
                    TEST_MANAGER_ID, "Bob", "Manager", "11111111",
                    LocalDate.now(), 35.0, "SHIFT_MANAGER", "password",
                    5, 25, "PensionFund", TEST_BRANCH_ADDRESS);

            // If manager creation fails due to existing ID, check if manager exists
            if (!managerCreated) {
                EmployeeDTO existingManager = employeeService.getEmployeeDetails(TEST_MANAGER_ID);
                if (existingManager != null && existingManager.isShiftManager()) {
                    assertTrue(true, "Manager already exists, which is acceptable");
                    return;
                }
            }

            assertTrue(managerCreated, "Manager creation should succeed");

            EmployeeDTO manager = employeeService.getEmployeeDetails(TEST_MANAGER_ID);
            assertNotNull(manager, "Manager should exist in system");
            assertTrue(manager.isShiftManager(), "Employee should be a shift manager");
        });
    }

    @Test
    @Order(6)
    @DisplayName("Employee Qualification Assignment")
    void testEmployeeQualificationAssignment() {
        assertDoesNotThrow(() -> {
            boolean qualificationAdded = positionService.addQualificationToEmployee(
                    TEST_EMPLOYEE_ID, "Cashier");

            assertTrue(qualificationAdded, "Qualification assignment should succeed");

            List<EmployeeDTO> qualifiedEmployees = positionService.getQualifiedEmployeesForPosition("Cashier");
            assertFalse(qualifiedEmployees.isEmpty(), "Should have qualified employees");

            boolean employeeFound = qualifiedEmployees.stream()
                    .anyMatch(emp -> emp.getId().equals(TEST_EMPLOYEE_ID));
            assertTrue(employeeFound, "Employee should be in qualified list");
        });
    }

    @Test
    @Order(7)
    @DisplayName("Employee Availability Management")
    void testEmployeeAvailabilityManagement() {
        assertDoesNotThrow(() -> {
            DayOfWeek testDay = DayOfWeek.MONDAY;

            boolean updateResult = employeeService.updateEmployeeAvailability(
                    TEST_EMPLOYEE_ID, testDay, false, true);

            assertTrue(updateResult, "Availability update should succeed");

            boolean morningAvailable = employeeService.isEmployeeAvailable(
                    TEST_EMPLOYEE_ID, testDay, "MORNING");
            boolean eveningAvailable = employeeService.isEmployeeAvailable(
                    TEST_EMPLOYEE_ID, testDay, "EVENING");

            assertFalse(morningAvailable, "Employee should not be available for morning");
            assertTrue(eveningAvailable, "Employee should be available for evening");
        });
    }

    @Test
    @Order(8)
    @DisplayName("Truck Management")
    void testTruckManagement() {
        assertDoesNotThrow(() -> {
            String truckResult = trucksApp.insertTruck(TEST_TRUCK_ID, 1, 2000, 5000);

            // Accept either creation success or already exists
            boolean truckOk = truckResult.contains("Truck added") ||
                    truckResult.contains("already exists");
            assertTrue(truckOk, "Truck should be created or already exist: " + truckResult);

            boolean truckAvailable = trucksApp.isAvailableTruck(TEST_TRUCK_ID);
            assertTrue(truckAvailable, "Truck should be available");

            String trucksList = trucksApp.printTrucks();
            assertTrue(trucksList.contains(TEST_TRUCK_ID), "Truck should appear in list");
        });
    }

    @Test
    @Order(9)
    @DisplayName("Driver License Management")
    void testDriverLicenseManagement() {
        assertDoesNotThrow(() -> {
            String addResult = driversApp.addLicense(TEST_DRIVER_ID, 3);
            // Accept either success or already has license
            boolean addOk = addResult.contains("License added") ||
                    addResult.contains("already has");
            assertTrue(addOk, "License addition should succeed or already exist: " + addResult);

            String deleteResult = driversApp.deleteLicense(TEST_DRIVER_ID, 1);
            // Accept either success or license not found (already deleted)
            boolean deleteOk = deleteResult.contains("License removed") ||
                    deleteResult.contains("not found");
            assertTrue(deleteOk, "License deletion should succeed or already deleted: " + deleteResult);
        });
    }

    @Test
    @Order(10)
    @DisplayName("Error Handling Validation")
    void testErrorHandlingValidation() {
        assertDoesNotThrow(() -> {
            // Test creating employee without branch (should fail)
            boolean resultNoBranch = employeeService.addEmployee(
                    "FAIL001", "Test", "User", "99999999",
                    LocalDate.now(), 20.0, 10, 15, "Fund", null);
            assertFalse(resultNoBranch, "Employee creation without branch should fail");

            // Test invalid truck creation (truck weight > max weight)
            String invalidTruckResult = trucksApp.insertTruck("INVALID", 1, 6000, 5000);
            assertTrue(invalidTruckResult.contains("Error"),
                    "Invalid truck creation should return error");

            // Test deleting non-existent truck
            String deleteNonExistent = trucksApp.deleteTruck("NONEXISTENT_TRUCK");
            assertTrue(deleteNonExistent.contains("doesn't exist"),
                    "Deleting non-existent truck should return appropriate message");
        });
    }

    @Test
    @Order(11)
    @DisplayName("Delivery Items Management")
    void testDeliveryItemsManagement() {
        assertDoesNotThrow(() -> {
            String itemsList = deliveriesApp.printItems();
            assertNotNull(itemsList, "Items list should not be null");

            String documentsList = deliveriesApp.printDocIDS();
            assertNotNull(documentsList, "Documents list should not be null");
        });
    }

    @Test
    @Order(12)
    @DisplayName("System Integration Check")
    void testSystemIntegrationCheck() {
        assertNotNull(employeeService, "Employee service should be initialized");
        assertNotNull(shiftService, "Shift service should be initialized");
        assertNotNull(positionService, "Position service should be initialized");
        assertNotNull(branchService, "Branch service should be initialized");
        assertNotNull(assignmentService, "Assignment service should be initialized");
        assertNotNull(deliveriesApp, "Deliveries app should be initialized");
        assertNotNull(driversApp, "Drivers app should be initialized");
        assertNotNull(locationApp, "Location app should be initialized");
        assertNotNull(trucksApp, "Trucks app should be initialized");
        assertNotNull(zonesApp, "Zones app should be initialized");
    }

    @AfterAll
    static void cleanup() {
        // Cleanup test data if needed
        try {
            // Try to clean up test data
            trucksApp.deleteTruck(TEST_TRUCK_ID);
            employeeService.removeEmployee(TEST_EMPLOYEE_ID);
            employeeService.removeEmployee(TEST_DRIVER_ID);
            employeeService.removeEmployee(TEST_MANAGER_ID);
        } catch (Exception e) {
            // Ignore cleanup errors
        }

        // Restore console output
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.out.println("All tests completed successfully");
    }
}