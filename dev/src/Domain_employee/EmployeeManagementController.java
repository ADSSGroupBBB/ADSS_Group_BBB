package Domain_employee;

import DTO.*;
import DataAccess.EmployeeInterface.*;
import DataAccess.EmployeeDAO.*;
import Domain_employee.Employee.UserRole;

import java.time.LocalDate;
import java.util.*;

/**
 * Controller responsible for employee management operations.
 * Handles employee CRUD operations, role management, driver management, and basic employee data.
 * Now includes all driver-related functionality for unified employee management.
 */
public class EmployeeManagementController {
    private final EmployeeDAO employeeDAO;
    private final BranchDAO branchDAO;
    private final AvailabilityDAO availabilityDAO;
    private final DriverDAO driverDAO;
    private static Map<String, Driver> driversMap = new HashMap<>();

    public EmployeeManagementController() {
        this.employeeDAO = new EmployeeDAOImpl();
        this.branchDAO = new BranchDAOImpl();
        this.availabilityDAO = new AvailabilityDAOImpl();
        this.driverDAO = new DriverDAOImpl();
    }

    // Basic Employee Management
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

    public boolean addStoreKeeper(String id, String firstName, String lastName, String bankAccount,
                                  LocalDate startDate, double salary, int sickDays, int vacationDays,
                                  String pensionFundName, String branchAddress) {
        try {
            // Validate branch exists
            if (branchAddress != null && !branchDAO.branchExists(branchAddress)) {
                return false;
            }

            EmployeeDTO employee = new EmployeeDTO(id, firstName, lastName, bankAccount,
                    startDate, salary, new ArrayList<>(), UserRole.STORE_KEEPER,
                    sickDays, vacationDays, pensionFundName, branchAddress);

            employeeDAO.save(employee);

            // Set default availability
            availabilityDAO.setDefaultAvailability(id);

            return true;
        } catch (Exception e) {
            System.err.println("Error adding store keeper: " + e.getMessage());
            return false;
        }
    }

    // ===== DRIVER MANAGEMENT - Merged from EmployeeDriverController =====

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

            // Add to drivers map (for delivery module compatibility)
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

    public boolean removeDriver(String id) {
        try {
            if (driversMap.containsKey(id)) {
                driversMap.remove(id);
                driverDAO.deleteById(id);
                return employeeDAO.deleteById(id);
            } else {
                if (driverDAO.deleteById(id)) {
                    return employeeDAO.deleteById(id);
                }
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error removing driver: " + e.getMessage());
            return false;
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

    // Driver map management for delivery module compatibility
    public static Map<String, Driver> getDriversMap() {
        return driversMap;
    }

    public static void addDriverToMap(String id, Driver driver) {
        driversMap.put(id, driver);
    }

    public static void removeDriverFromMap(String id) {
        driversMap.remove(id);
    }

    public static boolean driverExistsInMap(String id) {
        return driversMap.containsKey(id);
    }

    public static Driver getDriverFromMap(String id) {
        return driversMap.get(id);
    }

    // ===== END DRIVER MANAGEMENT =====

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

    public boolean updateEmployeeRole(String id, String roleName) {
        try {
            EmployeeDTO employee = employeeDAO.findById(id).orElse(null);
            if (employee == null) return false;

            UserRole role = UserRole.valueOf(roleName);

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
        // Password functionality can be implemented here if needed
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
}