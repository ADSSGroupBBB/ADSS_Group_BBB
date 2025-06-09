package Domain_employee;

import DTO.*;
import DataAccess.EmployeeInterface.*;
import DataAccess.EmployeeDAO.*;
import Domain_employee.Employee.UserRole;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/**
 * Controller responsible for EMPLOYEE-SIDE driver operations only.
 * Handles driver creation as employees, NOT delivery operations.
 */
public class EmployeeDriverController {
    private final EmployeeDAO employeeDAO;
    private final DriverDAO driverDAO;
    private final BranchDAO branchDAO;
    private final AvailabilityDAO availabilityDAO;
    private static Map<String, Driver> driversMap = new HashMap<>();

    public EmployeeDriverController() {
        this.employeeDAO = new EmployeeDAOImpl();
        this.driverDAO = new DriverDAOImpl();
        this.branchDAO = new BranchDAOImpl();
        this.availabilityDAO = new AvailabilityDAOImpl();
    }

    public boolean addDriver(String id, String firstName, String lastName, String bankAccount,
                             LocalDate startDate, double salary, int sickDays, int vacationDays,
                             String pensionFundName, String branchAddress, List<Integer> licenseList) {
        try {
            // Check if driver already exists in the drivers map
            if (driversMap.containsKey(id)) {
                System.err.println("Driver with ID " + id + " already exists.");
                return false;
            }

            // Validate branch exists
            if (branchAddress != null && !branchDAO.branchExists(branchAddress)) {
                return false;
            }

            EmployeeDTO employee = new EmployeeDTO(id, firstName, lastName, bankAccount,
                    startDate, salary, new ArrayList<>(), UserRole.DRIVER,
                    sickDays, vacationDays, pensionFundName, branchAddress);

            employeeDAO.save(employee);
            availabilityDAO.setDefaultAvailability(id);

            List<DriverDTO> optionalDriver = driverDAO.findByDriverId(id);
            if (!optionalDriver.isEmpty()) {
                return false;
            }

            // Save each license for the driver
            for (Integer license : licenseList) {
                DriverDTO driver = new DriverDTO(id, license, 0); // 0 means not on drive
                driverDAO.save(driver);
            }

            // Add to drivers map (if still needed for business logic)
            Driver domainDriver = new Driver(id, firstName, lastName, bankAccount,
                    startDate, salary, UserRole.DRIVER, "driver",
                    sickDays, vacationDays, pensionFundName,
                    branchAddress, licenseList);
            driversMap.put(id, domainDriver);

            return true;
        } catch (Exception e) {
            System.err.println("Error adding driver: " + e.getMessage());
            return false;
        }
    }

    public boolean removeDriver(String id) throws SQLException {
        if (driversMap.containsKey(id)) {
            driversMap.remove(id); // Remove driver from the map
            driverDAO.deleteById(id);
            return employeeDAO.deleteById(id);
        } else {
            if (driverDAO.deleteById(id)) {
                return employeeDAO.deleteById(id);
            }
            return false; // Driver not found
        }
    }

    public List<DriverDTO> getDriverLicenses(String driverId) {
        try {
            return driverDAO.findByDriverId(driverId);
        } catch (Exception e) {
            System.err.println("Error getting driver licenses: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Get the drivers map for backward compatibility with delivery module
     */
    public static Map<String, Driver> getDriversMap() {
        return driversMap;
    }

    /**
     * Add driver to map (for delivery module integration)
     */
    public static void addDriverToMap(String id, Driver driver) {
        driversMap.put(id, driver);
    }

    /**
     * Remove driver from map (for delivery module integration)
     */
    public static void removeDriverFromMap(String id) {
        driversMap.remove(id);
    }

    /**
     * Check if driver exists in map (for delivery module integration)
     */
    public static boolean driverExistsInMap(String id) {
        return driversMap.containsKey(id);
    }

    /**
     * Get driver from map (for delivery module integration)
     */
    public static Driver getDriverFromMap(String id) {
        return driversMap.get(id);
    }
}