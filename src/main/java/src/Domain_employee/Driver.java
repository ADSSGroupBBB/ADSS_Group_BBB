package src.Domain_employee;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a driver in the system.
 */
public class Driver extends Employee{
    private List<Integer> licenses_list; // List of license types (as integers)
    private boolean availableToDrive;// Driver's availability status

    // Constructor
    public Driver(String id, String firstName, String lastName, String bankAccount, LocalDate startDate, double salary,
                  UserRole role, String password, int sickDays, int vacationDays, String pensionFundName, String branch, List<Integer> licenses_list) {
        // Initialize driver fields
        super(id, firstName, lastName, bankAccount, startDate, salary, role, password, sickDays, vacationDays,pensionFundName, branch);
        this.licenses_list = licenses_list;
        this.availableToDrive = true; // Default availability is true
    }

    public List<Integer> getLicenses_list() {
        return licenses_list;
    }

    public void setLicenses_list(List<Integer> licenses_list) {
        this.licenses_list = licenses_list;
    }

    public boolean is_available() {
        return availableToDrive;
    }

    public void set_availabilityToDrive(boolean is_available) {
        this.availableToDrive = is_available;
    }

    // String representation of the driver
    @Override
    public String toString() {
        StringBuilder licensesStr = new StringBuilder();
        for (int i = 0; i < licenses_list.size(); i++) {
            licensesStr.append(licenses_list.get(i));

            if (i < licenses_list.size() - 1) {
                licensesStr.append(" ");
            }
        }
        return super.toString() + " licences list: " + licensesStr + " can drive: " + availableToDrive + " }";
    }
}