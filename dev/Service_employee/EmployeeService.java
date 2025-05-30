package Service_employee;

import Domain_employee.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service layer for handling employee-related operations.
 * Acts as a bridge between the controller layer and the domain layer.
 */
public class EmployeeService {
    private final IEmployeeManager employeeManager;

    /**
     * Constructor initializes the service with employeeManager from factory.
     */
    public EmployeeService() {
        this.employeeManager = EmployeeManagerFactory.getEmployeeManager();
    }

    // EmployeeDTO operations
    public EmployeeDTO getEmployee(String employeeId) {
        Employee employee = employeeManager.getEmployee(employeeId);
        if (employee == null) {
            return null;
        }
        return convertEmployeeToDTO(employee);
    }

    public boolean addNewEmployee(String id, String firstName, String lastName, String bankAccount,
                                  LocalDate startDate, double salary,
                                  int sickDays, int vacationDays, String pensionFundName) {Employee employee = new Employee(id, firstName, lastName, bankAccount, startDate, salary,
            Employee.UserRole.REGULAR_EMPLOYEE, "", sickDays, vacationDays, pensionFundName);
        return employeeManager.addEmployee(employee);
    }

    public boolean addNewEmployee(String id, String firstName, String lastName, String bankAccount,
                                  LocalDate startDate, double salary, String role, String password,
                                  int sickDays, int vacationDays, String pensionFundName) {
        try {
            Employee.UserRole userRole = Employee.UserRole.valueOf(role);
            Employee employee = new Employee(id, firstName, lastName, bankAccount, startDate, salary,
                    userRole, password, sickDays, vacationDays, pensionFundName);
            return employeeManager.addEmployee(employee);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean removeEmployee(String employeeId) {
        return employeeManager.removeEmployee(employeeId) != null;
    }

    public EmployeeDTO getEmployeeDetails(String employeeId) {
        Employee employee = employeeManager.getEmployee(employeeId);
        if (employee == null) {
            return null;
        }
        return convertEmployeeToDTO(employee);
    }

    public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeDTO> result = new ArrayList<>();

        for (Employee employee : employeeManager.getAllEmployees()) {
            result.add(convertEmployeeToDTO(employee));
        }

        return result;
    }

    public EmployeeDTO convertEmployeeToDTO(Employee employee) {
        List<String> qualifiedPositions = employee.getQualifiedPositions().stream()
                .map(Position::getName)
                .collect(Collectors.toList());

        return new EmployeeDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getBankAccount(),
                employee.getStartDate(),
                employee.getSalary(),
                qualifiedPositions,
                employee.getRole(),
                employee.getSickDays(),
                employee.getVacationDays(),
                employee.getPensionFundName()
        );
    }

    public boolean verifyPassword(String employeeId, String password) {
        Employee employee = employeeManager.getEmployee(employeeId);
        if (employee == null) {
            return false;
        }

        return employee.getPassword().equals(password);
    }

    public boolean updateEmployeeFirstName(String employeeId, String firstName) {
        Employee employee = employeeManager.getEmployee(employeeId);
        if (employee == null) {
            return false;
        }
        employee.setFirstName(firstName);
        return true;
    }

    public boolean updateEmployeeLastName(String employeeId, String lastName) {
        Employee employee = employeeManager.getEmployee(employeeId);
        if (employee == null) {
            return false;
        }
        employee.setLastName(lastName);
        return true;
    }

    public boolean updateEmployeeBankAccount(String employeeId, String bankAccount) {
        Employee employee = employeeManager.getEmployee(employeeId);
        if (employee == null) {
            return false;
        }
        employee.setBankAccount(bankAccount);
        return true;
    }

    public boolean updateEmployeeSalary(String employeeId, double salary) {
        Employee employee = employeeManager.getEmployee(employeeId);
        if (employee == null) {
            return false;
        }
        employee.setSalary(salary);
        return true;
    }

    public boolean updateEmployeeRole(String employeeId, String roleName) {
        Employee employee = employeeManager.getEmployee(employeeId);
        if (employee == null) {
            return false;
        }

        try {
            Employee.UserRole role = Employee.UserRole.valueOf(roleName);
            employee.setRole(role);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean updateEmployeePassword(String employeeId, String password) {
        Employee employee = employeeManager.getEmployee(employeeId);
        if (employee == null) {
            return false;
        }

        employee.setPassword(password);
        return true;
    }

    public boolean updateEmployeeAvailabilityForNextWeek(String employeeId, DayOfWeek dayOfWeek, boolean morningAvailable, boolean eveningAvailable) {
        Employee employee = employeeManager.getEmployee(employeeId);
        if (employee == null) {
            return false;
        }
        employee.getAvailability().updateAvailability(dayOfWeek, morningAvailable, eveningAvailable);
        return true;
    }

    public boolean isEmployeeAvailableForNextWeek(String employeeId, DayOfWeek dayOfWeek, String shiftType) {
        Employee employee = employeeManager.getEmployee(employeeId);
        if (employee == null) {
            return false;
        }
        ShiftType type = ShiftType.valueOf(shiftType);
        return employee.getAvailability().isAvailable(dayOfWeek, type);
    }

    private boolean canUpdateNextWeekAvailability() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        return today.getValue() <= DayOfWeek.THURSDAY.getValue();
    }

    public boolean updateEmployeeAvailability(String employeeId, DayOfWeek dayOfWeek, boolean morningAvailable, boolean eveningAvailable) {
        return employeeManager.updateEmployeeAvailability(employeeId, dayOfWeek, morningAvailable, eveningAvailable);
    }

    public boolean isEmployeeAvailable(String employeeId, DayOfWeek dayOfWeek, String shiftType) {
        Employee employee = employeeManager.getEmployee(employeeId);
        if (employee == null) {
            return false;
        }

        ShiftType type = ShiftType.valueOf(shiftType);
        return employee.getAvailability().isAvailable(dayOfWeek, type);
    }

    public boolean addPosition(String name, boolean isShiftManagerRole) {
        Position position = new Position(name, isShiftManagerRole);
        return employeeManager.addPosition(position);
    }

    public PositionDTO getPositionDetails(String positionName) {
        Position position = employeeManager.getPosition(positionName);
        if (position == null) {
            return null;
        }

        return new PositionDTO(position.getName(), position.isRequiresShiftManager());
    }

    public List<PositionDTO> getAllPositions() {
        List<PositionDTO> result = new ArrayList<>();

        for (Position position : employeeManager.getAllPositions()) {
            result.add(new PositionDTO(position.getName(), position.isRequiresShiftManager()));
        }

        return result;
    }

    public boolean addQualificationToEmployee(String employeeId, String positionName) {
        return employeeManager.addQualificationToEmployee(employeeId, positionName);
    }

    public List<EmployeeDTO> getQualifiedEmployeesForPosition(String positionName) {
        Position position = employeeManager.getPosition(positionName);
        if (position == null) {
            return new ArrayList<>();
        }

        List<EmployeeDTO> result = new ArrayList<>();
        for (Employee employee : employeeManager.getQualifiedEmployeesForPosition(position)) {
            result.add(convertEmployeeToDTO(employee));
        }

        return result;
    }

    public List<EmployeeDTO> getAccessibleEmployees(String employeeId) {
        Employee employee = employeeManager.getEmployee(employeeId);
        if (employee == null) {
            return new ArrayList<>();
        }

        List<EmployeeDTO> result = new ArrayList<>();

        if (employee.isManager()) {
            return getAllEmployees();
        } else {
            result.add(convertEmployeeToDTO(employee));
        }

        return result;
    }

    public boolean addRequiredPosition(String shiftType, String positionName, int count) {
        ShiftType type = ShiftType.valueOf(shiftType.toUpperCase());
        return employeeManager.addRequiredPosition(type, positionName, count);
    }

    public List<EmployeeDTO> getAvailableEmployeesForShift(LocalDate date, String shiftType) {
        ShiftType type = ShiftType.valueOf(shiftType);
        List<Employee> availableEmployees = employeeManager.getAvailableEmployeesForShift(date, type);

        List<EmployeeDTO> result = new ArrayList<>();
        for (Employee employee : availableEmployees) {
            result.add(convertEmployeeToDTO(employee));
        }

        return result;
    }

    public ShiftDTO createShift(LocalDate date, String shiftType) {
        ShiftType type = ShiftType.valueOf(shiftType);
        Shift shift = employeeManager.createShift(date, type);
        if (shift == null) {
            return null;
        }

        return convertShiftToDTO(shift);
    }

//    public boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName) {
//        Shift shift = employeeManager.getShift(shiftId);
//        Employee employee = employeeManager.getEmployee(employeeId);
//        Position position = employeeManager.getPosition(positionName);
//
//        if (shift == null || employee == null || position == null) {
//            return false;
//        }
//
//        RequiredPositions requiredPositions = employeeManager.getRequiredPositions();
//        int requiredCount = requiredPositions.getRequiredCount(shift.getShiftType(), position);
//
//        if (requiredCount == 0) {
//            return false;
//        }
//
//        long currentAssigned = shift.getAllAssignedEmployees().entrySet().stream()
//                .filter(entry -> entry.getKey().equals(position))
//                .count();
//
//        if (currentAssigned >= requiredCount) {
//            return false;
//        }
//
//        if (!employee.isQualifiedFor(position)) {
//            employee.addQualifiedPosition(position);
//        }
//
//        DayOfWeek dayOfWeek = shift.getDate().getDayOfWeek();
//        if (!employee.getAvailability().isAvailable(dayOfWeek, shift.getShiftType())) {
//            employee.getAvailability().updateAvailability(dayOfWeek, true, true);
//        }
//
//        return employeeManager.assignEmployeeToShift(shiftId, employeeId, positionName);
//    }

    public boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName) {
        return employeeManager.assignEmployeeToShift(shiftId, employeeId, positionName);
    }

    public boolean removeAssignmentFromShift(String shiftId, String positionName) {
        return employeeManager.removeAssignmentFromShift(shiftId, positionName);
    }

    public boolean areAllRequiredPositionsCovered(String shiftId) {
        return employeeManager.areAllRequiredPositionsCovered(shiftId);
    }

    private ShiftDTO convertShiftToDTO(Shift shift) {
        // Get shift manager info
        String managerId = null;
        String managerName = null;
        if (shift.getShiftManager() != null) {
            managerId = shift.getShiftManager().getId();
            managerName = shift.getShiftManager().getFullName();
        }

        // Build assignments map (position name -> employee name)
        Map<String, String> assignments = new HashMap<>();
        for (Map.Entry<Position, Employee> entry : shift.getAllAssignedEmployees().entrySet()) {
            assignments.put(entry.getKey().getName(), entry.getValue().getFullName());
        }

        return new ShiftDTO(
                shift.getId(),
                shift.getDate(),
                shift.getShiftType().toString(),
                shift.getStartTime(),
                shift.getEndTime(),
                managerId,
                managerName,
                assignments
        );
    }

    public List<ShiftDTO> getAllShiftsAsDTO() {
        List<ShiftDTO> result = new ArrayList<>();
        for (Shift shift : employeeManager.getAllShifts()) {
            result.add(convertShiftToDTO(shift));
        }
        return result;
    }

    public boolean updateShiftHours(String shiftTypeStr, String newStart, String newEnd) {
        ShiftType shiftType;
        if ("MORNING".equalsIgnoreCase(shiftTypeStr)) {
            shiftType = ShiftType.MORNING;
        } else if ("EVENING".equalsIgnoreCase(shiftTypeStr)) {
            shiftType = ShiftType.EVENING;
        } else {
            return false;
        }

        return employeeManager.updateShiftHours(shiftType, newStart, newEnd);
    }

    public boolean isEmployeeAlreadyAssignedToShift(String shiftId, String employeeId) {
        Shift shift = employeeManager.getShift(shiftId);
        Employee employee = employeeManager.getEmployee(employeeId);

        if (shift == null || employee == null) {
            return false;
        }

        return shift.getAllAssignedEmployees().containsValue(employee);
    }

    public int getRequiredPositionsCount(String shiftTypeStr, String positionName) {
        ShiftType shiftType = ShiftType.valueOf(shiftTypeStr.toUpperCase());
        Position position = employeeManager.getPosition(positionName);
        if (position == null) {
            return 0;
        }
        return employeeManager.getRequiredPositions().getRequiredCount(shiftType, position);
    }

    public boolean removeQualificationFromEmployee(String employeeId, String positionName) {
        Employee employee = employeeManager.getEmployee(employeeId);
        Position position = employeeManager.getPosition(positionName);

        if (employee == null || position == null) {
            return false;
        }

        return employee.removeQualifiedPosition(position);
    }

    public boolean updateEmployeeSickDays(String id, int sickDays) {
        Employee employee = employeeManager.getEmployee(id);
        if (employee == null) {
            return false;
        }
        employee.setSickDays(sickDays);
        return true;
    }

    public boolean updateEmployeeVacationDays(String id, int vacationDays) {
        Employee employee = employeeManager.getEmployee(id);
        if (employee == null) {
            return false;
        }
        employee.setVacationDays(vacationDays);
        return true;
    }

    public boolean updateEmployeePensionFund(String id, String pensionFundName) {
        Employee employee = employeeManager.getEmployee(id);
        if (employee == null) {
            return false;
        }
        employee.setPensionFundName(pensionFundName);
        return true;
    }

    public boolean hasShiftManagers() {
        for (Employee employee : employeeManager.getAllEmployees()) {
            if (employee.isShiftManager() || employee.isHRManager()) {
                return true;
            }
        }
        return false;
    }

    public boolean deleteShift(String shiftId) {
        return employeeManager.deleteShift(shiftId);
    }
}