package DataAccessDelivery.Interface;

import DTO.DriverDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DriverDAO {
    Optional<DriverDTO> findById(int id) throws SQLException;
    List<DriverDTO> findAll() throws SQLException;
    DriverDTO save(DriverDTO driver) throws SQLException;
}