package ServiceD;

import java.sql.SQLException;
import DomainD.DriverController;

public class DriversApplication {
    private static DriverController dc = new DriverController();

    // Method to delete a specific license for a driver
    public String deleteLicense(String id, int license) throws SQLException {
        return dc.deleteLicense(id, license); // Delegate to UserController to delete the license
    }

    // Method to add a new license to a driver
    public String addLicense(String id, int license) throws SQLException {
        return dc.insertDriverLicense(id, license); // Delegate to UserController to add the license
    }
    // Method to check if a driver is available for a specific truck
    public String isAvailableDriver(String id, String truckID, String shiftID) throws SQLException {
        return dc.isAvailableDriver(id, truckID, shiftID); // Delegate to UserController to check driver availability
    }
    public String setDriverAvailable(String id) throws SQLException {
        return dc.setDriverAvailable(id);
    }
    // Method to print all current driver IDs
    public String printDrivers() throws SQLException {
        return dc.printDrivers(); // Delegate to DriverController to print the IDs
    }
}