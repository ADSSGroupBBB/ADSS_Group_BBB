package DataAccess.Interface;

import DTO.TruckDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TruckDAO {
    Optional<TruckDTO> findById(String id) throws SQLException;
    List<TruckDTO> findAll() throws SQLException;
    TruckDTO save(TruckDTO truck) throws SQLException;
    boolean deleteById(String truckId) throws SQLException;
}