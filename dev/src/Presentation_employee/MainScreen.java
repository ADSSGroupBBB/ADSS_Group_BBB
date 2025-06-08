package Presentation_employee;

import DTO.EmployeeDTO;
import java.sql.SQLException;
import static Presentation.DeliveriesManagerIO.presentingDeliveriesMenu;

/**
 * Updated MainScreen with branch management support and deliveries integration.
 * Serves as the primary navigation hub of the application.
 * Sample data initialization option removed - using permanent database data.
 */
public class MainScreen extends BaseScreen {
    private final NavigationManager navigationManager;
    private final EmployeeDTO loggedInEmployee;

    /**
     * Constructs a MainScreen with the specified navigation manager.
     * Retrieves the logged-in employee from the navigation manager.
     */
    public MainScreen(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
        this.loggedInEmployee = navigationManager.getLoggedInEmployee();
    }

    /**
     * Displays the main menu of the application.
     * Shows appropriate welcome message based on the user's role and
     * provides options for navigating to different functional areas.
     */
    @Override
    public void display() throws SQLException {
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
            // HR Managers have access to all features including branch management and deliveries
            options = new String[]{
                    "Employee Management",
                    "Employee Availability",
                    "Qualification Management",
                    "Shift Scheduling",
                    "Shift History",
                    "View Future Shifts",
                    "Branch Management",
                    "Deliveries Management",
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
                    "Deliveries Management",
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

                        try {
                            presentingDeliveriesMenu();
                        } catch (SQLException e) {
                            displayError("Database error: " + e.getMessage());
                        }
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

                        try {
                            presentingDeliveriesMenu();
                        } catch (SQLException e) {
                            displayError("Database error: " + e.getMessage());
                        }
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
}