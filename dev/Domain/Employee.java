package Domain;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * The Employee class represents an employee in the organization with all relevant attributes and functionality.
 * This class manages personal information, employment details, availability, qualifications, and access permissions.
 *
 * Employees can have different roles (regular employee, shift manager, HR manager) which determine their permissions
 * in the system. Each employee can be qualified for multiple positions and has a configurable availability schedule.
 *
 * The class also manages employment benefits such as sick days, vacation days, and pension fund information.

 */


public class Employee{
    private String id; // id
    private String firstName;
    private String lastName;
    private String bankAccount; // bank account
    private LocalDate startDate; // date start
    private double salary; // salary
    private Set<Position> qualifiedPositions;
    private EmployeeAvailability availability;
    private UserRole role;
    private String password;
    private int sickDays;
    private int vacationDays;
    private String pensionFundName;

    public Employee(String id, String firstName, String lastName, String bankAccount,
                    LocalDate startDate, double salary, UserRole role,
                    String password, int sickDays, int vacationDays, String pensionFundName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bankAccount = bankAccount;
        this.startDate = startDate;
        this.salary = salary;
        this.role = role; // System role determining permissions
        this.password = password; // Login password

        this.qualifiedPositions = new HashSet<>();  // Positions the employee is qualified to perform
        this.availability = new EmployeeAvailability(id); // Employee's shift availability

        this.sickDays = sickDays;  // Available sick days balance
        this.vacationDays = vacationDays;
        this.pensionFundName = pensionFundName;  // Name of the employee's pension fund
    }

    public enum UserRole {
        REGULAR_EMPLOYEE,
        SHIFT_MANAGER,
        HR_MANAGER
    }
    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public EmployeeAvailability getAvailability() {
        return availability;
    }

    /**
     * Adds a new position that the employee is qualified to perform.
     * This is important for scheduling employees to appropriate shifts.
     *
     * @param position The position to add to qualified positions
     * @return true if the position was not already in the qualified positions set
     */
    public boolean addQualifiedPosition(Position position) {
        return qualifiedPositions.add(position);
    }

    public boolean isQualifiedFor(Position position) {
        return qualifiedPositions.contains(position);
    }

    public Set<Position> getQualifiedPositions() {
        return new HashSet<>(qualifiedPositions);
    }
    public int getSickDays() {
        return sickDays;
    }

    public void setSickDays(int sickDays) {
        this.sickDays = sickDays;
    }

    public int getVacationDays() {
        return vacationDays;
    }

    public void setVacationDays(int vacationDays) {
        this.vacationDays = vacationDays;
    }

    public String getPensionFundName() {
        return pensionFundName;
    }

    public void setPensionFundName(String pensionFundName) {
        this.pensionFundName = pensionFundName;
    }


    /**
     * Checks if two Employee objects are equal based on their ID.
     * Two employees are considered equal if they have the same ID.
     *
     * @param o The object to compare with
     * @return true if the objects are equal, false otherwise
     */


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Domain.Employee employee = (Domain.Employee) o;
        return id.equals(employee.id);
    }


    /**
     * Generates a hash code for the Employee based on its ID.
     *
     * @return The hash code value
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", startDate=" + startDate +
                ", sickDays=" + sickDays +
                ", vacationDays=" + vacationDays +
                ", pensionFund='" + pensionFundName + '\'' +
                '}';
    }



    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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


    /**
     * Removes a position from the employee's qualified positions.
     *
     * @param position The position to remove from qualified positions
     * @return true if the position was successfully removed
     */

    public boolean removeQualifiedPosition(Position position) {
        return qualifiedPositions.remove(position);
    }
}