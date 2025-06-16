package src.DataAccess.EmployeeDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import src.DataAccess.EmployeeInterface.DriverDAO;
import src.util.Database;
import src.DTO.DriverDTO;

public class DriverDAOImpl implements DriverDAO {

    @Override
    public List<DriverDTO> findByDriverId(String driverId) throws SQLException {
        String sql = "SELECT id, license, on_drive FROM drivers WHERE id = ?";
        List<DriverDTO> drivers = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, driverId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    drivers.add(new DriverDTO(
                            rs.getString("id"),
                            rs.getInt("license"),
                            rs.getInt("on_drive")
                    ));
                }
            }
        }
        return drivers;
    }
    @Override
    public List<DriverDTO> findAll() throws SQLException {
        String sql = "SELECT id, license, on_drive FROM drivers ORDER BY id";
        List<DriverDTO> drivers = new ArrayList<>();
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                drivers.add(new DriverDTO(
                        rs.getString("id"),
                        rs.getInt("license"),
                        rs.getInt("on_drive")
                ));
            }
        }
        return drivers;
    }

    @Override
    public DriverDTO save(DriverDTO driver) throws SQLException {
        String sql = "INSERT OR REPLACE INTO drivers (id, license, on_drive) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, driver.id());
            ps.setInt(2, driver.license());
            ps.setInt(3, driver.on_drive());
            ps.executeUpdate();
        }
        return driver;
    }

    @Override
    public boolean deleteById(String driverId) throws SQLException {
        String sql = "DELETE FROM drivers WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, driverId);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    @Override
    public boolean deleteByIdAndLicense(String id, Integer license) throws SQLException {
        String sql = "DELETE FROM drivers WHERE id = ? AND license = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setInt(2, license);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    @Override
    public boolean hasLicense(String driverId, Integer license) throws SQLException {
        String sql = "SELECT COUNT(*) FROM drivers WHERE id = ? AND license = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, driverId);
            ps.setInt(2, license);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}