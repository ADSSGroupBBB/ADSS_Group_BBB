package DomainD;

import dto.DriverDTO;


import DataAccessDE.EmployeeDAO.DriverDAOImpl;
import DataAccessDE.EmployeeDAO.EmployeeDAOImpl;

import DataAccessDE.EmployeeDAO.ShiftAssignmentDAOImpl;
import DataAccessDE.EmployeeInterface.DriverDAO;
import DataAccessDE.EmployeeInterface.EmployeeDAO;
import DataAccessDE.EmployeeInterface.ShiftAssignmentDAO;
import Domain_employee.Driver;
import Domain_employee.Employee;
import dto.EmployeeDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverController extends DeliveriesController {
    protected static final DriverDAO driverDAO = new DriverDAOImpl();
    protected static final EmployeeDAO employeeDAO = new EmployeeDAOImpl();
    protected final ShiftAssignmentDAO shiftASDAO = new ShiftAssignmentDAOImpl();

    // Adds a new driver license to the system if it doesn't already exist
    public String insertDriverLicense(String id, int license) throws SQLException {
        Driver driver = driversMap.get(id);
        if (driver == null) {
            // Check if driver exists in database but not in map
            List<DriverDTO> driverLicenses = driverDAO.findByDriverId(id);
            if (driverLicenses.isEmpty()) {
                return "No driver found with ID: " + id;
            }
            List<Integer> licenseList = new ArrayList<>();
            for (int i = 0; i < driverLicenses.size(); i++){
                licenseList.add(driverLicenses.get(i).license());
            }
            EmployeeDTO dEmployee = employeeDAO.findById(id).orElse(null);
            assert dEmployee != null;
            Driver newDriver = new Driver(id, dEmployee.getFirstName(), dEmployee.getLastName(), dEmployee.getBankAccount(),
                    dEmployee.getStartDate(), dEmployee.getSalary(), Employee.UserRole.DRIVER, "driver",
                    dEmployee.getSickDays(), dEmployee.getVacationDays(), dEmployee.getPensionFundName(),
                    dEmployee.getBranchAddress(), licenseList);
            newDriver.set_availabilityToDrive(driverLicenses.get(0).on_drive() == 0);

            driversMap.put(id, newDriver);
            driver  = newDriver;
        }

        List<Integer> licenses = driver.getLicenses_list();

        if (licenses.contains(license)) {
            return "Driver already has this license.";
        }

        // Add new license
        licenses.add(license);
        DriverDTO newDriverLicense = new DriverDTO(id, license, 0); // 0 means not on drive
        driverDAO.save(newDriverLicense);
        return "License added to driver " + id + ": " + license;
    }

    // Method to delete a license from a driver
    public String deleteLicense(String id, int license) throws SQLException {
        Driver driver = driversMap.get(id);
        if (driver == null) {
            List<DriverDTO> driverLicenses = driverDAO.findByDriverId(id);
            if (driverLicenses.isEmpty()) {
                return "No driver found with ID: " + id;
            }
            List<Integer> licenseList = new ArrayList<>();
            for (int i = 0; i < driverLicenses.size(); i++){
                licenseList.add(driverLicenses.get(i).license());
            }
            EmployeeDTO dEmployee = employeeDAO.findById(id).orElse(null);
            assert dEmployee != null;
            Driver newDriver = new Driver(id, dEmployee.getFirstName(), dEmployee.getLastName(), dEmployee.getBankAccount(),
                    dEmployee.getStartDate(), dEmployee.getSalary(), Employee.UserRole.DRIVER, "driver",
                    dEmployee.getSickDays(), dEmployee.getVacationDays(), dEmployee.getPensionFundName(),
                    dEmployee.getBranchAddress(), licenseList);
            newDriver.set_availabilityToDrive(driverLicenses.get(0).on_drive() == 0);

            driversMap.put(id, newDriver);
            driver  = newDriver;
        }
        List<Integer> licenses = driver.getLicenses_list();

        if (licenses.contains(license)) {
            Integer licenseObj = license;
            licenses.remove(licenseObj); // Removes the license from the list

            // Remove from database - delete specific license record
            driverDAO.deleteByIdAndLicense(id, license);


            return "License removed successfully.";
        } else {
            return "License not found.";
        }
    }


    // Checks if a driver is available for a specific truck
    public String isAvailableDriver(String id, String truckID, String shiftID) throws SQLException {
        Truck truck = trucksMap.get(truckID);
        if (truck == null) {
            return "Truck with ID " + truckID + " doesn't exist";
        }
        Driver driver = driversMap.get(id);
        if (driver == null) {
            List<DriverDTO> driverLicenses = driverDAO.findByDriverId(id);
            if (driverLicenses.isEmpty()) {
                return "No driver found with ID: " + id;
            }
            List<Integer> licenseList = new ArrayList<>();
            for (int i = 0; i < driverLicenses.size(); i++){
                licenseList.add(driverLicenses.get(i).license());
            }
            EmployeeDTO dEmployee = employeeDAO.findById(id).orElse(null);
            assert dEmployee != null;
            Driver newDriver = new Driver(id, dEmployee.getFirstName(), dEmployee.getLastName(), dEmployee.getBankAccount(),
                    dEmployee.getStartDate(), dEmployee.getSalary(), Employee.UserRole.DRIVER, "driver",
                    dEmployee.getSickDays(), dEmployee.getVacationDays(), dEmployee.getPensionFundName(),
                    dEmployee.getBranchAddress(), licenseList);
            newDriver.set_availabilityToDrive(driverLicenses.get(0).on_drive() == 0);

            driversMap.put(id, newDriver);
            driver  = newDriver;
        }

        if (driver.is_available()) {
            if (shiftASDAO.isEmployeeAssigned(shiftID, id)){
                if (driver.getLicenses_list().contains(truck.getType())) {
                    driver.set_availabilityToDrive(false); // Mark driver as unavailable

                    // Update all driver licenses in database to reflect on_drive status
                    List<DriverDTO> driverLicenses = driverDAO.findByDriverId(id);
                    for (DriverDTO driverLicense : driverLicenses) {
                        DriverDTO updatedDriver = new DriverDTO(id, driverLicense.license(), 1); // 1 means on drive
                        driverDAO.save(updatedDriver);
                    }

                    return "Driver have correct license and is ready to drive."; // Return success message
                }
                return "Driver doesn't have correct license for this truck type"; // Incorrect license
            }
            return "Driver is not in the origin of the destination."; // Driver unavailable
        } else {
            return "Driver is already in delivery"; // Driver unavailable

        }
    }

    // Sets driver as available again (when delivery ends)
    public String setDriverAvailable(String id) throws SQLException {
        if (driversMap.containsKey(id)) {
            Driver driver = driversMap.get(id);
            driver.set_availabilityToDrive(true); // Mark driver as available

            // Update all driver licenses in database to reflect available status
            List<DriverDTO> driverLicenses = driverDAO.findByDriverId(id);
            for (DriverDTO driverLicense : driverLicenses) {
                DriverDTO updatedDriver = new DriverDTO(id, driverLicense.license(), 0); // 0 means not on drive
                driverDAO.save(updatedDriver);
            }

            return "Driver " + id + " is now available.";
        } else {
            return "Driver with ID " + id + " doesn't exist in memory.";
        }
    }

    // Prints all drivers in the system
    public String printDrivers() throws SQLException {
        StringBuilder sb = new StringBuilder();
        List<DriverDTO> list = driverDAO.findAll();
        String lastId = "";
        // Load drivers from database to map if not already present
        for (DriverDTO dto : list) {
            if (!dto.id().equals(lastId)) {
                if (dto.on_drive() == 0){
                    sb.append("Driver: ").append(dto.id()).append(" ").append(dto.license()).append(" free to drive\n");
                }
                else {
                    sb.append("Driver: ").append(dto.id()).append(" ").append(dto.license()).append(" currently on another delivery\n");
                }
                lastId = dto.id();
            } else {
                sb.append("Additional license: ").append(dto.license()).append("\n");
            }
        }
        // Remove the last newline character if the StringBuilder is not empty
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString(); // Return formatted string of drivers
    }
}