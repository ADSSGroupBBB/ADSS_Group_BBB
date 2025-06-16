package Presentation_employee;

import dto.BranchDTO;
import dto.EmployeeDTO;
import dto.ShiftDTO;

import java.util.List;

/**
 * BranchManagementScreen provides the user interface for managing branches
 * and viewing branch-specific information.
 */
public class BranchManagementScreen extends BaseScreen {
    private final NavigationManager navigationManager;

    public BranchManagementScreen(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
    }

    @Override
    public void display() {
        String[] options = {
                "View All Branches",
                "View Branch Details",
                "View Employees by Branch",
                "View Shifts by Branch",
                "Branch Statistics"
        };

        int choice;
        do {
            choice = displayMenu("Branch Management", options);

            switch (choice) {
                case 1:
                    viewAllBranches();
                    break;
                case 2:
                    viewBranchDetails();
                    break;
                case 3:
                    viewEmployeesByBranch();
                    break;
                case 4:
                    viewShiftsByBranch();
                    break;
                case 5:
                    showBranchStatistics();
                    break;
                case 0:
                    // Return to previous menu
                    break;
            }
        } while (choice != 0);
    }

    private void viewAllBranches() {
        displayTitle("All Branches");

        List<BranchDTO> branches = navigationManager.getBranchService().getAllBranches();

        if (branches.isEmpty()) {
            displayMessage("No branches found in the system.");
            displayMessage("Note: Branches are managed by the delivery module.");
            return;
        }

        displayMessage("Available branches:");
        for (int i = 0; i < branches.size(); i++) {
            BranchDTO branch = branches.get(i);
            displayMessage((i + 1) + ". " + branch.getAddress() +
                    " (Zone: " + branch.getZoneName() +
                    ", Contact: " + branch.getContactName() +
                    ", Phone: " + branch.getContactNum() + ")");
        }
    }

    private void viewBranchDetails() {
        displayTitle("Branch Details");

        BranchDTO selectedBranch = selectBranch();
        if (selectedBranch == null) {
            return;
        }

        displayTitle("Details for Branch: " + selectedBranch.getAddress());
        displayMessage("Address: " + selectedBranch.getAddress());
        displayMessage("Zone: " + selectedBranch.getZoneName());
        displayMessage("Contact Person: " + selectedBranch.getContactName());
        displayMessage("Phone: " + selectedBranch.getContactNum());

        // Show employee count for this branch
        List<EmployeeDTO> branchEmployees = navigationManager.getEmployeeService()
                .getEmployeesByBranch(selectedBranch.getAddress());
        displayMessage("Number of Employees: " + branchEmployees.size());

        // Show future shifts count for this branch
        List<ShiftDTO> futureShifts = navigationManager.getShiftService()
                .getFutureShiftsByBranch(selectedBranch.getAddress());
        displayMessage("Upcoming Shifts: " + futureShifts.size());
    }

    private void viewEmployeesByBranch() {
        displayTitle("Employees by Branch");

        BranchDTO selectedBranch = selectBranch();
        if (selectedBranch == null) {
            return;
        }

        List<EmployeeDTO> employees = navigationManager.getEmployeeService()
                .getEmployeesByBranch(selectedBranch.getAddress());

        if (employees.isEmpty()) {
            displayMessage("No employees assigned to branch: " + selectedBranch.getAddress());
            return;
        }

        displayTitle("Employees at " + selectedBranch.getAddress());
        for (EmployeeDTO employee : employees) {
            String roleStr = "";
            if (employee.isHRManager()) {
                roleStr = " (HR Manager)";
            } else if (employee.isShiftManager()) {
                roleStr = " (Shift Manager)";
            }

            displayMessage("- " + employee.getFullName() + " (ID: " + employee.getId() + ")" + roleStr);
        }
    }

    private void viewShiftsByBranch() {
        displayTitle("Shifts by Branch");

        BranchDTO selectedBranch = selectBranch();
        if (selectedBranch == null) {
            return;
        }

        String[] timeOptions = {"Future Shifts", "Historical Shifts", "All Shifts"};
        int timeChoice = displayMenu("Select Time Period", timeOptions);

        if (timeChoice == 0) {
            return;
        }

        List<ShiftDTO> shifts;
        String title;

        switch (timeChoice) {
            case 1:
                shifts = navigationManager.getShiftService().getFutureShiftsByBranch(selectedBranch.getAddress());
                title = "Future Shifts";
                break;
            case 2:
                shifts = navigationManager.getShiftService().getHistoricalShiftsByBranch(selectedBranch.getAddress());
                title = "Historical Shifts";
                break;
            case 3:
                shifts = navigationManager.getShiftService().getShiftsByBranch(selectedBranch.getAddress());
                title = "All Shifts";
                break;
            default:
                return;
        }

        if (shifts.isEmpty()) {
            displayMessage("No " + title.toLowerCase() + " found for branch: " + selectedBranch.getAddress());
            return;
        }

        displayTitle(title + " at " + selectedBranch.getAddress());
        for (ShiftDTO shift : shifts) {
            String managerInfo = shift.hasShiftManager() ? " (Manager: " + shift.getShiftManagerName() + ")" : " (No Manager)";
            displayMessage("- " + shift.getDate() + " " + shift.getShiftType() +
                    " (" + shift.getStartTime() + "-" + shift.getEndTime() + ")" + managerInfo);
        }
    }

    private void showBranchStatistics() {
        displayTitle("Branch Statistics");

        List<BranchDTO> branches = navigationManager.getBranchService().getAllBranches();

        if (branches.isEmpty()) {
            displayMessage("No branches found in the system.");
            return;
        }

        displayMessage("=== Overall Statistics ===");
        displayMessage("Total Branches: " + branches.size());

        int totalEmployees = 0;
        int totalFutureShifts = 0;

        for (BranchDTO branch : branches) {
            List<EmployeeDTO> branchEmployees = navigationManager.getEmployeeService()
                    .getEmployeesByBranch(branch.getAddress());
            List<ShiftDTO> futureShifts = navigationManager.getShiftService()
                    .getFutureShiftsByBranch(branch.getAddress());

            totalEmployees += branchEmployees.size();
            totalFutureShifts += futureShifts.size();

            displayMessage("\n--- " + branch.getAddress() + " ---");
            displayMessage("Zone: " + branch.getZoneName());
            displayMessage("Employees: " + branchEmployees.size());
            displayMessage("Future Shifts: " + futureShifts.size());
        }

        displayMessage("\n=== Summary ===");
        displayMessage("Total Employees Across All Branches: " + totalEmployees);
        displayMessage("Total Future Shifts Across All Branches: " + totalFutureShifts);

        // Show employees without branch assignment
        List<EmployeeDTO> allEmployees = navigationManager.getEmployeeService().getAllEmployees();
        long employeesWithoutBranch = allEmployees.stream()
                .filter(emp -> !emp.hasBranch())
                .count();

        if (employeesWithoutBranch > 0) {
            displayMessage("Employees without branch assignment: " + employeesWithoutBranch);
        }
    }

    private BranchDTO selectBranch() {
        List<BranchDTO> branches = navigationManager.getBranchService().getAllBranches();

        if (branches.isEmpty()) {
            displayError("No branches available in the system");
            return null;
        }

        String[] branchNames = new String[branches.size()];
        for (int i = 0; i < branches.size(); i++) {
            BranchDTO branch = branches.get(i);
            branchNames[i] = branch.getAddress() + " (" + branch.getZoneName() + ")";
        }

        int choice = displayMenu("Select Branch", branchNames);

        if (choice == 0) {
            return null;
        }

        return branches.get(choice - 1);
    }
}