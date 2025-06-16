package src.Presentation_employee;

import src.DTO.EmployeeDTO;
import src.Service_employee.PositionService;
import src.Service_employee.ShiftService;
import src.Service_employee.*;

import java.sql.SQLException;

/**
 * Updated Navigation manager for the application.
 * Now uses the new service architecture with DAO support.
 */
public class NavigationManager {
    private final EmployeeService employeeService;
    private final ShiftService shiftService;
    private final PositionService positionService;
    private final AssignmentService assignmentService;
    private final BranchService branchService;
    private LoginScreen loginScreen;
    private EmployeeDTO loggedInEmployee;

    /**
     * Constructs a NavigationManager instance.
     * Initializes the core services and the login screen.
     */
    public NavigationManager() {
        this.employeeService = new EmployeeService();
        this.shiftService = new ShiftService();
        this.positionService = new PositionService();
        this.assignmentService = new AssignmentService();
        this.branchService = new BranchService();
        this.loginScreen = new LoginScreen(this);
        this.loggedInEmployee = null;
    }

    /**
     * Starts the application by displaying the login screen.
     * If login is successful, proceeds to the main screen.
     */
    public void start() throws SQLException {
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
    public void showEmployeeManagement() throws SQLException {
        if (loggedInEmployee != null && loggedInEmployee.isHRManager()) {
            EmployeeManagementScreen screen = new EmployeeManagementScreen(this);
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
            EmployeeAvailabilityScreen screen = new EmployeeAvailabilityScreen(this, loggedInEmployee);
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
        if (loggedInEmployee != null && loggedInEmployee.isManager()) {
            QualificationManagementScreen screen = new QualificationManagementScreen(this, loggedInEmployee);
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
            ShiftSchedulingScreen screen = new ShiftSchedulingScreen(this, loggedInEmployee);
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
        ShiftViewScreen screen = new ShiftViewScreen(this, loggedInEmployee);
        screen.displayShiftHistory();
    }

    /**
     * Shows the future shifts screen.
     * Available to all users but with different views based on user role.
     */
    public void showFutureShifts() {
        ShiftViewScreen screen = new ShiftViewScreen(this, loggedInEmployee);
        screen.displayFutureShifts();
    }

    /**
     * Shows the branch management screen.
     * Available to HR managers only.
     */
    public void showBranchManagement() {
        if (loggedInEmployee != null && loggedInEmployee.isHRManager()) {
            BranchManagementScreen screen = new BranchManagementScreen(this);
            screen.display();
        } else {
            displayUnauthorizedMessage();
        }
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

    // Getters for services
    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public ShiftService getShiftService() {
        return shiftService;
    }

    public PositionService getPositionService() {
        return positionService;
    }

    public AssignmentService getAssignmentService() {
        return assignmentService;
    }

    public BranchService getBranchService() {
        return branchService;
    }

    public EmployeeDTO getLoggedInEmployee() {
        return loggedInEmployee;
    }

    /**
     * Logs out the current user and restarts the login process.
     * Clears the current user session and returns to the login screen.
     */
    public void logout() throws SQLException {
        loginScreen.logout();
        loggedInEmployee = null;
        start();
    }
}