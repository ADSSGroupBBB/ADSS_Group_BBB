package Service;

import Domain.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for employee-related operations
 * Acts as a mediator between the presentation layer and domain layer
 */
public class EmployeeService {
    private final EmployeeManager employeeManager;

    public EmployeeService() {
        this.employeeManager = EmployeeManager.getInstance();
    }

    public EmployeeDTO getEmployee(String employeeId) {
        Employee employee = employeeManager.getEmployee(employeeId);
        if (employee == null) {
            return null;
        }
        return convertEmployeeToDTO(employee);
    }

    public boolean addNewEmployee(String id, String firstName, String lastName, String bankAccount,
                                  LocalDate startDate, double salary) {
        Employee employee = new Employee(id, firstName, lastName, bankAccount, startDate, salary);
        return employeeManager.addEmployee(employee);
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

    private EmployeeDTO convertEmployeeToDTO(Employee employee) {
        List<String> qualifiedPositions = employee.getQualifiedPositions().stream().map(Position::getName).collect(Collectors.toList());

        return new EmployeeDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getBankAccount(),
                employee.getStartDate(),
                employee.getSalary(),
                qualifiedPositions
        );
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

    public ShiftDTO getShift(LocalDate date, String shiftType) {
        ShiftType type = ShiftType.valueOf(shiftType);
        Shift shift = employeeManager.getShift(date, type);
        if (shift == null) {
            return null;
        }

        return convertShiftToDTO(shift);
    }

    public ShiftDTO createShift(LocalDate date, String shiftType) {
        ShiftType type = ShiftType.valueOf(shiftType);
        Shift shift = employeeManager.createShift(date, type);
        if (shift == null) {
            return null;
        }

        return convertShiftToDTO(shift);
    }



    public boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName) {
        // וודא שכל האובייקטים קיימים
        Shift shift = employeeManager.getShift(shiftId);
        Employee employee = employeeManager.getEmployee(employeeId);
        Position position = employeeManager.getPosition(positionName);

        if (shift == null || employee == null || position == null) {
            return false;
        }

        // בדוק אם העובד לא מוסמך ואם כן, הוסף הסמכה
        if (!employee.isQualifiedFor(position)) {
            employee.addQualifiedPosition(position);
        }

        // בדוק זמינות ועדכן אם צריך
        DayOfWeek dayOfWeek = shift.getDate().getDayOfWeek();
        if (!employee.getAvailability().isAvailable(dayOfWeek, shift.getShiftType())) {
            employee.getAvailability().updateAvailability(dayOfWeek, true, true);
        }

        // עכשיו העבר למנהל
        return employeeManager.assignEmployeeToShift(shiftId, employeeId, positionName);
    }

    public boolean removeAssignmentFromShift(String shiftId, String positionName) {
        return employeeManager.removeAssignmentFromShift(shiftId, positionName);
    }


    public boolean areAllRequiredPositionsCovered(String shiftId) {
        return employeeManager.areAllRequiredPositionsCovered(shiftId);
    }

    private ShiftDTO convertShiftToDTO(Shift shift) { // Helper method to convert Shift domain object to ShiftDTO
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
}