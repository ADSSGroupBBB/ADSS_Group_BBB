package Presentation_employee;

import Domain_employee.DataInitializationController;
import Service_employee.EmployeeDTO;

/**
 * Updated MainScreen with branch management support.
 * Serves as the primary navigation hub of the application.
 */
public class MainScreen extends BaseScreen {
    private final NavigationManager navigationManager;
    private final EmployeeDTO loggedInEmployee;
    private final DataInitializationController dataInitializationController;

    /**
     * Constructs a MainScreen with the specified navigation manager.
     * Retrieves the logged-in employee from the navigation manager.
     */
    public MainScreen(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
        this.loggedInEmployee = navigationManager.getLoggedInEmployee();
        this.dataInitializationController = new DataInitializationController();
    }

    /**
     * Displays the main menu of the application.
     * Shows appropriate welcome message based on the user's role and
     * provides options for navigating to different functional areas.
     */
    @Override
    public void display() {
        displayTitle("Super-Li Employee Management System");

        String employeeType = "Regular Employee";
        if (loggedInEmployee.isHRManager()) {
            employeeType = "HR Manager";
        } else if (loggedInEmployee.isShiftManager()) {
            employeeType = "Shift Manager";
        }

        String branchInfo = loggedInEmployee.hasBranch() ?
                " - Branch: " + loggedInEmployee.getBranchAddress() : " - No specific branch";

        displayMessage("Welcome, " + loggedInEmployee.getFullName() + " (" + employeeType + ")" + branchInfo);

        String[] options;

        // Different menu options based on user role
        if (loggedInEmployee.isHRManager()) {
            // HR Managers have access to all features including branch management
            options = new String[]{
                    "Employee Management",
                    "Employee Availability",
                    "Qualification Management",
                    "Shift Scheduling",
                    "Shift History",
                    "View Future Shifts",
                    "Branch Management",
                    "Initialize Sample Data",
                    "Logout"
            };
        } else if (loggedInEmployee.isShiftManager()) {
            // Shift Managers have access to most features but not employee management
            options = new String[]{
                    "Employee Availability",
                    "Qualification Management",
                    "Shift Scheduling",
                    "Shift History",
                    "View Future Shifts",
                    "Initialize Sample Data",
                    "Logout"
            };
        } else {
            // Regular employees have limited access
            options = new String[]{
                    "Employee Availability",
                    "Shift History",
                    "View Future Shifts",
                    "Logout"
            };
        }

        int choice;
        do {
            choice = displayMenu("Main Menu", options);

            if (loggedInEmployee.isHRManager()) {
                switch (choice) {
                    case 1:
                        navigationManager.showEmployeeManagement();
                        break;
                    case 2:
                        navigationManager.showEmployeeAvailability();
                        break;
                    case 3:
                        navigationManager.showQualificationManagement();
                        break;
                    case 4:
                        navigationManager.showShiftScheduling();
                        break;
                    case 5:
                        navigationManager.showShiftHistory();
                        break;
                    case 6:
                        navigationManager.showFutureShifts();
                        break;
                    case 7:
                        navigationManager.showBranchManagement();
                        break;
                    case 8:
                        initializeSampleData();
                        break;
                    case 9:
                        navigationManager.logout();
                        displayMessage("Logged out successfully");
                        return;
                    case 0:
                        displayMessage("Exiting system...");
                        return;
                }
            } else if (loggedInEmployee.isShiftManager()) {
                switch (choice) {
                    case 1:
                        navigationManager.showEmployeeAvailability();
                        break;
                    case 2:
                        navigationManager.showQualificationManagement();
                        break;
                    case 3:
                        navigationManager.showShiftScheduling();
                        break;
                    case 4:
                        navigationManager.showShiftHistory();
                        break;
                    case 5:
                        navigationManager.showFutureShifts();
                        break;
                    case 6:
                        initializeSampleData();
                        break;
                    case 7:
                        navigationManager.logout();
                        displayMessage("Logged out successfully");
                        return;
                    case 0:
                        displayMessage("Exiting system...");
                        return;
                }
            } else {
                // Regular employee menu
                switch (choice) {
                    case 1:
                        navigationManager.showEmployeeAvailability();
                        break;
                    case 2:
                        navigationManager.showShiftHistory();
                        break;
                    case 3:
                        navigationManager.showFutureShifts();
                        break;
                    case 4:
                        navigationManager.logout();
                        displayMessage("Logged out successfully");
                        return;
                    case 0:
                        displayMessage("Exiting system...");
                        return;
                }
            }
        } while (choice != 0);
    }

    /**
     * Initializes the system with sample data.
     * This option is only available to managers.
     */
    private void initializeSampleData() {
        displayTitle("Initialize Sample Data");

        if (getBooleanInput("Are you sure you want to add sample data to the system?")) {
            boolean success = dataInitializationController.initializeWithSampleData();

            if (success) {
                displayMessage("Sample data successfully loaded into the system.");
                displayMessage("Added employees, positions, shifts, and branch assignments.");
                displayMessage("Note: Sample data includes employees assigned to available branches.");
                displayMessage("You can now test the system with this sample data.");
            } else {
                displayError("Failed to initialize sample data. Please check the logs for details.");
            }
        }
    }
}