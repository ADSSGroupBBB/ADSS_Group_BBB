package Domain_employee;

import DTO.EmployeeDTO;
import DataAccess.EmployeeInterface.*;
import DataAccess.EmployeeDAO.*;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller responsible for employee availability management.
 * Handles availability updates and queries for scheduling purposes.
 */
public class AvailabilityManagementController {
    private final AvailabilityDAO availabilityDAO;
    private final EmployeeDAO employeeDAO;

    public AvailabilityManagementController() {
        this.availabilityDAO = new AvailabilityDAOImpl();
        this.employeeDAO = new EmployeeDAOImpl();
    }

    public void setDefaultAvailability(String id) throws SQLException {
        // Set default availability - available for all shifts
        availabilityDAO.setDefaultAvailability(id);
    }

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

    public List<EmployeeDTO> getAvailableEmployeesForShiftByBranch(LocalDate date, String shiftType, String branchAddress) {
        try {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            List<EmployeeDTO> branchEmployees = employeeDAO.findByBranch(branchAddress);

            return branchEmployees.stream()
                    .filter(employee -> {
                        try {
                            return availabilityDAO.isAvailable(employee.getId(), dayOfWeek, shiftType);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error getting available employees by branch: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean isEmployeeAvailableForDate(String employeeId, LocalDate date, String shiftType) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return isEmployeeAvailable(employeeId, dayOfWeek, shiftType);
    }

    public Map<String, Boolean> getEmployeeWeeklyAvailability(String employeeId, String shiftType) {
        Map<String, Boolean> weeklyAvailability = new HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            boolean available = isEmployeeAvailable(employeeId, day, shiftType);
            weeklyAvailability.put(day.toString(), available);
        }

        return weeklyAvailability;
    }

    public List<EmployeeDTO> getUnavailableEmployeesForShift(LocalDate date, String shiftType) {
        try {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            List<EmployeeDTO> allEmployees = employeeDAO.findAll();

            return allEmployees.stream()
                    .filter(employee -> {
                        try {
                            return !availabilityDAO.isAvailable(employee.getId(), dayOfWeek, shiftType);
                        } catch (Exception e) {
                            return true;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error getting unavailable employees: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}