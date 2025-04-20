package Presentation;

import Service.EmployeeService;
import Service.ShiftService;

/**
 * Navigation manager for the application.
 * Responsible for initializing and navigating between screens.
 */
public class NavigationManager {
    private final EmployeeService employeeService;
    private final ShiftService shiftService;


    public NavigationManager() {
        this.employeeService = new EmployeeService();
        this.shiftService = new ShiftService();
    }


    public void start() {
        // Start with main screen
        MainScreen mainScreen = new MainScreen(this);
        mainScreen.display();
    }


    public void showEmployeeManagement() {
        EmployeeManagementScreen screen = new EmployeeManagementScreen(employeeService);
        screen.display();
    }


    public void showEmployeeAvailability() {
        EmployeeAvailabilityScreen screen = new EmployeeAvailabilityScreen(employeeService);
        screen.display();
    }


    public void showQualificationManagement() {
        QualificationManagementScreen screen = new QualificationManagementScreen(employeeService);
        screen.display();
    }


    public void showShiftScheduling() {
        ShiftSchedulingScreen screen = new ShiftSchedulingScreen(employeeService, shiftService);
        screen.display();
    }


    public void showShiftHistory() {
        ShiftHistoryScreen screen = new ShiftHistoryScreen(employeeService, shiftService);
        screen.display();
    }


    public EmployeeService getEmployeeService() {
        return employeeService;
    }


    public ShiftService getShiftService() {
        return shiftService;
    }
}