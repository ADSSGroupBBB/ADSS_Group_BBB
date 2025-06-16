package src.Domain_employee;

/**
 * Factory for creating EmployeeManager instances.
 * Provides a cleaner way to get an EmployeeManager without using Singleton pattern.
 */
public class EmployeeManagerFactory {
    private static IEmployeeManager employeeManager;

    /**
     * Gets or creates an instance of EmployeeManager.
     * Still provides a centralized way to access the employee manager,
     * but with better separation and testability.
     */
    public static synchronized IEmployeeManager getEmployeeManager() {
        if (employeeManager == null) {
            employeeManager = new EmployeeManager();
        }
        return employeeManager;
    }

    /**
     * Sets a custom implementation of IEmployeeManager.
     * Useful for testing with mock objects.
     */
    public static void setEmployeeManager(IEmployeeManager manager) {
        employeeManager = manager;
    }
}