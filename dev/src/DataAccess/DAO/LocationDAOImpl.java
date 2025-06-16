package DataAccess.DAO;

import DataAccess.Interface.LocationDAO;
import DTO.LocationDTO;
import DTO.ShippingZoneDTO;
import util.Database_HR_DL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationDAOImpl implements LocationDAO {

    @Override
    public Optional<LocationDTO> findByAddress(String address) throws SQLException {
        String sql = "SELECT address, contact_name, contact_num, zone_name, zone_rank FROM locations WHERE address = ?";
        try (PreparedStatement ps = Database_HR_DL.getConnection().prepareStatement(sql)) {
            ps.setString(1, address);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ShippingZoneDTO zone = new ShippingZoneDTO(
                            rs.getString("zone_name"),
                            rs.getInt("zone_rank")
                    );
                    LocationDTO location = new LocationDTO(
                            rs.getString("address"),
                            rs.getString("contact_name"),
                            rs.getString("contact_num"),
                            zone
                    );
                    return Optional.of(location);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<LocationDTO> findAll() throws SQLException {
        String sql = "SELECT address, contact_name, contact_num, zone_name, zone_rank FROM locations ORDER BY address";
        List<LocationDTO> locations = new ArrayList<>();
        try (Statement st = Database_HR_DL.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ShippingZoneDTO zone = new ShippingZoneDTO(
                        rs.getString("zone_name"),
                        rs.getInt("zone_rank")
                );
                LocationDTO location = new LocationDTO(
                        rs.getString("address"),
                        rs.getString("contact_name"),
                        rs.getString("contact_num"),
                        zone
                );
                locations.add(location);
            }
        }
        return locations;
    }

    @Override
    public LocationDTO save(LocationDTO location) throws SQLException {
        String sql = "INSERT INTO locations (address, contact_name, contact_num, zone_name, zone_rank) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Database_HR_DL.getConnection().prepareStatement(sql)) {
            ps.setString(1, location.address());
            ps.setString(2, location.contact_name());
            ps.setString(3, location.contact_num());
            ps.setString(4, location.zone().name());
            ps.setInt(5, location.zone().num());
            ps.executeUpdate();
        }
        return location;
    }

    public boolean deleteByAddress(String address) throws SQLException {
        String sql = "DELETE FROM locations WHERE address = ?";
        try (PreparedStatement ps = Database_HR_DL.getConnection().prepareStatement(sql)) {
            ps.setString(1, address);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }
}