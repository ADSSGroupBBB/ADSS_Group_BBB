package Service_employee;

import dto.BranchDTO;
import dto.EmployeeDTO;
import dto.PositionDTO;
import dto.ShiftDTO;
import Domain_employee.*;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * Service layer that directly uses specialized controllers.
 */
public class EmployeeService {
    private final EmployeeManagementController employeeManagementController;
    private final PositionManagementController positionManagementController;
    private final AvailabilityManagementController availabilityManagementController;
    private final ShiftManagementController shiftManagementController;
    private final BranchManagementController branchManagementController;

    public EmployeeService() {
        this.employeeManagementController = new EmployeeManagementController();
        this.positionManagementController = new PositionManagementController();
        this.availabilityManagementController = new AvailabilityManagementController();
        this.shiftManagementController = new ShiftManagementController();
        this.branchManagementController = new BranchManagementController();
    }

    // Employee Management - Direct delegation
    public boolean addEmployee(String id, String firstName, String lastName, String bankAccount,
                               LocalDate startDate, double salary, int sickDays, int vacationDays,
                               String pensionFundName, String branchAddress) {
        if (branchAddress == null || branchAddress.trim().isEmpty()) {
            System.err.println("Error: Branch address is required for all employees");
            return false;
        }
        return employeeManagementController.addEmployee(id, firstName, lastName, bankAccount,
                startDate, salary, sickDays, vacationDays, pensionFundName, branchAddress);
    }

    public boolean addDriver(String id, String firstName, String lastName, String bankAccount,
                             LocalDate startDate, double salary, int sickDays, int vacationDays,
                             String pensionFundName, String branchAddress, List<Integer> license_list) {
        if (branchAddress == null || branchAddress.trim().isEmpty()) {
            System.err.println("Error: Branch address is required for all employees");
            return false;
        }
        return employeeManagementController.addDriver(id, firstName, lastName, bankAccount,
                startDate, salary, sickDays, vacationDays, pensionFundName, branchAddress, license_list);
    }

    public boolean addStoreKeeper(String id, String firstName, String lastName, String bankAccount,
                                  LocalDate startDate, double salary, int sickDays, int vacationDays,
                                  String pensionFundName, String branchAddress) {
        if (branchAddress == null || branchAddress.trim().isEmpty()) {
            System.err.println("Error: Branch address is required for all employees");
            return false;
        }
        return employeeManagementController.addStoreKeeper(id, firstName, lastName, bankAccount,
                startDate, salary, sickDays, vacationDays, pensionFundName, branchAddress);
    }

    public boolean addManagerEmployee(String id, String firstName, String lastName, String bankAccount,
                                      LocalDate startDate, double salary, String role, String password,
                                      int sickDays, int vacationDays, String pensionFundName, String branchAddress) {
        if (branchAddress == null || branchAddress.trim().isEmpty()) {
            System.err.println("Error: Branch address is required for all employees");
            return false;
        }
        return employeeManagementController.addManagerEmployee(id, firstName, lastName, bankAccount,
                startDate, salary, role, password, sickDays, vacationDays, pensionFundName, branchAddress);
    }

    public EmployeeDTO getEmployeeDetails(String id) {
        return employeeManagementController.getEmployee(id);
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeManagementController.getAllEmployees();
    }

    public List<EmployeeDTO> getEmployeesByBranch(String branchAddress) {
        return employeeManagementController.getEmployeesByBranch(branchAddress);
    }

    public boolean removeEmployee(String id) {
        return employeeManagementController.removeEmployee(id);
    }

    public boolean removeDriver(String id) {
        return employeeManagementController.removeDriver(id);
    }

    // Position Management - Direct delegation
    public boolean addPosition(String name, boolean isShiftManagerRole) {
        return positionManagementController.addPosition(name, isShiftManagerRole);
    }

    public List<PositionDTO> getAllPositions() {
        return positionManagementController.getAllPositions();
    }

    public PositionDTO getPositionDetails(String name) {
        return positionManagementController.getPositionDetails(name);
    }

    // Qualification Management - Direct delegation
    public boolean addQualificationToEmployee(String employeeId, String positionName) {
        return positionManagementController.addQualificationToEmployee(employeeId, positionName);
    }

    public boolean removeQualificationFromEmployee(String employeeId, String positionName) {
        return positionManagementController.removeQualificationFromEmployee(employeeId, positionName);
    }

    public List<EmployeeDTO> getQualifiedEmployeesForPosition(String positionName) {
        return positionManagementController.getQualifiedEmployeesForPosition(positionName);
    }

    // Availability Management - Direct delegation
    public boolean updateEmployeeAvailability(String employeeId, DayOfWeek dayOfWeek,
                                              boolean morningAvailable, boolean eveningAvailable) {
        return availabilityManagementController.updateEmployeeAvailability(employeeId, dayOfWeek,
                morningAvailable, eveningAvailable);
    }

    public boolean updateEmployeeAvailabilityForNextWeek(String employeeId, DayOfWeek dayOfWeek,
                                                         boolean morningAvailable, boolean eveningAvailable) {
        return availabilityManagementController.updateEmployeeAvailability(employeeId, dayOfWeek,
                morningAvailable, eveningAvailable);
    }

    public boolean isEmployeeAvailable(String employeeId, DayOfWeek dayOfWeek, String shiftType) {
        return availabilityManagementController.isEmployeeAvailable(employeeId, dayOfWeek, shiftType);
    }

    public boolean isEmployeeAvailableForNextWeek(String employeeId, DayOfWeek dayOfWeek, String shiftType) {
        return availabilityManagementController.isEmployeeAvailable(employeeId, dayOfWeek, shiftType);
    }

    // Shift Management - Direct delegation
    public ShiftDTO createShift(LocalDate date, String shiftType) {
        return shiftManagementController.createShift(date, shiftType, null);
    }

    public ShiftDTO createShift(LocalDate date, String shiftType, String branchAddress) {
        return shiftManagementController.createShift(date, shiftType, branchAddress);
    }

    public List<ShiftDTO> getAllShiftsAsDTO() {
        return shiftManagementController.getAllShifts();
    }

    public List<ShiftDTO> getFutureShifts() {
        return shiftManagementController.getFutureShifts();
    }

    public List<ShiftDTO> getHistoricalShifts() {
        return shiftManagementController.getHistoricalShifts();
    }

    public List<ShiftDTO> getShiftsByBranch(String branchAddress) {
        return shiftManagementController.getShiftsByBranch(branchAddress);
    }

    // Shift Assignment - Direct delegation
    public boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName) {
        return shiftManagementController.assignEmployeeToShift(shiftId, employeeId, positionName);
    }

    public boolean removeAssignmentFromShift(String shiftId, String positionName) {
        return shiftManagementController.removeAssignmentFromShift(shiftId, positionName);
    }

    public boolean isEmployeeAlreadyAssignedToShift(String shiftId, String employeeId) {
        return shiftManagementController.isEmployeeAlreadyAssignedToShift(shiftId, employeeId);
    }

    public boolean areAllRequiredPositionsCovered(String shiftId) {
        return shiftManagementController.areAllRequiredPositionsCovered(shiftId);
    }

    // Required Positions - Direct delegation
    public boolean addRequiredPosition(String shiftType, String positionName, int count) {
        return positionManagementController.addRequiredPosition(shiftType, positionName, count);
    }

    public int getRequiredPositionsCount(String shiftType, String positionName) {
        return positionManagementController.getRequiredPositionsCount(shiftType, positionName);
    }

    // Employee Updates - Direct delegation
    public boolean updateEmployeeFirstName(String id, String firstName) {
        return employeeManagementController.updateEmployeeFirstName(id, firstName);
    }

    public boolean updateEmployeeLastName(String id, String lastName) {
        return employeeManagementController.updateEmployeeLastName(id, lastName);
    }

    public boolean updateEmployeeBankAccount(String id, String bankAccount) {
        return employeeManagementController.updateEmployeeBankAccount(id, bankAccount);
    }

    public boolean updateEmployeeSalary(String id, double salary) {
        return employeeManagementController.updateEmployeeSalary(id, salary);
    }

    public boolean updateEmployeeRole(String id, String role) {
        return employeeManagementController.updateEmployeeRole(id, role);
    }

    public boolean updateEmployeePassword(String id, String password) {
        return employeeManagementController.updateEmployeePassword(id, password);
    }

    public boolean updateEmployeeSickDays(String id, int sickDays) {
        return employeeManagementController.updateEmployeeSickDays(id, sickDays);
    }

    public boolean updateEmployeeVacationDays(String id, int vacationDays) {
        return employeeManagementController.updateEmployeeVacationDays(id, vacationDays);
    }

    public boolean updateEmployeePensionFund(String id, String pensionFundName) {
        return employeeManagementController.updateEmployeePensionFund(id, pensionFundName);
    }

    public boolean updateEmployeeBranch(String id, String branchAddress) {
        return employeeManagementController.updateEmployeeBranch(id, branchAddress);
    }

    // Branch Management - Direct delegation
    public List<BranchDTO> getAllBranches() {
        return branchManagementController.getAllBranches();
    }

    public BranchDTO getBranchByAddress(String address) {
        return branchManagementController.getBranchByAddress(address);
    }

    public boolean branchExists(String address) {
        return branchManagementController.branchExists(address);
    }

    // Utility Methods - Direct delegation
    public boolean hasShiftManagers() {
        return employeeManagementController.hasShiftManagers();
    }

    public List<EmployeeDTO> getAvailableEmployeesForShift(LocalDate date, String shiftType) {
        return availabilityManagementController.getAvailableEmployeesForShift(date, shiftType);
    }

    public List<PositionDTO> getMissingPositionsForShift(String shiftId) {
        return positionManagementController.getMissingPositionsForShift(shiftId);
    }

    public boolean deleteShift(String shiftId) {
        return shiftManagementController.deleteShift(shiftId);
    }

    // Password verification (for login) - Direct delegation
    public boolean verifyPassword(String id, String password) {
        return employeeManagementController.verifyPassword(id, password);
    }

    // Shift hours management - Direct delegation
    public boolean updateShiftHours(String shiftTypeStr, String newStart, String newEnd) {
        return shiftManagementController.updateShiftHours(shiftTypeStr, newStart, newEnd);
    }

    public List<EmployeeDTO> getEmployeesWithoutBranch() {
        return employeeManagementController.getAllEmployees().stream()
                .filter(emp -> !emp.hasBranch())
                .toList();
    }

    // Helper method to get specific employee
    public EmployeeDTO getEmployee(String employeeId) {
        return employeeManagementController.getEmployee(employeeId);
    }

    // Methods for shift ID by time - delegated to shift management
    public String getShiftIdByTime(LocalDate startDate, String shiftTime, String branchAddress) throws SQLException {
        return shiftManagementController.getShiftIdByTime(startDate, shiftTime, branchAddress);
    }

    // Get all shifts
    public List<ShiftDTO> getAllShifts() {
        return shiftManagementController.getAllShifts();
    }
}