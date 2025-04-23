//package Presentation;
//
////Main screen for the application.
//public class MainScreen extends BaseScreen {
//    private final NavigationManager navigationManager;
//
//    public MainScreen(NavigationManager navigationManager) {
//        this.navigationManager = navigationManager;
//    }
//
//    @Override
//    public void display() { // main menu
//        displayTitle("Super-Li Employee Management System");
//        displayMessage("Welcome to the Employee Management System");
//
//        String[] options = {
//                "Employee Management",
//                "Employee Availability",
//                "Qualification Management",
//                "Shift Scheduling",
//                "Shift History",
//                "Exit System"
//        };
//
//        int choice;
//        do {
//            choice = displayMenu("Main Menu", options);
//
//            switch (choice) {
//                case 1:
//                    navigationManager.showEmployeeManagement();
//                    break;
//                case 2:
//                    navigationManager.showEmployeeAvailability();
//                    break;
//                case 3:
//                    navigationManager.showQualificationManagement();
//                    break;
//                case 4:
//                    navigationManager.showShiftScheduling();
//                    break;
//                case 5:
//                    navigationManager.showShiftHistory();
//                    break;
//                case 6:
//                    displayMessage("Exiting system...");
//                    choice = 0; // Exit
//                    break;
//            }
//        } while (choice != 0);
//    }
//}

package Presentation;

import Service.EmployeeDTO;

//Main screen for the application.
public class MainScreen extends BaseScreen {
    private final NavigationManager navigationManager;
    private final EmployeeDTO loggedInEmployee;

    public MainScreen(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
        this.loggedInEmployee = navigationManager.getLoggedInEmployee();
    }

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
                    navigationManager.showShiftHistory();   // כאן רואים גם היסטוריה וגם משמרות עתידיות
                    break;
                case 6:
                    navigationManager.showFutureShifts();   // גם כאן כי זו אותה פונקציה
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