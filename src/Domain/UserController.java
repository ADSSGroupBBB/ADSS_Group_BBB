package Domain;

import java.util.*;

public class UserController {

    Map<String, Driver> driversMap = new HashMap<>();

    public static void initBaseData(){

    }
    public void insertDriver (String id, String name, List<Integer> licenseList) {
        Driver new_d = new Driver(id, name, licenseList);

        if (!driversMap.containsKey(id)) {
            driversMap.put(id, new_d);
            System.out.print("New driver added: " + new_d.toString());
        } else {
            System.out.print("This driver already exist.");
        }
    }
    public void deleteDriver(String id) {
        if (driversMap.containsKey(id)) {
            Driver removed = driversMap.remove(id);
            System.out.println("Driver removed: " + removed);
        } else {
            System.out.println("Driver with ID " + id + " dont exist.");
        }
    }
    public void deleteLicense(String id, int license) {
        Driver driver = driversMap.get(id);
        if (driver == null) {
            System.out.println("No driver found with ID: " + id);
            return;
        }
        List<Integer> licenses = driver.getLicenses_list();

        if (licenses.contains(license)) {
            licenses.remove(license); // Removes the license from the list
            System.out.println("License removed successfully.");
        } else {
            System.out.println("License not found.");
        }
    }
    public void addLicense(String id, int license) {
        Driver driver = driversMap.get(id);
        if (driver == null) {
            System.out.println("No driver found with ID: " + id);
            return;
        }
        List<Integer> licenses = driver.getLicenses_list();
        if (licenses.contains(license)) {
            System.out.println("License already exist.");
            licenses.remove(license); // Removes the license from the list
            System.out.println("License removed successfully.");
        } else {
            licenses.add(license); // Removes the license from the list
            System.out.println("License added successfully.");
        }
    }

}
