package DataAccessDE.Interface;

import dto.LocationDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface LocationDAO {
    Optional<LocationDTO> findByAddress(String address) throws SQLException;
    List<LocationDTO> findAll() throws SQLException;
    LocationDTO save(LocationDTO location) throws SQLException;
    boolean deleteByAddress(String address) throws SQLException;
}