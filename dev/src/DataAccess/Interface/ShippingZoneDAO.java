package DataAccess.Interface;

import DTO.ShippingZoneDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ShippingZoneDAO {
    Optional<ShippingZoneDTO> findByName(String name) throws SQLException;

    List<ShippingZoneDTO> findAll() throws SQLException;

    ShippingZoneDTO save(ShippingZoneDTO item) throws SQLException;

    boolean deleteByName(String name) throws SQLException;
}