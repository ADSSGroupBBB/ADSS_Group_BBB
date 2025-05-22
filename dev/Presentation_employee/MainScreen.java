package Presentation_employee;

import Controller_employee.DataInitializationController;
import Service_employee.EmployeeDTO;

/**
 * MainScreen serves as the primary navigation hub of the application.
 * It displays the main menu and routes the user to various functional screens
 * based on their selection.
 */
public class MainScreen extends BaseScreen {
    private final NavigationManager navigationManager;
    private final EmployeeDTO loggedInEmployee;
    private final DataInitializationController dataInitializationController;

    /**
     * Constructs a MainScreen with the specified navigation manager.
     * Retrieves the logged-in employee from the navigation manager.
     *
     * @param navigationManager The manager responsible for screen navigation
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
    public void display() { // main menu
        displayTitle("Super-Li Employee Management System");

        String employeeType = "Regular Employee";
        if (loggedInEmployee.isHRManager()) {
            employeeType = "HR Manager";
        } else if (loggedInEmployee.isShiftManager()) {
            employeeType = "Shift Manager";
        }

        displayMessage("Welcome, " + loggedInEmployee.getFullName() + " (" + employeeType + ")");

        String[] options;

        // Only managers (HR or shift managers) have access to data initialization
        if (loggedInEmployee.isManager()) {
            options = new String[]{
                    "Employee Management",
                    "Employee Availability",
                    "Qualification Management",
                    "Shift Scheduling",
                    "Shift History",
                    "View Future Shifts",
                    "Initialize Sample Data",
                    "Logout"
            };
        } else {
            options = new String[]{
                    "Employee Management",
                    "Employee Availability",
                    "Qualification Management",
                    "Shift Scheduling",
                    "Shift History",
                    "View Future Shifts",
                    "Logout"
            };
        }

        int choice;
        do {
            choice = displayMenu("Main Menu", options);

            if (loggedInEmployee.isManager()) {
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
                        initializeSampleData(); // Call to initialize sample data
                        break;
                    case 8:
                        navigationManager.logout();
                        displayMessage("Logged out successfully");
                        return;
                    case 0:
                        displayMessage("Exiting system...");
                        return;
                }
            } else {
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
                displayMessage("Added 5 regular employees, 1 shift manager, 5 positions, and weekly shift requirements.");
                displayMessage("You can now test the system with these sample data.");
            } else {
                displayError("Failed to initialize sample data. Please check the logs for details.");
            }
        }
    }
}