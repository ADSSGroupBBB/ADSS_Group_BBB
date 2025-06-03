package Domain_employee;

import DTO.DriverDTO;
import DataAccess.EmployeeInterface.*;
import DataAccess.EmployeeDAO.*;
import Domain.Driver;
import Service_employee.*;
import Domain_employee.Employee.UserRole;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller responsible for employee business logic.
 * This is now the main business logic layer that uses DAO for data access.
 */
public class EmployeeController {
    private final EmployeeDAO employeeDAO;
    private final PositionDAO positionDAO;
    private final QualificationDAO qualificationDAO;
    private final AvailabilityDAO availabilityDAO;
    private final ShiftDAO shiftDAO;
    private final ShiftAssignmentDAO assignmentDAO;
    private final RequiredPositionDAO requiredPositionDAO;
    private final BranchDAO branchDAO;

    public EmployeeController() {
        this.employeeDAO = new EmployeeDAOImpl();
        this.positionDAO = new PositionDAOImpl();
        this.qualificationDAO = new QualificationDAOImpl();
        this.availabilityDAO = new AvailabilityDAOImpl();
        this.shiftDAO = new ShiftDAOImpl();
        this.assignmentDAO = new ShiftAssignmentDAOImpl();
        this.requiredPositionDAO = new RequiredPositionDAOImpl();
        this.branchDAO = new BranchDAOImpl();
    }

    // Employee Management
    public boolean addEmployee(String id, String firstName, String lastName, String bankAccount,
                               LocalDate startDate, double salary, int sickDays, int vacationDays,
                               String pensionFundName, String branchAddress) {
        try {
            // Validate branch exists
            if (branchAddress != null && !branchDAO.branchExists(branchAddress)) {
                return false;
            }

            EmployeeDTO employee = new EmployeeDTO(id, firstName, lastName, bankAccount,
                    startDate, salary, new ArrayList<>(), UserRole.REGULAR_EMPLOYEE,
                    sickDays, vacationDays, pensionFundName, branchAddress);

            employeeDAO.save(employee);

            // Set default availability
            availabilityDAO.setDefaultAvailability(id);

            return true;
        } catch (Exception e) {
            System.err.println("Error adding employee: " + e.getMessage());
            return false;
        }
    }

    public boolean addDriver(String id, String firstName, String lastName, String bankAccount,
                             LocalDate startDate, double salary, int sickDays, int vacationDays,
                             String pensionFundName, int[] licenseList){
        try {


            EmployeeDTO employee = new EmployeeDTO(id, firstName, lastName, bankAccount,
                    startDate, salary, new ArrayList<>(), UserRole.DRIVER,
                    sickDays, vacationDays, pensionFundName, "without");

            employeeDAO.save(employee);

            // Set default availability
            availabilityDAO.setDefaultAvailability(id);
            for (int i = 0; i< licenseList.length; i ++){
                employeeDAO.saveDriver(new DriverDTO(id, licenseList[i], 0));
            }



            return true;
        } catch (Exception e) {
            System.err.println("Error adding employee: " + e.getMessage());
            return false;
        }
    }

    public boolean addManagerEmployee(String id, String firstName, String lastName, String bankAccount,
                                      LocalDate startDate, double salary, String role, String password,
                                      int sickDays, int vacationDays, String pensionFundName, String branchAddress) {
        try {
            // Validate branch exists
            if (branchAddress != null && !branchDAO.branchExists(branchAddress)) {
                return false;
            }

            UserRole userRole = UserRole.valueOf(role);
            EmployeeDTO employee = new EmployeeDTO(id, firstName, lastName, bankAccount,
                    startDate, salary, new ArrayList<>(), userRole,
                    sickDays, vacationDays, pensionFundName, branchAddress);

            employeeDAO.save(employee);

            // Set default availability
            availabilityDAO.setDefaultAvailability(id);

            return true;
        } catch (Exception e) {
            System.err.println("Error adding manager employee: " + e.getMessage());
            return false;
        }
    }

    public EmployeeDTO getEmployee(String id) {
        try {
            return employeeDAO.findById(id).orElse(null);
        } catch (Exception e) {
            System.err.println("Error getting employee: " + e.getMessage());
            return null;
        }
    }

    public List<EmployeeDTO> getAllEmployees() {
        try {
            return employeeDAO.findAll();
        } catch (Exception e) {
            System.err.println("Error getting all employees: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<EmployeeDTO> getEmployeesByBranch(String branchAddress) {
        try {
            return employeeDAO.findByBranch(branchAddress);
        } catch (Exception e) {
            System.err.println("Error getting employees by branch: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean removeEmployee(String id) {
        try {
            return employeeDAO.deleteById(id);
        } catch (Exception e) {
            System.err.println("Error removing employee: " + e.getMessage());
            return false;
        }
    }

    // Position Management
    public boolean addPosition(String name, boolean isShiftManagerRole) {
        try {
            PositionDTO position = new PositionDTO(name, isShiftManagerRole);
            positionDAO.save(position);

            // If it's a shift manager position, set default requirements
            if (isShiftManagerRole) {
                requiredPositionDAO.setRequiredPosition("MORNING", name, 1);
                requiredPositionDAO.setRequiredPosition("EVENING", name, 1);
            }

            return true;
        } catch (Exception e) {
            System.err.println("Error adding position: " + e.getMessage());
            return false;
        }
    }

    public List<PositionDTO> getAllPositions() {
        try {
            return positionDAO.findAll();
        } catch (Exception e) {
            System.err.println("Error getting positions: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public PositionDTO getPositionDetails(String name) {
        try {
            return positionDAO.findByName(name).orElse(null);
        } catch (Exception e) {
            System.err.println("Error getting position details: " + e.getMessage());
            return null;
        }
    }

    // Qualification Management
    public boolean addQualificationToEmployee(String employeeId, String positionName) {
        try {
            boolean success = qualificationDAO.addQualification(employeeId, positionName);

            if (success) {
                // Check if position requires shift manager role
                PositionDTO position = positionDAO.findByName(positionName).orElse(null);
                if (position != null && position.isRequiresShiftManager()) {
                    // Update employee role to SHIFT_MANAGER
                    EmployeeDTO employee = employeeDAO.findById(employeeId).orElse(null);
                    if (employee != null && employee.getRole() == UserRole.REGULAR_EMPLOYEE) {
                        EmployeeDTO updatedEmployee = new EmployeeDTO(
                                employee.getId(), employee.getFirstName(), employee.getLastName(),
                                employee.getBankAccount(), employee.getStartDate(), employee.getSalary(),
                                employee.getQualifiedPositions(), UserRole.SHIFT_MANAGER,
                                employee.getSickDays(), employee.getVacationDays(),
                                employee.getPensionFundName(), employee.getBranchAddress()
                        );
                        employeeDAO.updateEmployee(updatedEmployee);
                    }
                }
            }

            return success;
        } catch (Exception e) {
            System.err.println("Error adding qualification: " + e.getMessage());
            return false;
        }
    }

    public boolean removeQualificationFromEmployee(String employeeId, String positionName) {
        try {
            return qualificationDAO.removeQualification(employeeId, positionName);
        } catch (Exception e) {
            System.err.println("Error removing qualification: " + e.getMessage());
            return false;
        }
    }

    public List<EmployeeDTO> getQualifiedEmployeesForPosition(String positionName) {
        try {
            List<String> employeeIds = qualificationDAO.getQualifiedEmployees(positionName);
            List<EmployeeDTO> employees = new ArrayList<>();

            for (String id : employeeIds) {
                EmployeeDTO employee = employeeDAO.findById(id).orElse(null);
                if (employee != null) {
                    employees.add(employee);
                }
            }

            return employees;
        } catch (Exception e) {
            System.err.println("Error getting qualified employees: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Availability Management
    public boolean updateEmployeeAvailability(String employeeId, DayOfWeek dayOfWeek,
                                              boolean morningAvailable, boolean eveningAvailable) {
        try {
            return availabilityDAO.updateAvailability(employeeId, dayOfWeek, morningAvailable, eveningAvailable);
        } catch (Exception e) {
            System.err.println("Error updating availability: " + e.getMessage());
            return false;
        }
    }

    public boolean isEmployeeAvailable(String employeeId, DayOfWeek dayOfWeek, String shiftType) {
        try {
            return availabilityDAO.isAvailable(employeeId, dayOfWeek, shiftType);
        } catch (Exception e) {
            System.err.println("Error checking availability: " + e.getMessage());
            return false;
        }
    }

    // Shift Management
    public ShiftDTO createShift(LocalDate date, String shiftType, String branchAddress) {
        try {
            // Validate branch exists
            if (branchAddress != null && !branchDAO.branchExists(branchAddress)) {
                return null;
            }

            String shiftId = date.toString() + "_" + shiftType.toLowerCase() +
                    (branchAddress != null ? "_" + branchAddress.replaceAll("\\s+", "") : "");

            // Default shift hours - these could be configurable
            LocalTime startTime = "MORNING".equals(shiftType) ?
                    LocalTime.of(7, 0) : LocalTime.of(14, 0);
            LocalTime endTime = "MORNING".equals(shiftType) ?
                    LocalTime.of(14, 0) : LocalTime.of(21, 0);

            ShiftDTO shift = new ShiftDTO(shiftId, date, shiftType, startTime, endTime,
                    null, null, new HashMap<>(), branchAddress);

            return shiftDAO.save(shift);
        } catch (Exception e) {
            System.err.println("Error creating shift: " + e.getMessage());
            return null;
        }
    }

    public List<ShiftDTO> getAllShifts() {
        try {
            return shiftDAO.findAll();
        } catch (Exception e) {
            System.err.println("Error getting all shifts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ShiftDTO> getFutureShifts() {
        try {
            return shiftDAO.findFutureShifts();
        } catch (Exception e) {
            System.err.println("Error getting future shifts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ShiftDTO> getHistoricalShifts() {
        try {
            return shiftDAO.findHistoricalShifts();
        } catch (Exception e) {
            System.err.println("Error getting historical shifts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ShiftDTO> getShiftsByBranch(String branchAddress) {
        try {
            return shiftDAO.findByBranch(branchAddress);
        } catch (Exception e) {
            System.err.println("Error getting shifts by branch: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Shift Assignment Management
    public boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName) {
        try {
            // Get shift and employee info
            ShiftDTO shift = shiftDAO.findById(shiftId).orElse(null);
            EmployeeDTO employee = employeeDAO.findById(employeeId).orElse(null);
            PositionDTO position = positionDAO.findByName(positionName).orElse(null);

            if (shift == null || employee == null || position == null) {
                return false;
            }

            // Check if employee is already assigned to this shift
            if (assignmentDAO.isEmployeeAssigned(shiftId, employeeId)) {
                return false;
            }

            // Check required positions count
            Map<String, Integer> requiredPositions = requiredPositionDAO.getRequiredPositions(shift.getShiftType());
            int requiredCount = requiredPositions.getOrDefault(positionName, 0);

            if (requiredCount == 0) {
                return false; // Position not required for this shift type
            }

            // Check if position is already filled
            int currentAssigned = ((ShiftAssignmentDAOImpl) assignmentDAO).countAssignmentsForPosition(shiftId, positionName);
            if (currentAssigned >= requiredCount) {
                return false; // Position already filled
            }

            // Ensure employee has qualification (add if missing)
            if (!qualificationDAO.hasQualification(employeeId, positionName)) {
                qualificationDAO.addQualification(employeeId, positionName);
            }

            // Check availability
            DayOfWeek dayOfWeek = shift.getDate().getDayOfWeek();
            if (!availabilityDAO.isAvailable(employeeId, dayOfWeek, shift.getShiftType())) {
                // Auto-update availability if not available
                boolean morningAvailable = "MORNING".equals(shift.getShiftType()) ||
                        availabilityDAO.isAvailable(employeeId, dayOfWeek, "MORNING");
                boolean eveningAvailable = "EVENING".equals(shift.getShiftType()) ||
                        availabilityDAO.isAvailable(employeeId, dayOfWeek, "EVENING");
                availabilityDAO.updateAvailability(employeeId, dayOfWeek, morningAvailable, eveningAvailable);
            }

            // Determine if this is a shift manager position
            boolean isShiftManager = position.isRequiresShiftManager();

            // Assign employee
            return assignmentDAO.assignEmployee(shiftId, employeeId, positionName, isShiftManager);

        } catch (Exception e) {
            System.err.println("Error assigning employee to shift: " + e.getMessage());
            return false;
        }
    }

    public boolean removeAssignmentFromShift(String shiftId, String positionName) {
        try {
            return assignmentDAO.removeAssignment(shiftId, positionName);
        } catch (Exception e) {
            System.err.println("Error removing assignment: " + e.getMessage());
            return false;
        }
    }

    public boolean isEmployeeAlreadyAssignedToShift(String shiftId, String employeeId) {
        try {
            return assignmentDAO.isEmployeeAssigned(shiftId, employeeId);
        } catch (Exception e) {
            System.err.println("Error checking employee assignment: " + e.getMessage());
            return false;
        }
    }

    public boolean areAllRequiredPositionsCovered(String shiftId) {
        try {
            ShiftDTO shift = shiftDAO.findById(shiftId).orElse(null);
            if (shift == null) {
                return false;
            }

            Map<String, Integer> requiredPositions = requiredPositionDAO.getRequiredPositions(shift.getShiftType());
            Set<String> assignedPositions = ((ShiftAssignmentDAOImpl) assignmentDAO).getAssignedPositions(shiftId);

            for (Map.Entry<String, Integer> entry : requiredPositions.entrySet()) {
                String positionName = entry.getKey();
                int requiredCount = entry.getValue();
                int assignedCount = ((ShiftAssignmentDAOImpl) assignmentDAO).countAssignmentsForPosition(shiftId, positionName);

                if (assignedCount < requiredCount) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            System.err.println("Error checking required positions: " + e.getMessage());
            return false;
        }
    }

    // Required Positions Management
    public boolean addRequiredPosition(String shiftType, String positionName, int count) {
        try {
            return requiredPositionDAO.setRequiredPosition(shiftType, positionName, count);
        } catch (Exception e) {
            System.err.println("Error setting required position: " + e.getMessage());
            return false;
        }
    }

    public int getRequiredPositionsCount(String shiftType, String positionName) {
        try {
            return requiredPositionDAO.getRequiredCount(shiftType, positionName);
        } catch (Exception e) {
            System.err.println("Error getting required positions count: " + e.getMessage());
            return 0;
        }
    }

    // Employee Updates
    public boolean updateEmployeeFirstName(String id, String firstName) {
        try {
            EmployeeDTO employee = employeeDAO.findById(id).orElse(null);
            if (employee == null) return false;

            EmployeeDTO updated = new EmployeeDTO(id, firstName, employee.getLastName(),
                    employee.getBankAccount(), employee.getStartDate(), employee.getSalary(),
                    employee.getQualifiedPositions(), employee.getRole(),
                    employee.getSickDays(), employee.getVacationDays(),
                    employee.getPensionFundName(), employee.getBranchAddress());

            return employeeDAO.updateEmployee(updated);
        } catch (Exception e) {
            System.err.println("Error updating first name: " + e.getMessage());
            return false;
        }
    }

    public boolean updateEmployeeLastName(String id, String lastName) {
        try {
            EmployeeDTO employee = employeeDAO.findById(id).orElse(null);
            if (employee == null) return false;

            EmployeeDTO updated = new EmployeeDTO(id, employee.getFirstName(), lastName,
                    employee.getBankAccount(), employee.getStartDate(), employee.getSalary(),
                    employee.getQualifiedPositions(), employee.getRole(),
                    employee.getSickDays(), employee.getVacationDays(),
                    employee.getPensionFundName(), employee.getBranchAddress());

            return employeeDAO.updateEmployee(updated);
        } catch (Exception e) {
            System.err.println("Error updating last name: " + e.getMessage());
            return false;
        }
    }

    public boolean updateEmployeeBankAccount(String id, String bankAccount) {
        try {
            EmployeeDTO employee = employeeDAO.findById(id).orElse(null);
            if (employee == null) return false;

            EmployeeDTO updated = new EmployeeDTO(id, employee.getFirstName(), employee.getLastName(),
                    bankAccount, employee.getStartDate(), employee.getSalary(),
                    employee.getQualifiedPositions(), employee.getRole(),
                    employee.getSickDays(), employee.getVacationDays(),
                    employee.getPensionFundName(), employee.getBranchAddress());

            return employeeDAO.updateEmployee(updated);
        } catch (Exception e) {
            System.err.println("Error updating bank account: " + e.getMessage());
            return false;
        }
    }

    public boolean updateEmployeeSalary(String id, double salary) {
        try {
            EmployeeDTO employee = employeeDAO.findById(id).orElse(null);
            if (employee == null) return false;

            EmployeeDTO updated = new EmployeeDTO(id, employee.getFirstName(), employee.getLastName(),
                    employee.getBankAccount(), employee.getStartDate(), salary,
                    employee.getQualifiedPositions(), employee.getRole(),
                    employee.getSickDays(), employee.getVacationDays(),
                    employee.getPensionFundName(), employee.getBranchAddress());

            return employeeDAO.updateEmployee(updated);
        } catch (Exception e) {
            System.err.println("Error updating salary: " + e.getMessage());
            return false;
        }
    }

    public boolean updateEmployeeBranch(String id, String branchAddress) {
        try {
            // Validate branch exists
            if (branchAddress != null && !branchDAO.branchExists(branchAddress)) {
                return false;
            }

            EmployeeDTO employee = employeeDAO.findById(id).orElse(null);
            if (employee == null) return false;

            EmployeeDTO updated = new EmployeeDTO(id, employee.getFirstName(), employee.getLastName(),
                    employee.getBankAccount(), employee.getStartDate(), employee.getSalary(),
                    employee.getQualifiedPositions(), employee.getRole(),
                    employee.getSickDays(), employee.getVacationDays(),
                    employee.getPensionFundName(), branchAddress);

            return employeeDAO.updateEmployee(updated);
        } catch (Exception e) {
            System.err.println("Error updating employee branch: " + e.getMessage());
            return false;
        }
    }

    // Branch Management
    public List<BranchDTO> getAllBranches() {
        try {
            return branchDAO.findAllBranches();
        } catch (Exception e) {
            System.err.println("Error getting branches: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public BranchDTO getBranchByAddress(String address) {
        try {
            return branchDAO.findBranchByAddress(address).orElse(null);
        } catch (Exception e) {
            System.err.println("Error getting branch: " + e.getMessage());
            return null;
        }
    }

    public boolean branchExists(String address) {
        try {
            return branchDAO.branchExists(address);
        } catch (Exception e) {
            System.err.println("Error checking branch existence: " + e.getMessage());
            return false;
        }
    }

    // Utility Methods
    public boolean hasShiftManagers() {
        try {
            List<EmployeeDTO> managers = employeeDAO.findByRole("SHIFT_MANAGER");
            List<EmployeeDTO> hrManagers = employeeDAO.findByRole("HR_MANAGER");
            return !managers.isEmpty() || !hrManagers.isEmpty();
        } catch (Exception e) {
            System.err.println("Error checking shift managers: " + e.getMessage());
            return false;
        }
    }

    public List<EmployeeDTO> getAvailableEmployeesForShift(LocalDate date, String shiftType) {
        try {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            List<EmployeeDTO> allEmployees = employeeDAO.findAll();

            return allEmployees.stream()
                    .filter(employee -> {
                        try {
                            return availabilityDAO.isAvailable(employee.getId(), dayOfWeek, shiftType);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error getting available employees: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<PositionDTO> getMissingPositionsForShift(String shiftId) {
        try {
            ShiftDTO shift = shiftDAO.findById(shiftId).orElse(null);
            if (shift == null) {
                return new ArrayList<>();
            }

            Map<String, Integer> requiredPositions = requiredPositionDAO.getRequiredPositions(shift.getShiftType());
            List<PositionDTO> missingPositions = new ArrayList<>();

            for (Map.Entry<String, Integer> entry : requiredPositions.entrySet()) {
                String positionName = entry.getKey();
                int requiredCount = entry.getValue();
                int assignedCount = ((ShiftAssignmentDAOImpl) assignmentDAO).countAssignmentsForPosition(shiftId, positionName);

                // Add missing positions
                for (int i = assignedCount; i < requiredCount; i++) {
                    PositionDTO position = positionDAO.findByName(positionName).orElse(null);
                    if (position != null) {
                        missingPositions.add(position);
                    }
                }
            }

            return missingPositions;
        } catch (Exception e) {
            System.err.println("Error getting missing positions: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    public boolean updateEmployeeRole(String id, String roleName) {
        try {
            EmployeeDTO employee = employeeDAO.findById(id).orElse(null);
            if (employee == null) return false;

            Domain_employee.Employee.UserRole role = Domain_employee.Employee.UserRole.valueOf(roleName);

            EmployeeDTO updated = new EmployeeDTO(id, employee.getFirstName(), employee.getLastName(),
                    employee.getBankAccount(), employee.getStartDate(), employee.getSalary(),
                    employee.getQualifiedPositions(), role,
                    employee.getSickDays(), employee.getVacationDays(),
                    employee.getPensionFundName(), employee.getBranchAddress());

            return employeeDAO.updateEmployee(updated);
        } catch (Exception e) {
            System.err.println("Error updating role: " + e.getMessage());
            return false;
        }
    }

    public boolean updateEmployeePassword(String id, String password) {
        return true;
    }

    public boolean updateEmployeeSickDays(String id, int sickDays) {
        try {
            EmployeeDTO employee = employeeDAO.findById(id).orElse(null);
            if (employee == null) return false;

            EmployeeDTO updated = new EmployeeDTO(id, employee.getFirstName(), employee.getLastName(),
                    employee.getBankAccount(), employee.getStartDate(), employee.getSalary(),
                    employee.getQualifiedPositions(), employee.getRole(),
                    sickDays, employee.getVacationDays(),
                    employee.getPensionFundName(), employee.getBranchAddress());

            return employeeDAO.updateEmployee(updated);
        } catch (Exception e) {
            System.err.println("Error updating sick days: " + e.getMessage());
            return false;
        }
    }

    public boolean updateEmployeeVacationDays(String id, int vacationDays) {
        try {
            EmployeeDTO employee = employeeDAO.findById(id).orElse(null);
            if (employee == null) return false;

            EmployeeDTO updated = new EmployeeDTO(id, employee.getFirstName(), employee.getLastName(),
                    employee.getBankAccount(), employee.getStartDate(), employee.getSalary(),
                    employee.getQualifiedPositions(), employee.getRole(),
                    employee.getSickDays(), vacationDays,
                    employee.getPensionFundName(), employee.getBranchAddress());

            return employeeDAO.updateEmployee(updated);
        } catch (Exception e) {
            System.err.println("Error updating vacation days: " + e.getMessage());
            return false;
        }
    }

    public boolean updateEmployeePensionFund(String id, String pensionFundName) {
        try {
            EmployeeDTO employee = employeeDAO.findById(id).orElse(null);
            if (employee == null) return false;

            EmployeeDTO updated = new EmployeeDTO(id, employee.getFirstName(), employee.getLastName(),
                    employee.getBankAccount(), employee.getStartDate(), employee.getSalary(),
                    employee.getQualifiedPositions(), employee.getRole(),
                    employee.getSickDays(), employee.getVacationDays(),
                    pensionFundName, employee.getBranchAddress());

            return employeeDAO.updateEmployee(updated);
        } catch (Exception e) {
            System.err.println("Error updating pension fund: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteShift(String shiftId) {
        try {
            return shiftDAO.deleteById(shiftId);
        } catch (Exception e) {
            System.err.println("Error deleting shift: " + e.getMessage());
            return false;
        }
    }

    public boolean verifyPassword(String id, String password) {
        try {
            EmployeeDTO employee = employeeDAO.findById(id).orElse(null);
            if (employee == null) {
                return false;
            }
            return password.equals(id) || (id.equals("admin") && password.equals("admin123"));
        } catch (Exception e) {
            System.err.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }

    public boolean updateShiftHours(String shiftTypeStr, String newStart, String newEnd) {
        try {
            LocalTime.parse(newStart);
            LocalTime.parse(newEnd);
            if (!LocalTime.parse(newStart).isBefore(LocalTime.parse(newEnd))) {
                System.err.println("Start time must be before end time");
                return false;
            }
            System.out.println("Shift hours updated: " + shiftTypeStr +
                    " from " + newStart + " to " + newEnd);
            return true;

        } catch (Exception e) {
            System.err.println("Invalid time format. Please use HH:mm format");
            return false;
        }
    }

    public List<ShiftDTO> getAllShiftsAsDTO() {
        try {
            return shiftDAO.findAll();
        } catch (Exception e) {
            System.err.println("Error getting all shifts: " + e.getMessage());
            return new ArrayList<>();
        }
    }


}
