package Presentation_employee;

import Service_employee.EmployeeService;
import Service_employee.ShiftService;
import Service_employee.PositionService;
import Service_employee.AssignmentService;
import Service_employee.EmployeeDTO;

/**
 * Navigation manager for the application.
 * Responsible for initializing and navigating between screens.
 */
public class NavigationManager {
    private final EmployeeService employeeController;
    private final ShiftService shiftController;
    private final PositionService positionController;
    private final AssignmentService assignmentController;
    private LoginScreen loginScreen;
    private EmployeeDTO loggedInEmployee;

    /**
     * Constructs a NavigationManager instance.
     * Initializes the core controllers and the login screen.
     */
    public NavigationManager() {
        this.employeeController = new EmployeeService();
        this.shiftController = new ShiftService();
        this.positionController = new PositionService();
        this.assignmentController = new AssignmentService();
        this.loginScreen = new LoginScreen(this);
        this.loggedInEmployee = null;
    }

    /**
     * Starts the application by displaying the login screen.
     * If login is successful, proceeds to the main screen.
     * This method represents the main entry point for the UI flow.
     */
    public void start() {
        // First show the login screen
        loginScreen.display();
        // If login is successful, store user details and proceed to main screen
        if (loginScreen.isLoggedIn()) {
            loggedInEmployee = loginScreen.getLoggedInEmployee();
            MainScreen mainScreen = new MainScreen(this);
            mainScreen.display();
        }
    }

    /**
     * Shows the employee management screen.
     * Restricts access to HR managers only.
     */
    public void showEmployeeManagement() {
        // Option 1 - HR manager only
        if (loggedInEmployee != null && loggedInEmployee.isHRManager()) {
            EmployeeManagementScreen screen = new EmployeeManagementScreen(employeeController, this);
            screen.display();
        } else {
            displayUnauthorizedMessage();
        }
    }

    /**
     * Shows the employee availability screen.
     * Available to all users (including regular employees).
     */
    public void showEmployeeAvailability() {
        if (loggedInEmployee != null) {
            EmployeeAvailabilityScreen screen = new EmployeeAvailabilityScreen(employeeController, loggedInEmployee);
            screen.display();
        } else {
            displayUnauthorizedMessage();
        }
    }

    /**
     * Shows the qualification management screen.
     * Restricts access to managers only (both shift managers and HR managers).
     */
    public void showQualificationManagement() {
        // Option 3 - Managers only (shift managers and HR managers)
        if (loggedInEmployee != null && loggedInEmployee.isManager()) {
            QualificationManagementScreen screen = new QualificationManagementScreen(positionController, loggedInEmployee);
            screen.display();
        } else {
            displayUnauthorizedMessage();
        }
    }

    /**
     * Shows the shift scheduling screen.
     * Restricts access to managers only (both shift managers and HR managers).
     */
    public void showShiftScheduling() {
        if (loggedInEmployee != null && loggedInEmployee.isManager()) {
            ShiftSchedulingScreen screen = new ShiftSchedulingScreen(employeeController, shiftController,
                    assignmentController, loggedInEmployee);
            screen.display();
        } else {
            displayUnauthorizedMessage();
        }
    }

    /**
     * Shows the shift history screen.
     * Available to all users but with different views based on user role.
     */
    public void showShiftHistory() {
        ShiftViewScreen screen = new ShiftViewScreen(employeeController, shiftController, loggedInEmployee);
        screen.displayShiftHistory();
    }

    /**
     * Shows the future shifts screen.
     * Available to all users but with different views based on user role.
     */
    public void showFutureShifts() {
        ShiftViewScreen screen = new ShiftViewScreen(employeeController, shiftController, loggedInEmployee);
        screen.displayFutureShifts();
    }

    /**
     * Displays an error message when a user attempts to access a screen
     * without the required permissions.
     */
    private void displayUnauthorizedMessage() {
        System.out.println("\n===== Access Denied =====");
        System.out.println("You do not have permission to access this functionality.");
        System.out.println("Please contact your manager if you need access.");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Ignore interruption
        }
    }

    public EmployeeService getEmployeeController() {
        return employeeController;
    }

    public ShiftService getShiftController() {
        return shiftController;
    }

    public PositionService getPositionController() {
        return positionController;
    }

    public AssignmentService getAssignmentController() {
        return assignmentController;
    }

    public EmployeeDTO getLoggedInEmployee() {
        return loggedInEmployee;
    }

    /**
     * Logs out the current user and restarts the login process.
     * Clears the current user session and returns to the login screen.
     */
    public void logout() {
        loginScreen.logout();
        loggedInEmployee = null;
        start();
    }
}