package Service;
import Domain.*;

import java.util.List;

public class UserApplication {
    UserController uc = new UserController();
    public void insertDriver(String id, String name, List<Integer> licenseList) {
        uc.insertDriver(id, name, licenseList);
    }
    public void deleteDriver(String id) {
        uc.deleteDriver(id);
    }
    public void deleteLicense(String id,int license) {
        uc.deleteLicense(id, license);
    }
    public void addLicense(String id, int license) {
        uc.addLicense(id, license);
    }
}
