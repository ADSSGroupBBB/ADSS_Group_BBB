package DataAccess.Interface;

import DTO.ShipmentItemDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ShipmentItemDAO {
    Optional<ShipmentItemDTO> findByName(String name) throws SQLException;

    List<ShipmentItemDTO> findAll() throws SQLException;

    ShipmentItemDTO save(ShipmentItemDTO item) throws SQLException;

    boolean deleteByName(String name) throws SQLException;
}