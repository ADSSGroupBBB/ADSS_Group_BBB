//package Presentation;
//
//import Service.EmployeeDTO;
//import Service.EmployeeService;
//import Service.PositionDTO;
//import java.util.List;
//
//
//public class QualificationManagementScreen extends BaseScreen {
//    private final EmployeeService employeeService;
//
//    public QualificationManagementScreen(EmployeeService employeeService) {
//        this.employeeService = employeeService;
//    }
//
//    @Override
//    public void display() {
//        String[] options = {
//                "Add New Position",
//                "View All Positions",
//                "Add Qualification to Employee",
//                "View Qualified Employees for Position",
//                "Update Required Positions for Shifts"
//        };
//
//        int choice;
//        do {
//            choice = displayMenu("Qualification Management", options);
//
//            switch (choice) {
//                case 1:
//                    addNewPosition();
//                    break;
//                case 2:
//                    displayAllPositions();
//                    break;
//                case 3:
//                    addEmployeeQualification();
//                    break;
//                case 4:
//                    displayQualifiedEmployees();
//                    break;
//                case 5:
//                    updateRequiredPositions();
//                    break;
//                case 0:
//                    // Return to previous menu
//                    break;
//            }
//        } while (choice != 0);
//    }
//
//    private void addNewPosition() {
//        displayTitle("Add New Position");
//
//        String name = getInput("Enter Position Name");
//
//        // Check if position already exists
//        if (employeeService.getPositionDetails(name) != null) {
//            displayError("Position with this name already exists");
//            return;
//        }
//
//        boolean isShiftManagerRole = getBooleanInput("Is this a shift manager position?");
//
//        // Add position to the system
//        if (employeeService.addPosition(name, isShiftManagerRole)) {
//            displayMessage("Position added successfully!");
//        } else {
//            displayError("Error adding position");
//        }
//    }
//
//
//    private void displayAllPositions() {
//        displayTitle("All Positions");
//
//        List<PositionDTO> positions = employeeService.getAllPositions();
//
//        if (positions.isEmpty()) {
//            displayMessage("No positions defined in the system");
//            return;
//        }
//
//        for (PositionDTO position : positions) {
//            String isManager = position.isRequiresShiftManager() ? " (Shift Manager Position)" : "";
//            displayMessage(position.getName() + isManager);
//        }
//    }
//
//    private void addEmployeeQualification() {
//        displayTitle("Add Qualification to Employee");
//
//        // Select employee
//        EmployeeDTO employee = selectEmployee();
//        if (employee == null) {
//            return;
//        }
//
//        // Display current qualifications
//        displayMessage("Current qualifications for " + employee.getFullName() + ":");
//        if (employee.getQualifiedPositions().isEmpty()) {
//            displayMessage("None");
//        } else {
//            for (String position : employee.getQualifiedPositions()) {
//                displayMessage("- " + position);
//            }
//        }
//
//        // Select position
//        PositionDTO position = selectPosition();
//        if (position == null) {
//            return;
//        }
//
//        // Check if already qualified
//        if (employee.getQualifiedPositions().contains(position.getName())) {
//            displayError("Employee is already qualified for this position");
//            return;
//        }
//
//        // Add qualification
//        boolean success = employeeService.addQualificationToEmployee(employee.getId(), position.getName());
//
//        if (success) {
//            displayMessage("Qualification added successfully");
//        } else {
//            displayError("Error adding qualification");
//        }
//    }
//
//
//    private void displayQualifiedEmployees() {
//        displayTitle("Qualified Employees for Position");
//
//        // Select position
//        PositionDTO position = selectPosition();
//        if (position == null) {
//            return;
//        }
//
//        // Get qualified employees
//        List<EmployeeDTO> qualifiedEmployees = employeeService.getQualifiedEmployeesForPosition(position.getName());
//
//        if (qualifiedEmployees.isEmpty()) {
//            displayMessage("No employees are qualified for position: " + position.getName());
//            return;
//        }
//
//        displayMessage("Employees qualified for position: " + position.getName());
//        for (EmployeeDTO employee : qualifiedEmployees) {
//            displayMessage("- " + employee.getFullName() + " (ID: " + employee.getId() + ")");
//        }
//    }
//
//    private void updateRequiredPositions() {
//        displayTitle("Update Required Positions for Shifts");
//
//        // Select position
//        PositionDTO position = selectPosition();
//        if (position == null) {
//            return;
//        }
//
//        updateRequiredPositionsForPosition(position);
//    }
//
//
//    private void updateRequiredPositionsForPosition(PositionDTO position) {
//        displayTitle("Update Shift Requirements for Position: " + position.getName());
//
//        // Update for morning shift
//        int morningCount = getIntInput("Number of employees required for morning shift");
//
//        if (morningCount >= 0) {
//            boolean success = employeeService.addRequiredPosition("MORNING", position.getName(), morningCount);
//
//            if (success) {
//                displayMessage("Morning shift requirements updated successfully");
//            } else {
//                displayError("Error updating morning shift requirements");
//            }
//        }
//
//        // Update for evening shift
//        int eveningCount = getIntInput("Number of employees required for evening shift");
//
//        if (eveningCount >= 0) {
//            boolean success = employeeService.addRequiredPosition("EVENING", position.getName(), eveningCount);
//
//            if (success) {
//                displayMessage("Evening shift requirements updated successfully");
//            } else {
//                displayError("Error updating evening shift requirements");
//            }
//        }
//    }
//
//    private EmployeeDTO selectEmployee() {
//        List<EmployeeDTO> employees = employeeService.getAllEmployees();
//        if (employees.isEmpty()) {
//            displayError("No employees in the system");
//            return null;
//        }
//
//        // Build array of names for display in menu
//        String[] employeeNames = new String[employees.size()];
//        for (int i = 0; i < employees.size(); i++) {
//            EmployeeDTO emp = employees.get(i);
//            employeeNames[i] = emp.getFullName() + " (ID: " + emp.getId() + ")";
//        }
//
//        int choice = displayMenu("Select Employee", employeeNames);
//        if (choice == 0) {
//            return null; // User chose to go back
//        }
//
//        return employees.get(choice - 1);
//    }
//
//    private PositionDTO selectPosition() {
//        List<PositionDTO> positions = employeeService.getAllPositions();
//
//        if (positions.isEmpty()) {
//            displayError("No positions defined in the system");
//            return null;
//        }
//
//        // Build array of names for display in menu
//        String[] positionNames = new String[positions.size()];
//        for (int i = 0; i < positions.size(); i++) {
//            PositionDTO pos = positions.get(i);
//            String isManager = pos.isRequiresShiftManager() ? " (Shift Manager)" : "";
//            positionNames[i] = pos.getName() + isManager;
//        }
//
//        int choice = displayMenu("Select Position", positionNames);
//        if (choice == 0) {
//            return null; // User chose to go back
//        }
//
//        return positions.get(choice - 1);
//    }
//}


package Presentation;

import Service.EmployeeDTO;
import Service.EmployeeService;
import Service.PositionDTO;
import java.util.List;
import java.util.stream.Collectors;


public class QualificationManagementScreen extends BaseScreen {
    private final EmployeeService employeeService;
    private final EmployeeDTO loggedInEmployee;

    public QualificationManagementScreen(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.loggedInEmployee = null;
    }

    public QualificationManagementScreen(EmployeeService employeeService, EmployeeDTO loggedInEmployee) {
        this.employeeService = employeeService;
        this.loggedInEmployee = loggedInEmployee;
    }

    @Override
    public void display() {
        // בדיקת הרשאות - רק מנהלים יכולים לגשת למסך זה
        if (loggedInEmployee != null && !loggedInEmployee.isManager()) {
            displayError("Access denied. Only managers can access this functionality.");
            return;
        }

        String[] options = {
                "Add New Position",
                "View All Positions",
                "Add Qualification to Employee",
                "Remove Qualification from Employee",  // אופציה חדשה
                "View Qualified Employees for Position",
                "Update Required Positions for Shifts"
        };

        int choice;
        do {
            choice = displayMenu("Qualification Management", options);

            switch (choice) {
                case 1:
                    addNewPosition();
                    break;
                case 2:
                    displayAllPositions();
                    break;
                case 3:
                    addEmployeeQualification();
                    break;
                case 4:
                    removeEmployeeQualification();  // קריאה לפונקציה חדשה
                    break;
                case 5:
                    displayQualifiedEmployees();
                    break;
                case 6:
                    updateRequiredPositions();
                    break;
                case 0:
                    // Return to previous menu
                    break;
            }
        } while (choice != 0);
    }

    private void addNewPosition() {
        displayTitle("Add New Position");

        String name = getInput("Enter Position Name");

        // Check if position already exists
        if (employeeService.getPositionDetails(name) != null) {
            displayError("Position with this name already exists");
            return;
        }

        boolean isShiftManagerRole = getBooleanInput("Is this a shift manager position?");

        // Add position to the system
        if (employeeService.addPosition(name, isShiftManagerRole)) {
            displayMessage("Position added successfully!");
        } else {
            displayError("Error adding position");
        }
    }


    private void displayAllPositions() {
        displayTitle("All Positions");

        List<PositionDTO> positions = employeeService.getAllPositions();

        if (positions.isEmpty()) {
            displayMessage("No positions defined in the system");
            return;
        }

        for (PositionDTO position : positions) {
            String isManager = position.isRequiresShiftManager() ? " (Shift Manager Position)" : "";
            displayMessage(position.getName() + isManager);
        }
    }

//    private void addEmployeeQualification() {
//        displayTitle("Add Qualification to Employee");
//
//        // Select employee
//        EmployeeDTO employee = selectEmployee();
//        if (employee == null) {
//            return;
//        }
//
//        // Display current qualifications
//        displayMessage("Current qualifications for " + employee.getFullName() + ":");
//        if (employee.getQualifiedPositions().isEmpty()) {
//            displayMessage("None");
//        } else {
//            for (String position : employee.getQualifiedPositions()) {
//                displayMessage("- " + position);
//            }
//        }
//
//        // Select position
//        PositionDTO position = selectPosition();
//        if (position == null) {
//            return;
//        }
//
//        // Check if already qualified
//        if (employee.getQualifiedPositions().contains(position.getName())) {
//            displayError("Employee is already qualified for this position");
//            return;
//        }
//
//        // Add qualification
//        boolean success = employeeService.addQualificationToEmployee(employee.getId(), position.getName());
//
//        if (success) {
//            displayMessage("Qualification added successfully");
//        } else {
//            displayError("Error adding qualification");
//        }
//    }
private void addEmployeeQualification() {
    displayTitle("Add Qualification to Employee");

    // Select employee
    EmployeeDTO employee = selectEmployee();
    if (employee == null) {
        return;
    }

    // Display current qualifications
    displayMessage("Current qualifications for " + employee.getFullName() + ":");
    if (employee.getQualifiedPositions().isEmpty()) {
        displayMessage("None");
    } else {
        for (String position : employee.getQualifiedPositions()) {
            displayMessage("- " + position);
        }
    }

    // Select position
    PositionDTO position = selectPosition();
    if (position == null) {
        return;
    }

    // Check if already qualified
    if (employee.getQualifiedPositions().contains(position.getName())) {
        displayError("Employee is already qualified for this position");
        return;
    }

    // Add qualification
    boolean success = employeeService.addQualificationToEmployee(employee.getId(), position.getName());

    if (success) {
        displayMessage("Qualification added successfully");

        // אם התפקיד דורש מנהל משמרת וגם העובד אינו מנהל כבר
        if (position.isRequiresShiftManager() && !employee.isManager()) {
            // עדכן את תפקיד העובד באופן אוטומטי ל-SHIFT_MANAGER
            if (employeeService.updateEmployeeRole(employee.getId(), "SHIFT_MANAGER")) {
                // קבע את הסיסמה להיות מספר תעודת הזהות של העובד
                employeeService.updateEmployeePassword(employee.getId(), employee.getId());
                displayMessage("Employee role updated to Shift Manager automatically");
                displayMessage("Password set to employee ID: " + employee.getId());
            } else {
                displayError("Failed to update employee role to Shift Manager");
            }
        }
    } else {
        displayError("Error adding qualification");
    }
}
    private void removeEmployeeQualification() {
        displayTitle("Remove Qualification from Employee");

        // בחירת עובד
        EmployeeDTO employee = selectEmployee();
        if (employee == null) {
            return;
        }

        // הצגת הסמכות נוכחיות
        displayMessage("Current qualifications for " + employee.getFullName() + ":");
        if (employee.getQualifiedPositions().isEmpty()) {
            displayMessage("None");
            return;  // אם אין הסמכות, חזור
        } else {
            for (String position : employee.getQualifiedPositions()) {
                displayMessage("- " + position);
            }
        }

        // בניית מערך של שמות הסמכות לתצוגה בתפריט
        String[] positionNames = new String[employee.getQualifiedPositions().size()];
        employee.getQualifiedPositions().toArray(positionNames);

        int choice = displayMenu("Select Qualification to Remove", positionNames);
        if (choice == 0) {
            return;  // המשתמש בחר לחזור
        }

        String positionToRemove = positionNames[choice - 1];

        // הסר הסמכה דרך הservice
        boolean success = employeeService.removeQualificationFromEmployee(employee.getId(), positionToRemove);

        if (success) {
            displayMessage("Qualification successfully removed");
        } else {
            displayError("Error removing qualification");
        }
    }


    private void displayQualifiedEmployees() {
        displayTitle("Qualified Employees for Position");

        // Select position
        PositionDTO position = selectPosition();
        if (position == null) {
            return;
        }

        // Get qualified employees
        List<EmployeeDTO> qualifiedEmployees = employeeService.getQualifiedEmployeesForPosition(position.getName());

        if (qualifiedEmployees.isEmpty()) {
            displayMessage("No employees are qualified for position: " + position.getName());
            return;
        }

        displayMessage("Employees qualified for position: " + position.getName());
        for (EmployeeDTO employee : qualifiedEmployees) {
            displayMessage("- " + employee.getFullName() + " (ID: " + employee.getId() + ")");
        }
    }

    private void updateRequiredPositions() {
        displayTitle("Update Required Positions for Shifts");

        // Select position
        PositionDTO position = selectPosition();
        if (position == null) {
            return;
        }

        updateRequiredPositionsForPosition(position);
    }


    private void updateRequiredPositionsForPosition(PositionDTO position) {
        displayTitle("Update Shift Requirements for Position: " + position.getName());

        // Update for morning shift
        int morningCount = getIntInput("Number of employees required for morning shift");

        if (morningCount >= 0) {
            boolean success = employeeService.addRequiredPosition("MORNING", position.getName(), morningCount);

            if (success) {
                displayMessage("Morning shift requirements updated successfully");
            } else {
                displayError("Error updating morning shift requirements");
            }
        }

        // Update for evening shift
        int eveningCount = getIntInput("Number of employees required for evening shift");

        if (eveningCount >= 0) {
            boolean success = employeeService.addRequiredPosition("EVENING", position.getName(), eveningCount);

            if (success) {
                displayMessage("Evening shift requirements updated successfully");
            } else {
                displayError("Error updating evening shift requirements");
            }
        }
    }

//    private EmployeeDTO selectEmployee() {
//        List<EmployeeDTO> employees = employeeService.getAllEmployees();
//        if (employees.isEmpty()) {
//            displayError("No employees in the system");
//            return null;
//        }
//
//        // Build array of names for display in menu
//        String[] employeeNames = new String[employees.size()];
//        for (int i = 0; i < employees.size(); i++) {
//            EmployeeDTO emp = employees.get(i);
//            employeeNames[i] = emp.getFullName() + " (ID: " + emp.getId() + ")";
//        }
//
//        int choice = displayMenu("Select Employee", employeeNames);
//        if (choice == 0) {
//            return null; // User chose to go back
//        }
//
//        return employees.get(choice - 1);
//    }

    private EmployeeDTO selectEmployee() {
        // קבל רשימת עובדים אבל סנן את האדמין
        List<EmployeeDTO> employees = employeeService.getAllEmployees().stream()
                .filter(emp -> !emp.getId().equals("admin"))
                .collect(Collectors.toList());

        if (employees.isEmpty()) {
            displayError("No employees in the system");
            return null;
        }

        // Build array of names for display in menu
        String[] employeeNames = new String[employees.size()];
        for (int i = 0; i < employees.size(); i++) {
            EmployeeDTO emp = employees.get(i);
            employeeNames[i] = emp.getFullName() + " (ID: " + emp.getId() + ")";
        }

        int choice = displayMenu("Select Employee", employeeNames);
        if (choice == 0) {
            return null; // User chose to go back
        }

        return employees.get(choice - 1);
    }

    private PositionDTO selectPosition() {
        List<PositionDTO> positions = employeeService.getAllPositions();

        if (positions.isEmpty()) {
            displayError("No positions defined in the system");
            return null;
        }

        // Build array of names for display in menu
        String[] positionNames = new String[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            PositionDTO pos = positions.get(i);
            String isManager = pos.isRequiresShiftManager() ? " (Shift Manager)" : "";
            positionNames[i] = pos.getName() + isManager;
        }

        int choice = displayMenu("Select Position", positionNames);
        if (choice == 0) {
            return null; // User chose to go back
        }

        return positions.get(choice - 1);
    }
}