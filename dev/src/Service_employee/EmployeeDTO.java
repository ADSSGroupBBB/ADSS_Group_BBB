
package Service_employee;

import Domain_employee.Employee.UserRole;
import java.time.LocalDate;
import java.util.List;


/**
 * Data Transfer Object for Employee information.
 * This class is used to transfer employee data between different layers
 * (such as service and presentation) without exposing internal domain objects.
 *
 * It encapsulates employee details in a read-only structure.
 * All fields are immutable (final).
 *
 * Fields include personal info, employment details, permissions, and benefits.
 */

public class EmployeeDTO {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String bankAccount;
    private final LocalDate startDate;
    private final double salary;
    private final List<String> qualifiedPositions;
    private final UserRole role;
    private final int sickDays;
    private final int vacationDays;
    private final String pensionFundName;

    /**
     * Constructor to initialize all fields of the EmployeeDTO.
     *
     * @param id                Employee ID
     * @param firstName         First name
     * @param lastName          Last name
     * @param bankAccount       Bank account number
     * @param startDate         Employment start date
     * @param salary            Salary amount
     * @param qualifiedPositions List of qualified positions
     * @param role              Employee role
     * @param sickDays          Number of sick days
     * @param vacationDays      Number of vacation days
     * @param pensionFundName   Pension fund name
     */
    public EmployeeDTO(String id, String firstName, String lastName, String bankAccount,
                       LocalDate startDate, double salary, List<String> qualifiedPositions, UserRole role,
                       int sickDays, int vacationDays, String pensionFundName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bankAccount = bankAccount;
        this.startDate = startDate;
        this.salary = salary;
        this.qualifiedPositions = qualifiedPositions;
        this.role = role;
        this.sickDays = sickDays;
        this.vacationDays = vacationDays;
        this.pensionFundName = pensionFundName;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public double getSalary() {
        return salary;
    }

    public List<String> getQualifiedPositions() {
        return qualifiedPositions;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isManager() {
        return role == UserRole.SHIFT_MANAGER || role == UserRole.HR_MANAGER;
    }

    public boolean isHRManager() {
        return role == UserRole.HR_MANAGER;
    }

    public boolean isShiftManager() {
        return role == UserRole.SHIFT_MANAGER;
    }
    public int getSickDays() { return sickDays; }
    public int getVacationDays() { return vacationDays; }
    public String getPensionFundName() { return pensionFundName; }
}