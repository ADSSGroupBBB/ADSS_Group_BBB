package Domain_employee;

/**
 * The Position class represents a job position or role within the organization.
 * Each position has a unique name and may require shift manager privileges to perform.
 */

public class Position {
    private String name;
    private boolean requiresShiftManager;

    public Position(String name, boolean requiresShiftManager) {
        this.name = name;
        this.requiresShiftManager = requiresShiftManager;

        if (requiresShiftManager) {
            IEmployeeManager employeeManager = EmployeeManagerFactory.getEmployeeManager();
            RequiredPositions requiredPositions = employeeManager.getRequiredPositions();
            requiredPositions.setRequiredPosition(ShiftType.MORNING, this, 1);
            requiredPositions.setRequiredPosition(ShiftType.EVENING, this, 1);
        }
    }

    // Getters & Setters

    public String getName() {
        return name;
    }

    public boolean isRequiresShiftManager() {
        return requiresShiftManager;
    }

    @Override
    public String toString() {
        return name;
    }
}