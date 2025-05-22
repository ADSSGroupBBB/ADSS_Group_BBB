package ServiceDelivery;

import java.util.List;
import DomainDelivery.DriverController;

public class DriversApplication {
    private static DriverController dc = new DriverController();
    // Method to insert a new driver
    public String insertDriver(String id, String name, List<Integer> licenseList) {
        return dc.insertDriver(id, name, licenseList); // Delegate to UserController to insert the driver
    }

    // Method to delete a driver by ID
    public String deleteDriver(String id) {
        return dc.deleteDriver(id); // Delegate to UserController to delete the driver
    }

    // Method to delete a specific license for a driver
    public String deleteLicense(String id, int license) {
        return dc.deleteLicense(id, license); // Delegate to UserController to delete the license
    }

    // Method to add a new license to a driver
    public String addLicense(String id, int license) {
        return dc.addLicense(id, license); // Delegate to UserController to add the license
    }
    // Method to check if a driver is available for a specific truck
    public String isAvailableDriver(String id, String truckID) {
        return dc.isAvailableDriver(id, truckID); // Delegate to UserController to check driver availability
    }
    // Method to print all current driver IDs
    public String printDrivers() {
        return dc.printDrivers(); // Delegate to DriverController to print the IDs
    }
}
