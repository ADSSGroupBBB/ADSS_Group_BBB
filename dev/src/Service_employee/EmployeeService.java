package Service_employee;

import Domain_employee.EmployeeController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * Thin service layer for employee operations.
 * Acts as a bridge between presentation layer and controller (business logic).
 * This service should be thin and mainly delegate to the controller.
 */
public class EmployeeService {
    private final EmployeeController employeeController;

    public EmployeeService() {
        this.employeeController = new EmployeeController();
    }

    // Employee Management
    public boolean addEmployee(String id, String firstName, String lastName, String bankAccount,
                               LocalDate startDate, double salary, int sickDays, int vacationDays,
                               String pensionFundName) {
        return employeeController.addEmployee(id, firstName, lastName, bankAccount,
                startDate, salary, sickDays, vacationDays, pensionFundName, null);
    }

    public boolean addEmployee(String id, String firstName, String lastName, String bankAccount,
                               LocalDate startDate, double salary, int sickDays, int vacationDays,
                               String pensionFundName, String branchAddress) {
        return employeeController.addEmployee(id, firstName, lastName, bankAccount,
                startDate, salary, sickDays, vacationDays, pensionFundName, branchAddress);
    }

    public boolean addDriver(String id, String firstName, String lastName, String bankAccount,
                             LocalDate startDate, double salary, int sickDays, int vacationDays,
                             String pensionFundName, int[] licenseList){
        return employeeController.addDriver(id, firstName, lastName, bankAccount,
                startDate, salary, sickDays, vacationDays, pensionFundName, licenseList);
    }

    public boolean addManagerEmployee(String id, String firstName, String lastName, String bankAccount,
                                      LocalDate startDate, double salary, String role, String password,
                                      int sickDays, int vacationDays, String pensionFundName) {
        return employeeController.addManagerEmployee(id, firstName, lastName, bankAccount,
                startDate, salary, role, password, sickDays, vacationDays, pensionFundName, null);
    }

    public boolean addManagerEmployee(String id, String firstName, String lastName, String bankAccount,
                                      LocalDate startDate, double salary, String role, String password,
                                      int sickDays, int vacationDays, String pensionFundName, String branchAddress) {
        return employeeController.addManagerEmployee(id, firstName, lastName, bankAccount,
                startDate, salary, role, password, sickDays, vacationDays, pensionFundName, branchAddress);
    }

    public EmployeeDTO getEmployeeDetails(String id) {
        return employeeController.getEmployee(id);
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeController.getAllEmployees();
    }

    public List<EmployeeDTO> getEmployeesByBranch(String branchAddress) {
        return employeeController.getEmployeesByBranch(branchAddress);
    }

    public boolean removeEmployee(String id) {
        return employeeController.removeEmployee(id);
    }

    // Position Management
    public boolean addPosition(String name, boolean isShiftManagerRole) {
        return employeeController.addPosition(name, isShiftManagerRole);
    }

    public List<PositionDTO> getAllPositions() {
        return employeeController.getAllPositions();
    }

    public PositionDTO getPositionDetails(String name) {
        return employeeController.getPositionDetails(name);
    }

    // Qualification Management
    public boolean addQualificationToEmployee(String employeeId, String positionName) {
        return employeeController.addQualificationToEmployee(employeeId, positionName);
    }

    public boolean removeQualificationFromEmployee(String employeeId, String positionName) {
        return employeeController.removeQualificationFromEmployee(employeeId, positionName);
    }

    public List<EmployeeDTO> getQualifiedEmployeesForPosition(String positionName) {
        return employeeController.getQualifiedEmployeesForPosition(positionName);
    }

    // Availability Management
    public boolean updateEmployeeAvailability(String employeeId, DayOfWeek dayOfWeek,
                                              boolean morningAvailable, boolean eveningAvailable) {
        return employeeController.updateEmployeeAvailability(employeeId, dayOfWeek, morningAvailable, eveningAvailable);
    }

    public boolean updateEmployeeAvailabilityForNextWeek(String employeeId, DayOfWeek dayOfWeek,
                                                         boolean morningAvailable, boolean eveningAvailable) {
        // For now, same as regular availability update
        return employeeController.updateEmployeeAvailability(employeeId, dayOfWeek, morningAvailable, eveningAvailable);
    }

    public boolean isEmployeeAvailable(String employeeId, DayOfWeek dayOfWeek, String shiftType) {
        return employeeController.isEmployeeAvailable(employeeId, dayOfWeek, shiftType);
    }

    public boolean isEmployeeAvailableForNextWeek(String employeeId, DayOfWeek dayOfWeek, String shiftType) {
        // For now, same as regular availability check
        return employeeController.isEmployeeAvailable(employeeId, dayOfWeek, shiftType);
    }

    // Shift Management
    public ShiftDTO createShift(LocalDate date, String shiftType) {
        return employeeController.createShift(date, shiftType, null);
    }

    public ShiftDTO createShift(LocalDate date, String shiftType, String branchAddress) {
        return employeeController.createShift(date, shiftType, branchAddress);
    }

    public List<ShiftDTO> getAllShiftsAsDTO() {
        return employeeController.getAllShifts();
    }

    public List<ShiftDTO> getFutureShifts() {
        return employeeController.getFutureShifts();
    }

    public List<ShiftDTO> getHistoricalShifts() {
        return employeeController.getHistoricalShifts();
    }

    public List<ShiftDTO> getShiftsByBranch(String branchAddress) {
        return employeeController.getShiftsByBranch(branchAddress);
    }

    // Shift Assignment
    public boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName) {
        return employeeController.assignEmployeeToShift(shiftId, employeeId, positionName);
    }

    public boolean removeAssignmentFromShift(String shiftId, String positionName) {
        return employeeController.removeAssignmentFromShift(shiftId, positionName);
    }

    public boolean isEmployeeAlreadyAssignedToShift(String shiftId, String employeeId) {
        return employeeController.isEmployeeAlreadyAssignedToShift(shiftId, employeeId);
    }

    public boolean areAllRequiredPositionsCovered(String shiftId) {
        return employeeController.areAllRequiredPositionsCovered(shiftId);
    }

    // Required Positions
    public boolean addRequiredPosition(String shiftType, String positionName, int count) {
        return employeeController.addRequiredPosition(shiftType, positionName, count);
    }

    public int getRequiredPositionsCount(String shiftType, String positionName) {
        return employeeController.getRequiredPositionsCount(shiftType, positionName);
    }

    // Employee Updates
    public boolean updateEmployeeFirstName(String id, String firstName) {
        return employeeController.updateEmployeeFirstName(id, firstName);
    }

    public boolean updateEmployeeLastName(String id, String lastName) {
        return employeeController.updateEmployeeLastName(id, lastName);
    }

    public boolean updateEmployeeBankAccount(String id, String bankAccount) {
        return employeeController.updateEmployeeBankAccount(id, bankAccount);
    }

    public boolean updateEmployeeSalary(String id, double salary) {
        return employeeController.updateEmployeeSalary(id, salary);
    }

    public boolean updateEmployeeRole(String id, String role) {
        return employeeController.updateEmployeeRole(id, role);
    }

    public boolean updateEmployeePassword(String id, String password) {
        return employeeController.updateEmployeePassword(id, password);
    }

    public boolean updateEmployeeSickDays(String id, int sickDays) {
        return employeeController.updateEmployeeSickDays(id, sickDays);
    }

    public boolean updateEmployeeVacationDays(String id, int vacationDays) {
        return employeeController.updateEmployeeVacationDays(id, vacationDays);
    }

    public boolean updateEmployeePensionFund(String id, String pensionFundName) {
        return employeeController.updateEmployeePensionFund(id, pensionFundName);
    }

    public boolean updateEmployeeBranch(String id, String branchAddress) {
        return employeeController.updateEmployeeBranch(id, branchAddress);
    }

    // Branch Management
    public List<BranchDTO> getAllBranches() {
        return employeeController.getAllBranches();
    }

    public BranchDTO getBranchByAddress(String address) {
        return employeeController.getBranchByAddress(address);
    }

    public boolean branchExists(String address) {
        return employeeController.branchExists(address);
    }

    // Utility Methods
    public boolean hasShiftManagers() {
        return employeeController.hasShiftManagers();
    }

    public List<EmployeeDTO> getAvailableEmployeesForShift(LocalDate date, String shiftType) {
        return employeeController.getAvailableEmployeesForShift(date, shiftType);
    }

    public List<PositionDTO> getMissingPositionsForShift(String shiftId) {
        return employeeController.getMissingPositionsForShift(shiftId);
    }

    public boolean deleteShift(String shiftId) {
        return employeeController.deleteShift(shiftId);
    }

    // Password verification (for login)
    public boolean verifyPassword(String id, String password) {
        return employeeController.verifyPassword(id, password);
    }

    // Shift hours management
    public boolean updateShiftHours(String shiftTypeStr, String newStart, String newEnd) {
        return employeeController.updateShiftHours(shiftTypeStr, newStart, newEnd);
    }
}