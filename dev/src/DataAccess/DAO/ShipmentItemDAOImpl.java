package DataAccess.DAO;

import DataAccess.Interface.ShipmentItemDAO;
import DTO.ShipmentItemDTO;
import util.Database_HR_DL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShipmentItemDAOImpl implements ShipmentItemDAO {

    @Override
    public Optional<ShipmentItemDTO> findByName(String name) throws SQLException {
        String sql = "SELECT name, weight FROM shipment_items WHERE name = ?";
        try (PreparedStatement ps = Database_HR_DL.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ShipmentItemDTO item = new ShipmentItemDTO(
                            rs.getString("name"),
                            rs.getInt("weight"),
                            1
                    );
                    return Optional.of(item);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<ShipmentItemDTO> findAll() throws SQLException {
        String sql = "SELECT name, weight FROM shipment_items ORDER BY name";
        List<ShipmentItemDTO> items = new ArrayList<>();
        try (Statement st = Database_HR_DL.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                items.add(new ShipmentItemDTO(
                        rs.getString("name"),
                        rs.getInt("weight"),
                        1
                ));
            }
        }
        return items;
    }
}