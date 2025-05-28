package Domain_employee;

import Service_employee.EmployeeService;
import Service_employee.PositionService;
import Service_employee.ShiftService;
import Service_employee.BranchService;
import Service_employee.ShiftDTO;
import Service_employee.BranchDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller responsible for initializing the system with sample data.
 * Updated to work with the new DAO-based architecture.
 */
public class DataInitializationController {
    private final EmployeeService employeeService;
    private final PositionService positionService;
    private final ShiftService shiftService;
    private final BranchService branchService;

    public DataInitializationController() {
        this.employeeService = new EmployeeService();
        this.positionService = new PositionService();
        this.shiftService = new ShiftService();
        this.branchService = new BranchService();
    }

    /**
     * Initializes the system with sample data including employees, positions,
     * qualifications, availability settings, and shift requirements.
     *
     * @return true if initialization was successful, false otherwise
     */
    public boolean initializeWithSampleData() {
        try {
            // Check if branches exist (from delivery module)
            List<BranchDTO> branches = branchService.getAllBranches();
            if (branches.isEmpty()) {
                System.out.println("Warning: No branches found in the system. Employees will be created without branch assignment.");
            }

            // Get first available branch for sample data
            String sampleBranch = branches.isEmpty() ? null : branches.get(0).getAddress();

            // Add positions
            positionService.addPosition("Cashier", false);
            positionService.addPosition("Stocker", false);
            positionService.addPosition("Security", false);
            positionService.addPosition("Floor Manager", true);
            positionService.addPosition("Customer Service", false);

            // Set position requirements for shifts
            positionService.setRequiredPosition("MORNING", "Cashier", 2);
            positionService.setRequiredPosition("MORNING", "Stocker", 1);
            positionService.setRequiredPosition("MORNING", "Security", 1);
            positionService.setRequiredPosition("MORNING", "Floor Manager", 1);
            positionService.setRequiredPosition("MORNING", "Customer Service", 1);

            positionService.setRequiredPosition("EVENING", "Cashier", 3);
            positionService.setRequiredPosition("EVENING", "Stocker", 2);
            positionService.setRequiredPosition("EVENING", "Security", 1);
            positionService.setRequiredPosition("EVENING", "Floor Manager", 1);
            positionService.setRequiredPosition("EVENING", "Customer Service", 2);

            // Add regular employees with branch assignment
            LocalDate today = LocalDate.now();

            // Distribute employees across different branches if available
            String[] branchAssignments = getBranchAssignments(branches);

            employeeService.addEmployee("111", "Yossi", "Cohen", "12345",
                    today.minusYears(2), 40.0, 10, 20, "Menora", branchAssignments[0]);

            employeeService.addEmployee("222", "Sara", "Levi", "23456",
                    today.minusYears(1), 42.0, 12, 15, "Harel", branchAssignments[1]);

            employeeService.addEmployee("333", "David", "Israeli", "34567",
                    today.minusMonths(8), 38.0, 8, 10, "Migdal", branchAssignments[2]);

            employeeService.addEmployee("444", "Noa", "Golan", "45678",
                    today.minusMonths(6), 45.0, 14, 18, "Phoenix", branchAssignments[3]);

            employeeService.addEmployee("555", "Moshe", "Peretz", "56789",
                    today.minusMonths(4), 40.0, 7, 12, "Clal", branchAssignments[4]);

            // Add a shift manager with branch assignment
            employeeService.addManagerEmployee("666", "Rachel", "Mizrahi", "67890",
                    today.minusYears(3), 55.0, "SHIFT_MANAGER", "password666",
                    15, 20, "Menora", sampleBranch);

            // Add qualifications
            positionService.addQualificationToEmployee("111", "Cashier");
            positionService.addQualificationToEmployee("111", "Customer Service");

            positionService.addQualificationToEmployee("222", "Cashier");
            positionService.addQualificationToEmployee("222", "Stocker");

            positionService.addQualificationToEmployee("333", "Security");

            positionService.addQualificationToEmployee("444", "Customer Service");
            positionService.addQualificationToEmployee("444", "Cashier");

            positionService.addQualificationToEmployee("555", "Stocker");

            positionService.addQualificationToEmployee("666", "Floor Manager");

            // Set availability for all employees
            // By default all employees are available for all shifts
            // Let's set some specific unavailability to demonstrate the feature
            employeeService.updateEmployeeAvailability("111", DayOfWeek.FRIDAY, false, false);
            employeeService.updateEmployeeAvailability("222", DayOfWeek.SATURDAY, false, false);
            employeeService.updateEmployeeAvailability("333", DayOfWeek.SUNDAY, true, false);
            employeeService.updateEmployeeAvailability("444", DayOfWeek.MONDAY, false, true);

            // Create shifts for the upcoming week starting from the next Sunday
            LocalDate nextSunday = LocalDate.now();
            while (nextSunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
                nextSunday = nextSunday.plusDays(1);
            }

            // Create shifts for each branch if multiple branches exist
            if (branches.size() > 1) {
                for (BranchDTO branch : branches.subList(0, Math.min(2, branches.size()))) {
                    List<ShiftDTO> shifts = shiftService.createShiftsForWeek(nextSunday, branch.getAddress());
                    System.out.println("Created " + shifts.size() + " shifts for branch: " + branch.getAddress());
                }
            } else {
                List<ShiftDTO> shifts = shiftService.createShiftsForWeek(nextSunday, sampleBranch);
                System.out.println("Created " + shifts.size() + " shifts" +
                        (sampleBranch != null ? " for branch: " + sampleBranch : ""));
            }

            return true;
        } catch (Exception e) {
            System.out.println("Error initializing data: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Distributes branch assignments across employees
     */
    private String[] getBranchAssignments(List<BranchDTO> branches) {
        String[] assignments = new String[5]; // For 5 employees

        if (branches.isEmpty()) {
            // No branches available
            for (int i = 0; i < assignments.length; i++) {
                assignments[i] = null;
            }
        } else if (branches.size() == 1) {
            // Only one branch - assign all to it
            String branchAddress = branches.get(0).getAddress();
            for (int i = 0; i < assignments.length; i++) {
                assignments[i] = branchAddress;
            }
        } else {
            // Multiple branches - distribute employees
            for (int i = 0; i < assignments.length; i++) {
                assignments[i] = branches.get(i % branches.size()).getAddress();
            }
        }

        return assignments;
    }

    /**
     * Initialize with branch-specific sample data
     */
    public boolean initializeWithSampleDataForBranch(String branchAddress) {
        try {
            if (!branchService.branchExists(branchAddress)) {
                System.out.println("Branch does not exist: " + branchAddress);
                return false;
            }

            // Create a simplified initialization for a specific branch
            LocalDate today = LocalDate.now();

            // Add one employee for this branch
            employeeService.addEmployee("BR_" + branchAddress.replaceAll("\\s+", ""),
                    "Branch", "Employee", "12345", today, 40.0, 10, 15, "Default", branchAddress);

            // Create shifts for next week for this branch
            LocalDate nextSunday = LocalDate.now();
            while (nextSunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
                nextSunday = nextSunday.plusDays(1);
            }

            List<ShiftDTO> shifts = shiftService.createShiftsForWeek(nextSunday, branchAddress);
            System.out.println("Created " + shifts.size() + " shifts for branch: " + branchAddress);

            return true;
        } catch (Exception e) {
            System.out.println("Error initializing branch data: " + e.getMessage());
            return false;
        }
    }
}