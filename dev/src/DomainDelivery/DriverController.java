package DomainDelivery;

import Domain.Driver;

import java.util.List;

public class DriverController extends DeliveriesController {
    // Method to insert a new driver
    public String insertDriver(String id, String name, List<Integer> licenseList) {
        Domain.Driver new_d = new Domain.Driver(id, name, licenseList);

        // Check if the driver already exists
        if (!driversMap.containsKey(id)) {
            driversMap.put(id, new_d);
            return "New driver added: " + new_d.toString();
        } else {
            return "Driver with the same ID already exist.";
        }
    }

    // Method to delete a driver
    public String deleteDriver(String id) {
        if (driversMap.containsKey(id)) {
            Domain.Driver removed = driversMap.remove(id);
            return "Driver removed: " + removed;
        } else {
            return "Driver with ID " + id + " doesn't exist.";
        }
    }

    // Method to delete a license from a driver
    public String deleteLicense(String id, int license) {
        Domain.Driver driver = driversMap.get(id);
        if (driver == null) {
            return "No driver found with ID: " + id;
        }
        List<Integer> licenses = driver.getLicenses_list();

        if (licenses.contains(license)) {
            Integer licenseObj = license;
            licenses.remove(licenseObj); // Removes the license from the list
            return "License removed successfully.";
        } else {
            return "License not found.";
        }
    }

    // Method to add a license to a driver
    public String addLicense(String id, int license) {
        Domain.Driver driver = driversMap.get(id);
        if (driver == null) {
            return "No driver found with ID: " + id;
        }
        List<Integer> licenses = driver.getLicenses_list();
        if (licenses.contains(license)) {
            return "License already exist.";
        } else {
            licenses.add(license);
            return "License added successfully.";
        }
    }

    // Checks if a driver is available for a specific truck
    public String isAvailableDriver(String id, String truckID) {
        if (driversMap.containsKey(id)) {
            Driver driver = driversMap.get(id);
            if (driver.is_available()) {
                Truck truck = trucksMap.get(truckID);
                if (driver.getLicenses_list().contains(truck.getType())) {
                    driver.set_availability(false); // Mark driver as unavailable
                    return "Driver is available."; // Return success message
                }
                return "Driver doesn't have correct license for this truck type"; // Incorrect license
            } else {
                return "Driver is not available for a new delivery"; // Driver unavailable
            }
        } else {
            return "Driver doesn't exist"; // Driver not found
        }
    }
    // Prints all driver id in the system
    public String printDrivers() {
        StringBuilder sb = new StringBuilder();
        for (String key : driversMap.keySet()) {
            sb.append("Driver: ").append(key).append("\n"); // Append address to result
        }
        // Remove the last newline character if the StringBuilder is not empty
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString(); // Return formatted string of addresses
    }
}
