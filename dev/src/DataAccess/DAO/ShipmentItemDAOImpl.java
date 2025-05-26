package DataAccess.DAO;

import DTO.ShipmentItemDTO;
import DataAccess.Interface.ShipmentItemDAO;
import util.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShipmentItemDAOImpl implements ShipmentItemDAO {

    @Override
    public Optional<ShipmentItemDTO> findByName(String name) throws SQLException {
        String sql = "SELECT name, weight, amount FROM shipment_items WHERE name = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ShipmentItemDTO item = new ShipmentItemDTO(
                            rs.getString("name"),
                            rs.getInt("weight"),
                            rs.getInt("amount")
                    );
                    return Optional.of(item);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<ShipmentItemDTO> findAll() throws SQLException {
        String sql = "SELECT name, weight, amount FROM shipment_items ORDER BY name";
        List<ShipmentItemDTO> items = new ArrayList<>();
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                items.add(new ShipmentItemDTO(
                        rs.getString("name"),
                        rs.getInt("weight"),
                        rs.getInt("amount")
                ));
            }
        }
        return items;
    }

    @Override
    public ShipmentItemDTO save(ShipmentItemDTO item) throws SQLException {
        if (item.name() == null) {
            String sql = "INSERT INTO shipment_items (name, quantity) VALUES (?, ?)";
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(2, item.weight());
                ps.setInt(3, item.amount());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        return new ShipmentItemDTO(keys.getString(1), item.weight(), item.amount());
                    } else {
                        throw new SQLException("Creating item failed, no ID obtained.");
                    }
                }
            }
        } else {
            String sql = "UPDATE shipment_items SET weight = ?, amount = ? WHERE name = ?";
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
                ps.setString(1, item.name());
                ps.setInt(2, item.weight());
                ps.setInt(3, item.amount());
                ps.executeUpdate();
                return item;
            }
        }
    }
}