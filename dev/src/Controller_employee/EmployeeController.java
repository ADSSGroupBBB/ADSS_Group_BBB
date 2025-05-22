package Controller_employee;

import Service_employee.EmployeeDTO;
import Service_employee.EmployeeService;
import Service_employee.PositionDTO;
import Service_employee.ShiftDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller responsible for handling employee-related operations.
 * Acts as an intermediary between the presentation layer and the service layer.
 */
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController() {
        this.employeeService = new EmployeeService();
    }

    /**
     * Adds a new employee to the system.
     */
    public boolean addEmployee(String id, String firstName, String lastName, String bankAccount, LocalDate startDate, double salary, int sickDays, int vacationDays, String pensionFundName) {
        return employeeService.addNewEmployee(id, firstName, lastName, bankAccount, startDate, salary, sickDays, vacationDays, pensionFundName);
    }

    /**
     * Adds a new employee with a manager role.
     */
    public boolean addManagerEmployee(String id, String firstName, String lastName, String bankAccount, LocalDate startDate, double salary, String role, String password, int sickDays, int vacationDays, String pensionFundName) {
        return employeeService.addNewEmployee(id, firstName, lastName, bankAccount, startDate, salary, role, password, sickDays, vacationDays, pensionFundName);
    }

    /**
     * Gets all employees from the system.
     */
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    /**
     * Gets an employee by ID.
     */
    public EmployeeDTO getEmployee(String id) {
        return employeeService.getEmployeeDetails(id);
    }

    /**
     * Removes an employee from the system.
     */
    public boolean removeEmployee(String id) {
        return employeeService.removeEmployee(id);
    }

    /**
     * Updates employee availability.
     */
    public boolean updateEmployeeAvailability(String employeeId, DayOfWeek dayOfWeek, boolean morningAvailable, boolean eveningAvailable) {
        return employeeService.updateEmployeeAvailability(employeeId, dayOfWeek, morningAvailable, eveningAvailable);
    }

    /**
     * Verifies employee credentials for login.
     */
    public boolean verifyEmployeeCredentials(String id, String password) {
        return employeeService.verifyPassword(id, password);
    }

    /**
     * Updates employee's first name.
     */
    public boolean updateEmployeeFirstName(String id, String firstName) {
        return employeeService.updateEmployeeFirstName(id, firstName);
    }

    /**
     * Updates employee's last name.
     */
    public boolean updateEmployeeLastName(String id, String lastName) {
        return employeeService.updateEmployeeLastName(id, lastName);
    }

    /**
     * Updates employee's bank account.
     */
    public boolean updateEmployeeBankAccount(String id, String bankAccount) {
        return employeeService.updateEmployeeBankAccount(id, bankAccount);
    }

    /**
     * Updates employee's salary.
     */
    public boolean updateEmployeeSalary(String id, double salary) {
        return employeeService.updateEmployeeSalary(id, salary);
    }

    /**
     * Updates employee's role.
     */
    public boolean updateEmployeeRole(String id, String role) {
        return employeeService.updateEmployeeRole(id, role);
    }

    /**
     * Updates employee's password.
     */
    public boolean updateEmployeePassword(String id, String password) {
        return employeeService.updateEmployeePassword(id, password);
    }

    /**
     * Updates employee's sick days.
     */
    public boolean updateEmployeeSickDays(String id, int sickDays) {
        return employeeService.updateEmployeeSickDays(id, sickDays);
    }

    /**
     * Updates employee's vacation days.
     */
    public boolean updateEmployeeVacationDays(String id, int vacationDays) {
        return employeeService.updateEmployeeVacationDays(id, vacationDays);
    }

    /**
     * Updates employee's pension fund.
     */
    public boolean updateEmployeePensionFund(String id, String pensionFundName) {
        return employeeService.updateEmployeePensionFund(id, pensionFundName);
    }

    /**
     * Checks if there are shift managers in the system.
     */
    public boolean hasShiftManagers() {
        return employeeService.hasShiftManagers();
    }

    /**
     * Checks if employee is available for next week for a specific day and shift type.
     */
    public boolean isEmployeeAvailableForNextWeek(String employeeId, DayOfWeek dayOfWeek, String shiftType) {
        return employeeService.isEmployeeAvailableForNextWeek(employeeId, dayOfWeek, shiftType);
    }

    /**
     * Updates employee availability for next week.
     */
    public boolean updateEmployeeAvailabilityForNextWeek(String employeeId, DayOfWeek dayOfWeek, boolean morningAvailable, boolean eveningAvailable) {
        return employeeService.updateEmployeeAvailabilityForNextWeek(employeeId, dayOfWeek, morningAvailable, eveningAvailable);
    }

    /**
     * Checks if employee is available for a specific day and shift type.
     */
    public boolean isEmployeeAvailable(String employeeId, DayOfWeek dayOfWeek, String shiftType) {
        return employeeService.isEmployeeAvailable(employeeId, dayOfWeek, shiftType);
    }

    /**
     * Updates shift hours for a specific shift type.
     */
    public boolean updateShiftHours(String shiftTypeStr, String newStart, String newEnd) {
        return employeeService.updateShiftHours(shiftTypeStr, newStart, newEnd);
    }

    /**
     * Gets all positions in the system.
     */
    public List<PositionDTO> getAllPositions() {
        return employeeService.getAllPositions();
    }

    /**
     * Gets a position by name.
     */
    public PositionDTO getPositionDetails(String name) {
        return employeeService.getPositionDetails(name);
    }

    /**
     * Gets qualified employees for a specific position.
     */
    public List<EmployeeDTO> getQualifiedEmployeesForPosition(String positionName) {
        return employeeService.getQualifiedEmployeesForPosition(positionName);
    }

    /**
     * Gets all shifts as DTOs.
     */
    public List<ShiftDTO> getAllShiftsAsDTO() {
        return employeeService.getAllShiftsAsDTO();
    }
}