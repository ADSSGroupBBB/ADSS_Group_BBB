//package Presentation;
//
//import Service.EmployeeService;
//import Service.ShiftService;
//
///**
// * Navigation manager for the application.
// * Responsible for initializing and navigating between screens.
// */
//public class NavigationManager {
//    private final EmployeeService employeeService;
//    private final ShiftService shiftService;
//
//
//    public NavigationManager() {
//        this.employeeService = new EmployeeService();
//        this.shiftService = new ShiftService();
//    }
//
//
//    public void start() {
//        // Start with main screen
//        MainScreen mainScreen = new MainScreen(this);
//        mainScreen.display();
//    }
//
//
//    public void showEmployeeManagement() {
//        EmployeeManagementScreen screen = new EmployeeManagementScreen(employeeService);
//        screen.display();
//    }
//
//
//    public void showEmployeeAvailability() {
//        EmployeeAvailabilityScreen screen = new EmployeeAvailabilityScreen(employeeService);
//        screen.display();
//    }
//
//
//    public void showQualificationManagement() {
//        QualificationManagementScreen screen = new QualificationManagementScreen(employeeService);
//        screen.display();
//    }
//
//
//    public void showShiftScheduling() {
//        ShiftSchedulingScreen screen = new ShiftSchedulingScreen(employeeService, shiftService);
//        screen.display();
//    }
//
//
//    public void showShiftHistory() {
//        ShiftHistoryScreen screen = new ShiftHistoryScreen(employeeService, shiftService);
//        screen.display();
//    }
//
//
//    public EmployeeService getEmployeeService() {
//        return employeeService;
//    }
//
//
//    public ShiftService getShiftService() {
//        return shiftService;
//    }
//}

package Presentation;

import Service.EmployeeDTO;
import Service.EmployeeService;
import Service.ShiftService;

/**
 * Navigation manager for the application.
 * Responsible for initializing and navigating between screens.
 */
public class NavigationManager {
    private final EmployeeService employeeService;
    private final ShiftService shiftService;
    private LoginScreen loginScreen;
    private EmployeeDTO loggedInEmployee;

    public NavigationManager() {
        this.employeeService = new EmployeeService();
        this.shiftService = new ShiftService();
        this.loginScreen = new LoginScreen(employeeService);
        this.loggedInEmployee = null;
    }

    public void start() {
        // תחילה הצג את מסך ההתחברות
        loginScreen.display();

        // אם ההתחברות הצליחה, שמור את פרטי המשתמש ועבור למסך הראשי
        if (loginScreen.isLoggedIn()) {
            loggedInEmployee = loginScreen.getLoggedInEmployee();
            MainScreen mainScreen = new MainScreen(this);
            mainScreen.display();
        }
    }

    public void showEmployeeManagement() {
        // אופציה 1 - רק למנהל כח אדם
        if (loggedInEmployee != null && loggedInEmployee.isHRManager()) {
            EmployeeManagementScreen screen = new EmployeeManagementScreen(employeeService);
            screen.display();
        } else {
            displayUnauthorizedMessage();
        }
    }

    public void showEmployeeAvailability() {
        // אופציה 2 - לכולם (כולל עובדים רגילים)
        if (loggedInEmployee != null) {
            EmployeeAvailabilityScreen screen = new EmployeeAvailabilityScreen(employeeService, loggedInEmployee);
            screen.display();
        } else {
            displayUnauthorizedMessage();
        }
    }

    public void showQualificationManagement() {
        // אופציה 3 - רק למנהל משמרת ומנהל כח אדם
        if (loggedInEmployee != null && loggedInEmployee.isManager()) {
            QualificationManagementScreen screen = new QualificationManagementScreen(employeeService);
            screen.display();
        } else {
            displayUnauthorizedMessage();
        }
    }

    public void showShiftScheduling() {
        // אופציה 4 - רק למנהל משמרת ומנהל כח אדם
        if (loggedInEmployee != null && loggedInEmployee.isManager()) {
            ShiftSchedulingScreen screen = new ShiftSchedulingScreen(employeeService, shiftService);
            screen.display();
        } else {
            displayUnauthorizedMessage();
        }
    }

    public void showShiftHistory() {
        // אופציה 5 - כל עובד יכול לראות רק את שלו, מנהלים יכולים לראות את כולם
        if (loggedInEmployee != null) {
            ShiftHistoryScreen screen = new ShiftHistoryScreen(employeeService, shiftService, loggedInEmployee);
            screen.display();
        } else {
            displayUnauthorizedMessage();
        }
    }

    private void displayUnauthorizedMessage() {
        System.out.println("\n===== Access Denied =====");
        System.out.println("You do not have permission to access this functionality.");
        System.out.println("Please contact your manager if you need access.");
        // השהיה קצרה כדי שהמשתמש יוכל לקרוא את ההודעה
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // התעלם
        }
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public ShiftService getShiftService() {
        return shiftService;
    }

    public EmployeeDTO getLoggedInEmployee() {
        return loggedInEmployee;
    }

    public void logout() {
        loginScreen.logout();
        loggedInEmployee = null;
        // מפעיל מחדש את לולאת ההתחברות
        start();
    }
}