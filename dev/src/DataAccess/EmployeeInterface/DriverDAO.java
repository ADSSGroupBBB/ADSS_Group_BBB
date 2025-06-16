package DataAccess.EmployeeInterface;

import DTO.DriverDTO;
import java.sql.SQLException;
import java.util.List;

public interface DriverDAO {
    List<DriverDTO> findAll() throws SQLException;
    DriverDTO save(DriverDTO driver) throws SQLException;
    boolean deleteById(String id) throws SQLException;
    boolean deleteByIdAndLicense(String id, Integer license) throws SQLException;
    List<DriverDTO> findByDriverId(String driverId) throws SQLException;
    boolean hasLicense(String driverId, Integer license) throws SQLException;
}