package DataAccessDE.Interface;

import dto.ShipmentItemDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ShipmentItemDAO {
    Optional<ShipmentItemDTO> findByName(String name) throws SQLException;

    List<ShipmentItemDTO> findAll() throws SQLException;
}