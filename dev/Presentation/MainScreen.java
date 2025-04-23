

package Presentation;

import Service.EmployeeDTO;

//Main screen for the application.
/**
 * MainScreen serves as the primary navigation hub of the application.
 * It displays the main menu and routes the user to various functional screens
 * based on their selection.
 *
 * The screen adapts its display and available options based on the logged-in user's
 * role (HR Manager, Shift Manager, or Regular Employee).
 *
 * MainScreen works closely with the NavigationManager to handle transitions between
 * different functional areas of the application.
 */
public class MainScreen extends BaseScreen {
    private final NavigationManager navigationManager;
    private final EmployeeDTO loggedInEmployee;


    /**
     * Constructs a MainScreen with the specified navigation manager.
     * Retrieves the logged-in employee from the navigation manager.
     *
     * @param navigationManager The manager responsible for screen navigation
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
    public void display() { // main menu
        displayTitle("Super-Li Employee Management System");

        String employeeType = "Regular Employee";
        if (loggedInEmployee.isHRManager()) {
            employeeType = "HR Manager";
        } else if (loggedInEmployee.isShiftManager()) {
            employeeType = "Shift Manager";
        }

        displayMessage("Welcome, " + loggedInEmployee.getFullName() + " (" + employeeType + ")");

        String[] options = {
                "Employee Management",
                "Employee Availability",
                "Qualification Management",
                "Shift Scheduling",
                "Shift History",
                "View Future Shifts",
                "Logout"
        };
        int choice;
        do {
            choice = displayMenu("Main Menu", options);

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
        } while (choice != 0);
    }
}