//package Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
///**
// * Data Transfer Object for Employee information
// * Used to pass employee data between service and presentation layers
// * without exposing domain objects
// */
//public class EmployeeDTO {
//    private final String id;
//    private final String firstName;
//    private final String lastName;
//    private final String bankAccount;
//    private final LocalDate startDate;
//    private final double salary;
//    private final List<String> qualifiedPositions;
//
//    public EmployeeDTO(String id, String firstName, String lastName, String bankAccount,
//                       LocalDate startDate, double salary, List<String> qualifiedPositions) {
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.bankAccount = bankAccount;
//        this.startDate = startDate;
//        this.salary = salary;
//        this.qualifiedPositions = qualifiedPositions;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public String getFullName() {
//        return firstName + " " + lastName;
//    }
//
//    public String getBankAccount() {
//        return bankAccount;
//    }
//
//    public LocalDate getStartDate() {
//        return startDate;
//    }
//    public double getSalary() {
//        return salary;
//    }
//
//    public List<String> getQualifiedPositions() {
//        return qualifiedPositions;
//    }
//}

package Service;

import Domain.Employee.UserRole;
import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object for Employee information
 * Used to pass employee data between service and presentation layers
 * without exposing domain objects
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
    // Getters לתנאי העסקה
    public int getSickDays() { return sickDays; }
    public int getVacationDays() { return vacationDays; }
    public String getPensionFundName() { return pensionFundName; }
}