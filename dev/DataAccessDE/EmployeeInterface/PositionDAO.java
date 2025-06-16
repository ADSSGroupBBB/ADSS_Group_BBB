package DataAccessDE.EmployeeInterface;

import dto.PositionDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PositionDAO {
    Optional<PositionDTO> findByName(String name) throws SQLException;
    List<PositionDTO> findAll() throws SQLException;
    PositionDTO save(PositionDTO position) throws SQLException;
    boolean deleteByName(String name) throws SQLException;
}
