package Presentation;

//Main screen for the application.
public class MainScreen extends BaseScreen {
    private final NavigationManager navigationManager;

    public MainScreen(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
    }

    @Override
    public void display() { // main menu
        displayTitle("Super-Li Employee Management System");
        displayMessage("Welcome to the Employee Management System");

        String[] options = {
                "Employee Management",
                "Employee Availability",
                "Qualification Management",
                "Shift Scheduling",
                "Shift History",
                "Exit System"
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
                    displayMessage("Exiting system...");
                    choice = 0; // Exit
                    break;
            }
        } while (choice != 0);
    }
}