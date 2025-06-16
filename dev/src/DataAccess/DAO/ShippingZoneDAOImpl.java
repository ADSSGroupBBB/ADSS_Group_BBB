package DataAccess.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import DataAccess.Interface.ShippingZoneDAO;
import DTO.ShippingZoneDTO;
import util.Database_HR_DL;

public class ShippingZoneDAOImpl implements ShippingZoneDAO {

    @Override
    public Optional<ShippingZoneDTO> findByName(String name) throws SQLException {
        String sql = "SELECT name, rank FROM shipping_zones WHERE name = ?";
        try (PreparedStatement ps = Database_HR_DL.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ShippingZoneDTO zone = new ShippingZoneDTO(
                            rs.getString("name"),
                            rs.getInt("rank")
                    );
                    return Optional.of(zone);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<ShippingZoneDTO> findAll() throws SQLException {
        String sql = "SELECT name, rank FROM shipping_zones ORDER BY name";
        List<ShippingZoneDTO> zones = new ArrayList<>();
        try (Statement st = Database_HR_DL.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                zones.add(new ShippingZoneDTO(
                        rs.getString("name"),
                        rs.getInt("rank")
                ));
            }
        }
        return zones;
    }

    @Override
    public ShippingZoneDTO save(ShippingZoneDTO zone) throws SQLException {
        // Check if the zone with the rank exists already
        Optional<ShippingZoneDTO> existing = findByName(zone.name());
        if (existing.isEmpty()) {
            // Insert new zone
            String sql = "INSERT INTO shipping_zones (name, rank) VALUES (?, ?)";
            try (PreparedStatement ps = Database_HR_DL.getConnection().prepareStatement(sql)) {
                ps.setString(1, zone.name());
                ps.setInt(2, zone.num());
                ps.executeUpdate();
                return zone;
            }
        } else {
            // Update existing zone
            String sql = "UPDATE shipping_zones SET rank = ? WHERE name = ?";
            try (PreparedStatement ps = Database_HR_DL.getConnection().prepareStatement(sql)) {
                ps.setString(1, zone.name());
                ps.setInt(2, zone.num());
                ps.executeUpdate();
                return zone;
            }
        }
    }

    public boolean deleteByName(String name) throws SQLException {
        String sql = "DELETE FROM shipping_zones WHERE name = ?";
        try (PreparedStatement ps = Database_HR_DL.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }
}