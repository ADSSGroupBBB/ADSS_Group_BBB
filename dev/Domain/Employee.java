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

    public Employee(String id, String firstName, String lastName, String bankAccount,
                   LocalDate startDate, double salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bankAccount = bankAccount;
        this.startDate = startDate;
        this.salary = salary;
        this.qualifiedPositions = new HashSet<>();
        this.availability = new EmployeeAvailability(id);
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

//    public void setAvailability(EmployeeAvailability availability) {
//        this.availability = availability;
//    }

    public boolean addQualifiedPosition(Position position) {
        return qualifiedPositions.add(position);
    }
//    public boolean removeQualifiedPosition(Position position) {
//        return qualifiedPositions.remove(position);
//    }

    public boolean isQualifiedFor(Position position) {
        return qualifiedPositions.contains(position);
    }

    public Set<Position> getQualifiedPositions() {
        return new HashSet<>(qualifiedPositions);
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
                '}';
    }
}