package Domain;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Employee{
    private String id; // id
    private String firstName;
    private String lastName;
    private String bankAccount; // bank account
    private LocalDate startDate; // date start
    private double salary; // salary
    private Set<Position> qualifiedPositions; // תפקידים שהעובד מוסמך לבצע
    private EmployeeAvailability availability; // זמינות העובד למשמרות
    private UserRole role;
    private String password;
    private int sickDays;
    private int vacationDays;
    private String pensionFundName;

    public Employee(String id, String firstName, String lastName, String bankAccount,
                    LocalDate startDate, double salary, UserRole role, String password,
                    int sickDays, int vacationDays, String pensionFundName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bankAccount = bankAccount;
        this.startDate = startDate;
        this.salary = salary;
        this.role = role;
        this.password = password;

        this.qualifiedPositions = new HashSet<>();
        this.availability = new EmployeeAvailability(id);

        this.sickDays = sickDays;
        this.vacationDays = vacationDays;
        this.pensionFundName = pensionFundName;
    }

    public enum UserRole {
        REGULAR_EMPLOYEE,  // עובד רגיל
        SHIFT_MANAGER,     // מנהל משמרת
        HR_MANAGER         // מנהל כח אדם
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Domain.Employee employee = (Domain.Employee) o;
        return id.equals(employee.id);
    }

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




    // גטרים וסטרים פשוטים
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

    // שיטות עזר לבדיקת הרשאות
    public boolean isManager() {
        return role == UserRole.SHIFT_MANAGER || role == UserRole.HR_MANAGER;
    }

    public boolean isHRManager() {
        return role == UserRole.HR_MANAGER;
    }

    public boolean isShiftManager() {
        return role == UserRole.SHIFT_MANAGER;
    }

    public boolean removeQualifiedPosition(Position position) {
        return qualifiedPositions.remove(position);
    }
}